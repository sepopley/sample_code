import java.io.*;
import java.util.*;

/* Sneha Popley 
 * University of Chicago
 * Question #3: Mars Rover
 * 
 * Assumptions: 
 * 1) The rover cannot move out of the grid. When it hits a bound on any side, 
 * it stops moving in that direction. 
 * 2) Two rovers cannot occupy one location on the grid. If the user tries to 
 * place a rover on another rover, an exception is thrown. 
 * If while  moving around the grid, the current rover collides with a previous 
 * rover, it cannot move in that direction. 
 * 3) The only constraint to the number of rovers in a file is computer memory. 
 * 4) The only constraint to the size of a grid (width and height) is the 
 * upper bound on ints. This can be easily modified to support a larger grid with
 * a long, unsigned int, or so on. 
 * 5) Expected output for each rover is final coordinates (I'm not sure what you meant 
 * by "heading")
 * 6) As per given requirements, (0,0) is bottom left corner of grid, (w,h) is the top 
 * right corner.  
 * 7) 'L' and 'R' makes the rover spin 90 degrees left or right respectively, 
 * without moving from its current spot. 'M' means move forward one grid point, and 
 * maintain the same heading.
 * 8) The input file should be formatted as specified in the question. Any errors 
 * will result in an IOException
 * 9) Rectangular grids are supported. 
 * 
 * Given Test Files: 
 * There are four possible input test case files.  
 * 1) input.txt -> Given test cases
 * 2) bounds.txt -> Test cases for all four bounds of the grid
 * 3) collision1.txt -> Test case for collision with starting position
 * 4) collision2.txt -> Test case for collision while the rover moves around the grid
 * */
public class MarsRover {

	private static int height; //height of grid
	private static int width; //width of grid
	//vector of rover locations
	private static Vector<Integer[]> occupied = new Vector<Integer[]>(); 
	 	
	public static void main(String[] args) {
		BufferedReader in;
		
		//CAN CHANGE DEFAULT FILENAME HERE
		File f = new File ("input.txt");
		
		try {//open file
			if(args.length>0) f = new File(args[0]);
			
			in = new BufferedReader(new FileReader(f));
			String dimensions = in.readLine();
			
			//parse first line of file, the dimensions of the grid
			StringTokenizer str = new StringTokenizer(dimensions);
			width = Integer.parseInt(str.nextToken());
			height = Integer.parseInt(str.nextToken());
			
			//read and process rest of file
			String startCoor = in.readLine(); 
			while(startCoor!=null){
				String moves = in.readLine();
				System.out.println(computeFinalPosition(startCoor, moves)); 
				startCoor = in.readLine();
			}
			in.close();
		} catch (IOException e) {
			System.out.println("Illegal input");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	/**
	 * Given a start position and list of moves, computes final position
	 * of MarsRover. 
	 * @param start rover's start position of format "1 2 N"
	 * @param moves List of moves. Moves can be 'M', 'L', or 'R'. L' and 
	 * 'R' makes the rover spin 90 degrees left or right respectively, 
	 * without moving from its current spot. 'M' means move forward one 
	 * grid point, and maintain the same heading.
	 * @return final position of rover 
	 * @throws OccupiedException 
	 * @throws IllegalMoveException 
	 * @throws OutsideBoundsException 
	 */
	private static String computeFinalPosition(String start, String moves) throws OccupiedException, IllegalMoveException, OutsideBoundsException { 
		Integer[] pos = new Integer[2]; 
		char direction; 
		
		StringTokenizer str = new StringTokenizer(start);
		pos[0] = Integer.parseInt(str.nextToken()); //horizontal position
		pos[1] = Integer.parseInt(str.nextToken()); //vertical position
		direction = str.nextToken().charAt(0);
		if(isOccupied(pos)) throw new OccupiedException(pos);
		if(outsideBounds(pos)) throw new OutsideBoundsException(pos,width,height);
		
		//implement each move sequentially
		for(int i=0; i<moves.length(); i++){
			char m = moves.charAt(i);			
			
			if(m=='L' || m=='R') //turn 90 degrees
				direction = move90(direction, m);
			 else if (m=='M') //move one square forward
				move(pos, direction);
			 else throw new IllegalMoveException(m);
			
			//System.out.println(i + "; " + pos[0] + " " + pos[1] + " " + direction);
			occupied.add(pos);
		}
		return (pos[0] + " " + pos[1] + " " + direction);  
	}
	/**
	 * Move 90 degrees clockwise (in the case of 'R') or counterclockwise
	 * (in the case of 'L')
	 * @param d current direction
	 * @param m intended direction of movement 'L' or 'R'
	 * @return final direction
	 */
	private static char move90(char d, char m){
		char[] dir = {'N', 'E', 'S','W'};
		
		//find corresponding index for current direction
		int index = 0; 
		for(; index<dir.length && dir[index]!=d; index++);
				
		if(m=='L')	index--; 
		else if(m=='R') index++;

		if(index<0) index = 3;
		else if (index>3) index = 0; 		
		
		return dir[index];
	}
	/**
	 * Move forward one square
	 * @param pos current position, modified to contain final position
	 * @param d direction of movement
	 */
	private static void move(Integer[] pos, char d){
		int next = 0; 
		
		//Cases for movement in each possible direction and bounds checking
		switch(d){
		case 'N': //Increase height to move North
			next = pos[1]+1; if(next<=height && !isOccupied(pos[0],next)) pos[1] = next;  
			break; 
		
		case 'S'://Decrease height to move South
			next = pos[1]-1; if(next>=0 && !isOccupied(pos[0],next)) pos[1] = next;  
			break;
			
		case 'W'://Decrease width to move West
			next = pos[0]-1; if(next>=0 && !isOccupied(next,pos[1])) pos[0] = next;
			break;
			
		case 'E'://Decrease width to move East
			next = pos[0]+1; if(next<=width && !isOccupied(next,pos[1])) pos[0] = next; 
		}
	}
	/**
	 * Checks if given location already has a rover in it
	 * @param w Current rover's start x coordinate
	 * @param h Current rover's start y coordinate
	 * @return
	 */
	private static boolean isOccupied(Integer w, Integer h){
		for(int i=0; i<occupied.size(); i++)
			if(w==occupied.get(i)[0] && h==occupied.get(i)[1]) 
				return true;
		return false; 
	}
	/**
	 * Checks if given location already has a rover in it
	 * @param p Position of current rover
	 * @return
	 */
	private static boolean isOccupied(Integer [] p){
		return isOccupied(p[0],p[1]);
	}
	/**
	 * Checks if a given position is between (0,0) and (w,h)
	 * where w and h are dimensions of grid. 
	 * @param p Position of current rover
	 * @return
	 */
	private static boolean outsideBounds(Integer p[]){
		return (p[0]<0 || p[1]<0 || p[0]>width || p[1]>height); 
	}
}
