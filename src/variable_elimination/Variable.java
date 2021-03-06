package variable_elimination;

import java.util.ArrayList;

/**
 * Responsible for creating variables and giving information about the variables
 * to other classes.
 *
 * @author Djamari Oetringer & Abdullahi Ali with adaptations by Leonieke van
 * den Bulk
 */
public class Variable {

    private String name;
    private ArrayList<String> values;
    private String value;
    private boolean observed = false;
    private ArrayList<Variable> parents;

    /**
     * Constructor of the class.
     *
     * @param name, name of the variable.
     * @param values
     */
    public Variable(String name, ArrayList<String> values) {
        this.name = name;
        this.values = values;
    }

    /**
     * Getter of the values.
     *
     * @return the values of the variable.
     */
    public ArrayList<String> getValues() {
        return values;
    }

    /**
     * Getter of the amount of values.
     *
     * @return the amount of values
     */
    public int getNumberOfValues() {
        return values.size();
    }

    /**
     * Getter of the name.
     *
     * @return the name of the variable.
     */
    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

    /**
     * Setter of the value.
     *
     * @param s
     */
    public void setValue(String s) {
        this.value = s;
    }

    /**
     * Check if string v is contained by the variable.
     *
     * @param v
     * @return a boolean denoting if values contains string v.
     */
    public boolean isValueOf(String v) {
        return values.contains(v);
    }

    /**
     * Getter of the value.
     *
     * @return the value of the variable.
     */
    public String getValue() {
        return value;
    }

    /**
     * Getter of the parents.
     *
     * @return the list of parents.
     */
    public ArrayList<Variable> getParents() {
        return parents;
    }

    /**
     * Setter of the parents.
     *
     * @param parents
     */
    public void setParents(ArrayList<Variable> parents) {
        this.parents = parents;
    }

    /**
     * Check if a variable has parents.
     *
     * @return a boolean denoting if the variable has parents.
     */
    public boolean hasParents() {
        return parents != null;
    }

    /**
     * Getter for the number of parents a variable has.
     *
     * @return the amount of parents
     */
    public int getNrOfParents() {
        if (parents != null) {
            return parents.size();
        }
        return 0;
    }

    /**
     * Setter for the observation of a variable.
     *
     * @param b
     */
    public void setObserved(boolean b) {
        this.observed = b;
    }

    /**
     * Getter for if a variable is observed.
     *
     * @return a boolean denoting if the variable is observed or not.
     */
    public boolean getObserved() {
        return observed;
    }
}
