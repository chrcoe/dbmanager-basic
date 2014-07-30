import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.*;
import javax.swing.border.BevelBorder;

/**
 * Controls the major GUI frame.
 *
 * @author chrcoe
 *
 */
public class DBFrame extends JFrame {
    private static final long serialVersionUID = -8509684737806048324L;
    // connection to db, contains helper methods
    public DBConnection dbc;
    // button panels
    private JPanel topPane;
    private JPanel inputPane;
    private JPanel bottomPane;
    // buttons used
    private JButton create;
    private JButton remove;
    private JButton save;
    private JButton clear;
    private JButton display;
    private JButton prev;
    private JButton search;
    private JButton next;
    private JButton exit;
    // labels used
    private JLabel name;
    private JLabel year;
    private JLabel comments;
    private JLabel currentValue;
    private JLabel genre;
    private JLabel mediaType;
    private JLabel purchaseLoc;
    private JLabel purchasePrice;
    private JLabel purchaseDate;
    // text fields used
    private JTextField jtfName;
    private JTextField jtfYear;
    private JTextField jtfComments;
    private JTextField jtfCurrentValue;
    // private JTextField jtfGenre;
    // private JTextField jtfMediaType;
    // private JTextField jtfPurchaseLoc;
    private JTextField jtfPurchasePrice;
    private JTextField jtfPurchaseDate;

    // experimental comboboxes
    private JComboBox<String> genreBox;
    private JComboBox<String> typeBox;
    private JComboBox<String> locBox;

    private int currentRecord;
    private ArrayList<MediaInfo> mediaItemList;

    public static DateFormat DF = new SimpleDateFormat("MM/dd/yyyy");

