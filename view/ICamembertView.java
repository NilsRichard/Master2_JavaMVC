package view;

import controller.ICamembertController;

/**
 * ICamembertView
 * 
 * @author Dorian Bouillet
 * @author Nils Richard
 */
public interface ICamembertView {

	/**
	 * Set the controller to this view
	 * 
	 * @param controller the controller to set
	 */
	public void setController(ICamembertController controller);
}