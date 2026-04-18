package coda.gui.utils;

import javax.swing.JPanel;

import coda.DataFrame;


public abstract class DataSelector extends JPanel {

    public static int ONLY_NUMERIC = 1;
    public static int ALL_VARIABLES = 2;
    public static int ONLY_CATEGORIC = 3;
    public int selection_type = ONLY_NUMERIC;

    public boolean group_by;

    public DataFrame ds_dataFrame;

    public abstract void updateDataLists(DataFrame dataFrame);

    public abstract String[] getSelectedData();

    public abstract String getSelectedGroup();
    
    public abstract void setSelectedGroup(String value);
}
