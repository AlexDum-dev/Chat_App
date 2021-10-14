/***
 * EchoServer
 * Example of a TCP server
 * Date: 10/01/04
 * Authors:
 */

package stream;

import java.net.*;
import java.util.*;
public class EchoServerMultiThreaded  {
  
 	/**
  	* main method
	* @param EchoServer port
  	* 
  	**/
       public static void main(String args[]){ 
        ServerSocket listenSocket;
        List<Socket> SocketsList = new ArrayList<Socket>();
        //List<ClientThread> ClientThreadList = new ArrayList<ClientThread>();
        Map<String, Socket> dicSocket = new HashMap<>(); 
        
        if (args.length != 1) {
              System.out.println("Usage: java EchoServer <EchoServer port>");
              System.exit(1);
        }
        try {
            listenSocket = new ServerSocket(Integer.parseInt(args[0])); //port
            System.out.println("Server ready...");
            while (true) {
                Socket clientSocket = listenSocket.accept();
                SocketsList.add(clientSocket);
                //System.out.println("Connexion from:" + clientSocket.getInetAddress());
                ClientThread ct = new ClientThread(clientSocket , SocketsList,dicSocket);
                //ClientThreadList.add(ct);
                ct.start();
                /*
                for(ClientThread tmp : ClientThreadList){
                    tmp.updateSocketList(SocketsList); //update the socketList for all the thread so they have the same socketlist.
                }

                 */
            }
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
       }

}
