package M4;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;

import M4.TextFX.Color;

/**
 * Server-side handler for one connected client.
 * Each client gets its own thread so multiple clients can be served concurrently.
 */

    // Date: July 9th   |    UCID: am4239
    // Changes done to this file:
    //
    // Processed the /flip command sent from the client in the processCommand method. When the server receives a /flip command, 
    // it calls the handleCoinFlip method in the Server class to handle the request.
    //
    // Processed the /pm command sent from the client in the processCommand method. When the server receives a /pm command,
    // it extracts the targetID and message content from the commandData array and calls the handlePrivateMessage method in the Server 
    // class to send a private message to the specified user.
    //
    // Processed the /shuffle command sent from the client in the processCommand method. When the server receives a /shuffle command,
    // it extracts the text to shuffle from the commandData array and calls the handleShuffleText method in the Server class to shuffle the provided text.
    //

public class ServerThread extends Thread {
    private Socket client;
    private volatile boolean isRunning = false; // volatile so changes are visible across threads immediately
    private ObjectOutputStream out;             // field so sendToClient() can reach it after run() opens it
    private Server server;
    private long clientId;
    private Consumer<ServerThread> onInitializationComplete;

    private void info(String message) {
        System.out.println(String.format("Thread[%s]: %s", this.getClientId(), message));
    }

    public boolean isRunning() {
        return isRunning;
    }

    protected ServerThread(Socket myClient, Server server, Consumer<ServerThread> onInitializationComplete) {
        Objects.requireNonNull(myClient, "Client socket cannot be null");
        Objects.requireNonNull(server, "Server cannot be null");
        Objects.requireNonNull(onInitializationComplete, "Callback cannot be null");
        this.clientId = this.threadId(); // set before any logging so the id appears correctly
        this.client = myClient;
        this.server = server;
        this.onInitializationComplete = onInitializationComplete;
        info("ServerThread created");
    }

    public long getClientId() {
        return this.clientId;
    }

    /**
     * Signals this thread to stop. Sets isRunning to false and interrupts the blocking readObject() call
     * so the run() loop exits and the finally block handles cleanup.
     * Call from outside this thread only (e.g. from Server).
     */
    protected void disconnect() {
        if (!isRunning) {
            return;
        }
        info("Thread being disconnected by server");
        isRunning = false;
        this.interrupt(); // unblocks readObject() if the thread is waiting on it
    }

    /**
     * Sends a message to this client.
     *
     * @return true on success, false if the send failed (client likely disconnected)
     */
    protected boolean sendToClient(String message) {
        if (!isRunning) {
            return false;
        }
        try {
            out.writeObject(message);
            out.flush();
            return true;
        } catch (IOException e) {
            info("Error sending message to client (most likely disconnected)");
            return false;
        }
    }

    @Override
    public void run() {
        info("Thread starting");
        // ObjectOutputStream must be opened before ObjectInputStream on both sides.
        // Reversing the order causes a deadlock: both sides block waiting for the other's stream header.
        try (ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(client.getInputStream())) {
            this.out = out;
            isRunning = true;
            onInitializationComplete.accept(this); // notify Server this thread is ready
            while (isRunning) {
                try {
                    String fromClient = (String) in.readObject(); // blocks until client sends something
                    if (fromClient == null) {
                        throw new IOException("Connection interrupted");
                    } else {
                        info(TextFX.colorize("Received from client: " + fromClient, Color.CYAN));
                        processPayload(fromClient);
                    }
                } catch (ClassCastException | ClassNotFoundException cce) {
                    System.err.println("Error reading object as specified type: " + cce.getMessage());
                    cce.printStackTrace();
                } catch (IOException e) {
                    if (Thread.currentThread().isInterrupted()) {
                        info("Thread interrupted during read, disconnect() was called");
                        break;
                    }
                    info("IO exception while reading from client");
                    e.printStackTrace();
                    break;
                }
            }
        } catch (Exception e) {
            info("Unexpected exception outside read loop (stream setup may have failed)");
            e.printStackTrace();
            info("Client disconnected unexpectedly");
        } finally {
            isRunning = false;
            info("Exited thread loop. Cleaning up connection");
            cleanup();
        }
    }

    /**
     * Routes incoming text to a command handler or broadcasts it as a chat message.
     */
    private void processPayload(String incoming) {
        if (!processCommand(incoming)) {
            server.handleMessage(this, incoming);
        }
    }

    /**
     * Checks if the message is a command and handles it.
     * Commands start with COMMAND_TRIGGER and are comma-separated: trigger,name,data
     * Example: [cmd],reverse,hello world
     *
     * @return true if a command was handled, false if plain text
     */
    private boolean processCommand(String message) {
        if (!message.startsWith(Constants.COMMAND_TRIGGER)) {
            return false;
        }
        String[] commandData = message.split(",");
        if (commandData.length < 2) {
            return false;
        }
        final String command = commandData[1].trim();
        System.out.println(TextFX.colorize("Checking command: " + command, Color.YELLOW));
        switch (command) {
            case "disconnect":
                server.handleDisconnect(this);
                return true;
            case "users":
                server.handleGetUserList(this);
                return true;
            case "reverse":
                // join remaining segments back into the text to reverse
                String relevantText = String.join(" ", Arrays.copyOfRange(commandData, 2, commandData.length));
                server.handleReverseText(this, relevantText);
                return true;
            case "flip":
                server.handleCoinFlip(this);
                return true;
            case "pm":
                if (commandData.length >= 4) {
                    String targetIDStr = commandData[2].trim();
                    String pmText = String.join(" ", Arrays.copyOfRange(commandData, 3, commandData.length));
                    server.handlePrivateMessage(this, targetIDStr, pmText);

                } else {
                    this.sendToClient("Server: Invalid formating received.");
                }
                return true;
            case "shuffle":
                if (commandData.length >= 3) {
                    // Conviently recombines the segments in case the original text contained internal commas
                    String textToShuffle = String.join(",", Arrays.copyOfRange(commandData, 2, commandData.length));
                    server.handleShuffleText(this, textToShuffle);
                }
                return true;
            default:
                return false;
        }
    }

    private void cleanup() {
        info("ServerThread cleanup() start");
        try {
            client.close();
            info("Closed server-side socket");
        } catch (IOException e) {
            info("Client already closed");
        }
        info("ServerThread cleanup() end");
    }
}
