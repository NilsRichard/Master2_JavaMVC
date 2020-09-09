package model;

import java.util.List;
import java.util.Observable;

import data.Item;
import view.CamembertView;

public interface ICamembertModel {

    public List<Item> getItems();

    public String getTitle();

    public void updateTitle(String newTitle);

    public void addItem(Item newItem);

    public void removeItem(Item newItem);

    public float getValue(int i);

    public float total();

    public int size();

	public String getTitle(int i);

	public String getUnit();

	public String getDescription(int i);

}
