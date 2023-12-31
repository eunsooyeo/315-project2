import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.TreeMap;

/** 
This class is for the GUI of the employee tab on the manager side of the POS. It shows the managers and employees and their information.
@author Kevin Tang
@author Dicong Wang
*/
public class EmployeeApp extends JPanel {
    private JTextArea detailsTextArea;
    private JTextField idField;
    private JTextField nameField;
    private JTextField hoursField;
    private JTextField passwordField; // Added password field
    private JTextField payField; // Added pay field
    private JButton editButton;
    private DefaultListModel<String> managerListModel;
    private DefaultListModel<String> employeeListModel;
    private JTextArea informationTextArea;
    private ManagerFunctions managerFunctions;
    private HashMap<String, ArrayList<String>> detailsMap;

    /** 
    Constructor for the employee app. Sets up the GUI framework for this page.
    @param msp to include usage of managerfunctions class
    */
    public EmployeeApp(ManagerFunctions msp) {
        setLayout(new BorderLayout());
        
        managerFunctions = msp;

        // Create the center panel with sections for managers and employees
        JPanel centerPanel = new JPanel(new GridLayout(2, 1));

        // Create the managers section with horizontal scrollbar
        JPanel managersSection = new JPanel();
        managersSection.setLayout(new BoxLayout(managersSection, BoxLayout.X_AXIS));

        managerListModel = new DefaultListModel<>();
            //This should have the employee/manager with corresponding information
        detailsMap = new HashMap<>();


        TreeMap<String, Boolean> treeMap = managerFunctions.getEmployeeNames();
        ArrayList<String> allManagers = new ArrayList<>();
        ArrayList<String> allEmployees = new ArrayList<>();
        for (String name : treeMap.keySet()){
            if (treeMap.get(name)){ //if isManager is true
                allManagers.add(name);
            }
            else{
                allEmployees.add(name);
            }
            detailsMap.put(name, managerFunctions.getEmployeeInfo(name));
        }

        int numManagers = managerFunctions.getNumOfManagers();
        for (int i = 0; i < numManagers; i++) {
            //get all managers
            managerListModel.addElement(allManagers.get(i));
        }

        JList<String> managerList = new JList<>(managerListModel);
        managerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        managerList.setVisibleRowCount(1);
        JScrollPane managersScrollPane = new JScrollPane(managerList);
        managersScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        // Create the employees section with horizontal scrollbar
        JPanel employeesSection = new JPanel();
        employeesSection.setLayout(new BoxLayout(employeesSection, BoxLayout.X_AXIS));

        employeeListModel = new DefaultListModel<>();

        int numEmployees = managerFunctions.getNumOfEmployees();
        for (int i = 0; i < numEmployees; i++) {
            employeeListModel.addElement(allEmployees.get(i));
            
        }


        JList<String> employeeList = new JList<>(employeeListModel);
        employeeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeList.setVisibleRowCount(1);
        JScrollPane employeesScrollPane = new JScrollPane(employeeList);
        employeesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        centerPanel.add(managersScrollPane);
        centerPanel.add(employeesScrollPane);

        // Create the right sidebar for displaying details
        JPanel rightSidebar = new JPanel(new BorderLayout());
        detailsTextArea = new JTextArea("Details will be displayed here.");
        informationTextArea = new JTextArea("Information will be displayed here.");
        rightSidebar.add(new JScrollPane(detailsTextArea), BorderLayout.CENTER);
        rightSidebar.add(new JScrollPane(informationTextArea), BorderLayout.SOUTH);

        // Create the editing panel
        JPanel editingPanel = new JPanel();
        editingPanel.setLayout(new BoxLayout(editingPanel, BoxLayout.Y_AXIS));

        nameField = new JTextField(20);
        idField = new JTextField(20);
        hoursField = new JTextField(20);
        passwordField = new JTextField(20); // Added password field
        payField = new JTextField(20); // Added pay field
        editButton = new JButton("Edit Information");

        JButton addEmployeeButton = new JButton("Add Employee");
        JButton addManagerButton = new JButton("Add Manager");
        JButton removeEmployeeButton = new JButton("Remove Employee");
        JButton removeManagerButton = new JButton("Remove Manager");

        editingPanel.add(new JLabel("ID:"));
        editingPanel.add(idField);
        editingPanel.add(new JLabel("Name:"));
        editingPanel.add(nameField);
        editingPanel.add(new JLabel("Hours/Week:"));
        editingPanel.add(hoursField);
        editingPanel.add(new JLabel("Password:")); // Added password label
        editingPanel.add(passwordField); // Added password field
        editingPanel.add(new JLabel("Pay:")); // Added pay label
        editingPanel.add(payField); // Added pay field
        editingPanel.add(editButton);
        editingPanel.add(addEmployeeButton);
        editingPanel.add(addManagerButton);
        editingPanel.add(removeEmployeeButton);
        editingPanel.add(removeManagerButton);

        rightSidebar.add(editingPanel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);
        add(rightSidebar, BorderLayout.EAST);

        // Add action listeners to display details and edit information
        managerList.addListSelectionListener(e -> {
            int selectedIndex = managerList.getSelectedIndex();
            if (selectedIndex != -1) {
                String managerName = managerListModel.get(selectedIndex);
                displayManagerDetails(managerName);
            }
        });

        employeeList.addListSelectionListener(e -> {
            int selectedIndex = employeeList.getSelectedIndex();
            if (selectedIndex != -1) {
                String employeeName = employeeListModel.get(selectedIndex);
                displayEmployeeDetails(employeeName);
            }
        });

        editButton.addActionListener(e -> {
            // Handle editing employee or manager information
            int selectedIndex = managerList.getSelectedIndex();
            if (selectedIndex != -1) {
                updateManagerInformation(selectedIndex);
            } 
            else {
                selectedIndex = employeeList.getSelectedIndex();
                if (selectedIndex != -1) {
                    updateEmployeeInformation(selectedIndex);
                }
            }
        });

        addEmployeeButton.addActionListener(e -> {
            // Handle adding a new employee
            String newID = idField.getText();
            String newName = nameField.getText();
            String newPosition = hoursField.getText();
            String newPassword = passwordField.getText(); // Get the password
            String newPay = payField.getText(); // Get the pay
            addEmployee(newID, newName, newPosition, newPassword, newPay);
        });

        addManagerButton.addActionListener(e -> {
            // Handle adding a new manager
            String newID = idField.getText();
            String newName = nameField.getText();
            String newPosition = hoursField.getText();
            String newPassword = passwordField.getText(); // Get the password
            String newPay = payField.getText(); // Get the pay
            addManager(newID, newName, newPosition, newPassword, newPay);
        });

        removeEmployeeButton.addActionListener(e -> {
            // Handle removing an employee
            int selectedIndex = employeeList.getSelectedIndex();
            if (selectedIndex != -1) {
                removeEmployee(selectedIndex);
            }
        });

        removeManagerButton.addActionListener(e -> {
            int selectedIndex = managerList.getSelectedIndex();
            if (selectedIndex != -1) {
                removeManager(selectedIndex);
            }
        });
    }
    /** 
    Function to load details about manager from database that is clicked on on the GUI.
    @param managerName string of the name to find in the database

    */
    private void displayManagerDetails(String managerName) {
        if (managerName != null) {
            ArrayList<String> managerInfo = detailsMap.get(managerName);
            if (managerInfo != null) {
                detailsTextArea.setText("Manager Details:\n"+ "ID: " + managerInfo.get(0) + "\nName: " + managerName + "\nPay: " + managerInfo.get(3) + "\nHours: " + managerInfo.get(4) + "\nPosition: manager");
            } else {
                detailsTextArea.setText("Details not found for " + managerName);
            }
        } else {
            detailsTextArea.setText("No manager selected.");
        }
    }
    /** 
    Function to load details about employee from database that is clicked on on the GUI.
    @param employeeName string of the name to find in the database
    @return void
    @throws none

    */
    private void displayEmployeeDetails(String employeeName) {
        if (employeeName != null) {
            ArrayList<String> employeeInfo = detailsMap.get(employeeName);
            if (employeeInfo != null) {
                detailsTextArea.setText("Employee Details:\n"+ "ID: " + employeeInfo.get(0) + "\nName: " + employeeName + "\nPay: " + employeeInfo.get(3) + "\nHours: " + employeeInfo.get(4) + "\nPosition: employee");
            } else {
                detailsTextArea.setText("Details not found for " + employeeName);
            }
        } else {
            detailsTextArea.setText("No employee selected.");
        }
    }
    /** 
    Function to update information about manager that user changed. Also includes a call to SQL function
    @param index  refers to the position in the listmodel to identify which manager.

    */
    private void updateManagerInformation(int index) {
        String oldManager = managerListModel.get(index);
        String newID = idField.getText();
        String newName = nameField.getText();
        String newHours = hoursField.getText();
        String newPassword = passwordField.getText(); // Get the password
        String newPay = payField.getText(); // Get the pay
        String updatedDetails = "ID: " + newID + "\nName: " + newName + "\nHours/Week: " + newHours +
                "\nPassword: " + newPassword + "\nPay: " + newPay;
        managerListModel.set(index, newName);
        detailsTextArea.setText(updatedDetails);
        ArrayList<String> managerInfo = new ArrayList<>();
        managerInfo.add(newID);
        managerInfo.add(newName);
        managerInfo.add(newPassword);
        managerInfo.add(newPay);
        managerInfo.add(newHours);
        managerInfo.add(",t");
        detailsMap.put(newName, managerInfo);
        detailsMap.remove(oldManager);
        // Update the database
        managerFunctions.updateEmployeeSQL(newName, newHours, newPassword, newPay);
    }

