/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rotlib;

import java.io.IOException;
import java.util.ArrayList;
import org.biojava.bio.structure.AminoAcid;
import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.Group;
import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.io.PDBFileReader;
import pdb.PDBReader;
import pdb.PDBWrite;
import pdb.db.PDBInserts;
import rotlib.io.RotamerLibraryReader;
import utils.StructureUtil;

/**
 *
 * @author administrator
 */
public class RotamerUtil
{

    static public void main(String[] args) throws IOException
    {
        PDBInserts insert = new PDBInserts();
        RotamerLibraryCollection rlc = RotamerLibraryReader.readRotamerLibrary(RotamerLibraryCollection.DUN_IND_ROT_LIB_TYPE);
        ArrayList<String> pdbIds =  PDBReader.readPDBList("pdbIds03");

        for(int i=0; i<pdbIds.size(); i++)
        {
            Structure sIn = PDBReader.readStructure(pdbIds.get(i));
            PDBFileReader pdbFR = new PDBFileReader();
            //Structure sOut = StructureUtil.createBestStructure(sIn, rlc);
            Structure sOut = pdbFR.getStructure("/home/administrator/Documents/pdb_out/"+pdbIds.get(i)+".ivy");
            sOut.setPDBCode(pdbIds.get(i));

            insertSCPResidues(insert, sIn, sOut);

            //PDBWrite.writePDB(sOut, "/home/administrator/Documents/pdb_out/"+pdbIds.get(i)+".ivy");
        }



    }
    private static void insertSCPResidues(PDBInserts insert, Structure s1, Structure s2)
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
