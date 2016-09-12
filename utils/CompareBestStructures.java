/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;

import java.io.IOException;
import org.biojava.bio.structure.Structure;
import pdb.PDBReader;
import pdb.PDBWrite;
import java.util.ArrayList;
import rotlib.io.RotamerLibraryReader;
import rotlib.RotamerLibraryCollection;
//import Algorithm.Algorithm;
//import scwrl4.io.SCWRL4Reader;
//import opusRota.OpusRotaReader;

/**
 *
 * @author clezcano
 */
public class CompareBestStructures
{
    static public final String PruebaPath = "/home/clezcano/Documents/IvyBioUtils/ComparacionEstructurasBest/";
    static public final String PathAlgorithm = "/home/clezcano/Documents/IvyBioUtils/Algorithm/";
    
    public static void main(String[] args) throws IOException
    {
        Structure s1 = null, s2 = null;
        ArrayList<String> alPDBIds = PDBReader.readPDBList("pdb1test");
        RotamerLibraryCollection rlc = RotamerLibraryReader.readRotamerLibrary(RotamerLibraryCollection.DUN_IND_ROT_LIB_TYPE);

        System.out.println("Total : " + alPDBIds.size());
        
        for (int i = 0; i < alPDBIds.size(); i++)
        {
            System.out.println((i + 1) + ": " + alPDBIds.get(i));
            s1 = PDBReader.readStructure(alPDBIds.get(i));
            s2 = StructureUtil.createBestStructure(s1, rlc);
            //s2 = StructureUtil.createPerfectStructure(s1);
            //System.out.println(" 1 main ////////////////////////");
            PDBWrite.writePDB(s2, PruebaPath + "BestStructures/" + alPDBIds.get(i) + ".chr");
            StructureUtil.CollisionAnalysis(PruebaPath + "Statistics/" + alPDBIds.get(i) + ".chr", s2);
            //System.out.println(" 2 main////////////////////");
        }//end for
        


        

    }//end main
}





