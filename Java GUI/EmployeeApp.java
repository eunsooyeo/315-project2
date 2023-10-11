import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EmployeeApp extends JPanel {
    public EmployeeApp() {
        setLayout(new BorderLayout());

        // Create the center panel with sections for managers and employees
        JPanel centerPanel = new JPanel(new GridLayout(2, 1));

        JPanel managersSection = new JPanel(new GridLayout(1, 5));
        for (int i = 1; i <= 5; i++) {
            JButton managerButton = new JButton("Manager " + i);
            managerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Display manager details in the right sidebar
                    // You can implement this logic
                }
            });
            managersSection.add(managerButton);
        }

        JPanel employeesSection = new JPanel(new GridLayout(3, 5));
        for (int i = 1; i <= 15; i++) {
            JButton employeeButton = new JButton("Employee " + i);
            employeeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    /* TODO ****************************************************************************************************************************/
                    // Display employee details in the right sidebar
                    
                }
            });
            employeesSection.add(employeeButton);
        }

        centerPanel.add(managersSection);
        centerPanel.add(employeesSection);

        // Create the right sidebar for displaying details
        JPanel rightSidebar = new JPanel();
        rightSidebar.setLayout(new BorderLayout());
        JTextArea detailsTextArea = new JTextArea("Details will be displayed here.");
        rightSidebar.add(detailsTextArea, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
        add(rightSidebar, BorderLayout.EAST);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Employee Page");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 400);
            frame.add(new EmployeeApp());
            frame.setVisible(true);
        });
    }
}