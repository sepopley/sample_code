
public class OccupiedException extends Exception {

	OccupiedException(Integer [] p){
		System.out.println("Another rover already at location " + p[0] + " " + p[1]);
	}
	
}
