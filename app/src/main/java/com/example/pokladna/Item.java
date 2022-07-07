package com.example.pokladna;

public class Item {

    private Long id;
    private String name;
    private Integer buy;
    private Integer sell;
    private Integer ammount;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBuy() {
        return buy;
    }

    public void setBuy(Integer buy) {
        this.buy = buy;
    }

    public Integer getSell() {
        return sell;
    }

    public void setSell(Integer sell) {
        this.sell = sell;
    }

    public Integer getAmmount() {
        return ammount;
    }

    public void setAmmount(Integer ammount) {
        this.ammount = ammount;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", buy=" + buy +
                ", sell=" + sell +
                ", ammount=" + ammount +
                '}';
    }

    public Item(String name, Integer buy, Integer sell, Integer ammount) {
        this.name = name;
        this.buy = buy;
        this.sell = sell;
        this.ammount = ammount;
    }

    public Item(Long id, String name, Integer buy, Integer sell, Integer ammount) {
        this.id = id;
        this.name = name;
        this.buy = buy;
        this.sell = sell;
        this.ammount = ammount;
    }
}
