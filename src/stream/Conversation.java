package stream;

import java.net.Socket;

public class Conversation {

    private String idFirstClient;
    private String idSecondClient;
    private Socket socketFirstClient;
    private Socket socketSecondClient;


    Conversation(String idFirstClient, String idSecondClient, Socket socketFirstClient,
    Socket socketSecondClient){
        this.idFirstClient = idFirstClient;
        this.idSecondClient = idSecondClient;
        this.socketFirstClient = socketFirstClient;
        this.socketSecondClient = socketSecondClient;

    }


    public String getIdFirstClient() {
        return idFirstClient;
    }


    public void setIdFirstClient(String idFirstClient) {
        this.idFirstClient = idFirstClient;
    }


    public String getIdSecondClient() {
        return idSecondClient;
    }


    public void setIdSecondClient(String idSecondClient) {
        this.idSecondClient = idSecondClient;
    }


    public Socket getSocketFirstClient() {
        return socketFirstClient;
    }


    public void setSocketFirstClient(Socket socketFirstClient) {
        this.socketFirstClient = socketFirstClient;
    }


    public Socket getSocketSecondClient() {
        return socketSecondClient;
    }


    public void setSocketSecondClient(Socket socketSecondClient) {
        this.socketSecondClient = socketSecondClient;
    }

    /*
    * Persist the messages between the two participants.
    */
    public void persistConversation(){

    }


    /**
     * Add the message to the conversation
     * @param : String msg (could be an object)
     */
     
    public void addMessage(String msg){

    }

    

}

