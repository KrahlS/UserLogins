import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*; 

public class UserLoginDriver implements ActionListener {

  private static JFrame frame;
  private static JPanel panel;
  private static JLabel userLabel;
  private static JTextField userText; 
  private static JLabel passwordLabel; 
  private static JPasswordField passwordText;
  private static JButton loginButton;
  private static JLabel success;
  private static JButton addUserButton;
  
  public static void loginScreen() {
    panel.removeAll(); 
    userLabel = new JLabel("Username");
    userLabel.setBounds(10,20,80,25); 
    panel.add(userLabel); 
    
    
    userText = new JTextField(); 
    userText.setBounds(100, 20, 165, 25);
    panel.add(userText); 
    
    passwordLabel = new JLabel("Password"); 
    passwordLabel.setBounds(10,50,80,25); 
    panel.add(passwordLabel);
    
    
    passwordText = new JPasswordField(); 
    passwordText.setBounds(100, 50, 165, 25);
    panel.add(passwordText); 
    
    
    loginButton = new JButton("Login"); 
    loginButton.setBounds(10, 80, 80, 25); 
    loginButton.addActionListener(new UserLoginDriver());
    panel.add(loginButton);
    
    addUserButton = new JButton("Add User"); 
    addUserButton.setBounds(100, 80, 90, 25); 
    addUserButton.addActionListener(new UserLoginDriver());
    panel.add(addUserButton);
    
    success = new JLabel(""); 
    success.setBounds(10, 110, 300, 25); 
    panel.add(success); 
    success.setText(null);
    
  }
  
  public static void main(String[] args) {
    frame = new JFrame();
    panel = new JPanel();  
    frame.setSize(350, 200); 
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    frame.add(panel); 
    
    panel.setLayout(null);
    frame.setSize(350, 200); 
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    frame.add(panel);     
    panel.setLayout(null);
   
    loginScreen();
    
    frame.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    System.out.println("Button pressed"); 
    
  }
  
  
}
