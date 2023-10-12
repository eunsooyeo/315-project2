import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//package jdbc_demo.dbSetup;


//@Kevin Tang
public class LoginApp extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;
    private Order order; 
    private ManagerFunctions managerFunctions;

    // @param: none
    // @return : void
    // @tnrows: none
    public LoginApp(Order o, ManagerFunctions m) {
        setTitle("Login Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 200);
        setLayout(new BorderLayout());
        initComponents();
        order = o;
        managerFunctions = m;
    }

    // @param: none
    // @return : void
    // @tnrows: none
    private void initComponents() {
        JPanel centerPanel = new JPanel(new GridLayout(3, 2, 5, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel usernameLabel = new JLabel("Username: ");
        JLabel passwordLabel = new JLabel("Password:");
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");
        statusLabel = new JLabel("");

        // Add padding to the labels and center-align them
        usernameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        passwordLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        // Style the login button
        loginButton.setBackground(new Color(0, 123, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();

                /* TODO ****************************************************************************************************************************/
                // Here, you can add code to validate the username and password.
                // For simplicity, let's use "manager" and "cashier" as the valid usernames and
                // "password" as the password.

                if (username.equals("manager") && new String(password).equals("password")) {
                    statusLabel.setText("Login Successful");
                    openManagerApp();
                } else if (username.equals("cashier") && new String(password).equals("password")) {
                    statusLabel.setText("Login Successful");
                    openCashierApp();
                } else {
                    statusLabel.setText("Login Failed");
                }
            }
        });

        centerPanel.add(usernameLabel);
        centerPanel.add(usernameField);
        centerPanel.add(passwordLabel);
        centerPanel.add(passwordField);
        centerPanel.add(new JLabel()); // Empty cell for spacing
        centerPanel.add(loginButton);

        add(centerPanel, BorderLayout.CENTER);

        // Style the status label
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setForeground(Color.RED);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(statusLabel, BorderLayout.SOUTH);
    }

    // @param: none
    // @return : void
    // @tnrows: none
    private void openManagerApp() {
        ManagerApp managerApp = new ManagerApp(managerFunctions);
        managerApp.setVisible(true);
        dispose();
    }

    // @param: none
    // @return : void
    // @tnrows: none
    private void openCashierApp() {
        CashierApp cashierApp = new CashierApp(order);
        cashierApp.setVisible(true);
        dispose();
    }

    // @param: none
    // @return : void
    // @tnrows: none
    
}
