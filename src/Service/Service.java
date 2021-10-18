package Service;
import Model.*;

import java.io.*;
import java.util.*;


public class Service {
    
    private Data data;

    public Service() throws IOException{
        this.data = new Data();
    }


    public Data getData() {
        return data;
    }


    public void setData(Data data) {
        this.data = data;
    }


    /**
     * Get the id of the person given its pseudo
     * @param : Strng pseudo de l'utilisateur
     * @return : String id of the user
     */
    public String getIdGivenPseudo(String pseudo){
        String id = null;

        for(User u : data.getUsers()){
            if(u.getPseudo().compareTo(pseudo) == 0){
                id = u.getId();
                return id;
            }
        }
        return id;
    }

    public User getUserById(String id){
        List<User> listUsers = data.getUsers();
        for(User u : listUsers){
            if(id.compareTo(u.getId()) == 0) {
                if(u.getCurrentConversationGroupe() == null) System.out.println("[Service.getUserById] : curentConv null");
                return u;
            }
        }

        return null;

    }

    public void updateUserIntheDatabase(User user){
        List<User> listUsers = data.getUsers();
        for(int i=0;i<listUsers.size();i++){
            if(user.getId().compareTo(listUsers.get(i).getId()) == 0){
                data.changeUser(user, i);
                return;
            }
        }

    }

    /**
     * 
     * @param pseudo
     * @param password
     * @return
     */
    public User authenticate(String pseudo, String password){
        User user = null;
        List<User> users = data.getUsers();
        for(User u : users){
            if(u.getPseudo().compareTo(pseudo) == 0 && u.getPassword().compareTo(password) == 0){
                return u;
            }  
        }
        return user;
    }

    public List<String> getMessages(String filename) throws IOException
    {
        BufferedReader messages;
        try{
            messages = new BufferedReader(new FileReader("../bdd/conversations/" + filename));
        } catch(IOException e){
            System.out.println("[Service.getConversationFilename] can't read " + filename);
            return null;
        }
        
        
        String messageLine;
        List<String> result = new ArrayList<>();        
        while ((messageLine = messages.readLine()) != null){
            result.add(messageLine);
        }
        messages.close();
        return result;
        
    }

    public List<String> loadMessages(String idClient, List<String> participantIds)
    {
        List<InfoConversation> infosConversations = data.getInfosConversations();
        List<String> participants = new ArrayList<>();
        participants.add(idClient);
        for (String tmp : participantIds)
        {
            participants.add(tmp);
        }
        //participantIds.add(idClient);
        //Check if a conversation exists
        for (InfoConversation tmp : infosConversations)
        {
            List<String> ids = tmp.getParticipantIds();
            if (ids.size() == participants.size())
            {
                Boolean conversationExists = true;
                for (String id : participants)
                {
                    if (!ids.contains(id))
                    {
                        conversationExists = false;
                    }
                }
                if (conversationExists)
                {
                    try {
                        return getMessages(tmp.getFilename());
                    }
                    catch (IOException ex) {
                        System.err.println(ex.getMessage());
                    }
                    
                }
            }
        }
        return null;

    }

    public boolean checkIfIdsExist(String[] ids){
        List<User> listUser = data.getUsers();
        
        for(String id : ids){
            Boolean userExists = false;
            for(User u : listUser){
                if(u.getId().compareTo(id) == 0) userExists = true;
            }
            if (!userExists)
            {
                return false;
            }
        }
        return true;        
    }


    public String getConversationFilename(Object[] participantIds) throws IOException
    {
        BufferedReader index;
        String[] participantsIds = new String[participantIds.length];
        for (int i = 0; i < participantIds.length; i++)
        {
            participantsIds[i] = String.valueOf(participantIds[i]);
        }

        if(participantsIds == null) System.out.println("PARTICIPANT IDS NULL");
        System.out.println("[Service.getConversationFilename] participantsIds : " + participantsIds[0] +" "+participantsIds[1]);
        try {
            index = new BufferedReader(new FileReader("../bdd/conversations/index.csv"));
        }
        catch (IOException ex)
        {
            System.out.println("[Service.getConversationFilename] can't read index.csv");
            return null;
        }  
        
        String conversationEntry;

        while ((conversationEntry = index.readLine()) != null)
        {
            String[] line = conversationEntry.split(",");
            System.out.println("[Service.getConversationFilename] conversationEntry = " + conversationEntry);
            Boolean conversationExists = true;
            if (line.length != participantsIds.length + 1) conversationExists = false;
            for (String id : participantsIds)
            {
                if (!Arrays.asList(line).contains(id))
                {
                    conversationExists = false;
                }
            }
            if (conversationExists)
            {
                System.out.println("[Service.getConversationFilename] The conversation has been found. return value is : " + line[0]);
                index.close();
                return line[0];
            }
        }

        System.out.println("[Service.getConversationFilename] The entire file has been read, no conversation exists in index.csv");
        
        index.close();
        return null;

    }

    public void persistMessage(String message, String filename) throws IOException
    {
        System.out.println("[Service.persistMessage] message : " + message);
        FileWriter historyFile = new FileWriter("../bdd/conversations/" + filename,true);        
        historyFile.write(message + "\r\n");
        historyFile.close();
    }


}
