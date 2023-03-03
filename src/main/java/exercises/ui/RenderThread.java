package exercises.ui;

import java.awt.Color;

import exercises.maths.Complex;
import exercises.maths.Mandelbrot;
import java.util.Random;
public class RenderThread extends Thread {

	private MandelbrotDemo mandelbrot;
	private int startX;
	private int endX;
	private int startY;
	private int endY;
	private int colorInt;
	
	public RenderThread(MandelbrotDemo mandelbrot, int startX, int endX, int startY, int endY, int color) {
		this.mandelbrot = mandelbrot;
		this.startX = startX;
		this.endX = endX;
		this.startY = startY;
		this.endY = endY;
		this.colorInt = color;
	}

	@Override
	public void run() {
		for (int i = startX; i < endX; i++) {
            for (int j = startY; j <endY; j++) {
                double x0 = j / mandelbrot.zoomX + mandelbrot.x1;
                double y0 = i / mandelbrot.zoomY + mandelbrot.y1;
				Random rand = new Random(colorInt);
				int r = rand.nextInt(255);
				int g = rand.nextInt(255);
				int b = rand.nextInt(255);
                Complex z0 = new Complex(x0, y0);
				//color = Mandelbrot.redMandelbrot(z0, mandelbrot.maxIteration, color);
				int value = Mandelbrot.colorMandelbrotFormula(z0,mandelbrot.maxIteration);
				Color color =  new Color(value*r/255, value*g/255, value*b/255);
				mandelbrot.renderImage.setRGB(j, i, color.getRGB());
            	mandelbrot.repaint();
            }            
        }
	}

}
