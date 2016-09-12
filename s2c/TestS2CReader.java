package s2c;


import java.io.IOException;
import org.biojava.bio.structure.AminoAcid;
import org.biojava.bio.structure.Group;
import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.StructureException;
import pdb.PDBReader;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author rcorona
 */
public class TestS2CReader
{

    static public void main(String[] args) throws IOException, StructureException
    {
        String pdbId = "1ah0";
        S2CEntity s2cTest = S2CReader.readS2CFile(pdbId);
        for (int i = 0; i < s2cTest.getSeqCrdRecords().size(); i++)
        {
            S2CResidue s2cResidue = s2cTest.getSeqCrdRecords().get(i);
        }
    }
}