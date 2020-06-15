package com.urgentrn.urncexchange.models.bank;

public class Field {

    private String bankName;
    private String accountType;
    private String routingNumber;
    private String accountNumber;

    private String name;
    private double balance;
    private String account_id;

    private String cardNumber;
    private String fullName;
    private String expMonth;
    private String expYear;
    private String cvc;
    private String zipcode;

    private String token;
    private String customer;
    private String paymentMethod;
    private String brand;
    private String last4;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAccountId() {
        return account_id;
    }

    public void setAccountId(String account_id) {
        this.account_id = account_id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public String getExpMonth() {
        return expMonth;
    }

    public String getExpYear() {
        return expYear;
    }

    public String getCvc() {
        return cvc;
    }

    public String getZipCode() {
        return zipcode;
    }

    public String getToken() {
        return token;
    }

    public String getCustomer() {
        return customer;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getBrand() {
        return brand;
    }

    public String getLast4() {
        return last4 != null ? last4 : "";
    }
}
