package Model;

public class User {
    private String pseudo;
    private String password;
    private String id;
    private ConversationGroupe currentConversationGroupe;

    public User(String pseudo, String password, String id){
        this.pseudo = pseudo;
        this.password = password;
        this.id = id;
    }


    public String getPseudo() {
        return pseudo;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }

    public ConversationGroupe getCurrentConversationGroupe()
    {
        return currentConversationGroupe;
    }

    public void setConversationGroupe(ConversationGroupe currentConversationGroupe)
    {
        this.currentConversationGroupe = currentConversationGroupe;
    }


}
