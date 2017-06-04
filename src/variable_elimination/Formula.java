/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package variable_elimination;

import java.util.ArrayList;

/**
 *
 * @author Lisa Tostrams s4386167
 */
public class Formula {

    ArrayList<Variable> product_formula = new ArrayList();

    ArrayList<Variable> Vs = new ArrayList();
    Variable Query;
    ArrayList<Variable> O = new ArrayList();
    ArrayList<ArrayList<ProbRow>> Ps = new ArrayList();
    String form = "";
    String pform = "";
    ArrayList<Factor> Factors = new ArrayList();

    ArrayList<Variable> Elimination = new ArrayList();
    int fsize = 0;

    /**
     * Constructor of formula
     *
     * @param Vs
     * @param Q
     * @param O
     * @param Ps
     */
    Formula(ArrayList<Variable> Vs, Variable Q, ArrayList<Variable> O, ArrayList<ArrayList<ProbRow>> Ps) {
        this.Vs = Vs;
        this.O = O;
        this.Query = Q;
        this.Ps = Ps;
        makeFormulas();

        System.out.println(pform);
        System.out.println(form);
    }

    /**
     * Create strings to represent formulas
     */
    public void makeFormulas() {
        form += ("P(" + Query.getName());

        if (O.size() > 0) {
            form += "|";
            for (int i = 0; i < O.size(); i++) {
                form += (O.get(i).getName() + "=" + O.get(i).getValue() + ",");

            }
            form = form.substring(0, form.length() - 1);
        }
        form += ") = ";
        pform += form;
        productForm();
        reducedForm();

    }

    /**
     * Create string of complete product formula
     */
    private void productForm() {
        for (int i = 0; i < Vs.size(); i++) {
            if (O.contains(Vs.get(i))) {
                pform += ("P(" + Vs.get(i).getName() + "=" + Vs.get(i).getValue() + "|");
            } else {
                pform += ("P(" + Vs.get(i).getName() + "|");
            }
            for (int j = 0; j < i; j++) {
                if (O.contains(Vs.get(j))) {
                    pform += (Vs.get(j).getName() + "=" + Vs.get(j).getValue() + ",");
                } else {
                    pform += (Vs.get(j).getName() + ",");
                }
            }
            pform = pform.substring(0, pform.length() - 1);
            pform += ")";
        }
    }

    /**
     * create String of formula reduced based on network structure
     */
    private void reducedForm() {
        for (int i = 0; i < Vs.size(); i++) {
            if (Vs.get(i).getNrOfParents() != 0) {
                if (O.contains(Vs.get(i))) {
                    form += ("P(" + Vs.get(i).getName() + "=" + Vs.get(i).getValue() + "|");
                } else {
                    form += ("P(" + Vs.get(i).getName() + "|");
                }
                product_formula.add(Vs.get(i));

                for (int j = 0; j < Vs.get(i).getParents().size(); j++) {
                    if (O.contains(Vs.get(i).getParents().get(j))) {
                        form += (Vs.get(i).getParents().get(j).getName() + "=" + Vs.get(i).getParents().get(j).getValue() + ",");
                    } else {
                        form += (Vs.get(i).getParents().get(j).getName() + ",");
                    }
                }
                form = form.substring(0, form.length() - 1);
                form += ")";
            } else {
                product_formula.add(Vs.get(i));

                if (O.contains(Vs.get(i))) {
                    form += ("P(" + Vs.get(i).getName() + "=" + Vs.get(i).getValue() + ")");
                } else {
                    form += ("P(" + Vs.get(i).getName() + ")");
                }
            }

        }
    }

    /**
     * identify factors in formula and reduce observed variables
     */
    public void factorize() {

        int factor_idx = 0;
        for (ArrayList<ProbRow> table : Ps) { //for each part of formula, factorize
            boolean parent_obs = false;
            Variable variable = table.get(0).getNode();
            if (variable.hasParents()) {
                for (Variable parent : variable.getParents()) {
                    if (parent.getObserved()) {
                        parent_obs = true;
                    }

                }
            }

            if (variable.getObserved()) {
                //no need to add to factors
                product_formula.set(product_formula.indexOf(variable), null);
            } else if (parent_obs) { //if one or more of parents is observed, reduce observed variable and create factor
                reduceObs_createFactor(variable, table, factor_idx);
                factor_idx++;
            } else { //simply create factor
                createFactor(variable, table, factor_idx);
                factor_idx++;
            }

        }
        fsize = Factors.size();

    }

