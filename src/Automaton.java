import java.util.HashSet;
import java.util.Set;

public class Automaton {
	private Set <Integer> passed = new HashSet<Integer>(); 
	private String state = "Q0";
	String getState (int e) {
		if (state.equals("Q11")) {return state;}
		if(addNCheck(e)) {
			state = "Q0 or Q"+String.valueOf(e+1);
			return state;
		}
		else {
			state="Q11";
			return state;
		}	
	}
	private boolean addNCheck(int e) {
		return passed.add(e);
	}
}
