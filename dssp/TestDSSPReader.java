package dssp;


import java.util.logging.Level;
import java.util.logging.Logger;
import org.biojava.bio.structure.AminoAcid;
import org.biojava.bio.structure.Group;
import org.biojava.bio.structure.Structure;
import pdb.PDBReader;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author rcorona
 */
public class TestDSSPReader {

    static public void main(String[] args) {
        String pdbId = "1atz";
        DSSPEntity dssp = DSSPReader.readDSSPFile(pdbId);
        Structure sTest = PDBReader.readStructure(pdbId);
        for (int i = 0; i < dssp.getDsspResidues().size(); i++) {
            DSSPResidue residue = dssp.getDsspResidues().get(i);
            String resCode = residue.getResidueCode();
            int iChain = residue.getChain() - 'A';
            Group g = null;
            try {
                g = sTest.getChain(iChain).getGroupByPDB(resCode);
            } catch (Exception ex) {
                System.out.println(residue.getRecord());
                System.out.println("-"+resCode+"-");
                Logger.getLogger(TestDSSPReader.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (g instanceof AminoAcid) {
                //AminoAcid aa = (AminoAcid) g;
                System.out.println(residue.getRecord());
            }
        }
    }
}