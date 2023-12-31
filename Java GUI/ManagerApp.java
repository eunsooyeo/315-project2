import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** 
Manager app that sets up main starting page of GUI on manager side
@author Kevin Tang
@author Dicong Wang
*/
public class ManagerApp extends JFrame {
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private ManagerFunctions managerFunctions;
    private Order order;

    /** 
    Constructor to set up the manager app page
    @param m to include the managerFunctions usability
    @param o to include the order usability

    */
    public ManagerApp(ManagerFunctions m, Order o) {
        setTitle("Manager Interface");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);

        managerFunctions = m;
        order = o;

        // Create the main panel to hold the UI components
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create the side bar on the left with options
        JPanel sideBar = new JPanel();
        sideBar.setLayout(new GridLayout(7, 1));
        JButton employeesButton = new JButton("Employees");
        JButton inventoryButton = new JButton("Inventory");
        JButton supplyHistoryButton = new JButton("Supply History");
        JButton orderHistoryButton = new JButton("Order History");
        JButton menusButton = new JButton("Menus");
        JButton reportsButton = new JButton("Reports");

        employeesButton.setFocusPainted(false);
        employeesButton.setBorderPainted(false);
        employeesButton.setBackground(new Color(223, 227, 238)); // Light gray

        inventoryButton.setFocusPainted(false);
        inventoryButton.setBorderPainted(false);
        inventoryButton.setBackground(new Color(223, 227, 238));

        menusButton.setFocusPainted(false);
        menusButton.setBorderPainted(false);
        menusButton.setBackground(new Color(223, 227, 238)); 

        supplyHistoryButton.setFocusPainted(false);
        supplyHistoryButton.setBorderPainted(false);
        supplyHistoryButton.setBackground(new Color(223, 227, 238)); 

        orderHistoryButton.setFocusPainted(false);
        orderHistoryButton.setBorderPainted(false);
        orderHistoryButton.setBackground(new Color(223, 227, 238)); 

        reportsButton.setFocusPainted(false);
        reportsButton.setBorderPainted(false);
        reportsButton.setBackground(new Color(223, 227, 238)); 

        sideBar.add(employeesButton);
        sideBar.add(inventoryButton);
        sideBar.add(menusButton);
        sideBar.add(supplyHistoryButton);
        sideBar.add(orderHistoryButton);
        sideBar.add(reportsButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LogoutPopup(ManagerApp.this, order, managerFunctions);
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
        JPanel employeesPage = new EmployeeApp(managerFunctions);
        cardPanel.add(employeesPage, "Employees");

        // Create the Inventory page
        JPanel inventoryPage = new InventoryApp(managerFunctions);
        cardPanel.add(inventoryPage, "Inventory");

        // Create the Supply History page
        JPanel supplyHistoryPage = new SupplyHistoryApp(managerFunctions);
        cardPanel.add(supplyHistoryPage, "Supply History");

        // Create the Menus page
        JPanel menusPage = new MenusApp(m);
        cardPanel.add(menusPage, "Menus");

        JPanel orderHistoryPage = new OrderHistoryApp(managerFunctions);
        cardPanel.add(orderHistoryPage, "Order History");

        JPanel reportsPage = new ReportsApp(managerFunctions);
        cardPanel.add(reportsPage, "Reports");

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
                openEmployeeApp();
                cardLayout.show(cardPanel, "Employees");
            }
        });

        inventoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "Inventory");
                ((InventoryApp) cardPanel.getComponent(1)).updateDisplay();
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
                openMenusApp();
                cardLayout.show(cardPanel, "Menus");
            }
        });
        orderHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "Order History");
            }
        });
        reportsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "Reports");
            }
        });

    }

    /** 
    Function to open the menus app page from the existing page

    */
    private void openMenusApp() {
        MenusApp menusApp = new MenusApp(managerFunctions);
        menusApp.setVisible(true);
        // dispose();
    }
    /** 
    Function to open the employees app page from the existing page

    */
    private void openEmployeeApp() {
        EmployeeApp employeeApp = new EmployeeApp(managerFunctions);
        employeeApp.setVisible(true);
    }
}