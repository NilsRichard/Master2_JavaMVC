package controller;

import java.awt.Component;

public interface ICamembertController {

	void setSelected(boolean b);

	int getSelectedPie();

	void setSelectedPie(int i);

	boolean isSelected();

	Component getView();

	void updateTitle(String newTitle);

}
