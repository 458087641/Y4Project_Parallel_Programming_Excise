package exercises.mandelbrot;

import java.awt.Dimension;
import java.awt.Graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.concurrent.TimeUnit;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 * The UI implementation of Mandelbrot graph rendering exercise refers to Gustavo Pinto https://github.com/catree/SimpleMandelbrotDemo
 */
public class Mandelbrot extends JFrame {


	public double x1 = -2.0;
	public double x2 = 0.5;
	public double y1  = -1.0;
	public double y2 = 1.0;
	public double scaleX;
	public double scaleY;
	public int maxIteration = 250;
	public int width  = 1920;
	public int height = 1080;

	public BufferedImage renderImage = null;

	private Canvas canvas;
	private JButton renderButton;
	private JTextField infoText;
	private JMenuBar menuBar;


	
	private Mandelbrot instance;
	private boolean isComputing = false;
	protected int coreNum = Runtime.getRuntime().availableProcessors();
	protected int threadsNum = Runtime.getRuntime().availableProcessors();


	
	
	public Mandelbrot() {
		super("Mandelbrot");
		instance = this;
		initialize();
		setMinimumSize(new Dimension(1080, 720));
		setSize(1920, 1080);
		setVisible(true);
	}
	
	private void initialize() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		canvas = new Canvas();

		renderButton = new JButton("Render");
		renderButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				computeMandelbrot();
			}
		});

		infoText = new JTextField("Number of threads : " + threadsNum);
		infoText.setColumns(50);
		infoText.setEditable(false);

		JPanel interactionPanel = new JPanel();
		interactionPanel.add(renderButton);
		interactionPanel.add(infoText);


		JMenuItem paramItem = new JMenuItem("Parameters");
		paramItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new ParametersDialog(instance);
			}
		});


		menuBar = new JMenuBar();
		menuBar.add(paramItem);
		setJMenuBar(menuBar);

		add("Center", canvas);
		add("South", interactionPanel);
	}
    private void computeMandelbrot() {
    	scaleX = width / (x2 - x1) ;
    	scaleY = height / (y2 - y1) ;
		renderImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    	repaint();
    	SwingWorker<Object, Object> worker = new SwingWorker<>() {
			@Override
			protected Object doInBackground() throws Exception {
				multithread();
				return null;
			}
		};
    	worker.execute();
    }
    
    protected void multithread() {
		long before=System.nanoTime();
		ExecutorService executor = Executors.newFixedThreadPool(threadsNum);
		int step = (int) Math.floor(width / (double) threadsNum);
		for (int j = 0; j < threadsNum; j++) {
			int start = step * j;
			int end = start + step;
			if (j == threadsNum - 1) {
				end = width ;
			}
			executor.submit(new RenderThread(instance, 0, height, start, end,j));
		}
		executor.shutdown();
		try {
			executor.awaitTermination(3600, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long after=System.nanoTime();
		final double time = (after - before) / 1000000000.0;
		final DecimalFormat df = new DecimalFormat("#.###");
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				infoText.setText("Number of Threads : " + threadsNum + " Processing time : " + df.format(time) + " s");
			}
		});

    }
    
    public void setThreadsNum(int num) {
    	threadsNum = num;
    	infoText.setText("number of threads : " + threadsNum);
    }

	public void setMaxIteration(int num){
		maxIteration = num;
	}
    private class Canvas extends JPanel {
		public Canvas() {
		}

		@Override
    	public void paintComponent(Graphics g) {
    		super.paintComponent(g);
			g.drawImage(renderImage, 0, 0, getWidth(), getHeight(), this);
    	}
    }

    public static void main(String[] args)  {
		new Mandelbrot();

    }
}

