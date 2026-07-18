package M4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
    // Date: July 9th   |    UCID: am4239
    // Changes done to this file:
    //
    // Added handleCoinFlip() method which uses Math.random() for a 50/50 choice between Heads and Tails, 
    // the outcome message is formatted and sent to everyone from the server via broadcast() method.
    //
    // Added handlePrivateMessage() method which takes the sender, targetIdStr, and message as parameters.
    // It sends the PM to the target using the overloaded broadcast() method, and sends a confirmation back 
    // to the sender. This only happens if the target is connected, otherwise an error message is sent back to the sender.
    //
    // 

public class Server {
    private int port = 3000;
    // thread-safe map; multiple ServerThreads may call Server methods concurrently
    private final ConcurrentHashMap<Long, ServerThread> connectedClients = new ConcurrentHashMap<>();
    private boolean isRunning = true;

    private void start(int port) {
        this.port = port;
        System.out.println("Listening on port " + this.port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (isRunning) {
                System.out.println("Waiting for next client");
                Socket incomingClient = serverSocket.accept(); // blocks until a client connects
                System.out.println("Client connected");
                // third arg is a callback; ServerThread calls it once streams are open and it is ready
                ServerThread serverThread = new ServerThread(incomingClient, this, this::onServerThreadInitialized);
                serverThread.start();
                // not added to connectedClients here; that happens inside the callback after setup
            }
        } catch (IOException e) {
            System.err.println("Error accepting connection");
            e.printStackTrace();
        } finally {
            System.out.println("Server socket closed");
        }
    }

    /**
     * Callback from ServerThread once streams are open and it is ready to send/receive.
     * Registers the client and announces their arrival.
     */
    private synchronized void onServerThreadInitialized(ServerThread serverThread) {
        connectedClients.put(serverThread.getClientId(), serverThread);
        broadcast(null, String.format("*User[%s] connected*", serverThread.getClientId()));
    }

    /**
     * Internal disconnect: stops the thread, removes it from the map, and broadcasts a notice.
     * Used by handleDisconnect() and can be reused for server-side actions like kicks or timeouts.
     */
    private synchronized void disconnect(ServerThread serverThread) {
        serverThread.disconnect();
        connectedClients.remove(serverThread.getClientId());
        broadcast(null, String.format("User[%s] disconnected", serverThread.getClientId()));
    }

    /**
     * Sends a message to all connected clients.
     * Any client whose send fails is removed from the map.
     */
    private synchronized void broadcast(ServerThread sender, String message) {
        String senderLabel = sender == null ? "Server" : String.format("User[%s]", sender.getClientId());
        final String formatted = String.format("%s: %s", senderLabel, message);
        connectedClients.values().removeIf(serverThread -> !serverThread.sendToClient(formatted));
    }

    /** Overloaded broadcast method that only sends the message to the sender and the target recipient. */
    private synchronized void broadcast(ServerThread sender, ServerThread receiver, String message) {
        // Keeps the message "from the Server" as required
        final String formatted = String.format("Server: %s", message);
        
        // Sends the PM to the receiver
        if (receiver != null) {
            receiver.sendToClient(formatted);
        }
        
        // If PM succesfully reaches the target, a message is sent back to the sender
        if (sender != null) {
            sender.sendToClient(String.format("Server: (Sent to User[%d]): %s", receiver.getClientId(), message));
        }
    }

    // handle* methods are the interface ServerThread uses to trigger Server actions

    /**
     * Called when a client requests to disconnect. Delegates to disconnect().
     */
    protected synchronized void handleDisconnect(ServerThread sender) {
        disconnect(sender);
    }

    /**
     * Sends the connected user list only to the requesting client.
     * The requester's entry is tagged with "(you)".
     */
    protected synchronized void handleGetUserList(ServerThread serverThread) {
        StringBuilder sb = new StringBuilder("Connected users:\n");
        connectedClients.forEach((id, st) -> {
            if (id == serverThread.getClientId()) {
                sb.append(String.format("  User[%s] (you)\n", id));
            } else {
                sb.append(String.format("  User[%s]\n", id));
            }
        });
        serverThread.sendToClient(sb.toString().trim());
    }

    /** Reverses the text and broadcasts the result. */
    protected synchronized void handleReverseText(ServerThread sender, String text) {
        StringBuilder sb = new StringBuilder(text);
        sb.reverse();
        broadcast(sender, sb.toString());
    }

    /** Rolls a 50/50 coin flip and broadcasts the outcome to everyone. */
    protected synchronized void handleCoinFlip(ServerThread sender) {
        // Generates a 1 or 0 randomly
        String result = (Math.random() < 0.5) ? "Heads" : "Tails";
        
        // Formats the message exactly like so: <who> flipped a coin and got <result>
        String broadcastMessage = String.format("User[%s] flipped a coin and got %s", sender.getClientId(), result);
        
        // Passing null ensures the message comes from the Server
        broadcast(null, broadcastMessage);
    }

    /** Broadcasts a chat message from the sender to all clients. */
    protected synchronized void handleMessage(ServerThread sender, String text) {
        broadcast(sender, text);
    }

    /** Handles the private message request and forwards it to the overloaded broadcast method. */
    protected synchronized void handlePrivateMessage(ServerThread sender, String targetIdStr, String message) {
        try {
            long targetId = Long.parseLong(targetIdStr);
            ServerThread targetClient = connectedClients.get(targetId);

            if (targetClient != null) {
                String pmPayload = String.format("PM from User[%d]: %s", sender.getClientId(), message);
                
                // Passes the message to the overloaded broadcast() method
                broadcast(sender, targetClient, pmPayload);
            } else {
                sender.sendToClient(String.format("Server: Error - User with ID [%d] is not connected.", targetId));
            }
        } catch (NumberFormatException e) {
            sender.sendToClient("Server: Error - Target ID must be a numeric value.");
        }
    }

    /** Scrambles the characters of a message randomly and broadcasts the result to everyone */
    protected synchronized void handleShuffleText(ServerThread sender, String text) {
        // Converts the text into a list of individual characters
        java.util.List<Character> characters = new java.util.ArrayList<>();
        for (char c : text.toCharArray()) {
            characters.add(c);
        }
        
        // Uses the Collections shuffle method to scramble the list items into a completely random order
        java.util.Collections.shuffle(characters);
        
        // Rebuilds the scrambled characters back into a standard String layout
        StringBuilder shuffledSB = new StringBuilder();
        for (char c : characters) {
            shuffledSB.append(c);
        }
        
        // Formats the shuffled text accordingly
        String broadcastMessage = String.format("Shuffled from User[%d]: %s", sender.getClientId(), shuffledSB.toString());
        
        // Passes the now shuffled and formated message to the broadcast method; Passing null ensures the message comes from the Server
        broadcast(null, broadcastMessage);
    }

    public static void main(String[] args) {
        System.out.println("Server Starting");
        Server server = new Server();
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
