/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pdb;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
//import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import org.biojava.bio.structure.AminoAcid;
import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.Compound;
import org.biojava.bio.structure.Group;
import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.io.PDBFileReader;

/**
 *
 * @author rcorona
 */
public class PDBReader
{

    static public final String DEFAULT_PATH = "/home/clezcano/Documents/pdb_files";

    static public Structure readStructure(String pdbId)
    {
        PDBFileReader pdbReader = null;
        Structure struc = null;
        pdbReader = new PDBFileReader();
        pdbReader.setAlignSeqRes(true);
        pdbReader.setAutoFetch(true);
        pdbReader.setParseCAOnly(false);
        pdbReader.setParseSecStruc(true);
        pdbReader.setPath(DEFAULT_PATH);
        try
        {
            struc = pdbReader.getStructureById(pdbId);
            if (!(new File(DEFAULT_PATH + File.separator + pdbId + ".pdb")).exists() && (new File(DEFAULT_PATH + File.separator + pdbId + ".pdb.gz")).exists())
            {
                uncompressPDBFile(pdbId);
            }
        } 
        catch (IOException ex)
        {
            Logger.getLogger(PDBReader.class.getName()).log(Level.SEVERE, null, ex);
        }

        cleanStructure(struc);

        return struc;
    }

    static public Structure readStructureCollisionSCWRL4(String pdbId)
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
            sb.append("/home/clezcano/Documents/collisions/cisrr/output");
            sb.append(File.separator);
            sb.append(pdbId.toLowerCase());
            sb.append(".pdb");
            struc = pdbReader.getStructure(sb.toString());
            struc.setPDBCode(pdbId);
        }
        catch (IOException ex)
        {
            Logger.getLogger(PDBReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return struc;
    }


    static public void uncompressPDBFile(String pdbId) {
        GZIPInputStream in = null;
        try {
            String source = DEFAULT_PATH + File.separator + pdbId + ".pdb.gz";
            in = new GZIPInputStream(new FileInputStream(source));
            // Open the output file
            String target = DEFAULT_PATH + File.separator + pdbId + ".pdb";
            OutputStream out = new FileOutputStream(target);
            // Transfer bytes from the compressed file to the output file
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            // Close the file and stream
            in.close();
            out.close();
            (new File(source)).delete();
        } catch (IOException ex) {
            Logger.getLogger(PDBReader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex1) {
                Logger.getLogger(PDBReader.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    static public void main(String[] args)
    {
        Structure s = readStructure("1a0b");
        for (int i = 0; i < s.getCompounds().size(); i++)
        {
            Compound c = s.getCompounds().get(i);
           // System.out.println(" Numero : " + i + "--\n");
            System.out.println(c);
            for (int j = 0; j < c.getEcNums().size(); j++)
            {
            // System.out.println(" Numero : " + j + "***\n");
                System.out.println(c.getEcNums().get(j));
               // System.out.println(c.getEcNums());
               // System.out.println(" fin : " + j + "***\n");
            }
        }
    }

    static public ArrayList<String> readPDBList(String filename)
    {
        ArrayList<String> result = null;
        FileInputStream fis = null;
        DataInputStream dis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try
        {
            fis = new FileInputStream(filename + ".lst");
            dis = new DataInputStream(fis);
            isr = new InputStreamReader(dis);
            br = new BufferedReader(isr);
            String strLine = null;
            while ((strLine = br.readLine()) != null)
            {
                result = result == null ? new ArrayList<String>() : result;
                result.add(strLine.substring(0, 4));
            }
            br.close();
            isr.close();
            dis.close();
            fis.close();
        } 
        catch (IOException ex)
        {
            Logger.getLogger(PDBReader.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(PDBReader.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(PDBReader.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(PDBReader.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(PDBReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    private static void cleanStructure(Structure struc)
    {
        for (int chain = 0; struc != null && struc.getChains() != null && chain < struc.getChains().size(); chain++)
        {
            Chain c = struc.getChain(chain);
            for (int residue = 0; c != null && c.getAtomGroups() != null && residue < c.getAtomGroups().size(); residue++)
            {
                Group g = c.getAtomGroup(residue);
                if (!(g instanceof AminoAcid))// && g.getType().equals("amino")))
                {
                    //System.out.println("NO ES ATOM   " + g.getPDBCode() +"   " + g.getPDBName()+ "   "+ g.getType()) ;
                    c.getAtomGroups().remove(g);
                    residue--;
                }
            }
        }
    }
}
