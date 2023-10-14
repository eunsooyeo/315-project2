import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SupplyHistoryApp extends JPanel {

    private ManagerFunctions managerFunctions;

    public SupplyHistoryApp(ManagerFunctions m) {
        managerFunctions = m;
        setLayout(new BorderLayout());

        // Create the center panel with a scrollable list of rows
        JPanel centerPanel = new JPanel(new BorderLayout());

        // Create a scroll pane to hold the supply history rows
        JScrollPane scrollPane = new JScrollPane();
        JPanel rowsPanel = new JPanel();
        rowsPanel.setLayout(new BoxLayout(rowsPanel, BoxLayout.Y_AXIS));

        // Simulate a list of supply history rows
        ArrayList<String> supplyHistory = managerFunctions.getAllSupplyHistory();

        for (int i = 0; i < supplyHistory.size(); i++) {
            JPanel row = new JPanel();
            row.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            row.setPreferredSize(new Dimension(400, 40));

            JLabel rowLabel = new JLabel(supplyHistory.get(i).substring(0, supplyHistory.get(i).indexOf("{")));
            JButton detailsButton = new JButton("Details");

            String details = supplyHistory.get(i).substring(supplyHistory.get(i).indexOf("{"));

            row.add(rowLabel);
            row.add(detailsButton);

            detailsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Display supply order details in a dropdown menu
                    showSupplyDetails(rowLabel.getText(), details);
                }
            });

            rowsPanel.add(row);
        }

        scrollPane.setViewportView(rowsPanel);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void showSupplyDetails(String row, String supplyOrder) {
        
        String[] lines = supplyOrder.split("\n");
        String details = "Payment: $" + lines[lines.length - 1] + "\n";

        String[] supplies = lines[0].substring(1, lines[0].length() - 1).split(",");
        String[] amounts = lines[1].substring(1, lines[1].length() - 1).split(",");

        for(int i = 0; i < supplies.length; i++) {
            String unit = managerFunctions.getUnitOfIngredient(supplies[i]);
            if(unit.equals("units")) unit = supplies[i].toLowerCase();

            details += "- " + supplies[i] + ": " + amounts[i] + unit;
            if(i != supplies.length - 1) details += "\n";
        }

        String trackingNum = row.substring(row.indexOf("#"));
        trackingNum = trackingNum.substring(0, trackingNum.indexOf(" "));
        trackingNum = "    " + trackingNum;

        JOptionPane.showMessageDialog(this, details, row.substring(0, row.indexOf(" ")) + trackingNum, JOptionPane.INFORMATION_MESSAGE);
    }

    /* public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Supply History Page");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 400);
            frame.add(new SupplyHistoryApp());
            frame.setVisible(true);
        });
    } */
}