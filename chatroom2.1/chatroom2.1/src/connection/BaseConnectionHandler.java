package connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BaseConnectionHandler implements Runnable {

    protected Socket client;
    protected BufferedReader in;
    protected PrintWriter out;

    public BaseConnectionHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
            handleClient();
        }  catch (IOException e) {
            shutdown();
        }
    }

    protected void handleClient() throws IOException {
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void shutdown() {
        try {
            in.close();
            out.close();
            if (!client.isClosed()) {
                client.close();
            }
        } catch (IOException e) {
            // ignore
        }
    }
}
