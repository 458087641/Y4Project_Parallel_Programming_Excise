package exercises.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.JSlider;

public class ParametersDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private JLabel sliderLabel;
	private JSlider nbThreadsSlider;


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
		sliderLabel = new JLabel("Nb threads :");

		nbThreadsSlider = new JSlider(1, 2 * mandelbrot.nbCores, mandelbrot.nbThreads);
		nbThreadsSlider.setMajorTickSpacing(mandelbrot.nbCores / 2 );
		nbThreadsSlider.setMinorTickSpacing(1);
		nbThreadsSlider.setPaintTicks(true);
		nbThreadsSlider.setPaintLabels(true);

		GridBagConstraints c1 = new GridBagConstraints();
		c1.gridx = 0;
		c1.gridy = 0;
		c1.weightx = 0;
		c1.weighty = 0;
		c1.anchor = GridBagConstraints.WEST;
		c1.insets = new Insets(10, 10, 5, 5);

		GridBagConstraints c2 = new GridBagConstraints();
		c2.gridx = 1;
		c2.gridy = 0;
		c2.gridwidth = 5;
		c2.weightx = 100;
		c2.weighty = 0;
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.insets = new Insets(10, 5, 10, 10);


		centerPanel.add(sliderLabel, c1);
		centerPanel.add(nbThreadsSlider, c2);



		okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mandelbrot.setNbCores(nbThreadsSlider.getValue());

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
