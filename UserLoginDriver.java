import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class UserLoginDriver implements ActionListener {

	private static JFrame loginFrame;
	private static JFrame addUserFrame;
	private static JPanel addUserPanel;
	private static JPanel loginPanel;
	private static JLabel userLabel;
	private static JLabel newUserLabel;
	private static JTextField userText;
	private static JTextField newUserText;
	private static JLabel passwordLabel;
	private static JLabel newPasswordLabel;
	private static JPasswordField passwordText;
	private static JPasswordField newPasswordText;
	private static JButton loginButton;
	private static JLabel loginStatus;
	private static JLabel newUserStatus;
	private static JButton addUserButton;
	private static JButton addNewUserButton;

	public static void setup() {
		UserLogins.setupDB();

		loginFrame = new JFrame("Login");
		loginPanel = new JPanel();
		loginFrame.setSize(350, 200);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		loginFrame.add(loginPanel);

		loginPanel.setLayout(null);
		loginFrame.setSize(350, 200);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		loginFrame.add(loginPanel);
		loginPanel.setLayout(null);

	}

	public static void printAddUserScreen() {
		addUserFrame = new JFrame("Add New User");
		addUserPanel = new JPanel();
		addUserFrame.setSize(350, 200);
		addUserFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		addUserFrame.add(addUserPanel);

		addUserPanel.setLayout(null);
		addUserFrame.setSize(350, 200);
		addUserFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		addUserFrame.add(addUserPanel);
		addUserPanel.setLayout(null);
		addUserFrame.setVisible(true);

		newUserLabel = new JLabel("Desired Username");
		newUserLabel.setBounds(10, 20, 120, 25);
		addUserPanel.add(newUserLabel);

		newUserText = new JTextField();
		newUserText.setBounds(130, 20, 165, 25);
		addUserPanel.add(newUserText);

		newPasswordLabel = new JLabel("Desired Password");
		newPasswordLabel.setBounds(10, 50, 120, 25);
		addUserPanel.add(newPasswordLabel);

		newPasswordText = new JPasswordField();
		newPasswordText.setBounds(130, 50, 165, 25);
		addUserPanel.add(newPasswordText);

		addNewUserButton = new JButton("Add New User");
		addNewUserButton.setBounds(130, 80, 165, 25);
		addNewUserButton.addActionListener(new UserLoginDriver());
		addUserPanel.add(addNewUserButton);

		newUserStatus = new JLabel("");
		newUserStatus.setBounds(130, 110, 300, 25);
		addUserPanel.add(newUserStatus);

	}

	public static void printLoginScreen() {
		userLabel = new JLabel("Username");
		userLabel.setBounds(10, 20, 80, 25);
		loginPanel.add(userLabel);

		userText = new JTextField();
		userText.setBounds(100, 20, 165, 25);
		loginPanel.add(userText);

		passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(10, 50, 80, 25);
		loginPanel.add(passwordLabel);

		passwordText = new JPasswordField();
		passwordText.setBounds(100, 50, 165, 25);
		loginPanel.add(passwordText);

		loginButton = new JButton("Login");
		loginButton.setBounds(10, 80, 80, 25);
		loginButton.addActionListener(new UserLoginDriver());
		loginPanel.add(loginButton);

		addUserButton = new JButton("Add User");
		addUserButton.setBounds(100, 80, 90, 25);
		addUserButton.addActionListener(new UserLoginDriver());
		loginPanel.add(addUserButton);

		loginStatus = new JLabel("");
		loginStatus.setBounds(10, 110, 300, 25);
		loginPanel.add(loginStatus);
		loginStatus.setText(null);
	}

	public static void main(String[] args) {
		setup();
		printLoginScreen();

		loginFrame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addUserButton) {
			printAddUserScreen();
		} else if (e.getSource() == addNewUserButton) {
			String desiredUsername = newUserText.getText();
			String desiredPassword = newPasswordText.getText();

			if (desiredUsername.trim().equals("") || desiredPassword.trim().equals("")) {
				newUserStatus.setText("Missing username and/or password");

			} else if (UserLogins.addUser(desiredUsername, desiredPassword)) {
				newUserStatus.setText("User added sucessfully!");
			} else {
				newUserStatus.setText("Username already taken.");
			}
		} else if (e.getSource() == loginButton) {
			String username = userText.getText();
			String password = passwordText.getText();

			if (username.equals("") || password.equals("")) {
				loginStatus.setText("Missing username and/or password");
			} else if (UserLogins.isValidCredentials(username, password)) {
				loginStatus.setText("Login successful!");
			} else {
				loginStatus.setText("Invalid username and/or password");
			}
		}
	}
}
