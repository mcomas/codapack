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
import coda.gui.CoDaPackMain;
import java.util.Arrays;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.controlsfx.control.ListSelectionView;

/**
 *
 * @author mcomas
 */
public class MenuDialog extends Stage{
    CoDaPackMain mainApplication;

    int WIDTH = 650;//560;
    int HEIGHT = 500;//430;
    public MenuDialog(final CoDaPackMain mainApp, String title){
        Stage mainStage = mainApp.mainStage;        
        this.setTitle(title);
        BorderPane bp = new BorderPane();
        ListSelectionView lsv = new ListSelectionView();
        DataFrame df = mainApp.workspace.getActiveDataFrame();
        System.out.println(df.getNames().toString());
        lsv.getSourceItems().addAll(df.getNames());
        bp.setCenter(lsv);
        setScene(new Scene(bp, 600, 400));
        
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
}