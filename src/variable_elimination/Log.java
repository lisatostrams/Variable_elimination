/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package variable_elimination;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lisa Tostrams s4386167 Klasse om alle stappen weg te schrijven naar
 * log file
 */
public class Log {

    private String networkname;

    BufferedWriter writer = null;

    /**
     *
     * @param networkname
     * @param network
     */
    public Log(String networkname, String network) {
        this.networkname = networkname;

        try {
            //create a temporary file
            String timeLog = new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss").format(Calendar.getInstance().getTime());
            File logFile = new File(timeLog + "_Log_Variable_Elimination.txt");

            // This will output the full path where the file will be written to...
            System.out.println(logFile.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(logFile));
            writer.write("Logfile for network " + networkname + "\n" + network + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * schrijf query var weg
     *
     * @param Q
     */
    public void log_a(Variable Q) {
        try {
            writer.write("\n\n -------THE STEPS OF THE VARIABLE ELIMINATION ALGORITHM------- \n\nStep a) Query variable: \n\t" + Q.getName() + "\n");
        } catch (IOException ex) {
            Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * schrijf geobserveerde var weg
     *
     * @param O
     */
    public void log_b(ArrayList<Variable> O)  {
        
        try { 
            writer.write("\nStep b) Observed variables: \n\t");
        } catch (IOException ex) {
            Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(Variable obs : O) 
            try {
                writer.write("\t"+obs.getName() + "=" + obs.getValue() + "\n");
            } catch (IOException ex) {
                Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    /**
     * schrijf product formule weg schrijf gereduceerde formule weg
     * @param red_form
     * @param prod_form
     */
    public void log_c(String red_form, String prod_form) {
        try {
            writer.write("Step c) 1) Product formula: \n\t" + prod_form + "\n\t2) Reduced formula: \n\t" + red_form + "\n" );
             
        } catch (IOException ex) {
            Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * schrijf geidentificeerde factoren weg
     * @param factor_probs
     */
    public void log_d(String factor_probs) {
        try {
            writer.write("Step d) \n\tIdentify factors and reduce observed variables: \n\t" + factor_probs);
        } catch (IOException ex) {
            Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * schrijf elimination order weg
     *
     * @param elimination_order
     */
    public void log_e(String elimination_order) {
        try {
            writer.write("Step e) Elimination order \n\t" + elimination_order + "\n");
        } catch (IOException ex) {
            Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Schrijf de factoren die Z bevatten weg Scrhijf weg welke nieuwe factor
     * berekend is
     *
     * @param Z
     */
    public void log_f(String log) {
        try {
            writer.write("\nStep f) For each variable Z in elimination order: \n\t 1) multiply factors containing Z\n\t" +" 2) sum out Z to obtain new factor f_z\n\t" +" 3) remove multiplied factors from list and add f_z\n" + log );
        } catch (IOException ex) {
            Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Schrijf genormaliseerde verdeling weg
     * @param log
     */
    public void log_g(String log) {
        try {
            writer.write("Step g) Normalize result: \n\t" + log);
        } catch (IOException ex) {
            Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            
            writer.close();
        } catch (Exception e) {
        }
    }
}
