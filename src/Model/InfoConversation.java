package Model;

import java.util.*;

public class InfoConversation {
    private String filename;
    private List<String> idParticipantList;

    public InfoConversation(String filename, List<String> idParticipantList) {
        this.filename = filename;
        this.idParticipantList = idParticipantList;
    }

    public String getFilename(){
        return filename;
    }

    public List<String> getParticipantIds(){
        return idParticipantList;
    }

}