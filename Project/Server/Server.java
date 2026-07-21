package Project.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import Project.Common.Move;
import Project.Common.TextFX;
import Project.Common.TextFX.Color;

// Date: July 20th   |    UCID: am4239
// Changes done to this file: 
//
// Added activeGames and clientToGameMap ConcurrentHashMaps to maintain player and game session relationships.
// Implemented handleRPSChallenge, handleRPSAccept, handleRPSMove, and handleRPSCancel to enforce game rules, unicast updates, and clean up active games.
// Updated disconnect cleanup to safely clear active games and notify remaining opponents if a participant disconnects.

public enum Server {
    INSTANCE; // Singleton instance

    private int port = 3000;
    private ServerSocket serverSocket = null; // kept as field so shutdown() can close it
    // thread-safe map; multiple ServerThreads may call Server methods concurrently
    private final ConcurrentHashMap<Long, ServerThread> connectedClients = new ConcurrentHashMap<>();
    // Game maps tracking active matches and player associations
    private final ConcurrentHashMap<Long, RPSGame> activeGames = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Long> clientToGameMap = new ConcurrentHashMap<>();
    // ArrayList is sufficient; all access flows through synchronized methods
    private final List<ServerThread> disconnectedBuffer = new ArrayList<>();
    private boolean isRunning = true;

    private long nextClientId = 1; // simple client ID generator

    private void info(String message) {
        System.out.println(TextFX.colorize(String.format("Server: %s", message), Color.YELLOW));
    }

