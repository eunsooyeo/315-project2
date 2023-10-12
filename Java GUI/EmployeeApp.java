import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EmployeeApp extends JPanel {
    private JTextArea detailsTextArea;
    private JTextField nameField;
    private JTextField positionField;
    private JButton editButton;
    private DefaultListModel<String> managerListModel;
    private DefaultListModel<String> employeeListModel;
    private JTextArea informationTextArea;
    private ManagerFunctions managerFunctions;

    public EmployeeApp(ManagerFunctions m) {
        setLayout(new BorderLayout());

        managerFunctions = m;

        // Create the center panel with sections for managers and employees
        JPanel centerPanel = new JPanel(new GridLayout(2, 1));

        // Create the managers section with horizontal scrollbar
        JPanel managersSection = new JPanel();
        managersSection.setLayout(new BoxLayout(managersSection, BoxLayout.X_AXIS));

        managerListModel = new DefaultListModel<>();
        for (int i = 1; i <= 15; i++) {
            managerListModel.addElement("Manager " + i);
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
        for (int i = 1; i <= 30; i++) {
            employeeListModel.addElement("Employee " + i);
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
        positionField = new JTextField(20);
        editButton = new JButton("Edit Information");

        JButton addEmployeeButton = new JButton("Add Employee");
        JButton addManagerButton = new JButton("Add Manager");
        JButton removeEmployeeButton = new JButton("Remove Employee");
        JButton removeManagerButton = new JButton("Remove Manager");

        editingPanel.add(new JLabel("Name:"));
        editingPanel.add(nameField);
        editingPanel.add(new JLabel("Hours/Week:"));
        editingPanel.add(positionField);
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
                displayManagerDetails(selectedIndex);
            }
        });

        employeeList.addListSelectionListener(e -> {
            int selectedIndex = employeeList.getSelectedIndex();
            if (selectedIndex != -1) {
                displayEmployeeDetails(selectedIndex);
            }
        });

        editButton.addActionListener(e -> {
            // Handle editing employee or manager information
            int selectedIndex = managerList.getSelectedIndex();
            if (selectedIndex != -1) {
                updateManagerInformation(selectedIndex);
            } else {
                selectedIndex = employeeList.getSelectedIndex();
                if (selectedIndex != -1) {
                    updateEmployeeInformation(selectedIndex);
                }
            }
        });

        addEmployeeButton.addActionListener(e -> {
            // Handle adding a new employee
            String newName = nameField.getText();
            String newPosition = positionField.getText();
            addEmployee(newName, newPosition);
        });

        addManagerButton.addActionListener(e -> {
            // Handle adding a new manager
            String newName = nameField.getText();
            String newPosition = positionField.getText();
            addManager(newName, newPosition);
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

    private void displayManagerDetails(int index) {
        String managerDetails = managerListModel.get(index) + "\nInfo:\n";
        detailsTextArea.setText(managerDetails);
    }

    private void displayEmployeeDetails(int index) {
        String employeeDetails = employeeListModel.get(index) + "\nInfo:\n";
        detailsTextArea.setText(employeeDetails);
    }

    private void updateManagerInformation(int index) {
        String newName = nameField.getText();
        String newPosition = positionField.getText();
        String updatedDetails = "Name: " + newName + "\nHours/Week: " + newPosition;
        managerListModel.set(index, newName);
        detailsTextArea.setText(updatedDetails);

        //update database
        managerFunctions.updateEmployeeSQL(newName,newPosition);
    }

    private void updateEmployeeInformation(int index) {
        String newName = nameField.getText();
        String newPosition = positionField.getText();
        String updatedDetails = "Name: " + newName + "\nHours/Week: " + newPosition;
        employeeListModel.set(index, newName);
        detailsTextArea.setText(updatedDetails);

        //update database
        managerFunctions.updateEmployeeSQL(newName,newPosition);
    }


    private void addEmployee(String name, String position) {
        if (name.isEmpty()) {
            return; // Don't add if the name is empty
        }
        //update database
        managerFunctions.createNewEmployee(name,"password", "0.0", "0.0", position);

        String newEmployee = "Employee " + (employeeListModel.getSize() + 1) + "\nName: " + name + "\nPosition: " + position;
        employeeListModel.addElement(newEmployee);
        detailsTextArea.setText(newEmployee);
    }

    private void addManager(String name, String position) {
        if (name.isEmpty()) {
            return; // Don't add if the name is empty
        }
        //update database
        managerFunctions.createNewEmployee(name,"password", "0.0", "0.0", position);

        String newManager = "Manager " + (managerListModel.getSize() + 1) + "\nName: " + name + "\nPosition: " + position;
        managerListModel.addElement(newManager);
        detailsTextArea.setText(newManager);
    }

    private void removeEmployee(int index) {
        if (index >= 0 && index < employeeListModel.getSize()) {
            //update database
            managerFunctions.removeEmployeeSQL(employeeListModel.get(index));

            employeeListModel.remove(index);
            detailsTextArea.setText("Employee removed.");
        }
    }

    private void removeManager(int index) {
        if (index >= 0 && index < managerListModel.getSize()) {
            //update database
            managerFunctions.removeEmployeeSQL(managerListModel.get(index));

            managerListModel.remove(index);
            detailsTextArea.setText("Manager removed.");
        }
    }

    /*public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Employee Page");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 400);
            frame.add(new EmployeeApp());
            frame.setVisible(true);
        });
    }*/
}