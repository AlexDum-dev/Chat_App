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
	private String historyFilename = null;
	private List<String> idsDest;

	ClientThread(Socket s, List<Socket> SocketsList, Map<String, Socket> dicSocket, List<ConversationGroupe> conversationGroupe, Service service) {
		this.clientSocket = s;
		this.SocketsList = SocketsList;
		this.dicSocket = dicSocket;
		this.listConv = conversationGroupe;
		this.service = service;
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
		ConversationGroupe conv;
		
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
			//ConversationGroupe conv;
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
					dicSocket.put(id, clientSocket);
					break;
				} else {
					socOut.println("Access denied");
				}
			}

			while (true) {
				String[] pseudosDestinataire = socIn.readLine().split(",");
				String[] idsDestinataires = getAllIds(pseudosDestinataire);
				//String[] idsDestinataires = socIn.readLine().split(",");
				List<String> idsUsersInGroup = new ArrayList<>();
				for(String s : idsDestinataires){
					idsUsersInGroup.add(s);
				}
				idsUsersInGroup.add(id);
				this.idsDest = idsUsersInGroup;
				
				if(isInputCorrect(idsDestinataires)){
					socOut.println("Input is good");
					//dicSocket.put(id, clientSocket);
					Map<String, Socket> listParticipants = new HashMap<>();
				
					for(String idDestinataire : idsDestinataires){
						Socket SocketParticipant = isConnected(idDestinataire);
						/*
						if(SocketParticipant != null){
							listParticipants.put(idDestinataire, SocketParticipant);
						}
						*/
						listParticipants.put(idDestinataire, SocketParticipant);
					}
					listParticipants.put(id, clientSocket); //put the actual client

					String filename = service.getConversationFilename(listParticipants.keySet().toArray());
					List<String> history = service.loadMessages(id, Arrays.asList(idsDestinataires));
					if(history != null){
						displayMessages(history, socOut);
					}
					historyFilename = filename;

					try {
						if (filename == null) 
						{
							UUID uuid = UUID.randomUUID();
							File newFile = new File("../bdd/conversations/"+uuid.toString() + ".txt");
							newFile.createNewFile();
							historyFilename = newFile.getName();
							//Add entry in index.csv
							FileWriter index = new FileWriter("../bdd/conversations/index.csv",true);
							String indexEntry = newFile.getName() + "," + id;
							for (String idString : idsDestinataires)
							{
								indexEntry = indexEntry + "," + idString;
							}
							indexEntry = indexEntry + "\r\n";
							index.write(indexEntry);
							index.close();
						}
						
						conv = findConversationGroup(idsUsersInGroup);
						if (conv == null)
						{
							System.out.println("Conversation not found");
							conv = new ConversationGroupe(listParticipants);
						} else {
							conv.addSocketToParticipant(id,clientSocket);
						}
						listConv.add(conv);	
						connectedClient.setConversationGroupe(conv);
						service.updateUserIntheDatabase(connectedClient);				
						//service.getUserById(id).setConversationGroupe(conv);
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
				for (User u : service.getData().getUsers())
				{
					try {
						if (u.getCurrentConversationGroupe() != null)
						{
							System.out.println("[ClientThread.l169] : " + u.getId() + " has a currentConversationGroup");
						}
					}
					catch (Exception e)
					{
						System.out.println("[ClientThread.l169] : " + u.getId() + " has no currentConversationGroup");
					}
				}



				System.out.println("Id du user actuel : "+id);
				
				//list des ids dans laquelle je suis correspond à la liste des currentsGroupe de chaque destinataire
				//si une liste d'un destinataire diffère de ma liste alors j'envoie rien à personne par contre j'écris dans 
				//historique

				//get all keys de chaque participant à ma Conversation. Si différent pour un j'envoie pas mais j écris dans le fichier
				String line = socIn.readLine();
				if(line != null && line.compareTo("-q")!=0){
					line = "[" + connectedClient.getPseudo() + "]: " + line;
					System.out.println("This message is about to get sent : " + line);
					Map<String,Socket> getMapDest = conv.getRecipients(id);
					String dest = new String();
					for (String tmp : getMapDest.keySet())
					{
						dest = dest + tmp + ",";
					}
					System.out.println("These are the destinataires : " + dest);
					for(String unIdDestinataire : this.idsDest){
						Socket s = getMapDest.get(unIdDestinataire);
						//if(isInMyCurrentGroup(id, service.getUserById(pair).getCurrentConversationGroupe().getDicParticipantsInConversation().keySet() , conv.getDicParticipantsInConversation().keySet()) == false) System.out.println("isInGroup is null");
						if(unIdDestinataire != this.id && s != null && isInMyCurrentGroup(id,unIdDestinataire, conv.getDicParticipantsInConversation().keySet())){
							PrintStream SocOutOtherClient = new PrintStream(getMapDest.get(unIdDestinataire).getOutputStream());
							SocOutOtherClient.println(line);							
						}
						else if (s == null)
						{
							System.out.println("Erreur : s est null");
						}
					}

					//ecrire dans le bon fichier d'historique le message envoyé
					service.persistMessage(line, historyFilename);
				}
				else {
					//handle disconnection
					disconnect();
					System.out.println(id+" want to disconnect");
					try{
						clientSocket.close();
					} catch(Exception e){
						System.err.println(e);
					}
					SocketsList.remove(clientSocket);
					dicSocket.remove(id);
					System.out.println("Thread has been closed for : "+id);
					return;
				}

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
		
		Set<String> setListIds = new HashSet<>(listIds);		
		for(ConversationGroupe convG : listConv){
			var idSet = convG.getDicParticipantsInConversation().keySet();

			if(idSet.equals(setListIds)) return convG;
		}
		return null;
		
	}

	public String[] getAllIds(String[] pseudos){
		String[] idsDestinataires = new String[pseudos.length];
		int i = 0;
		for(String pseudo : pseudos){
			idsDestinataires[i] = service.getIdGivenPseudo(pseudo);
			i++;
		}
		return idsDestinataires;
	}

	public boolean isInMyCurrentGroup(String myId, String unIdDestinataire,Set <String> setParticipantInConv){
		//System.out.println("[ClientThread.isInMyCurrenrGroup]"+setParticipantInConvDest.toString());
		//System.out.println("[ClientThread.isInMyCurrentGroup"+setParticipantInConv.toString());
		//User destinataireUser = service.getUserById(idDest);
		//System.out.println("Utilisateur destinataire : "+destinataireUser.getId()+ " "+destinataireUser.getPseudo());
		//if(setParticipantInConvDest.getCurrentConversationGroupe() == null) System.out.println("NUUUUL");
		//System.out.println("Dic du destinataire :"+destinataireUser.getCurrentConversationGroupe().getDicParticipantsInConversation().toString()); //we never set the convGroupe to the user
		//Set <String> setUserGroupDestinataire = destinataireUser.getCurrentConversationGroupe().getDicParticipantsInConversation().keySet(); //ERROR
		User destinataire = service.getUserById(unIdDestinataire);
		if(destinataire.getCurrentConversationGroupe() == null) return false;
		Set<String> setParticipantInConvDest = destinataire.getCurrentConversationGroupe().getDicParticipantsInConversation().keySet();
		if(setParticipantInConvDest.equals(setParticipantInConv)) return true;
	

		return false;
	}

	public void disconnect(){
		System.out.println(id+" want to disconnect");
		try{
			clientSocket.close();
		} catch(Exception e){
			System.err.println(e);
		}
			SocketsList.remove(clientSocket);
			dicSocket.remove(id);
			connectedClient.setConversationGroupe(null);
			service.updateUserIntheDatabase(connectedClient);
	}
}