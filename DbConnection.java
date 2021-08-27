import java.sql.*;

public class DbConnection {

  public static Connection connect(String dbName) {
    Connection con = null;
    try {
      Class.forName("org.sqlite.JDBC");
      con = DriverManager.getConnection("jdbc:sqlite:" + dbName);
      System.out.println("Connected!");
    } catch (ClassNotFoundException | SQLException e) {
      // TODO Auto-generated catch block
      System.out.println(e + "");
    }
    return con;
  }

}