    /**
     * from variable with probability table, create a new factor with name
     * f_factor_idx
     *
     * @param variable
     * @param table
     * @param factor_idx
     */
    private void createFactor(Variable variable, ArrayList<ProbRow> table, int factor_idx) {
        ArrayList<String> pr = new ArrayList();
        pr.add("p");
        ArrayList<ProbRow> new_prop = new ArrayList();
        Variable fact = new Variable("f_" + factor_idx, pr);

        ArrayList<Variable> parents;
        if (variable.hasParents()) {
            parents = variable.getParents();
        } else {
            parents = new ArrayList();
        }
        parents.add(variable);

        for (ProbRow tablerow : table) {
            for (int p = 0; p < tablerow.getNode().getNumberOfValues(); p++) {

                double[] probs = {tablerow.getProbs()[p]};
                String[] pv = new String[tablerow.getPVsAsArrayList().size() + 1];
                pv = tablerow.getPVsAsArrayList().toArray(pv);
                pv[tablerow.getPVsAsArrayList().size()] = tablerow.getNode().getValues().get(p);

                ProbRow new_p = new ProbRow(fact, probs, pv, parents);
                new_prop.add(new_p);
            }
        }
        Factor factor = new Factor(fact, parents, new_prop);
        Factors.add(factor);
    }

    /**
     * if one of the parents of variable is observed, create factor with reduced
     * observed variable
     *
     * @param variable
     * @param table
     * @param factor_idx
     */
    private void reduceObs_createFactor(Variable variable, ArrayList<ProbRow> table, int factor_idx) {
        ArrayList<String> pr = new ArrayList();
        pr.add("p");
        ArrayList<ProbRow> new_prop = new ArrayList();
        Variable fact = new Variable("f_" + factor_idx, pr);
        ArrayList<Variable> parents = new ArrayList();
        parents.addAll(variable.getParents());
        parents.add(variable);
        int[] obs_p = new int[O.size()];
        int o = 0;
        for (Variable obs : O) {
            obs_p[o++] = parents.indexOf(obs);

        }
        for (ProbRow tablerow : table) {
            boolean row_obs = true;
            for (int p = 0; p < tablerow.getParents().size(); p++) { //only add row to factor if observed value equals value in probrow
                if (tablerow.getParents().get(p).getObserved()) {
                    String obs_val = tablerow.getParents().get(p).getValue();
                    String p_val = tablerow.getPVsAsArrayList().get(p);
                    if (!p_val.equals(obs_val)) {
                        row_obs = false;

                    }
                }
            }
            if (row_obs) {
                for (int p = 0; p < tablerow.getNode().getNumberOfValues(); p++) {
                    double[] probs = {tablerow.getProbs()[p]};
                    ArrayList<String> tmp = tablerow.getPVsAsArrayList();
                    for (int o_i = 0; o_i < obs_p.length; o_i++) {
                        tmp.remove(obs_p[o_i]);
                    }
                    String[] pv = new String[tmp.size() + 1];

                    pv = tmp.toArray(pv);
                    pv[tmp.size()] = tablerow.getNode().getValues().get(p);
                    for (Variable obs : O) {
                        parents.remove(obs);
                    }
                    ProbRow new_p = new ProbRow(fact, probs, pv, parents);
                    new_prop.add(new_p);
                }

            }
        }
        Factor factor = new Factor(fact, parents, new_prop);
        Factors.add(factor);
    }

    /**
     * Deduct best elimination order: start with leafs, then roots, then the
     * rest
     *
     * @return elimination order
     */
    public ArrayList<Variable> EliminationOrder() {
        for (Variable v : product_formula) {
            if (isLeaf(v) && !v.getName().equals(Query.getName())) {
                Elimination.add(v);
            }
        }
        for (Variable v : product_formula) {
            if (isRoot(v) && !v.getName().equals(Query.getName())) {
                Elimination.add(v);
            }
        }
        for (Variable v : product_formula) {
            if (!Elimination.contains(v) && v != null && !v.getName().equals(Query.getName())) {
                Elimination.add(v);
            }
        }

        return Elimination;
    }

