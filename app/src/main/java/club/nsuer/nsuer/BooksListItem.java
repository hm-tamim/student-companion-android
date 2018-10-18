package club.nsuer.nsuer;

public class BooksListItem {

    private String bookName;
    private String edition;
    private String author;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public BooksListItem(String bookName, String edition, String author,String url) {

        this.bookName = bookName;
        this.edition = edition;
        this.author = author;
        this.url = url;
    }
}
