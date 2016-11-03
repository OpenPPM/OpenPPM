/*
 * Copyright (C) 2009-2015 SM2 SOFTWARE & SERVICES MANAGEMENT
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program has been created in the hope that it will be useful.
 * It is distributed WITHOUT ANY WARRANTY of any Kind,
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see http://www.gnu.org/licenses/.
 *
 * For more information, please contact SM2 Software & Services Management.
 * Mail: info@talaia-openppm.com
 * Web: http://www.talaia-openppm.com
 *
 * Module: core
 * File: Line.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:53:00
 */

package es.sm2.openppm.core.javabean;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Line extends Line2D.Double{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Line() {
		super();
	}
	
	public Line(Point2D p1, Point2D p2) {
		this.setLine(p1, p2);
	}
	
	public Line(double x1, double y1, double x2, double y2) {
		this.setLine(x1, y1, x2, y2);
	}
	
	public Point2D lineIntersection(Line line2) {
		
		Point2D cp = null;
		double a1,b1,c1,a2,b2,c2,denom;
		
		// a1x + b1y + c1 = 0 line1 eq
		a1 = this.getY2()-this.getY1();
		b1 = this.getX1()-this.getX2();
		c1 = this.getX2()*this.getY1()-this.getX1()*this.getY2();
		
		// a2x + b2y + c2 = 0 line2 eq
		a2 = line2.getY2()-line2.getY1();
		b2 = line2.getX1()-line2.getX2();
		c2 = line2.getX2()*line2.getY1()-line2.getX1()*line2.getY2();
		
		denom = a1*b2 - a2*b1;
		
		if(denom != 0) {
			cp = new Point2D.Double((b1*c2 - b2*c1)/denom,(a2*c1 - a1*c2)/denom);
		} 
		else {
		// lines are parallel
		}
		return cp;
	}
}
