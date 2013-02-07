/*
 Sneha Popley @sepopley
 */
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;  


public class GameGUI implements ActionListener{

	protected JButton [] board = new JButton[9]; 
	protected JLabel status = new JLabel("Computer 0, Player 0");
	protected JButton restart = new JButton("Restart Game");
	protected JButton newGame = new JButton("New Game");
	protected String winner = "Computer"; //To set status label
	private Game currentGame; 
	
	/***************************************
	 * Create GUI for TicTacToe game
	 * @param g instance of initialized Game
	 * @param frame container to add GUI to
	 ***************************************/
	public GameGUI(Game g, Container frame){
		currentGame = g; 
        frame.setLayout(new BoxLayout(frame, BoxLayout.Y_AXIS));
        addComponentsToPane(frame);
        frame.setVisible(true); 
	}

	/***************************************
	 * Add GUI components to pane
	 * @param pane container to add GUI to
	 ***************************************/
	public  void addComponentsToPane(Container pane) {
        //Board Game
        Container b = new Container();
        b.setLayout(new GridLayout(3,3));
        for(int i=0; i<9; i++){        	
        	board[i] = new JButton(" ");
        	board[i].setFont(new Font("Times New Roman", Font.BOLD, 30));
        	board[i].setAlignmentX(Component.CENTER_ALIGNMENT);
        	board[i].addActionListener(this);
        	b.add(board[i]);
        }
       pane.add(b);
       
       //Status Label
       status.setFont(new Font("Times New Roman", Font.BOLD, 20));
       status.setAlignmentX(Component.CENTER_ALIGNMENT);
       pane.add(status);
       
       //Restart Button
       restart.setFont(new Font("Times New Roman", Font.BOLD, 20));
       restart.setAlignmentX(Component.CENTER_ALIGNMENT);
       restart.addActionListener(this);
       pane.add(restart);
      
       //New Game Button
       newGame.setFont(new Font("Times New Roman", Font.BOLD, 20));
       newGame.setAlignmentX(Component.CENTER_ALIGNMENT);
       newGame.addActionListener(this);
       newGame.setEnabled(false);
       pane.add(newGame);
	}

	/***************************************
	 * Process restart, newGame, and board actions
	 ***************************************/
	public void actionPerformed(ActionEvent e) {
		// Restart button pressed
		if(e.getSource().equals(restart)) currentGame.restartGame(); 
		//New Game button pressed
		else if(e.getSource().equals(newGame)) currentGame.makeNewGame();
		//A new play on the game board
		else for(int i=0; i<9; i++)
				if(e.getSource().equals(board[i])) currentGame.playAt(i);
	}
	
	
	/***************************************
	 * Set gui for a win. 
	 * @param c 'O'for computer or 'X' for player
	 ***************************************/
	public void winner(char c){
		boolean b = (c=='O');
		if(b){ 
			winner = "Computer";
			currentGame.computerWins++;}
		else{
			winner = "Player";
			currentGame.playerWins++;}
		
		//Set GUI 
		for(int i=0; i<9; i++) board[i].setEnabled(false);
		status.setText(winner + " wins!");
		newGame.setEnabled(true);
		restart.setEnabled(false);
	}
	
}
	
