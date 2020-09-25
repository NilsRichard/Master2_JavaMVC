package model;

import java.util.List;

import data.Item;

/**
 * ICamembertModel
 * 
 * @author Dorian Bouillet
 * @author Nils Richard
 */
public interface ICamembertModel {

	/**
	 * Add an item to the pie
	 * 
	 * @param newItem the item to add
	 */
	public void addItem(Item newItem);

	/**
	 * Get the description of the specified item
	 * 
	 * @param i the number of the item
	 * @return the description of the specified item
	 */
	public String getDescription(int i);

	/**
	 * Get all the items of the pie
	 * 
	 * @return the list of items of the pie
	 */
	public List<Item> getItems();

	/**
	 * Get the title of the pie
	 * 
	 * @return the title of the pie
	 */
	public String getTitle();

	/**
	 * Get the title of a specified item
	 * 
	 * @param i the number of the item
	 * @return the title of the specified item
	 */
	public String getTitle(int i);

	/**
	 * Get the unit of the pie
	 * 
	 * @return the unit of the pie
	 */
	public String getUnit();

	/**
	 * Get the value of a specified item
	 * 
	 * @param i the number of the item
	 * @return the value of the specified item
	 */
	public float getValue(int i);

	/**
	 * Remove an item from the pie
	 * 
	 * @param newItem the item to remove
	 */
	public void removeItem(Item newItem);

	/**
	 * Get the number of items in the pie
	 * 
	 * @return the number of items in the pie
	 */
	public int size();

	/**
	 * Get the total of all values in the pie
	 * 
	 * @return the total of all values in the pie
	 */
	public float total();

	/**
	 * Update the title of the pie
	 * 
	 * @param newTitle the new title
	 */
	public void updateTitle(String newTitle);

}
