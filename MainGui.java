import javax.swing.*;

import java.awt.* ;
import java.awt.event.* ;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;



/**
 * @author Kyle
 * 
 * This program generates population data in .csv format based upon specified parameters.
 * Parameters are read as inputs from the GUI.
 */
public class MainGui extends JFrame  {
    
    //Gui Parts
    private JTextField popSize, numPops, numGens;
    private JTextField a1a1fit, a1a2fit, a2a2fit, initA1;
    private JRadioButton bottleYes, bottleNo;
    private ButtonGroup bottleGroup;
    private JTextField bottlePop, bottleTime, mutRate;
    private JTextArea display;
    private JButton run;
    
    
    
    /**		This is creates the GUI. It has any number of rows, and 3 columns
     * 		
     */
    public MainGui(){
    	
    	//Title
        setTitle("Population Change Analysis");
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE ) ;
        setLayout ( new BorderLayout()) ;
        
        JPanel nPanel = new JPanel() ;
        nPanel.setLayout(new GridLayout(0,3));										//any number of rows, 3 columns
        nPanel.setBorder(BorderFactory.createTitledBorder("Set Parameters"));		//border
        
        //Row 1
        nPanel.add(new JLabel("Population size: "));
        popSize = new JTextField();	
        nPanel.add(popSize);
        nPanel.add(new JLabel());
        
        //Row 2
        nPanel.add(new JLabel("Number of Populations: "));	
        numPops = new JTextField();
        nPanel.add(numPops);
        nPanel.add(new JLabel());
        
        //Row 3
        nPanel.add(new JLabel("Number of Generations: "));	
        numGens = new JTextField();
        nPanel.add(numGens);
        nPanel.add(new JLabel());
        
        //Row 4
        nPanel.add(new JLabel("Initial A1 frequency: "));	
        initA1 = new JTextField();
        nPanel.add(initA1);
        nPanel.add(new JLabel());
        
        //Space
        nPanel.add(new JLabel());
        nPanel.add(new JLabel());
        nPanel.add(new JLabel());
        
        //row 4.5
        nPanel.add(new JLabel("Fitness values must be less than or equal to 1"));
        nPanel.add(new JLabel());
        nPanel.add(new JLabel());
        
        //Row 5
        nPanel.add(new JLabel("A1A1 Fitness"));
        nPanel.add(new JLabel("A1A2 Fitness"));
        nPanel.add(new JLabel("A2A2 Fitness"));
        
        //Space
        nPanel.add(new JLabel());
        nPanel.add(new JLabel());
        nPanel.add(new JLabel());
        
        //Row 6
        a1a1fit = new JTextField();
        nPanel.add(a1a1fit);
        
        a1a2fit = new JTextField();
        nPanel.add(a1a2fit);
        
        a2a2fit = new JTextField();
        nPanel.add(a2a2fit);
        
        //Row 7
        nPanel.add(new JLabel());
        nPanel.add(new JLabel());
        nPanel.add(new JLabel());
       
        //Row 8
        nPanel.add(new JLabel("Mutation Rate (A1 into A2): "));
        mutRate = new JTextField();
        nPanel.add(mutRate);
        nPanel.add(new JLabel());
        
        //Row 9
        nPanel.add(new JLabel("Bottleneck: "));
        bottleYes = new JRadioButton( "Yes", false);
        bottleNo = new JRadioButton( "No", true);
        bottleGroup = new ButtonGroup();      
        bottleGroup.add(bottleYes);
        bottleGroup.add(bottleNo);
        
        JPanel bottleButton = new JPanel();
        bottleButton.setLayout(new FlowLayout());
        bottleButton.add(bottleYes);
        bottleButton.add(bottleNo);
        nPanel.add(bottleButton);
        
        nPanel.add(new JLabel());
        
        //Row 10
        nPanel.add(new JLabel("Bottleneck Population, Generation Range: "));
       
        bottlePop = new JTextField();
        nPanel.add(bottlePop);
        
        bottleTime = new JTextField("start     end");
        nPanel.add(bottleTime);
        
        
        
       //Lower Area
        add( nPanel, BorderLayout.NORTH) ;    
     
        JPanel cPanel = new JPanel();
        cPanel.setBorder(BorderFactory.createTitledBorder("Run (Outputs to Excel)"));
        run = new JButton("Run") ;
        cPanel.add( run ) ;
        run.addActionListener( 
                new ActionListener() {
                    public void actionPerformed( ActionEvent e ){
                        try {
							doAnalysis() ;
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							display.setText("Cannot Generate File");
						}
                    }
                }
        ) ;
          
