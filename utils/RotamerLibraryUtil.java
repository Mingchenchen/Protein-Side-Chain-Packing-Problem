package utils;

import java.util.Arrays;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import org.biojava.bio.structure.AminoAcid;
//import org.biojava.bio.structure.Calc;
import org.biojava.bio.structure.StructureException;
import rotlib.Rotamer;
import rotlib.RotamerLibrary;
//import org.biojava.bio.structure.AminoAcid;
public class RotamerLibraryUtil
{

    public RotamerLibraryUtil()
    {}

    public static Rotamer getBestRotamer(RotamerLibrary rl, AminoAcid aa)
    {
        int iAminoAcidIndex = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aa.getPDBName());
        String[] sideChainAtomList = ListUtil.SIDE_CHAIN_ATOM_LIST[iAminoAcidIndex];
        int iBestRotamerAccuracy = 0, iTmpRotamerAccuracy;
        Rotamer bestRotamer = null, r;
        double aaTorAngle;
        double rotTorAngle;

        if (rl == null || rl.getRotamer() == null)
        {
            return null;
        }

        for (int i = 0; i < rl.getRotamer().size(); i++)
        {
            r = rl.getRotamer().get(i);
            iTmpRotamerAccuracy = 0;

            for (int j = 3; j < sideChainAtomList.length; j++)
            {
                try
                {
                    if (aa.hasAtom(sideChainAtomList[j - 3]) && aa.hasAtom(sideChainAtomList[j - 2]) && aa.hasAtom(sideChainAtomList[j - 1]) && aa.hasAtom(sideChainAtomList[j]))
                    {
                         aaTorAngle = StructureUtil.getTorsionAngle(aa.getAtom(sideChainAtomList[j - 3]), aa.getAtom(sideChainAtomList[j - 2]), aa.getAtom(sideChainAtomList[j - 1]), aa.getAtom(sideChainAtomList[j]));
                         rotTorAngle = r.getChiValue(j - 2);
                         if (StructureUtil.isAngleOK(aaTorAngle, rotTorAngle, 40))
                         {
                            iTmpRotamerAccuracy++;
                         }
                         else
                         {
                            break;
                         }
                    }
                } 
                catch (StructureException ex)
                {
                    ex.printStackTrace();
                }
            }

            if (bestRotamer == null || iBestRotamerAccuracy < iTmpRotamerAccuracy || (iBestRotamerAccuracy == iTmpRotamerAccuracy && bestRotamer.getProbabilityValue() < r.getProbabilityValue()))
            {
                bestRotamer = r;
                iBestRotamerAccuracy = iTmpRotamerAccuracy;
            }
        }
        return bestRotamer;
    }

    public static Rotamer getBestRotamerRMSD(RotamerLibrary rl, AminoAcid aa)
    {
        Rotamer r;
        AminoAcid aa2;
        double bestRMSD, actualRMSD;
        int bestRotamerNumber;

        if (rl == null || rl.getRotamer() == null)
        {
            return null;
        }
        
        r = rl.getRotamer().get(0);
        aa2 = StructureUtil.createAminoacid(aa, r);
        bestRMSD = Double.parseDouble(StructureUtil.getRMSD(aa, aa2));
        bestRotamerNumber = 0;
        for (int i = 1; i < rl.getRotamer().size(); i++)
        {
            r = rl.getRotamer().get(i);
            aa2 = StructureUtil.createAminoacid(aa, r);
            actualRMSD = Double.parseDouble(StructureUtil.getRMSD(aa, aa2));
            if(actualRMSD < bestRMSD)
            {
               bestRMSD = actualRMSD;
               bestRotamerNumber = i;
            }
        }
        return rl.getRotamer().get(bestRotamerNumber);
    }


    public static int getNumberBestRotamer(RotamerLibrary rl, AminoAcid aa)
    {
        int bestRotamerIndex = 0;
        int iAminoAcidIndex = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aa.getPDBName());
        String[] sideChainAtomList = ListUtil.SIDE_CHAIN_ATOM_LIST[iAminoAcidIndex];
        int iBestRotamerAccuracy = 0;
        Rotamer bestRotamer = null;

        if (rl == null || rl.getRotamer() == null)
        {
            return -1;
        }

        for (int i = 0; i < rl.getRotamer().size(); i++)
        {
            Rotamer r = rl.getRotamer().get(i);
            int iTmpRotamerAccuracy = 0;

            for (int j = 3; j < sideChainAtomList.length; j++)
            {
                try
                {
                    if (aa.hasAtom(sideChainAtomList[j - 3]) && aa.hasAtom(sideChainAtomList[j - 2]) && aa.hasAtom(sideChainAtomList[j - 1]) && aa.hasAtom(sideChainAtomList[j]))
                    {
                          double aaTorAngle = StructureUtil.getTorsionAngle(aa.getAtom(sideChainAtomList[j - 3]), aa.getAtom(sideChainAtomList[j - 2]), aa.getAtom(sideChainAtomList[j - 1]), aa.getAtom(sideChainAtomList[j]));
                          double rotTorAngle = r.getChiValue(j - 2);
                          if (StructureUtil.isAngleOK(aaTorAngle, rotTorAngle, 40))
                          {
                               iTmpRotamerAccuracy++;
                          }
                          else
                          {
                               break;
                          }
                    }
                } 
                catch (StructureException ex)
                {
                    ex.printStackTrace();
                }
            }

            if (bestRotamer == null || iBestRotamerAccuracy < iTmpRotamerAccuracy || (iBestRotamerAccuracy == iTmpRotamerAccuracy && bestRotamer.getProbabilityValue() < r.getProbabilityValue()))
            {
                bestRotamerIndex = i;
                bestRotamer = r;
                iBestRotamerAccuracy = iTmpRotamerAccuracy;
            }
        }
        return bestRotamerIndex;
    }


    public static int getNumberBestRotamerRMSD(RotamerLibrary rl, AminoAcid aa)
    {
        Rotamer r;
        AminoAcid aa2;
        double bestRMSD, actualRMSD;
        int bestRotamerNumber;

        if (rl == null || rl.getRotamer() == null)
        {
            return -1;
        }

        r = rl.getRotamer().get(0);
        aa2 = StructureUtil.createAminoacid(aa, r);
        bestRMSD = Double.parseDouble(StructureUtil.getRMSD(aa, aa2));
        bestRotamerNumber = 0;
        for (int i = 1; i < rl.getRotamer().size(); i++)
        {
            r = rl.getRotamer().get(i);
            aa2 = StructureUtil.createAminoacid(aa, r);
            actualRMSD = Double.parseDouble(StructureUtil.getRMSD(aa, aa2));
            if(actualRMSD < bestRMSD)
            {
               bestRMSD = actualRMSD;
               bestRotamerNumber = i;
            }
        }
        return bestRotamerNumber;
    }

    
}

