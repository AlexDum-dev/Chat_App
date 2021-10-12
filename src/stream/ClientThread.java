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

public class ClientThread
	extends Thread {
	
	private Socket clientSocket;
    private List<Socket> SocketsList;
	
	ClientThread(Socket s , List<Socket> SocketsList) {
		this.clientSocket = s;
        this.SocketsList = SocketsList;
	}

    public void updateSocketList(List<Socket> SocketsList){
        this.SocketsList=SocketsList;
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
    		while (true) {
    		  String line = socIn.readLine();
    		  socOut.println(line);
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