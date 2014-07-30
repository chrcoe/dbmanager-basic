import java.sql.Date;

/**
 * Contains all the info that a record needs in the DB.  Helps manage the
 * records easily.
 *
 * @author chrcoe
 *
 */
public class MediaInfo {

    // all columns from using complete_view created on my DB
    // id, name, year, comments, currentvalue, genre, mediatype,
    // purchaselocation, purchaseprice, purchasedate

    private int id, year;
    private String name, comments, genre, mediaType, purchaseLocation;
    private double currentValue, purchasePrice;
    private Date purchaseDate;

    // is this ever needed? we are always pulling or pushing to the DB
//    public MediaInfo() {
//        id = -1;
//        name = "";
//        year = 0;
//        comments = "";
//        currentValue = 0.0;
//        genre = "";
//        mediaType = "";
//        purchaseLocation = "";
//        purchasePrice = 0.0;
//        purchaseDate = new Date(20000101);
//    }

    /**
     * Constructs the MediaInfo object with specific info provided.
     */
    public MediaInfo(int id, String name, int year, String comments,
            double currentValue, String genre, String mediaType,
            String purchaseLoc, double purchasePrice, Date purchaseDate) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.comments = comments;
        this.currentValue = currentValue;
        this.genre = genre;
        this.mediaType = mediaType;
        this.purchaseLocation = purchaseLoc;
        this.purchasePrice = purchasePrice;
        this.purchaseDate = purchaseDate;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getPurchaseLoc() {
        return purchaseLocation;
    }

    public void setPurchaseLocation(String purchaseLocation) {
        this.purchaseLocation = purchaseLocation;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public int getId() {
        return id;
    }

}
