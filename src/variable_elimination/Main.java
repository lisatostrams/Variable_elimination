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
		String log_network = reader.printNetwork(Vs, Ps);
                Log log = new Log(networkName, log_network); 
		// Ask user for query and heuristic
		reader.askForQuery(); 
		//reader.askForHeuristic();
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
                Formula form = new Formula(Vs, Q, O, Ps);
                
                log.log_c(form.toString(), form.product_toString());
                
                //d) identify factors and reduce observed variables
                form.factorize();
                
                log.log_d(form.factor_probs()); 
                
                //e) fix elimination order: start with leaf variables, then roots, then remaining nodes
                ArrayList<Variable> elimination = form.EliminationOrder();
                log.log_e(form.elimination_toString());
                
                //f) for every variable Z in ordering:
                //  1) multiply factors containing Z
                //  2) sum out Z to obtain new factor f_z
                //  3) remove multiplied factors from list and add f_z
                String log_f = "";
                for(Variable Z : elimination) {
                    log_f += form.eliminate_Z(Z);
                    System.out.println();
                }
                System.out.println(log_f);
                log.log_f(log_f);
                
                //g) normalize result
                String log_g = form.normalize();
                System.out.println(log_g);
                log.log_g(log_g); 
	}
        
        
}