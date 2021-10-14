package Model;
import java.io.*;
import java.net.*;
import java.util.*;

public class ConversationGroupe {

    private Map<String, Socket> dicParticipantsInConversation = new HashMap<>();
    
    //on creation of the conversation she has to be persisted
    public ConversationGroupe(Map<String, Socket> dicParticipantsInConversation){
        this.dicParticipantsInConversation = dicParticipantsInConversation;
    }

    public Map<String, Socket> getDicParticipantsInConversation() {
        return dicParticipantsInConversation;
    }

    /**
     * Allows to get the socket to all the recipients of the message
     * it exclused the person that sent the message.
     * @return List<Socket> 
     * @param : id representing the person who send the message
     */
    public List<Socket> getRecipients(String id){
        List<Socket> listSocketRecipients = new ArrayList<>();

        for(var pair : dicParticipantsInConversation.entrySet()){
            if(id.compareTo(pair.getKey()) != 0){
                listSocketRecipients.add(pair.getValue());
            }
        }
        return listSocketRecipients;
    }
}
