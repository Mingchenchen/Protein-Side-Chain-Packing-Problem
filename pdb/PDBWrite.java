/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pdb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.biojava.bio.structure.AminoAcid;
import org.biojava.bio.structure.Atom;
import org.biojava.bio.structure.Group;
import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.StructureException;

/**
 *
 * @author Ivetth
 */
public class PDBWrite
{

    static public void writePDB(Structure s, String fileName)
    {
        File fileOut = null;
        PrintWriter outFile = null;
        try
        {
            fileOut = new File(fileName);
            outFile = new PrintWriter(new BufferedWriter(new FileWriter(fileOut)));
            for (int iChain = 0; iChain < s.getChains().size(); iChain++)
            {
                for (int iGroup = 0; iGroup < s.getChain(iChain).getAtomGroups().size(); iGroup++)
                {
                    Group g = s.getChain(iChain).getAtomGroup(iGroup);
                    if (g instanceof AminoAcid)
                    {
                        AminoAcid aa = (AminoAcid) g;
                        for (int iAtom = 0; iAtom < aa.getAtoms().size(); iAtom++)
                        {
                            try
                            {
                                outFile.println(getATOMRecord(aa.getAtom(iAtom)));
                            } 
                            catch (StructureException ex)
                            {
                                Logger.getLogger(PDBWrite.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            }
            outFile.close();
        } 
        catch (IOException ex)
        {
            Logger.getLogger(PDBWrite.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static public String getATOMRecord(Atom a)
    {
        StringBuilder sb = new StringBuilder();
        DecimalFormat coords = new DecimalFormat("#0.000");
        DecimalFormat df2 = new DecimalFormat("#0.00");

        sb.append("ATOM  "); //record name

        //atom serial number
        String temp = Integer.toString(a.getPDBserial());
        while (temp.length() < 5)
        {
            temp = " " + temp;
        }
        sb.append(temp);
        sb.append(" ");

        //atom name
        sb.append(a.getName());
        while (sb.length() < 16)
        {
            sb.append(" ");
        }


        sb.append(a.getAltLoc());   //alt loc

        //res name
        sb.append(a.getParent().getPDBName());
        sb.append(" ");

        sb.append(a.getParent().getParent().getName()); //chain identifier

        //residue seq number + insertion code
        temp = a.getParent().getPDBCode();
        while (temp == null || temp.length() < 4)
        {
            temp = " " + temp;
        }
        if ('0' <= temp.charAt(temp.length() - 1) && temp.charAt(temp.length() - 1) <= '9')
        {
            temp = temp + " ";
        } 
        else
        {
            temp = " " + temp;
        }
        sb.append(temp);
        sb.append("   ");

        //X
        temp = coords.format(a.getX());
        while (temp.length() < 8)
        {
            temp = " " + temp;
        }
        sb.append(temp);

        //Y
        temp = coords.format(a.getY());
        while (temp.length() < 8)
        {
            temp = " " + temp;
        }
        sb.append(temp);

        //Z
        temp = coords.format(a.getZ());
        while (temp.length() < 8)
        {
            temp = " " + temp;
        }
        sb.append(temp);

        //occupancy
        temp = df2.format(a.getOccupancy());
        while (temp.length() < 6)
        {
            temp = " " + temp;
        }
        sb.append(temp);

        //temperature factor
        temp = df2.format(a.getTempFactor());
        while (temp.length() < 6)
        {
            temp = " " + temp;
        }
        sb.append(temp);
        sb.append("          ");

        //element symbol
        temp = "" + a.getName().charAt(0);
        while (temp.length() < 2)
        {
            temp = " " + temp;
        }
        sb.append(temp);

        sb.append("  ");    //charge

        return sb.toString();
    }

    static public void writeSelectedPDBs(ArrayList<String> pdbIds, String fileName)
    {
        File fileOut = null;
        PrintWriter outFile = null;
        try
        {
            fileOut = new File(fileName + ".lst");
            outFile = new PrintWriter(new BufferedWriter(new FileWriter(fileOut)));
            for (int i = 0; pdbIds != null && i < pdbIds.size(); i++)
            {
                outFile.println(pdbIds.get(i));
            }
            outFile.close();
        } 
        catch (IOException ex)
        {
            Logger.getLogger(PDBWrite.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
