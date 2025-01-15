package CodesEJ2800;
/* Copyright material for students working on assignments and projects */

import org.jogamp.java3d.GeometryArray;
import org.jogamp.java3d.LineArray;
import org.jogamp.java3d.Shape3D;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3f;

class L2StarEJ extends GroupObjects {
	/* a function to create and return a points on a circle */
	public static Point3f[] circle_Points(float z, float r, int n) {
		float x, y;
		Point3f c_pts[] = new Point3f[n];                  // declare 'n' number of points

		for (int i = 0; i < n; i++) {                      // calculate x and y
			x = (float) Math.cos(Math.PI / 180 * (360.0 * i / n)) * r;
			y = (float) Math.sin(Math.PI / 180 * (360.0 * i / n)) * r;
			c_pts[i] = new Point3f(x, y, z);               // set points on the circle
		}

		return c_pts;
	}
	
	/* a function to create and return the geometry of a star shape */
	private static LineArray one_Star(int num, Color3f clr, float r) {

		LineArray lineArr = new LineArray(num * 2,         // give two points for each line
				GeometryArray.COLOR_3 | GeometryArray.COORDINATES);

		Point3f c_pts[] = circle_Points(0.0f, r, num);     // prepare points on the circle

		for (int i = 0; i <  num; i++) {                   // define lines for the start
			lineArr.setCoordinate(i * 2, c_pts[i]);
			lineArr.setCoordinate(i * 2 + 1, c_pts[(i + 1) % num]);
			lineArr.setColor(i * 2, clr);    
			lineArr.setColor(i * 2 + 1, CommonsEJ.White);  // set color for end points
		}

		return lineArr;
	}
			
	/* a function to create and return a Shape3D with one LineArray */
	public static Shape3D line_Shape(float r) {
		int n = 60;                                         // create an n-sided start with 'r' radius
		LineArray starGeometry = one_Star(n, CommonsEJ.Black, r); 

		return new Shape3D(starGeometry);
	}
	
	public L2StarEJ(float r) {
		super(line_Shape(r));
	}
}
