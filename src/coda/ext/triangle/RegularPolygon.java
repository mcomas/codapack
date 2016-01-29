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

package coda.ext.triangle;

import java.awt.*;

public class RegularPolygon extends Polygon {
    public RegularPolygon(int x, int y, int r, int vertexCount) {
        this(x, y, r, vertexCount, 0);
    }
    public RegularPolygon(int x, int y, int r, int vertexCount, double startAngle) {
        super(getXCoordinates(x, y, r, vertexCount, startAngle)
              ,getYCoordinates(x, y, r, vertexCount, startAngle)
              ,vertexCount);
    }

    protected static int[] getXCoordinates(int x, int y, int r, int vertexCount, double startAngle) {
        int res[]=new int[vertexCount];
        double addAngle=2*Math.PI/vertexCount;
        double angle=startAngle;
        for (int i=0; i<vertexCount; i++) {
            res[i]=(int)Math.round(r*Math.cos(angle))+x;
            angle+=addAngle;
        }
        return res;
    }

    protected static int[] getYCoordinates(int x, int y, int r, int vertexCount, double startAngle) {
        int res[]=new int[vertexCount];
        double addAngle=2*Math.PI/vertexCount;
        double angle=startAngle;
        for (int i=0; i<vertexCount; i++) {
            res[i]=(int)Math.round(r*Math.sin(angle))+y;
            angle+=addAngle;
        }
        return res;
    }
}
