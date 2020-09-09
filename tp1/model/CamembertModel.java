package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;

import data.Item;
import view.CamembertView;

public class CamembertModel extends Observable implements ICamembertModel {

    private String title;
    private String unit;
    private List<Item> items;

    @Override
    public void updateTitle(String newTitle) {
        this.title = newTitle;
    }

    @Override
    public void addItem(Item newItem) {
        this.items.add(newItem);
    }

    @Override
    public void removeItem(Item item) {
        this.items.remove(item);
    }

    @Override
    public List<Item> getItems() {
        return new ArrayList<Item>(this.items);
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public float getValue(int i) {
        return this.items.get(i).getValue();
    }

    @Override
    public float total() {
        float ret = 0;
        for (Item item : items) {
            ret += item.getValue();
        }
        return ret;
    }

    @Override
    public int size() {
        return this.items.size();
    }

    @Override
    public String getTitle(int i) {
        return this.items.get(i).getTitle();
    }

    @Override
    public String getUnit() {
        return this.unit;
    }

    @Override
    public String getDescription(int i) {
        return this.items.get(i).getDescription();
    }

    public CamembertModel(String title, String unit, List<Item> items) {
        this.title = title;
        this.unit = unit;
        this.items = items;
    }

}
