/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package variable_elimination;

import java.util.ArrayList;

/**
 *
 * @author Lisa Tostrams
 */
public class Factor {

    ArrayList<ProbRow> prob_table;
    Variable factor;
    ArrayList<Variable> variables;

    public Factor(Variable factor, ArrayList<Variable> variables, ArrayList<ProbRow> table) {
        this.factor = factor;
        this.prob_table = table;
        this.variables = variables;
    }

    public String getName() {
        return factor.getName();
    }

    public ArrayList<Variable> getVariables() {
        return variables;
    }

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
                    double[] pr = {this.getProb(this_row) * other.getProb(other_row)}; //new probablilty
                    ProbRow new_prob = new ProbRow(factor, pr, vv, parents);
                    tmp.add(new_prob);
                }
            }

        }
        Factor factor_Z = new Factor(factor, parents, tmp);

        return factor_Z;
    }

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

    public Factor sumOut(Variable Z) {
        int f_z_idx = getVariables().indexOf(Z);
        ArrayList<ProbRow> tmp = new ArrayList();
        ArrayList<Variable> parents = new ArrayList();
        parents.addAll(getVariables());
        parents.remove(Z);

        for (int row1 = 0; row1 < prob_table.size(); row1++) {
            for (int row2 = 0; row2 < prob_table.size(); row2++) { //sum out Z to obtain new factor
                if (possibleToSumOut(row1, row2, f_z_idx)) { //sum rows where possible

                    ArrayList<String> varvalues = new ArrayList();
                    varvalues.addAll(getVarValues(row1));
                    varvalues.remove(f_z_idx);

                    double[] pr = {getProb(row1) + getProb(row2)};
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

    public void normalize() {
        double sum = 0;
        for (int i = 0; i < prob_table.size(); i++) {
            sum += getProb(i);
        }
        for (int i = 0; i < prob_table.size(); i++) {
            setProb(i, getProb(i) / sum);
        }
    }

    public double getProb(int i) {
        if (prob_table.size() > i) {
            return prob_table.get(i).getProbs()[0];
        }
        return -1;
    }

    private void setProb(int i, double p) {
        if (prob_table.size() > i) {
            prob_table.get(i).getProbs()[0] = p;
        }
    }

    public ArrayList<String> getVarValues(int i) {
        if (prob_table.size() > i) {
            return prob_table.get(i).getPVsAsArrayList();
        }
        return null;
    }

    public ArrayList<ProbRow> getTable() {
        return prob_table;
    }

    @Override
    public String toString() {
        String str = factor.getName();
        str += "(";
        for (Variable var : variables) {
            str += (var.getName() + ",");
        }
        str = str.substring(0, str.length() - 1);
        str += ")=\n";
        for (ProbRow pr : prob_table) {
            str += (pr.toString() + "\n");
        }
        return str;

    }

}
