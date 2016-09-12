/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scop.db;

import scop.entity.PDBSection;
import scop.entity.SCOPClass;
import scop.entity.SCOPDomain;
import scop.entity.SCOPFamily;
import scop.entity.SCOPFold;
import scop.entity.SCOPProtein;
import scop.entity.SCOPSpecies;
import scop.entity.SCOPSuperfamily;

/**
 *
 * @author administrator
 */
public class TestSCOPInserts {

    static public void main(String[] args){
        SCOPClass cl = new SCOPClass();
        cl.setDescription("All alpha proteins");
        cl.setSccs('a');
        cl.setSunid(46456);
        cl.setId(1);

        SCOPFold cf = new SCOPFold(cl);
        cf.setDescription("Globin-like");
        cf.setSccs(1);
        cf.setSunid(46457);
        cf.setId(1);

        SCOPSuperfamily sf = new SCOPSuperfamily(cf);
        sf.setDescription("Globin-like");
        sf.setSccs(1);
        sf.setSunid(46458);
        sf.setId(1);

        SCOPFamily fa = new SCOPFamily(sf);
        fa.setDescription("Truncated hemoglobin");
        fa.setSccs(1);
        fa.setSunid(46459);
        fa.setId(1);
        
        SCOPProtein dm = new SCOPProtein(fa);
        dm.setDescription("Protozoan/bacterial hemoglobin");
        dm.setSunid(46460);
        dm.setId(1);

        SCOPSpecies sp = new SCOPSpecies(dm);
        sp.setDescription("Ciliate (Paramecium caudatum)");
        sp.setSunid(46461);
        sp.setTaxid(5885);
        sp.setId(1);

        SCOPDomain px = new SCOPDomain(sp);
        px.setSid("d1dlwa_");
        px.setSunid(14982);
        px.setId(1);

        PDBSection px1 = new PDBSection(px);
        px1.setChain('A');
        px1.setPdbId("1dlw");
        px1.setId(1);
        
        boolean update = SCOPUpdates.updatePDBSection(px1);
        System.out.println("update = " + update);
    }

}
