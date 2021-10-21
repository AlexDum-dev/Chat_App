package Model;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Data {

    private List<User> users = new ArrayList<>();    
    private List<InfoConversation> infosConversations = new ArrayList<>();

    public Data() throws IOException{
        initUsers();
        initInfosConversation();
    }

    /**
     * Read in the csv database, create the user and fill in the list users
     * @throws IOException
     */
    private void initUsers() throws IOException{
        BufferedReader usersInfo = new BufferedReader(new FileReader("../bdd/user/user.csv"));

        String userLine;
		String[] userInfos;
        
        while ((userLine = usersInfo.readLine()) != null){
            userInfos = userLine.split(",");            
            User user = new User(userInfos[0], userInfos[1], userInfos[2]);
            addUserToUsersList(user);
        }
        usersInfo.close();

    }

    public void initInfosConversation() throws IOException {
        this.infosConversations.clear();
        BufferedReader conversationInfos = new BufferedReader(new FileReader("../bdd/conversations/index.csv"));
        String infoConversationLine;
        String[] infos;

        while ((infoConversationLine = conversationInfos.readLine()) != null)
        {
            infos = infoConversationLine.split(",");
            List<String> ids = new ArrayList<>();
            for (int i = 1; i < infos.length; i++)
            {
                ids.add(infos[i]);
            }
            InfoConversation infoLine = new InfoConversation(infos[0], ids);
            infosConversations.add(infoLine);
        }

        conversationInfos.close();
    }
    

    private void addUserToUsersList(User user){
        users.add(user);
    }

    public List<User> getUsers() {
        return users;
    }

    public List<InfoConversation> getInfosConversations()
    {
        return infosConversations;
    }

    public void changeUser(User u, int index){
        System.out.println("Length of data user before removing : "+this.getUsers().size());
        this.users.remove(index);
        System.out.println("Length of data user after removing : "+this.getUsers().size());
        this.users.add(u);
        System.out.println("Length of data user after adding : "+this.getUsers().size());
        if(this.users.get(3).getCurrentConversationGroupe() == null) System.out.println("[Data.changeUser : conv is null");
    }
}
