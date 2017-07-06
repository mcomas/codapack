/**	
 *	Copyright 2011-2016 Marc Comas - Santiago Thió
 *
 *	This file is part of CoDaPack.
 *
 *  CoDaPack is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CoDaPack is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CoDaPack.  If not, see <http://www.gnu.org/licenses/>.
 */

package coda.gui.menu;

import coda.DataFrame;
import coda.Variable;
import coda.gui.CoDaPackMain;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.ListSelectionView;

/**
 *
 * @author mcomas
 */
public abstract class MenuDialog extends Stage{
    protected CoDaPackMain mainApplication;
    protected DataFrame dataframe;
    protected ListSelectionView selector;
    protected OptionPane options;
    
    int WIDTH = 650;//560;
    int HEIGHT = 500;//430;
    public MenuDialog(final CoDaPackMain mainApp, String title, OptionPane options){
        this.mainApplication = mainApp;
        this.setTitle(title);
        this.options = options;
        
        Stage mainStage = mainApp.mainStage;   
        
        dataframe = mainApp.workspace.getActiveDataFrame();
        ArrayList<String> vnumeric = dataframe.getNames(Variable.VAR_NUMERIC);
        ArrayList<String> vtext = dataframe.getNames(Variable.VAR_TEXT);
        
        
        // Selection pane
        selector = new ListSelectionView();
        selector.getSourceItems().addAll(vnumeric);        
        
        CheckComboBox ccb = new CheckComboBox();
        ccb.getItems().addAll(vtext);
        VBox groups = new VBox();
        groups.setPadding(new Insets(5,25,5,5));
        groups.setAlignment(Pos.BASELINE_RIGHT);
        Label lgroup = new Label("Group by:");
        lgroup.setStyle("-fx-font-weight: bold");
        groups.getChildren().addAll(lgroup,ccb);
        
        BorderPane bps = new BorderPane();
        bps.setCenter(selector);
        bps.setBottom(groups);
        
        // Accept, cancel buttons
        HBox buttons = new HBox();
        buttons.setPadding(new Insets(25));
        //buttons.setPrefHeight(50);
        buttons.setAlignment(Pos.BASELINE_RIGHT);
        buttons.setSpacing(10);
        Button accept = new Button("Accept");
        accept.setOnAction((ActionEvent e) -> {
            acceptButtonActionPerformed();
        });
        Button cancel = new Button("Cancel");
        cancel.setOnAction((ActionEvent e) -> {
            cancelButtonActionPerformed();
        });
        
        buttons.getChildren().addAll(accept, cancel);
        
        BorderPane bp = new BorderPane();        
        bp.setCenter(bps);
        bp.setRight(options);
        bp.setBottom(buttons);
        
        setScene(new Scene(bp, 700, 400));
        
        initialize(mainStage);
        
        
        
    }
    private void initialize(Stage mainStage){
        
        double centerXPosition = mainStage.getX() + mainStage.getWidth()/2d;
        double centerYPosition = mainStage.getY() + mainStage.getHeight()/2d;

        // Hide the pop-up stage before it is shown and becomes relocated
        this.setOnShowing(ev -> this.hide());

        // Relocate the pop-up Stage
        this.setOnShown(ev -> {
            this.setX(centerXPosition - this.getWidth()/2d);
            this.setY(centerYPosition - this.getHeight()/2d);
            this.show();
        });
    }
    public abstract void acceptButtonActionPerformed();
    public void cancelButtonActionPerformed(){
        this.close();
    }
}