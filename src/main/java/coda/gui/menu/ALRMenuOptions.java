package coda.gui.menu;

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
        public ALRMenuOptions(){
            
            HBox hb = new HBox();
            hb.setSpacing(10);
            RadioButton ra = new RadioButton("Raw-ALR");
            ra.setSelected(true);
            RadioButton ar = new RadioButton("ALR-Raw");
            ToggleGroup group = new ToggleGroup();
            ar.setToggleGroup(group);
            ar.setSelected(true);
            ra.setToggleGroup(group);
            hb.getChildren().addAll(ar,ra);
            getChildren().add(hb);
        }
    }
