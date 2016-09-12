/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opusRota;

import java.util.logging.Level;
import java.util.logging.Logger;
import opusRota.db.OpusRotaUpdate;
import java.util.ArrayList;
import opusRota.db.OpusRotaInsert;
import org.biojava.bio.structure.AminoAcid;
import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.Group;
import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.StructureException;
import pdb.PDBReader;
import pdb.db.PDBSelect;
import scwrl4.io.SCWRL4Reader;

/**
 *
 * @author administrator
 */
public class OpusRotaUtil
{

    static public void main(String[] args)
    {
        PDBSelect select = new PDBSelect();
        OpusRotaUpdate update = new OpusRotaUpdate();
        ArrayList<String> pdbIds = select.getSelectedPDBEntries();
        for (int expNum = 0; expNum < 1; expNum++)
        {
            for (int i = 0; i < pdbIds.size(); i++)
            {
                System.out.println(expNum + ":" + i + ": " + pdbIds.get(i));
                Structure s1 = PDBReader.readStructure("full" + pdbIds.get(i));
                Structure s2 = OpusRotaReader.readStructure(expNum, pdbIds.get(i));
                Structure s3 = SCWRL4Reader.readStructure(pdbIds.get(i));
                s1.setPDBCode(pdbIds.get(i));
                updateRMSD(update, s1, s2, s3, expNum);
            }
        }
    }

    private static void updateRMSD(OpusRotaUpdate update, Structure s1, Structure s2, Structure s3, int expNum)
    {
        for (int chain = 0; s2 != null && s2.getChains() != null && chain < s2.getChains().size(); chain++)
        {
            Chain c = s2.getChain(chain);
            c.setName(s1.getChain(chain).getName());
            for (int residue = 0; c != null && c.getAtomGroups() != null && residue < c.getAtomLength(); residue++)
            {
                Group g = c.getAtomGroup(residue);
                if (g instanceof AminoAcid)
                {
                    /*
                     if (g.getPDBName().equalsIgnoreCase("ALA"))
                     {
                        AminoAcid aa1 = (AminoAcid) s1.getChain(chain).getAtomGroup(residue);
                        AminoAcid aa2 = (AminoAcid) s2.getChain(chain).getAtomGroup(residue);
                        AminoAcid aa3 = null;
                        try
                        {
                           aa3 = (AminoAcid) s3.getChain(chain).getGroupByPDB(aa1.getPDBCode());
                        }
                        catch (StructureException ex)
                        {
                           Logger.getLogger(OpusRotaUtil.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    //System.out.println("aa1 = " + aa1);
                    //System.out.println("aa3 = " + aa3);
                    //String rmsd = utils.StructureUtil.getRMSD(s1, s2, chain, residue);
                    //System.out.println("rmsd = " + rmsd);
                    //rmsd = utils.StructureUtil.getRMSD(aa1, aa1);
                    //System.out.println("rmsd = " + rmsd);
                    System.out.println("ivy "+aa1);
                    String rmsd1 = utils.StructureUtil.getRMSD(aa1, aa2);
                    System.out.println("rmsd = " + rmsd1);
                    String rmsd2 = utils.StructureUtil.getRMSD(aa1, aa3);
                    System.out.println("rmsd = " + rmsd2);
                    //if (!rmsd1.equalsIgnoreCase(rmsd2))
                    //System.out.println("ivy "+aa1);
                    }*/
                    AminoAcid aa1 = (AminoAcid) s1.getChain(chain).getAtomGroup(residue);
                    AminoAcid aa2 = (AminoAcid) s2.getChain(chain).getAtomGroup(residue);
                    AminoAcid aa3 = null;
                    try
                    {
                        aa3 = (AminoAcid) s3.getChain(chain).getGroupByPDB(aa1.getPDBCode());
                    }
                    catch (StructureException ex)
                    {
                        Logger.getLogger(OpusRotaUtil.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    update.updateRMSD(aa1, aa2, aa3, expNum);
                }
            }
        }
    }

    static public void insertAllInfo()
    {
        PDBSelect select = new PDBSelect();
        OpusRotaInsert insert = new OpusRotaInsert();
        ArrayList<String> pdbIds = select.getSelectedPDBEntries();
        //int expNum = 0;
        //String pdbId = "1HXN";
        for (int expNum = 29; expNum < 30; expNum++)
        {
            for (int i = 0; i < pdbIds.size(); i++)
            {
                System.out.println(expNum + ":" + i + ": " + pdbIds.get(i));
                Structure s1 = PDBReader.readStructure("clean" + pdbIds.get(i));
                Structure s2 = OpusRotaReader.readStructure(expNum, pdbIds.get(i));
                //Structure s1 = PDBReader.readStructure("clean"+pdbId);
                s1.setPDBCode(pdbIds.get(i));
                //Structure s2 = OpusRotaReader.readStructure(expNum, pdbId);
                insertSCPResidues(insert, s1, s2, expNum);
            }
        }
    }

    private static void insertSCPResidues(OpusRotaInsert insert, Structure s1, Structure s2, int expNum)
    {
        for (int chain = 0; s2 != null && s2.getChains() != null && chain < s2.getChains().size(); chain++)
        {
            Chain c = s2.getChain(chain);
            c.setName(s1.getChain(chain).getName());
            for (int residue = 0; c != null && c.getAtomGroups() != null && residue < c.getAtomLength(); residue++)
            {
                Group g = c.getAtomGroup(residue);
                if (g instanceof AminoAcid)
                {
                    insert.insertSCPResidue(s1, s2, chain, residue, expNum);
                }
            }
        }
    }
}
