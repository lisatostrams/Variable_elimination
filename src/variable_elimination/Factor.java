/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package variable_elimination;

import java.util.ArrayList;

/**
 * Represent a factor and factor computations
 * @author Lisa Tostrams
 */
public class Factor {

    ArrayList<ProbRow> value_table;
    Variable factor;
    ArrayList<Variable> variables;

    /**
     * Constructor of the Factor, with the name factor, a list of variables the
     * factor is on and the value table associated with the factor.
     * @param factor
     * @param variables
     * @param table
     */
    public Factor(Variable factor, ArrayList<Variable> variables, ArrayList<ProbRow> table) {
        this.factor = factor;
        this.value_table = table;
        this.variables = variables;
    }

    /**
     * getter of the name
     * @return name
     */
    public String getName() {
        return factor.getName();
    }

    /**
     * setter of a new name f
     * @param f
     */
    public void setFactor(Variable f) {
        factor = f;
    }

    /**
     * getter of the list of variables of the factor
     * @return variables
     */
    public ArrayList<Variable> getVariables() {
        return variables;
    }

    /**
     * Function that multiplies this factor with other factor, with the common 
     * variable commonVar. 
     * @param other
     * @param commonVar
     * @return result from multiplication
     */
    public Factor multiply(Factor other, Variable commonVar) {
        ArrayList<ProbRow> tmp = new ArrayList();
        ArrayList<Variable> parents = new ArrayList();
        parents.addAll(this.getVariables());
        parents.addAll(other.getVariables());
        parents.remove(commonVar);

        int this_idx = this.getVariables().indexOf(commonVar); //index of Z in this factor
        int other_idx = other.getVariables().indexOf(commonVar); //index of Z in other factor

        for (int this_row = 0; this_row < this.getTable().size(); this_row++) {
            for (int other_row = 0; other_row < other.getTable().size(); other_row++) {
                if (this.getVarValues(this_row).get(this_idx).equals(other.getVarValues(other_row).get(other_idx))) { //only multiply rows where Z has same value
                    ArrayList<String> varvalues = new ArrayList();
                    varvalues.addAll(this.getVarValues(this_row));
                    varvalues.remove(this_idx);
                    varvalues.addAll(other.getVarValues(other_row));

                    String[] vv = new String[varvalues.size()];
                    vv = varvalues.toArray(vv);
                    double[] pr = {this.getVal(this_row) * other.getVal(other_row)}; //new probablilty
                    ProbRow new_prob = new ProbRow(factor, pr, vv, parents);
                    tmp.add(new_prob);
                }
            }

        }
        Factor factor_Z = new Factor(factor, parents, tmp);

        return factor_Z;
    }

    /**
     * Check whether it is possible to sum out the variable at index Z_idx in the list
     * of variables from the two rows at index row1 and row2 in the list of probability rows.
     * @param row1
     * @param row2
     * @param Z_idx
     * @return true if the variable at Z_idx can be summed out.
     */
    public boolean possibleToSumOut(int row1, int row2, int Z_idx) {
        if (getVarValues(row1).get(Z_idx).equals(getVarValues(row2).get(Z_idx))) {
            return false;
        }

        for (int j = 0; j < getVariables().size(); j++) {
            if (j != Z_idx && !getVarValues(row1).get(j).equals(getVarValues(row2).get(j))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sum out variable Z from the factor. 
     * @param Z
     * @return result from summing out Z
     */
    public Factor sumOut(Variable Z) {
        int f_z_idx = getVariables().indexOf(Z);
        ArrayList<ProbRow> tmp = new ArrayList();
        ArrayList<Variable> parents = new ArrayList();
        parents.addAll(getVariables());
        parents.remove(Z);

        for (int row1 = 0; row1 < value_table.size(); row1++) {
            for (int row2 = 0; row2 < value_table.size(); row2++) { //sum out Z to obtain new factor
                if (possibleToSumOut(row1, row2, f_z_idx)) { //sum rows where possible

                    ArrayList<String> varvalues = new ArrayList();
                    varvalues.addAll(getVarValues(row1));
                    varvalues.remove(f_z_idx);

                    double[] pr = {getVal(row1) + getVal(row2)};
                    String[] pv = new String[varvalues.size()];
                    pv = varvalues.toArray(pv);
                    ProbRow summedout = new ProbRow(factor, pr, pv, parents);

                    boolean add = true;
                    for (ProbRow prow : tmp) {
                        if (prow.getPVsAsArrayList().equals(summedout.getPVsAsArrayList())) {
                            add = false;
                        }
                    }

                    if (add) {
                        tmp.add(summedout);
                    }
                }
            }
        }
        Factor factor_Z = new Factor(factor, parents, tmp);
        return factor_Z;
    }

    /**
     * Normalize current factor
     */
    public void normalize() {
        double sum = 0;
        for (int i = 0; i < value_table.size(); i++) {
            sum += getVal(i);
        }
        for (int i = 0; i < value_table.size(); i++) {
            setVal(i, getVal(i) / sum);
        }
    }

    /**
     * Getter of the value at row i of the value table
     * @param i
     * @return
     */
    public double getVal(int i) {
        if (value_table.size() > i) {
            return value_table.get(i).getProbs()[0];
        }
        return -1;
    }

    /**
     * Setter of the value at row i of the value table to value p
     * @param i
     * @param p 
     */
    private void setVal(int i, double p) {
        if (value_table.size() > i) {
            value_table.get(i).getProbs()[0] = p;
        }
    }

    /**
     * getter of the variable values at row i of the valye table
     * @param i
     * @return
     */
    public ArrayList<String> getVarValues(int i) {
        if (value_table.size() > i) {
            return value_table.get(i).getPVsAsArrayList();
        }
        return null;
    }

    /**
     * getter of the complete table
     * @return table
     */
    public ArrayList<ProbRow> getTable() {
        return value_table;
    }

    /**
     * checks wheter any variables are associated with this factor
     * @return true when no variables are associated with this factor
     */
    public boolean isEmpty() {
        return variables.isEmpty();
    }

    /**
     * returns a string representation of the Factor
     * @return string representation
     */
    @Override
    public String toString() {
        String str = factor.getName();
        str += "(";
        for (Variable var : variables) {
            str += (var.getName() + ",");
        }
        str = str.substring(0, str.length() - 1);
        str += ")=\n";
        for (ProbRow pr : value_table) {
            str += (pr.toString() + "\n");
        }
        return str;

    }

}
