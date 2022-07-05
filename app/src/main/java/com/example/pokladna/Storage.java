package com.example.pokladna;

import java.util.ArrayList;
import java.util.List;

public class Storage {

    private List<Item> items = new ArrayList<>();

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Storage{" +
                "items=" + items +
                '}';
    }

    public Storage(List<Item> items) {
        this.items = items;
    }
}
