package coda.gui.menu;

import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author marc
 */
public class ALRMenuOptions extends OptionPane{
    RadioButton ra;
    RadioButton ar;
    public ALRMenuOptions(){

        HBox hb = new HBox();

        hb.setSpacing(10);
        ra = new RadioButton("Raw-ALR");
        ra.setSelected(true);
        ar = new RadioButton("ALR-Raw");
        ToggleGroup group = new ToggleGroup();
        ar.setToggleGroup(group);
        ra.setToggleGroup(group);
        hb.getChildren().addAll(ra,ar);
        getChildren().add(hb);
    }
}