
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

import javax.swing.JButton;
import javax.swing.JPanel;
/**
* Class working as the view
* Contains methods for painting the user interface and the falling words
* 
* @author Ebba Rosander
* 
*/
public class WordPanel extends JPanel implements Runnable {
		public static volatile boolean done;
		private WordRecord[] words;
		private int noWords;
		private int maxY;
		private volatile boolean paintnormal=true;

		
		public void paintComponent(Graphics g) {
			if(WordApp.gameRunning) {
		    int width = getWidth();
		    int height = getHeight();
		    g.clearRect(0,0,width,height);
		    g.setColor(Color.red);
		    g.fillRect(0,maxY-10,width,height);

		    g.setColor(Color.black);
		    g.setFont(new Font("Helvetica", Font.PLAIN, 26));
		  		   
		    for (int i=0;i<noWords;i++){	
		    	 synchronized(WordApp.words[i]) {
		    	g.drawString(words[i].getWord(),words[i].getX(),words[i].getY()+20);  //y-offset for skeleton so that you can see the words	
		    }
		    }
			}else {
				int width = getWidth();
			    int height = getHeight();
			    g.clearRect(0,0,width,height);
			    g.setColor(Color.red);
			    g.fillRect(0,maxY-10,width,height);

			    g.setColor(Color.black);
			    g.setFont(new Font("Helvetica", Font.PLAIN, 26));
			    
				if (paintnormal) {	
				g.drawString("Press start to play the game",width/10,height/2);
				}else {
			    g.drawString("You have reached the word limit, if you want to play again press start",width/10,height/2);
				
			   }
		   
		  }
		}
		
		WordPanel(WordRecord[] words, int maxY) {
			this.words=words; 
			noWords = words.length;
			done=false;
			this.maxY=maxY;		
		}
		
		//Changes what to draw due to that the limit has been reached
		public void limitReach() {
			paintnormal = false;
			repaint();
					
		}
		
		public void run() {
			paintnormal=true;
			while (WordApp.gameRunning) {
				repaint();	
					
				}
			
			
		}


	}
