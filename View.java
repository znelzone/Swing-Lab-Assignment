import javax.swing.*;
import java.beans.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * View Class provides a graphical user interface for the thread sorts demo 
 * @author Lewis, Depasquale, and Chase
 * @version 4.0
 *
 */
public class View implements ActionListener, PropertyChangeListener
{
	private JFrame frame = new JFrame("Thread Sorts Demo");
	private JPanel pane = new JPanel(new GridLayout(6,1));

	private JLabel inputLabel = new JLabel("Enter number between 1000 and 1000000");
	private JButton go = new JButton("Go");
	private JLabel finished = new JLabel("Process Status:");

	//new label "clickLabel" to be added to the consturtor and the
	//pane within the constructor
	//--Zack
	private JLabel clickLabel = new JLabel("Click 'Click Me!' button to increase number on click counter:");
	//new clickMe button added --Zack
	private JButton clickMe = new JButton("Click Me!");


	private JTextField input = new JTextField();

	private JPanel inputPanel = new JPanel(new FlowLayout());

	//added a clickInputPanel where the click button and text
	//label will sit once added to the pane in the constructor
	//--Zack
	private JPanel clickInputPanel = new JPanel(new FlowLayout());

	private JPanel insertionPanel = new JPanel(new FlowLayout());
	
	private JProgressBar insertionBar = new JProgressBar();

	private JPanel quickPanel = new JPanel(new FlowLayout());
	
	private JProgressBar quickBar = new JProgressBar();
	
	private int userInput;
	
	private InsertionSortDemo insertionSort;
	private QuickSortDemo quickSort;
	
	private Integer[] data;
	private Integer[] insertionData;
	private Integer[] quickData;

	private JLabel insertionLabel = new JLabel("Insertion Sort:");
	private JLabel quickLabel = new JLabel("Quick Sort:");
	
	/**
	 * Constructor
	 */
	public View()
	{
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		input.setPreferredSize(new Dimension(150, 25));
        inputPanel.add(inputLabel);
		inputPanel.add(input);
		inputPanel.add(go);
		
		insertionPanel.add(insertionLabel);
		insertionPanel.add(insertionBar);
		quickPanel.add(quickLabel);
		quickPanel.add(quickBar);

		//new clickInputPanel to be added to the
		//pane
		//new button clickMe --Zack
		clickInputPanel.add(clickLabel);
		clickInputPanel.add(clickMe);

		pane.add(inputPanel);
		pane.add(insertionPanel);
		pane.add(quickPanel);
		pane.add(finished);
		//adds the new click button panel called "clickInputPanel" to
		//the window pane that's being created.
		//--Zack
		pane.add(clickInputPanel);

		go.addActionListener(this);
		//added new action listener for clickMe button --Zack
		clickMe.addActionListener(this);

		frame.add(pane);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
	}
	
	/**
	 * actionPerformed method is called when an object to which it is attached 
	 * receives an action event
	 * @param ActionEvent arg0 the event to be handled
	 */
	
	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		if(arg0.getSource() == go)
		{
			
			try{
			if(Integer.parseInt(input.getText()) > 1000000 || Integer.parseInt(input.getText()) < 1000)
				JOptionPane.showMessageDialog(null, "Enter an integer between 1000 and 1000000");
			else
			{
				userInput = Integer.parseInt(input.getText());
				createArray();
				runThreads();
			}
			}
			catch (java.lang.NumberFormatException e)
			{
				JOptionPane.showMessageDialog(null, "Input must be an integer between 1000 and 1000000");
			}
				
				
		}
	}
	
	/**
	 * private helper method to create an array of randomly generated integers
	 * and then copy it as needed
	 */	
	private void createArray()
	{
		//creates array 
		data = new Integer[userInput];
		
		Random generator = new Random();
		
		//loads array with random ints
		for(int x = 0; x < data.length; x++)
		{
			data[x] = generator.nextInt(1000000);
			
		}
		//creates "deep copies of array"
		insertionData = data.clone();
		quickData = data.clone();
	}		

	/**
	 * private helper method to execute the SwingWorker threads
	 */		
	private void runThreads()
	{
		//passes these copies into runnable classes
		insertionSort = new InsertionSortDemo(insertionData);
		quickSort = new QuickSortDemo(quickData);

		//add change listeners for each of the demos
		insertionSort.addPropertyChangeListener(this);
		quickSort.addPropertyChangeListener(this);
		
		//starting threads
		insertionSort.execute();
		quickSort.execute();

	}
	
	/**
	 * propertyChange method is called when an object to which it is attached 
	 * issues a property change event
	 * @param PropertyChangeEvent evt the event to be handled
	 */
    public void propertyChange(PropertyChangeEvent evt) 
	{
		
		if (evt.getSource().equals(insertionSort))
		{
		    if ("progress" == evt.getPropertyName()) 
		    {
			    int progress = (Integer) evt.getNewValue();
			    if (progress == 100) 
				{
				    insertionBar.setValue(progress);
				    finished.setText("Process Status: complete");
			    }
			    else 
			    {
				    finished.setText("Process Status: running");
				    insertionBar.setValue(progress);
			    }
            }
		}
		else if (evt.getSource().equals(quickSort))
		{
	        if ("progress" == evt.getPropertyName()) 
			{
				int progress = (Integer) evt.getNewValue();
				if (progress == 100) 
				{
					quickBar.setValue(progress);
					finished.setText("Process Status: complete");
				}
				else 
				{
					finished.setText("Process Status: running");
					quickBar.setValue(progress);
				}
			}
		}
	}
}