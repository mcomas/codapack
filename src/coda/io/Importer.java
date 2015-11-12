/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.io;


import coda.DataFrame;
import java.awt.Component;

/**
 *
 * @author marc
 */
public interface Importer {
    /*
     * It creates an importer from parameters manually choosen
     * by a user. The menu are located according to frame.
     */
    /**
     *
     * @param frame
     * @return
     */
    public Importer setParameters(Component frame);
    /*
     * It creates an importer from parameters passed through
     * string pars.
     */
    public Importer setParameters(String pars);
    /*
     * Get the current parameters of an importer codified as 
     * a single string. Values are separated with '?' character.
     */
    public String getParameters();
    /*
     * import data if the importer is well defined otherwise
     * the function should return null
     */
    public DataFrame importDataFrame();
    

}
