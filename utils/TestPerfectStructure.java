package utils;


import java.io.IOException;
import org.biojava.bio.structure.Structure;
import pdb.PDBReader;
import pdb.PDBWrite;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rcorona
 */
public class TestPerfectStructure
{

    static public void main(String[] args) throws IOException
    {
        String pdbId = "1atz";
        Structure sTest = PDBReader.readStructure(pdbId);
        Structure sOut = StructureUtil.createPerfectStructure(sTest);
        PDBWrite.writePDB(sOut, pdbId+".ivy");
    }

}
