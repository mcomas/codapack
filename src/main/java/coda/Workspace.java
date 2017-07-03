/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coda;

import java.util.ArrayList;

/**
 *
 * @author marcc
 */
public class Workspace {
    
    private final ArrayList<DataFrame> dataFrame = new ArrayList<DataFrame>();
    private int activeDataFrame = -1;
    
    public void addDataFrame(DataFrame df){
//        if(isDataFrameNameAvailable(df.name)){
            activeDataFrame = dataFrame.size();
            dataFrame.add(df);
//            dataList.setData(df);
            //tablePane.setDataFrame(df);
//            dataFrameSelector.removeItemListener(dataFrameListener);
//            dataFrameSelector.addItem(df.name);
//            dataFrameSelector.setSelectedItem(df.name);
//            dataFrameSelector.addItemListener(dataFrameListener);
//            df.setChange(false);
//        }else{
//            JOptionPane.showMessageDialog(this,"<html>Dataframe <i>" +
//                    df.name + "</i> is already loaded.</html>");
//        }
    }
    public int numberOfDataFrames(){
        return dataFrame.size();
    }
    public Workspace(){
    }
    public DataFrame getActiveDataFrame(){
        return dataFrame.get(activeDataFrame);
    }
}
