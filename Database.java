import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
public class Database {
    private Connection c;
    private Statement stmt;

//	public void test() {
//		try {
//			stmt = c.createStatement();
//			ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE ID = 1;");
//			while(rs.next()) {
//				int ID = rs.getInt("ID");
//				String username = rs.getString("USERNAME");
//				String password = rs.getString("PASSWORD");
//				String room = rs.getString("ROOM");
//				System.out.println(ID + " | " + username + " | " + password + " | " + room);
//			}
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//			System.err.print(e.getClass().getName()+": "+e.getMessage());
//			System.exit(0);
//		}
//	}

    public void setup() {
        connect();
        createTable("users");
        createTable("rooms");
        createTable("messages");
    }

    private void connect() {
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/chatapp", "postgres", "pass");
            System.out.println("Connected to the DB");

        }catch(Exception e) {
            e.printStackTrace();
            System.err.print(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    private void createTable(String type) {
        try {
            stmt = c.createStatement();
            String sql;
            if(type.equals("users")) {
                sql = "CREATE TABLE IF NOT EXISTS USERS" +
                        " (ID SERIAL PRIMARY KEY NOT NULL," +
                        " USERNAME TEXT NOT NULL," +
                        " PASSWORD TEXT NOT NULL," +
                        " ROOM TEXT)";
            }
            else if(type.equals("rooms")) {
                sql = "CREATE TABLE IF NOT EXISTS ROOMS" +
                        " (ID SERIAL PRIMARY KEY NOT NULL," +
                        " NAME TEXT NOT NULL)";
            }
            else if(type.equals("messages")) {
                sql = "CREATE TABLE IF NOT EXISTS MESSAGES" +
                        " (ID SERIAL PRIMARY KEY NOT NULL," +
                        " USERNAME TEXT NOT NULL," +
                        " ROOM TEXT NOT NULL," +
                        " MESSAGE TEXT NOT NULL)";
            }
            else {
                //MIGHT NOT WORK
                sql = "";
            }
            stmt.executeUpdate(sql);
            stmt.close();
            //c.close();
            System.out.println("Table was created");
        }catch(Exception e) {
            e.printStackTrace();
            System.err.print(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    private void insert(String sql) {
        try {
            c.setAutoCommit(false);
            stmt = c.createStatement();
            stmt.executeLargeUpdate(sql);
            stmt.close();
            //must include this command or wont work
            c.commit();
            //c.close();
        }
        catch(Exception e) {
            e.printStackTrace();
            System.err.print(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    public void addUser(String name, String password) {
        String sql = "INSERT INTO USERS (" +
                "USERNAME,PASSWORD)" +
                "VALUES('" + name + "', '" + password + "');";
        insert(sql);
    }

    public void addRoom(String name) {
        String sql = "INSERT INTO ROOMS (" +
                "NAME)" +
                "VALUES('" + name + "');";
        insert(sql);
    }

    public void addMessage(String user, String room, String message) {
        String sql = "INSERT INTO MESSAGES (" +
                "USERNAME,ROOM,MESSAGE)" +
                "VALUES('" + user + "', '" + room + "', '" + message + "');";
        insert(sql);
    }

    private ResultSet getUserInfo(String username) {
        ResultSet rs = null;
        try {
            stmt = c.createStatement();
            //rs = stmt.executeQuery("SELECT id,password FROM users WHERE EXISTS (SELECT 1 FROM users WHERE username = '" + username + "');");
            rs = stmt.executeQuery("SELECT id,password FROM users WHERE username = '" + username + "';");
        }
        catch(Exception e) {
            e.printStackTrace();
            System.err.print(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }

        return rs;
    }

    //used to check if username is taken before creating new account
    public boolean checkUserExists(String username) {
        ResultSet rs = getUserInfo(username);
        try {
            if(rs.next()) return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.print(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return false;
    }

    public boolean checkRoomExists(String room) {
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id FROM rooms WHERE EXISTS (SELECT 1 FROM rooms WHERE name = '" + room + "');");
            return rs.next();
        }
        catch(Exception e) {
            e.printStackTrace();
            System.err.print(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }

        return false;
    }

    public boolean checkUserPassword(String username, String password) {
        ResultSet rs = getUserInfo(username);
        String realPassword = "";
        try {
            if(rs.next()) {
                realPassword = rs.getString("password");
                //System.out.println(realPassword);
                if(password.equals(realPassword)) return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.print(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }


        return false;
    }

    public void updateUsername(String oldUsername, String newUsername) {
        updateUserInfo("username", oldUsername, newUsername);
    }

    public void updatePassword(String username, String newPassword) {
        updateUserInfo("password", username, newPassword);
    }

    public void updateUserRoom(String username, String room) {
        updateUserInfo("room", username, room);
    }

    private void updateUserInfo(String infoType, String username, String newInfo) {
        ResultSet rs = getUserInfo(username);
        try {
            if(rs.next()) {
                int ID = rs.getInt("ID");
                c.setAutoCommit(false);
                stmt = c.createStatement();
                String sql = "UPDATE USERS SET " + infoType + " = '" + newInfo + "' WHERE ID = " + ID + ";";
                stmt.executeUpdate(sql);
                c.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.print(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    public ArrayList<String> getRoomHistory(String room) {
        ArrayList<String> messages = new ArrayList<>();
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT username,message FROM messages WHERE room = '" + room + "';");
            while(rs.next()) {
                String username = rs.getString("username");
                String message = rs.getString("message");
                messages.add(username + ": " + message);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            System.err.print(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return messages;
    }

    public ArrayList<String> getNewMessages(String room, int lastID) {
        ArrayList<String> messages = new ArrayList<>();
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT username,message FROM messages WHERE (room = '" + room + "' AND id > " + lastID + ");");
            while(rs.next()) {
                String username = rs.getString("username");
                String message = rs.getString("message");
                messages.add(username + ": " + message);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            System.err.print(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return messages;
    }

    public int getLastID(String room) {
        int ID = -1;
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id FROM messages WHERE room = '" + room + "' order by id desc limit 1;");
            if(rs.next()) {
                ID = rs.getInt("id");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            System.err.print(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return ID;
    }

    //TODO: fix string when no active users
    //displays all active users in a room
    public ArrayList<User> getUsers(String room) {
        ArrayList<User> users = new ArrayList<>();
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT username,password FROM users WHERE room = '" + room + "';");
            while(rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                User tempUser = new User(username, password);
                users.add(tempUser);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            System.err.print(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return users;
    }

    public void close() {
        try {
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.print(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }
}