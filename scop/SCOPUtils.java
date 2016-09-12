/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scop;

import dssp.DSSPUtils;
import java.util.ArrayList;
import pdb.PDBUtils;
import s2c.S2CUtils;
import scop.db.SCOPSelects;
import scop.entity.PDBSection;

/**
 *
 * @author administrator
 */
public class SCOPUtils {

    static public ArrayList<PDBSection> getSCOPClassification(String pdbId) {
        SCOPSelects selects = new SCOPSelects();
        return selects.getPDBSections(pdbId);
    }

    static public void main(String[] args) {
        ArrayList<String> pdbIds = (new SCOPSelects()).getMissingMonoDomainPDBIds(2000);
        for (int i = 0; i < pdbIds.size(); i++) {
            String pdbId = pdbIds.get(i);
            PDBUtils.insertAllStructure(pdbId);
            S2CUtils.insertS2C(pdbId);
            DSSPUtils.updateDDSP(pdbId);
            System.out.println(pdbId);
        }
    }
}
