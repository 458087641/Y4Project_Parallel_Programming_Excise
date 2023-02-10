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
	private boolean isBenchmarking;
	
	
	public RenderThread(MandelbrotDemo mandelbrot, int startX, int endX, int startY, int endY, boolean isBenchmarking) {
		this.mandelbrot = mandelbrot;
		this.startX = startX;
		this.endX = endX;
		this.startY = startY;
		this.endY = endY;
		this.isBenchmarking = isBenchmarking;
	}

	@Override
	public void run() {
		for (int i = startX; i < endX; i++) {
            for (int j = startY; j <endY; j++) {
                double x0 = j / mandelbrot.zoomX + mandelbrot.x1;
                double y0 = i / mandelbrot.zoomY + mandelbrot.y1;
                
                Complex z0 = new Complex(x0, y0);
                Color color = new Color(168, 32, 32);

                switch(mandelbrot.colorMethod) {
                case 0:
                	color = Mandelbrot.blackAndWhiteMandelbrot(z0, mandelbrot.maxIteration);
                	break;
                case 1:
                	color = Mandelbrot.greyMandelbrot(z0, mandelbrot.maxIteration);
                	break;
                case 2:
                	color = Mandelbrot.redMandelbrot(z0, mandelbrot.maxIteration, mandelbrot.color);
                	break;
                case 3:
                	color = Mandelbrot.greenMandelbrot(z0, mandelbrot.maxIteration, mandelbrot.color);
                	break;
                case 4:
                	color = Mandelbrot.blueMandelbrot(z0, mandelbrot.maxIteration, mandelbrot.color);
                	break;
            	default:
            		color = Mandelbrot.blackAndWhiteMandelbrot(z0, mandelbrot.maxIteration);
            		break;
                }

                if(!isBenchmarking && mandelbrot.isLiveRendering) {
					//Not needed as setRGB is already synchronized
                	//@see: http://hg.openjdk.java.net/jdk7/jdk7/jdk/file/9b8c96f96a0f/src/share/classes/java/awt/image/BufferedImage.java#l987
                    //synchronized (mandelbrot.lock) {
                    	mandelbrot.renderImage.setRGB(j, i, color.getRGB());
    				//}
                } else {
                	mandelbrot.imageArray[i * mandelbrot.width + j] = color.getRGB();
                }
            }
            
            if(!isBenchmarking && mandelbrot.isLiveRendering) {
            	mandelbrot.repaint();
            }            
        }
	}

}
