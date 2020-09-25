package data;

/**
 * Item
 * 
 * @author Dorian Bouillet
 * @author Nils Richard
 */
public class Item {
	private String title;
	private String description;
	private float value;
	private String unit;

	/**
	 * Constructor
	 * 
	 * @param title       the title of the item
	 * @param description the description of the item
	 * @param value       the value of the item
	 */
	public Item(String title, String description, int value) {
		this.title = title;
		this.description = description;
		this.value = value;
	}

	/**
	 * Get the description of the item
	 * 
	 * @return the description of the item
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Get the title of the item
	 * 
	 * @return the title of the item
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Get the unit of the item
	 * 
	 * @return the unit of the item
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * Get the value of the item
	 * 
	 * @return the value of the item
	 */
	public float getValue() {
		return value;
	}

	/**
	 * Set the description of the item
	 * 
	 * @param description the description of the item
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Set the title of the item
	 * 
	 * @param title the title of the item
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Set the unit of the item
	 * 
	 * @param unit the unit of the item
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * Set the value of the item
	 * 
	 * @param value the value of the item
	 */
	public void setValue(float value) {
		this.value = value;
	}

}
