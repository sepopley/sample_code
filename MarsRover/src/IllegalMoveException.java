
public class IllegalMoveException extends Exception {

	IllegalMoveException(char d){
		System.out.println("'" + d + "' is an urecognized operation. Only 'L', 'R', and 'M' are allowed.");
	}
}