    /**
     * check if v is leaf node
     *
     * @param v
     * @return
     */
    public boolean isLeaf(Variable v) {
        if (v == null || v.getNrOfParents() == 0) {

            return false;
        }
        for (Variable var : Vs) {
            if (var.hasParents()) {
                if (var.getParents().contains(v) && !v.getName().equals(var.getName())) {

                    return false;
                }
            }
        }
        return true;
    }

    /**
     * check if v is root node
     *
     * @param v
     * @return
     */
    public boolean isRoot(Variable v) {
        if (v == null) {
            return false;
        }
        return !v.hasParents();
    }

    /**
     * eliminate the variable Z from the factors
     *
     * @param Z
     * @return log string
     */
    public String eliminate_Z(Variable Z) {
        //  1) multiply factors containing Z
        //  2) sum out Z to obtain new factor f_z
        //  3) remove multiplied factors from list and add f_z
        
        Factor f_Z = null;
        ArrayList<String> p = new ArrayList();
        p.add("p");
        Variable f = new Variable("f_" + fsize++, p);
        String log = "\n\n";
        for (Factor factor : factorsContaining_Z(Z)) {
            log += (Z.getName() + " is in factor " + factor.getName() + "\n");

            if (f_Z == null) {
                f_Z = factor;
                f_Z.setFactor(f);
                Factors.remove(factor);
            } else {  //multiply factors containing Z

                f_Z = f_Z.multiply(factor, Z);
                Factors.remove(factor); //remove multiplied factors from list

            }

        }

        f_Z = f_Z.sumOut(Z);
        if(!f_Z.isEmpty())
            Factors.add(f_Z);
        log += ("\nMultiply factors and sum out " + Z.getName() + "\nAdded new factor " + f_Z.getName() + "(" + f_Z.getVariables() + ") \n\n");
        log += factor_probs();
        return log;
    }

    /**
     * create a list of factors that contain variable Z
     *
     * @param Z
     * @return
     */
    public ArrayList<Factor> factorsContaining_Z(Variable Z) {
        ArrayList<Factor> factors = new ArrayList();
        for (Factor factor : Factors) {
            if (factor.getVariables().contains(Z)) {
                factors.add(factor);
            }

        }
        return factors;
    }

    /**
     * normalize values in factors
     *
     * @return
     */
    public String normalize() {
        double sum = 0;
        for (Factor factor : Factors) {
            factor.normalize();
        }

        String log = "\n After normalization: ";
        log += factor_probs();
        return log;

    }

    /**
     * string of reduced formula
     *
     * @return
     */
    @Override
    public String toString() {
        return form;
    }

    /**
     * string of product formula
     *
     * @return
     */
    public String product_toString() {
        return pform;
    }

    /**
     * create string of factor probabilities
     *
     * @return
     */
    public String factor_probs() {

        String factor_prob = "";
        factor_prob += ("p(" + Query.getName() + ") = ");
        for (Factor factor : Factors) {

            factor_prob += (factor.getName() + "(");

            for (Variable var : factor.getVariables()) {
                factor_prob += (var.getName() + ",");
            }

            factor_prob = factor_prob.substring(0, factor_prob.length() - 1);
            factor_prob += (") ");

        }

        factor_prob += "\n\nThe Factors\n";
        for (Factor factor : Factors) {

            factor_prob += (factor.toString() + "\n");

        }
        System.out.println(factor_prob);
        return factor_prob;
    }

    /**
     * string of elimination ordering of variables
     *
     * @return
     */
    public String elimination_toString() {
        String el_o = "";
        el_o += "Elimination order: \n";
        for (int i = 0; i < Elimination.size(); i++) {
            el_o += (Elimination.get(i).getName() + " > ");
        }
        el_o = el_o.substring(0, el_o.length() - 2);
        System.out.println(el_o);
        return el_o;
    }
}
