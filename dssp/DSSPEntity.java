/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dssp;

import java.util.ArrayList;

/**
 *
 * @author rcorona
 */
public class DSSPEntity {

    private String pdbId;
    private ArrayList<DSSPResidue> dsspResidues;

    public DSSPEntity(String pdbId) {
        this.pdbId = pdbId;
    }

    public void addDSSPResidue(String strLine) {
        dsspResidues = getDsspResidues() == null ? new ArrayList<DSSPResidue>() : getDsspResidues();
        getDsspResidues().add(new DSSPResidue(this, strLine));
    }

    /**
     * @return the pdbId
     */
    public String getPdbId() {
        return pdbId;
    }

    /**
     * @return the dsspResidues
     */
    public ArrayList<DSSPResidue> getDsspResidues() {
        return dsspResidues;
    }

}
