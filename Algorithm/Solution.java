/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Algorithm;

import org.biojava.bio.structure.Structure;
import java.util.ArrayList;
import rotlib.RotamerLibrary;
import rotlib.RotamerLibraryCollection;
import rotlib.io.RotamerLibraryReader;
import org.biojava.bio.structure.AminoAcid;
import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.Group;
import java.util.Date;
import java.util.Random;
import utils.StructureUtil;
import java.util.logging.Logger;
import java.util.logging.Level;
import rotlib.Rotamer;
/*
 import org.biojava.bio.structure.StructureException;
import org.biojava.bio.structure.Atom;
import org.biojava.bio.structure.Calc;
import utils.ListUtil;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.logging.Level;
 * */
 //*/
/**
 *
 * @author clezcano
 */
public class Solution
{
    private final static double EPSILON  = 0.00001;
    //private final static double kvdW  = 5.0;
    private final static double kvdW  = 0.0;
    //private final static double kcratio  = 1000000.0;
    //private final static double kcratio  = 10000000.0;
    private final static double kcratio  = 2250000.0;
    //private final static double kcratio  = 0.0;
    //private final static double krot  = 0.35;
    //private final static double krot  = 3.5;
    private final static double krot  = 0.0;
    private int lastChangedResidue1 = -1, lastChangedResidue2 = -1;
    private ArrayList<ArrayList<String>> RotamerVector = null;
    private ArrayList<ArrayList<String>> AAVector = null;
    private RotamerLibraryCollection rlc = RotamerLibraryReader.readRotamerLibrary(RotamerLibraryCollection.DUN_IND_ROT_LIB_TYPE);
    private Structure s_pdb = null;
    public double energy = -1, vdWPotential = -1, cRatio = -1, frequency = -1;

    public Solution()
    {}

    public Solution(Structure s1)
    {
        Chain c;
        Group g;
        AminoAcid aa1;
        //int aminoAcidIndex;
        String pdbName;

        vdWPotential = -1;
        cRatio = -1;
        frequency = -1;
        energy = -1;
        lastChangedResidue1 = -1;
        lastChangedResidue2 = -1;
        s_pdb = s1;
        RotamerVector = new ArrayList(s1.getChains().size());
        AAVector = new ArrayList(s1.getChains().size());
        for (int chain = 0; s1 != null && s1.getChains() != null && chain < s1.getChains().size(); chain++)
        {
            c = s1.getChain(chain);
            RotamerVector.add(new ArrayList());
            AAVector.add(new ArrayList());
            for (int group = 0; c != null && c.getAtomGroups() != null && group < c.getAtomGroups().size(); group++)
            {
                g = c.getAtomGroup(group);
                if (g instanceof AminoAcid)
                {
                    aa1 = (AminoAcid) g;
                    pdbName = aa1.getPDBName();
                    RotamerVector.get(chain).add("-1");
                    AAVector.get(chain).add(pdbName);
                    //aminoAcidIndex = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aa1.getPDBName());
                }
            }
        
       }
    }

    public int lastChangedResidue(int i)
    {
        int respond = -1;
        if (i == 1)
        {
           respond = lastChangedResidue1;
        }
        else if (i == 2)
        {
           respond = lastChangedResidue2;
        }
        else
        {
           System.out.println("Error lastChangedResidue");
        }
        return respond;
    }

    public ArrayList<ArrayList<String>> getRomaterVector()
    {
        return RotamerVector;
    }
    
    public ArrayList<ArrayList<String>> getAAVector()
    {
        return AAVector;
    }

    public Solution CopySolution()
    {
        Solution sol = new Solution();
        sol.s_pdb = s_pdb;
        sol.RotamerVector = new ArrayList(RotamerVector.size());
        sol.AAVector = new ArrayList(AAVector.size());
        sol.lastChangedResidue1 = lastChangedResidue1;
        sol.lastChangedResidue2 = lastChangedResidue2;
        sol.vdWPotential = vdWPotential;
        sol.cRatio = cRatio;
        sol.frequency = frequency;
        sol.energy = energy;
        for (int chain = 0; chain < AAVector.size(); chain++)
        {
            sol.RotamerVector.add(new ArrayList());
            sol.AAVector.add(new ArrayList());
            for (int group = 0; group < AAVector.get(chain).size(); group++)
            {
                sol.RotamerVector.get(chain).add(RotamerVector.get(chain).get(group));
                sol.AAVector.get(chain).add(AAVector.get(chain).get(group));
                //AAVector.get(chain).add(pdbName);
                    //aminoAcidIndex = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aa1.getPDBName());
            }
        }
        return sol;
    }
    
