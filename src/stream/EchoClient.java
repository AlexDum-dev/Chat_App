/***
 * EchoClient
 * Example of a TCP client 
 * Date: 10/01/04
 * Authors:
 */
package stream;

import java.io.*;
import java.net.*;



public class EchoClient {

 
  /**
  *  main method
  *  accepts a connection, receives a message from client then sends an echo to the client
  **/
    public static void main(String[] args) throws IOException {

        Socket echoSocket = null;
        PrintStream socOut = null;
        BufferedReader stdIn = null;
        BufferedReader socIn = null;

        if (args.length != 2) {
          System.out.println("Usage: java EchoClient <EchoServer host> <EchoServer port>");
          System.exit(1);
        }

        try {
      	    // creation socket ==> connexion
      	    echoSocket = new Socket(args[0],new Integer(args[1]).intValue());
	        socIn = new BufferedReader(
	    		          new InputStreamReader(echoSocket.getInputStream()));    
	        socOut= new PrintStream(echoSocket.getOutputStream());
	        stdIn = new BufferedReader(new InputStreamReader(System.in));


            System.out.println("Welcome in this chat application !");
            System.out.println("Commands : "); 
            System.out.println("--connected    -> to print the pseudo of connected users");
            System.out.println("--change-group -> to change your current conversation group");
            System.out.println("--disconnect   -> to leave the application");
            System.out.println("***********************************************************");
            System.out.println("Please authenticate : ");
	        //Authentification :
            //Basic authentication
            while (true)
            {
                System.out.println("login: ");
                String login = stdIn.readLine();
                System.out.println("password: ");
                String password = stdIn.readLine();
                socOut.println(login);
                socOut.println(password);
                String response = socIn.readLine();
                if (response.compareTo("Access confirmed") == 0)
                {
                    System.out.println(response);
                    //ask to who he wants to talk : 
                    while (true){
                        /*
                        System.out.println("With which user(s) do you want to talk ?");
                        System.out.println("(Specify each person with comma separated please.)");
                        String pseudosDestinataires = stdIn.readLine();
                        //String idsDestinataires = stdIn.readLine();
                        socOut.println(pseudosDestinataires);
                        */
                        System.out.println(socIn.readLine());
                        System.out.println(socIn.readLine());
                        String pseudosDestinataires = stdIn.readLine();
                        socOut.println(pseudosDestinataires);
                        if (socIn.readLine().compareTo("Input is good") == 0)
                        {
                            break;
                        }
                    }
                    
                    break;
                }
                else
                {
                    System.out.println("Wrong login or password, try again");
                }
            }


        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:" + args[0]);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                               + "the connection to:"+ args[0]);
            System.exit(1);
        }
                             
        String line;
        ClientListener cl = new ClientListener (socIn);
        cl.start();
        while (true) {
        	line=stdIn.readLine();
        	if (line.equals("-q")) break;
        	socOut.println(line);
        }

      socOut.close();
      socIn.close();
      stdIn.close();
      echoSocket.close();
    }
}


