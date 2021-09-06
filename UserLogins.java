
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Scanner;

public class UserLogins {

  private static Connection c = null;
  private static Statement stmt = null;
  private static String sql = null;

  public static boolean addUser(String username, String password) {
    try {
      // Check if username is taken
      if (checkUserExists(username)) return false;
      String hashedPassword = getHash(password);

      sql = "INSERT INTO users (username,password_hash) VALUES('" + username + "','" + hashedPassword
          + "');";
 
      stmt.executeUpdate(sql);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
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
        System.out.println("Launching GUI...");
      }
      sql =
          "CREATE TABLE IF NOT EXISTS \"users\" (\"username\" VARCHAR, \"password_hash\" VARCHAR);";
      stmt.execute(sql);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public static boolean isValidCredentials(String username, String password) {
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
      return false;
    } catch (SQLException e) {
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
}
