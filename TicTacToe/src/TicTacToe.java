/* 
 Sneha Popley @sepopley
 
 */
import javax.swing.JApplet;

 public class TicTacToe extends JApplet{

	private  Game newGame;  
	private  GameGUI gui; 
	
	/***************************************
	 * Initiate the applet
	 ***************************************/
	public void init() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
                startGame();
            }}); 
	}
	
	/***************************************
	 * Create a new Game and GUI instance
	 ***************************************/
	private  void createAndShowGUI(){
		newGame = new Game();
		gui = new GameGUI(newGame, this.getContentPane());
	}
	
	/***************************************
	 * Initialize Game variables in Game
	 ***************************************/
	private  void startGame(){
		newGame.initGame(gui); 
	}
	
	/***************************************
	 * Helper function to print a string
	 * @param s string to be printed
	 ***************************************/
	public  void print(final String s){
		System.out.println(s);
	}
}
