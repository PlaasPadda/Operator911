package operator911.app;
import java.io.*;
import java.net.*;

public class ServerApp {

	public static void main(String[] args) throws IOException {

		// Maak n socket genaamd "serverSocker", assigned na port 5000
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Server listening..."); //System.out is terminal

        // Accept Client se connection
        Socket socket = serverSocket.accept();
        System.out.println("Client connected");

        // Maak n reader genaamd "in", assign dit aan die input wat Server kry van socket
        BufferedReader in = new BufferedReader(
            new InputStreamReader(socket.getInputStream())
        );
        
        // Maak n printer genaamd "out", assign dit aan die output wat Server stuur deur socket
        PrintWriter out = new PrintWriter(
            socket.getOutputStream(), true
        );

        // Reader thread 
        // Maak n loop op n seperate thread wat kyk of daar n message kom deur socket. As ja, print dit
        new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    System.out.println("Client: " + msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // Writer loop
        // Maak n nuwe reader genaamd console, assign dit aan terminal
        BufferedReader console = new BufferedReader(
            new InputStreamReader(System.in)
        );

        // As iets in terminal getik word, stuur dit deur socket
        String input;
        while ((input = console.readLine()) != null) {
            out.println(input);
        }

        socket.close();
        serverSocket.close();
	}

}
