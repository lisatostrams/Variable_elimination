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
    ArrayList<ArrayList<ProbRow>> Factors = new ArrayList();
    String fform = "";
    ArrayList<Variable> Elimination = new ArrayList();
    int fsize = 0;

    Formula(ArrayList<Variable> Vs, Variable Q, ArrayList<Variable> O, ArrayList<ArrayList<ProbRow>> Ps) {
        this.Vs = Vs;
        this.O = O;
        this.Query = Q;
        this.Ps = Ps;

        form += ("P(" + Q.getName());
        ArrayList<String> obs = new ArrayList();
        if (O.size() > 0) {
            form += "|";
            for (int i = 0; i < O.size(); i++) {
                form += (O.get(i).getName() + "=" + O.get(i).getValue() + ",");
                obs.add(O.get(i).getName());
            }
            form = form.substring(0, form.length() - 1);
        }
        form += ") = ";
        pform += form;
        for (int i = 0; i < Vs.size(); i++) {
            if (obs.contains(Vs.get(i).getName())) {
                pform += ("P(" + Vs.get(i).getName() + "=" + Vs.get(i).getValue() + "|");
            } else {
                pform += ("P(" + Vs.get(i).getName() + "|");
            }
            for (int j = 0; j < i; j++) {
                if (obs.contains(Vs.get(j).getName())) {
                    pform += (Vs.get(j).getName() + "=" + Vs.get(j).getValue() + ",");
                } else {
                    pform += (Vs.get(j).getName() + ",");
                }
            }
            pform = pform.substring(0, pform.length() - 1);
            pform += ")";
        }
        for (int i = 0; i < Vs.size(); i++) {
            if (Vs.get(i).getNrOfParents() != 0) {
                if (obs.contains(Vs.get(i).getName())) {
                    form += ("P(" + Vs.get(i).getName() + "=" + Vs.get(i).getValue() + "|");
                } else {
                    form += ("P(" + Vs.get(i).getName() + "|");
                }
                product_formula.add(Vs.get(i));
                Factors.add(Ps.get(i));
                for (int j = 0; j < Vs.get(i).getParents().size(); j++) {
                    if (obs.contains(Vs.get(i).getParents().get(j).getName())) {
                        form += (Vs.get(i).getParents().get(j).getName() + "=" + Vs.get(i).getParents().get(j).getValue() + ",");
                    } else {
                        form += (Vs.get(i).getParents().get(j).getName() + ",");
                    }
                }
                form = form.substring(0, form.length() - 1);
                form += ")";
            } else {
                product_formula.add(Vs.get(i));
                Factors.add(Ps.get(i));
                if (obs.contains(Vs.get(i).getName())) {
                    form += ("P(" + Vs.get(i).getName() + "=" + Vs.get(i).getValue() + ")");
                } else {
                    form += ("P(" + Vs.get(i).getName() + ")");
                }
            }

        }
        System.out.println(pform);
        System.out.println(form);
    }

    public void factorize() {
        ArrayList<String> pr = new ArrayList();
        pr.add("p");
        for (int i = 0; i < product_formula.size(); i++) {
            boolean parent_obs = false;
            if (!product_formula.get(i).getObserved()) {
                fform += ("f_" + i + "(" + product_formula.get(i).getName());
                if (product_formula.get(i).getNrOfParents() > 0) {
                    for (int j = 0; j < product_formula.get(i).getNrOfParents(); j++) {

                        if (product_formula.get(i).getParents().get(j).getObserved()) {
                            parent_obs = true;
                            fform += ("," + product_formula.get(i).getParents().get(j).getName() + "=" + product_formula.get(i).getParents().get(j).getValue());
                        } else {
                            fform += ("," + product_formula.get(i).getParents().get(j).getName());
                        }

                    }
                }
                fform += ")";
            }

            if (product_formula.get(i).getObserved()) {
                //in text
                Factors.set(i, null);
                product_formula.set(i, null);
            } else if (parent_obs) {
                ArrayList<ProbRow> new_prop = new ArrayList();
                Variable fact = new Variable("f_" + i, pr);
                ArrayList<Variable> parents = Factors.get(i).get(0).getParents();
                parents.add(Factors.get(i).get(0).getNode());
                int[] obs_p = new int[O.size()];
                int o = 0;
                for (Variable obs : O) {
                    obs_p[o++] = parents.indexOf(obs);

                }
                for (int j = 0; j < Factors.get(i).size(); j++) {
                    boolean row_obs = true;

                    for (int p = 0; p < Factors.get(i).get(j).getParents().size(); p++) {
                        if (Factors.get(i).get(j).getParents().get(p).getObserved()) {
                            String obs_val = Factors.get(i).get(j).getParents().get(p).getValue();
                            String p_val = Factors.get(i).get(j).getPVsAsArrayList().get(p);
                            if (!p_val.equals(obs_val)) {
                                row_obs = false;

                            }
                        }
                    }
                    if (row_obs) {
                        for (int p = 0; p < Factors.get(i).get(j).getNode().getNumberOfValues(); p++) {
                            double[] probs = {Factors.get(i).get(j).getProbs()[p]};
                            ArrayList<String> tmp = Factors.get(i).get(j).getPVsAsArrayList();
                            for (int o_i = 0; o_i < obs_p.length; o_i++) {
                                tmp.remove(obs_p[o_i]);
                            }
                            String[] pv = new String[tmp.size() + 1];

                            pv = tmp.toArray(pv);
                            pv[tmp.size()] = Factors.get(i).get(j).getNode().getValues().get(p);
                            for (Variable obs : O) {
                                parents.remove(obs);
                            }
                            ProbRow new_p = new ProbRow(fact, probs, pv, parents);
                            new_prop.add(new_p);
                        }

                    }
                }
                Factors.set(i, new_prop);

            } else {
                ArrayList<ProbRow> new_prop = new ArrayList();
                Variable fact = new Variable("f_" + i, pr);
                ArrayList<Variable> parents = Factors.get(i).get(0).getParents();
                parents.add(Factors.get(i).get(0).getNode());
                for (int j = 0; j < Factors.get(i).size(); j++) {
                    for (int p = 0; p < Factors.get(i).get(j).getNode().getNumberOfValues(); p++) {
                        double[] probs = {Factors.get(i).get(j).getProbs()[p]};
                        String[] pv = new String[Factors.get(i).get(j).getPVsAsArrayList().size() + 1];
                        pv = Factors.get(i).get(j).getPVsAsArrayList().toArray(pv);
                        pv[Factors.get(i).get(j).getPVsAsArrayList().size()] = Factors.get(i).get(j).getNode().getValues().get(p);

                        ProbRow new_p = new ProbRow(fact, probs, pv, parents);
                        new_prop.add(new_p);
                    }
                }
                Factors.set(i, new_prop);

            }

        }
        fsize = Factors.size();

    }

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

    public boolean isRoot(Variable v) {
        if (v == null) {
            return false;
        }
        return !v.hasParents();
    }

    public String eliminate_Z(Variable Z) {
        //  1) multiply factors containing Z
        //  2) sum out Z to obtain new factor f_z
        //  3) remove multiplied factors from list and add f_z
        ArrayList<ProbRow> f_Z = new ArrayList();
        ArrayList<String> p = new ArrayList();
        p.add("p");
        Variable f = new Variable("f_" + fsize++, p);
        String log = "\n\n";
        for (ArrayList<ProbRow> factor : factorsContaining_Z(Z)) {
            log += (Z.getName() + " is in factor " + factor.get(0).getNode().getName() + "\n");
            if (f_Z.isEmpty()) {
                f_Z = factor;
                Factors.remove(factor);
            } else {

                ArrayList<ProbRow> tmp = new ArrayList();
                ArrayList<Variable> parents = new ArrayList();
                parents.addAll(f_Z.get(0).getParents());
                parents.addAll(factor.get(0).getParents());
                parents.remove(Z);
                int f_z_idx = f_Z.get(0).getParents().indexOf(Z);
                int factor_idx = factor.get(0).getParents().indexOf(Z);
                for (ProbRow f_z_row : f_Z) {
                    for (ProbRow factor_row : factor) {
                        if (f_z_row.getPVsAsArrayList().get(f_z_idx).equals(factor_row.getPVsAsArrayList().get(factor_idx))) {
                            ArrayList<String> parentsvalues = new ArrayList();
                            parentsvalues.addAll(f_z_row.getPVsAsArrayList());
                            parentsvalues.remove(f_z_idx);
                            parentsvalues.addAll(factor_row.getPVsAsArrayList());

                            String[] pv = new String[parentsvalues.size()];
                            pv = parentsvalues.toArray(pv);
                            double[] pr = {f_z_row.getProbs()[0] * factor_row.getProbs()[0]};
                            ProbRow new_prob = new ProbRow(f, pr, pv, parents);
                            tmp.add(new_prob);
                        }
                    }

                }
                f_Z = tmp;
                Factors.remove(factor);

            }

        }

        int f_z_idx = f_Z.get(0).getParents().indexOf(Z);
        ArrayList<ProbRow> tmp = new ArrayList();
        ArrayList<Variable> parents = new ArrayList();
        parents.addAll(f_Z.get(0).getParents());
        parents.remove(Z);

        for (ProbRow f_z_row : f_Z) {
            for (ProbRow f_z_row2 : f_Z) {
                if (f_z_row.possibleToSumOut(f_z_row2, f_z_idx)) {
                    ArrayList<String> parentsvalues = new ArrayList();
                    parentsvalues.addAll(f_z_row.getPVsAsArrayList());
                    parentsvalues.remove(f_z_idx);
                    double[] pr = {f_z_row.getProbs()[0] + f_z_row2.getProbs()[0]};
                    String[] pv = new String[parentsvalues.size()];
                    pv = parentsvalues.toArray(pv);
                    ProbRow summedout = new ProbRow(f, pr, pv, parents);
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

        f_Z = tmp;
        Factors.add(f_Z);
        log += ("\nMultiply factors and sum out " + Z.getName() + "\nAdded new factor " + f.getName() + "(" + f_Z.get(0).getParents() + ") \n\n");
        log += factor_probs();
        return log;
    }

    public ArrayList<ArrayList<ProbRow>> factorsContaining_Z(Variable Z) {
        ArrayList<ArrayList<ProbRow>> factors = new ArrayList();
        for (ArrayList<ProbRow> factor : Factors) {
            if (factor != null) {
                if (Z.getName().equals(factor.get(0).getNode().getName())) {
                    factors.add(factor);
                } else {
                    for (Variable parent : factor.get(0).getParents()) {
                        if (Z.getName().equals(parent.getName())) {
                            factors.add(factor);
                            break;
                        }
                    }

                }
            }
        }
        return factors;
    }

    public String normalize() {
        double sum = 0;
        for (ArrayList<ProbRow> factor : Factors) {
            if (factor != null) {
                for (ProbRow probability : factor) {
                    sum += probability.getProbs()[0];
                }
            }
        }

        for (ArrayList<ProbRow> factor : Factors) {
            if (factor != null) {
                for (ProbRow probability : factor) {
                    probability.getProbs()[0] /= sum;
                }
            }
        }

        String log = "\n After normalization: ";
        log += factor_probs();
        return log;

    }

    public Variable get(int i) {
        return product_formula.get(i);
    }

    public void add(Variable v) {
        product_formula.add(v);
    }

    @Override
    public String toString() {
        return form;
    }

    public String product_toString() {
        return pform;
    }

    public String factors_toString() {
        return fform;
    }

    public String factor_probs() {

        String factor_prob = "";
        factor_prob += ("p(" + Query.getName() + ") = ");
        for (int i = 0; i < Factors.size(); i++) {
            if (Factors.get(i) != null) {
                //System.out.println(Factors.get(i).size());
                //System.out.println(factor_prob);
                factor_prob += (Factors.get(i).get(0).getNode().getName() + "(");
                if (Factors.get(i).get(0).getParents().size() != 0) {
                    for (int j = 0; j < Factors.get(i).get(0).getParents().size(); j++) {
                        factor_prob += (Factors.get(i).get(0).getParents().get(j).getName() + ","); // Printing
                        // the
                        // probabilities.
                    }
                }
                factor_prob = factor_prob.substring(0, factor_prob.length() - 1);
                factor_prob += (") ");
            }
        }

        factor_prob += "\n\nThe Factors\n";
        for (int i = 0; i < Factors.size(); i++) {
            if (Factors.get(i) != null) {
                //System.out.println(Factors.get(i).size());
                //System.out.println(factor_prob);
                factor_prob += (Factors.get(i).get(0).getNode().getName() + "(");
                if (Factors.get(i).get(0).getParents().size() != 0) {
                    for (int j = 0; j < Factors.get(i).get(0).getParents().size(); j++) {
                        factor_prob += (Factors.get(i).get(0).getParents().get(j).getName() + ","); // Printing
                        // the
                        // probabilities.
                    }
                }
                factor_prob = factor_prob.substring(0, factor_prob.length() - 1);
                factor_prob += (") = \n");
                ArrayList<ProbRow> probs = Factors.get(i);
                for (int l = 0; l < probs.size(); l++) {
                    factor_prob += (probs.get(l) + "\n");
                }
                factor_prob += "\n";
            }

        }
        System.out.println(factor_prob);
        return factor_prob;
    }

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
