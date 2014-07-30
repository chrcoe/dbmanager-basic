import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * This class maintains the DB's connection, allowing for CRUD operations on the
 * DB.
 *
 * @author chrcoe
 */
public class DBConnection {

    private ArrayList<MediaInfo> mediaItemList;
    private Connection con = null;
    private String userid = "franklin";
    private String pw = "FranklinCoe05";
    // from HOME - this is local network only
    static String url = "jdbc:mysql://localhost/mediamanager";

    private int lastPIMIAutoKey;
    private int lastMIAutoKey;

    /**
     * Sets up the object to use for the DB.
     */
    public DBConnection() {
        lastPIMIAutoKey = -1;
        lastMIAutoKey = -1;
        mediaItemList = new ArrayList<MediaInfo>();
        getConnection();
    }

    /**
     * This opens up the connection to the DB
     * @return the Connection object of the DB
     */
    public Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (java.lang.ClassNotFoundException e) {
            System.err.print("ClassNotFoundException: ");
            System.err.println(e.getMessage());
        }
        try {
            con = DriverManager.getConnection(url, userid, pw);
        }
        catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }

        return con;
    }

    /**
     * Gets a list of all media items in the db.
     *
     * @return all media items in the db
     */
    public ArrayList<MediaInfo> findRecord(String name) {
        mediaItemList.clear();
        try {
            String sql = "SELECT * FROM mediamanager.complete_view WHERE name like '%"
                    + name + "%'";

            // prepared statement
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(sql);

            int id, year;
            String pname = "";
            String comments = "";
            String genre = "";
            String mediaType = "";
            String purchaseLocation = "";
            Date purchaseDate = new Date(20000101);
            double currentValue, purchasePrice;

            while (rs.next()) {
                id = rs.getInt("id");
                pname = rs.getString("name");
                year = rs.getInt("year");
                comments = rs.getString("comments");
                currentValue = rs.getDouble("currentvalue");
                genre = rs.getString("genre");
                mediaType = rs.getString("mediatype");
                purchaseLocation = rs.getString("purchaselocation");
                purchasePrice = rs.getDouble("purchaseprice");
                purchaseDate = rs.getDate("purchasedate");

                MediaInfo recordInfo = new MediaInfo(id, pname, year, comments,
                        currentValue, genre, mediaType, purchaseLocation,
                        purchasePrice, purchaseDate);
                mediaItemList.add(recordInfo);

            }

            s.close();
            rs.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }

        return mediaItemList;
    }

    /**
     * Gives an array list of all the records in the DB.
     * @return ArrayList<MediaInfo> containing all the records in the DB
     */
    public ArrayList<MediaInfo> getAllRecords() {
        mediaItemList.clear();

        try {
            String sql = "SELECT * FROM mediamanager.complete_view";

            // prepared statement
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(sql);

            int id, year;
            String pname = "";
            String comments = "";
            String genre = "";
            String mediaType = "";
            String purchaseLocation = "";
            Date purchaseDate = new Date(20000101);
            double currentValue, purchasePrice;

            while (rs.next()) {
                id = rs.getInt("id");
                pname = rs.getString("name");
                year = rs.getInt("year");
                comments = rs.getString("comments");
                currentValue = rs.getDouble("currentvalue");
                genre = rs.getString("genre");
                mediaType = rs.getString("mediatype");
                purchaseLocation = rs.getString("purchaselocation");
                purchasePrice = rs.getDouble("purchaseprice");
                purchaseDate = rs.getDate("purchasedate");

                MediaInfo recordInfo = new MediaInfo(id, pname, year, comments,
                        currentValue, genre, mediaType, purchaseLocation,
                        purchasePrice, purchaseDate);
                mediaItemList.add(recordInfo);
            }

            s.close();
            rs.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }

        return mediaItemList;
    }

    /**
     * Removes the record passed in to the method from the DB.
     *
     * @param record the record to remove from the DB
     */
    public void removeRecord(MediaInfo record) {

        int purchaseInfoId = getPurchaseInfoId(record);

        if (record == null) {
            return; // nothing to do here yet..
        }

        try {
            String sql = String.format(
                    "DELETE FROM mediamanager.MediaItem WHERE ID = %d",
                    record.getId());

            PreparedStatement ps;

            ps = con.prepareStatement(sql);
            ps.execute();

            String sql2 = String.format("DELETE FROM "
                    + "mediamanager.PurchaseInfoMediaItem WHERE "
                    + "PurchaseInfoID = %d AND MediaItemID = %d",
                    purchaseInfoId, record.getId());

            PreparedStatement ps2;

            ps2 = con.prepareStatement(sql2);
            ps2.execute();

        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Adds the given record to the DB.
     *
     * @param newRecord the new record object to add to the DB
     */
    public void createRecord(MediaInfo newRecord) {

        int genreId = getGenreId(newRecord);
        int mediaTypeId = getMediaTypeId(newRecord);
        int purchaseInfoId = getPurchaseInfoId(newRecord);

        try {

            Statement s = con.createStatement(
                    java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_UPDATABLE);

            ResultSet rs = null;
            // int autoIncKey = -1;

            String sql = String
                    .format("INSERT INTO mediamanager.MediaItem (ID, Name, "
                            + "Year, Comments, CurrentValue, GenreID, MediaTypeID) "
                            + "VALUES (null,\'%s\',%d,\'%s\',%.2f,%d,%d)",
                            newRecord.getName(), newRecord.getYear(),
                            newRecord.getComments(),
                            newRecord.getCurrentValue(), genreId, mediaTypeId);

            s.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            rs = s.getGeneratedKeys();
            rs.last();
            lastMIAutoKey = rs.getInt(1);

            // PreparedStatement ps = con.prepareStatement(sql);
            // ps.execute();

            // purchaseInfo and MediaItem share the same primary keys
            // and one cannot be added without the other...
            // these combined makeup PurchaseInfoMediaItem table which is used
            // for my complete_view which is how the program finds all the data

            String sql2 = String
                    .format("INSERT INTO mediamanager.PurchaseInfoMediaItem "
                            + "(ID,PurchasePrice,PurchaseDate,PurchaseInfoID,MediaItemID)"
                            + "VALUES (null,%.2f,\'%s\',%d,%d)", newRecord
                            .getPurchasePrice(), newRecord.getPurchaseDate()
                            .toString(), purchaseInfoId, lastMIAutoKey);

            // use the last automatically incremented key from adding mediaItem
            // because it needs to be attached to this PurchaseInfoMediaItem
            // by way of it's PrimKey

            s.executeUpdate(sql2, Statement.RETURN_GENERATED_KEYS);
            rs = s.getGeneratedKeys();
            rs.last();
            lastPIMIAutoKey = rs.getInt(1);

            s.close();
            rs.close();

            // PreparedStatement ps2 = con.prepareStatement(sql2);
            // ps2.setDouble(1, newRecord.getPurchasePrice());
            // ps2.setString(2, newRecord.getPurchaseDate().toString());
            // ps2.setInt(3, purchaseInfoId);
            // ps2.setInt(4, autoIncKey);
            // ps2.executeUpdate(PreparedStatement.RETURN_GENERATED_KEYS);

        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Updates the DB with the given record information.
     *
     * @param result the record to update
     */
    public void updateRecord(MediaInfo result) {
        int genreId = getGenreId(result);
        int mediaTypeId = getMediaTypeId(result);
        int purchaseInfoId = getPurchaseInfoId(result);

        try {
            String sql = "UPDATE mediamanager.MediaItem SET GenreID = ?,"
                    + "MediaTypeID = ?,Name = ? , Year = ? , Comments = ?,"
                    + "CurrentValue = ? WHERE ID = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, genreId);
            ps.setInt(2, mediaTypeId);
            ps.setString(3, result.getName());
            ps.setInt(4, result.getYear());
            ps.setString(5, result.getComments());
            ps.setDouble(6, result.getCurrentValue());
            ps.setInt(7, result.getId());
            ps.executeUpdate();

            String sql2 = "UPDATE mediamanager.PurchaseInfoMediaItem SET"
                    + " PurchasePrice = ?, PurchaseDate = ?,PurchaseInfoID = ?,"
                    + " MediaItemID = ? " + "WHERE MediaItemID = ?";

            PreparedStatement ps2 = con.prepareStatement(sql2);

            ps2.setDouble(1, result.getPurchasePrice());
            ps2.setDate(2, result.getPurchaseDate());
            ps2.setInt(3, purchaseInfoId);
            ps2.setInt(4, result.getId());
            ps2.setDouble(5, result.getId());
            ps2.executeUpdate();

        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Private method to handle pulling a genre ID out when given a new record.
     *
     * @param newRecord
     *            the record to search
     * @return the genreId of the given record
     */
    private int getGenreId(MediaInfo newRecord) {

        int genreId = -1;

        try {
            String sql = "SELECT ID FROM mediamanager.Genre WHERE "
                    + "GenreDescription LIKE '%" + newRecord.getGenre() + "%'";

            ResultSet rs = con.createStatement().executeQuery(sql);

            while (rs.next()) {
                genreId = rs.getInt("id");
            }

            rs.close();
        }
        catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return genreId;
    }

    /**
     * Private method to handle pulling a media type ID out when given a new
     * record.
     *
     * @param newRecord
     *            the record to search
     * @return the mediaTypeId of the given record
     */
    private int getMediaTypeId(MediaInfo newRecord) {

        int mediaTypeId = -1;

        try {
            String sql = "SELECT ID FROM mediamanager.MediaType WHERE "
                    + "MediaTypeDescription LIKE '%" + newRecord.getMediaType()
                    + "%'";

            ResultSet rs = con.createStatement().executeQuery(sql);

            while (rs.next()) {
                mediaTypeId = rs.getInt("id");
            }

            rs.close();
        }
        catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return mediaTypeId;
    }

    /**
     * Private method to handle pulling a purchase ID out when given a new
     * record.
     *
     * @param newRecord
     *            the record to search
     * @return the PurchaseInfoID of the given record
     */
    private int getPurchaseInfoId(MediaInfo newRecord) {

        int purchaseInfoId = -1;

        try {
            String sql = "SELECT ID FROM mediamanager.PurchaseInfo WHERE "
                    + "PurchaseLocation LIKE '%" + newRecord.getPurchaseLoc()
                    + "%'";

            ResultSet rs = con.createStatement().executeQuery(sql);

            while (rs.next()) {
                purchaseInfoId = rs.getInt("id");
            }

            rs.close();
        }
        catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return purchaseInfoId;
    }

    public void closeConnection() {
        try {
            con.close();
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
