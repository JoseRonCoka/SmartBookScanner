package com.example.sbstest;


import android.widget.EditText;

public class Order  {

    private String orderNumber;
    private String customerName;
    private String emailAddress;

    private String customerPhone;
    private String customerAddress;
    private String bookISBN;

    private String quantityBook;


    Order(String name, String email,String phone, String address, String isbn, String quantity ){
        customerName = name;
        emailAddress = email;
        customerPhone = phone;
        customerAddress = address;
        bookISBN = isbn;
        quantityBook = quantity;
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

    @Override
    public String toString() {
        return "Order Details: \n" +
                "Customer Name= " + customerName + '\n' +
                "Email Address= " + emailAddress + '\n' +
                "Customer Phone Number= " + customerPhone + '\n' +
                "Customer Address= " + customerAddress + '\n' +
                "Book ISBN= " + bookISBN + '\n' +
                "Quantity of Book= " + quantityBook + '\n';
    }
}
