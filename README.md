# 💬 Java Terminal Chat App

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)

A simple terminal-based chat application built in Java using JDBC and PostgreSQL. Users can register, log in, join or create chat rooms, and chat in real time.

---

## 📦 Features

- ✅ Register/Login with a username and password  
- ✅ Create and join chat rooms  
- ✅ Send and view messages in real-time  
- ✅ View chat history and active users  
- ✅ Update account information (username/password)  

---

## 🧰 Technologies Used

- **Java** – Main application language  
- **JDBC** – Database interaction  
- **PostgreSQL** – Database for storing users, rooms, and messages  

---

## 🚀 Getting Started

### 🗂️ Prerequisites

- Java 8 or higher  
- PostgreSQL installed and running  
- PostgreSQL JDBC Driver (included in classpath)

---

### 🛠️ Database Setup

1. Open your PostgreSQL terminal or GUI and run:

```sql
CREATE DATABASE chatapp;
```

2. Default connection info is in `Database.java`:

```java
DriverManager.getConnection("jdbc:postgresql://localhost:5432/chatapp", "postgres", "pass");
```

> 💡 Update credentials and connection string if needed.

---

## 🧪 Running the App

### Compile the source files:

```bash
javac *.java
```

### Run the main program:

```bash
java Main
```

---

## 📁 Project Structure

```
.
├── Database.java   # Handles DB connection and operations
├── Main.java       # Entry point, user interactions, CLI
├── Room.java       # Room model class
├── User.java       # User model class
```

---

## ⚠️ Security Warning

⚠️ This is for learning/demo purposes only. Not safe for production use.

- ❌ Passwords are stored in plaintext – use a hashing algorithm like BCrypt
- ❌ SQL queries use string concatenation – vulnerable to SQL injection
- ✅ Recommended: Use `PreparedStatement` and password hashing before real-world use

---

## 👥 Authors

- Mason Herwegh  
- Dain Lee  
- Matthew Shin  
- Jack White  

---

## ✨ Future Improvements

- [ ] Implement password hashing  
- [ ] Use prepared SQL statements  
- [ ] Add unit tests  
- [ ] Build a web interface with Spring Boot  
- [ ] Improve error handling (remove `System.exit(0)`)
