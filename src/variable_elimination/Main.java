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
                Log log = new Log(networkName); 
		
		// Get the variables and probabilities of the network
		ArrayList<Variable> Vs = reader.getVs(); 
		ArrayList<ArrayList<ProbRow>> Ps = reader.getPs(); 
		
		// Print variables and probabilities
		reader.printNetwork(Vs, Ps);

		// Ask user for query and heuristic
		reader.askForQuery(); 
		reader.askForHeuristic();
		Variable Q = reader.getQueriedVariable(); 
		
                log.log_a(Q);
		
                // Ask user for observed variables 
		reader.askForObservedVariables(); 
		ArrayList<Variable> O = reader.getObservedVariables(); 
		
                log.log_b(O);
		
                // Print the query and observed variables
		reader.printQueryAndObserved(Q, O); 
		
		
		//PUT YOUR CODE FOR THE VARIABLE ELIMINATION ALGORITHM HERE
                
                //c) 1) product forumula to compute the query
                
                //   2) reduced formula based on network structure
                
                log.log_c();
                
                //d) identify factors and reduce observed variables
                
                log.log_d(); 
                
                //e) fix elimination order: start with leaf variables, then roots, then remaining nodes
                
                log.log_e(null);
                
                //f) for every variable Z in ordering:
                //  1) multiply factors containing Z
                //  2) sum out Z to obtain new factor f_z
                //  3) remove multiplied factors from list and add f_z
                
                log.log_f(null, null);
                
                //g) normalize result
                
                log.log_g(); 
	}
        
        
}