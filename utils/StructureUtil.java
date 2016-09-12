/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

//import java.text.DecimalFormat;
import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Iterator;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.biojava.bio.structure.AminoAcid;
import org.biojava.bio.structure.AminoAcidImpl;
//import org.biojava.bio.structure.Atom;
//import org.biojava.bio.structure.Calc;
//import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.ChainImpl;
//import org.biojava.bio.structure.Group;
//import org.biojava.bio.structure.GroupIterator;
import org.biojava.bio.structure.SVDSuperimposer;
import org.biojava.bio.structure.Structure;
//import org.biojava.bio.structure.StructureException;
import org.biojava.bio.structure.StructureImpl;
import org.biojava.bio.structure.io.PDBParseException;
import rotlib.Rotamer;
import rotlib.RotamerLibrary;
import rotlib.RotamerLibraryCollection;
import s2c.S2CEntity;
import s2c.S2CReader;
import s2c.S2CResidue;
import Algorithm.Solution;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import org.biojava.bio.structure.io.PDBFileReader;
import java.text.DecimalFormat;
import java.util.Arrays;
import org.biojava.bio.structure.AminoAcid;
import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.Group;
import org.biojava.bio.structure.StructureException;
import org.biojava.bio.structure.Atom;
import org.biojava.bio.structure.Calc;
import java.io.IOException;
import pdb.PDBWrite;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author rcorona
 */
public class StructureUtil
{

    private static DecimalFormat df = new DecimalFormat("#0.0000000");
    private static final double EPSILON = 0.00001;

