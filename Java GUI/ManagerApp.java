import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManagerApp extends JFrame {
    private JPanel cardPanel;
    private CardLayout cardLayout;

    public ManagerApp() {
        setTitle("Manager Interface");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);

        // Create the main panel to hold the UI components
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create the side bar on the left with options
        JPanel sideBar = new JPanel();
        sideBar.setLayout(new GridLayout(4, 1));
        JButton employeesButton = new JButton("Employees");
        JButton inventoryButton = new JButton("Inventory");
        JButton supplyHistoryButton = new JButton("Supply History");
        JButton orderHistoryButton = new JButton("Order History");

        sideBar.add(employeesButton);
        sideBar.add(inventoryButton);
        sideBar.add(supplyHistoryButton);
        sideBar.add(orderHistoryButton);

        // Create a panel with CardLayout to switch between pages
        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);

        // Create the Employees page
        JPanel employeesPage = new EmployeeApp();
        cardPanel.add(employeesPage, "Employees");

        // Create the Inventory page
        JPanel inventoryPage = new InventoryApp();
        cardPanel.add(inventoryPage, "Inventory");

        // Create the Supply History page
        JPanel supplyHistoryPage = new SupplyHistoryApp();
        cardPanel.add(supplyHistoryPage, "Supply History");

        // Initially show the Employees page
        cardLayout.show(cardPanel, "Employees");

        // Add components to the main panel
        mainPanel.add(sideBar, BorderLayout.WEST);
        mainPanel.add(cardPanel, BorderLayout.CENTER);

        add(mainPanel);

        // Add action listeners to switch between pages
        employeesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "Employees");
            }
        });

        inventoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "Inventory");
            }
        });

        supplyHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "Supply History");
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerApp managerApp = new ManagerApp();
            managerApp.setVisible(true);
        });
    }
}