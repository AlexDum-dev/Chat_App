/***
 * EchoServer
 * Example of a TCP server
 * Date: 10/01/04
 * Authors:
 */

package stream;

import java.io.IOException;
import java.net.*;
import java.util.*;
import Model.ConversationGroupe;
import Service.Service;

public class EchoServerMultiThreaded  {
  
 	/**
  	* main method
	* @param EchoServer port
 	 * @throws IOException
  	* 
  	**/
    public static void main(String args[]) throws IOException{ 
        ServerSocket listenSocket;
        List<Socket> SocketsList = new ArrayList<Socket>();
        //List<ClientThread> ClientThreadList = new ArrayList<ClientThread>();
        Map<String, Socket> dicSocket = new HashMap<>(); 
        List<ConversationGroupe> conversationGroupes = new ArrayList<>();
        Service service = new Service();
        
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
                ClientThreadV2 ct = new ClientThreadV2(clientSocket , SocketsList, dicSocket, conversationGroupes,service);
                //ClientThreadList.add(ct);
                ct.start();
                /*
                for(ClientThread tmp : ClientThreadList){
                    tmp.updateSocketList(SocketsList); //update the socketList for all the thread so they have the same socketlist.
                }

                 */
            }
        } catch (Exception e) {
            System.err.println("Error in EchoServeServerMultiThreaded:" + e);
        }
       }

}