    /**
     * Creates the main frame for the GUI.
     */
    public DBFrame() {
        ClickListener listener = new ClickListener();

        // calls the connection to the DB
        dbc = new DBConnection();

        mediaItemList = new ArrayList<MediaInfo>();

        currentRecord = -1;

        // add a panel for buttons
        topPane = new JPanel(new FlowLayout());
        topPane.setBorder(new BevelBorder(BevelBorder.RAISED));
        topPane.setPreferredSize(new Dimension(400, 68));

        // instantiate top buttons
        create = new JButton("Add Record");
        save = new JButton("Update Record");
        remove = new JButton("Delete Record");
        clear = new JButton("Clear Fields");
        display = new JButton("Display All");

        create.addActionListener(listener);
        save.addActionListener(listener);
        remove.addActionListener(listener);
        clear.addActionListener(listener);
        display.addActionListener(listener);

        topPane.add(create);
        topPane.add(save);
        topPane.add(remove);
        topPane.add(clear);
        topPane.add(display);

        // add panel for labels and text fields
        inputPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        // instantiate labels
        name = new JLabel("Name: ");
        year = new JLabel("Year: ");
        comments = new JLabel("Comments: ");
        currentValue = new JLabel("Value ($):");
        genre = new JLabel("Genre: ");
        mediaType = new JLabel("Media Type: ");
        purchaseLoc = new JLabel("Purchase Location: ");
        purchasePrice = new JLabel("Purchase Price: ");
        purchaseDate = new JLabel("Purchase Date (mm/dd/yyyy): ");
        // instantiate text fields
        jtfName = new JTextField(28);
        jtfYear = new JTextField(28);
        jtfComments = new JTextField(28);
        jtfCurrentValue = new JTextField(28);
        // jtfGenre = new JTextField(32);
        // jtfMediaType = new JTextField(32);
        // jtfPurchaseLoc = new JTextField(32);
        jtfPurchasePrice = new JTextField(15);
        jtfPurchaseDate = new JTextField(15);

        String[] genreItems = { "", "Science Fiction", "Horror", "Comedy",
                "Country", "Alternative", "Pop", "Dance", "Rock", "Action",
                "Childrens", "Fantasy" };
        String[] mediaTypes = { "", "CD", "DVD", "BLU-RAY" };
        String[] locations = { "", "Best Buy", "Micro Center", "Walmart",
                "Amazon", "eBay", "Target" };
        genreBox = new JComboBox<>(genreItems);
        typeBox = new JComboBox<>(mediaTypes);
        locBox = new JComboBox<>(locations);

        // add these to the input pane
        JPanel namePane = new JPanel(new BorderLayout());
        namePane.add(name, BorderLayout.WEST);
        namePane.add(jtfName, BorderLayout.EAST);
        JPanel yearPane = new JPanel(new BorderLayout());
        yearPane.add(year, BorderLayout.WEST);
        yearPane.add(jtfYear, BorderLayout.EAST);
        JPanel commentPane = new JPanel(new BorderLayout());
        commentPane.add(comments, BorderLayout.WEST);
        commentPane.add(jtfComments, BorderLayout.EAST);
        JPanel valuePane = new JPanel(new BorderLayout());
        valuePane.add(currentValue, BorderLayout.WEST);
        valuePane.add(jtfCurrentValue, BorderLayout.EAST);
        //
        JPanel genrePane = new JPanel(new BorderLayout());
        genrePane.add(genreBox);
        genrePane.add(genre, BorderLayout.WEST);
        // genrePane.add(jtfGenre, BorderLayout.EAST);
        //
        JPanel mediaTypePane = new JPanel(new BorderLayout());
        mediaTypePane.add(typeBox);
        mediaTypePane.add(mediaType, BorderLayout.WEST);
        // mediaTypePane.add(jtfMediaType, BorderLayout.EAST);
        JPanel purchaseLocPane = new JPanel(new BorderLayout());
        purchaseLocPane.add(locBox);
        purchaseLocPane.add(purchaseLoc, BorderLayout.WEST);
        // purchaseLocPane.add(jtfPurchaseLoc, BorderLayout.EAST);
        JPanel purchasePricePane = new JPanel(new BorderLayout());
        purchasePricePane.add(purchasePrice, BorderLayout.WEST);
        purchasePricePane.add(jtfPurchasePrice, BorderLayout.EAST);
        JPanel purchaseDatePane = new JPanel(new BorderLayout());
        purchaseDatePane.add(purchaseDate, BorderLayout.WEST);
        purchaseDatePane.add(jtfPurchaseDate, BorderLayout.EAST);

        inputPane.add(namePane);
        inputPane.add(yearPane);
        inputPane.add(commentPane);
        inputPane.add(valuePane);
        inputPane.add(genrePane);
        inputPane.add(mediaTypePane);
        inputPane.add(purchaseLocPane);
        inputPane.add(purchasePricePane);
        inputPane.add(purchaseDatePane);

        // add bottom pane for left, search, right, exit
        bottomPane = new JPanel(new FlowLayout());
        bottomPane.setBorder(new BevelBorder(BevelBorder.LOWERED));

        prev = new JButton("Previous");
        search = new JButton("Search by Name");
        next = new JButton("Next");
        exit = new JButton("Exit");

        prev.addActionListener(listener);
        search.addActionListener(listener);
        next.addActionListener(listener);
        exit.addActionListener(listener);

        bottomPane.add(prev);
        bottomPane.add(search);
        bottomPane.add(next);
        bottomPane.add(exit);

        // add panel to frame
        setLayout(new BorderLayout());
        add(topPane, BorderLayout.NORTH);
        add(inputPane, BorderLayout.CENTER);
        add(bottomPane, BorderLayout.SOUTH);
    }

    /**
     * Inner class to control actions when each of the buttons are pressed.
     * @author chrcoe
     *
     */
    class ClickListener implements ActionListener {

