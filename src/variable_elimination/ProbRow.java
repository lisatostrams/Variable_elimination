package variable_elimination;

import java.util.ArrayList;

/**
 * Represents a row of probabilities
 *
 * @author Djamari Oetringer & Abdullahi Ali with adaptations by Leonieke van
 * den Bulk
 */
public class ProbRow {

    private double[] probs;
    private String[] parentsValues;
    private Variable node;
    private ArrayList<Variable> parents;

    /**
     * Constructor
     *
     * @param node a variable
     * @param probs probabilities associated to the variable
     * @param parentsValues values of the parent variables
     * @param parents the parent variables
     */
    public ProbRow(Variable node, double[] probs, String[] parentsValues, ArrayList<Variable> parents) {
        this.probs = probs;
        this.parentsValues = parentsValues;
        this.node = node;

        this.parents = parents;
    }

    /**
     * Check whether two have ProbRows have the same parents
     *
     * @param pr a probability row
     * @return True if this probRow and pr have the same parents
     */
    public boolean sameParentsValues(ProbRow pr) {
        return this.parentsValues.equals(pr.parentsValues);
    }

    /**
     * Getter of the probabilities.
     *
     * @return the probabilities of the node.
     */
    public double[] getProbs() {
        return probs;
    }

    /**
     * Getter of the node.
     *
     * @return node given the probabilities.
     */
    public Variable getNode() {
        return node;
    }

    /**
     * Function that transforms the string of parents values into an ArrayList
     *
     * @return: list of parents values
     */
    public ArrayList<String> getPVsAsArrayList() {
        ArrayList<String> list = new ArrayList<String>();
        if (parentsValues != null) {
            for (String s : parentsValues) {
                list.add(s);
            }
        }

        return list;
    }

    /**
     * Transform probabilities to string.
     *
     * @return
     */
    public String toString() {
        String parents = "";
        if (parentsValues != null) {
            for (int i = 0; i < parentsValues.length - 1; i++) {
                parents = parents + parentsValues[i] + ", ";
            }
            parents = parents + parentsValues[parentsValues.length - 1];
        }

        String probabilities = "";
        for (int i = 0; i < probs.length - 1; i++) {
            probabilities = probabilities + probs[i] + ", ";
        }
        probabilities = probabilities + probs[probs.length - 1];
        return parents + " | " + probabilities;
    }

    /**
     * Getter of the parents.
     *
     * @return the parents of the node given the probabilities.
     */
    public ArrayList<Variable> getParents() {
        return parents;
    }
}
