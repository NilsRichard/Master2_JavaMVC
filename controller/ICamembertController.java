package controller;

import java.awt.Component;

/**
 * ICamembertController
 * 
 * @author Dorian Bouillet
 * @author Nils Richard
 */
public interface ICamembertController {

	/**
	 * Get the selected part of the pie
	 * 
	 * @return the selected part of the pie
	 */
	int getSelectedPie();

	/**
	 * Get the view
	 * 
	 * @return the view
	 */
	Component getView();

	/**
	 * Get the set state of the pie, true if a part of the pie is selected, false
	 * otherwise
	 * 
	 * @return the set state of the pie
	 */
	boolean isSelected();

	/**
	 * Set the set state of the pie
	 * 
	 * @param b the new set state, true if a part of the pie is selected, false
	 *          otherwise
	 */
	void setSelected(boolean b);

	/**
	 * Set the selected part of the pie
	 * 
	 * @param i the selected part
	 */
	void setSelectedPie(int i);

	/**
	 * Update the title of the pie
	 * 
	 * @param newTitle the new title of the pie
	 */
	void updateTitle(String newTitle);

}
