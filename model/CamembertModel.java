package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import data.Item;

/**
 * CamembertModel
 * 
 * @author Dorian Bouillet
 * @author Nils Richard
 */
public class CamembertModel extends Observable implements ICamembertModel {

	private String title;
	private String unit;
	private List<Item> items;

	/**
	 * Constructor
	 * 
	 * @param title the title of the pie
	 * @param unit  the unit of the pie
	 * @param items the items in the pie
	 */
	public CamembertModel(String title, String unit, List<Item> items) {
		this.title = title;
		this.unit = unit;
		this.items = items;
	}

	@Override
	public void addItem(Item newItem) {
		notifyObservers();
		this.items.add(newItem);
	}

	@Override
	public String getDescription(int i) {
		return this.items.get(i).getDescription();
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
	public String getTitle(int i) {
		return this.items.get(i).getTitle();
	}

	@Override
	public String getUnit() {
		return this.unit;
	}

	@Override
	public float getValue(int i) {
		return this.items.get(i).getValue();
	}

	@Override
	public void removeItem(Item item) {
		notifyObservers();
		this.items.remove(item);
	}

	@Override
	public int size() {
		return this.items.size();
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
	public void updateTitle(String newTitle) {
		notifyObservers();
		this.title = newTitle;
	}

}
