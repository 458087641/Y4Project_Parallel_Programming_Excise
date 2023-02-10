package exercises.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import java.text.DecimalFormat;
import java.util.ArrayDeque;
import java.util.Calendar;
import java.util.Deque;
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
	protected double x1 = -2.1;
	protected double x2 = 0.6;
	protected double y1  = -1.2;
	protected double y2 = 1.2;
	protected double zoomX;
	protected double zoomY;
	
	protected int maxIteration = 1000;
	protected int width  = 1920;
	protected int height = 1080;
	
	protected int patchSizeIndex = 1;
	protected int[][]  patchSize = new int[][]{
			{5, 5},
			{10, 10},
			{25, 25},
			{50, 50},
			{100, 100}
			};
	
	protected boolean isFixeSize = false;
	protected int colorMethod = 1;
	protected Color color = new Color(255, 255, 255);
	protected int multithreadMethod = 0;
	protected boolean isLiveRendering = true;
	
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
	protected int nbCores = Runtime.getRuntime().availableProcessors();
	protected int nbThreads = Runtime.getRuntime().availableProcessors();
	
	private Deque<double[]> stackOfZoom = null;
	private Deque<BufferedImage> stackOfZoomImage = null;
	
	protected int threshold = 10000;
	
	
	public MandelbrotDemo() {
		super("mand");

		instance = this;
		stackOfZoomImage = new ArrayDeque<BufferedImage>();
		stackOfZoom = new ArrayDeque<double[]>();
		
		initComponents();
		
		setMinimumSize(new Dimension(640, 480));
		setSize(850, 850);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void initComponents() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		canvas = new Canvas();

		startButton = new JButton("Start !");
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				computeMandelbrot();
				if (stackOfZoomImage.isEmpty()) {
					stackOfZoomImage.add(renderImage);
					stackOfZoom.add(new double[]{x1, y1, x2, y2});
				}
			}
		});

		infoTextField = new JTextField("nb threads : " + nbThreads);
		infoTextField.setColumns(26);
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
    	isComputing = true;
    	startButton.setEnabled(false);
    	menu1.setEnabled(false);

    	setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    	
    	if(!isFixeSize) {
        	width = canvas.getWidth();
        	height = canvas.getHeight();
    	}
		
    	zoomX = width / (x2 - x1);
    	zoomY = height / (y2 - y1);
    	
    	imageArray = new int[width * height];
    	renderImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    	repaint();
    	
    	SwingWorker<Object, Object> sw = new SwingWorker<Object, Object>() {
			@Override
			protected Object doInBackground() throws Exception {
				multithread(false);
				return null;
			}
			
			@Override
			protected void done() {
				startButton.setEnabled(true);
		    	menu1.setEnabled(true);
				isComputing = false;
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				
				if(!isLiveRendering) {
					renderImage.setRGB(0, 0, renderImage.getWidth(), renderImage.getHeight(), imageArray, 0, renderImage.getWidth());
					repaint();
				}
			}
		};
    	sw.execute();
    }
    
    protected long multithread(boolean isBenchmarking) {
    	long before = Calendar.getInstance().getTimeInMillis();
		ExecutorService executor = Executors.newFixedThreadPool(nbThreads);
		
		int sqrNbThreads = (int) Math.sqrt(nbThreads);
		int stepI, stepJ;
		
		if(nbThreads % 2 == 0) {
			stepI = (int) Math.floor(height / 2.0);
			stepJ = (int) Math.floor(width / (double)(nbThreads/2));
			
			for(int i = 0; i<2; i++) {
				for(int j = 0; j<nbThreads/2; j++) {
					int startI 	 = stepI * i;
					int endI	 = startI + stepI;
					int startJ 	 = stepJ * j;
					int endJ 	 = startJ + stepJ;
					
					if(j == nbThreads/2 - 1) {
						endJ = width - 1;
					}
					if(i == 1) {
						endI = height - 1;
					}
					executor.submit(new RenderThread(instance, startI, endI, startJ, endJ, isBenchmarking));
				}
			}
		} else {
			stepI = height;
			stepJ = (int) Math.floor(width / (double)nbThreads);
			
			for(int j = 0; j<nbThreads; j++) {
				int startJ 	 = stepJ * j;
				int endJ 	 = startJ + stepJ;
				
				if(j == nbThreads-1) {
					endJ = width - 1;
				}
				executor.submit(new RenderThread(instance, 0, height, startJ, endJ, isBenchmarking));
			}
		}
		
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		long after = Calendar.getInstance().getTimeInMillis();
		if(!isBenchmarking) {
			final double time = (after - before) / 1000.0;
			final DecimalFormat df = new DecimalFormat("#.###");
			SwingUtilities.invokeLater(new Runnable() {					
				@Override
				public void run() {
					infoTextField.setText("nb threads : " + nbThreads + "  /  processing time : " + df.format(time) + " s");
				}
			});
		}
		
		return after - before;
    }
    
    public void setNbCores(int nb) {
    	nbThreads = nb;
    	infoTextField.setText("nb threads : " + nbThreads);
    }
    
    private class Canvas extends JPanel {
		private static final long serialVersionUID = 1L;
		private MyRect currentRect = null;
		private boolean mouseDragged = false;
		
		public Canvas() {
			addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
					if(null != renderImage && !isComputing && mouseDragged) {
						renderImage = null;

						updateSize(e);
						if(currentRect.isZooming()) {
				            
				        	zoomX 	= width / (x2 - x1);
				        	zoomY 	= height / (y2 - y1);
				            
				        	double oldX1 = x1, oldY1 = y1;
				        	
							x1 = (currentRect.getLeftSide() * width / getWidth()) / zoomX + oldX1;
							x2 = (currentRect.getRightSide() * width / getWidth()) / zoomX + oldX1;
							y1 = (currentRect.getUpSide() * height / getHeight()) / zoomY + oldY1;
							y2 = (currentRect.getDownSide() * height / getHeight()) / zoomY + oldY1;
							
							stackOfZoom.add(new double[]{x1, y1, x2, y2});
							
							computeMandelbrot();
							stackOfZoomImage.add(renderImage);
						} else {
							stackOfZoom.pollLast();
							if(!stackOfZoom.isEmpty()) {
								x1 = stackOfZoom.getLast()[0];
								y1 = stackOfZoom.getLast()[1];
								x2 = stackOfZoom.getLast()[2];
								y2 = stackOfZoom.getLast()[3];
							}
							
							stackOfZoomImage.pollLast();
							if(!stackOfZoomImage.isEmpty()) {
								renderImage = stackOfZoomImage.getLast();
							}							
						}
					}
					
					currentRect = null;
					mouseDragged = false;
					repaint();
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					int x = e.getX();
		            int y = e.getY();
		            currentRect = new MyRect(x, y);
		            repaint();
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
				}
			});
			
			addMouseMotionListener(new MouseMotionListener() {				
				@Override
				public void mouseMoved(MouseEvent e) {					
				}
				
				@Override
				public void mouseDragged(MouseEvent e) {
					mouseDragged = true;
					updateSize(e);
				}
			});
		}
		
		void updateSize(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            currentRect.update(x, y);
            repaint();
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
    		
    		if(null != currentRect) {
    			g2.drawRect(currentRect.getLeftSide(), currentRect.getUpSide(), currentRect.getWidth(), currentRect.getHeight());
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

