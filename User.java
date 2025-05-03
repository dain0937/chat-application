public class User{
    private String username;
    private String password;
    private Room currentRoom;
    public User( String name, String passkey) {
        username = name;
        password = passkey;
    }
    public String getUsername(){
        return username;
    }
    public void setUsername(String name){
        username = name;
    }
    public void setPassword(String passkey){
        password = passkey;
    }
    public String getPassword() {
        return this.password;
    }
    public Room getRoom() {
        return currentRoom;
    }
    public Room setRoom(Room newRoom){
        currentRoom = newRoom;
        return currentRoom;
    }
}