    public static Structure createPerfectStructure(Structure sIn)
    {
        int counter = 0;
        String resNum = "";
        Structure sOut = new StructureImpl();
        for (int iChain = 0; iChain < sIn.getChains().size(); iChain++)
        {
            sOut.addChain(new ChainImpl());
            sOut.getChain(iChain).setName(sIn.getChain(iChain).getName());
        }
        
        S2CEntity s2c = S2CReader.readS2CFile(sIn.getPDBCode());
        
        for (int i = 0; i < s2c.getSeqCrdRecords().size(); i++)
        {
            //System.out.println(" hola 1 : ");
            S2CResidue s2cResidue = s2c.getSeqCrdRecords().get(i);
            //System.out.println(" hola 2 : ");
            /*if (!s2cResidue.getSeqresThreeLetterCode().equalsIgnoreCase("MSE"))
            {
                 System.out.println("ENCONTRO MSE");
            */
  
            if (s2cResidue.hasAtomRecord() && s2cResidue.isAAstandard())
            {
                try
                {
                    char chain = s2cResidue.getChain();
                    resNum = s2cResidue.getAtomResNum();
                    int iChain = chain - 'A';
              //      System.out.println("tamano : " + s2c.getSeqCrdRecords().size() + " numero residuo :" +  resNum);
                    //System.out.println(" 1 structural");
                    //System.out.println("Chain : " + chain + " iChain : " + iChain + " Residuo : " + resNum);
                    //System.out.println(" Residuo : " + resNum);
                    Group g = sIn.getChain(iChain).getGroupByPDB(resNum);
                    //System.out.println(" 2 structural");
                    if (g instanceof AminoAcid )// && ////if(g.getType().equals("amino"))
                    {
                        //System.out.println(" 2.2 structural");
                        //System.out.print("Number : " + ++counter);
                        AminoAcid aa = (AminoAcid) g;
                        sOut.getChain(iChain).addGroup(createPerfectAminoAcid2(aa));
                    }
                    //System.out.println(" 3 structural");
                }
                catch (StructureException ex)
                {
                    Logger.getLogger(StructureUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            /*else
            {
               System.out.println(" NO Entro : ");
            }*/
        //System.out.println(" hola 3 : ");
        }

        return sOut;
    }

    private static Group createPerfectAminoAcid(AminoAcid aaIn)
    {
        Atom a, x1, x2 ,x3, x4;
        double chi, delta, k;
        AminoAcid aaOut = new AminoAcidImpl();
        try
        {
            aaOut.setAminoType(aaIn.getAminoType());
            aaOut.setPDBCode(aaIn.getPDBCode());
            aaOut.setPDBName(aaIn.getPDBName());
            aaOut.setPDBFlag(aaIn.has3D());
//System.out.print("\nAA : " + aaIn.getPDBName());
            if (aaIn.hasAtom("N"))
            {
                aaOut.addAtom(aaIn.getN());
            }

            if (aaIn.hasAtom("CA"))
            {
                aaOut.addAtom(aaIn.getCA());
            }

            if (aaIn.hasAtom("C"))
            {
                aaOut.addAtom(aaIn.getC());
            }

            if (aaIn.hasAtom("O"))
            {
                aaOut.addAtom(aaIn.getO());
            }

            if (!aaIn.getPDBName().equalsIgnoreCase("GLY"))
            {
                aaOut.addAtom(aaIn.getCB());
            }

            String aminoAcid = aaIn.getPDBName();
            int aminoAcidIndex = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aaIn.getPDBName());
            
            String[] sideChainAtomList = ListUtil.SIDE_CHAIN_ATOM_LIST[aminoAcidIndex];

            for (int i = 3; i < sideChainAtomList.length; i++)
            {
                if (aaIn.hasAtom(sideChainAtomList[i]) && aaIn.hasAtom(sideChainAtomList[i - 3]) && aaIn.hasAtom(sideChainAtomList[i - 2]) && aaIn.hasAtom(sideChainAtomList[i - 1]))
                {
                     x1 = aaIn.getAtom(sideChainAtomList[i - 3]);
                     x2 = aaIn.getAtom(sideChainAtomList[i - 2]);
                     x3 = aaIn.getAtom(sideChainAtomList[i - 1]);
                     x4 = aaIn.getAtom(sideChainAtomList[i]);
                     chi = getTorsionAngle(x1, x2, x3, x4);
                     //System.out.print("\nchi : " + chi);
                     delta = BondUtil.getBondAngle(aminoAcid, i - 2);
                     //System.out.print("\tangle : " + delta);
                     k = BondUtil.getBondLength(aminoAcid, i - 2);
                     //System.out.println("\tlength : " + k);

                     x1 = aaOut.getAtom(sideChainAtomList[i - 3]);
                     x2 = aaOut.getAtom(sideChainAtomList[i - 2]);
                     x3 = aaOut.getAtom(sideChainAtomList[i - 1]);
                     a = AngleUtil.getAtom(x1, x2, x3, chi, delta, k);

                     a.setAltLoc(x4.getAltLoc());
                     a.setFullName(x4.getFullName());
                     a.setName(x4.getName());
                     a.setOccupancy(0);
                     a.setPDBserial(x4.getPDBserial());
                     a.setTempFactor(0);
                     aaOut.addAtom(a);
                }
            }
            fillAminoAcid(aaOut);

        }
        catch (StructureException ex)
        {
            Logger.getLogger(StructureUtil.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (PDBParseException ex)
        {
            Logger.getLogger(StructureUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return aaOut;
    }


    private static Group createPerfectAminoAcid2(AminoAcid aaIn)
    {
        Atom a, x1, x2 ,x3, x4, atomA, atomB;
        double chi, delta, k;
        AminoAcid aaOut = new AminoAcidImpl();
        try
        {
            aaOut.setAminoType(aaIn.getAminoType());
            aaOut.setPDBCode(aaIn.getPDBCode());
            aaOut.setPDBName(aaIn.getPDBName());
            aaOut.setPDBFlag(aaIn.has3D());
//System.out.print("\nAA : " + aaIn.getPDBName());
            if (aaIn.hasAtom("N"))
            {
                aaOut.addAtom(aaIn.getN());
            }

            if (aaIn.hasAtom("CA"))
            {
                aaOut.addAtom(aaIn.getCA());
            }

            if (aaIn.hasAtom("C"))
            {
                aaOut.addAtom(aaIn.getC());
            }

            if (aaIn.hasAtom("O"))
            {
                aaOut.addAtom(aaIn.getO());
            }

            if (!aaIn.getPDBName().equalsIgnoreCase("GLY"))
            {
                aaOut.addAtom(aaIn.getCB());
            }

            String aminoAcid = aaIn.getPDBName();
            int aminoAcidIndex = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aaIn.getPDBName());

            String[] sideChainAtomList = ListUtil.SIDE_CHAIN_ATOM_LIST[aminoAcidIndex];

            for (int i = 3; i < sideChainAtomList.length; i++)
            {
                if (aaIn.hasAtom(sideChainAtomList[i]) && aaIn.hasAtom(sideChainAtomList[i - 3]) && aaIn.hasAtom(sideChainAtomList[i - 2]) && aaIn.hasAtom(sideChainAtomList[i - 1]))
                {
                     x1 = aaIn.getAtom(sideChainAtomList[i - 3]);
                     x2 = aaIn.getAtom(sideChainAtomList[i - 2]);
                     x3 = aaIn.getAtom(sideChainAtomList[i - 1]);
                     x4 = aaIn.getAtom(sideChainAtomList[i]);
                     chi = getTorsionAngle(x1, x2, x3, x4);
                     //System.out.print("\nchi : " + chi);
                     delta = BondUtil.getBondAngle(aminoAcid, i - 2);
                     /*atomA = Calc.substract(x3, x2);
                     atomB = Calc.substract(x4, x3);
                     delta = (180 - Calc.angle(atomA, atomB));
                     System.out.print("\tangle : " + delta);*/
                     k = BondUtil.getBondLength(aminoAcid, i - 2);
                     // k = Calc.getDistance(x4, x3);
                     //System.out.println("\tlength : " + k);

                     x1 = aaOut.getAtom(sideChainAtomList[i - 3]);
                     x2 = aaOut.getAtom(sideChainAtomList[i - 2]);
                     x3 = aaOut.getAtom(sideChainAtomList[i - 1]);
                     a = AngleUtil.getAtom(x1, x2, x3, chi, delta, k);

                     a.setAltLoc(x4.getAltLoc());
                     a.setFullName(x4.getFullName());
                     a.setName(x4.getName());
                     a.setOccupancy(0);
                     a.setPDBserial(x4.getPDBserial());
                     a.setTempFactor(0);
                     aaOut.addAtom(a);
                }
            }
            fillAminoAcid(aaOut);

        }
        catch (StructureException ex)
        {
            Logger.getLogger(StructureUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (PDBParseException ex)
        {
            Logger.getLogger(StructureUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return aaOut;
    }

    private static void fillAminoAcid(AminoAcid aaOut)
    {
        try
        {
            int aminoAcidIndex = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aaOut.getPDBName());
            switch (aminoAcidIndex)
            {
                case 0:
                    //ALA
                    break;
                case 1:
                    //ARG
                    if (aaOut.hasAtom("CD") && aaOut.hasAtom("NE") && aaOut.hasAtom("CZ"))
                    {
                      Atom nh1 = null;
                      Atom nh2 = null;
                      Atom x1 = aaOut.getAtom("CD");
                      Atom x2 = aaOut.getAtom("NE");
                      Atom x3 = aaOut.getAtom("CZ");
                      nh1 = AngleUtil.getAtom(x1, x2, x3, 180, 120, 1.326); //C-NC2 = 1.326
                      nh2 = AngleUtil.getAtom(x1, x2, x3, 0, 120, 1.326); //C-NC2 = 1.326
                      nh1.setName("NH1");
                      nh2.setName("NH2");
                      aaOut.addAtom(nh1);
                      aaOut.addAtom(nh2);
                    }
                    break;
                case 2:
                    //ASN
                    if (aaOut.hasAtom("CA") && aaOut.hasAtom("CB") && aaOut.hasAtom("CG") && aaOut.hasAtom("OD1"))
                    {
                      Atom nd2 = null;
                      Atom x1 = aaOut.getAtom("CA");
                      Atom x2 = aaOut.getAtom("CB");
                      Atom x3 = aaOut.getAtom("CG");
                      Atom x4 = aaOut.getAtom("OD1");
                      double chi = getTorsionAngle(x1, x2, x3, x4);
                      nd2 = AngleUtil.getAtom(x1, x2, x3, 180 + chi, 120, 1.328); //C-NH2 = 1.328
                      nd2.setName("ND2");
                      aaOut.addAtom(nd2);
                    }
                    break;
                case 3:
                    //ASP
                    if (aaOut.hasAtom("CA") && aaOut.hasAtom("CB") && aaOut.hasAtom("CG") && aaOut.hasAtom("OD1"))
                    {
                      Atom od2 = null;
                      Atom x1 = aaOut.getAtom("CA");
                      Atom x2 = aaOut.getAtom("CB");
                      Atom x3 = aaOut.getAtom("CG");
                      Atom x4 = aaOut.getAtom("OD1");
                      double chi = getTorsionAngle(x1, x2, x3, x4);
                      od2 = AngleUtil.getAtom(x1, x2, x3, 180 + chi, 120, 1.249); //C-OC = 1.249
                      od2.setName("OD2");
                      aaOut.addAtom(od2);
                    }
                    break;
                case 4:
                    //CYS
                    break;
                case 5:
                    //GLN
                    if (aaOut.hasAtom("CB") && aaOut.hasAtom("CG") && aaOut.hasAtom("CD") && aaOut.hasAtom("OE1"))
                    {
                      Atom ne2 = null;
                      Atom x1 = aaOut.getAtom("CB");
                      Atom x2 = aaOut.getAtom("CG");
                      Atom x3 = aaOut.getAtom("CD");
                      Atom x4 = aaOut.getAtom("OE1");
                      double chi = getTorsionAngle(x1, x2, x3, x4);
                      ne2 = AngleUtil.getAtom(x1, x2, x3, 180 + chi, 120, 1.328); //C-NH2 = 1.328
                      ne2.setName("NE2");
                      aaOut.addAtom(ne2);
                    }
                    break;
                case 6:
                    //GLU
                    if (aaOut.hasAtom("CB") && aaOut.hasAtom("CG") && aaOut.hasAtom("CD") && aaOut.hasAtom("OE1"))
                    {
                      Atom oe2 = null;
                      Atom x1 = aaOut.getAtom("CB");
                      Atom x2 = aaOut.getAtom("CG");
                      Atom x3 = aaOut.getAtom("CD");
                      Atom x4 = aaOut.getAtom("OE1");
                      double chi = getTorsionAngle(x1, x2, x3, x4);
                      oe2 = AngleUtil.getAtom(x1, x2, x3, 180 + chi, 120, 1.249); //C-OC = 1.249
                      oe2.setName("OE2");
                      aaOut.addAtom(oe2);
                    }
                    break;
                case 7:
                    //GLY
                    break;
                case 8:
                    //HIS
                    if (aaOut.hasAtom("CA") && aaOut.hasAtom("CB") && aaOut.hasAtom("CG") && aaOut.hasAtom("ND1"))
                    {
                      Atom cd2 = null;
                      Atom x1 = aaOut.getAtom("CA");
                      Atom x2 = aaOut.getAtom("CB");
                      Atom x3 = aaOut.getAtom("CG");
                      Atom x4 = aaOut.getAtom("ND1");
                      double chi = getTorsionAngle(x1, x2, x3, x4);
                      cd2 = AngleUtil.getAtom(x1, x2, x3, chi + 180, (129.1 + 131.2) / 2, (1.354 + 1.356) / 2);
                      cd2.setName("CD2");
                      aaOut.addAtom(cd2);
                    }
                    
                    if (aaOut.hasAtom("CB") && aaOut.hasAtom("CG") && aaOut.hasAtom("ND1"))
                    {
                      Atom ce1 = null;
                      Atom x1 = aaOut.getAtom("CB");
                      Atom x2 = aaOut.getAtom("CG");
                      Atom x3 = aaOut.getAtom("ND1");
                      ce1 = AngleUtil.getAtom(x1, x2, x3, 180, 105.6, 1.319);
                      ce1.setName("CE1");
                      aaOut.addAtom(ce1);
                    }

                    if (aaOut.hasAtom("CB") && aaOut.hasAtom("CG") && aaOut.hasAtom("CD2"))
                    {
                      Atom ne2 = null;
                      Atom x1 = aaOut.getAtom("CB");
                      Atom x2 = aaOut.getAtom("CG");
                      Atom x3 = aaOut.getAtom("CD2");
                      ne2 = AngleUtil.getAtom(x1, x2, x3, 180, (106.5 + 107.2) / 2, 1.374);
                      ne2.setName("NE2");
                      aaOut.addAtom(ne2);
                    }
                    break;
                case 9:
                    //ILE
                    if (aaOut.hasAtom("N") && aaOut.hasAtom("CA") && aaOut.hasAtom("CB") && aaOut.hasAtom("CG1"))
                    {
                      Atom cg2 = null;
                      Atom x1 = aaOut.getAtom("N");
                      Atom x2 = aaOut.getAtom("CA");
                      Atom x3 = aaOut.getAtom("CB");
                      Atom x4 = aaOut.getAtom("CG1");
                      double chi = getTorsionAngle(x1, x2, x3, x4);
                      cg2 = AngleUtil.getAtom(x1, x2, x3, chi - 120, 109.5, 1.521); //CH1E-CH3E = 1.521
                      cg2.setName("CG2");
                      aaOut.addAtom(cg2);
                    }
                    break;
                case 10:
                    //LEU
                    if (aaOut.hasAtom("CA") && aaOut.hasAtom("CB") && aaOut.hasAtom("CG") && aaOut.hasAtom("CD1"))
                    {
                      Atom cd2 = null;
                      Atom x1 = aaOut.getAtom("CA");
                      Atom x2 = aaOut.getAtom("CB");
                      Atom x3 = aaOut.getAtom("CG");
                      Atom x4 = aaOut.getAtom("CD1");
                      double chi = getTorsionAngle(x1, x2, x3, x4);
                      cd2 = AngleUtil.getAtom(x1, x2, x3, chi + 120, 109.5, 1.521); //CH1E-CH3E = 1.521
                      cd2.setName("CD2");
                      aaOut.addAtom(cd2);
                    }
                    break;
                case 11:
                    //LYS
                    break;
                case 12:
                    //MET
                    break;
                case 13:
                    //PHE
                    if (aaOut.hasAtom("CA") && aaOut.hasAtom("CB") && aaOut.hasAtom("CG") && aaOut.hasAtom("CD1"))
                    {
                      Atom cd2 = null;
                      Atom x1 = aaOut.getAtom("CA");
                      Atom x2 = aaOut.getAtom("CB");
                      Atom x3 = aaOut.getAtom("CG");
                      Atom x4 = aaOut.getAtom("CD1");
                      double chi = getTorsionAngle(x1, x2, x3, x4);
                      cd2 = AngleUtil.getAtom(x1, x2, x3, chi + 180, 120, 1.384); //CF - CR1E = 1.384
                      cd2.setName("CD2");
                      aaOut.addAtom(cd2);
                    }
                    
                    if (aaOut.hasAtom("CB") && aaOut.hasAtom("CG") && aaOut.hasAtom("CD1"))
                    {
                      Atom ce1 = null;
                      Atom x1 = aaOut.getAtom("CB");
                      Atom x2 = aaOut.getAtom("CG");
                      Atom x3 = aaOut.getAtom("CD1");
                      ce1 = AngleUtil.getAtom(x1, x2, x3, 180, 120, 1.382); //CR1E-CR1E=1.382
                      ce1.setName("CE1");
                      aaOut.addAtom(ce1);
                    }

                    if (aaOut.hasAtom("CB") && aaOut.hasAtom("CG") && aaOut.hasAtom("CD2"))
                    {
                      Atom ce2 = null;
                      Atom x1 = aaOut.getAtom("CB");
                      Atom x2 = aaOut.getAtom("CG");
                      Atom x3 = aaOut.getAtom("CD2");
                      ce2 = AngleUtil.getAtom(x1, x2, x3, 180, 120, 1.382); //CR1E-CR1E=1.382
                      ce2.setName("CE2");
                      aaOut.addAtom(ce2);
                    }

                    if (aaOut.hasAtom("CG") && aaOut.hasAtom("CD1") && aaOut.hasAtom("CE1"))
                    {
                      Atom cz = null;
                      Atom x1 = aaOut.getAtom("CG");
                      Atom x2 = aaOut.getAtom("CD1");
                      Atom x3 = aaOut.getAtom("CE1");
                      cz = AngleUtil.getAtom(x1, x2, x3, 0, 120, 1.382); //CR1E-CR1E=1.382
                      cz.setName("CZ");
                      aaOut.addAtom(cz);
                    }
                    break;
                case 14:
                    //PRO
                    break;
                case 15:
                    //SER
                    break;
                case 16:
                    //THR
                    if (aaOut.hasAtom("N") && aaOut.hasAtom("CA") && aaOut.hasAtom("CB") && aaOut.hasAtom("OG1"))
                    {
                      Atom cg2 = null;
                      Atom x1 = aaOut.getAtom("N");
                      Atom x2 = aaOut.getAtom("CA");
                      Atom x3 = aaOut.getAtom("CB");
                      Atom x4 = aaOut.getAtom("OG1");
                      double chi = getTorsionAngle(x1, x2, x3, x4);
                      cg2 = AngleUtil.getAtom(x1, x2, x3, chi - 120, 109.5, 1.521); //CH1E-CH3E = 1.521
                      cg2.setName("CG2");
                      aaOut.addAtom(cg2);
                    }
                    break;
                case 17:
                    //TRP
                    if (aaOut.hasAtom("CA") && aaOut.hasAtom("CB") && aaOut.hasAtom("CG") && aaOut.hasAtom("CD1"))
                    {
                      Atom cd2 = null;
                      Atom x1 = aaOut.getAtom("CA");
                      Atom x2 = aaOut.getAtom("CB");
                      Atom x3 = aaOut.getAtom("CG");
                      Atom x4 = aaOut.getAtom("CD1");
                      double chi = getTorsionAngle(x1, x2, x3, x4);
                      cd2 = AngleUtil.getAtom(x1, x2, x3, chi + 180, 126.8, 1.433);
                      cd2.setName("CD2");
                      aaOut.addAtom(cd2);
                    }

                    if (aaOut.hasAtom("CB") && aaOut.hasAtom("CG") && aaOut.hasAtom("CD1"))
                    {
                      Atom ne1 = null;
                      Atom x1 = aaOut.getAtom("CB");
                      Atom x2 = aaOut.getAtom("CG");
                      Atom x3 = aaOut.getAtom("CD1");
                      ne1 = AngleUtil.getAtom(x1, x2, x3, 180, 110.2, 1.374);
                      ne1.setName("NE1");
                      aaOut.addAtom(ne1);
                    }

                    if (aaOut.hasAtom("CB") && aaOut.hasAtom("CG") && aaOut.hasAtom("CD2"))
                    {
                      Atom ce2 = null;
                      Atom x1 = aaOut.getAtom("CB");
                      Atom x2 = aaOut.getAtom("CG");
                      Atom x3 = aaOut.getAtom("CD2");
                      ce2 = AngleUtil.getAtom(x1, x2, x3, 180, 107.2, 1.409);
                      ce2.setName("CE2");
                      aaOut.addAtom(ce2);
                    }
                    
                    if (aaOut.hasAtom("CB") && aaOut.hasAtom("CG") && aaOut.hasAtom("CD2"))
                    {
                      Atom ce3 = null;
                      Atom x1 = aaOut.getAtom("CB");
                      Atom x2 = aaOut.getAtom("CG");
                      Atom x3 = aaOut.getAtom("CD2");
                      ce3 = AngleUtil.getAtom(x1, x2, x3, 0, 133.9, 1.398);
                      ce3.setName("CE3");
                      aaOut.addAtom(ce3);
                    }

                    if (aaOut.hasAtom("CG") && aaOut.hasAtom("CD2") && aaOut.hasAtom("CE2"))
                    {
                      Atom cz2 = null;
                      Atom x1 = aaOut.getAtom("CG");
                      Atom x2 = aaOut.getAtom("CD2");
                      Atom x3 = aaOut.getAtom("CE2");
                      cz2 = AngleUtil.getAtom(x1, x2, x3, 180, 122.4, 1.394);
                      cz2.setName("CZ2");
                      aaOut.addAtom(cz2);
                    }

                    if (aaOut.hasAtom("CG") && aaOut.hasAtom("CD2") && aaOut.hasAtom("CE3"))
                    {
                      Atom cz3 = null;
                      Atom x1 = aaOut.getAtom("CG");
                      Atom x2 = aaOut.getAtom("CD2");
                      Atom x3 = aaOut.getAtom("CE3");
                      cz3 = AngleUtil.getAtom(x1, x2, x3, 180, 118.6, 1.382);
                      cz3.setName("CZ3");
                      aaOut.addAtom(cz3);
                    }

                    if (aaOut.hasAtom("CD2") && aaOut.hasAtom("CE2") && aaOut.hasAtom("CZ2"))
                    {
                      Atom ch2 = null;
                      Atom x1 = aaOut.getAtom("CD2");
                      Atom x2 = aaOut.getAtom("CE2");
                      Atom x3 = aaOut.getAtom("CZ2");
                      ch2 = AngleUtil.getAtom(x1, x2, x3, 0, 117.5, 1.368);
                      ch2.setName("CH2");
                      aaOut.addAtom(ch2);
                    }
                    break;
                case 18:
                    //TYR
                    if (aaOut.hasAtom("CA") && aaOut.hasAtom("CB") && aaOut.hasAtom("CG") && aaOut.hasAtom("CD1"))
                    {
                      Atom cd2 = null;
                      Atom x1 = aaOut.getAtom("CA");
                      Atom x2 = aaOut.getAtom("CB");
                      Atom x3 = aaOut.getAtom("CG");
                      Atom x4 = aaOut.getAtom("CD1");
                      double chi = getTorsionAngle(x1, x2, x3, x4);
                      cd2 = AngleUtil.getAtom(x1, x2, x3, chi + 180, 120, 1.389); //CF - CR1E = 1.384
                      cd2.setName("CD2");
                      aaOut.addAtom(cd2);
                    }

                    if (aaOut.hasAtom("CB") && aaOut.hasAtom("CG") && aaOut.hasAtom("CD1"))
                    {
                      Atom ce1 = null;
                      Atom x1 = aaOut.getAtom("CB");
                      Atom x2 = aaOut.getAtom("CG");
                      Atom x3 = aaOut.getAtom("CD1");
                      ce1 = AngleUtil.getAtom(x1, x2, x3, 180, 120, 1.382); //CR1E-CR1E=1.382
                      ce1.setName("CE1");
                      aaOut.addAtom(ce1);
                    }

                    if (aaOut.hasAtom("CB") && aaOut.hasAtom("CG") && aaOut.hasAtom("CD2"))
                    {
                      Atom ce2 = null;
                      Atom x1 = aaOut.getAtom("CB");
                      Atom x2 = aaOut.getAtom("CG");
                      Atom x3 = aaOut.getAtom("CD2");
                      ce2 = AngleUtil.getAtom(x1, x2, x3, 180, 120, 1.382); //CR1E-CR1E=1.382
                      ce2.setName("CE2");
                      aaOut.addAtom(ce2);
                    }

                    if (aaOut.hasAtom("CG") && aaOut.hasAtom("CD1") && aaOut.hasAtom("CE1"))
                    {
                      Atom cz = null;
                      Atom x1 = aaOut.getAtom("CG");
                      Atom x2 = aaOut.getAtom("CD1");
                      Atom x3 = aaOut.getAtom("CE1");
                      cz = AngleUtil.getAtom(x1, x2, x3, 0, 120, 1.378); //CR1E-CR1E=1.382
                      cz.setName("CZ");
                      aaOut.addAtom(cz);
                    }

                    if (aaOut.hasAtom("CD1") && aaOut.hasAtom("CE1") && aaOut.hasAtom("CZ"))
                    {
                      Atom oh = null;
                      Atom x1 = aaOut.getAtom("CD1");
                      Atom x2 = aaOut.getAtom("CE1");
                      Atom x3 = aaOut.getAtom("CZ");
                      oh = AngleUtil.getAtom(x1, x2, x3, 180, 120, 1.376); //CR1E-CR1E=1.382
                      oh.setName("OH");
                      aaOut.addAtom(oh);
                    }
                    break;
                case 19:
                    //VAL
                    if (aaOut.hasAtom("N") && aaOut.hasAtom("CA") && aaOut.hasAtom("CB") && aaOut.hasAtom("CG1"))
                    {
                      Atom cg2 = null;
                      Atom x1 = aaOut.getAtom("N");
                      Atom x2 = aaOut.getAtom("CA");
                      Atom x3 = aaOut.getAtom("CB");
                      Atom x4 = aaOut.getAtom("CG1");
                      double chi = getTorsionAngle(x1, x2, x3, x4);
                      cg2 = AngleUtil.getAtom(x1, x2, x3, chi + 120, 109.5, 1.521); //CH1E-CH3E = 1.521
                      cg2.setName("CG2");
                      aaOut.addAtom(cg2);
                    }
            }
        } 
        catch (StructureException ex)
        {
            Logger.getLogger(StructureUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static public String getX1(Structure s, int chain, int residue)
    {
        return getSideChainTorsionAngle(s, chain, residue, 1);
    }

    static public String getX2(Structure s, int chain, int residue)
    {
        return getSideChainTorsionAngle(s, chain, residue, 2);
    }

    static public String getX3(Structure s, int chain, int residue)
    {
        return getSideChainTorsionAngle(s, chain, residue, 3);
    }

    static public String getX4(Structure s, int chain, int residue)
    {
        return getSideChainTorsionAngle(s, chain, residue, 4);
    }

    static public String getX5(Structure s, int chain, int residue)
    {
        return getSideChainTorsionAngle(s, chain, residue, 5);
    }

    static public String getRMSD(Structure s1, Structure s2, int chain, int residue)
    {
        AminoAcid aa1 = (AminoAcid) s1.getChain(chain).getAtomGroup(residue);
        AminoAcid aa2 = (AminoAcid) s2.getChain(chain).getAtomGroup(residue);
        return getRMSD(aa1, aa2);
    }

    static public String getRMSD(AminoAcid aa1, AminoAcid aa2)
    {
        int aminoAcidIndex = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aa2.getPDBName());
        String[] sideChainAtomList = ListUtil.ATOM_LIST[aminoAcidIndex];
        ArrayList<Atom> alAtom1 = new ArrayList();
        ArrayList<Atom> alAtom2 = new ArrayList();
        for (int i = 4; i < sideChainAtomList.length; i++)
        {
            String atomName = sideChainAtomList[i];
            if (aa1 != null && aa2 != null && aa1.hasAtom(atomName) && aa2.hasAtom(atomName))
            {
                try
                {
                    alAtom1.add(aa1.getAtom(atomName));
                    alAtom2.add(aa2.getAtom(atomName));
                } 
                catch (StructureException ex)
                {
                    Logger.getLogger(StructureUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        Atom[] atom1 = new Atom[alAtom1.size()];
        Atom[] atom2 = new Atom[alAtom2.size()];
        for (int i = 0; i < atom1.length; i++)
        {
            atom1[i] = alAtom1.get(i);
            atom2[i] = alAtom2.get(i);
        }
        double rmsd = -1;
        try
        {
            rmsd = alAtom1.size() == 0 ? 0 : SVDSuperimposer.getRMS(atom1, atom2);
        } 
        catch (StructureException ex)
        {
            Logger.getLogger(StructureUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return df.format(rmsd);
    }

    static private String getSideChainTorsionAngle(Structure s, int chain, int residue, int i)
    {
        i = i + 3;
        AminoAcid aa = (AminoAcid) s.getChain(chain).getAtomGroup(residue);
        int aminoAcidIndex = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aa.getPDBName());
        String[] scAtomList = ListUtil.SIDE_CHAIN_ATOM_LIST[aminoAcidIndex];
        if (scAtomList.length >= i && aa.hasAtom(scAtomList[i - 4]) && aa.hasAtom(scAtomList[i - 3]) && aa.hasAtom(scAtomList[i - 2]) && aa.hasAtom(scAtomList[i - 1]))
        {
            double torsionAngle = 0;
            try
            {
                torsionAngle = getTorsionAngle(
                        aa.getAtom(scAtomList[i - 4]),
                        aa.getAtom(scAtomList[i - 3]),
                        aa.getAtom(scAtomList[i - 2]),
                        aa.getAtom(scAtomList[i - 1]));
            } 
            catch (StructureException ex)
            {
                Logger.getLogger(StructureUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
            return df.format(torsionAngle);
        } 
        else
        {
            return null;
        }
    }

    public static double getTorsionAngle(Atom a, Atom b, Atom c, Atom d)
            throws StructureException
    {
        Atom ab = Calc.substract(a, b);
        Atom cb = Calc.substract(c, b);
        Atom bc = Calc.substract(b, c);
        Atom dc = Calc.substract(d, c);

        Atom abc = Calc.vectorProduct(ab, cb);
        Atom bcd = Calc.vectorProduct(bc, dc);

        double angl = Calc.angle(abc, bcd);

        Atom vecprod = Calc.vectorProduct(abc, bcd);
        double val = Calc.skalarProduct(cb, vecprod);
       /* if (Math.abs(val) <= EPSILON)
        {
            System.out.println("///----////////=============-----------ATENCION getTorsionAngle() archivo: StructuralUtil.java");
            return 180;
        }*/
        if (val < 0.0)
        {
            angl = -angl;
        }
        return angl;
    }

    static public void cisRR(Solution sol, Structure s, RotamerLibraryCollection rlc)
    {
        //Rotamer r;
        AminoAcid aa1, aa2, aa11, aa22;
        Chain c1, c2;
        Group g1, g2;
        RotamerLibrary rl1, rl2;
        int aminoAcidIndex1, aminoAcidIndex2;
        Solution collisionSol = sol.CopySolution();
        boolean collision = false;
        double bestEnergy = sol.getEnergy();

        for (int chain1 = 0; s != null && s.getChains() != null && chain1 < s.getChains().size(); chain1++)
        {
            c1 = s.getChain(chain1);
            //for (int residue1 = 0; c1 != null && c1.getAtomGroups() != null && residue1 < c1.getAtomLength(); residue1++)
            for (int residue1 = 0; c1 != null && c1.getAtomGroups() != null && residue1 < c1.getAtomLength() - 1; residue1++)
            {
                 g1 = c1.getAtomGroup(residue1);
                 if (g1 instanceof AminoAcid)
                 {
                     aa1 = (AminoAcid) g1;
                     rl1 = rlc.getRotamerLibrary(aa1.getPDBName());
                     aminoAcidIndex1 = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aa1.getPDBName());
                     if (aminoAcidIndex1 != 7 && aminoAcidIndex1 != 0)
                     {
                           for (int rot1 = 0; rl1 != null && rl1.getRotamer() != null && rot1 < rl1.getRotamer().size(); rot1++)
                           {
                                aa11 = createAminoacid(aa1, rl1.getRotamer().get(rot1));
                                collision = false;
                                collisionSol.getRomaterVector().get(chain1).set(residue1, String.valueOf(rot1));
                                for (int chain2 = 0; s != null && s.getChains() != null && chain2 < s.getChains().size(); chain2++)
                                {
                                       c2 = s.getChain(chain2);
                                       //for (int residue2 = 0; c2 != null && c2.getAtomGroups() != null && residue2 < c2.getAtomLength(); residue2++)
                                       for (int residue2 = residue1 + 1; c2 != null && c2.getAtomGroups() != null && residue2 < c2.getAtomLength(); residue2++)
                                       {
                                           //if(residue2 != residue1)
                                           //{
                                               g2 = c2.getAtomGroup(residue2);
                                               if (g2 instanceof AminoAcid)
                                               {
                                                  aa2 = (AminoAcid) g2;
                                                  aminoAcidIndex2 = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aa2.getPDBName());
                                                  if (aminoAcidIndex2 != 7 && aminoAcidIndex2 != 0)
                                                  {
                                                       rl2 = rlc.getRotamerLibrary(aa2.getPDBName());
                                                       aa22 = createAminoacid(aa2, rl2.getRotamer().get(Integer.parseInt(sol.getRomaterVector().get(chain2).get(residue2))));
                                                       if(isThereCollision(aa11, aa22))
                                                       {
                                                            collision = true;
                                                            for (int rot2 = 0; rl2 != null && rl2.getRotamer() != null && rot2 < rl2.getRotamer().size(); rot2++)
                                                            {
                                                                  collisionSol.getRomaterVector().get(chain2).set(residue2, String.valueOf(rot2));
                                                                  if(collisionSol.getEnergy() < bestEnergy)
                                                                  {
                                                                       sol.getRomaterVector().get(chain2).set(residue2, String.valueOf(rot2));
                                                                       sol.getRomaterVector().get(chain1).set(residue1, String.valueOf(rot1));
                                                                       bestEnergy = collisionSol.energy;
                                                                  }
                                                            }
                                                            collisionSol.getRomaterVector().get(chain2).set(residue2, sol.getRomaterVector().get(chain2).get(residue2));
                                                       }
                                                  }
                                               }
                                           //}
                                       }
                                }

                                if (collision == false)
                                {
                                       if(collisionSol.getEnergy() < bestEnergy)
                                       {
                                           sol.getRomaterVector().get(chain1).set(residue1, String.valueOf(rot1));
                                           bestEnergy = collisionSol.energy;
                                       }
                                }
                           }
                           collisionSol.getRomaterVector().get(chain1).set(residue1, sol.getRomaterVector().get(chain1).get(residue1));
                     }
                 }
            }
        }
    }
    
    public static Structure createBestStructure(Structure sIn, RotamerLibraryCollection rlc)
    {
        Rotamer r;
        AminoAcid aaIn;
        Chain c;
        Group g;
        RotamerLibrary rl;
        int aminoAcidIndex;
        Structure sOut = null;
        sOut = new StructureImpl();
        sOut.setPDBCode(sIn.getPDBCode());

        for (int chain = 0; sIn != null && sIn.getChains() != null && chain < sIn.getChains().size(); chain++)
        {
            c = sIn.getChain(chain);
            sOut.addChain(new ChainImpl());
            sOut.getChain(chain).setName(c.getName());

            for (int group = 0; c != null && c.getAtomGroups() != null && group < c.getAtomGroups().size(); group++)
            {
                g = c.getAtomGroup(group);
                if (g instanceof AminoAcid)
                {
                    r = null;
                    aaIn = (AminoAcid) g;
                    aminoAcidIndex = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aaIn.getPDBName());
                    if (aminoAcidIndex != 7 && aminoAcidIndex != 0)
                    {
                        if (rlc.getRotamerLibraryType().equalsIgnoreCase(RotamerLibraryCollection.DUN_IND_ROT_LIB_TYPE))
                        {
                            rl = rlc.getRotamerLibrary(aaIn.getPDBName());
                            r = RotamerLibraryUtil.getBestRotamer(rl, aaIn);
                        }
                    }
                    //System.out.print("\nAA : " + aaIn.getPDBName());
                    sOut.getChain(chain).addGroup(createAminoacid(aaIn, r));
                }
            }
        }
        return sOut;
    }

    public static void getTorsionAnglesFromStructure(Structure sIn, PrintWriter outFile)
    {
        Atom x1, x2 ,x3, x4;
        AminoAcid aaIn;
        Chain c;
        Group g;
        int aminoAcidIndex, counter = 0;
        String[] sideChainAtomList;
        double chi;

        for (int chain = 0; sIn != null && sIn.getChains() != null && chain < sIn.getChains().size(); chain++)
        {
            c = sIn.getChain(chain);
            for (int group = 0; c != null && c.getAtomGroups() != null && group < c.getAtomGroups().size(); group++)
            {
                g = c.getAtomGroup(group);
                if (g instanceof AminoAcid)
                {
                    aaIn = (AminoAcid) g;
                    aminoAcidIndex = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aaIn.getPDBName());
                    System.out.print("\nNumber: " + ++counter); outFile.print("\nNumber: " + counter);
                    System.out.print("\nAA : " + aaIn.getPDBName());outFile.print("\nAA : " + aaIn.getPDBName());
                    sideChainAtomList = ListUtil.SIDE_CHAIN_ATOM_LIST[aminoAcidIndex];
                    try
                    {
                       for (int i = 3; i < sideChainAtomList.length; i++)
                       {
                           if (aaIn.hasAtom(sideChainAtomList[i]) && aaIn.hasAtom(sideChainAtomList[i - 3]) && aaIn.hasAtom(sideChainAtomList[i - 2]) && aaIn.hasAtom(sideChainAtomList[i - 1]))
                           {
                              x1 = aaIn.getAtom(sideChainAtomList[i - 3]);
                              x2 = aaIn.getAtom(sideChainAtomList[i - 2]);
                              x3 = aaIn.getAtom(sideChainAtomList[i - 1]);
                              x4 = aaIn.getAtom(sideChainAtomList[i]);
                              chi = getTorsionAngle(x1, x2, x3, x4);
                              System.out.print("\nchi : " + chi);outFile.print("\nchi : " + chi);
                           }
                       }
                    }
                    catch (StructureException ex)
                    {
                       Logger.getLogger(StructureUtil.class.getName()).log(Level.SEVERE, null, ex);
                    }
               }
            }
        }
    }


    public static Structure createBestStructureRSMD(Structure sIn, RotamerLibraryCollection rlc)
    {
        Rotamer r;
        AminoAcid aaIn;
        Chain c;
        Group g;
        RotamerLibrary rl;
        int aminoAcidIndex;
        Structure sOut = null;
        sOut = new StructureImpl();
        sOut.setPDBCode(sIn.getPDBCode());

        for (int chain = 0; sIn != null && sIn.getChains() != null && chain < sIn.getChains().size(); chain++)
        {
            c = sIn.getChain(chain);
            sOut.addChain(new ChainImpl());
            sOut.getChain(chain).setName(c.getName());

            for (int group = 0; c != null && c.getAtomGroups() != null && group < c.getAtomGroups().size(); group++)
            {
                g = c.getAtomGroup(group);
                if (g instanceof AminoAcid)
                {
                    r = null;
                    aaIn = (AminoAcid) g;
                    aminoAcidIndex = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aaIn.getPDBName());
                    if (aminoAcidIndex != 7 && aminoAcidIndex != 0)
                    {
                        if (rlc.getRotamerLibraryType().equalsIgnoreCase(RotamerLibraryCollection.DUN_IND_ROT_LIB_TYPE))
                        {
                            rl = rlc.getRotamerLibrary(aaIn.getPDBName());
                            //r = RotamerLibraryUtil.getBestRotamer(rl, aaIn);
                            r = RotamerLibraryUtil.getBestRotamerRMSD(rl, aaIn);
                        }
                    }
                    sOut.getChain(chain).addGroup(createAminoacid(aaIn, r));
                }
            }
        }
        return sOut;
    }


    public static void createBestStructure(Structure sIn, Solution sol, RotamerLibraryCollection rlc)
    {
        Rotamer r;
        AminoAcid aaIn;
        Chain c;
        Group g;
        RotamerLibrary rl;
        int aminoAcidIndex;
        Structure sOut = null;
        sOut = new StructureImpl();
        sOut.setPDBCode(sIn.getPDBCode());

        for (int chain = 0; sIn != null && sIn.getChains() != null && chain < sIn.getChains().size(); chain++)
        {
            c = sIn.getChain(chain);
            sOut.addChain(new ChainImpl());
            sOut.getChain(chain).setName(c.getName());

            for (int group = 0; c != null && c.getAtomGroups() != null && group < c.getAtomGroups().size(); group++)
            {
                g = c.getAtomGroup(group);
                if (g instanceof AminoAcid)
                {
                    r = null;
                    aaIn = (AminoAcid) g;
                    aminoAcidIndex = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aaIn.getPDBName());
                    if (aminoAcidIndex != 7 && aminoAcidIndex != 0)
                    {
                        if (rlc.getRotamerLibraryType().equalsIgnoreCase(RotamerLibraryCollection.DUN_IND_ROT_LIB_TYPE))
                        {
                            rl = rlc.getRotamerLibrary(aaIn.getPDBName());
                            r = RotamerLibraryUtil.getBestRotamer(rl, aaIn);
                            sol.getRomaterVector().get(chain).set(group, Integer.toString(RotamerLibraryUtil.getNumberBestRotamer(rl, aaIn)));
                        }
                    }
                    sOut.getChain(chain).addGroup(createAminoacid(aaIn, r));
                }
            }
        }
    }


    public static void createBestStructureRSMD(Structure sIn, Solution sol, RotamerLibraryCollection rlc)
    {
        Rotamer r;
        AminoAcid aaIn;
        Chain c;
        Group g;
        RotamerLibrary rl;
        int aminoAcidIndex;
        Structure sOut = null;
        sOut = new StructureImpl();
        sOut.setPDBCode(sIn.getPDBCode());

        for (int chain = 0; sIn != null && sIn.getChains() != null && chain < sIn.getChains().size(); chain++)
        {
            c = sIn.getChain(chain);
            sOut.addChain(new ChainImpl());
            sOut.getChain(chain).setName(c.getName());

            for (int group = 0; c != null && c.getAtomGroups() != null && group < c.getAtomGroups().size(); group++)
            {
                g = c.getAtomGroup(group);
                if (g instanceof AminoAcid)
                {
                    r = null;
                    aaIn = (AminoAcid) g;
                    aminoAcidIndex = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aaIn.getPDBName());
                    if (aminoAcidIndex != 7 && aminoAcidIndex != 0)
                    {
                        if (rlc.getRotamerLibraryType().equalsIgnoreCase(RotamerLibraryCollection.DUN_IND_ROT_LIB_TYPE))
                        {
                            rl = rlc.getRotamerLibrary(aaIn.getPDBName());
                            r = RotamerLibraryUtil.getBestRotamerRMSD(rl, aaIn);
                            sol.getRomaterVector().get(chain).set(group, Integer.toString(RotamerLibraryUtil.getNumberBestRotamerRMSD(rl, aaIn)));
                        }
                    }
                    sOut.getChain(chain).addGroup(createAminoacid(aaIn, r));
                }
            }
        }
    }

    public static Structure createStructure(Structure sIn, Solution sol, RotamerLibraryCollection rlc)
    {
        
        Rotamer r;
        Chain c;
        Group g;
        AminoAcid aaIn;
        Structure sOut = null;
        RotamerLibrary rl;

        sOut = new StructureImpl();
        sOut.setPDBCode(sIn.getPDBCode());
       

        for (int chain = 0; sIn != null && sIn.getChains() != null && chain < sIn.getChains().size(); chain++)
        {
            c = sIn.getChain(chain);
            sOut.addChain(new ChainImpl());
            sOut.getChain(chain).setName(c.getName());

            for (int group = 0; c != null && c.getAtomGroups() != null && group < c.getAtomGroups().size(); group++)
            {
                g = c.getAtomGroup(group);
                if (g instanceof AminoAcid)
                {
                    r = null;
                    aaIn = (AminoAcid) g;
                    int aminoAcidIndex = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aaIn.getPDBName());
                    if (aminoAcidIndex != 7 && aminoAcidIndex != 0)
                    {
                        if (rlc.getRotamerLibraryType().equalsIgnoreCase(RotamerLibraryCollection.DUN_IND_ROT_LIB_TYPE))
                        {
                            rl = rlc.getRotamerLibrary(aaIn.getPDBName());
                            r = rl.getRotamer().get(Integer.parseInt(sol.getRomaterVector().get(chain).get(group)));
                            //r = RotamerLibraryUtil.getBestRotamer(rl, aaIn);
                        }
                    }
                    sOut.getChain(chain).addGroup(createAminoacid(aaIn, r));
                }
            }
        }
       
        return sOut;
    }

    public static void getDistancesAndAnglesFromStructure(Structure sIn, PrintWriter outFile, PrintWriter outFile2, PrintWriter outFile3)
    {
        AminoAcid aaIn;
        Chain c;
        Group g;
        int aminoAcidIndex;
        double [][] aminoDistances = new double[20][4];
        for (int i = 0; i < 20; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                aminoDistances[i][j] = -1;
            }
        }
        for (int chain = 0; sIn != null && sIn.getChains() != null && chain < sIn.getChains().size(); chain++)
        {
            c = sIn.getChain(chain);
            for (int group = 0; c != null && c.getAtomGroups() != null && group < c.getAtomGroups().size(); group++)
            {
                g = c.getAtomGroup(group);
                if (g instanceof AminoAcid)
                {
                    aaIn = (AminoAcid) g;
                    aminoAcidIndex = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aaIn.getPDBName());
                    if (aminoAcidIndex != 7 && aminoAcidIndex != 0)
                    {
                        outFile.println("\nAminoAcid : " + aaIn.getPDBName());
                        outFile2.println("\nAminoAcid : " + aaIn.getPDBName());
                        outFile3.println("\nAminoAcid : " + aaIn.getPDBName());
                        DistancesAndAnglesFromStructure(aaIn, aminoDistances, outFile, outFile2, outFile3);
                    }
                 }
             }
        }
    }

    public static void DistancesAndAnglesFromStructure(AminoAcid aaIn, double [][] aminoDistances, PrintWriter outFile, PrintWriter outFile2, PrintWriter outFile3)
    {
        int aminoAcidIndex = -1;
        String[] sideChainAtomList;
        double distance;
        Atom atom1, atom2, atom3, atomA, atomB;
        //AminoAcid aa1, aa2;


        aminoAcidIndex = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aaIn.getPDBName());
        sideChainAtomList = ListUtil.SIDE_CHAIN_ATOM_LIST[aminoAcidIndex];
        for (int i = 3; i < sideChainAtomList.length; i++)
        {
             if (aaIn.hasAtom(sideChainAtomList[i]) && aaIn.hasAtom(sideChainAtomList[i - 1]))
             {
                  try
                  {
                         atom1 = aaIn.getAtom(sideChainAtomList[i]);
                         atom2 = aaIn.getAtom(sideChainAtomList[i - 1]);
                         distance = Calc.getDistance(atom1, atom2);
                         outFile.print("\t" + distance);
                         if(aminoDistances[aminoAcidIndex][i - 3] == -1)
                         {
                            aminoDistances[aminoAcidIndex][i - 3] = roundDouble(distance, 3);
                            outFile3.print("\t" + aminoDistances[aminoAcidIndex][i - 3]);
                         }
                         else
                         {
                            outFile3.print("\t<" + roundDouble(distance, 3) + "," + aminoDistances[aminoAcidIndex][i - 3] + ">" + (roundDouble(distance, 3) - aminoDistances[aminoAcidIndex][i - 3]));
                         }

                         if (aaIn.hasAtom(sideChainAtomList[i - 2]))
                         {
                            atom3 = aaIn.getAtom(sideChainAtomList[i - 2]);
                            atomA = Calc.substract(atom2, atom3);
                            atomB = Calc.substract(atom1, atom2);
                            outFile2.print("\t" + (180 - Calc.angle(atomA, atomB)));
                         }
                  }
                  catch (StructureException ex)
                  {
                         Logger.getLogger(StructureUtil.class.getName()).log(Level.SEVERE, null, ex);
                  }
             }
        }
    }


    static public AminoAcid createAminoacid(AminoAcid aaIn, Rotamer r)
    {
        double chi, delta, k;
        AminoAcid aaOut = null;
        Atom a, x1, x2, x3;
        //System.out.println("Aminoacid : " + aaIn.getPDBName()) ;
        try
        {
            String aminoAcid = aaIn.getPDBName();
            int aminoAcidIndex = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aaIn.getPDBName());
            String[] sideChainAtomList = ListUtil.SIDE_CHAIN_ATOM_LIST[aminoAcidIndex];
            aaOut = new AminoAcidImpl();
            aaOut.setPDBName(aaIn.getPDBName());
            aaOut.setPDBCode(aaIn.getPDBCode());
            
            if (aaIn.hasAtom("N"))
            {
                aaOut.addAtom(aaIn.getN());
              //  System.out.println("HAS Atom N : ");
            }

            if (aaIn.hasAtom("CA"))
            {
                aaOut.addAtom(aaIn.getCA());
                 //  System.out.println("HAS Atom CA : ");
            }

            if (aaIn.hasAtom("C"))
            {
                aaOut.addAtom(aaIn.getC());
                 //  System.out.println("HAS Atom C : ");
            }

            if (aaIn.hasAtom("O"))
            {
                aaOut.addAtom(aaIn.getO());
                 //  System.out.println("HAS Atom O : ");
            }

            if (aminoAcidIndex != 7)
            { //GLY = 7
                aaOut.addAtom(aaIn.getCB());
                  // System.out.println("HAS Atom CB : ");
            }

            for (int i = 3; i < sideChainAtomList.length; i++)
            {

           // System.out.println(sideChainAtomList[i - 3] + " HAS : " + aaIn.hasAtom(sideChainAtomList[i - 3]));// + " X : " + aa1.getAtom(sideChainAtomList1[i]).getX() + " Y : " +aa1.getAtom(sideChainAtomList1[i]).getY() + " Z : " +  aa1.getAtom(sideChainAtomList1[i]).getZ()) ;
           // System.out.println(sideChainAtomList[i - 2] + " HAS : " + aaIn.hasAtom(sideChainAtomList[i - 2]));// + " X : " + aa1.getAtom(sideChainAtomList1[i]).getX() + " Y : " +aa1.getAtom(sideChainAtomList1[i]).getY() + " Z : " +  aa1.getAtom(sideChainAtomList1[i]).getZ()) ;
           // System.out.println(sideChainAtomList[i - 1] + " HAS : " + aaIn.hasAtom(sideChainAtomList[i - 1]));// + " X : " + aa1.getAtom(sideChainAtomList1[i]).getX() + " Y : " +aa1.getAtom(sideChainAtomList1[i]).getY() + " Z : " +  aa1.getAtom(sideChainAtomList1[i]).getZ()) ;
           // System.out.println(sideChainAtomList[i] + " (HAS) : " + aaIn.hasAtom(sideChainAtomList[i]));// + " X : " + aa1.getAtom(sideChainAtomList1[i]).getX() + " Y : " +aa1.getAtom(sideChainAtomList1[i]).getY() + " Z : " +  aa1.getAtom(sideChainAtomList1[i]).getZ()) ;
                if (aaOut.hasAtom(sideChainAtomList[i - 3]) && aaOut.hasAtom(sideChainAtomList[i - 2]) && aaOut.hasAtom(sideChainAtomList[i - 1]))
                {
                   x1 = aaOut.getAtom(sideChainAtomList[i - 3]);
                   x2 = aaOut.getAtom(sideChainAtomList[i - 2]);
                   x3 = aaOut.getAtom(sideChainAtomList[i - 1]);
                   chi = r.getChiValue(i - 2);
                   delta = BondUtil.getBondAngle(aminoAcid, i - 2);
                   k = BondUtil.getBondLength(aminoAcid, i - 2);
                   a = AngleUtil.getAtom(x1, x2, x3, chi, delta, k);
                   a.setName(sideChainAtomList[i]);
                   aaOut.addAtom(a);
                   //System.out.println("NUEVO : " + aaOut.getAtom(sideChainAtomList[i]).getName() + " X : " + aaOut.getAtom(sideChainAtomList[i]).getX() + " Y : " + aaOut.getAtom(sideChainAtomList[i]).getY() + " Z : " +  aaOut.getAtom(sideChainAtomList[i]).getZ()) ;
                }
            }
            fillAminoAcid(aaOut);
        } 
        catch (StructureException ex)
        {
            //ex.printStackTrace();
        } 
        catch (PDBParseException ex)
        {
            //ex.printStackTrace();
        }
        return aaOut;
    }

    public static boolean isAngleOK(double angle1, double angle2, int cutoff)
    {   //System.out.println("angle pdb :" + angle1 + "angle pdb :" + angle2);
        double dif = Math.abs(angle1 - angle2);
        dif = Math.min(dif, 360 - dif);
        //boolean a = dif <= cutoff;
        //System.out.println("result : " + a);
        return dif <= cutoff;
        //return a;
    }

    public static void CollisionAnalysis(String Filename, Structure s)
    {
        Chain c1, c2;
        Group g1, g2;
        int aux;

        File fileOut = null;
        PrintWriter outFile = null;
        try
        {
             fileOut = new File(Filename);
             outFile = new PrintWriter(new BufferedWriter(new FileWriter(fileOut)));

             outFile.println("COLLISION ATOMS");
             outFile.println("CHAIN   RESIDUE  AA   ATOM   X   Y    Z   -  CHAIN   RESIDUE  AA   ATOM   X    Y    Z   Distance  Minimun");
             for (int chain1 = 0; s != null && s.getChains() != null && chain1 < s.getChains().size(); chain1++)
             {
                c1 = s.getChain(chain1);
                for (int residue1 = 0; c1 != null && c1.getAtomGroups() != null && residue1 < c1.getAtomLength() -1; residue1++)
                {
                    g1 = c1.getAtomGroup(residue1);
                    if (g1 instanceof AminoAcid)
                    {
                        aux = residue1;
                        for (int chain2 = chain1; s != null && s.getChains() != null && chain2 < s.getChains().size(); chain2++)
                        {
                             c2 = s.getChain(chain2);
                             for (int residue2 = aux + 1; c2 != null && c2.getAtomGroups() != null && residue2 < c2.getAtomLength(); residue2++)
                             {
                                  g2 = c2.getAtomGroup(residue2);
                                  if (g2 instanceof AminoAcid)
                                  {
                                      collision(outFile, s, chain1, residue1, chain2, residue2);
                                  }//end if
                             }//end for
                             aux = -1;
                        }//end for
                    }//end if
                }//end for
             }//end for
             outFile.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(PDBWrite.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//end funcion

    static private void collision(PrintWriter outFile, Structure s, int chain1, int residue1, int chain2, int residue2)
    {
        int aminoAcidIndex1 = -1, aminoAcidIndex2 = -1;
        String[] sideChainAtomList1, sideChainAtomList2;
        double distance, vdWradius1, vdWradius2, MIN_DISTANCE = 0.0;
        Atom atom1, atom2;
        //char oneletter_atomName1, oneletter_atomName2;
        String atomRecord1, atomRecord2;
        AminoAcid aa1, aa2;


        aa1 = (AminoAcid)s.getChain(chain1).getAtomGroup(residue1);
        aa2 = (AminoAcid)s.getChain(chain2).getAtomGroup(residue2);
        aminoAcidIndex1 = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aa1.getPDBName());
        sideChainAtomList1 = ListUtil.ATOM_LIST[aminoAcidIndex1];
        aminoAcidIndex2 = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aa2.getPDBName());
        sideChainAtomList2 = ListUtil.ATOM_LIST[aminoAcidIndex2];


            for (int i = 0; i < sideChainAtomList1.length; i++)
            {
               if (aa1 != null && aa1.hasAtom(sideChainAtomList1[i]))
               {
                /*  for (int ii = 5; ii < sideChainAtomList1.length; ii++)
                  {
                      if (i < ii && aa1 != null && aa1.hasAtom(sideChainAtomList1[ii]))
                      {
                             try
                              {
                                  atom1 = aa1.getAtom(sideChainAtomList1[i]);
                                  atom2 = aa1.getAtom(sideChainAtomList1[ii]);
                                  distance = Calc.getDistance(atom1, atom2);

                                  vdWradius1 = radius(aa1, atom1);
                                  vdWradius2 = radius(aa1, atom2);
                                  MIN_DISTANCE = vdWradius1 + vdWradius2;

                                  if (distance < MIN_DISTANCE)
                                  {
                                    //System.out.println("distance : " + distance + "  min : " + MIN_DISTANCE);
                                    atomRecord1 = chain1 + "  " + residue1 + "  " + aa1.getPDBName() + "  " +  sideChainAtomList1[i] + "  " + atom1.getX()+ "  " + atom1.getY()+ "  " + atom1.getZ();
                                    atomRecord2 = chain1 + "  " + residue1 + "  " + aa1.getPDBName() + "  " +  sideChainAtomList1[ii] + "  " + atom2.getX()+ "  " + atom2.getY()+ "  " + atom2.getZ();
                                    outFile.println(atomRecord1 + "    " + atomRecord2 + "    " + distance + "    " + MIN_DISTANCE);
                                  }
                              }
                              catch (StructureException ex)
                              {
                                  Logger.getLogger(StructureUtil.class.getName()).log(Level.SEVERE, null, ex);
                              }
                          
                      }
                  }
                  */
                  for (int j = 0; j < sideChainAtomList2.length; j++)
                  {
                      if (aa2 != null && aa2.hasAtom(sideChainAtomList2[j]))
                      {
                          if(!(!isSideChainAtom(sideChainAtomList1[i]) && !isSideChainAtom(sideChainAtomList2[j])))
                          {
                              try
                              {
                                 atom1 = aa1.getAtom(sideChainAtomList1[i]);
                                 atom2 = aa2.getAtom(sideChainAtomList2[j]);
                                 distance = Calc.getDistance(atom1, atom2);

                                 vdWradius1 = radius(aa1, atom1);
                                 vdWradius2 = radius(aa2, atom2);
                                 MIN_DISTANCE = vdWradius1 + vdWradius2;

                                 if (distance < MIN_DISTANCE)
                                 {
                                   //System.out.println("distance : " + distance + "  min : " + MIN_DISTANCE);
                                   atomRecord1 = chain1 + "  " + residue1 + "  " + aa1.getPDBName() + "  " +  sideChainAtomList1[i] + "  " + atom1.getX()+ "  " + atom1.getY()+ "  " + atom1.getZ();
                                   atomRecord2 = chain2 + "  " + residue2 + "  " + aa2.getPDBName() + "  " +  sideChainAtomList2[j] + "  " + atom2.getX()+ "  " + atom2.getY()+ "  " + atom2.getZ();
                                   outFile.println(atomRecord1 + "    " + atomRecord2 + "    " + distance + "    " + MIN_DISTANCE);
                                 }
                              }
                              catch (StructureException ex)
                              {
                                  Logger.getLogger(StructureUtil.class.getName()).log(Level.SEVERE, null, ex);
                              }
                          }
                      }
                  }
               }
            }

    }

    public static int NumberOfCollision(Structure s)
    {
        Chain c1, c2;
        Group g1, g2;
        int aux;
        int counter = 0;
        AminoAcid aa1, aa2;
        //File fileOut = null;
        //PrintWriter outFile = null;
        try
        {
             //fileOut = new File(Filename);
             //outFile = new PrintWriter(new BufferedWriter(new FileWriter(fileOut)));

             //outFile.println("COLLISION ATOMS");
             //outFile.println("CHAIN   RESIDUE  AA   ATOM   X   Y    Z   -  CHAIN   RESIDUE  AA   ATOM   X    Y    Z");
             for (int chain1 = 0; s != null && s.getChains() != null && chain1 < s.getChains().size(); chain1++)
             {
                c1 = s.getChain(chain1);
                for (int residue1 = 0; c1 != null && c1.getAtomGroups() != null && residue1 < c1.getAtomLength() - 1; residue1++)
                {
                    g1 = c1.getAtomGroup(residue1);
                    if (g1 instanceof AminoAcid)
                    {
                        aa1 = (AminoAcid) g1;
                        aux = residue1;
                        for (int chain2 = chain1; s != null && s.getChains() != null && chain2 < s.getChains().size(); chain2++)
                        {
                             c2 = s.getChain(chain2);
                             for (int residue2 = aux + 1; c2 != null && c2.getAtomGroups() != null && residue2 < c2.getAtomLength(); residue2++)
                             {
                                  g2 = c2.getAtomGroup(residue2);
                                  if (g2 instanceof AminoAcid)
                                  {
                                      aa2 = (AminoAcid) g2;
                                      //collision(outFile, s, chain1, residue1, chain2, residue2);
                                      counter += collisionNumber(aa1, aa2);
                                  }//end if
                             }//end for
                             aux = -1;
                        }//end for
                    }//end if
                }//end for
             }//end for
             //outFile.close();
        }
        catch (Exception ex)
        {
            Logger.getLogger(PDBWrite.class.getName()).log(Level.SEVERE, null, ex);
        }
        return counter;
    }//end funcion


    private static double radius(AminoAcid aa, Atom atom)
    {
                               int atomLenght;
                               double vdWradius = 0.0;
                               char firstCaracter;

                               atomLenght = atom.getName().length();
                               firstCaracter = atom.getName().charAt(0);

                               if (atomLenght == 1)
                               {
                                   if(atom.getName().equalsIgnoreCase("N") || atom.getName().equalsIgnoreCase("O"))
                                   {
                                        vdWradius = 1.1;
                                   }
                                   else if (atom.getName().equalsIgnoreCase("C"))
                                   {
                                        vdWradius = 1.3;
                                   }
                               }
                               else if(atomLenght == 2)
                               {
                                   if(atom.getName().equalsIgnoreCase("CA"))
                                   {
                                       vdWradius = 1.3;
                                   }
                                   else if(aa.getPDBName().equalsIgnoreCase("LYS"))
                                   {
                                       if(atom.getName().equalsIgnoreCase("CE"))
                                       {
                                          vdWradius = 0.8;
                                       }
                                       else if(atom.getName().equalsIgnoreCase("NZ"))
                                       {
                                          vdWradius = 0.4;
                                       }
                                   }
                                   else if(aa.getPDBName().equalsIgnoreCase("MET"))
                                   {
                                       if(atom.getName().equalsIgnoreCase("CE"))
                                       {
                                          vdWradius = 0.8;
                                       }
                                   }
                                   else if(aa.getPDBName().equalsIgnoreCase("ARG"))
                                   {
                                       if(atom.getName().equalsIgnoreCase("NE"))
                                       {
                                          vdWradius = 0.7;
                                       }
                                       else if(atom.getName().equalsIgnoreCase("CZ"))
                                       {
                                          vdWradius = 0.8;
                                       }
                                   }
                               }
                               else if(atomLenght == 3)
                               {
                                   if(aa.getPDBName().equalsIgnoreCase("ASP") || aa.getPDBName().equalsIgnoreCase("ASN"))
                                   {
                                       if(atom.getName().equalsIgnoreCase("OD1") || atom.getName().equalsIgnoreCase("OD2")|| atom.getName().equalsIgnoreCase("ND2"))
                                       {
                                          vdWradius = 0.7;
                                       }
                                   }
                                   else if(aa.getPDBName().equalsIgnoreCase("GLU") || aa.getPDBName().equalsIgnoreCase("GLN"))
                                   {
                                       if(atom.getName().equalsIgnoreCase("OE1") || atom.getName().equalsIgnoreCase("OE2")|| atom.getName().equalsIgnoreCase("NE2"))
                                       {
                                          vdWradius = 0.7;
                                       }
                                   }
                                   else if(aa.getPDBName().equalsIgnoreCase("ARG"))
                                   {
                                       if(atom.getName().equalsIgnoreCase("NH1") || atom.getName().equalsIgnoreCase("NH2"))
                                       {
                                          vdWradius = 0.4;
                                       }
                                   }
                               }

                               if (vdWradius > 0.0)
                               {//System.out.println("Llego))))))))))))))))))))))))))))))))");
                                   return vdWradius;
                               }

                               if(firstCaracter == 'C' || firstCaracter == 'S')
                               {
                                   vdWradius = 1.3;
                               }
                               else if(firstCaracter == 'O' || firstCaracter == 'N')
                               {
                                   vdWradius = 1.1;
                               }
                               //System.out.println("Llego))))))))))))))))))))))))))))))))");
                               return vdWradius;
    }


    public static double getvdWPotential(Structure s)
    {
        Chain c1, c2;
        Group g1, g2;
        int aux;
        double sum = 0.0;

             for (int chain1 = 0; s != null && s.getChains() != null && chain1 < s.getChains().size(); chain1++)
             {
                c1 = s.getChain(chain1);
                for (int residue1 = 0; c1 != null && c1.getAtomGroups() != null && residue1 < c1.getAtomLength() - 1; residue1++)
                {
                    g1 = c1.getAtomGroup(residue1);
                    if (g1 instanceof AminoAcid)
                    {
                        aux = residue1;
                        for (int chain2 = chain1; s != null && s.getChains() != null && chain2 < s.getChains().size(); chain2++)
                        {
                             c2 = s.getChain(chain2);
                             for (int residue2 = aux + 1; c2 != null && c2.getAtomGroups() != null && residue2 < c2.getAtomLength(); residue2++)
                             {
                                  g2 = c2.getAtomGroup(residue2);
                                  if (g2 instanceof AminoAcid)
                                  {
                                     //System.out.println("hola 1");
                                     sum += vdWPotencial(s, chain1, residue1, chain2, residue2);
                                     //System.out.println("(hola 2) " + " - " + chain1+ " - " +  residue1+ " - " +  chain2+ " - " +  residue2);
                                  }//end if
                             }//end for
                             aux = -1;
                        }//end for
                    }//end if
                }//end for
             }//end for

             return sum;
    }//end funcion

    public static double vdWPotencial(Structure s, int chain1, int residue1, int chain2, int residue2)
    {
        int aminoAcidIndex1 = -1, aminoAcidIndex2 = -1;
        String[] sideChainAtomList1, sideChainAtomList2;
        double distance, vdWradius1, vdWradius2, MIN_DISTANCE, sum = 0.0, krep = 5.882, katt = 0.08;
        Atom atom1, atom2;
        AminoAcid aa1, aa2;


        aa1 = (AminoAcid)s.getChain(chain1).getAtomGroup(residue1);
        aa2 = (AminoAcid)s.getChain(chain2).getAtomGroup(residue2);
        aminoAcidIndex1 = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aa1.getPDBName());
        sideChainAtomList1 = ListUtil.ATOM_LIST[aminoAcidIndex1];
        aminoAcidIndex2 = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aa2.getPDBName());
        sideChainAtomList2 = ListUtil.ATOM_LIST[aminoAcidIndex2];
//System.out.println("1 " + aa1.getPDBName() + " - " + "2 " + aa2.getPDBName());

            for (int i = 0; i < sideChainAtomList1.length; i++)
            {  /* try
                {
                    System.out.println("1 " + aa1.getPDBName() + " - " + sideChainAtomList1[i] + " HAS : " + aa1.hasAtom(sideChainAtomList1[i]) + " X : " + aa1.getAtom(sideChainAtomList1[i]).getX() + " Y : " +aa1.getAtom(sideChainAtomList1[i]).getY() + " Z : " +  aa1.getAtom(sideChainAtomList1[i]).getZ()) ;
                }
                catch (Exception ex)
                {
                    Logger.getLogger(StructureUtil.class.getName()).log(Level.SEVERE, null, ex);
                }*/
               if (aa1 != null && aa1.hasAtom(sideChainAtomList1[i]))// && !isSideChainAtom(sideChainAtomList1[i]))
               {  //System.out.println("1 entro");
                  /*for (int ii = 5; ii < sideChainAtomList1.length; ii++)
                  {
                      if (i < ii && aa1 != null && aa1.hasAtom(sideChainAtomList1[ii]))
                      {
                             try
                              {
                                  atom1 = aa1.getAtom(sideChainAtomList1[i]);
                                  atom2 = aa1.getAtom(sideChainAtomList1[ii]);
                                  distance = Calc.getDistance(atom1, atom2);

                                  vdWradius1 = radius(aa1, atom1);
                                  vdWradius2 = radius(aa1, atom2);
                                  MIN_DISTANCE = vdWradius1 + vdWradius2;
                                  //System.out.println("Distance : " + distance + "  MIN : " + MIN_DISTANCE + " R1 " +  vdWradius1 + " R2 " +  vdWradius2);

                                  if (distance <= MIN_DISTANCE)
                                  {
                                     sum += krep * (1 - (distance/MIN_DISTANCE));
                                   //System.out.println("Errorrrrrrrrrrrrrrrrrrrr       1 " + "suma : " + sum);
                                  }
                                  else if ((distance > MIN_DISTANCE) && (distance < (2 * MIN_DISTANCE)))
                                  {
                                     sum += katt * (Math.pow(distance/MIN_DISTANCE, 2) - 3 * (distance/MIN_DISTANCE) + 2);
                                     //  System.out.println("Errorrrrrrrrrrrrrrrrrrrr       2 " + "suma : " + sum);
                                  }
                                  else if (distance >= 2 * MIN_DISTANCE)
                                  {
                                     sum += 0;
                                     //    System.out.println("Errorrrrrrrrrrrrrrrrrrrr       3 " + "suma : " + sum);
                                  }
                                  else
                                  {System.out.println("Errorrrrrrrrrrrrrrrrrrrr          4 " );
                                  }
                              }
                              catch (StructureException ex)
                              {
                                  Logger.getLogger(StructureUtil.class.getName()).log(Level.SEVERE, null, ex);
                              }

                      }
                  }
                    */
                  for (int j = 0; j < sideChainAtomList2.length; j++)
                  {   //System.out.println("2 " + aa2.getPDBName() + " - " + sideChainAtomList2[j] + " HAS : " + (aa2 != null && aa2.hasAtom(sideChainAtomList2[j])));
                      if (aa2 != null && aa2.hasAtom(sideChainAtomList2[j]))// && !isSideChainAtom(sideChainAtomList2[j]))
                      {    //System.out.println("2 entro");
                           if(!(!isSideChainAtom(sideChainAtomList1[i]) && !isSideChainAtom(sideChainAtomList2[j])))
                           {
                               try
                               {
                                  atom1 = aa1.getAtom(sideChainAtomList1[i]);
                                  atom2 = aa2.getAtom(sideChainAtomList2[j]);
                                  distance = Calc.getDistance(atom1, atom2);

                                  vdWradius1 = radius(aa1, atom1);
                                  vdWradius2 = radius(aa2, atom2);
                                  MIN_DISTANCE = vdWradius1 + vdWradius2;
                                  //System.out.println("Distance : " + distance + "  MIN : " + MIN_DISTANCE + " R1 " +  vdWradius1 + " R2 " +  vdWradius2);
                               
                                  if (distance <= MIN_DISTANCE)
                                  {
                                     sum += krep * (1 - (distance/MIN_DISTANCE));
                                   //System.out.println("Errorrrrrrrrrrrrrrrrrrrr       1 " + "suma : " + sum);
                                  }
                                  else if ((distance > MIN_DISTANCE) && (distance < (2 * MIN_DISTANCE)))
                                  {
                                     sum += katt * (Math.pow(distance/MIN_DISTANCE, 2) - 3 * (distance/MIN_DISTANCE) + 2);
                                     //  System.out.println("Errorrrrrrrrrrrrrrrrrrrr       2 " + "suma : " + sum);
                                  }
                                  else if (distance >= 2 * MIN_DISTANCE)
                                  {
                                     sum += 0;
                                     //    System.out.println("Errorrrrrrrrrrrrrrrrrrrr       3 " + "suma : " + sum);
                                  }
                                  else
                                  {System.out.println("Errorrrrrrrrrrrrrrrrrrrr          4 " );
                                  }
                               
                                  // ---------------------------------Second option-------------------------------------------------------
                                  /*
                                  if (distance >= MIN_DISTANCE)
                                  {
                                      sum += 0;
                                      //System.out.println("Errorrrrrrrrrrrrrrrrrrrr       1 " + "suma : " + sum);
                                  }
                                  else if (distance <= (0.8254 * MIN_DISTANCE))
                                  {
                                      sum += 10;
                                      //  System.out.println("Errorrrrrrrrrrrrrrrrrrrr       2 " + "suma : " + sum);
                                  }
                                  else
                                  {
                                      sum += 57.273 * (1 - (distance/MIN_DISTANCE));
                                      //    System.out.println("Errorrrrrrrrrrrrrrrrrrrr       3 " + "suma : " + sum);
                                  }
                                  */
                               }
                               catch (StructureException ex)
                               {
                                  Logger.getLogger(StructureUtil.class.getName()).log(Level.SEVERE, null, ex);
                               }
                           }
                      }
                  }
               }
            }
            //System.out.println("Termino : " + sum);
            return sum;
    }

    public static void getvdWPotentialAndCRatio(Structure s, Solution sol)
    {
        Chain c1, c2;
        Group g1, g2;
        int aux, Counter = 0;
        double sumvdW = 0.0, sumCRatio = 0.0;
        double [] temp = new double[2];
        temp[0] = 0.0;
        temp[1] = 0.0;

             for (int chain1 = 0; s != null && s.getChains() != null && chain1 < s.getChains().size(); chain1++)
             {
                c1 = s.getChain(chain1);
                for (int residue1 = 0; c1 != null && c1.getAtomGroups() != null && residue1 < c1.getAtomLength() - 1; residue1++)
                {
                    g1 = c1.getAtomGroup(residue1);
                    if (g1 instanceof AminoAcid)
                    {
                        aux = residue1;
                        for (int chain2 = chain1; s != null && s.getChains() != null && chain2 < s.getChains().size(); chain2++)
                        {
                             c2 = s.getChain(chain2);
                             for (int residue2 = aux + 1; c2 != null && c2.getAtomGroups() != null && residue2 < c2.getAtomLength(); residue2++)
                             {
                                  g2 = c2.getAtomGroup(residue2);
                                  if (g2 instanceof AminoAcid)
                                  {
                                     //System.out.println("hola 1");
                                     temp[0] = 0.0;
                                     temp[1] = 0.0;
                                     vdWPotencialAndCRatio(s, chain1, residue1, chain2, residue2, temp);
                                     //System.out.println("Despues(vdw) " + " : " + temp[0] + " cratio : " +  temp[1]);
                                     sumvdW += temp[0];
                                     sumCRatio += temp[1];
                                     Counter++;
                                     //System.out.println("Despues(vdw) " + " : " + sumvdW + " cratio : " +  sumCRatio);
                                     //System.out.println("Antes(sumvdW) " + " : " + temp[0] + " cratio : " + temp[1]);
                                  }//end if
                             }//end for
                             aux = -1;
                        }//end for
                    }//end if
                }//end for
             }//end for
             sol.vdWPotential = sumvdW;
             sol.cRatio = (double)sumCRatio/Counter;
             //System.out.println("Total(cratio) " + " : " + sumCRatio + " counter : " +  Counter);
             //return sum;
    }//end funcion

    public static void vdWPotencialAndCRatio(Structure s, int chain1, int residue1, int chain2, int residue2, double [] temp)
    {
        int aminoAcidIndex1 = -1, aminoAcidIndex2 = -1, collisionCounter = 0, collisionTotal = 0;
        String[] sideChainAtomList1, sideChainAtomList2;
        double distance, vdWradius1, vdWradius2, MIN_DISTANCE, sum = 0.0, krep = 5.882, katt = 0.08;
        Atom atom1, atom2;
        AminoAcid aa1, aa2;


        aa1 = (AminoAcid)s.getChain(chain1).getAtomGroup(residue1);
        aa2 = (AminoAcid)s.getChain(chain2).getAtomGroup(residue2);
        aminoAcidIndex1 = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aa1.getPDBName());
        sideChainAtomList1 = ListUtil.ATOM_LIST[aminoAcidIndex1];
        aminoAcidIndex2 = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aa2.getPDBName());
        sideChainAtomList2 = ListUtil.ATOM_LIST[aminoAcidIndex2];
//System.out.println("1 " + aa1.getPDBName() + " - " + "2 " + aa2.getPDBName());

            for (int i = 0; i < sideChainAtomList1.length; i++)
            {/*   try
                {
                    System.out.println("1 " + aa1.getPDBName() + " - " + sideChainAtomList1[i] + " HAS : " + aa1.hasAtom(sideChainAtomList1[i]) + " X : " + aa1.getAtom(sideChainAtomList1[i]).getX() + " Y : " +aa1.getAtom(sideChainAtomList1[i]).getY() + " Z : " +  aa1.getAtom(sideChainAtomList1[i]).getZ()) ;
                }
                catch (Exception ex)
                {
                    Logger.getLogger(StructureUtil.class.getName()).log(Level.SEVERE, null, ex);
                } */
               if (aa1 != null && aa1.hasAtom(sideChainAtomList1[i]))// && !isSideChainAtom(sideChainAtomList1[i]))
               {

                  /*for (int ii = 5; ii < sideChainAtomList1.length; ii++)
                  {
                      if (i < ii && aa1 != null && aa1.hasAtom(sideChainAtomList1[ii]))
                      {
                             try
                              {
                                  atom1 = aa1.getAtom(sideChainAtomList1[i]);
                                  atom2 = aa1.getAtom(sideChainAtomList1[ii]);
                                  distance = Calc.getDistance(atom1, atom2);

                                  vdWradius1 = radius(aa1, atom1);
                                  vdWradius2 = radius(aa1, atom2);
                                  MIN_DISTANCE = vdWradius1 + vdWradius2;
                                  //System.out.println("Distance : " + distance + "  MIN : " + MIN_DISTANCE + " R1 " +  vdWradius1 + " R2 " +  vdWradius2);
                                  collisionTotal++;
                                  if (distance <= MIN_DISTANCE)
                                  {
                                     sum += krep * (1 - (distance/MIN_DISTANCE));
                                     collisionCounter++;
                                   //System.out.println("Errorrrrrrrrrrrrrrrrrrrr       1 " + "suma : " + sum);
                                  }
                                  else if ((distance > MIN_DISTANCE) && (distance < (2 * MIN_DISTANCE)))
                                  {
                                     sum += katt * (Math.pow(distance/MIN_DISTANCE, 2) - 3 * (distance/MIN_DISTANCE) + 2);
                                     //  System.out.println("Errorrrrrrrrrrrrrrrrrrrr       2 " + "suma : " + sum);
                                  }
                                  else if (distance >= 2 * MIN_DISTANCE)
                                  {
                                     sum += 0;
                                     //    System.out.println("Errorrrrrrrrrrrrrrrrrrrr       3 " + "suma : " + sum);
                                  }
                                  else
                                  {System.out.println("Errorrrrrrrrrrrrrrrrrrrr          4 " );
                                  }
                              }
                              catch (StructureException ex)
                              {
                                  Logger.getLogger(StructureUtil.class.getName()).log(Level.SEVERE, null, ex);
                              }

                      }
                  }
                    */
                  //System.out.println("1 entro");
                  for (int j = 0; j < sideChainAtomList2.length; j++)
                  { //  System.out.println("2 " + aa2.getPDBName() + " - " + sideChainAtomList2[j] + " HAS : " + (aa2 != null && aa2.hasAtom(sideChainAtomList2[j])));
                      if (aa2 != null && aa2.hasAtom(sideChainAtomList2[j]))// && !isSideChainAtom(sideChainAtomList2[j]))
                      {    //System.out.println("2 entro");
                           //System.out.println("* 1 " + aa1.getPDBName() + " - " + sideChainAtomList1[i] + " * 2 " + aa2.getPDBName() + " - " + sideChainAtomList2[j]);
                           if(!(!isSideChainAtom(sideChainAtomList1[i]) && !isSideChainAtom(sideChainAtomList2[j])))
                           {//System.out.println("* 1 " + aa1.getPDBName() + " - " + sideChainAtomList1[i] + " * 2 " + aa2.getPDBName() + " - " + sideChainAtomList2[j]);
                               //System.out.println("Entro");
                               try
                               {
                                  atom1 = aa1.getAtom(sideChainAtomList1[i]);
                                  atom2 = aa2.getAtom(sideChainAtomList2[j]);
                                  distance = Calc.getDistance(atom1, atom2);

                                  vdWradius1 = radius(aa1, atom1);
                                  vdWradius2 = radius(aa2, atom2);
                                  MIN_DISTANCE = vdWradius1 + vdWradius2;
                                  //System.out.println("Distance : " + distance + "  MIN : " + MIN_DISTANCE + " R1 " +  vdWradius1 + " R2 " +  vdWradius2);

                                  collisionTotal++;
                                  if (distance <= MIN_DISTANCE)
                                  {
                                     sum += krep * (1 - (distance/MIN_DISTANCE));
                                     collisionCounter++;
                                     //System.out.println("Errorrrrrrrrrrrrrrrrrrrr       1 " + "suma : " + sum);
                                  }
                                  else if ((distance > MIN_DISTANCE) && (distance < (2 * MIN_DISTANCE)))
                                  {
                                     sum += katt * (Math.pow(distance/MIN_DISTANCE, 2) - 3 * (distance/MIN_DISTANCE) + 2);
                                     //System.out.println("Errorrrrrrrrrrrrrrrrrrrr       2 " + "suma : " + sum);
                                  }
                                  else if (distance >= 2 * MIN_DISTANCE)
                                  {
                                     sum += 0;
                                     //System.out.println("Errorrrrrrrrrrrrrrrrrrrr       3 " + "suma : " + sum);
                                  }
                                  else
                                  {System.out.println("Errorrrrrrrrrrrrrrrrrrrr          4 " );
                                  }

                                  // ---------------------------------Second option-------------------------------------------------------
                                  /*
                                  if (distance >= MIN_DISTANCE)
                                  {
                                      sum += 0;
                                      //System.out.println("Errorrrrrrrrrrrrrrrrrrrr       1 " + "suma : " + sum);
                                  }
                                  else if (distance <= (0.8254 * MIN_DISTANCE))
                                  {
                                      sum += 10;
                                      //  System.out.println("Errorrrrrrrrrrrrrrrrrrrr       2 " + "suma : " + sum);
                                  }
                                  else
                                  {
                                      sum += 57.273 * (1 - (distance/MIN_DISTANCE));
                                      //    System.out.println("Errorrrrrrrrrrrrrrrrrrrr       3 " + "suma : " + sum);
                                  }
                                  */
                               }
                               catch (StructureException ex)
                               {
                                  Logger.getLogger(StructureUtil.class.getName()).log(Level.SEVERE, null, ex);
                               }
                           }
                      }
                  }
               }
            }
            //System.out.println("Termino : " + sum);
            //return sum;
            temp[0] = sum;
            if (collisionTotal == 0)
            {
                temp[1] = 0;
            }
            else
            {
                temp[1] = (double)collisionCounter/collisionTotal;
            }
            //System.out.println("(collision) " + " : " + collisionCounter + " total : " +  collisionTotal);
            //System.out.println("Antes(sumvdW) " + " : " + temp[0] + " cratio : " + temp[1]);
    }

    public static double getCRatio(Structure s)
    {
        Chain c1, c2;
        Group g1, g2;
        int aux, Counter = 0;
        double /*sumvdW = 0.0,*/ sumCRatio = 0.0;
        double [] temp = new double[1];
        temp[0] = 0.0;
        //temp[1] = 0.0;

             for (int chain1 = 0; s != null && s.getChains() != null && chain1 < s.getChains().size(); chain1++)
             {
                c1 = s.getChain(chain1);
                for (int residue1 = 0; c1 != null && c1.getAtomGroups() != null && residue1 < c1.getAtomLength() - 1; residue1++)
                {
                    g1 = c1.getAtomGroup(residue1);
                    if (g1 instanceof AminoAcid)
                    {
                        aux = residue1;
                        for (int chain2 = chain1; s != null && s.getChains() != null && chain2 < s.getChains().size(); chain2++)
                        {
                             c2 = s.getChain(chain2);
                             for (int residue2 = aux + 1; c2 != null && c2.getAtomGroups() != null && residue2 < c2.getAtomLength(); residue2++)
                             {
                                  g2 = c2.getAtomGroup(residue2);
                                  if (g2 instanceof AminoAcid)
                                  {
                                     //System.out.println("hola 1");
                                     temp[0] = 0.0;
                                     //temp[1] = 0.0;
                                     CRatio(s, chain1, residue1, chain2, residue2, temp);
                                     //System.out.println("Despues(vdw) " + " : " + temp[0] + " cratio : " +  temp[1]);
                                     //sumvdW += temp[0];
                                     //sumCRatio += temp[1];
                                     sumCRatio += temp[0];
                                     Counter++;
                                     //System.out.println("Despues(vdw) " + " : " + sumvdW + " cratio : " +  sumCRatio);
                                     //System.out.println("Antes(sumvdW) " + " : " + temp[0] + " cratio : " + temp[1]);
                                  }//end if
                             }//end for
                             aux = -1;
                        }//end for
                    }//end if
                }//end for
             }//end for
             //sol.vdWPotential = sumvdW;
             //sol.cRatio = (double)sumCRatio/Counter;
             return (double)sumCRatio/Counter;
             //System.out.println("Total(cratio) " + " : " + sumCRatio + " counter : " +  Counter);
             //return sum;
    }//end funcion

    public static void CRatio(Structure s, int chain1, int residue1, int chain2, int residue2, double [] temp)
    {
        int aminoAcidIndex1 = -1, aminoAcidIndex2 = -1, collisionCounter = 0, collisionTotal = 0;
        String[] sideChainAtomList1, sideChainAtomList2;
        double distance, vdWradius1, vdWradius2, MIN_DISTANCE, sum = 0.0, krep = 5.882, katt = 0.08;
        Atom atom1, atom2;
        AminoAcid aa1, aa2;


        aa1 = (AminoAcid)s.getChain(chain1).getAtomGroup(residue1);
        aa2 = (AminoAcid)s.getChain(chain2).getAtomGroup(residue2);
        aminoAcidIndex1 = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aa1.getPDBName());
        sideChainAtomList1 = ListUtil.ATOM_LIST[aminoAcidIndex1];
        aminoAcidIndex2 = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aa2.getPDBName());
        sideChainAtomList2 = ListUtil.ATOM_LIST[aminoAcidIndex2];
//System.out.println("1 " + aa1.getPDBName() + " - " + "2 " + aa2.getPDBName());

            for (int i = 0; i < sideChainAtomList1.length; i++)
            {/*   try
                {
                    System.out.println("1 " + aa1.getPDBName() + " - " + sideChainAtomList1[i] + " HAS : " + aa1.hasAtom(sideChainAtomList1[i]) + " X : " + aa1.getAtom(sideChainAtomList1[i]).getX() + " Y : " +aa1.getAtom(sideChainAtomList1[i]).getY() + " Z : " +  aa1.getAtom(sideChainAtomList1[i]).getZ()) ;
                }
                catch (Exception ex)
                {
                    Logger.getLogger(StructureUtil.class.getName()).log(Level.SEVERE, null, ex);
                } */
               if (aa1 != null && aa1.hasAtom(sideChainAtomList1[i]))// && !isSideChainAtom(sideChainAtomList1[i]))
               {
                 /* for (int ii = 5; ii < sideChainAtomList1.length; ii++)
                  {
                      if (i < ii && aa1 != null && aa1.hasAtom(sideChainAtomList1[ii]))
                      {
                             try
                              {
                                  atom1 = aa1.getAtom(sideChainAtomList1[i]);
                                  atom2 = aa1.getAtom(sideChainAtomList1[ii]);
                                  distance = Calc.getDistance(atom1, atom2);

                                  vdWradius1 = radius(aa1, atom1);
                                  vdWradius2 = radius(aa1, atom2);
                                  MIN_DISTANCE = vdWradius1 + vdWradius2;
                                  //System.out.println("Distance : " + distance + "  MIN : " + MIN_DISTANCE + " R1 " +  vdWradius1 + " R2 " +  vdWradius2);
                                  collisionTotal++;
                                  if (distance <= MIN_DISTANCE)
                                  {
                                     collisionCounter++;
                                   //System.out.println("Errorrrrrrrrrrrrrrrrrrrr       1 " + "suma : " + sum);
                                  }

                              }
                              catch (StructureException ex)
                              {
                                  Logger.getLogger(StructureUtil.class.getName()).log(Level.SEVERE, null, ex);
                              }
                      }
                  }
                  */
                  //System.out.println("1 entro");
                  for (int j = 0; j < sideChainAtomList2.length; j++)
                  { //  System.out.println("2 " + aa2.getPDBName() + " - " + sideChainAtomList2[j] + " HAS : " + (aa2 != null && aa2.hasAtom(sideChainAtomList2[j])));
                      if (aa2 != null && aa2.hasAtom(sideChainAtomList2[j]))// && !isSideChainAtom(sideChainAtomList2[j]))
                      {    //System.out.println("2 entro");
                           //System.out.println("* 1 " + aa1.getPDBName() + " - " + sideChainAtomList1[i] + " * 2 " + aa2.getPDBName() + " - " + sideChainAtomList2[j]);
                           if(!(!isSideChainAtom(sideChainAtomList1[i]) && !isSideChainAtom(sideChainAtomList2[j])))
                           {//System.out.println("* 1 " + aa1.getPDBName() + " - " + sideChainAtomList1[i] + " * 2 " + aa2.getPDBName() + " - " + sideChainAtomList2[j]);
                               //System.out.println("Entro");
                               try
                               {
                                  atom1 = aa1.getAtom(sideChainAtomList1[i]);
                                  atom2 = aa2.getAtom(sideChainAtomList2[j]);
                                  distance = Calc.getDistance(atom1, atom2);

                                  vdWradius1 = radius(aa1, atom1);
                                  vdWradius2 = radius(aa2, atom2);
                                  MIN_DISTANCE = vdWradius1 + vdWradius2;
                                  //System.out.println("Distance : " + distance + "  MIN : " + MIN_DISTANCE + " R1 " +  vdWradius1 + " R2 " +  vdWradius2);

                                  collisionTotal++;
                                  if (distance <= MIN_DISTANCE)
                                  {
                                     //sum += krep * (1 - (distance/MIN_DISTANCE));
                                     collisionCounter++;
                                     //System.out.println("Errorrrrrrrrrrrrrrrrrrrr       1 " + "suma : " + sum);
                                  }
                                /*  else if ((distance > MIN_DISTANCE) && (distance < (2 * MIN_DISTANCE)))
                                  {
                                     sum += katt * (Math.pow(distance/MIN_DISTANCE, 2) - 3 * (distance/MIN_DISTANCE) + 2);
                                     //System.out.println("Errorrrrrrrrrrrrrrrrrrrr       2 " + "suma : " + sum);
                                  }
                                  else if (distance >= 2 * MIN_DISTANCE)
                                  {
                                     sum += 0;
                                     //System.out.println("Errorrrrrrrrrrrrrrrrrrrr       3 " + "suma : " + sum);
                                  }
                                  else
                                  {System.out.println("Errorrrrrrrrrrrrrrrrrrrr          4 " );
                                  }
                                  */
                                  // ---------------------------------Second option-------------------------------------------------------
                                  /*
                                  if (distance >= MIN_DISTANCE)
                                  {
                                      sum += 0;
                                      //System.out.println("Errorrrrrrrrrrrrrrrrrrrr       1 " + "suma : " + sum);
                                  }
                                  else if (distance <= (0.8254 * MIN_DISTANCE))
                                  {
                                      sum += 10;
                                      //  System.out.println("Errorrrrrrrrrrrrrrrrrrrr       2 " + "suma : " + sum);
                                  }
                                  else
                                  {
                                      sum += 57.273 * (1 - (distance/MIN_DISTANCE));
                                      //    System.out.println("Errorrrrrrrrrrrrrrrrrrrr       3 " + "suma : " + sum);
                                  }
                                  */
                               }
                               catch (StructureException ex)
                               {
                                  Logger.getLogger(StructureUtil.class.getName()).log(Level.SEVERE, null, ex);
                               }
                           }
                      }
                  }
               }
            }
            //System.out.println("Termino : " + sum);
            //return sum;
            //temp[0] = sum;
            /*
            if (collisionTotal == 0)
            {
                temp[1] = 0;
            }
            else
            {
                temp[1] = (double)collisionCounter/collisionTotal;
            }
            */
            if (collisionTotal == 0)
            {
                temp[0] = 0;
            }
            else
            {
                temp[0] = (double)collisionCounter/collisionTotal;
            }
            //System.out.println("(collision) " + " : " + collisionCounter + " total : " +  collisionTotal);
            //System.out.println("Antes(sumvdW) " + " : " + temp[0] + " cratio : " + temp[1]);
    }

