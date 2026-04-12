package operator911.app;

import java.io.*;
import java.net.*;

public class ClientApp {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private MessageListener listener;

    public void connect(String host, int port, MessageListener listener) throws IOException {
        this.listener = listener;

		// Connect aan die socket wat Server gemaak het (socket 5000)
        socket = new Socket(host, port);

        // Maak n reader genaamd "in", assign dit aan die input wat Client kry van socket
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Maak n printer genaamd "out", assign dit aan die output wat Client stuur deur socket
        out = new PrintWriter(socket.getOutputStream(), true);

        startReaderThread();
    }

    private void startReaderThread() {

        // Maak n loop op n seperate thread wat kyk of daar n message kom deur socket. As ja, print dit
        new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    if (listener != null) {
                        listener.onMessageReceived(msg);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Stuur message deur socket
    public void sendMessage(String msg) {
        if (out != null) {
            out.println(msg);
        }
    }
}
