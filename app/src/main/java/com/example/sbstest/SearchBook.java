package com.example.sbstest;

public class SearchBook {

    private String isbn;
    private String title;
    private String author;

    private String price;
    SearchBook(String scanned){
        isbn = scanned;
        title ="";
        author = "";
        price = "";
    }

    SearchBook(String scanned, String t, String a, String p){
        isbn = scanned;
        title =t;
        author =a;
        price =p;
    }
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getPrice() {
        return price;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Book Result \n" +
                "ISBN= " + isbn + '\n' +
                "Title= " + title + '\n' +
                "Author= " + author + '\n' +
                "Price= $" + price + '\n';
    }
}


