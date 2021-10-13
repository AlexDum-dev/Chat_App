/***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package stream;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientThread extends Thread {

	private Socket clientSocket;
    private List<Socket> SocketsList;
    public String id;
	private Map<String, Socket> dicSocket = new HashMap<>(); 

	ClientThread(Socket s , List<Socket> SocketsList,Map<String, Socket> dicSocket ) {
		this.clientSocket = s;
        this.SocketsList = SocketsList;
		this.dicSocket = dicSocket;
	}

    public void changeId(String id){
		this.id = id;
	}
 	/**
  	* receives a request from client then sends an echo to the client
  	* @param clientSocket the client socket
  	**/
	public void run() {
    	  try {
    	  	BufferedReader socIn = null;
    	  	socIn = new BufferedReader(
    	  			new InputStreamReader(clientSocket.getInputStream()));
    	  	PrintStream socOut = new PrintStream(clientSocket.getOutputStream());
    		/*ask to the client his id : could write his email. In the database we have a correspondance between the client and a unique id ?
			  Pour l'instant on demande aux clients de */
    		
			//Authentication process			
			BufferedReader usersInfo = new BufferedReader(new FileReader("../bdd/user/user.csv"));
			String userLine;
			String[] userInfos;
			Boolean authorizedAcess;
			Conversation conv;
    		while(true){
				System.out.println("Debug");
				authorizedAcess = false;
				String login = socIn.readLine();
    			String password = socIn.readLine();
				while ((userLine = usersInfo.readLine()) != null)
				{
					userInfos = userLine.split(",");
					if (userInfos[0].compareTo(login) == 0)
					{
						if (userInfos[1].compareTo(password) == 0)
						{
							authorizedAcess = true;
							this.id = userInfos[2];
							break;
						}
						else 
						{
							break;
						}
					}
				}
				if (authorizedAcess) {
					socOut.println("Access confirmed");
					SocketsList.add(clientSocket);
					System.out.println("Connexion from:" + clientSocket.getInetAddress());
					dicSocket.put(id, clientSocket);
					break;
				}
				else 
				{
					socOut.println("Access denied");
					usersInfo = new BufferedReader(new FileReader("../bdd/user/user.csv"));
				}				
			}

			while(true){
				String idDestinataire = socIn.readLine();
				//vérification que l'ID existe.
				//Test if destinataire is connected (so in the dictionnary) :
				Socket socketDestinataire = dicSocket.get(idDestinataire); 
				if(socketDestinataire != null){
					conv = new Conversation(id, idDestinataire, clientSocket,socketDestinataire);
					break;
				} else {
					//case where the user is either disconnected or doesn't exist at all
				}
				
			}

		  	while (true)
		  	{
		  		String line = socIn.readLine();
				PrintStream tmpSocOut = new PrintStream(conv.getSocketSecondClient().getOutputStream());
				tmpSocOut.println(id+": "+line);
		  		//socOut.println(line);
		  		//send to all other clients in the group the message delivered
				/*
				for(Socket tmp : SocketsList){
					if(tmp!=clientSocket){
						PrintStream tmpSocOut = new PrintStream(tmp.getOutputStream());
						tmpSocOut.println(line);
                	}
              	}
				*/
    		}
    	} catch (Exception e) {
        	System.err.println("Error in EchoServer:" + e);
        }
    }

}