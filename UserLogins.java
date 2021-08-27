
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Scanner;

public class UserLogins {

  private static Connection c = null;
  private static Statement stmt = null;
  private static String sql = null;

  public static void addUser() {
    String username = "";
    String password = "";
    Scanner scnr = new Scanner(System.in);

    System.out.print("Enter desired username:\n>");
    username = scnr.next();

    try {

      // Check if username is taken

      boolean userExists = checkUserExists(username);

      while (userExists) {
        System.out.print("That name is already taken. Please try something different.\n>");
        username = scnr.next();
        userExists = checkUserExists(username);
      }

      System.out.print("Enter new password:\n>");
      password = getHash(scnr.next());

      sql = "INSERT INTO users (username,password_hash) VALUES('" + username + "','" + password
          + "');";
      System.out.println("Added user: " + username);
      System.out.println("Added password hash: " + password);
      stmt.executeUpdate(sql);

      System.out.println("User added successfully");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static boolean checkUserExists(String username) {
    try {
      sql = "SELECT username FROM users";
      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()) {
        if (rs.getString("username").equals(username)) {
          return true;
        }
      }
      return false;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public static void createNewDatabase(String fileName) {

    String url = "jdbc:sqlite:C:/sqlite/db/" + fileName;

    try (Connection conn = DriverManager.getConnection(url)) {
      if (conn != null) {
        DatabaseMetaData meta = conn.getMetaData();
        System.out.println("The driver name is " + meta.getDriverName());
        System.out.println("A new database has been created.");
      }

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }


  public static void setupDB() {

    Scanner scnr = new Scanner(System.in);
    char userIn;
    String dbName = "userData.db";

    try {

      c = DbConnection.connect(dbName);

      stmt = c.createStatement();
      DatabaseMetaData dbm = c.getMetaData();

      // check for previous data
      ResultSet tables = dbm.getTables(null, null, "users", null);
      if (tables.next()) {
        System.out.print("The database contains previous data. Override? (y/n)\n>");
        userIn = scnr.next().toLowerCase().charAt(0);
        if (userIn == 'y') {
          sql = "DELETE FROM 'users'";
          stmt.executeUpdate(sql);


          System.out.println("Data has been overwritten.");
        } else {
          System.out.println("OK. Proceeding with previous data.");
        }
      }

      sql =
          "CREATE TABLE IF NOT EXISTS \"users\" (\"username\" VARCHAR, \"password_hash\" VARCHAR);";
      stmt.execute(sql);

      System.out.println("Successfully set up database!");

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

  }



  private static boolean isValidCredentials(String username, String password) {
    String hashedPass = getHash(password);
    
    try {
      sql = "SELECT username, password_hash FROM users"; 
      stmt = c.createStatement();
      ResultSet rs = stmt.executeQuery(sql); 
      
      while(rs.next()) {
        if (rs.getString("username").equals(username)) {
          if (rs.getString("password_hash").equals(hashedPass)) {
            return true;
          }
        }
      }
      System.out.println("Incorrect username or password.\nGet lost!"); 
      return false;
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } 
    
    
    return false;
  }

  private static String bytesToHex(byte[] hash) {
    StringBuilder hexString = new StringBuilder(2 * hash.length);
    for (int i = 0; i < hash.length; i++) {
      String hex = Integer.toHexString(0xff & hash[i]);
      if (hex.length() == 1) {
        hexString.append('0');
      }
      hexString.append(hex);
    }
    return hexString.toString();
  }

  public static String getHash(String plaintext) {
    MessageDigest digest;
    try {
      digest = MessageDigest.getInstance("SHA-256");
      byte[] encodedhash = digest.digest(plaintext.getBytes(StandardCharsets.UTF_8));
      String hexValue = bytesToHex(encodedhash);
      return hexValue;
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static void main(String[] args) {

    Scanner scnr = new Scanner(System.in);

    try {
      setupDB();
      addUser();
      
      System.out.print("Enter your username:\n>"); 
      String username = scnr.next(); 
      System.out.print("Enter your password:\nn>"); 
      String password = scnr.next(); 
      
      System.out.println(isValidCredentials(username, password)); 
      
      stmt.close();
      c.close();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }


  }

}
