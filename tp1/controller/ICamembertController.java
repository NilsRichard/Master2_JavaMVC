package controller;

import java.awt.Component;


public interface ICamembertController {

	void setSelected(boolean b);

	int getSelectedPie();

	void setSelectedPie(int i);

	boolean isSelected();

	void deSelect();

	void selectPie(int i);

	void nextPie();

	void previousPie();

	Component getView();
    
}
