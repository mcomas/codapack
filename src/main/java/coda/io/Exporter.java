/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.io;

import coda.DataFrame;

/**
 *
 * @author marcc
 */
public interface Exporter {
    public void exportDataFrame(DataFrame dataframe);    
}
