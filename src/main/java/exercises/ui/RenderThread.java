package exercises.ui;

import java.awt.Color;

import exercises.maths.Complex;

import java.util.Random;
public class RenderThread extends Thread {

	private Mandelbrot mandelbrot;
	private int startX;
	private int endX;
	private int startY;
	private int endY;
	private int colorInt;
	
	public RenderThread(Mandelbrot mandelbrot, int startY, int endY, int startX, int endX, int color) {
		this.mandelbrot = mandelbrot;
		this.startX = startX;
		this.endX = endX;
		this.startY = startY;
		this.endY = endY;
		this.colorInt = color;
	}

	@Override
	public void run() {
		for (int i = startY; i < endY; i++) {
            for (int j = startX; j <endX; j++) {
                double x = j / mandelbrot.zoomX + mandelbrot.x1;
                double y = i / mandelbrot.zoomY + mandelbrot.y1;

				Random rand = new Random(colorInt);
				int r = rand.nextInt(255);
				int g = rand.nextInt(255);
				int b = rand.nextInt(255);
                Complex z = new Complex(x, y);
				//color = Mandelbrot.redMandelbrot(z0, mandelbrot.maxIteration, color);
				int value = colorMandelbrot(z,mandelbrot.maxIteration);
				Color color =  new Color(value*r, value*g, value*b);
				mandelbrot.renderImage.setRGB(j, i, color.getRGB());
            	mandelbrot.repaint();
            }            
        }
	}
	public static int mandelbrot(Complex z0, int max) {
		Complex z = new Complex(0,0);
		for (int t = 0; t < max; t++) {
			if (z.abs() > 2.0) return t;
			z = z.times(z).plus(z0);
		}
		return max;
	}

	public static int colorMandelbrot(Complex z0, int maxIter) {
		if(mandelbrot(z0, maxIter) == maxIter) {
			return 1;
		}
		return mandelbrot(z0, maxIter) / maxIter ;
	}

}
