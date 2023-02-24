package exercises.maths;

import java.awt.Color;
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
    	if(maxIter - Mandelbrot.mandelbrot(z0, maxIter) == 0) {
    		return 0;
    	}
    	return Mandelbrot.mandelbrot(z0, maxIter) * 255 / maxIter ;
    }
    
    public static Color blackAndWhiteMandelbrot(Complex z0, int maxIter) {
    	int colorValue = Mandelbrot.colorMandelbrotFormula(z0, maxIter);
        return new Color(colorValue, colorValue, colorValue);

    }
    
    public static Color redMandelbrot(Complex z0, int maxIter, Color c) {
    	int colorValue = Mandelbrot.colorMandelbrotFormula(z0, maxIter);
        if (colorValue > 100 ){
            System.out.println(colorValue);
        }
        return new Color(colorValue, c.getGreen(), c.getBlue());
    }
    
    public static Color greenMandelbrot(Complex z0, int maxIter, Color c) {
    	int colorValue = Mandelbrot.colorMandelbrotFormula(z0, maxIter);
        return new Color(c.getRed(), colorValue, c.getBlue());
    }
    
    public static Color blueMandelbrot(Complex z0, int maxIter, Color c) {
    	int colorValue = Mandelbrot.colorMandelbrotFormula(z0, maxIter);
        return new Color(c.getRed(), c.getGreen(), colorValue);
    }
}