    /**
     * Gracefully disconnect clients
     */
    private void shutdown() {
        try {
            isRunning = false; // stop the accept() loop in start()
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close(); // unblocks the blocking accept() call in start()
            }
            // ConcurrentHashMap.values().forEach() is thread-safe; no
            // ConcurrentModificationException risk.
            // By closing the serverSocket first, no new clients can be added to
            // connectedClients during this iteration.
            connectedClients.values().forEach(serverThread -> serverThread.disconnect());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Server() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            info("JVM is shutting down. Perform cleanup tasks.");
            shutdown();
        }));
    }

    private void start(int port) {
        this.port = port;
        info("Listening on port " + this.port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            this.serverSocket = serverSocket; // store reference so shutdown() can close it
            while (isRunning) {
                info("Waiting for next client");
                Socket incomingClient = serverSocket.accept(); // blocks until a client connects
                info("Client connected");
                // third arg is a callback; ServerThread calls it once streams are open and it
                // is ready
                ServerThread serverThread = new ServerThread(incomingClient, this::onServerThreadInitialized);
                serverThread.start();
                // not added to connectedClients here; that happens inside the callback after
                // setup
            }
        } catch (IOException e) {
            info("Error accepting connection");
            e.printStackTrace();
        } finally {
            info("Server socket closed");
        }
    }

    /**
     * Callback from ServerThread once streams are open and it is ready to
     * send/receive.
     * Registers the client and announces their arrival.
     */
    private synchronized void onServerThreadInitialized(ServerThread serverThread) {
        serverThread.setClientId(nextClientId++);// set then increase client id
        if (nextClientId < 0) { // handle overflow just in case
            nextClientId = 1;
        }
        serverThread.sendClientId(); // send the assigned client ID back to the client
        connectedClients.put(serverThread.getClientId(), serverThread);
        unicastClientStatus(serverThread); // send existing users to new client first
        // Note: broadcastClientStatus includes the new client itself (they receive their own SERVER_JOIN
        // and a SERVER_SYNC about themselves from unicast). This is intentional — filtering it out would
        // add complexity that isn't the focus of this lesson; the client handles it safely via putIfAbsent.
        broadcastClientStatus(serverThread, true, false); // then announce new client to everyone
    }

    /**
     * Internal disconnect: stops the thread, removes it from the map, and
     * broadcasts a notice.
     * Used by handleDisconnect() and can be reused for server-side actions like
     * kicks or timeouts.
     */
    private synchronized void disconnect(ServerThread serverThread) {
        if (!connectedClients.containsKey(serverThread.getClientId())) {
            // already removed (e.g. sendOrDisconnect pruned it during a broadcast)
            // — avoid double-broadcast of the leave status
            return;
        }

        long disconnectingId = serverThread.getClientId();
        Long gameId = clientToGameMap.remove(disconnectingId);
        if (gameId != null) {
            RPSGame game = activeGames.remove(gameId);
            if (game != null) {
                long opponentId = game.getOpponentId(disconnectingId);
                clientToGameMap.remove(opponentId);
                
                // Notifies the remaining opponent
                ServerThread opponent = connectedClients.get(opponentId);
                if (opponent != null) {
                    opponent.sendMessage("Server: Your opponent disconnected. Game cancelled.");
                }
            }
        }

        serverThread.sendDisconnectTrigger();
        System.out.println(TextFX.colorize("Client " + serverThread.getDisplayName() + " disconnected.", Color.RED));
        connectedClients.remove(serverThread.getClientId());
        broadcastClientStatus(serverThread, false, false);
    }

    // start region for handle*() methods ===================================
    // handle* methods are the interface ServerThread uses to trigger Server actions

    /**
     * Called when a client requests to disconnect. Delegates to disconnect().
     */
    protected synchronized void handleDisconnect(ServerThread sender) {
        disconnect(sender);
    }

    /** Reverses the text and broadcasts the result. */
    protected synchronized void handleReverseText(ServerThread sender, String text) {
        StringBuilder sb = new StringBuilder(text);
        sb.reverse();
        broadcast(sender, sb.toString());
    }

    /** Broadcasts a chat message from the sender to all clients. */
    protected synchronized void handleMessage(ServerThread sender, String text) {
        broadcast(sender, text);
    }

    /** Handles an RPS challenge request between a sender and a target player. */
    protected synchronized void handleRPSChallenge(ServerThread sender, long targetId) {
        long senderId = sender.getClientId();

        // Rejects if target isn't connected
        ServerThread target = connectedClients.get(targetId);
        if (target == null) {
            sender.sendMessage("Server: Target client ID #" + targetId + " is not connected.");
            return;
        }

        // Rejects if target is self
        if (targetId == senderId) {
            sender.sendMessage("Server: You cannot challenge yourself.");
            return;
        }

        // Rejects if either sender or target is already in a game
        if (clientToGameMap.containsKey(senderId)) {
            sender.sendMessage("Server: You are already in an active game.");
            return;
        }
        if (clientToGameMap.containsKey(targetId)) {
            sender.sendMessage("Server: Player " + target.getDisplayName() + " is already in another game.");
            return;
        }

        // Creates RPSGame, and is tracked in maps
        RPSGame game = new RPSGame(senderId, targetId);
        activeGames.put(game.getGameId(), game);
        clientToGameMap.put(senderId, game.getGameId());
        clientToGameMap.put(targetId, game.getGameId());

        // Unicast notifications to sender and target
        sender.sendMessage("Server: Challenge sent to " + target.getDisplayName() + ". Waiting for them to respond.");
        target.sendMessage("Server: You have been challenged to RPS by " + sender.getDisplayName() + 
                "! Type `/accept` or `/decline`.");
    }

    /** Handles acceptance or decline of an incoming RPS game challenge. */
    protected synchronized void handleRPSAccept(ServerThread sender, boolean accepted) {
        long senderId = sender.getClientId();

        // Rejects if sender has no entry in clientToGameMap
        Long gameId = clientToGameMap.get(senderId);
        if (gameId == null) {
            sender.sendMessage("Server: You have no pending challenge.");
            return;
        }

        // Looks up the gameID
        RPSGame game = activeGames.get(gameId);
        if (game == null) {
            // Cleans up entry if game doesn't exist
            clientToGameMap.remove(senderId);
            sender.sendMessage("Server: You have no pending challenge.");
            return;
        }

        long opponentId = game.getOpponentId(senderId);
        ServerThread opponent = connectedClients.get(opponentId);

        if (accepted) {
            // Unicasts "game on" notice to both players
            sender.sendMessage("Server: Challenge accepted! Use `/move <rock|paper|scissors>` to play.");
            if (opponent != null) {
                opponent.sendMessage("Server: " + sender.getDisplayName() + " accepted your challenge! Use `/move <rock|paper|scissors>` to play.");
            }
        } else {
            // If declined, unicasts a notice to both, then removes game and player mappings
            sender.sendMessage("Server: You declined the challenge.");
            if (opponent != null) {
                opponent.sendMessage("Server: " + sender.getDisplayName() + " declined your challenge.");
            }
            activeGames.remove(gameId);
            clientToGameMap.remove(senderId);
            clientToGameMap.remove(opponentId);
        }
    }

    /** Handles move submission and triggers round evaluation when both moves are present. */
    protected synchronized void handleRPSMove(ServerThread sender, Move move) {
        long senderId = sender.getClientId();

        // Rejects if sender has no entry in clientToGameMap
        Long gameId = clientToGameMap.get(senderId);
        if (gameId == null) {
            sender.sendMessage("Server: You are not in an active RPS game.");
            return;
        }

        // Looks up the gameID
        RPSGame game = activeGames.get(gameId);
        if (game == null) {
            clientToGameMap.remove(senderId);
            sender.sendMessage("Server: You are not in an active RPS game.");
            return;
        }

        // Calls updateMove; if false, assumes sender already moved this round
        boolean moveAccepted = game.updateMove(senderId, move);
        if (!moveAccepted) {
            sender.sendMessage("Server: You have already submitted your move for this round!");
            return;
        }

        sender.sendMessage("Server: You locked in your move (" + move + "). Waiting for opponent...");

        // If the game is now complete: evaluate, notify both participants, then cleanup
        if (game.isComplete()) {
            long playerAId = game.getPlayerAId();
            long playerBId = game.getPlayerBId();

            ServerThread playerA = connectedClients.get(playerAId);
            ServerThread playerB = connectedClients.get(playerBId);

            Move moveA = game.getOpponentMove(playerBId); // Player A's move
            Move moveB = game.getOpponentMove(playerAId); // Player B's move

            // Determines outcomes
            if (moveA == moveB) {
                if (playerA != null) playerA.sendMessage("Server: Match tied! Both players threw " + moveA + ".");
                if (playerB != null) playerB.sendMessage("Server: Match tied! Both players threw " + moveB + ".");
            } else if (game.playerWins(playerAId)) {
                if (playerA != null) playerA.sendMessage("Server: You WIN! Your " + moveA + " beat " + (playerB != null ? playerB.getDisplayName() : "Opponent") + "'s " + moveB + ".");
                if (playerB != null) playerB.sendMessage("Server: You LOSE! " + (playerA != null ? playerA.getDisplayName() : "Opponent") + "'s " + moveA + " beat your " + moveB + ".");
            } else {
                if (playerA != null) playerA.sendMessage("Server: You LOSE! " + (playerB != null ? playerB.getDisplayName() : "Opponent") + "'s " + moveB + " beat your " + moveA + ".");
                if (playerB != null) playerB.sendMessage("Server: You WIN! Your " + moveB + " beat " + (playerA != null ? playerA.getDisplayName() : "Opponent") + "'s " + moveA + ".");
            }

            // Removes game and map entries
            activeGames.remove(gameId);
            clientToGameMap.remove(playerAId);
            clientToGameMap.remove(playerBId);
        }
    }

    /** Handles a player's request to cancel an ongoing RPS match. */
    protected synchronized void handleRPSCancel(ServerThread sender) {
        long senderId = sender.getClientId();

        // Checks if sender is in a game
        Long gameId = clientToGameMap.get(senderId);
        if (gameId == null) {
            sender.sendMessage("Server: You are not in an active RPS game to cancel.");
            return;
        }

        // Looks up gameID
        RPSGame game = activeGames.get(gameId);
        if (game == null) {
            clientToGameMap.remove(senderId);
            sender.sendMessage("Server: You are not in an active RPS game to cancel.");
            return;
        }

        long opponentId = game.getOpponentId(senderId);
        ServerThread opponent = connectedClients.get(opponentId);

        // Rejects if target/opponent isn't in connectedClients
        if (opponent == null) {
            sender.sendMessage("Server: Opponent is no longer connected. Game cancelled.");
            activeGames.remove(gameId);
            clientToGameMap.remove(senderId);
            clientToGameMap.remove(opponentId);
            return;
        }

        // Removes game information from activeGames and clientToGameMap
        activeGames.remove(gameId);
        clientToGameMap.remove(senderId);
        clientToGameMap.remove(opponentId);

        // Sends notice to both players
        sender.sendMessage("Server: You cancelled the ongoing RPS match.");
        opponent.sendMessage("Server: " + sender.getDisplayName() + " cancelled the ongoing RPS match.");
    }
    // end region for handle*() methods ===================================

    // start region for data broadcast ==============================

    private void unicastClientStatus(ServerThread incomingServerThread) {
        // send existing clients' info to the newly connected client as a silent sync.

        // Uses a "for each" loop so it can early exit on failure
        // (e.g. if the new client's connection drops during the sync, we don't want to
        // keep trying to send the rest of the clients' info)
        for (ServerThread existingServerThread : connectedClients.values()) {
            boolean success = incomingServerThread.sendClientStatus(
                    existingServerThread.getClientId(),
                    existingServerThread.getClientName(),
                    true,
                    true);
            if (!success) {
                disconnect(incomingServerThread);
                break;
            }
        }
    }

    /**
     * Sends connected clients a status update about a client joining or leaving
     * 
     * @param targetServerThread the client whose status changed
     * @param isJoin             true if joining, false if leaving
     * @param isSync             true if this is a silent background sync, false for
     *                           a real-time join/leave event
     */
    private void broadcastClientStatus(ServerThread targetServerThread, boolean isJoin, boolean isSync) {
        sendOrDisconnect(serverThread -> serverThread.sendClientStatus(
                targetServerThread.getClientId(),
                targetServerThread.getClientName(),
                isJoin,
                isSync));

    }

    /**
     * Sends a message to all connected clients.
     * Any client whose send fails is removed from the map.
     */
    private synchronized void broadcast(ServerThread sender, String message) {
        String senderLabel = sender == null ? "Server" : String.format("User[%s]", sender.getDisplayName());
        final String formatted = String.format("%s: %s", senderLabel, message);
        sendOrDisconnect(serverThread -> serverThread.sendMessage(formatted));
    }
    // end region for data broadcast ==============================

    // utils
    /**
     * Applies sendAction to each connected client, buffering failures, then
     * processes the disconnected buffer. Reduces duplicated removeIf boilerplate.
     */
    private synchronized void sendOrDisconnect(Function<ServerThread, Boolean> sendAction) {
        connectedClients.values().removeIf(serverThread -> {
            boolean success = sendAction.apply(serverThread);
            if (!success) {
                System.out.println(TextFX.colorize("Failed to send message to client " + serverThread.getDisplayName()
                        + ". Removing from connected clients.", Color.RED));
                disconnectedBuffer.add(serverThread);
            }
            return !success;
        });
        processDisconnectedBuffer();
    }

    /**
     * Processes the disconnected buffer by broadcasting disconnects for each
     * buffered client, then clearing the buffer. Used to handle edge cases where a
     * client disconnects in the middle
     */
    private void processDisconnectedBuffer() {
        if (disconnectedBuffer.isEmpty())
            return;
        List<ServerThread> snapshot = new ArrayList<>(disconnectedBuffer);
        disconnectedBuffer.clear();
        snapshot.forEach(st -> broadcastClientStatus(st, false, false));
    }

    public static void main(String[] args) {
        System.out.println("Server Starting");
        Server server = Server.INSTANCE;
        int port = 3000;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception e) {
            // use default port
        }
        server.start(port);
        System.out.println("Server Stopped");
    }
}
