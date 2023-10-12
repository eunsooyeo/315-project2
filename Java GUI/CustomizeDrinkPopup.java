import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomizeDrinkPopup extends JDialog {
    private JLabel titleLabel;
    private JButton cancelButton;
    private JButton addButton;
    private JComboBox<String> iceComboBox;
    private JComboBox<String> sweetnessComboBox;
    private JCheckBox[] toppingsCheckboxes;

    private String selectedIce;
    private String selectedSweetness;
    private String[] selectedToppings;

    private Order order;

    public CustomizeDrinkPopup(JFrame parent, String drinkName, Order o) {
        order = o;
        
        super(parent, "Customize Drink", true);
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
        String[] iceOptions = {"Regular Ice", "No Ice", "Light Ice", "Extra Ice"};
        iceComboBox = new JComboBox<>(iceOptions);
        icePanel.add(iceComboBox);

        // Sweetness options
        JPanel sweetnessPanel = new JPanel();
        sweetnessPanel.add(new JLabel("Sweetness: "));
        String[] sweetnessOptions = {"0% sugar", "30% sugar", "50% sugar", "80% sugar", "100% sugar"};
        sweetnessComboBox = new JComboBox<>(sweetnessOptions);
        sweetnessPanel.add(sweetnessComboBox);

        // Toppings options
        JPanel toppingsPanel = new JPanel();
        toppingsPanel.add(new JLabel("Toppings: "));
        String[] toppingOptions = {"Pearl", "Mini Pearl", "Ice Cream", "Pudding", "Aloe Vera", "Red Bean",
                "Herb Jelly", "Aiyu Jelly", "Lychee Jelly", "Creama", "Crystal Boba"};
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
                selectedToppings = new String[toppingOptions.length];
                for (int i = 0; i < toppingOptions.length; i++) {
                    selectedToppings[i] = toppingsCheckboxes[i].isSelected() ? toppingOptions[i] : null;
                }

                // Close the pop-up
                dispose();
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

    public String getSelectedIce() {
        return selectedIce;
    }

    public String getSelectedSweetness() {
        return selectedSweetness;
    }

    public String[] getSelectedToppings() {
        return selectedToppings;
    }
}