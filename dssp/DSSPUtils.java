/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dssp;

import dssp.db.DSSPUpdates;

/**
 *
 * @author administrator
 */
public class DSSPUtils {

    static public void updateDDSP(String pdbId) {
        updateDSSP(DSSPReader.readDSSPFile(pdbId));
    }

    static public void main(String[] args) {
        updateDDSP("2bsy");
    }

    private static void updateDSSP(DSSPEntity dssp) {
        DSSPUpdates update = new DSSPUpdates();
        for (int i = 0; dssp!=null && i < dssp.getDsspResidues().size(); i++) {
            update.updateDSSP(dssp.getDsspResidues().get(i));
        }
    }
}
