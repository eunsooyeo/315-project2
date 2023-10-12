import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManagerApp extends JFrame {
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private Order order;

    public ManagerApp(Order o) {
        order = o;

        setTitle("Manager Interface");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);

        // Create the main panel to hold the UI components
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create the side bar on the left with options
        JPanel sideBar = new JPanel();
        sideBar.setLayout(new GridLayout(6, 1));
        JButton employeesButton = new JButton("Employees");
        JButton inventoryButton = new JButton("Inventory");
        JButton supplyHistoryButton = new JButton("Supply History");
        JButton orderHistoryButton = new JButton("Order History");
        JButton menusButton = new JButton("Menus");

        employeesButton.setFocusPainted(false);
        employeesButton.setBorderPainted(false);
        employeesButton.setBackground(new Color(223, 227, 238)); // Light gray

        inventoryButton.setFocusPainted(false);
        inventoryButton.setBorderPainted(false);
        inventoryButton.setBackground(new Color(223, 227, 238)); // Light gray

        menusButton.setFocusPainted(false);
        menusButton.setBorderPainted(false);
        menusButton.setBackground(new Color(223, 227, 238)); // Light gray

        supplyHistoryButton.setFocusPainted(false);
        supplyHistoryButton.setBorderPainted(false);
        supplyHistoryButton.setBackground(new Color(223, 227, 238)); // Light gray

        orderHistoryButton.setFocusPainted(false);
        orderHistoryButton.setBorderPainted(false);
        orderHistoryButton.setBackground(new Color(223, 227, 238)); // Light gray

        sideBar.add(employeesButton);
        sideBar.add(inventoryButton);
        sideBar.add(menusButton);
        sideBar.add(supplyHistoryButton);
        sideBar.add(orderHistoryButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LogoutPopup(ManagerApp.this, order);
            }
        });
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setBackground(new Color(181, 184, 192)); // Dark gray
        sideBar.add(logoutButton);

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

        //Create the Menus page
        JPanel menusPage = new MenusApp();
        cardPanel.add(menusPage, "Menus");

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

        menusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "Menus");
            }
        });
    }

/*     public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerApp managerApp = new ManagerApp(order);
            managerApp.setVisible(true);
        });
    } */
}