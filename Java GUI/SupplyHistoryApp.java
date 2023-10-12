import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SupplyHistoryApp extends JPanel {
    public SupplyHistoryApp() {
        setLayout(new BorderLayout());

        // Create the center panel with a scrollable list of rows
        JPanel centerPanel = new JPanel(new BorderLayout());

        // Create a scroll pane to hold the supply history rows
        JScrollPane scrollPane = new JScrollPane();
        JPanel rowsPanel = new JPanel();
        rowsPanel.setLayout(new BoxLayout(rowsPanel, BoxLayout.Y_AXIS));

        // Simulate a list of supply history rows
        for (int i = 1; i <= 50; i++) {
            JPanel row = new JPanel();
            row.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            row.setPreferredSize(new Dimension(400, 40));

            JLabel rowLabel = new JLabel("Supply Order #" + i);
            JButton detailsButton = new JButton("Details");

            row.add(rowLabel);
            row.add(detailsButton);

            detailsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Display supply order details in a dropdown menu
                    showSupplyDetails(rowLabel.getText());
                }
            });

            rowsPanel.add(row);
        }

        scrollPane.setViewportView(rowsPanel);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void showSupplyDetails(String supplyOrder) {
        /* TODO ****************************************************************************************************************************/
        // details are simply what was ordered and the amount
        // Implement the logic to display supply order details in a dropdown menu
        JOptionPane.showMessageDialog(this, "Supply Order Details: " + supplyOrder, "Supply Order Details", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Supply History Page");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 400);
            frame.add(new SupplyHistoryApp());
            frame.setVisible(true);
        });
    }
}