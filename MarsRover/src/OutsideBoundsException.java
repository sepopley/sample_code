
public class OutsideBoundsException extends Exception {
	OutsideBoundsException(Integer p[], int w, int h){
		System.out.println(p[0] + " " + p[1] + " is outside " +
				"the grid 0 0 to " + w + " " + h);
	}
}
