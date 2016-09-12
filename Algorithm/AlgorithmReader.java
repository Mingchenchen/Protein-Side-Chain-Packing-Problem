/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm;

import opusRota.*;
import scwrl4.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.io.PDBFileReader;

/**
 *
 * @author administrator
 */
public class AlgorithmReader
{

    static public Structure readStructure(int expNum, String pdbId)
    {
        PDBFileReader pdbReader = null;
        Structure struc = null;
        pdbReader = new PDBFileReader();
        pdbReader.setAlignSeqRes(true);
        pdbReader.setAutoFetch(false);
        pdbReader.setParseCAOnly(false);
        pdbReader.setParseSecStruc(false);
        try
        {
            StringBuffer sb = new StringBuffer();
            sb.append(OpusRotaRun.OUT_PATH);
            sb.append(File.separator);
            sb.append(OpusRotaRun.DF_EXP_NUM.format(expNum));
            sb.append(File.separator);
            sb.append("clean");
            sb.append(pdbId);
            sb.append(".pdb.out");
            struc = pdbReader.getStructure(sb.toString());
            struc.setPDBCode(pdbId);
        } 
        catch (IOException ex)
        {
            Logger.getLogger(AlgorithmReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return struc;
    }
}