    public static boolean isThereCollision(AminoAcid aa1, AminoAcid aa2)
    {
        int aminoAcidIndex1 = -1, aminoAcidIndex2 = -1;//, collisionCounter = 0, collisionTotal = 0;
        String[] sideChainAtomList1, sideChainAtomList2;
        double distance, vdWradius1, vdWradius2, MIN_DISTANCE;//, sum = 0.0, krep = 5.882, katt = 0.08;
        Atom atom1, atom2;
        //AminoAcid aa1, aa2;
        //boolean thereIsCollision = false;

        //aa1 = (AminoAcid)s.getChain(chain1).getAtomGroup(residue1);
        //aa2 = (AminoAcid)s.getChain(chain2).getAtomGroup(residue2);
        aminoAcidIndex1 = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aa1.getPDBName());
        sideChainAtomList1 = ListUtil.ATOM_LIST[aminoAcidIndex1];
        aminoAcidIndex2 = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aa2.getPDBName());
        sideChainAtomList2 = ListUtil.ATOM_LIST[aminoAcidIndex2];
//System.out.println("1 " + aa1.getPDBName() + " - " + "2 " + aa2.getPDBName());

            for (int i = 0; i < sideChainAtomList1.length; i++)
            {/*   try
                {
                    System.out.println("1 " + aa1.getPDBName() + " - " + sideChainAtomList1[i] + " HAS : " + aa1.hasAtom(sideChainAtomList1[i]) + " X : " + aa1.getAtom(sideChainAtomList1[i]).getX() + " Y : " +aa1.getAtom(sideChainAtomList1[i]).getY() + " Z : " +  aa1.getAtom(sideChainAtomList1[i]).getZ()) ;
                }
                catch (Exception ex)
                {
                    Logger.getLogger(StructureUtil.class.getName()).log(Level.SEVERE, null, ex);
                } */
               if (aa1 != null && aa1.hasAtom(sideChainAtomList1[i]))// && !isSideChainAtom(sideChainAtomList1[i]))
               {  //System.out.println("1 entro");
                  for (int j = 0; j < sideChainAtomList2.length; j++)
                  { //  System.out.println("2 " + aa2.getPDBName() + " - " + sideChainAtomList2[j] + " HAS : " + (aa2 != null && aa2.hasAtom(sideChainAtomList2[j])));
                      if (aa2 != null && aa2.hasAtom(sideChainAtomList2[j]))// && !isSideChainAtom(sideChainAtomList2[j]))
                      {    //System.out.println("2 entro");
                           //System.out.println("* 1 " + aa1.getPDBName() + " - " + sideChainAtomList1[i] + " * 2 " + aa2.getPDBName() + " - " + sideChainAtomList2[j]);
                           if(!(!isSideChainAtom(sideChainAtomList1[i]) && !isSideChainAtom(sideChainAtomList2[j])))
                           {//System.out.println("* 1 " + aa1.getPDBName() + " - " + sideChainAtomList1[i] + " * 2 " + aa2.getPDBName() + " - " + sideChainAtomList2[j]);
                               //System.out.println("Entro");
                               try
                               {
                                  atom1 = aa1.getAtom(sideChainAtomList1[i]);
                                  atom2 = aa2.getAtom(sideChainAtomList2[j]);
                                  distance = Calc.getDistance(atom1, atom2);

                                  vdWradius1 = radius(aa1, atom1);
                                  vdWradius2 = radius(aa2, atom2);
                                  MIN_DISTANCE = vdWradius1 + vdWradius2;
                                  //System.out.println("Distance : " + distance + "  MIN : " + MIN_DISTANCE + " R1 " +  vdWradius1 + " R2 " +  vdWradius2);

                                  //collisionTotal++;
                                  if (distance <= MIN_DISTANCE)
                                  {
                                     //sum += krep * (1 - (distance/MIN_DISTANCE));
                                     //thereIsCollision = true;
                                     return true;
                                     //collisionCounter++;
                                     //System.out.println("Errorrrrrrrrrrrrrrrrrrrr       1 " + "suma : " + sum);
                                  }
                                /*  else if ((distance > MIN_DISTANCE) && (distance < (2 * MIN_DISTANCE)))
                                  {
                                     sum += katt * (Math.pow(distance/MIN_DISTANCE, 2) - 3 * (distance/MIN_DISTANCE) + 2);
                                     //System.out.println("Errorrrrrrrrrrrrrrrrrrrr       2 " + "suma : " + sum);
                                  }
                                  else if (distance >= 2 * MIN_DISTANCE)
                                  {
                                     sum += 0;
                                     //System.out.println("Errorrrrrrrrrrrrrrrrrrrr       3 " + "suma : " + sum);
                                  }
                                  else
                                  {System.out.println("Errorrrrrrrrrrrrrrrrrrrr          4 " );
                                  }
                                  */
                                  // ---------------------------------Second option-------------------------------------------------------
                                  /*
                                  if (distance >= MIN_DISTANCE)
                                  {
                                      sum += 0;
                                      //System.out.println("Errorrrrrrrrrrrrrrrrrrrr       1 " + "suma : " + sum);
                                  }
                                  else if (distance <= (0.8254 * MIN_DISTANCE))
                                  {
                                      sum += 10;
                                      //  System.out.println("Errorrrrrrrrrrrrrrrrrrrr       2 " + "suma : " + sum);
                                  }
                                  else
                                  {
                                      sum += 57.273 * (1 - (distance/MIN_DISTANCE));
                                      //    System.out.println("Errorrrrrrrrrrrrrrrrrrrr       3 " + "suma : " + sum);
                                  }
                                  */
                               }
                               catch (StructureException ex)
                               {
                                  Logger.getLogger(StructureUtil.class.getName()).log(Level.SEVERE, null, ex);
                               }
                           }
                      }
                  }
               }
            }
            //System.out.println("Termino : " + sum);
            //return sum;
            //temp[0] = sum;
            /*
            if (collisionTotal == 0)
            {
                temp[1] = 0;
            }
            else
            {
                temp[1] = (double)collisionCounter/collisionTotal;
            }
            
            if (collisionTotal == 0)
            {
                temp[0] = 0;
            }
            else
            {
                temp[0] = (double)collisionCounter/collisionTotal;
            }
            */
            //System.out.println("(collision) " + " : " + collisionCounter + " total : " +  collisionTotal);
            //System.out.println("Antes(sumvdW) " + " : " + temp[0] + " cratio : " + temp[1]);
            return false;
    }

    public static int collisionNumber(AminoAcid aa1, AminoAcid aa2)
    {
        int aminoAcidIndex1 = -1, aminoAcidIndex2 = -1, collisionCounter = 0;//, collisionTotal = 0;
        String[] sideChainAtomList1, sideChainAtomList2;
        double distance, vdWradius1, vdWradius2, MIN_DISTANCE;//, sum = 0.0, krep = 5.882, katt = 0.08;
        Atom atom1, atom2;
        //AminoAcid aa1, aa2;
        //boolean thereIsCollision = false;

        //aa1 = (AminoAcid)s.getChain(chain1).getAtomGroup(residue1);
        //aa2 = (AminoAcid)s.getChain(chain2).getAtomGroup(residue2);
        aminoAcidIndex1 = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aa1.getPDBName());
        sideChainAtomList1 = ListUtil.ATOM_LIST[aminoAcidIndex1];
        aminoAcidIndex2 = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aa2.getPDBName());
        sideChainAtomList2 = ListUtil.ATOM_LIST[aminoAcidIndex2];
