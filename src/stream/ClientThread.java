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

import Model.*;
import Service.Service;

public class ClientThread extends Thread {

	private Socket clientSocket;
	private List<Socket> SocketsList;
	public String id;
	private Map<String, Socket> dicSocket = new HashMap<>();
	private Service service;
	private User connectedClient = null;
	List<ConversationGroupe> listConv;

	ClientThread(Socket s, List<Socket> SocketsList, Map<String, Socket> dicSocket, List<ConversationGroupe> conversationGroupe) {
		this.clientSocket = s;
		this.SocketsList = SocketsList;
		this.dicSocket = dicSocket;
		this.listConv = conversationGroupe;
	}

	public void changeId(String id) {
		this.id = id;
	}

	/**
	 * receives a request from client then sends an echo to the client
	 * 
	 * @param clientSocket the client socket
	 **/
	public void run() {
		try {
			service = new Service();
		}
		catch (IOException ex){
			System.err.println("Error in ClientThread: " + ex.getMessage());
			System.exit(1);
		}
		try {
			BufferedReader socIn = null;
			socIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			PrintStream socOut = new PrintStream(clientSocket.getOutputStream());
			/*
			 * ask to the client his id : could write his email. In the database we have a
			 * correspondance between the client and a unique id ? Pour l'instant on demande
			 * aux clients de
			 */

			// Authentication process
			Boolean authorizedAcess;
			ConversationGroupe conv;
			String login,password;
			while (true) {
				login = socIn.readLine();
				password = socIn.readLine();
				authorizedAcess = false;
				connectedClient = service.authenticate(login, password);
				if (connectedClient != null) authorizedAcess = true;
				if (authorizedAcess) {
					id = connectedClient.getId();
					socOut.println("Access confirmed");
					SocketsList.add(clientSocket);
					System.out.println("Connexion from:" + clientSocket.getInetAddress());
					//dicSocket.put(id, clientSocket);
					break;
				} else {
					socOut.println("Access denied");
				}
			}

			while (true) {
				String[] idsDestinataires = socIn.readLine().split(",");
				List<String> idsUsersInGroup = new ArrayList<>();
				for(String s : idsDestinataires){
					idsUsersInGroup.add(s);
				}
				idsUsersInGroup.add(id);
				
				if(isInputCorrect(idsDestinataires)){
					socOut.println("Input is good");
					dicSocket.put(id, clientSocket);
					Map<String, Socket> listParticipants = new HashMap<>();
				
					for(String idDestinataire : idsDestinataires){
						Socket SocketParticipant = isConnected(idDestinataire);
						if(SocketParticipant != null){
							listParticipants.put(idDestinataire, SocketParticipant);
						}
					}


					//Faire aussi l'appel au service loadMessages
					String filename = null;
					List<String> history = service.loadMessages(id, Arrays.asList(idsDestinataires),filename);
					if(history != null){
						displayMessages(history, socOut);
					}

					try {

						//!!!!\ ajouter la ligne au fichier index.csv
						PrintStream file;
						if (filename != null)
						{
							file = new PrintStream("../bdd/conversations/"+filename);
						}
						else {
							UUID uuid = UUID.randomUUID();
							File newFile = new File("../bdd/conversations/"+uuid.toString() + ".txt");
							newFile.createNewFile();
							file = new PrintStream("../bdd/conversations/"+newFile.getName());
						}
						
						conv = findConversationGroup(idsUsersInGroup);
						if (conv == null)
						{
							conv = new ConversationGroupe(listParticipants,file);
						}
							
						connectedClient.setConversationGroupe(conv);
						
						break;
					}
					catch (Exception e)
					{
						System.out.println("Message de l'exception : " + e.getMessage());
					}
					
				}
				
				socOut.println("Non valid user");
				
			}

			while (true) {

				//list des ids dans laquelle je suis correspond à la liste des currentsGroupe de chaque destinataire
				//si une liste d'un destinataire diffère de ma liste alors j'envoie rien à personne par contre j'écris dans 
				//historique
				String line = socIn.readLine();
				for(Socket SocketDest : conv.getRecipients(id)){
					PrintStream SocOutOtherClient = new PrintStream(SocketDest.getOutputStream());
					SocOutOtherClient.println("[" + connectedClient.getPseudo() + "]: " + line);
				}

				//ecrire dans le fichier d'historique le message envoyé



			}
		} catch (Exception e) {
			System.err.println("Error in EchoServer:" + e);
		}
	}

	public Socket isConnected(String idDestinataire){
		Socket socketDestinataire = dicSocket.get(idDestinataire);
		
		return socketDestinataire == null ? null : socketDestinataire;
	}

	public boolean isInputCorrect(String[] ids){

		if (checkDoublon(ids) && service.checkIfIdsExist(ids)) return true;
		
		return false;
	}

	public boolean checkDoublon(String[] ids){

		Set<String> setIds = Set.of(ids);
		if(setIds.size() != ids.length) return false;

		return true;
	}

	public void displayMessages(List<String> listMessages, PrintStream socOut){
		for(String msg : listMessages){
			socOut.println(msg);
		}
	}

	public ConversationGroupe findConversationGroup(List<String> listIds){
		for(ConversationGroupe convG : listConv){
			var idSet = convG.getDicParticipantsInConversation().keySet();

			Set<String> setListIds = new HashSet<>(listIds);

			if(idSet.equals(setListIds)) return convG;
		}
		return null;
		
	}

}