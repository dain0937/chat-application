import java.util.ArrayList;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Database mainApp = new Database();
        Scanner scnr = new Scanner(System.in);
        User currUser = null;
        mainApp.setup();
        //credits
        welcome();
        //start program
        while (true) {
            promptBegin();
            String input = scnr.next().toLowerCase();
            if (input.equals("q") || input.equals("quit")) {
                System.out.println("Are you sure you want to quit?");
                String input2 = scnr.next().toLowerCase();
                if (input2.equals("q") || input2.equals("y") || input2.equals("quit") || input2.equals("yes")) {
                    System.out.println("Bye!");
                    mainApp.close();
                    break;
                } else {
                    System.out.println("Returning to the program...");
                }
            }
            else if (input.equals("r") || input.equals("register")) {
                register(mainApp);
            }
            else if (input.equals("l") || input.equals("login")) {
                currUser = promptLogin(mainApp);

            }
            else {
                System.out.println("Command not recognized, please try again: ");
            }
            if (currUser != null) {
                while (true) {
                    promptMain();
                    input = scnr.next().toLowerCase();
                    if (input.equals("l") || input.equals("logout")) {
                        System.out.println("Are you sure you want to logout?");
                        String input2 = scnr.next().toLowerCase();
                        if (input2.equals("l") || input2.equals("y") || input2.equals("logout") || input2.equals("yes")) {
                            System.out.println("Successfully logged out");
                            System.out.println("Returning to login page...");
                            break;
                        } else {
                            System.out.println("Returning to the program...");
                        }
                    }
                    else if (input.equals("j") || input.equals("join")) {
                        Room temp = promptJoin(mainApp, currUser);
                        if (temp != null) {
                            currUser.setRoom(temp);
                            promptRoom(currUser);
                            while (true) {
                                String input2 = scnr.nextLine();
                                checkMessage(mainApp, currUser, input2);
                                if (input2.equals("/leave")) {
                                    currUser.setRoom(null);
                                    mainApp.updateUserRoom(currUser.getUsername(), null);
                                    System.out.println("Bye!");
                                    break;
                                }
                            }
                        } else {
                            System.out.println("Returning to main menu...");
                        }
                    }
                    else if (input.equals("c") || input.equals("create")) {
                        promptCreate(mainApp, currUser);
                    }
                    else if (input.equals("a") || input.equals("account")) {
                        promptAccount(mainApp, currUser);
                    }
                    else {
                        System.out.println("Command not recognized, please try again: ");
                    }
                }
            }
        }
        //exit message
        System.out.println("bye");
        mainApp.close();
    }
    public static void welcome() {
        System.out.println("Welcome to our Chatting App! Created By: Mason Herwegh, Dain Lee, Matthew Shin, Jack White");
        System.out.println();
        divide();
    }
    public static void promptBegin() {
        System.out.println("Please select from the following options:");
        System.out.println("(R)egister, (L)ogin, (Q)uit");
    }
    public static void promptMain() {
        System.out.println("Please select from the following options:");
        System.out.println("(J)oin, (C)reate, (A)ccount, (L)ogout");
    }
    public static void promptRoom(User currUser) {
        if (currUser.getRoom() == null) {
            return;
        }
        System.out.println("Welcome to "+currUser.getRoom().getName()+", "+currUser.getUsername()+"!");
        System.out.println("Type /help for help: ");
    }
    public static void divide() {
        System.out.println("-----------------------------------------");
    }
    public static void register(Database chatApp) {
        System.out.println("Please register your username and password:");
        System.out.println("Wrong screen? Type /back to return to the login page:");
        System.out.println("Username: ");
        Scanner scnr = new Scanner(System.in);
        String username = scnr.next().toLowerCase();
        if (username.equals("/back")) {
            return;
        }
        if (chatApp.checkUserExists(username)) {
            System.out.println("Username is taken, please try a new username:");
            register(chatApp);
        } else {
            System.out.println("Password: ");
            String password = scnr.next();
            System.out.println("Verify your password: ");
            String password2 = scnr.next();
            if (password2.equals(password)) {
                chatApp.addUser(username, password);
                System.out.println("Username and Password successfully registered!");
            } else {
                System.out.println("The two passwords do not match, please try again: ");
                register(chatApp);
            }
        }
    }
    public static Room promptJoin(Database chatApp, User currUser) {
        Scanner scnr = new Scanner(System.in);
        System.out.println("Please enter the room name");
        System.out.println("Wrong screen? Type /back to return to the main page:");
        System.out.println("Room Name: ");
        String roomName = scnr.next().toLowerCase();
        if (roomName.equals("/back")) {
            return null;
        }
        return joinRoom(chatApp, roomName, currUser);
    }
    public static Room joinRoom(Database chatApp, String roomName, User currUser) {
        if (chatApp.checkRoomExists(roomName)) {
            System.out.println("Successfully joined room");
            chatApp.updateUserRoom(currUser.getUsername(), roomName);
            ArrayList<User> userList = chatApp.getUsers(roomName);
            ArrayList<String> chatHistory = chatApp.getRoomHistory(roomName);
            int lastID = chatApp.getLastID(roomName);
            return new Room(roomName, userList, chatHistory, lastID);
        } else {
            System.out.println("Room with that name not found, please try again");
            promptJoin(chatApp, currUser);
        }
        return null;
    }
    public static Room promptCreate(Database chatApp, User currUser) {
        System.out.println("Please type in the room name: ");
        System.out.println("Wrong screen? Type /back to return to the main page:");
        System.out.println("Room Name: ");
        Scanner scnr = new Scanner(System.in);
        String roomName = scnr.next().toLowerCase();
        if (roomName.equals("/back")) {
            return null;
        }
        if (chatApp.checkRoomExists(roomName)) {
            System.out.println("Room name is taken, please try a new room name:");
            promptCreate(chatApp, currUser);
        } else if (roomName.matches("[a-z0-9]+")) {
            chatApp.addRoom(roomName);
            System.out.println("Successfully created the room!");
            //return joinRoom(chatApp, roomName, currUser);
        } else {
            System.out.println("You cannot have symbols in the room name! Try again: ");
            promptCreate(chatApp, currUser);
        }
        return null;
    }
    public static void promptAccount(Database chatApp, User currUser) {
        Scanner scnr = new Scanner(System.in);
        System.out.println("Welcome to the settings page, would you like to change your username or password?");
        System.out.println("Your current credentials are \nUsername: "+currUser.getUsername() + "\nPassword: "+currUser.getPassword());
        System.out.println("Type (U)ser, (P)assword, or /back: ");
        String input = scnr.next().toLowerCase();
        if (input.equals("u") || input.equals("user")) {
            System.out.println("Please enter a new username: ");
            System.out.println("Wrong screen? Type /back to go back:");
            String newUsername = scnr.next().toLowerCase();
            if (newUsername.equals("/back")) {
                promptAccount(chatApp, currUser);
            }
            if(chatApp.checkUserExists(newUsername)) {
                System.out.println("Username is taken, please try a new username:");
                promptAccount(chatApp, currUser);
            }
            else {
                chatApp.updateUsername(currUser.getUsername(), newUsername);
                currUser.setUsername(newUsername);
                System.out.println("Username successfully updated!");
            }
        } else if (input.equals("p") || input.equals("password")) {
            System.out.println("Please enter a new password");
            System.out.println("Wrong screen? Type /back to go back:");
            String newPass = scnr.next();
            if (newPass.equals("/back")) {
                promptAccount(chatApp, currUser);
            }
            System.out.println("Please confirm your password: ");
            String newPass2 = scnr.next();
            if (newPass2.equals(newPass)) {
                chatApp.updatePassword(currUser.getUsername(), newPass);
                currUser.setPassword(newPass);
                System.out.println("Password successfully changed!");
            } else {
                System.out.println("The two passwords do not match, please try again: ");
                promptAccount(chatApp, currUser);
            }
        } else if (input.equals("/back")) {
            //do nithinbghn
        } else {
            System.out.println("Command not recognized, please try again");
            promptAccount(chatApp, currUser);
        }

    }
    public static User promptLogin(Database chatApp) {
        Scanner scnr = new Scanner(System.in);
        System.out.println("Please enter your username and password: ");
        System.out.println("Wrong screen? Type /back to return to the login page:");
        System.out.println("Username: ");
        String username = scnr.next().toLowerCase();
        if (username.equals("/back")) {
            return null;
        }
        System.out.println("Password: ");
        String password = scnr.next();
        return login(chatApp, username, password);
    }
    public static User login(Database chatApp, String username, String password) {
        if (chatApp.checkUserPassword(username,password)) {
            System.out.println("Login successful");
            return new User(username, password);//the user with this username
        } else {
            System.out.println("Login attempt failed, please try again");
            return promptLogin(chatApp);
        }
    }
    public static void leaveRoom(Database chatApp, User currUser, Room myRoom) {
        myRoom.removeUser(currUser);
        chatApp.updateUserRoom(currUser.getUsername(), null);
        currUser.setRoom(null);
    }
    public static void checkMessage(Database chatApp, User currUser, String message) {
        //checks if message starts with a / then executes cmd or uploads message to database
        if (message.length() == 0) {
            return;
        }
        if (message.charAt(0) == '/') {
            String command = message.substring(1);
            if (command.equals("help")) {
                divide();
                System.out.println("/list - Displays active users");
                System.out.println("/leave - Exits the chat room");
                System.out.println("/history - Prints all past messages for the room");
                System.out.println("/help - Prints the list of available commands");
                divide();
            }
            else if(command.equals("list")) {
                currUser.getRoom().setUserList(chatApp.getUsers(currUser.getRoom().getName()));
                System.out.println("Active users: " + currUser.getRoom().currUsers());
            }
            else if(command.equals("leave")) {
                System.out.println("See ya!");
                leaveRoom(chatApp, currUser, currUser.getRoom());
            }
            else if(command.equals("history")) {
                currUser.getRoom().setChatHistory(chatApp.getRoomHistory(currUser.getRoom().getName()));
                divide();
                System.out.println("Chat History: ");
                currUser.getRoom().chatHistory();
                divide();
            }
            else {
                System.out.println("Invalid command.");
            }
        }
        else {
            //currUser.getRoom().addMessage(currUser.getUsername() + ": " + message);
            chatApp.addMessage(currUser.getUsername(), currUser.getRoom().getName(), message);
            ArrayList<String> newMessages = chatApp.getNewMessages(currUser.getRoom().getName(), currUser.getRoom().getLastID());
            for(int i = 0; i < newMessages.size(); i++) {
                System.out.println(newMessages.get(i));
                currUser.getRoom().addMessage(newMessages.get(i));

            }
            //currUser.getRoom().addMessage(currUser.getUsername() + ": " + message);
            currUser.getRoom().setLastID(chatApp.getLastID(currUser.getRoom().getName()));
        }
    }

}