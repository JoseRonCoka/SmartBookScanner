//Project: SBS
//Author: Jose Ron Coka
//File: SearchBook.java
//Version: Working Prototype 1
//Date: 04/16/2024



package com.example.sbstest;


//SearchBook class represent a Book object

public class SearchBook {

    //Elements of the SearchBook object
    private String isbn;
    private String title;
    private String author;
    private String price;

    //Constructors
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


