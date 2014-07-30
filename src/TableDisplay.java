import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 * This creates a 'popup' dialog object to display all the records in the DB in
 * one location.
 *
 * @author chrcoe
 *
 */
public class TableDisplay extends JDialog {
    private static final long serialVersionUID = 4563906062226166225L;

    private JTable table;
    private JScrollPane scrollPane;
    private JPanel tablePane;

    /**
     * Constructs the TableDisplay with all records.
     * @param allRecords all records in the DB provided by DBConnection.getAllRecords()
     */
    public TableDisplay(ArrayList<MediaInfo> allRecords) {
        setTitle("DBManager - All Records");
        tablePane = new JPanel(new BorderLayout());

        // Create columns names
        String columnNames[] = { "Name", "Year", "Comments", "Current Value",
                "Genre", "Media Type", "Purchase Location", "Purchase Price",
                "Purchase Date" };

        // [row][col]
        String data[][] = new String[allRecords.size()][columnNames.length];

//        ArrayList<String> rowData = new ArrayList<String>();

        int rowCount = 0;
        for (MediaInfo m : allRecords) {
            data[rowCount][0] = m.getName();
            data[rowCount][1] = String.valueOf(m.getYear());
            data[rowCount][2] = m.getComments();
            data[rowCount][3] = String.format("%.2f", m.getCurrentValue());
            data[rowCount][4] = m.getGenre();
            data[rowCount][5] = m.getMediaType();
            data[rowCount][6] = m.getPurchaseLoc();
            data[rowCount][7] = String.format("%.2f", m.getPurchasePrice());
            data[rowCount][8] = DBFrame.DF.format(m.getPurchaseDate());
            rowCount++;
        }

        table = new JTable(data, columnNames);
        scrollPane = new JScrollPane(table);
        tablePane.add(scrollPane);
        add(tablePane, BorderLayout.CENTER);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(800,400));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
