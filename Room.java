import java.util.ArrayList;

class Room
{
    private String name;
    private int lastID;
    private ArrayList<User> users;
    private ArrayList<String> messages;

    public Room(String name, ArrayList<User> users, ArrayList<String> messages, int lastID){
        this.name = name;
        this.users = users;
        this.messages = messages;
        this.lastID = lastID;
    }

    public String currUsers() {
        if(users.size() == 0)
            return "No active users.";

        String result = users.get(0).getUsername();
        for(int i = 1; i < users.size(); i++) {
            result += ", " + users.get(i).getUsername();
        }
        return result;
    }

    public void chatHistory() {
        if(messages.size() == 0)
            System.out.println("No chat history.");

        for(int i = 0; i < messages.size(); i++) {
            System.out.println(messages.get(i));
        }
    }

    public void addUser(User x){
        users.add(x);
    }

    public void removeUser(User y){
        users.remove(y);
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    public String getName() {
        return name;
    }

    public void setUserList(ArrayList<User> users) {
        this.users = users;
    }

    public void setChatHistory(ArrayList<String> messages) {
        this.messages = messages;
    }

    public void setLastID(int lastID) {
        this.lastID = lastID;
    }

    public int getLastID() {
        return lastID;
    }
}