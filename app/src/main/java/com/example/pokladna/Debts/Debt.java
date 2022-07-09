package com.example.pokladna.Debts;

public class Debt {


    private Long id;
    private String debtor;
    private String date;
    private String name;
    private Integer price;
    private Integer ammount;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDebtor() {
        return debtor;
    }

    public void setDebtor(String debtor) {
        this.debtor = debtor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getAmmount() {
        return ammount;
    }

    public void setAmmount(Integer ammount) {
        this.ammount = ammount;
    }

    public Debt(Long id, String debtor, String date, String name, Integer price, Integer ammount) {
        this.id = id;
        this.debtor = debtor;
        this.date = date;
        this.name = name;
        this.price = price;
        this.ammount = ammount;
    }
}
