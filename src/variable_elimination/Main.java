package variable_elimination;

import java.util.ArrayList;

/**
 * Main class to read in a network, add queries and observed variables, and
 * eliminate variables.
 * 
 * @author Djamari Oetringer & Abdullahi Ali with adaptations by Leonieke van den Bulk
 */

public class Main {
	private final static String networkName = "earthquake.bif"; // The network to be read in (format from http://www.bnlearn.com/bnrepository/)

	public static void main(String[] args) {
		
		// Read in the network
		Networkreader reader = new Networkreader(networkName); 
		
		// Get the variables and probabilities of the network
		ArrayList<Variable> Vs = reader.getVs(); 
		ArrayList<ArrayList<ProbRow>> Ps = reader.getPs(); 
		
		// Print variables and probabilities
		reader.printNetwork(Vs, Ps);

		// Ask user for query and heuristic
		reader.askForQuery(); 
		reader.askForHeuristic();
		Variable Q = reader.getQueriedVariable(); 
		
		// Ask user for observed variables 
		reader.askForObservedVariables(); 
		ArrayList<Variable> O = reader.getObservedVariables(); 
		
		// Print the query and observed variables
		reader.printQueryAndObserved(Q, O); 
		
		
		//PUT YOUR CODE FOR THE VARIABLE ELIMINATION ALGORITHM HERE
	}
}