    public double getEnergy()
    {
        getvdWPotentialAndCRatio();
        getRotamerFrequency();
        //System.out.println("Int vdW : " + vdWPotential + " + CRatio : " + kcratio + " * " + cRatio + "( - " + kcratio * cRatio + ")" + " + FRE : " + krot + " * " + frequency + "( - " + krot * frequency + ")" + " = " + (vdWPotential + kcratio * cRatio + krot * frequency));
        energy = kvdW * vdWPotential + kcratio * cRatio + krot * frequency;
        return energy;
        //return getvdWPotential() + krot * getRotamerFrequency() + getAccuracy() * 100;
    }
    /*
    public double getEnergy()
    {
        return getAccuracy() * 100;
    }
    */
    public double getAccuracy()
    {
        if (s_pdb == null)
        {
           System.out.println("PDB in Solution is null");
        }

        return 1/((StructureUtil.AbsoluteAccuracy(s_pdb, StructureUtil.createStructure(s_pdb, this, rlc), "X1") +
                   StructureUtil.AbsoluteAccuracy(s_pdb, StructureUtil.createStructure(s_pdb, this, rlc), "X12") +
                   StructureUtil.AbsoluteAccuracy(s_pdb, StructureUtil.createStructure(s_pdb, this, rlc), "X123")
                  )/3);
    }

    public void getRotamerFrequency()
    {
        double sum = 0.0, gamma, rotamerProb;
        RotamerLibrary rl;

        for (int chain = 0; chain < AAVector.size(); chain++)
        {
            for (int group = 0; group < AAVector.get(chain).size(); group++)
            {
                if (!(AAVector.get(chain).get(group).equalsIgnoreCase("GLY") || AAVector.get(chain).get(group).equalsIgnoreCase("ALA")))
                {
                    rl = rlc.getRotamerLibrary(AAVector.get(chain).get(group));
                    if (AAVector.get(chain).get(group).equalsIgnoreCase("PHE") || AAVector.get(chain).get(group).equalsIgnoreCase("TYR") || AAVector.get(chain).get(group).equalsIgnoreCase("TRP") || AAVector.get(chain).get(group).equalsIgnoreCase("HIS"))
                    {
                        gamma = 2.0;
                    }
                    else
                    {
                        gamma = 1.0;
                    }

                    rotamerProb = rl.getRotamer().get(Integer.parseInt(RotamerVector.get(chain).get(group))).getProbabilityValue();
                    if(equals(rotamerProb, 0) == true)
                    {
                        rotamerProb = 0.001;
                    }
                    //sum += gamma * Math.log10(rotamerProb/MaxRotamerProb(rl));
                    sum += gamma * Math.log10(rotamerProb / MaxRotamerProb(AAVector.get(chain).get(group)));
                }
            }
        }
        this.frequency = -sum;
    }

