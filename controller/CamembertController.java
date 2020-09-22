package controller;

import java.awt.Component;

import model.ICamembertModel;
import view.CamembertView;
import view.ICamembertView;

/**
 * CamembertController
 */
public class CamembertController implements ICamembertController {

    private ICamembertView view;
    private ICamembertModel model;

    private boolean selected;
    private int selectedPie;

    public CamembertController(ICamembertView view, ICamembertModel model) {
        this.view = view;
        this.model = model;
        this.selected = false;
        this.selectedPie = 0;
    }

    @Override
    public void setSelected(boolean b) {
        this.selected = b;
    }

    @Override
    public int getSelectedPie() {
        return this.selectedPie;
    }

    @Override
    public void setSelectedPie(int i) {
        this.selectedPie = i;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public Component getView() {
        return ((CamembertView) this.view);
    }

    @Override
    public void updateTitle(String newTitle) {
        this.model.updateTitle(newTitle);
    }

}