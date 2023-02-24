package exercises.ui;

import java.awt.Color;

import exercises.maths.Complex;
import exercises.maths.Mandelbrot;

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
                
                Complex z0 = new Complex(x0, y0);
                Color color = new Color(168, 32, 32);
				//color = Mandelbrot.greyMandelbrot(z0, mandelbrot.maxIteration);
				if(colorInt==-1) {
					color = Mandelbrot.blackAndWhiteMandelbrot(z0, mandelbrot.maxIteration);
				}
				if(colorInt == 0){
					color = Mandelbrot.redMandelbrot(z0, mandelbrot.maxIteration, color);
				}
				if(colorInt == 1){
					color = new Color(37, 168, 32);

					color = Mandelbrot.greenMandelbrot(z0, mandelbrot.maxIteration, color);
				}
				if(colorInt == 2){
					color = new Color(12, 29, 93);
					color = Mandelbrot.blueMandelbrot(z0, mandelbrot.maxIteration, color);
				}
				if(colorInt == 3){
					color = Mandelbrot.blackAndWhiteMandelbrot(z0, mandelbrot.maxIteration);
				}
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
