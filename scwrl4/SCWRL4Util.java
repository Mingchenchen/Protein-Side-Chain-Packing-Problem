/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scwrl4;

import scwrl4.io.SCWRL4Reader;
import java.util.ArrayList;
import org.biojava.bio.structure.AminoAcid;
import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.Group;
import org.biojava.bio.structure.Structure;
import pdb.PDBReader;
import pdb.db.PDBSelect;
import scwrl4.db.SCWRL4Insert;

/**
 *
 * @author administrator
 */
public class SCWRL4Util
{

    static public void main(String[] args)
    {
        PDBSelect select = new PDBSelect();
        SCWRL4Insert insert = new SCWRL4Insert();
        ArrayList<String> pdbIds = select.getSelectedPDBEntries();
        for (int i = 0; i < pdbIds.size(); i++)
        {
            System.out.println((i + 1) + ": " + pdbIds.get(i));
            Structure s1 = PDBReader.readStructure(pdbIds.get(i));
            Structure s2 = SCWRL4Reader.readStructure(pdbIds.get(i));
            insertSCPResidues(insert, s1, s2);
        }
    }

    private static void insertSCPResidues(SCWRL4Insert insert, Structure s1, Structure s2)
    {
        for (int chain = 0; s2 != null && s2.getChains() != null && chain < s2.getChains().size(); chain++)
        {
            Chain c = s2.getChain(chain);
            for (int residue = 0; c != null && c.getAtomGroups() != null && residue < c.getAtomLength(); residue++)
            {
                Group g = c.getAtomGroup(residue);
                if (g instanceof AminoAcid)
                {
                    insert.insertSCPResidue(s1, s2, chain, residue);
                }
            }
        }
    }
}
