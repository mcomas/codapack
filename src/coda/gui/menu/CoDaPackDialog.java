/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.gui.menu;

import coda.gui.CoDaPackMain;
import java.awt.Toolkit;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author mcomas
 */
public class CoDaPackDialog extends JDialog{
    public CoDaPackDialog(){
        this.setIconImage(Toolkit.getDefaultToolkit()
                .getImage(CoDaPackMain.RESOURCE_PATH + "logo.png"));
        
    }
    public CoDaPackDialog(JFrame app, String title){
        super(app, title);
    }
}
