/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pdb;

import java.util.ArrayList;
import pdb.db.PDBUpdates;
import java.util.Arrays;
import org.biojava.bio.structure.AminoAcid;
import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.Group;
import org.biojava.bio.structure.Structure;
import pdb.db.PDBInserts;
import pdb.db.PDBSelect;
import scop.db.SCOPSelects;
import scwrl4.db.SCWRL4Insert;
import utils.ListUtil;

/**
 *
 * @author administrator
 */
public class PDBUtils
{

    static public void insertAllStructure(Structure s)
    {
        PDBInserts pdbIns = new PDBInserts();
        pdbIns.insertStructure(s);
        for (int chain = 0; chain < s.getChains().size(); chain++)
        {
            pdbIns.insertChain(s.getChain(chain));
            for (int group = 0; group < s.getChain(chain).getAtomGroups().size(); group++)
            {
                if (s.getChain(chain).getAtomGroup(group) instanceof AminoAcid)
                {
                    pdbIns.insertResidue(((AminoAcid) s.getChain(chain).getAtomGroup(group)));
                    /*                    
                    for (int atom = 0; atom < ((AminoAcid) s.getChain(chain).getAtomGroup(group)).getAtoms().size(); atom++)
                     {
                    try
                    {
                        pdbIns.insertAtom(((AminoAcid) s.getChain(chain).getAtomGroup(group)).getAtom(atom));
                    }
                    catch (StructureException ex)
                    {
                        Logger.getLogger(PDBUtils.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    }
                     */
                }
            }

        }
        for (int i = 0; s.getCompounds() != null && i < s.getCompounds().size(); i++)
        {
            for (int j = 0; s.getCompounds().get(i).getEcNums() != null && j < s.getCompounds().get(i).getEcNums().size(); j++)
            {
                if (s.getCompounds().get(i).getEcNums().get(j) != null)
                {
                    pdbIns.insertECNumbers(s.getPDBCode(), s.getCompounds().get(i).getEcNums().get(j));
                }
            }
        }
    }

    static public void insertAllStructure(String pdbId)
    {
        Structure s = PDBReader.readStructure(pdbId);
        if (s != null)
        {
            insertAllStructure(s);
        }
    }

    static public void main(String[] args)
    {
        PDBSelect select = new PDBSelect();
        PDBInserts insert = new PDBInserts();
        ArrayList<String> pdbIds = select.getSelectedPDBEntries();
        for (int i = 0; i < pdbIds.size(); i++)
        {
            System.out.println((i + 1) + ": " + pdbIds.get(i));
            insertSCPResidues(insert, PDBReader.readStructure(pdbIds.get(i)));
        }
    }

    static public void checkStructure(Structure s)
    {
        PDBUpdates update = new PDBUpdates();
        for (int i = 0; s != null && s.getChains() != null && i < s.getChains().size(); i++)
        {
            Chain c = s.getChain(i);
            for (int j = 0; c != null && c.getAtomGroups() != null && j < c.getAtomGroups().size(); j++)
            {
                Group g = c.getAtomGroup(j);
                if (g instanceof AminoAcid)
                {
                    boolean complete = true;
                    AminoAcid aa = (AminoAcid) g;
                    String aaName = aa.getPDBName();
                    int aaIndex = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aaName);
                    String[] scAtoms = ListUtil.SIDE_CHAIN_ATOM_LIST[aaIndex];
                    complete = complete && aa.hasAtom("N");
                    complete = complete && aa.hasAtom("C");
                    complete = complete && aa.hasAtom("O");
                    complete = complete && aa.hasAtom("CA");
                    for (int k = 2; k < scAtoms.length; k++)
                    {
                        complete = complete && aa.hasAtom(scAtoms[k]);
                    }
                    if (!complete)
                    {
                        System.out.println(s.getPDBCode() + "\t" + c.getName() + "\t" + aa.getPDBCode());
                    }
                    update.updateCheckedResidue(aa, complete);
                }
            }
        }
    }

    private static void checkStructure(String pdbId)
    {
        Structure s = PDBReader.readStructure(pdbId);
        checkStructure(s);
    }

    private static void insertSCPResidues(PDBInserts insert, Structure s)
    {
        for (int chain = 0; s != null && s.getChains() != null && chain < s.getChains().size(); chain++)
        {
            Chain c = s.getChain(chain);
            for (int residue = 0; c != null && c.getAtomGroups() != null && residue < c.getAtomLength(); residue++)
            {
                Group g = c.getAtomGroup(residue);
                if(g instanceof AminoAcid)
                {
                    insert.insertSCPResidue(s, chain, residue);
                }
            }
        }
    }
}
