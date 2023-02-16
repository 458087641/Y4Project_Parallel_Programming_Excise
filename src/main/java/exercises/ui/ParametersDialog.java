package exercises.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ParametersDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private JLabel sliderLabel;
	private JSlider threadsNumSlider;
	private JLabel iterLabel;
	private JSpinner iterBox;

	private JButton okButton;
	private JButton cancelButton;
	
	private MandelbrotDemo mandelbrot;
	
	
	public ParametersDialog(JFrame parent) {
		super(parent, "Parameters", true);
		
		mandelbrot = (MandelbrotDemo) parent;
		initComponents();
		
		setMinimumSize(new Dimension(650, 550));
		setLocationRelativeTo(parent);
		setVisible(true);
	}
	
	private void initComponents() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		JPanel centerPanel = new JPanel(new GridBagLayout());
		sliderLabel = new JLabel("number of threads :");

		threadsNumSlider = new JSlider(1, 2 * mandelbrot.coreNum, mandelbrot.threadsNum);
		threadsNumSlider.setMajorTickSpacing(mandelbrot.coreNum / 2 );
		threadsNumSlider.setMinorTickSpacing(1);
		threadsNumSlider.setPaintTicks(true);
		threadsNumSlider.setPaintLabels(true);

		iterLabel = new JLabel("Max Iterations");
		iterBox = new JSpinner(new SpinnerNumberModel(1000, 1, 10000, 1));

		GridBagConstraints c1 = new GridBagConstraints();
		GridBagConstraints c2 = new GridBagConstraints();
		GridBagConstraints c3 = new GridBagConstraints();
		GridBagConstraints c4 = new GridBagConstraints();

		centerPanel.add(sliderLabel, c1);
		centerPanel.add(threadsNumSlider, c2);
		centerPanel.add(iterLabel,c3);
		centerPanel.add(iterBox,c4);


		okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mandelbrot.setThreadsNum(threadsNumSlider.getValue());
				mandelbrot.setMaxIteration((Integer) iterBox.getValue());
				dispose();
			}
		});
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		JPanel interactionPanel = new JPanel();
		interactionPanel.add(okButton);
		interactionPanel.add(cancelButton);
		
		add("Center", centerPanel);
		add("South", interactionPanel);
	}

}
