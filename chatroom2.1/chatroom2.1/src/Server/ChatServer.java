package Server;

import connection.ChatConnectionHandler;
import java.net.Socket;

public class ChatServer extends BaseServer {

    public ChatServer(int port) {
        super(port);
    }

    @Override
    protected ChatConnectionHandler createConnectionHandler(Socket client) {
        return new ChatConnectionHandler(client, this);
    }

    public static void main(String[] args) {
        ChatServer server = new ChatServer(9999);
        server.run();
    }
}
