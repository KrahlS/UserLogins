import java.sql.*;

public class DbConnection {

  public static Connection connect(String dbName) {
    Connection con = null;
    try {
      Class.forName("org.sqlite.JDBC");
      con = DriverManager.getConnection("jdbc:sqlite:" + dbName);
      System.out.println("Connected to database...");
    } catch (ClassNotFoundException | SQLException e) {
      System.out.println(e + "");
    }
    return con;
  }

}
