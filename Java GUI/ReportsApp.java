import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.*;
import javax.swing.border.Border;

public class ReportsApp extends JPanel {
    private ManagerFunctions managerFunctions;
    private JPanel mainPanel;
    private JTextField startDateField;
    private JTextField endDateField;
    private JLabel messageLabel;
    private JPanel centeredPanel;

    public ReportsApp(ManagerFunctions m) {
        managerFunctions = m;
        setLayout(new BorderLayout());

        // Create the top panel with GridBagLayout
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; // Allow the buttons to take 50% each

        JButton excessButton = new JButton("Generate Excess Ingredients Report");
        JButton drinksButton = new JButton("Generate Popular Drink Pairs Report");

        excessButton.setBackground(new Color(255, 255, 255));
        drinksButton.setBackground(new Color(255, 255, 255));

        // Add action listeners to the buttons
        excessButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showExcessIngredientsForm();
            }
        });

        drinksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPopularDrinkPairsForm();
            }
        });

        // Add buttons to the top panel with GridBagLayout
        gbc.gridx = 0;
        gbc.weightx = 0.5;
        topPanel.add(excessButton, gbc);

        gbc.gridx = 1;
        topPanel.add(drinksButton, gbc);

        // Add the top panel to the main panel
        add(topPanel, BorderLayout.NORTH);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        centeredPanel = new JPanel(new GridBagLayout());
        messageLabel = new JLabel("Welcome to the Reports App.");
        centeredPanel.add(messageLabel);
        mainPanel.add(centeredPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void showExcessIngredientsForm() {
        // Replace the main panel content with a form
        mainPanel.removeAll();
        mainPanel.revalidate();
        mainPanel.repaint();

        // Create the form for entering a start date
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel startDateLabel = new JLabel("Enter Start Date (YYYY-MM-DD):");
        startDateField = new JTextField(10);
        JButton generateButton = new JButton("Generate Report");

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateExcessIngredientsReport();
            }
        });

        formPanel.add(startDateLabel);
        formPanel.add(startDateField);
        formPanel.add(generateButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showPopularDrinkPairsForm() {
        // Replace the main panel content with a form
        mainPanel.removeAll();

        // Create the form for entering start and end dates
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel startDateLabel = new JLabel("From (YYYY-MM-DD):");
        startDateField = new JTextField(10);
        JLabel endDateLabel = new JLabel("To (YYYY-MM-DD):");
        endDateField = new JTextField(10);
        JButton generateButton = new JButton("Generate Report");

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generatePopularDrinkPairsReport();
            }
        });

        formPanel.add(startDateLabel);
        formPanel.add(startDateField);
        formPanel.add(endDateLabel);
        formPanel.add(endDateField);
        formPanel.add(generateButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void generateExcessIngredientsReport() {
        centeredPanel.removeAll();

        String startDate = startDateField.getText();
        
        if (invalidDate(startDate)) {
            messageLabel.setText("ERROR: Please enter a valid start date (YYYY-MM-DD).");
            messageLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the label text
            messageLabel.setVerticalAlignment(SwingConstants.CENTER);
        } else {
            messageLabel.setText("Excess Ingredients Report generated for start date: " + startDate);
        }

        centeredPanel.add(messageLabel);
        centeredPanel.revalidate();
        centeredPanel.repaint();
    }

    private void generatePopularDrinkPairsReport() {
        centeredPanel.removeAll();

        String startDate = startDateField.getText();
        String endDate = endDateField.getText();
        
        if (invalidDate(startDate) || invalidDate(endDate)) {
            messageLabel.setText("ERROR: Please enter valid start and end dates (YYYY-MM-DD).");
            messageLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the label text
            messageLabel.setVerticalAlignment(SwingConstants.CENTER);
        } else {
            messageLabel.setText("Popular Drink Pairs Report generated from " + startDate + " to " + endDate);
        }

        centeredPanel.add(messageLabel);
        centeredPanel.revalidate();
        centeredPanel.repaint();
    }

    private boolean invalidDate(String date) {
        if(date.length() != 10) return true;
        for(int i = 0; i < date.length(); i++) {
            if(i == 4 || i == 7) {
                if(date.charAt(i) != '-' && date.charAt(i) != '/') return true;
            }
            else {
                if(!Character.isDigit(date.charAt(i))) return true;
            }
        }

        return false;
    }
}