    private double MaxRotamerProb(String AminoName)
    {
                double maxPro;
                if (AminoName.equalsIgnoreCase("ARG"))
                {
                   maxPro = 10.99;
                }
                else if (AminoName.equalsIgnoreCase("ASN"))
                {
                   maxPro = 28.20;
                }
                else if (AminoName.equalsIgnoreCase("ASP"))
                {
                   maxPro = 30.87;
                }
                else if (AminoName.equalsIgnoreCase("CYS"))
                {
                   maxPro = 56.64;
                }
                else if (AminoName.equalsIgnoreCase("GLN"))
                {
                   maxPro = 18.53;
                }
                else if (AminoName.equalsIgnoreCase("GLU"))
                {
                   maxPro = 18.84;
                }
                else if (AminoName.equalsIgnoreCase("HIS"))
                {
                   maxPro = 29.64;
                }
                else if (AminoName.equalsIgnoreCase("ILE"))
                {
                   maxPro = 61.58;
                }
                else if (AminoName.equalsIgnoreCase("LEU"))
                {
                   maxPro = 62.52;
                }
                else if (AminoName.equalsIgnoreCase("LYS"))
                {
                   maxPro = 22.79;
                }
                else if (AminoName.equalsIgnoreCase("MET"))
                {
                   maxPro = 18.53;
                }
                else if (AminoName.equalsIgnoreCase("PHE"))
                {
                   maxPro = 47.08;
                }
                else if (AminoName.equalsIgnoreCase("PRO"))
                {
                   maxPro = 50.72;
                }
                else if (AminoName.equalsIgnoreCase("SER"))
                {
                   maxPro = 47.07;
                }
                else if (AminoName.equalsIgnoreCase("THR"))
                {
                   maxPro = 48.86;
                }
                else if (AminoName.equalsIgnoreCase("TRP"))
                {
                   maxPro = 30.81;
                }
                else if (AminoName.equalsIgnoreCase("TYR"))
                {
                   maxPro = 47.35;
                }
                else if (AminoName.equalsIgnoreCase("VAL"))
                {
                   maxPro = 73.90;
                }
                else
                {
                   maxPro = 0;
                }

                return maxPro;
    }
/*
    private double MaxRotamerProb(RotamerLibrary rl)
    {
        double maxPro = rl.getRotamer().get(0).getProbabilityValue();
        for (int i = 0; i < rl.getRotamer().size(); i++)
        {
                if (rl.getRotamer().get(i).getProbabilityValue() > maxPro)
                {
                    maxPro = rl.getRotamer().get(i).getProbabilityValue();
                }
        }
        return maxPro;
    }
*/
    public void getvdWPotentialAndCRatio()
    {
        Structure s = StructureUtil.createStructure(s_pdb, this, rlc);
        StructureUtil.getvdWPotentialAndCRatio(s, this);
    }
    
    
    public void Generate_NeighbordSolution(int numPivots, Random random_var)
    {
        //Random random_var = new Random();
        //Date date_var = new Date();
        int chain, residueNumber;
        //random_var.setSeed(date_var.getTime());
        RotamerLibrary rl;
        
        if (numPivots == 1)
        {
           lastChangedResidue2 = -1;
        }
        for (int i = 1; i <= numPivots; i++)
        {
           chain = random_var.nextInt(AAVector.size());
           do
              residueNumber = random_var.nextInt(AAVector.get(chain).size());
           while(residueNumber == lastChangedResidue1 || residueNumber == lastChangedResidue2);
           if (!(AAVector.get(chain).get(residueNumber).equalsIgnoreCase("GLY") || AAVector.get(chain).get(residueNumber).equalsIgnoreCase("ALA")))
           {
              rl = rlc.getRotamerLibrary(AAVector.get(chain).get(residueNumber));
              //RotamerVector.get(chain).set(residueNumber, RandomRotamer(AAVector.get(chain).get(residueNumber)));
              RotamerVector.get(chain).set(residueNumber, Integer.toString(random_var.nextInt(rl.getRotamer().size())));
              if(i == 1)
              {  //System.out.println("Pivot 1");
                 lastChangedResidue1 = residueNumber;
              }
              else if(i == 2)
              {  //System.out.println("Pivot 2");
                 lastChangedResidue2 = residueNumber;
              }
           }
        }
    }
    /*
    public void Generate_NeighbordSolution(int numPivots)
    {
        Random random_var = new Random();
        Date date_var = new Date();
        int chain, residueNumber, rotamer;
        random_var.setSeed(date_var.getTime());
        RotamerLibrary rl;
        
        if (numPivots == 1)
        {
           lastChangedResidue2 = -1;
        }
        for (int i = 1; i <= numPivots; i++)
        {
           chain = random_var.nextInt(AAVector.size());
           do
              residueNumber = random_var.nextInt(AAVector.get(chain).size());
           while(residueNumber == lastChangedResidue1 || residueNumber == lastChangedResidue2);
           if (!(AAVector.get(chain).get(residueNumber).equalsIgnoreCase("GLY") || AAVector.get(chain).get(residueNumber).equalsIgnoreCase("ALA")))
           {
               rl = rlc.getRotamerLibrary(AAVector.get(chain).get(residueNumber));
              // rotamer = Integer.parseInt(RotamerVector.get(chain).get(residueNumber)) - 1;
               
               do
                  rotamer = Integer.parseInt(RandomRotamer(AAVector.get(chain).get(residueNumber)));
               while(rl.getRotamer().get(rotamer).getChiValue(1) > 0);
               
                    //rl = rlc.getRotamerLibrary(AAVector.get(chain).get(residueNumber));
                                 
               RotamerVector.get(chain).set(residueNumber, String.valueOf(rotamer));
               if(i == 1)
               {  //System.out.println("Pivot 1");
                  lastChangedResidue1 = residueNumber;
               }
               else if(i == 2)
               {  //System.out.println("Pivot 2");
                  lastChangedResidue2 = residueNumber;
               }
           }
        }
    }
*/
    /*
    public void Generate_NeighbordSolution()
    {
        int chain1, residueNumber1, residueNumber2 = 0, rotNumber, collisionNumber1, collisionNumber2;
        RotamerLibrary rl1, rl2;
        AminoAcid a1, a2;
       
        Random random_var = new Random();
        Date date_var = new Date();
        random_var.setSeed(date_var.getTime());
        chain1 = random_var.nextInt(AAVector.size());
        //chain2 = random_var.nextInt(AAVector.size());

//        do
//        {
           do
           {
              residueNumber1 = random_var.nextInt(AAVector.get(chain1).size());
           }
           while(AAVector.get(chain1).get(residueNumber1).equalsIgnoreCase("GLY") || AAVector.get(chain1).get(residueNumber1).equalsIgnoreCase("ALA"));
           
                    //do
                    //{
                    //    residueNumber2 = random_var.nextInt(AAVector.get(chain2).size());
                   // }
                    //while(AAVector.get(chain2).get(residueNumber2).equalsIgnoreCase("GLY") || AAVector.get(chain2).get(residueNumber2).equalsIgnoreCase("ALA"));
           
 //       }
 //       while(residueNumber1 == residueNumber2);
        
        rl1 = rlc.getRotamerLibrary(AAVector.get(chain1).get(residueNumber1));
        a1 = StructureUtil.createAminoacid((AminoAcid)s_pdb.getChain(chain1).getAtomGroup(residueNumber1), rl1.getRotamer().get(Integer.parseInt(RotamerVector.get(chain1).get(residueNumber1))));
        for (int i = 1; i <= 2; i++)
        {
            if(i == 1)
            {
                residueNumber2 = residueNumber1 + 1;
                if ((residueNumber2 == AAVector.get(chain1).size()) || AAVector.get(chain1).get(residueNumber2).equalsIgnoreCase("GLY") || AAVector.get(chain1).get(residueNumber2).equalsIgnoreCase("ALA"))
                {
                   continue;
                }
            }
            else if(i == 2)
            {
                residueNumber2 = residueNumber1 - 1;
                if ((residueNumber2 == -1)  ||  AAVector.get(chain1).get(residueNumber2).equalsIgnoreCase("GLY") || AAVector.get(chain1).get(residueNumber2).equalsIgnoreCase("ALA"))
                {
                   continue;
                }
            }
            System.out.println("pivot : " + residueNumber1 + " res2 : " + residueNumber2);
            rl2 = rlc.getRotamerLibrary(AAVector.get(chain1).get(residueNumber2));
            a2 = StructureUtil.createAminoacid((AminoAcid)s_pdb.getChain(chain1).getAtomGroup(residueNumber2), rl2.getRotamer().get(Integer.parseInt(RotamerVector.get(chain1).get(residueNumber2))));
            collisionNumber1 = StructureUtil.collisionNumber(a1, a2);
            System.out.println("col1 : " + collisionNumber1);
            if(collisionNumber1 > 0)
            {
                rotNumber = random_var.nextInt(rl2.getRotamer().size());
                a2 = StructureUtil.createAminoacid((AminoAcid)s_pdb.getChain(chain1).getAtomGroup(residueNumber2), rl2.getRotamer().get(rotNumber));
                collisionNumber2 = StructureUtil.collisionNumber(a1, a2);
                if(collisionNumber2 < collisionNumber1)
                {   System.out.println("col2 : " + collisionNumber2);
                    RotamerVector.get(chain1).set(residueNumber2, Integer.toString(rotNumber));
                }
            }
        }
    }
    */

