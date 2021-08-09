import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class UserLogins {

	private static ArrayList<User> userArray = new ArrayList<User>();
	private static Connection c = null; 
	private static Statement stmt = null; 

	public static void addUser() {
		String username = "";
		String password = "";
		Scanner scnr = new Scanner(System.in);

		System.out.println("Enter desired username:\n>");
		username = scnr.next();

		System.out.println("Enter new password:\n>");
		password = getHash(scnr.next());

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:ppab6.db");
			stmt = c.createStatement();
			String sql = "INSERT INTO users (username)\n" + "VALUES(" + username + ");";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO users (password_hash)\n" + "VALUES(" + password + ");";
			stmt.executeUpdate(sql);
			stmt.close();
			// c.commit();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error connecting to SQLite database");
			System.exit(0);
		}
		System.out.println("User added successfully");
	}

	public static void setupDB() {

		Scanner scnr = new Scanner(System.in);
		char userIn; 
		
		try {
			File file = new File("ppab6.db");

			if (file.exists()) // here's how to check
			{
				System.out.println("This database name already exists");
				System.out.print("Would you like to delete and recreate it? (y/n)\n>");
				userIn = scnr.nextLine().toUpperCase().charAt(0);
				if(userIn == 'N') {
					return; 
				} else if (userIn == 'Y') {
					if(file.delete()) {
						System.out.println("Database deleted successfully.");
					} else {
						System.out.println("A problem was encountered while deleting the database.");
					}
				} else {
					System.out.println("Invalid input. Returning to main");
				}
					
			} 

				Class.forName("org.sqlite.JDBC");
				c = DriverManager.getConnection("jdbc:sqlite:ppab6.db");
				stmt = c.createStatement();
				String sql = "CREATE TABLE users (username VARCHAR, password_hash VARCHAR);";
				stmt.executeUpdate(sql);
				stmt.close();
				c.close();
				System.out.println("Successfully created database");
			

		} catch (Exception e) {
			System.err.println("Error connecting to SQLite database");
			System.exit(0);
		}
		
	}

	private static boolean isValidCredentials(String username, String password) {
		String hashedPass = getHash(password);
		for (int i = 0; i < userArray.size(); i++) {
			User validUser = userArray.get(i);
			if (username.equals(validUser.getUsername())) {
				if (hashedPass.equals(validUser.getPassword()))
					return true;
			}
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

		/*
		 * User user1 = new User("user1", "password1"); User user2 = new User("user2",
		 * "password2"); User user3 = new User("user3", "password3");
		 * 
		 * userArray.add(user1); userArray.add(user2); userArray.add(user3);
		 * 
		 * 
		 * Scanner scnr = new Scanner(System.in);
		 * System.out.print("Enter username:\n>"); String username = scnr.next();
		 * System.out.print("Enter password:\n>"); String password = scnr.next();
		 * 
		 * if (isValidCredentials(username, password)) {
		 * System.out.println("Deep dark secret!"); } else {
		 * System.out.println("Invalid username or password\nGet lost!"); }
		 */
		Scanner scnr = new Scanner(System.in);
		setupDB();
		addUser();

	}

}
