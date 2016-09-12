/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scwrl4.io;

import java.io.BufferedReader;
//import java.io.DataInputStream;
import java.io.DataInputStream;
import scwrl4.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.io.PDBFileReader;
import scwrl4.entity.SCWRL4LogEntity;

/**
 *
 * @author administrator
 */
public class SCWRL4Reader
{

    static public Structure readStructure(String pdbId)
    {
        PDBFileReader pdbReader = null;
        StringBuffer sb;
        Structure struc = null;
        pdbReader = new PDBFileReader();
        pdbReader.setAlignSeqRes(true);
        pdbReader.setAutoFetch(false);
        pdbReader.setParseCAOnly(false);
        pdbReader.setParseSecStruc(true);
        try
        {
            sb = new StringBuffer();
            sb.append(SCWRL4Run.OUT_PATH);
            sb.append(File.separator);
            sb.append(pdbId.toLowerCase());
            sb.append(".out");
            struc = pdbReader.getStructure(sb.toString());
            struc.setPDBCode(pdbId);
        }
        catch (IOException ex)
        {
            Logger.getLogger(SCWRL4Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return struc;
    }

    static public Structure readStructureCollision(String pdbId)
    {
        PDBFileReader pdbReader = null;
        StringBuffer sb;
        Structure struc = null;
        pdbReader = new PDBFileReader();
        pdbReader.setAlignSeqRes(true);
        pdbReader.setAutoFetch(false);
        pdbReader.setParseCAOnly(false);
        pdbReader.setParseSecStruc(true);
        try
        {
            sb = new StringBuffer();
            sb.append("/home/clezcano/Documents/collisions/scwrl4_copy/output");
            sb.append(File.separator);
            sb.append(pdbId.toLowerCase());
            sb.append(".pdb");
            struc = pdbReader.getStructure(sb.toString());
            struc.setPDBCode(pdbId);
        }
        catch (IOException ex)
        {
            Logger.getLogger(SCWRL4Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return struc;
    }

    static public SCWRL4LogEntity readLogFile(String pdbId)
    {
        SCWRL4LogEntity log = new SCWRL4LogEntity();
        FileInputStream fis = null;
        DataInputStream dis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try
        {
            fis = new FileInputStream(SCWRL4Run.LOG_PATH + File.separator + pdbId + ".log");
            dis = new DataInputStream(fis);
            isr = new InputStreamReader(dis);
            br = new BufferedReader(isr);
            String strLine = null;
            while ((strLine = br.readLine()) != null)
            {
                log.addLine(strLine);
            }
            br.close();
            isr.close();
            dis.close();
            fis.close();
        } 
        catch (IOException ex)
        {
            Logger.getLogger(SCWRL4Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                if (br != null)
                {
                    br.close();
                }
            } 
            catch (IOException ex)
            {
                Logger.getLogger(SCWRL4Reader.class.getName()).log(Level.SEVERE, null, ex);
            }
            try
            {
                if (isr != null)
                {
                    isr.close();
                }
            } 
            catch (IOException ex)
            {
                Logger.getLogger(SCWRL4Reader.class.getName()).log(Level.SEVERE, null, ex);
            }
            try
            {
                if (dis != null)
                {
                    dis.close();
                }
            } 
            catch (IOException ex)
            {
                Logger.getLogger(SCWRL4Reader.class.getName()).log(Level.SEVERE, null, ex);
            }
            try
            {
                if (fis != null)
                {
                    fis.close();
                }
            } 
            catch (IOException ex)
            {
                Logger.getLogger(SCWRL4Reader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return log;
    }
}
