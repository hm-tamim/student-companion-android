package club.nsuer.nsuer;

public class BuySellItem {

    private int id;
    private String sellerName;
    private int sellerID;
    private String title;
    private String price;
    private String imageUrl;
    private long time;
    private int category;
    private String description;

    private int sold;
    private int approved;

    public BuySellItem(int id, String sellerName, int sellerID, String title, String price, String imageUrl, long time, int category, String description, int sold, int approved) {
        this.id = id;
        this.sellerName = sellerName;
        this.sellerID = sellerID;
        this.title = title;
        this.price = price;
        this.imageUrl = imageUrl;
        this.time = time;
        this.category = category;
        this.description = description;
        this.sold = sold;
        this.approved = approved;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public int getSellerID() {
        return sellerID;
    }

    public void setSellerID(int sellerID) {
        this.sellerID = sellerID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
