

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;


import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
//model is separate from the view.
/**
* class working as the controller
* Contains methods to set up GUI, read in file and the main method
* 
* @author Ebba Rosander
* 
*/
public class WordApp{
//shared variables
	static int noWords=4;
	static int totalWords;

   	static int frameX=1000;
	static int frameY=600;
	static int yLimit=480;

	static WordDictionary dict = new WordDictionary(); //use default dictionary, to read from file eventually

	static WordRecord[] words;
	static volatile boolean done;  //must be volatile
	static 	Score score = new Score();

	static WordPanel w;
	static volatile boolean gameRunning=false;
	static JLabel caught;
	static JLabel missed;
	static JLabel scr;
	
	
	
	public static void setupGUI(int frameX,int frameY,int yLimit) {
		// Frame init and dimensions
    	JFrame frame = new JFrame("WordGame"); 
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setSize(frameX, frameY);
    	
      	JPanel g = new JPanel();
        g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS)); 
      	g.setSize(frameX,frameY);
 
    	
		w = new WordPanel(words,yLimit);
		w.setSize(frameX,yLimit+100);
	    g.add(w);
	    
	    
	    JPanel txt = new JPanel();
	    txt.setLayout(new BoxLayout(txt, BoxLayout.LINE_AXIS)); 
	    caught =new JLabel("Caught: " + score.getCaught() + "    ");
	    missed =new JLabel("Missed:" + score.getMissed()+ "    ");
	    scr =new JLabel("Score:" + score.getScore()+ "    ");    
	    txt.add(caught);
	    txt.add(missed);
	    txt.add(scr);
    
  
	   final JTextField textEntry = new JTextField("",20);
	   textEntry.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent evt) {
	          String text = textEntry.getText();
	          
	          for(int j=0; j<words.length;j++) {
	        	  if(words[j].matchWord(text)) {
	        		  score.caughtWord(text.length());  
	        		  words[j].resetWord();
	        		  w.repaint();
	        		  caught.setText("Caught: "+score.getCaught()+ "  ");
	        		  scr.setText("Score:" + score.getScore()+ "    ");
	        	  }
	          }
	          	          
	          textEntry.setText("");
	          textEntry.requestFocus();
	      }

		
	    });
		
	   
	   txt.add(textEntry);
	   txt.setMaximumSize( txt.getPreferredSize() );
	   g.add(txt);
	    
	    JPanel b = new JPanel();
        b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS)); 
	   
            	JButton startB = new JButton("Start");;
            	startB.addActionListener(new ActionListener()
		    {
		      public void actionPerformed(ActionEvent e)
		      {
		    	  
		    	  textEntry.requestFocus();
		    	  gameRunning=true;  
		    	  start();
		    	  
		      }
		      

					
			
		    });
			
		        JButton endB = new JButton("End");;
				endB.addActionListener(new ActionListener()
			    {
			      public void actionPerformed(ActionEvent e)
			      {
			    	  end();
			      }

				
			    });
				
				JButton quitB = new JButton("Quit");;
				quitB.addActionListener(new ActionListener()
			    {
			      public void actionPerformed(ActionEvent e)
			      {
			    	  quitgame();
			      }

				private void quitgame() {
					for(int i=0; i<words.length;i++) {
				    	  words[i].setWord("");
				    	  w.repaint();
				    	  }	
					gameRunning=false;
					frame.dispose();
					
				}
			    });
		
		b.add(startB);
		b.add(endB);
		b.add(quitB);
		
		g.add(b);
    	
      	frame.setLocationRelativeTo(null);  // Center window on screen.
      	frame.add(g); //add contents to window
        frame.setContentPane(g);     
       	//frame.pack();  // don't do this - packs it into small space
        frame.setVisible(true);

		
	}

	
public static String[] getDictFromFile(String filename) {
		String [] dictStr = null;
		try {
			Scanner dictReader = new Scanner(new FileInputStream(filename));
			int dictLength = dictReader.nextInt();
			//System.out.println("read '" + dictLength+"'");

			dictStr=new String[dictLength];
			for (int i=0;i<dictLength;i++) {
				dictStr[i]=new String(dictReader.next());
				//System.out.println(i+ " read '" + dictStr[i]+"'"); //for checking
			}
			dictReader.close();
		} catch (IOException e) {
	        System.err.println("Problem reading file " + filename + " default dictionary will be used");
	    }
		return dictStr;

	}

//changes the interface lower part if a word is missed
public static void setmissedWord() {
	score.missedWord();
	missed.setText("Missed: "+score.getMissed()+ "  ");
	scr.setText("Score:" + score.getScore()+ "    ");
		}

//called upon when start button is pushed and initiated the threads and by doing so starting the program
private static void start() {
	
	Thread t=new Thread(w);
	t.start();
	
	for (int i=0;i<noWords;i++) {
	Thread tw = new Thread(words[i]);
	tw.start();
}}

//reset scores to 0
public static void resetPoints() {
	score.resetScore();	
	caught.setText("Caught: "+score.getCaught()+ "  ");
	missed.setText("Missed: "+score.getMissed()+ "  ");
	scr.setText("Score:" + score.getScore()+ "    ");
}

//called upon when end button is pushed and end the game
public static void end() {
	for(int i =0; i<words.length;i++) {
		  words[i].resetWord();
	  }
	resetPoints();
	gameRunning=false;
	  
}

//called upon when the word limit is reached and ends the game 
public static void endLimit() {
	resetPoints();
    end();
	w.limitReach();
}

//Main method run when program starts
public static void main(String[] args) {
    	
		//deal with command line arguments
		totalWords=Integer.parseInt(args[0]);  //total words to fall
		noWords=Integer.parseInt(args[1]); // total words falling at any point
		assert(totalWords>=noWords); // this could be done more neatly
		String[] tmpDict=getDictFromFile(args[2]); //file of words
		if (tmpDict!=null)
			dict= new WordDictionary(tmpDict);
		
		WordRecord.dict=dict; //set the class dictionary for the words.
		
		words = new WordRecord[noWords];  //shared array of current words
		
		setupGUI(frameX, frameY, yLimit);  
    
					
		int x_inc=(int)frameX/noWords;
	  	//initialize shared array of current words

		for (int i=0;i<noWords;i++) {
			words[i]=new WordRecord(dict.getNewWord(),i*x_inc,yLimit);
		}
		
		
		
		}






	    

	}


	
	


	



