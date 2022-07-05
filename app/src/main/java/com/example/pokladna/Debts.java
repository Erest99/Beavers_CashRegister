package com.example.pokladna;

import java.util.ArrayList;
import java.util.List;

public class Debts {

    private List<List<Item>> debts = new ArrayList<>();

    public List<List<Item>> getDebts() {
        return debts;
    }

    public void setDebts(List<List<Item>> debts) {
        this.debts = debts;
    }

    public Debts(List<List<Item>> debts) {
        this.debts = debts;
    }

    @Override
    public String toString() {
        return "Debts{" +
                "debts=" + debts +
                '}';
    }
}
