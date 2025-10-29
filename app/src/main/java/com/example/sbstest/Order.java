//Project: SBS
//Author: Jose Ron Coka
//File: Order.java
//Version: Working Prototype 1
//Date: 04/16/2024



package com.example.sbstest;

import java.io.Serializable;

//Creates an Order Object

public class Order implements Serializable {

    //Order Object elements
    private String customerName;
    private String emailAddress;
    private String customerPhone;
    private String customerAddress;
    private String bookISBN;
    private int quantityBook;
    private String orderID;
    private String bookTitle;
    private String bookAuthor;
    private String orderCost;
    private String bookPrice;

    private String orderDate;


    Order(String name, String email,String phone, String address, String book, int quantity ){
        customerName = name;
        emailAddress = email;
        customerPhone = phone;
        customerAddress = address;
        bookISBN = book;
        quantityBook = quantity;

    }

    // Empty constructor required for Firebase
    public Order() {}

    //public Order(String ID, String name, String email, String phone, String address, String book, String orderPrice, String quantity ){
    public Order(String ID, String name, String email, String phone, String address, String book, String title, String author, String price, String orderPrice, int quantity, String date ){
        orderID = ID;
        customerName = name;
        emailAddress = email;
        customerPhone = phone;
        customerAddress = address;
        bookISBN= book;
        bookTitle= title;
        bookAuthor = author;
        bookPrice= price;
        orderCost = orderPrice;
        quantityBook = quantity;
        orderDate= date;
    }
    //Getters and Setters

    public String getOrderCost() {
        return orderCost;
    }
    public String getBookTitle() {
        return bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public String getBookPrice() {
        return bookPrice;
    }
    public String getBookISBN() {
        return bookISBN;
    }

    public int getQuantityBook() {
        return quantityBook;
    }
    public String getOrderID() {
        return orderID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public String getOrderDate(){return orderDate;}


    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public void setBookPrice(String bookPrice) {
        this.bookPrice = bookPrice;
    }
    public void setBookISBN(String bookISBN) {
        this.bookISBN = bookISBN;
    }

    public void setQuantityBook(int quantityBook) {
        this.quantityBook = quantityBook;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setOrderDate(String orderDate){this.orderDate=orderDate;}

    //Helper Function to print out Book details
    public String displayBookInfo(){
        return "Book  \n" +
                "ISBN= " + bookISBN+ '\n' +
                "Title= " + bookTitle + '\n' +
                "Author= " + bookAuthor + '\n' +
                "Price= $" + bookPrice + '\n';
    }
    //To String Method to print out Order Details
    @Override
    public String toString() {
        return "Order Details: \n" +
                "Customer Name= " + customerName + '\n' +
                "Order Date= "+ orderDate + '\n'+
                "Email Address= " + emailAddress + '\n' +
                "Customer Phone Number= " + customerPhone + '\n' +
                "Customer Address= " + customerAddress + '\n' +
                "Order Cost= " + orderCost+ '\n' +
                "Copies of Book= " + quantityBook + '\n';
    }
}
