import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.*;
import javax.swing.border.Border;

/** 
This class sets up the Reports page on the manager side 
@author Kevin Tang
@author Dicong Wang
*/
public class ReportsApp extends JPanel {
    private ManagerFunctions managerFunctions;
    private JPanel mainPanel;
    private JLabel startDateLabel;
    private JTextField startDateField;
    private JLabel endDateLabel;
    private JTextField endDateField;
    private JButton generateButton;
    private JLabel messageLabel;
    private JPanel centeredPanel;
    private JLabel excessIngredients;

    /** 
    @function Constructor to set up the reports page
    @param m to allow usage of managerFunctions
    @throws none

    */
    public ReportsApp(ManagerFunctions m) {
        excessIngredients = new JLabel();

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

    /** 
    @function sets up GUI to show the excess ingredients information page
    @param none
    @return void
    @throws none
    */
    private void showExcessIngredientsForm() {
        // Replace the main panel content with a form
        mainPanel.removeAll();
        mainPanel.revalidate();
        mainPanel.repaint();

        centeredPanel.removeAll();
        centeredPanel.revalidate();
        centeredPanel.repaint(); 

        JLabel startDateLabel = new JLabel("Enter Start Date (YYYY-MM-DD):");
        startDateField = new JTextField(10);
        JButton generateButton = new JButton("Generate Report");

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateExcessIngredientsReport();
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0); // Add some vertical spacing between components

        gbc.gridx = 0;
        gbc.gridy = 0;
        centeredPanel.add(startDateLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        centeredPanel.add(startDateField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        centeredPanel.add(generateButton, gbc);

        centeredPanel.add(startDateLabel);
        centeredPanel.add(startDateField);
        centeredPanel.add(generateButton);

        mainPanel.add(centeredPanel, BorderLayout.NORTH);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /** 
    @function information fill for the report of excess ingredients
    @param none
    @return void
    @throws none

    */
    private void generateExcessIngredientsReport() {
        //centeredPanel.removeAll();

        String startDate = startDateField.getText();
        String currentText = "<html>";
        if (invalidDate(startDate)) {
            excessIngredients.setText("ERROR: Please enter a valid start date (YYYY-MM-DD).");
            excessIngredients.setHorizontalAlignment(SwingConstants.CENTER); // Center the label text
            excessIngredients.setVerticalAlignment(SwingConstants.CENTER);
        } else {
            //messageLabel.setText("Excess Ingredients Report generated for start date: " + startDate);
            ArrayList<String> ingredients = managerFunctions.getExcessReport(startDate);
            for (String ingredient : ingredients){
                currentText += "<br>" + ingredient;
            }
            currentText += "</html>";
            excessIngredients.setText(currentText);
        }

        //centeredPanel.add(messageLabel);
        // Create GridBagConstraints for centeredPanel components
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0); // Add some vertical spacing between components

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3; // Span three columns
        centeredPanel.add(excessIngredients, gbc);
        centeredPanel.revalidate();
        centeredPanel.repaint();
    }

    /** 
    @function sets up GUI to show the popular drink pairs page
    @param none
    @return void
    @throws none
    */
    private void showPopularDrinkPairsForm() {
        // Replace the main panel content with a form
        mainPanel.removeAll();
        mainPanel.revalidate(); // Add this line to revalidate the mainPanel
        mainPanel.repaint(); 

        centeredPanel.removeAll();
        centeredPanel.revalidate();
        centeredPanel.repaint(); 

        startDateLabel = new JLabel("From (YYYY-MM-DD):");
        startDateField = new JTextField(10);
        endDateLabel = new JLabel("To (YYYY-MM-DD):");
        endDateField = new JTextField(10);
        generateButton = new JButton("Generate Report");

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generatePopularDrinkPairsReport();
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0); // Add some vertical spacing between components

        gbc.gridx = 0;
        gbc.gridy = 0;
        centeredPanel.add(startDateLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        centeredPanel.add(startDateField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        centeredPanel.add(endDateLabel, gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        centeredPanel.add(endDateField, gbc);

        gbc.gridx = 4;
        gbc.gridy = 0;
        centeredPanel.add(generateButton, gbc); 

        centeredPanel.add(startDateLabel);
        centeredPanel.add(startDateField);
        centeredPanel.add(endDateLabel);
        centeredPanel.add(endDateField);
        centeredPanel.add(generateButton);

        mainPanel.add(centeredPanel, BorderLayout.NORTH);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /** 
    @function generates the report information for popular drink pairs
    @param none
    @return void
    @throws none
    */
    private void generatePopularDrinkPairsReport() {
        //centeredPanel.removeAll();

        String startDate = startDateField.getText();
        String endDate = endDateField.getText();

        String currentText = "<html>";
        
        if (invalidDate(startDate) || invalidDate(endDate)) {
            excessIngredients.setText("ERROR: Please enter valid start and end dates (YYYY-MM-DD).");
            excessIngredients.setHorizontalAlignment(SwingConstants.CENTER); // Center the label text
            excessIngredients.setVerticalAlignment(SwingConstants.CENTER);
        } else {
            //excessIngredients.setText("Popular Drink Pairs Report generated from " + startDate + " to " + endDate);
            HashMap<ArrayList<String>, Integer> map = managerFunctions.getWhatSalesTogether(startDate, endDate);
            java.util.List<Map.Entry<ArrayList<String>, Integer>> list = new ArrayList<>(map.entrySet());

            Collections.sort(list, new Comparator<Map.Entry<ArrayList<String>, Integer>>() {
                @Override
                public int compare(Map.Entry<ArrayList<String>, Integer> o1, Map.Entry<ArrayList<String>, Integer> o2) {
                    // Compare in descending order
                    return o2.getValue().compareTo(o1.getValue());
                }
            });

            // Add headers for the two columns
            currentText += "<table>";
            currentText += "<tr><th>Drink Pair</th><th>Order Count</th></tr>";

            for (Map.Entry<ArrayList<String>, Integer> entry : list) {
                // Add a row with drink pair and order count
                currentText += "<tr><td>" + entry.getKey().get(0) + ", " + entry.getKey().get(1) + "</td><td>" + entry.getValue() + "</td></tr>";
            }

            currentText += "</table>";

            currentText += "</html>";
            excessIngredients.setText(currentText);
        }

        // Create a JScrollPane and set the preferred size
        JScrollPane scrollPane = new JScrollPane(excessIngredients);
        scrollPane.setPreferredSize(new Dimension(400, 300)); // Adjust the dimensions as needed

        // Create a panel for the date form
        JPanel dateFormPanel = new JPanel();
        dateFormPanel.add(startDateLabel);
        dateFormPanel.add(startDateField);
        dateFormPanel.add(endDateLabel);
        dateFormPanel.add(endDateField);
        dateFormPanel.add(generateButton);

        // Create a panel for the scroll pane
        JPanel scrollPanel = new JPanel(new BorderLayout());
        scrollPanel.add(dateFormPanel, BorderLayout.NORTH);
        scrollPanel.add(scrollPane, BorderLayout.CENTER);

        // Replace the centeredPanel content with the scrollPanel
        mainPanel.removeAll();
        mainPanel.revalidate();
        mainPanel.repaint();
        mainPanel.add(scrollPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /** 
    @function checks if date entered is valid
    @param date string of the entered date
    @return boolean true if the date is invalid
    @throws none

    */
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