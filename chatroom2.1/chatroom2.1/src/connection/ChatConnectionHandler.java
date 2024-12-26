package connection;

import Server.ChatServer;
import java.io.IOException;
import java.net.Socket;

public class ChatConnectionHandler extends BaseConnectionHandler {

    private String username;
    private ChatServer server;

    public ChatConnectionHandler(Socket client, ChatServer server) {
        super(client);
        this.server = server;
    }

    @Override
    protected void handleClient() throws IOException {
        out.println("Enter your username: ");
        username = in.readLine();
        System.out.println(username + " connected");
        server.broadcast(username + " joined the room!");

        String message;
        while ((message = in.readLine()) != null) {
            if (message.startsWith("/user")) {
                String[] messageSplit = message.split(" ", 2);
                if (messageSplit.length == 2) {
                    server.broadcast(username + " changed their username to: " + messageSplit[1]);
                    username = messageSplit[1];
                    out.println("Successfully changed username to " + username);
                } else {
                    out.println("No username was provided");
                }
            } else if (message.startsWith("/quit")) {
                server.broadcast(username + " has left the room");
                shutdown();
            } else {
                server.broadcast(username + " : " + message);
            }
        }
    }

    @Override
    public void shutdown() {
        super.shutdown();
        server.broadcast(username + " has left the chat.");
    }
}
