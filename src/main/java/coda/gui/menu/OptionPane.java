/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.gui.menu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

/**
 *
 * @author marc
 */
public class OptionPane extends VBox{
    public OptionPane(){
        setAlignment(Pos.TOP_LEFT);
        setPadding(new Insets(25));
        setSpacing(10);
        setPrefWidth(250);
    }
    
}
