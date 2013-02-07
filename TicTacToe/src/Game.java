import java.util.*;

/* 
 Sneha Popley @sepopley
 */

public class Game{

	private GameGUI gui; //Corresponding GUI
	
	protected char game[] = new char[9]; //Array for game board
	protected char computer = 'O'; //Computer's symbol
	protected char player = 'X'; //Player's symbol
	protected int count = 0; //# moves in game 
	protected boolean first = true; //If true, computer plays first
	protected int computerWins = 0; //# of computer wins
	protected int playerWins = 0; //# of player wins

	protected int next = 0; 	 
	protected char winner = '0';
	
	/***************************************
	 * Initialize game variables
	 * @param g game variables in class Game
	 ***************************************/
	public void initGame(GameGUI g){
		gui = g; 

		//Initialize Game
		computerWins = 0;  playerWins = 0; 
		//randomly determine who plays first, if 0, computer, else player
		first = ((int) Math.round(Math.random()) == 0); 
		for(int i=0; i<9; i++) game[i] = (i+"").charAt(0); 

		//First move
		if(first) nextMove(); 
	}

	/***************************************
	 * Reset game variables; restart current game
	 ***************************************/
	public void restartGame(){
		//Reset variables
		count = 0; 
		for(int i=0; i<9; i++){ 
			gui.board[i].setText(" ");
			gui.board[i].setEnabled(true);
			game[i] = (i+"").charAt(0);
		}

		//Reset GUI components
		gui.restart.setEnabled(false);
		gui.status.setText("Computer " + computerWins + 
				", Player " + playerWins);

		//First move (if computer has first move)
		if(first) nextMove();
	}

	/***************************************
	 * Set variables for new game
	 ***************************************/	
	public void makeNewGame(){
		//Set variables for new game
		count = 0; 
		first = !first;
		for(int i=0; i<9; i++){ 
			gui.board[i].setText(" ");
			gui.board[i].setEnabled(true);
			game[i] = (i+"").charAt(0);
		}

		//Reset GUI components
		gui.restart.setEnabled(false);
		gui.newGame.setEnabled(false);
		gui.status.setText("Computer " + computerWins + 
				", Player " + playerWins);

		//First move
		if(first) nextMove(); 	
	}

	/***************************************
	 * Implement play when corresponding board area is 
	 * pressed. Called in response to an action listener
	 * on the button.   
	 * @param i player's move at location i in 1D game board
	 ***************************************/
	public void playAt(int i){
		game[i] = player;
		count++;

		//GUI stuff
		gui.board[i].setText(player+"");
		gui.board[i].setEnabled(false);
		gui.restart.setEnabled(true);

		//Evaluate + next step
		if(winner=='X' && win(game)) //Player wins
			gui.winner(player);
		else if(count<9) nextMove();
		else if (count>=9){//it's a draw	
			gui.restart.setEnabled(false);
			gui.newGame.setEnabled(true);
			gui.status.setText("Draw");
		}
	}

	/***************************************
	 * Compute and play computer's next move. 
	 ***************************************/
	public void nextMove(){
		int move = findMove(); 
		game[move]= computer;
		count++; 

		//Set GUI 
		gui.board[move].setText(computer+"");
		gui.board[move].setEnabled(false);
		gui.restart.setEnabled(true);

		//Test for winner/draw
		if(win(game)) gui.winner(computer);
		else if(count>=9){ //Draw
			gui.restart.setEnabled(false);
			gui.newGame.setEnabled(true);
			gui.status.setText("Draw");
		}
	}

	/***************************************
	 * Compute computer's next move with negamax algorithm. 
	 * No alpha-beta pruning. just the simple algorithm.
	 * @return computer's next move according to negamax
	 ***************************************/
	public int findMove(){
		//First move
		if(count==0) return 0; 
		else{//run algorithm
			int value = negamax(game, count, 'O');
			return next;
		}
	}

