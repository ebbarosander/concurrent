package skeletonCodeAssgnmt2;
/**
* Class working as the model
* Contains methods and variables representing one word. 
* Is responsible for the calculations of when and if a word should fall
* @author Ebba Rosander
* 
*/
public class WordRecord implements Runnable {
	private String text;
	private  int x;
	private int y;
	private int maxY;
	private boolean dropped;
	
	private int fallingSpeed;
	private static int maxWait=1500;
	private static int minWait=100;

	public static WordDictionary dict;
	
	
	WordRecord() {
		text="";
		x=0;
		y=0;	
		maxY=300;
		dropped=false;
		fallingSpeed=(int)(Math.random() * (maxWait-minWait)+minWait); 
	}
	
	WordRecord(String text) {
		this();
		this.text=text;
	}
	
	WordRecord(String text,int x, int maxY) {
		this(text);
		this.x=x;
		this.maxY=maxY;
	}
	

	public synchronized  void setY(int y) {
		if (y>maxY) {
			y=maxY;
			dropped=true;
		}
		this.y=y;
	}
	
	public synchronized  void setX(int x) {
		this.x=x;
	}
	
	public synchronized  void setWord(String text) {
		this.text=text;
	}

	public synchronized  String getWord() {
		return text;
	}
	
	public synchronized  int getX() {
		return x;
	}	
	
	public synchronized  int getY() {
		return y;
	}
	
	public synchronized  int getSpeed() {
		return fallingSpeed;
	}

	public synchronized void setPos(int x, int y) {
		setY(y);
		setX(x);
	}
	public synchronized void resetPos() {
		setY(0);
	}

	public synchronized void resetWord() {
		resetPos();
		text=dict.getNewWord();
		dropped=false;
		fallingSpeed=(int)(Math.random() * (maxWait-minWait)+minWait); 

	}
	
	public synchronized boolean matchWord(String typedText) {
		if (typedText.equals(this.text)) {
			resetWord();
			return true;
		}
		else
			return false;
	}
	

	public synchronized void drop(int inc) {
		setY(y+inc);
	}
	
	public synchronized  boolean dropped() {
		return dropped;
	}

	//starts when threads are initiated and runs for as long gameRunning=true, makes to calculations on what to do with thread
	public void run() {
		

		while (WordApp.gameRunning) {
			if(WordApp.score.getCaught()+WordApp.score.getMissed()>=WordApp.totalWords) {
				WordApp.endLimit();  	
			}
			synchronized(this) {
			if(y!=maxY) {
			    drop(1);		
				}else {
				resetWord();
				WordApp.setmissedWord();
				
				}}
		  try {
			  Thread.sleep(fallingSpeed);
		  }catch (InterruptedException ie) {
		  System.out.print(ie);
		  }
		
          
		
		

	}
	
	

}
}
