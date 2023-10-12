import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EmployeeApp extends JPanel {
    private JTextArea detailsTextArea;

    public EmployeeApp() {
        setLayout(new BorderLayout());

        // Create the center panel with sections for managers and employees
        JPanel centerPanel = new JPanel(new GridLayout(2, 1));

        // Create the managers section with horizontal scrollbar
        JPanel managersSection = new JPanel();
        managersSection.setLayout(new BoxLayout(managersSection, BoxLayout.X_AXIS));

        DefaultListModel<String> managerListModel = new DefaultListModel<>();
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

        DefaultListModel<String> employeeListModel = new DefaultListModel<>();
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
        rightSidebar.add(new JScrollPane(detailsTextArea), BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
        add(rightSidebar, BorderLayout.EAST);

        // Add action listeners to display details
        managerList.addListSelectionListener(e -> {
            int selectedIndex = managerList.getSelectedIndex();
            if (selectedIndex != -1) {
                String managerDetails = "Details for " + managerList.getSelectedValue() + "\nAdd more details here.\n";
                detailsTextArea.setText(managerDetails);
            }
        });

        employeeList.addListSelectionListener(e -> {
            int selectedIndex = employeeList.getSelectedIndex();
            if (selectedIndex != -1) {
                String employeeDetails = "Details for " + employeeList.getSelectedValue() + "\nAdd more details here.\n";
                detailsTextArea.setText(employeeDetails);
            }
        });
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
