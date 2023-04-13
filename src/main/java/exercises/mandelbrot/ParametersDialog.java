package exercises.mandelbrot;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ParametersDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private JLabel threadLabel;
	private JSpinner threadsNumbox;
	private JLabel iterLabel;
	private JSpinner iterBox;

	private JButton okButton;
	private JButton cancelButton;
	
	private Mandelbrot mandelbrot;
	
	
	public ParametersDialog(JFrame parent) {
		super(parent, "Parameters", true);
		
		mandelbrot = (Mandelbrot) parent;
		initialize();
		setMinimumSize(new Dimension(500, 500));
		setLocationRelativeTo(parent);
		setVisible(true);
	}
	
	private void initialize() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		JPanel centerPanel = new JPanel(new GridBagLayout());
		threadLabel = new JLabel("number of threads");

		threadsNumbox=new JSpinner(new SpinnerNumberModel(1, 1, mandelbrot.coreNum, 1));


		iterLabel = new JLabel("Max Iterations");
		iterBox = new JSpinner(new SpinnerNumberModel(1000, 1, 10000, 1));

		GridBagConstraints c1 = new GridBagConstraints();
		GridBagConstraints c2 = new GridBagConstraints();
		GridBagConstraints c3 = new GridBagConstraints();
		GridBagConstraints c4 = new GridBagConstraints();

		centerPanel.add(threadLabel, c1);
		centerPanel.add(threadsNumbox, c2);
		centerPanel.add(iterLabel,c3);
		centerPanel.add(iterBox,c4);


		okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mandelbrot.setThreadsNum((Integer) threadsNumbox.getValue());
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
