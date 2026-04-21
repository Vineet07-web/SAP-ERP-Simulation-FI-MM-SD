import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.ArrayList;

public class SAP_Advanced_GUI {

    static HashMap<String, Integer> inventory = new HashMap<>();
    static ArrayList<String[]> finance = new ArrayList<>();

    public static void main(String[] args) {

        JFrame frame = new JFrame("SAP Easy Access");
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ----- MENU BAR -----
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("SAP Menu");
        menu.add(new JMenuItem("MM - Materials Management"));
        menu.add(new JMenuItem("SD - Sales & Distribution"));
        menu.add(new JMenuItem("FI - Financial Accounting"));
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);

        // ----- TABS -----
        JTabbedPane tabs = new JTabbedPane();

        // ===== MM TAB =====
        JPanel mmPanel = new JPanel(new GridLayout(5,2,10,10));
        JTextField matField = new JTextField();
        JTextField qtyField = new JTextField();
        JTextField priceField = new JTextField();

        mmPanel.add(new JLabel("Material"));
        mmPanel.add(matField);
        mmPanel.add(new JLabel("Quantity"));
        mmPanel.add(qtyField);
        mmPanel.add(new JLabel("Price"));
        mmPanel.add(priceField);

        JButton purchaseBtn = new JButton("Create Purchase Order");
        JLabel mmOutput = new JLabel("");

        purchaseBtn.addActionListener(e -> {
            try {
                String mat = matField.getText();
                int qty = Integer.parseInt(qtyField.getText());
                double price = Double.parseDouble(priceField.getText());

                inventory.put(mat, inventory.getOrDefault(mat,0)+qty);
                finance.add(new String[]{"Purchase", String.valueOf(qty*price), "Debit"});

                mmOutput.setText("PO Created | Stock: " + mat + " = " + inventory.get(mat));
            } catch(Exception ex){
                mmOutput.setText("Invalid Input!");
            }
        });

        mmPanel.add(purchaseBtn);
        mmPanel.add(mmOutput);

        tabs.add("MM", mmPanel);

        // ===== SD TAB =====
        JPanel sdPanel = new JPanel(new GridLayout(5,2,10,10));
        JTextField sMat = new JTextField();
        JTextField sQty = new JTextField();
        JTextField sPrice = new JTextField();

        sdPanel.add(new JLabel("Material"));
        sdPanel.add(sMat);
        sdPanel.add(new JLabel("Quantity"));
        sdPanel.add(sQty);
        sdPanel.add(new JLabel("Price"));
        sdPanel.add(sPrice);

        JButton salesBtn = new JButton("Create Sales Order");
        JLabel sdOutput = new JLabel("");

        salesBtn.addActionListener(e -> {
            try {
                String mat = sMat.getText();
                int qty = Integer.parseInt(sQty.getText());
                double price = Double.parseDouble(sPrice.getText());

                if(inventory.getOrDefault(mat,0) < qty){
                    sdOutput.setText("Not enough stock!");
                    return;
                }

                inventory.put(mat, inventory.get(mat)-qty);
                finance.add(new String[]{"Sale", String.valueOf(qty*price), "Credit"});

                sdOutput.setText("Sale Done | Stock: " + mat + " = " + inventory.get(mat));
            } catch(Exception ex){
                sdOutput.setText("Invalid Input!");
            }
        });

        sdPanel.add(salesBtn);
        sdPanel.add(sdOutput);

        tabs.add("SD", sdPanel);

        // ===== FI TAB =====
        JPanel fiPanel = new JPanel(new BorderLayout());

        String[] columns = {"Type", "Amount", "Dr/Cr"};
        DefaultTableModel model = new DefaultTableModel(columns,0);
        JTable table = new JTable(model);

        JButton refreshBtn = new JButton("Refresh Data");

        refreshBtn.addActionListener(e -> {
            model.setRowCount(0);
            for(String[] row : finance){
                model.addRow(row);
            }
        });

        fiPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        fiPanel.add(refreshBtn, BorderLayout.SOUTH);

        tabs.add("FI", fiPanel);

        // ----- ADD TABS -----
        frame.add(tabs);
        frame.setVisible(true);
    }
}