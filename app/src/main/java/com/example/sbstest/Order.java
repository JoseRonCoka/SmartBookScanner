package com.example.sbstest;


import android.widget.EditText;

import java.io.Serializable;

public class Order implements Serializable {


    private String customerName;
    private String emailAddress;

    private String customerPhone;
    private String customerAddress;
    private String bookISBN;

    private String quantityBook;

    public SearchBook bookObject;

    private String orderID;
    private String bookTitle;
    private String bookAuthor;

    private String orderCost;
    private String bookPrice;



    Order(String name, String email,String phone, String address, String book, String quantity ){
        customerName = name;
        emailAddress = email;
        customerPhone = phone;
        customerAddress = address;
        bookISBN = book;
        quantityBook = quantity;

    }

    public String getOrderCost() {
        return orderCost;
    }

    Order(String ID, String name, String email, String phone, String address, String book, String orderPrice, String quantity ){
        orderID = ID;
        customerName = name;
        emailAddress = email;
        customerPhone = phone;
        customerAddress = address;
        bookISBN= book;
        bookTitle= "";
        bookAuthor = "";
        bookPrice="";
        orderCost = orderPrice;
        quantityBook = quantity;
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

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public void setBookPrice(String bookPrice) {
        this.bookPrice = bookPrice;
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

    public String getBookISBN() {
        return bookISBN;
    }

    public String getQuantityBook() {
        return quantityBook;
    }

    //Set method in case Order needs to be modified.
    public void setBookISBN(String bookISBN) {
        this.bookISBN = bookISBN;
    }

    public void setQuantityBook(String quantityBook) {
        this.quantityBook = quantityBook;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String displayBookInfo(){
        return "Book  \n" +
                "ISBN= " + bookISBN+ '\n' +
                "Title= " + bookTitle + '\n' +
                "Author= " + bookAuthor + '\n' +
                "Price= $" + bookPrice + '\n';
    }

    @Override
    public String toString() {
        return "Order Details: \n" +
                "Customer Name= " + customerName + '\n' +
                "Email Address= " + emailAddress + '\n' +
                "Customer Phone Number= " + customerPhone + '\n' +
                "Customer Address= " + customerAddress + '\n' +
                "Order Cost= " + orderCost+ '\n' +
                "Copies of Book= " + quantityBook + '\n';
    }
}