    /** 
    Function to update information about employee that user changed. Also includes a call to SQL function
    @param index  refers to the position in the listmodel to identify which employee.

    */
    private void updateEmployeeInformation(int index) {
        String oldEmployee = employeeListModel.get(index);
        // Handle editing employee information
        String newID = idField.getText();
        String newName = nameField.getText();
        String newHours = hoursField.getText();
        String newPassword = passwordField.getText(); // Get the password
        String newPay = payField.getText(); // Get the pay
        String updatedDetails = "ID: " + newID + "\nName: " + newName + "\nHours/Week: " + newHours +
                "\nPassword: " + newPassword + "\nPay: " + newPay;
        employeeListModel.set(index, newName);
        detailsTextArea.setText(updatedDetails);
        ArrayList<String> employeeInfo = new ArrayList<>();
        employeeInfo.add(newID);
        employeeInfo.add(newName);
        employeeInfo.add(newPassword);
        employeeInfo.add(newPay);
        employeeInfo.add(newHours);
        employeeInfo.add(",f");
        detailsMap.put(newName, employeeInfo);
        detailsMap.remove(oldEmployee);
        // Update the database
        managerFunctions.updateEmployeeSQL(newName, newHours, newPassword, newPay);
    }

    /** 
    Function to create new employee on POS and calls a function that updates database
    @param id string of new employee id
    @param name string of new employee name
    @param hours string of new employee hours
    @param password string of new employee password
    @param pay string of new employee pay

    */
    private void addEmployee(String id, String name, String hours, String password, String pay) {
        if (name.isEmpty()) {
            return; // Don't add if the name is empty
        }

        // Update the database
        managerFunctions.createNewEmployee(id, name, password, hours, pay, "f");
        employeeListModel.addElement(name);
        detailsTextArea.setText("Name: " + name + "\nHours/Week: " + hours +
                "\nPassword: " + password + "\nPay: " + pay);
        ArrayList<String> employeeInfo = new ArrayList<>();
        employeeInfo.add(id);
        employeeInfo.add(name);
        employeeInfo.add(password);
        employeeInfo.add(pay);
        employeeInfo.add(hours);
        employeeInfo.add(",f");
        detailsMap.put(name, employeeInfo);
    }

