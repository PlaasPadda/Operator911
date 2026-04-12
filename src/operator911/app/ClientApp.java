package operator911.app;

import java.io.*;
import java.net.*;

public class ClientApp {

	public static void main(String[] args) throws IOException {

		// Connect aan die socket wat Server gemaak het (socket 5000)
		Socket socket = new Socket("localhost", 5000);

        // Maak n reader genaamd "in", assign dit aan die input wat Client kry van socket
        BufferedReader in = new BufferedReader(
            new InputStreamReader(socket.getInputStream())
        );

        // Maak n printer genaamd "out", assign dit aan die output wat Client stuur deur socket
        PrintWriter out = new PrintWriter(
            socket.getOutputStream(), true
        );

        
        // Reader thread
        // Maak n loop op n seperate thread wat kyk of daar n message kom deur socket. As ja, print dit
        new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    System.out.println("Server: " + msg);
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
	}

}