//System.out.println("1 " + aa1.getPDBName() + " - " + "2 " + aa2.getPDBName());

            for (int i = 0; i < sideChainAtomList1.length; i++)
            {/*   try
                {
                    System.out.println("1 " + aa1.getPDBName() + " - " + sideChainAtomList1[i] + " HAS : " + aa1.hasAtom(sideChainAtomList1[i]) + " X : " + aa1.getAtom(sideChainAtomList1[i]).getX() + " Y : " +aa1.getAtom(sideChainAtomList1[i]).getY() + " Z : " +  aa1.getAtom(sideChainAtomList1[i]).getZ()) ;
                }
                catch (Exception ex)
                {
                    Logger.getLogger(StructureUtil.class.getName()).log(Level.SEVERE, null, ex);
                } */
               if (aa1 != null && aa1.hasAtom(sideChainAtomList1[i]))// && !isSideChainAtom(sideChainAtomList1[i]))
               {
                 /* for (int ii = 5; ii < sideChainAtomList1.length; ii++)
                  {
                      if (i < ii && aa1 != null && aa1.hasAtom(sideChainAtomList1[ii]))
                      {
                             try
                              {
                                  atom1 = aa1.getAtom(sideChainAtomList1[i]);
                                  atom2 = aa1.getAtom(sideChainAtomList1[ii]);
                                  distance = Calc.getDistance(atom1, atom2);

                                  vdWradius1 = radius(aa1, atom1);
                                  vdWradius2 = radius(aa1, atom2);
                                  MIN_DISTANCE = vdWradius1 + vdWradius2;
                                  //System.out.println("Distance : " + distance + "  MIN : " + MIN_DISTANCE + " R1 " +  vdWradius1 + " R2 " +  vdWradius2);
                                  if (distance <= MIN_DISTANCE)
                                  {
                                     collisionCounter++;
                                   //System.out.println("Errorrrrrrrrrrrrrrrrrrrr       1 " + "suma : " + sum);
                                  }
                              }
                              catch (StructureException ex)
                              {
                                  Logger.getLogger(StructureUtil.class.getName()).log(Level.SEVERE, null, ex);
                              }
                      }
                  }
                 
                  */
                   //System.out.println("1 entro");
                  for (int j = 0; j < sideChainAtomList2.length; j++)
                  { //  System.out.println("2 " + aa2.getPDBName() + " - " + sideChainAtomList2[j] + " HAS : " + (aa2 != null && aa2.hasAtom(sideChainAtomList2[j])));
                      if (aa2 != null && aa2.hasAtom(sideChainAtomList2[j]))// && !isSideChainAtom(sideChainAtomList2[j]))
                      {    //System.out.println("2 entro");
                           //System.out.println("* 1 " + aa1.getPDBName() + " - " + sideChainAtomList1[i] + " * 2 " + aa2.getPDBName() + " - " + sideChainAtomList2[j]);
                           if(!(!isSideChainAtom(sideChainAtomList1[i]) && !isSideChainAtom(sideChainAtomList2[j])))
                           {//System.out.println("* 1 " + aa1.getPDBName() + " - " + sideChainAtomList1[i] + " * 2 " + aa2.getPDBName() + " - " + sideChainAtomList2[j]);
                               //System.out.println("Entro");
                               try
                               {
                                  atom1 = aa1.getAtom(sideChainAtomList1[i]);
                                  atom2 = aa2.getAtom(sideChainAtomList2[j]);
                                  distance = Calc.getDistance(atom1, atom2);

                                  vdWradius1 = radius(aa1, atom1);
                                  vdWradius2 = radius(aa2, atom2);
                                  MIN_DISTANCE = vdWradius1 + vdWradius2;
                                  //System.out.println("Distance : " + distance + "  MIN : " + MIN_DISTANCE + " R1 " +  vdWradius1 + " R2 " +  vdWradius2);

                                  //collisionTotal++;
                                  if (distance <= MIN_DISTANCE)
                                  {
                                     //sum += krep * (1 - (distance/MIN_DISTANCE));
                                     //thereIsCollision = true;
                                     //return true;
                                     collisionCounter++;
                                     //System.out.println("Errorrrrrrrrrrrrrrrrrrrr       1 " + "suma : " + sum);
                                  }
                                /*  else if ((distance > MIN_DISTANCE) && (distance < (2 * MIN_DISTANCE)))
                                  {
                                     sum += katt * (Math.pow(distance/MIN_DISTANCE, 2) - 3 * (distance/MIN_DISTANCE) + 2);
                                     //System.out.println("Errorrrrrrrrrrrrrrrrrrrr       2 " + "suma : " + sum);
                                  }
                                  else if (distance >= 2 * MIN_DISTANCE)
                                  {
                                     sum += 0;
                                     //System.out.println("Errorrrrrrrrrrrrrrrrrrrr       3 " + "suma : " + sum);
                                  }
                                  else
                                  {System.out.println("Errorrrrrrrrrrrrrrrrrrrr          4 " );
                                  }
                                  */
                                  // ---------------------------------Second option-------------------------------------------------------
                                  /*
                                  if (distance >= MIN_DISTANCE)
                                  {
                                      sum += 0;
                                      //System.out.println("Errorrrrrrrrrrrrrrrrrrrr       1 " + "suma : " + sum);
                                  }
                                  else if (distance <= (0.8254 * MIN_DISTANCE))
                                  {
                                      sum += 10;
                                      //  System.out.println("Errorrrrrrrrrrrrrrrrrrrr       2 " + "suma : " + sum);
                                  }
                                  else
                                  {
                                      sum += 57.273 * (1 - (distance/MIN_DISTANCE));
                                      //    System.out.println("Errorrrrrrrrrrrrrrrrrrrr       3 " + "suma : " + sum);
                                  }
                                  */
                               }
                               catch (StructureException ex)
                               {
                                  Logger.getLogger(StructureUtil.class.getName()).log(Level.SEVERE, null, ex);
                               }
                           }
                      }
                  }
               }
            }
            //System.out.println("Termino : " + sum);
            //return sum;
            //temp[0] = sum;
            /*
            if (collisionTotal == 0)
            {
                temp[1] = 0;
            }
            else
            {
                temp[1] = (double)collisionCounter/collisionTotal;
            }

            if (collisionTotal == 0)
            {
                temp[0] = 0;
            }
            else
            {
                temp[0] = (double)collisionCounter/collisionTotal;
            }
            */
            //System.out.println("(collision) " + " : " + collisionCounter + " total : " +  collisionTotal);
            //System.out.println("Antes(sumvdW) " + " : " + temp[0] + " cratio : " + temp[1]);
            return collisionCounter;
    }

    private static boolean isSideChainAtom(String atomName)
    {
       // System.out.println("isSideChainAtom : " + atomName);
       if (atomName.equalsIgnoreCase("N") || atomName.equalsIgnoreCase("CA") || atomName.equalsIgnoreCase("C") || atomName.equalsIgnoreCase("O") || atomName.equalsIgnoreCase("CB"))
       {//System.out.println("isSideChainAtom : false");
         return false;
       }
       else
       {//System.out.println("isSideChainAtom : true" );
         return true;
       }
    }


    static public double AbsoluteAccuracy(Structure pdbStructure, Structure methodStructure, String measure)
    {

         //System.out.println("measure : " + measure);
        //int a = correctAngles(pdbStructure, methodStructure, measure);
        //System.out.println("correct angle : " + a);
        //int b = enabledResidues(methodStructure, measure);
        //System.out.println("enables : " + b);
        //System.out.println("a b : " + (double)a/b);
        //return (double)a/b;

        return (double)correctAngles(pdbStructure, methodStructure, measure)/enabledResidues(methodStructure, measure);
    }

    static public int correctAngles(Structure pdbStructure, Structure methodStructure, String measure)
    {
        Chain pdbChain, methodChain;
        Group pdbGroup, methodGroup;
        AminoAcid pdbAA, methodAA;
        int pdbAminoAcidIndex, methodAminoAcidIndex, counter = 0;
        //int coun = 0, suma = 0;
        for (int chain = 0; pdbStructure != null && pdbStructure.getChains() != null && chain < pdbStructure.getChains().size(); chain++)
        {
            pdbChain = pdbStructure.getChain(chain);
            methodChain = methodStructure.getChain(chain);
            for (int group = 0; pdbChain != null && pdbChain.getAtomGroups() != null && group < pdbChain.getAtomGroups().size(); group++)
            {
                pdbGroup = pdbChain.getAtomGroup(group);
                methodGroup = methodChain.getAtomGroup(group);
                if (pdbGroup instanceof AminoAcid && methodGroup instanceof AminoAcid)
                {
                    pdbAA = (AminoAcid) pdbGroup;
                    methodAA = (AminoAcid) methodGroup;
                    pdbAminoAcidIndex = Arrays.binarySearch(utils.ListUtil.THREE_LETTER_CODE, pdbAA.getPDBName());
                    methodAminoAcidIndex = Arrays.binarySearch(utils.ListUtil.THREE_LETTER_CODE, methodAA.getPDBName());
                    // System.out.println("Residue : " + methodAA.getPDBName());
                    if(pdbAminoAcidIndex != methodAminoAcidIndex)
                    {
                        System.out.println("Error : Residues names are not equals");
                        return -1;
                    }
                    //System.out.println("Counter : " + ++coun);
                    if (correctAngles(pdbAA, methodAA, measure))
                    {
                       counter++;
                    }//else{suma++;}
                }
            }
        }
        //System.out.println("suma :" + suma);
        return counter;
    }

    static public boolean correctAngles(AminoAcid pdbAA, AminoAcid methodAA, String measure)
    {
         double pdbAATorAngle, methodAATorAngle;
         int limit = 0;//, counter1 = 0;
         boolean isCorrect = true;
         int aminoAcidIndex = Arrays.binarySearch(utils.ListUtil.THREE_LETTER_CODE, pdbAA.getPDBName());
         String[] SideChainAtomList = utils.ListUtil.SIDE_CHAIN_ATOM_LIST[aminoAcidIndex];

         if (pdbAA.getPDBName().equalsIgnoreCase("GLY") || pdbAA.getPDBName().equalsIgnoreCase("ALA"))
         {
             return false;
         }

         if (measure.equalsIgnoreCase("X1"))
         {
             limit = 1;
         }
         else if (measure.equalsIgnoreCase("X12"))
         {
             if (methodAA.getPDBName().equalsIgnoreCase("CYS") || methodAA.getPDBName().equalsIgnoreCase("SER") || methodAA.getPDBName().equalsIgnoreCase("THR") || methodAA.getPDBName().equalsIgnoreCase("VAL"))
             {
                 return false;
             }
             limit = 2;
         }
         else if (measure.equalsIgnoreCase("X123"))
         {
             if (!(methodAA.getPDBName().equalsIgnoreCase("GLU") || methodAA.getPDBName().equalsIgnoreCase("LYS") || methodAA.getPDBName().equalsIgnoreCase("MET") || methodAA.getPDBName().equalsIgnoreCase("GLN") || methodAA.getPDBName().equalsIgnoreCase("ARG")))
             {//System.out.println("aa de x123");
                 return false;
             }
             limit = 3;
         }
         else
         {
             System.out.println("Error : limit was not read Measure : " + measure);
         }
         //System.out.println("AA : " + pdbAA.getPDBName() + " Measure : " + measure + " Limit : " + limit);
         for (int i = 3; i < 3 + limit && i < SideChainAtomList.length; i++)
         {
             //counter1++;
             try
             {
                 if (pdbAA.hasAtom(SideChainAtomList[i - 3]) && pdbAA.hasAtom(SideChainAtomList[i - 2]) && pdbAA.hasAtom(SideChainAtomList[i - 1]) && pdbAA.hasAtom(SideChainAtomList[i]))
                 {
                      pdbAATorAngle = StructureUtil.getTorsionAngle(pdbAA.getAtom(SideChainAtomList[i - 3]), pdbAA.getAtom(SideChainAtomList[i - 2]), pdbAA.getAtom(SideChainAtomList[i - 1]), pdbAA.getAtom(SideChainAtomList[i]));
                      if (methodAA.hasAtom(SideChainAtomList[i - 3]) && methodAA.hasAtom(SideChainAtomList[i - 2]) && methodAA.hasAtom(SideChainAtomList[i - 1]) && methodAA.hasAtom(SideChainAtomList[i]))
                      {
                          methodAATorAngle = StructureUtil.getTorsionAngle(methodAA.getAtom(SideChainAtomList[i - 3]), methodAA.getAtom(SideChainAtomList[i - 2]), methodAA.getAtom(SideChainAtomList[i - 1]), methodAA.getAtom(SideChainAtomList[i]));
                          //System.out.println("pdb angle : " + pdbAATorAngle);
                          //System.out.println("method angle : " + methodAATorAngle);

                          if (!StructureUtil.isAngleOK(pdbAATorAngle, methodAATorAngle, 40))
                          {
                             isCorrect = false;/*System.out.println("sale");*///System.out.println("correcto : FALSE ");
                             break;
                          }
                          //System.out.println("correcto : TRUE ");
                      }
                 }
             }
             catch (StructureException ex)
             {
                 ex.printStackTrace();
             }
         }
         //System.out.println("Counter : " + counter1);
         return isCorrect;
    }

    static public int enabledResidues(Structure methodStructure, String measure)
    {
        Chain methodChain;
        Group methodGroup;
        AminoAcid methodAA;
        int counter = 0, numResidues = 0, output = 0;

        for (int chain = 0; methodStructure != null && methodStructure.getChains() != null && chain < methodStructure.getChains().size(); chain++)
        {
            methodChain = methodStructure.getChain(chain);
            numResidues += methodChain.getAtomGroups().size();
            for (int group = 0; methodChain != null && methodChain.getAtomGroups() != null && group < methodChain.getAtomGroups().size(); group++)
            {
                methodGroup = methodChain.getAtomGroup(group);
                if (methodGroup instanceof AminoAcid)
                {
                    methodAA = (AminoAcid) methodGroup;
                    if (measure.equalsIgnoreCase("X1"))
                    {
                       if (methodAA.getPDBName().equalsIgnoreCase("ALA") || methodAA.getPDBName().equalsIgnoreCase("GLY"))
                       {
                            counter++; //System.out.println("X1 : " + methodAA.getPDBName());
                       }
                    }
                    else if (measure.equalsIgnoreCase("X12"))
                    {
                       if (methodAA.getPDBName().equalsIgnoreCase("ALA") || methodAA.getPDBName().equalsIgnoreCase("GLY") || methodAA.getPDBName().equalsIgnoreCase("CYS") || methodAA.getPDBName().equalsIgnoreCase("SER") || methodAA.getPDBName().equalsIgnoreCase("THR") || methodAA.getPDBName().equalsIgnoreCase("VAL"))
                       {
                            counter++;
                       }
                    }
                    else if (measure.equalsIgnoreCase("X123"))
                    {
                       if (methodAA.getPDBName().equalsIgnoreCase("GLU") || methodAA.getPDBName().equalsIgnoreCase("LYS") || methodAA.getPDBName().equalsIgnoreCase("MET") || methodAA.getPDBName().equalsIgnoreCase("GLN") || methodAA.getPDBName().equalsIgnoreCase("ARG"))
                       {//System.out.println("aa de x123");
                            counter++;
                       }
                    }
                    else
                    {
                        System.out.println("Error : Measure unknown Measure : " + measure);
                    }
                }
            }
        }

        if (measure.equalsIgnoreCase("X1") || measure.equalsIgnoreCase("X12"))
        {
            output = numResidues - counter;
            //System.out.println(" measure : " + measure + " num REs : " + numResidues + " counter : " + counter);
        }
        else if (measure.equalsIgnoreCase("X123"))
        {
            output = counter;
        }
        return output;
    }

    public static double RMSD(Structure pdbStructure, Structure methodStructure)
    {
            int numResidues = 0;
            double sum = 0;
            Chain c;
            Group g;
            AminoAcid aa;
            for (int chain = 0; methodStructure != null && methodStructure.getChains() != null && chain < methodStructure.getChains().size(); chain++)
             {
                c = methodStructure.getChain(chain);
                numResidues += c.getAtomGroups().size();
                for (int residue = 0; c != null && c.getAtomGroups() != null && residue < c.getAtomLength(); residue++)
                {
                    g = c.getAtomGroup(residue);
                    if (g instanceof AminoAcid)
                    {
                        aa = (AminoAcid) g;
                        sum += Double.parseDouble(StructureUtil.getRMSD(pdbStructure, methodStructure, chain, residue));
                    }//end if
                }//end for
             }//end for
             return sum/numResidues;
    }//end funcion


    static public Structure readStructure(String pdbId, String method, String PathAlgorithm)
    {
        PDBFileReader pdbReader = null;
        Structure struc = null;
        StringBuffer sb;
        pdbReader = new PDBFileReader();
        pdbReader.setAlignSeqRes(true);
        pdbReader.setAutoFetch(false);
        pdbReader.setParseCAOnly(false);
        pdbReader.setParseSecStruc(false);
        try
        {
            sb = new StringBuffer();
            sb.append(PathAlgorithm);
            if (method.equalsIgnoreCase("OpusRota"))
            {
                return opusRota.OpusRotaReader.readStructure(1, pdbId);
            }
            else if (method.equalsIgnoreCase("Scwrl4"))
            {
                return scwrl4.io.SCWRL4Reader.readStructure(pdbId);
            }
            else if (method.equalsIgnoreCase("Perfect"))
            {
                sb.append("Perfect/");
            }
            else if (method.equalsIgnoreCase("Best"))
            {
                sb.append("Best/");
            }
            else if (method.equalsIgnoreCase("Algorithm"))
            {
                sb.append("Algorithm/");
            }
            sb.append(pdbId);
            sb.append(".out");
            struc = pdbReader.getStructure(sb.toString());
            struc.setPDBCode(pdbId);
        }
        catch (IOException ex)
        {
            Logger.getLogger(opusRota.OpusRotaReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return struc;
    }
    
    static public void printRotamers(Solution sol, PrintWriter outFile, double kvdW, double kcratio, double krot)
    {
        outFile.println("\nSolution Rotamers : ");
        System.out.println("\nBest Solution Rotamers : ");
        //double vdWPotential = sol.vdWPotential;
        //double RotamerFrequency = sol.frequency;
        //double energy =  kvdW * sol.vdWPotential + kcratio * sol.cRatio + krot * sol.frequency;
        outFile.println("vdW : " + kvdW + " * " + sol.vdWPotential + " + CRatio : " + kcratio + " * " + sol.cRatio + " + FRE : " + krot + " * " + sol.frequency + " = " + sol.energy);
        System.out.println("vdW : " + kvdW + " * " + sol.vdWPotential + " + CRatio : " + kcratio + " * " + sol.cRatio + " + FRE : " + krot + " * " + sol.frequency + " = " + sol.energy);
        for (int chain = 0; chain < sol.getRomaterVector().size(); chain++)
        {
            for (int group = 0; group < sol.getRomaterVector().get(chain).size(); group++)
            {
                outFile.print(sol.getRomaterVector().get(chain).get(group) + "\t");
                System.out.print(sol.getRomaterVector().get(chain).get(group) + "\t");
            }
        }
        outFile.println("\n");
    }

    static public double roundDouble(double r, int decimalPlace)
    {
        BigDecimal bd = new BigDecimal(r);
        bd = bd.setScale(decimalPlace, RoundingMode.HALF_UP);
        r = bd.doubleValue();
        return(r);
    }
}