	/***************************************
	 * negamax algorithm. Iterates through the possible trees, 
	 * stopping at a win or draw in each subtree. Used the wiki
	 * algorithm as reference but simplified it.  
	 * @param g gameboard
	 * @param counting # of plays in game so far
	 * @param current char that represents current player
	 * @return product of minimax  
	 ***************************************/	
	public int negamax(char g[], int counting, char current){ 
		//return 1 if current player is winner, else return -1. 
		if(win(g)){ return (winner==current?1:(-1));}
		
		else if(counting==9) { return 0;} //draw

		else{			
			int alpha = -1; //Lower bound on the possible values
			Object[] moves = possibleMoves(g); 
			
			for(int i=0; i<moves.length; i++){  
				int nextMove = (Integer) moves[i];
				char [] gCopy = g.clone();
				gCopy[nextMove] = current;
				//Call function recursively on its children
				int val = -negamax(gCopy, counting+1, nextPlayer(current));	
				if(val > alpha){
					//if at second level of created tree, update next move
					if(counting==count) next = nextMove; 
					alpha = val;
				} 		
			}
		//return largest possible value returned by the algorithm on children
			return alpha;
		}
	}

	/***************************************
	 * Array of possible moves on current game board
	 * @param g gameboard
	 * @return array of empty & possible positions
	 ***************************************/
	public Object[] possibleMoves(char g[]){
		Vector<Integer> result = new Vector<Integer>();
		for(int i=0; i<9; i++)
			if(g[i]==(i+"").charAt(0)) result.add(i);

		return result.toArray();
	}

	/***************************************
	 * Switch player from 'O' to 'X' and vice versa
	 * @param c char to be switched (either 'O' or 'X')
	 * @return player with next turn
	 ***************************************/
	public char nextPlayer(char c){
		return (c=='O'?'X':'O');
	}

	/***************************************
	 * Check for a win on given game board.
	 * @param g game board
	 * @return true is there is a win, else false
	 ***************************************/
	public boolean win(char[] g){
		return (winHorizontal(g) || winVertical(g) || winDiagonal(g)); 
	}
	
	/***************************************
	 * Check for a horizontal win on current game board. Update a variable to
	 * store the player that wins.  
	 * @param game game board
	 * @return true is there is a win, else false
	 ***************************************/
	public boolean winHorizontal(char[] game){
		boolean row1 = (game[0] == game[1] && game[1] == game[2]); 
		boolean row2 = (game[3] == game[4] && game[4] == game[5]);
		boolean row3 = (game[6] == game[7] && game[7] == game[8]);
		if(row1) winner = game[0];
		else if(row2) winner = game[3];
		else if(row3) winner = game[6];
		return (row1 || row2 || row3); 
	}

	/***************************************
	 * Check for a vertical win on current game board. Update a variable to
	 * store the player that wins.
	 * @param game game board
	 * @return true is there is a win, else false
	 ***************************************/
	public boolean winVertical(char[] game){
		boolean col1 = (game[0] == game[3] && game[3] == game[6]); 
		boolean col2 = (game[1] == game[4] && game[4] == game[7]);
		boolean col3 = (game[2] == game[5] && game[5] == game[8]);
		if(col1) winner = game[0];
		else if(col2) winner = game[1];
		else if(col3) winner = game[2];
		return (col1 || col2 || col3); 
	}

	/***************************************
	 * Check for a diagonal win on current game board.Update a variable to
	 * store the player that wins.
	 * @return game game board
	 ***************************************/
	public boolean winDiagonal(char[] game){
		boolean dia1 = (game[0] == game[4] && game[4] == game[8]); 
		boolean dia2 = (game[2] == game[4] && game[4] == game[6]);
		if(dia1) winner = game[0];
		else if(dia2) winner = game[2];
		return (dia1 || dia2); 
	}

	/***************************************
	 * Helper function to print a string
	 * @param s string to be printed
	 ***************************************/
	public void print(String s){
		System.out.println(s);
	}

	/***************************************
	 * Helper function to print 1D char array
	 * @param g game board as 1D char array
	 ***************************************/
	public void printArray(char[] g){
		for(int i=0; i<9; i++){
			if(i%3 == 0) System.out.println("");
			System.out.print(g[i]+ " ");
		}
		System.out.println("");
	}
}
