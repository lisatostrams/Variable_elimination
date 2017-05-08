/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package variable_elimination;

import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author Lisa Tostrams s4386167
 * Klasse om alle stappen weg te schrijven naar log file
 */
public class Log {
    private String networkname; 
    
    
    public Log(String networkname) {
        this.networkname = networkname; 
        //schrijf hier nog netwerk naam weg
    }
    
    
    /**
     * schrijf query var weg
     * @param Q 
     */
    public void log_a(Variable Q) {
        
    }
    
    /**
     * schrijf geobserveerde var weg
     * @param O 
     */
    public void log_b(ArrayList<Variable> O) {
        
    }
    
    /**
     * schrijf product formule weg
     * schrijf gereduceerde formule weg
     */
    public void log_c() {
        
    }
    
    /**
     * schrijf geidentificeerde factoren weg
     */
    public void log_d() {
    
    }
    
    /**
     * schrijf elimination order weg
     * @param elimination_order
     */
    public void log_e(Vector<String> elimination_order) {
        
    }
    
    /**
     * Schrijf de factoren die Z bevatten weg
     * Scrhijf weg welke nieuwe factor berekend is
     * @param Z 
     */
    public void log_f(Variable Z, Variable f_z ) {
        
    }
    
    /**
     * Schrijf genormaliseerde verdeling weg
     */
    public void log_g() {
        
    }
}