        /**
         * Determines what to do when each of the buttons are pressed.
         *
         * @param e the object that was pressed
         */
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == create) {
                createRecord();
                clear();
            }
            else if (e.getSource() == remove) {
                removeRecord();
                clear();
            }
            else if (e.getSource() == save) {
                saveRecord();
                clear();
            }
            else if (e.getSource() == search) {
                search();
            }
            else if (e.getSource() == next) {
                nextResult();
            }
            else if (e.getSource() == prev) {
                prevResult();
            }
            else if (e.getSource() == clear) {
                clear();
            }
            else if (e.getSource() == display) {
                clear();
                new TableDisplay(dbc.getAllRecords());
            }
            else if (e.getSource() == exit) {
                int close = JOptionPane
                        .showConfirmDialog(null,
                                "Are you sure you want to exit?",
                                "End Application?", JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE);
                if (close == 1)
                    return;
                dbc.closeConnection();
                System.exit(0);
            }
        }
    }

    /**
     * Sets the fields in the GUI based on the values of the params.
     *
     * @param name The name field
     * @param year year field
     * @param comments comments field
     * @param currentValue currentValue field
     * @param genre from dropdown box
     * @param mediaType from the dropdown box
     * @param purchaseLoc from the dropdown box
     * @param purchasePrice purchase price field
     * @param purchaseDate date object based on text in the date field
     */
    private void setFields(String name, int year, String comments,
            double currentValue, String genre, String mediaType,
            String purchaseLoc, double purchasePrice, Date purchaseDate) {

        jtfName.setText(name);
        jtfYear.setText(String.valueOf(year));
        jtfComments.setText(comments);
        jtfCurrentValue.setText(String.format("%.2f", currentValue));
        // jtfGenre.setText(genre);
        //
        genreBox.setSelectedItem(genre);
        //
        typeBox.setSelectedItem(mediaType);
        // jtfMediaType.setText(mediaType);
        locBox.setSelectedItem(purchaseLoc);
        // jtfPurchaseLoc.setText(purchaseLoc);
        jtfPurchasePrice.setText(String.format("%.2f", purchasePrice));
        jtfPurchaseDate.setText(DF.format(purchaseDate));

    }

    /**
     * Clears the GUI fields and sets the drop down boxes back to defaults.
     */
    private void clear() {
        jtfName.setText("");
        jtfYear.setText("");
        jtfComments.setText("");
        jtfCurrentValue.setText("");
        // jtfGenre.setText("");
        //
        genreBox.setSelectedIndex(0);
        //
        typeBox.setSelectedIndex(0);
        // jtfMediaType.setText("");
        locBox.setSelectedIndex(0);
        // jtfPurchaseLoc.setText("");
        jtfPurchasePrice.setText("");
        jtfPurchaseDate.setText("");

        next.setEnabled(true);
        prev.setEnabled(true);
        currentRecord = -1;
        mediaItemList.clear();
    }

    /**
     * Calls the DB backend method to search for records based on the name field.
     * @return the media object
     */
    private MediaInfo search() {

        mediaItemList.clear();
        currentRecord = 0;

        MediaInfo result = null;

        String name = jtfName.getText();

        if (name.equals("")) {
            JOptionPane.showMessageDialog(null,
                    "Please enter a name to search for.");
        }
        else {
            mediaItemList = dbc.findRecord(name);

            if (mediaItemList.size() == 0) {
                JOptionPane.showMessageDialog(null, "No records found.");
                // Perform a clear if no records are found.
                clear();
            }
            else {
                result = mediaItemList.get(currentRecord);

                setFields(result.getName(), result.getYear(),
                        result.getComments(), result.getCurrentValue(),
                        result.getGenre(), result.getMediaType(),
                        result.getPurchaseLoc(), result.getPurchasePrice(),
                        result.getPurchaseDate());
            }
        }
        return result;
    }

    /**
     * Goes through the result list and shows the next record in the GUI.
     */
    private void nextResult() {
        currentRecord++;

        // mediaItemList is populated from the search button
        if (currentRecord >= mediaItemList.size()) {
            JOptionPane.showMessageDialog(null,
                    "You have reached the end of search results");
            next.setEnabled(false);
            prev.setEnabled(true);
            currentRecord--;
        }
        else {
            prev.setEnabled(true);
            MediaInfo result = mediaItemList.get(currentRecord);

            setFields(result.getName(), result.getYear(), result.getComments(),
                    result.getCurrentValue(), result.getGenre(),
                    result.getMediaType(), result.getPurchaseLoc(),
                    result.getPurchasePrice(), result.getPurchaseDate());

        }
    }

    /**
     * Goes through the result list and shows the previous record in the GUI.
     */
    private void prevResult() {
        currentRecord--;

        if (currentRecord < 0) {
            JOptionPane.showMessageDialog(null,
                    "You have reached the beginning of search results");
            next.setEnabled(true);
            prev.setEnabled(false);
            currentRecord++;
        }
        else {
            next.setEnabled(true);
            MediaInfo result = mediaItemList.get(currentRecord);

            setFields(result.getName(), result.getYear(), result.getComments(),
                    result.getCurrentValue(), result.getGenre(),
                    result.getMediaType(), result.getPurchaseLoc(),
                    result.getPurchasePrice(), result.getPurchaseDate());
        }
    }

    /**
     * Calls the DB backend to add the record based on the GUI fields.
     */
    private void createRecord() {

        java.util.Date date;
        try {
            date = DF.parse(jtfPurchaseDate.getText());
            java.sql.Date newDate = new java.sql.Date(date.getTime());

            MediaInfo newRecord = new MediaInfo(-1, jtfName.getText(),
                    Integer.parseInt(jtfYear.getText()), jtfComments.getText(),
                    Double.parseDouble(jtfCurrentValue.getText()),
                    (String) genreBox.getSelectedItem(),
                    (String) typeBox.getSelectedItem(),
                    (String) locBox.getSelectedItem(),
                    Double.parseDouble(jtfPurchasePrice.getText()), newDate);

            // MediaInfo newRecord = new MediaInfo(mediaItemList.size() + 1,
            // jtfName.getText(), Integer.parseInt(jtfYear.getText()),
            // jtfComments.getText(), Double.parseDouble(jtfCurrentValue
            // .getText()), jtfGenre.getText(),
            // jtfMediaType.getText(), jtfPurchaseLoc.getText(),
            // Double.parseDouble(jtfPurchasePrice.getText()), newDate);

            dbc.createRecord(newRecord);

            JOptionPane.showMessageDialog(null, "Record added to database.");

        }
        catch (ParseException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Invalid Date Format.");
        }
    }

    /**
     * Calls the DB backend to remove the record based on the GUI fields.
     */
    private void removeRecord() {
        dbc.removeRecord(search());
        JOptionPane.showMessageDialog(null, "Record removed successfully.");
    }

    /**
     * Calls the DB backend to save the record based on the GUI fields.
     */
    private void saveRecord() {
        if (currentRecord >= 0 && currentRecord < mediaItemList.size()) {
            MediaInfo result = mediaItemList.get(currentRecord);

            int id = result.getId();
            String name = jtfName.getText();
            int year = Integer.parseInt(jtfYear.getText());
            String comments = jtfComments.getText();
            double currentValue = Double.parseDouble(jtfCurrentValue.getText());
            // String genre = jtfGenre.getText();
            String genre = (String) genreBox.getSelectedItem();
            // String mediaType = jtfMediaType.getText();
            String mediaType = (String) typeBox.getSelectedItem();
            // String purchaseLoc = jtfPurchaseLoc.getText();
            String purchaseLoc = (String) locBox.getSelectedItem();
            double purchasePrice = Double.parseDouble(jtfPurchasePrice
                    .getText());

            java.util.Date date;
            java.sql.Date purchaseDate = null;
            try {
                date = DF.parse(jtfPurchaseDate.getText());
                purchaseDate = new java.sql.Date(date.getTime());
                result = new MediaInfo(id, name, year, comments, currentValue,
                        genre, mediaType, purchaseLoc, purchasePrice,
                        purchaseDate);
                dbc.updateRecord(result);

                JOptionPane.showMessageDialog(null,
                        "Record saved successfully.");
            }
            catch (ParseException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Invalid Date Format.");
            }

        }
        else {
            JOptionPane.showMessageDialog(null, "No record to Update");
        }
    }

}
