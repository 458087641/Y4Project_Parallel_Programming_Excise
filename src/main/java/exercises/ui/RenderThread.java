package exercises.ui;

import java.awt.Color;

import exercises.maths.Complex;
import exercises.maths.Mandelbrot;

public class RenderThread implements Runnable {

	private MandelbrotDemo mandelbrot;
	private int startX;
	private int endX;
	private int startY;
	private int endY;
	
	
	public RenderThread(MandelbrotDemo mandelbrot, int startX, int endX, int startY, int endY) {
		this.mandelbrot = mandelbrot;
		this.startX = startX;
		this.endX = endX;
		this.startY = startY;
		this.endY = endY;
	}

	@Override
	public void run() {
		for (int i = startX; i < endX; i++) {
            for (int j = startY; j <endY; j++) {
                double x0 = j / mandelbrot.zoomX + mandelbrot.x1;
                double y0 = i / mandelbrot.zoomY + mandelbrot.y1;
                
                Complex z0 = new Complex(x0, y0);
                Color color = new Color(168, 32, 32);
				color = Mandelbrot.greyMandelbrot(z0, mandelbrot.maxIteration);

                if(mandelbrot.isLiveRendering) {
					mandelbrot.renderImage.setRGB(j, i, color.getRGB());
                } else {
                	mandelbrot.imageArray[i * mandelbrot.width + j] = color.getRGB();
                }
            }
            if(mandelbrot.isLiveRendering) {
            	mandelbrot.repaint();
            }            
        }
	}

}
