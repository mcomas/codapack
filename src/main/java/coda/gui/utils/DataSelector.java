package coda.gui.utils;

import javax.swing.JPanel;

import coda.DataFrame;


public abstract class DataSelector extends JPanel {

    public DataFrame ds_dataFrame;

    public abstract void updateDataLists(DataFrame dataFrame);

    public abstract String[] getSelectedData();

    public abstract String getSelectedGroup();
    
}
