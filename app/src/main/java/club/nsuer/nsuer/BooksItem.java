package club.nsuer.nsuer;

import java.util.ArrayList;

import club.nsuer.nsuer.BooksListItem;


public class BooksItem {

    private String course;

    private ArrayList<BooksListItem> books;



    public BooksItem(String course, ArrayList<BooksListItem> books) {

        this.course = course;
        this.books = books;


    }

    public String getCourse() {

        return course;
    }


    public void setCourse(String course) {

        this.course = course;
    }

    public ArrayList<BooksListItem> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<BooksListItem> books) {
        this.books = books;
    }


}