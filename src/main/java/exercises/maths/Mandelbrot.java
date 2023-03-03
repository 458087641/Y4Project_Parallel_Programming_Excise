package exercises.maths;

import java.awt.Color;
import java.util.Random;

public class Mandelbrot {

    public static int mandelbrot(Complex z0, int max) {
        Complex z = new Complex(0,0);
        for (int t = 0; t < max; t++) {
            if (z.abs() > 2.0) return t;
            z = z.times(z).plus(z0);
        }
        return max;
    }

    public static int colorMandelbrotFormula(Complex z0, int maxIter) {
    	if(Mandelbrot.mandelbrot(z0, maxIter) == maxIter) {
    		return 255;
    	}
    	return Mandelbrot.mandelbrot(z0, maxIter) * 255 / maxIter ;
    }
}
