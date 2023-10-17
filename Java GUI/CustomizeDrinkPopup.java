import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
/*
Customize drink popup page on GUI
@author Kevin Tang
@author Dicong Wang
*/
public class CustomizeDrinkPopup extends JDialog {
    private JLabel titleLabel;
    private JButton cancelButton;
    private JButton addButton;
    private JComboBox<String> iceComboBox;
    private JComboBox<String> sweetnessComboBox;
    private JCheckBox[] toppingsCheckboxes;

    private String selectedIce;
    private String selectedSweetness;
    private ArrayList<String> selectedToppings;

    private Order order;
    /*
    @function constructor for the customization page
    @param parent to reference original frame of GUI
    @param drinkName to include information about drink
    @param o order to include usage of functions
    @return none
    @throws none
    */
    public CustomizeDrinkPopup(JFrame parent, String drinkName, Order o) {

        super(parent, "Customize Drink", true);

        order = o;
        setLayout(new BorderLayout());

        // Create the header section with title, cancel, and add buttons
        JPanel headerPanel = new JPanel(new BorderLayout());
        titleLabel = new JLabel(drinkName, JLabel.CENTER);
        cancelButton = new JButton("Cancel");
        addButton = new JButton("Add");

        // Create the body section with options for Ice, Sweetness, and Toppings
        JPanel bodyPanel = new JPanel(new GridLayout(3, 1));

        // Ice options
        JPanel icePanel = new JPanel();
        icePanel.add(new JLabel("Ice: "));
        String[] iceOptions = { "Regular Ice", "No Ice", "Light Ice", "Extra Ice" };
        iceComboBox = new JComboBox<>(iceOptions);
        icePanel.add(iceComboBox);

        // Sweetness options
        JPanel sweetnessPanel = new JPanel();
        sweetnessPanel.add(new JLabel("Sweetness: "));
        String[] sweetnessOptions = { "100% sugar", "80% sugar", "50% sugar", "30% sugar", "0% sugar" };
        sweetnessComboBox = new JComboBox<>(sweetnessOptions);
        sweetnessPanel.add(sweetnessComboBox);

        // Toppings options
        JPanel toppingsPanel = new JPanel();
        toppingsPanel.add(new JLabel("Toppings: "));
        String[] toppingOptions = { "Pearl", "Mini Pearl", "Ice Cream", "Pudding", "Aloe Vera", "Red Bean",
                "Herb Jelly", "Aiyu Jelly", "Lychee Jelly", "Creama", "Crystal Boba" };
        toppingsCheckboxes = new JCheckBox[toppingOptions.length];
        for (int i = 0; i < toppingOptions.length; i++) {
            toppingsCheckboxes[i] = new JCheckBox(toppingOptions[i]);
            toppingsPanel.add(toppingsCheckboxes[i]);
        }

        bodyPanel.add(icePanel);
        bodyPanel.add(sweetnessPanel);
        bodyPanel.add(toppingsPanel);

        // Add components to the header and body sections
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(cancelButton, BorderLayout.WEST);
        headerPanel.add(addButton, BorderLayout.EAST);

        // Add action listeners for the cancel and add buttons
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the pop-up without adding the drink
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected options for Ice, Sweetness, and Toppings
                selectedIce = iceComboBox.getSelectedItem().toString();
                selectedSweetness = sweetnessComboBox.getSelectedItem().toString();
                selectedToppings = new ArrayList<String>();

                for (int i = 0; i < toppingOptions.length; i++) {
                    if (toppingsCheckboxes[i].isSelected()) {
                        selectedToppings.add(toppingOptions[i]);
                    }
                }

                // Close the pop-up
                dispose();

                // make update to database
                boolean updatedInventory = order.updateInventory(drinkName, selectedIce, selectedSweetness,
                        selectedToppings);

                if (!updatedInventory) {
                    JOptionPane.showMessageDialog(CustomizeDrinkPopup.this,
                        "Sorry, we don't have enough resources to make this drink. Please choose another option.",
                        "Inventory Low", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Set the layout and add components to the pop-up window
        add(headerPanel, BorderLayout.NORTH);
        add(bodyPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }
    /*
    @function Get function to get the selected ice string
    @param none
    @return String of the user-selected ice amount
    @throws none
    */
    public String getSelectedIce() {
        return selectedIce;
    }
    /*
    @function Get function to get the selected sweetness
    @param none
    @return String of the user-selected sweetness level
    @throws none
    */
    public String getSelectedSweetness() {
        return selectedSweetness;
    }
    /*
    @function Get function to return an arraylist of the toppings selected
    @param none
    @return array of strings of selected toppings
    @throws none
    */
    public ArrayList<String> getSelectedToppings() {
        return selectedToppings;
    }
}