    //public void Generate_RandomRotamers()
    public void Generate_RandomRotamers(Random random_var)
    {
        //Random random_var = new Random();
        //Date date_var = new Date();
        //random_var.setSeed(date_var.getTime());
        RotamerLibrary rl;
        for (int chain = 0; chain < AAVector.size(); chain++)
        {
            for (int group = 0; group < AAVector.get(chain).size(); group++)
            {
                if (!(AAVector.get(chain).get(group).equalsIgnoreCase("GLY") || AAVector.get(chain).get(group).equalsIgnoreCase("ALA")))
                {
                     rl = rlc.getRotamerLibrary(AAVector.get(chain).get(group));
                     //RotamerVector.get(chain).set(group, RandomRotamer(AAVector.get(chain).get(group)));
                     RotamerVector.get(chain).set(group, Integer.toString(random_var.nextInt(rl.getRotamer().size())));
                }
            }
        }
    }
    
    public void AssignLastRotamer()
    {
        RotamerLibrary rl;
        int LastRotamer;
        for (int chain = 0; chain < AAVector.size(); chain++)
        {
            for (int group = 0; group < AAVector.get(chain).size(); group++)
            {
                if (!(AAVector.get(chain).get(group).equalsIgnoreCase("GLY") || AAVector.get(chain).get(group).equalsIgnoreCase("ALA")))
                {
                    rl = rlc.getRotamerLibrary(AAVector.get(chain).get(group));
                    LastRotamer = rl.getRotamer().size() - 1;
                    RotamerVector.get(chain).set(group, String.valueOf(LastRotamer));
                }
            }
        }
    }

