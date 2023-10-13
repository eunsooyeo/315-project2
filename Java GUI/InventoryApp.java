import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InventoryApp extends JPanel {
    public InventoryApp() {
        setLayout(new BorderLayout());

        // Create the center panel with a grid of 41 ingredients/items
        JPanel centerPanel = new JPanel(new GridLayout(6, 7));
        String[] items = {
            "Aiyu Jelly", "Aloe Vera", "Black Tea", "Brown Sugar", "Cocoa", "Coffee", "Creama",
            "Creamer", "Crystal Boba", "Cups", "Fructose", "Grapefruit", "Green Tea", "Herb Jelly",
            "Honey", "Ice", "Ice Cream", "Lime", "Lychee Jelly", "Mango", "Matcha", "Milk", "Mint",
            "Napkins", "Okinawa", "Oolong Tea", "Orange", "Passionfruit", "Peach", "Pearls", "Mini Pearls",
            "Pineapple", "Plastic Cover", "Pudding", "Red Bean", "Strawberry", "Straws", "Taro", "Water",
            "White Sugar", "Wintermelon"
        };

        for (String item : items) {
            JButton itemButton = new JButton(item);
            itemButton.setBackground(Color.GRAY);
            itemButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    /* TODO ****************************************************************************************************************************/
                    // Display item details in the right sidebar
                    // Make its contents editable after retrieveing from Database
                }
            });
            centerPanel.add(itemButton);
        }

        // Create the right sidebar for displaying item details
        JPanel rightSidebar = new JPanel();
        rightSidebar.setLayout(new BorderLayout());
        JTextArea detailsTextArea = new JTextArea("Details will be displayed here.");
        rightSidebar.add(detailsTextArea, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
        add(rightSidebar, BorderLayout.EAST);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Inventory Page");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 400);
            frame.add(new InventoryApp());
            frame.setVisible(true);
        });
    }
}