        add( cPanel, BorderLayout.CENTER) ;    
        
        display = new JTextArea("Input parameters, and press Run") ;
		JScrollPane scroller2 = new JScrollPane (display);
		scroller2.setPreferredSize(new Dimension(400,200));	
		scroller2.setBorder(BorderFactory.createTitledBorder("Display Area"));
		add(scroller2, BorderLayout.SOUTH); 
		
        pack();

        setVisible(true ) ;

    }
    
    
    
    /**This method runs when the run button is pressed. It creates and runs populations/reproduction.
     * It outputs data to a .csv file.
     * 
     * @throws IOException	When created file is already open, it cannot be written to.
     */
    private void doAnalysis() throws IOException {
   
    //acquire parameters for the simulation	
    	//calculate bottleneck segment
    	int botStart = -1;
    	int botEnd = -1;
    	if(bottleYes.isSelected()){
    		Scanner scan = new Scanner(bottleTime.getText());
    		botStart = scan.nextInt();
    		botEnd = scan.nextInt();
    	}
    	//-----------
    	
    	int populations = Integer.parseInt(numPops.getText());
    	int populationSize = Integer.parseInt(popSize.getText());
    	double A1Freq = Double.parseDouble(initA1.getText());
    	int generations = Integer.parseInt(numGens.getText());
    	
    	PrintWriter output = null;
    	
    	
    //start simulating	
    	for(int i = 1; i <= populations; i++){								//for each populations
    		Population tempPop = new Population(populationSize, A1Freq);		//generate the population
    		String fileName = "output" + i +".csv";
    		output = new PrintWriter(new FileWriter(fileName));
    		
    		output.println("Generation,A1 Frequency,A2 Frequency,A1A1 Frequency,A1A2 Frequency,A2A2 Frequency");
    		for(int j = 0; j <= generations; j++){								//for each generation 
    			printGen(j, output, tempPop);										//output current generation statistics to file
    			selection(tempPop);													//apply selection
    			
    			//check for bottleneck and within its duration
    			if(bottleYes.isSelected() && j >= botStart && j <= botEnd){		//if within a bottleneck
    				tempPop.nextGen(Integer.parseInt(bottlePop.getText()));			//generate next generation with the specified bottleneck population size
        			tempPop.calcFreqs();
    			}
    			else{															//otherwise
    				
    			tempPop.nextGen(populationSize);									//generate next generation with the normal population size
    			
    			//apply the mutation rate
    			if(mutRate.getText().isEmpty() == false){						
    			tempPop.applyMutation(Double.parseDouble(mutRate.getText()));		
    			}
    			tempPop.calcFreqs();
    			}    			
    		}
    		output.close();
    	}
    
    //after simulating
    	display.setText("Complete");
		display.repaint();
    }
    	
	/**Outputs generation data to file.
	 * 
	 * @param generation
	 * @param output
	 * @param tempPop
	 */
	private void printGen(int generation, PrintWriter output, Population tempPop){
		output.print(generation);
		//System.out.println("\nGeneration: " + generation);	for debug purposes
		output.print("," + tempPop.getA1Freq());
		output.print("," + tempPop.getA2Freq());
		output.print("," + tempPop.getA1A1Freq());
		output.print("," + tempPop.getA1A2Freq());
		output.println("," + tempPop.getA2A2Freq());
	}
    	
	 /**This checks to see if selection is in place. If it is, it applies selection
	  * to organisms in population. (Shortcut in case no selection applied).
	  * 
	 * @param pop	is the population selection will be applied to
	 */
	private void selection(Population pop){
		 
		 Double a1a1 = Double.parseDouble(a1a1fit.getText());
		 Double a1a2 = Double.parseDouble(a1a2fit.getText());
		 Double a2a2 = Double.parseDouble(a2a2fit.getText());
	
	    	//if selection is in effect
	    	if(a1a1 != 1 || a1a2 != 1 || a2a2 != 1){
	    		pop.selection(a1a1, a1a2, a2a2);
	    	}
	    }
    
    /*
     * Start everything up and set the defaults
     */
    public static void main(String[] args) {
        MainGui window = new MainGui();    
    }
}

