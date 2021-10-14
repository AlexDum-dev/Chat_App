package Service;
import Model.*;

import java.io.*;
import java.util.*;


public class Service {
    
    private Data data;

    public Service() throws IOException{
        this.data = new Data();
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
        BufferedReader messages = new BufferedReader(new FileReader("../bdd/conversations/" + filename));
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
        System.out.println("ParticipantIds : " + infosConversations.get(0).getParticipantIds().toString());
        for (InfoConversation tmp : infosConversations)
        {
            List<String> ids = tmp.getParticipantIds();
            System.out.println("ParticipantIds : " + ids.toString());
            if (ids.size() == participants.size())
            {
                System.out.println("Participant number is good");
                Boolean conversationExists = true;
                for (String id : participants)
                {
                    if (!ids.contains(id))
                    {
                        System.out.println("The id list of the conversation doesn't contain the id");
                        conversationExists = false;
                    }
                }
                if (conversationExists)
                {
                    System.out.println("Conversation exists");
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
}
