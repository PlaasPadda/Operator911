package operator911.app;
import java.io.*;
import java.net.*;
import com.google.gson.Gson;
import java.util.List;
import java.util.ArrayList;

public class ServerApp {
	
	private static float userX;
	private static float userY;

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
        // Maak n loop op n seperate thread wat kyk of daar n message kom deur socket. As ja, skryf dit na JSON file. 
        Gson gson = new Gson();
        
        new Thread(() -> {
            try {
                String received;
                // Maak die file oop om dit te append (true)
                FileWriter fileWriter = new FileWriter("requests.json", true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                while ((received = in.readLine()) != null) {
                    Request rqst = gson.fromJson(received, Request.class);

                    // Print to terminal as before
                    System.out.println("Type: " + rqst.type);
                    System.out.println("Services: " + rqst.services);
                    System.out.println("X Location: " + rqst.x);
                    System.out.println("Y Location: " + rqst.y);

                    // Write the JSON string to the file, one entry per line
                    if (rqst.type.equals("request")) {
                        bufferedWriter.write(received);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                        
                        // Update availability in the correct file
                        updateResourceAvailability(rqst.id, gson, out);
                    }
                    
                    if (rqst.type.equals("info")) {
                    	userX = rqst.x;
                    	userY = rqst.y;
                    	System.out.println("User:" + userX + userY);
                    	searchAndSendByType(rqst.services, out);
                    }
                }

                bufferedWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        
        // Writer loop
        // __Maak__ n __nuwe__ reader __genaamd__ console, assign __dit__ __aan__ terminal
        BufferedReader console = new BufferedReader(
            new InputStreamReader(System.in)
        );

        // As __iets__ in terminal __getik__ word, __stuur__ __dit__ __deur__ socket
        String input;
        while ((input = console.readLine()) != null) {
            out.println(input);
        }

        socket.close();
        serverSocket.close();
	}
	
	private static void searchAndSendByType(String targetType, PrintWriter out) {

	    	if (targetType.contains("F")) {
	    		System.out.println("It does contain F");
	    		readAndSendLine("F.json", out);
	    	}
	    	if (targetType.contains("H")) {
	    		System.out.println("It does contain H");
	    		readAndSendLine("H.json", out);
	    	}
	    	if (targetType.contains("P")) {
	    		System.out.println("It does contain P");
	    		readAndSendLine("P.json", out);
	    	}

	}
	
	private static void readAndSendLine(String filename, PrintWriter out) {
	    try {
			BufferedReader fileReader = new BufferedReader(new FileReader(filename));
			Gson gson = new Gson();
			String line;

			while ((line = fileReader.readLine()) != null) {
				out.println(line);
				System.out.println("Sent matching entry: " + line);
				}

			fileReader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void updateResourceAvailability(String id, Gson gson, PrintWriter out) {
	    if (id == null || id.isEmpty()) return;

	    // Determine which file to update based on first character of ID
	    char firstChar = id.charAt(0);
	    String filename;

	    if (firstChar == 'F') {
	        filename = "F.json";
	    } else if (firstChar == 'H') {
	        filename = "H.json";
	    } else if (firstChar == 'P') {
	        filename = "P.json";
	    } else {
	        System.out.println("Unknown resource type: " + id);
	        return;
	    }

	    try {
	        // Lees hele file 
	        BufferedReader fileReader = new BufferedReader(new FileReader(filename));
	        List<String> updatedLines = new ArrayList<>();
	        String line;

	        while ((line = fileReader.readLine()) != null) {
	            Resource rsrc = gson.fromJson(line, Resource.class);

	            // If this is the matching entry, set availability to 0
	            if (rsrc.id != null && rsrc.id.equals(id)) {
	                rsrc.available = false;
	                line = gson.toJson(rsrc);
	                System.out.println("Updated availability for: " + id);
	                
	                Resource confirm = new Resource("", "confirm", 0, 0, false);
					String json = gson.toJson(confirm);
					out.println(json);
	            }

	            updatedLines.add(line);
	        }

	        fileReader.close();

	        // ReWrite all lines back to the file (false die keer)
	        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filename, false));
	        for (String updatedLine : updatedLines) {
	            fileWriter.write(updatedLine);
	            fileWriter.newLine();
	        }
	        fileWriter.flush();
	        fileWriter.close();

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
		
	
};

