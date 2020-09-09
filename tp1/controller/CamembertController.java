package controller;

import java.awt.Component;

import view.CamembertView;
import view.ICamembertView;

/**
 * CamembertController
 */
public class CamembertController implements ICamembertController {

    private ICamembertView view;

    private boolean selected;

    public CamembertController(ICamembertView view) {
        this.view = view;
    }

    @Override
    public void setSelected(boolean b) {
        this.selected = b;
    }

    @Override
    public int getSelectedPie() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setSelectedPie(int i) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void deSelect() {
        // TODO Auto-generated method stub

    }

    @Override
    public void selectPie(int i) {
        // TODO Auto-generated method stub

    }

    @Override
    public void nextPie() {
        // TODO Auto-generated method stub

    }

    @Override
    public void previousPie() {
        // TODO Auto-generated method stub

    }

    @Override
    public Component getView() {
        return ((CamembertView) this.view);
    }

}