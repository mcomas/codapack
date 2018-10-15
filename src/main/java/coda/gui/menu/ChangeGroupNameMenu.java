/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.gui.menu;

import coda.DataFrame;
import coda.gui.CoDaPackMain;

/**
 *
 * @author Guest2
 */
public class ChangeGroupNameMenu extends AbstractMenuDialog{
    
    public static final long serialVersionUID =1L;
    
    DataFrame df;
    
    public ChangeGroupNameMenu(final CoDaPackMain mainApp){
        super(mainApp,"Change Group Name Menu", "categoric");
    }
    
   @Override
   public void acceptButtonActionPerformed(){
       
       df = mainApplication.getActiveDataFrame();
       
   }
    
   public DataFrame getDataFrame(){
       return this.df;
   }
}
