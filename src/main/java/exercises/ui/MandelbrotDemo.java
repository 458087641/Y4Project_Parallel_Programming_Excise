package exercises.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.concurrent.TimeUnit;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class MandelbrotDemo extends JFrame {

	private static final long serialVersionUID = 1L;
	protected double x1 = -2.0;
	protected double x2 = 0.5;
	protected double y1  = -1.0;
	protected double y2 = 1.0;
	protected double zoomX;
	protected double zoomY;
	protected int maxIteration = 255;
	protected int width  = 1920;
	protected int height = 1080;

	
	protected int[]imageArray = null;
	protected BufferedImage renderImage = null;
	protected Object lock = new Object();
	
	private Canvas canvas;
	private JButton startButton;
	private JTextField infoTextField;
	private JMenuBar menuBar;
	private JMenu menu1;

	
	private MandelbrotDemo instance;
	private boolean isComputing = false;
	protected int coreNum = Runtime.getRuntime().availableProcessors();
	protected int threadsNum = Runtime.getRuntime().availableProcessors();


	
	
	public MandelbrotDemo() {
		super("Mandel");

		instance = this;
		initComponents();
		setMinimumSize(new Dimension(1080, 720));
		setSize(1920, 1080);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void initComponents() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		canvas = new Canvas();

		startButton = new JButton("Render");
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				computeMandelbrot();
			}
		});

		infoTextField = new JTextField("Number of threads : " + threadsNum);
		infoTextField.setColumns(40);
		infoTextField.setEditable(false);

		JPanel interactionPanel = new JPanel();
		interactionPanel.add(startButton);
		interactionPanel.add(infoTextField);

		menu1 = new JMenu("Option");
		JMenuItem paramItem = new JMenuItem("Parameters");
		paramItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new ParametersDialog(instance);
			}
		});

		menu1.add(paramItem);

		menuBar = new JMenuBar();
		menuBar.add(menu1);
		setJMenuBar(menuBar);

		add("Center", canvas);
		add("South", interactionPanel);
	}
    private void computeMandelbrot() {
    	zoomX = width / (x2 - x1) ;
    	zoomY = height / (y2 - y1) ;
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
    	long before = Calendar.getInstance().getTimeInMillis();
		ExecutorService executor = Executors.newFixedThreadPool(threadsNum);
		int stepJ = (int) Math.floor(width / (double) threadsNum);

		for (int j = 0; j < threadsNum; j++) {
			int startJ = stepJ * j;
			int endJ = startJ + stepJ;
			if (j == threadsNum - 1) {
				endJ = width - 1;
			}
			executor.submit(new RenderThread(instance, 0, height, startJ, endJ,j));
		}
		/*
		if (optimise==false) {
			pass;
		}
		else {
			if (threadsNum % 2 == 0) {
				stepI = (int) Math.floor(height / 2.0);
				stepJ = (int) Math.floor(width / (double) (threadsNum / 2));

				for (int i = 0; i < 2; i++) {
					for (int j = 0; j < threadsNum / 2; j++) {
						int startI = stepI * i;
						int endI = startI + stepI;
						int startJ = stepJ * j;
						int endJ = startJ + stepJ;

						if (j == threadsNum / 2 - 1) {
							endJ = width - 1;
						}
						if (i == 1) {
							endI = height - 1;
						}
						executor.submit(new RenderThread(instance, startI, endI, startJ, endJ,-1));
					}
				}
			} else {
				stepJ = (int) Math.floor(width / (double) threadsNum);

				for (int j = 0; j < threadsNum; j++) {
					int startJ = stepJ * j;
					int endJ = startJ + stepJ;

					if (j == threadsNum - 1) {
						endJ = width - 1;
					}
					executor.submit(new RenderThread(instance, 0, height, startJ, endJ,-1));
				}
			}
		}
		*/

		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long after = Calendar.getInstance().getTimeInMillis();

		final double time = (after - before) / 1000.0;
		final DecimalFormat df = new DecimalFormat("#.###");
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				infoTextField.setText("Number of Threads : " + threadsNum + " Processing time : " + df.format(time) + " s");
			}
		});

    }
    
    public void setThreadsNum(int num) {
    	threadsNum = num;
    	infoTextField.setText("number of threads : " + threadsNum);
    }

	public void setMaxIteration(int num){
		maxIteration = num;
	}
    private class Canvas extends JPanel {
		private static final long serialVersionUID = 1L;

		public Canvas() {
		}

		@Override
    	public void paintComponent(Graphics g) {
    		super.paintComponent(g);
    		Graphics2D g2 = (Graphics2D) g;
    		synchronized (lock) {
	    		if(null != renderImage) {
	    			g2.drawImage(renderImage, 0, 0, getWidth(), getHeight(), this);
	    		}
    		}

    	}
    }

    public static void main(String[] args)  {
        SwingUtilities.invokeLater(new Runnable() {			
			@Override
			public void run() {
				new MandelbrotDemo();
			}
		});
    }
}

