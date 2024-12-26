package Server;

import connection.BaseConnectionHandler;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BaseServer implements Runnable {

    protected ArrayList<BaseConnectionHandler> connections;
    protected ServerSocket server;
    protected boolean done;
    protected ExecutorService pool;

    public BaseServer(int port) {
        connections = new ArrayList<>();
        done = false;
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Could not start server on port " + port);
        }
    }

    public void run() {
        try {
            pool = Executors.newCachedThreadPool();
            while (!done) {
                Socket client = server.accept();
                BaseConnectionHandler handler = createConnectionHandler(client);
                connections.add(handler);
                pool.execute(handler);
            }
        } catch (IOException e) {
            shutdown();
        }
    }

    protected BaseConnectionHandler createConnectionHandler(Socket client)
    {
        return new BaseConnectionHandler(client);
    }

    public void broadcast(String message) {
        for (BaseConnectionHandler ch : connections) {
            if (ch != null) {
                ch.sendMessage(message);
            }
        }
    }

    public void shutdown() {
        try {
            done = true;
            pool.shutdown();
            if (!server.isClosed()) {
                server.close();
            }
            for (BaseConnectionHandler ch : connections) {
                ch.shutdown();
            }
        } catch (IOException e) {
            // ignore
        }
    }
}