    /** 
    Function to create new manager on POS and calls a function that updates database
    @param id string of new manager id
    @param name string of new manager name
    @param hours string of new manager hours
    @param password string of new manager password
    @param pay string of new manager pay

    */
    private void addManager(String id, String name, String hours, String password, String pay) {
        if (name.isEmpty()) {
            return; // Don't add if the name is empty
        }

        // Update the database
        managerFunctions.createNewEmployee(id, name, password, hours, pay, "t");
        managerListModel.addElement(name);
        detailsTextArea.setText("Name: " + name + "\nHours/Week: " + hours +
                "\nPassword: " + password + "\nPay: " + pay);
        ArrayList<String> managerInfo = new ArrayList<>();
        managerInfo.add(id);
        managerInfo.add(name);
        managerInfo.add(password);
        managerInfo.add(pay);
        managerInfo.add(hours);
        managerInfo.add(",t");
        detailsMap.put(name, managerInfo);
    }

    /** 
    Function to remove employee on POS and calls a function that updates database
    @param index  refers to the position in the listmodel to identify which employee is being removed

    */
    private void removeEmployee(int index) {
        if (index >= 0 && index < employeeListModel.getSize()) {
            // Update the database
            managerFunctions.removeEmployeeSQL(employeeListModel.get(index));
            String employeeName = employeeListModel.get(index);
            employeeListModel.remove(index);
            detailsTextArea.setText("Employee removed.");

            detailsMap.remove(employeeName);
        }
    }

    /** 
    Function to remove manager on POS and calls a function that updates database
    @param index  refers to the position in the listmodel to identify which manager is being removed
    @return void
    @throws none

    */
    private void removeManager(int index) {
        if (index >= 0 && index < managerListModel.getSize()) {
            // Update the database
            managerFunctions.removeEmployeeSQL(managerListModel.get(index));
            String managerName = employeeListModel.get(index);
            managerListModel.remove(index);
            detailsTextArea.setText("Manager removed.");
            
            detailsMap.remove(managerName);

        }
    }
}