    /*
    private String RandomRotamer(String pdbName)
    {
        Random random_var = new Random();
        Date date_var = new Date();
        random_var.setSeed(date_var.getTime());

        RotamerLibrary rl = rlc.getRotamerLibrary(pdbName);
        return Integer.toString(random_var.nextInt(rl.getRotamer().size()));
    }
    */
    public void setMaxProb()
    {
        String r = "";
        for (int chain = 0; chain < AAVector.size(); chain++)
        {
            for (int group = 0; group < AAVector.get(chain).size(); group++)
            {
                if (AAVector.get(chain).get(group).equalsIgnoreCase("ARG"))
                {
                   r = "56";
                }
                else if (AAVector.get(chain).get(group).equalsIgnoreCase("ASN"))
                {
                   r = "14";
                }
                else if (AAVector.get(chain).get(group).equalsIgnoreCase("ASP"))
                {
                   r = "7";
                }
                else if (AAVector.get(chain).get(group).equalsIgnoreCase("CYS"))
                {
                   r = "2";
                }
                else if (AAVector.get(chain).get(group).equalsIgnoreCase("GLN"))
                {
                   r = "29";
                }
                else if (AAVector.get(chain).get(group).equalsIgnoreCase("GLU"))
                {
                   r = "22";
                }
                else if (AAVector.get(chain).get(group).equalsIgnoreCase("HIS"))
                {
                   r = "8";
                }
                else if (AAVector.get(chain).get(group).equalsIgnoreCase("ILE"))
                {
                   r = "7";
                }
                else if (AAVector.get(chain).get(group).equalsIgnoreCase("LEU"))
                {
                   r = "7";
                }
                else if (AAVector.get(chain).get(group).equalsIgnoreCase("LYS"))
                {
                   r = "55";
                }
                else if (AAVector.get(chain).get(group).equalsIgnoreCase("MET"))
                {
                   r = "25";
                }
                else if (AAVector.get(chain).get(group).equalsIgnoreCase("PHE"))
                {
                   r = "4";
                }
                else if (AAVector.get(chain).get(group).equalsIgnoreCase("PRO"))
                {
                   r = "0";
                }
                else if (AAVector.get(chain).get(group).equalsIgnoreCase("SER"))
                {
                   r = "0";
                }
                else if (AAVector.get(chain).get(group).equalsIgnoreCase("THR"))
                {
                   r = "0";
                }
                else if (AAVector.get(chain).get(group).equalsIgnoreCase("TRP"))
                {
                   r = "8";
                }
                else if (AAVector.get(chain).get(group).equalsIgnoreCase("TYR"))
                {
                   r = "4";
                }
                else if (AAVector.get(chain).get(group).equalsIgnoreCase("VAL"))
                {
                   r = "1";
                }
                else
                {
                   r = "-1";
                }
                RotamerVector.get(chain).set(group, r);
            }
        }
    }
    // ARG 60 ASN 14 ASP 7 CYS 2 GLN 30 GLU 22 HIS 8 ILE 7 LEU 7 LYS 61 MET 26 PHE 4 PRO 0 SER 0 THR 0 TRP 8 TYR 4 VAL 1
    

    public static boolean equals(double a, double b)
    {
        return Math.abs(a - b) < EPSILON;
    }

}
