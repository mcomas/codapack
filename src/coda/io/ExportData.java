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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.io;

import coda.DataFrame;
import coda.Variable;
import coda.Zero;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author marc
 */
public class ExportData {
    static public void exportXLS(String fname, DataFrame dataframe) throws FileNotFoundException, IOException{


        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("data");
        Row row = sheet.createRow((short)0);
        short col = 0;
        ArrayList<String> vnames = dataframe.getNames();
        for(String vname: vnames){
            row.createCell(col++).setCellValue(vname);
        }
        Variable vars[] = new Variable[dataframe.size()];

        int nrows = 0;
        int nrow[] = new int[dataframe.size()];
        for(int i=0;i<dataframe.size();i++){
            vars[i] = dataframe.get(vnames.get(i));
            nrow[i] = vars[i].size();
            nrows = Math.max(nrows, nrow[i]);
        }
        short r = 1;

        for(int i=0;i<nrows;i++){
            row = sheet.createRow(r++);
            col = 0;
            for(int j=0;j<vnames.size();j++){
                Cell cell = row.createCell(col++);
                if(r <= nrow[j] + 1){
                    if(vars[j].isText()){
                        //cell.setCellValue((String)vars[j].get(i));
                    }else{
                        Object obj = vars[j].get(i);
                        if( obj instanceof Zero){
                            Zero d = (Zero)obj;
                            cell.setCellValue("<" + Double.toString(d.detection));
                        }else{
                            double d = (Double)obj;
                            if(Double.isNaN(d)){
                                cell.setCellValue("NA");
                            }else{
                                cell.setCellValue((Double)obj);
                            }

                        }
                    }
                }
            }
        }
         

        // Write the output to a file
        //FileOutputStream fileOut = new FileOutputStream(fname);
        if(!fname.endsWith(".xls"))
            fname = fname.concat(".xls");
        FileOutputStream fileOut = new FileOutputStream(fname);
        wb.write(fileOut);
        fileOut.close();
    }
}
