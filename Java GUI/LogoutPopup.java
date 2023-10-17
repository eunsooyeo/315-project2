import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
GUI Logout popup to confirm and allow logout
@author Kevin Tang
@author Dicong Wang
*/
public class LogoutPopup extends JDialog {
    private ManagerFunctions managerFunctions;
    private Order order;

    // @function Constructor to set up the popup page
    // @param parent to use existing jframe for setup
    // @param o include to allow usage of order functions
    // @param m to allow usage of managerFunctions
    public LogoutPopup(JFrame parent, Order o, ManagerFunctions m) {
        super(parent, "Logout Confirmation", true);

        managerFunctions = m;
        order = o;

        setLayout(new BorderLayout());

        JLabel confirmationLabel = new JLabel("Are you sure you want to logout?");
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton logoutButton = new JButton("Logout");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(logoutButton);
        buttonPanel.add(cancelButton);

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle the logout action, such as returning to the login page
                // You can close the current CashierApp frame and open a new LoginApp frame here
                parent.dispose(); // Close the CashierApp frame
                
                // Open a new LoginApp frame with the Order object
                LoginApp loginApp = new LoginApp(order, managerFunctions);
                loginApp.setVisible(true);
                
                dispose(); // Close the LogoutPopup
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the popup without logging out
            }
        });

        add(confirmationLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }
}