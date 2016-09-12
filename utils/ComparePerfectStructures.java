/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.io.IOException;
import org.biojava.bio.structure.Structure;
//import org.biojava.bio.structure.StructureException;
import java.util.logging.Logger;
import java.util.logging.Level;
import pdb.PDBReader;
import pdb.PDBWrite;
import java.util.ArrayList;
import java.util.Arrays;
import org.biojava.bio.structure.AminoAcid;
import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.Group;
//import org.biojava.bio.structure.Atom;
import java.util.Iterator;
//import java.util.ListIterator;


/**
 *
 * @author clezcano
 */
public class ComparePerfectStructures
{
    static public final String PruebaPath = "/home/clezcano/Documents/IvyBioUtils/ComparacionEstructurasPerfectas/";
    static public final int AA_AMOUNT = 20;
    static public final int STRUCTURES_AMOUNT = 30;
    static ArrayList [][] RSMDvector = new ArrayList[AA_AMOUNT][STRUCTURES_AMOUNT];
    static double[][] DesviationVector = new double[AA_AMOUNT][STRUCTURES_AMOUNT];
    static double[][] Media = new double[AA_AMOUNT][STRUCTURES_AMOUNT];

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException
    {
        Structure s1 = null, s2 = null;
        ArrayList<String> alPDBIds = PDBReader.readPDBList("pdb1");
        initializeVectors(); 
        System.out.println("Total : " + alPDBIds.size());
        for (int i = 0; i < alPDBIds.size(); i++)
        {
            System.out.println((i + 1) + ": " + alPDBIds.get(i));
            s1 = PDBReader.readStructure(alPDBIds.get(i));
            s2 = StructureUtil.createPerfectStructure(s1);
            //System.out.println(" 1 main");
            PDBWrite.writePDB(s2, PruebaPath + "PerfectStructures/" + alPDBIds.get(i) + ".chr");
            RMSDanalysis(s1, s2, i);
            //System.out.println(" 2 main");
            //printmean();
            writeToFile(PruebaPath + "Statistics/" + alPDBIds.get(i) + ".chr", i);
        }//end for
        writeToFile(PruebaPath + "Summary/global.chr");
        //printmean();
    }//end main

 /*
    private static void printmean()
    {
        for (int i = 0; i < AA_AMOUNT; i++)
        {
           for (int j = 0; j < STRUCTURES_AMOUNT; j++)
           {
              System.out.print(Media[i][j] + "  ");
           }
           System.out.println();
        }
    }
*/
    private static void RMSDanalysis(Structure s1, Structure s2, int Structure_number)
    {
             int aminoAcidIndex = -1;
        
             
             for (int chain = 0; s2 != null && s2.getChains() != null && chain < s2.getChains().size(); chain++)
             {
                Chain c = s2.getChain(chain);
                for (int residue = 0; c != null && c.getAtomGroups() != null && residue < c.getAtomLength(); residue++)
                {
                    Group g = c.getAtomGroup(residue);
                    if (g instanceof AminoAcid)
                    {
                        AminoAcid aa = (AminoAcid) g;
                        aminoAcidIndex = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aa.getPDBName());
                        RSMDvector[aminoAcidIndex][Structure_number].add(Double.parseDouble((StructureUtil.getRMSD(s1, s2, chain, residue))));
                    }//end if
                }//end for
             }//end for
             
             for (int i = 0; i < AA_AMOUNT; i++)
             {
                 Media[i][Structure_number] = mean(RSMDvector[i][Structure_number]);
                 DesviationVector[i][Structure_number] = deviation(RSMDvector[i][Structure_number], Media[i][Structure_number]);
             }
             
    }//end funcion

    private static void initializeVectors()
    {
         for (int i = 0; i < AA_AMOUNT; i++)
         {
              for (int j = 0; j < STRUCTURES_AMOUNT; j++)
              {
                    RSMDvector[i][j] = new ArrayList<Double>();
                    DesviationVector[i][j]= 0.0;
                    Media[i][j] = 0.0;
              }
         }
    }

    private static double mean(ArrayList<Double> dev)
    {
          double sum = 0.0;

          if (dev.isEmpty())
          {
              return -1;
          }

          for (int i = 0; i < dev.size(); i++)
          {
              sum += dev.get(i).doubleValue();
          }
          return sum / dev.size();
    }

    private static double deviation(ArrayList<Double> dev, double mean)
    {

           if (dev.isEmpty())
           {
              return -1;
           }
           else if(dev.size() == 1)
           {
              return 0;
           }

           double squareSum = 0;

           for (int i = 0; i < dev.size(); i++)
           {
                squareSum += Math.pow(dev.get(i).doubleValue() - mean, 2);
           }

           return Math.sqrt((squareSum)/(dev.size()-1));
    }

    private static void writeToFile(String fileName, int StructureNumber)
    {
        File fileOut = null;
        PrintWriter outFile = null;
        try
        {
            fileOut = new File(fileName);
            outFile = new PrintWriter(new BufferedWriter(new FileWriter(fileOut)));
            
            //outFile.println("AA  AMOUNT  RSMD");
            outFile.println("AA  AMOUNT   MEAN     RSMD");
            for (int i = 0; i < AA_AMOUNT; i++)
            {
                 try
                 {
                    outFile.println(getRecord(i, StructureNumber));
                 }
                 catch (Exception ex)
                 {
                    Logger.getLogger(PDBWrite.class.getName()).log(Level.SEVERE, null, ex);
                 }
            }
            outFile.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(PDBWrite.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void writeToFile(String fileName)
    {
        File fileOut = null;
        PrintWriter outFile = null;
        try
        {
            fileOut = new File(fileName);
            outFile = new PrintWriter(new BufferedWriter(new FileWriter(fileOut)));
            
            outFile.println("AA  DETAILED AVERAGE MATRIX");
            for (int i = 0; i < AA_AMOUNT; i++)
            {
                try
                {
                   outFile.println(getRecord(i, 'a')); // matrix de medias
                }
                catch (Exception ex)
                {
                   Logger.getLogger(PDBWrite.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            outFile.println();
            
            outFile.println("AA  DETAILED DEVIATION MATRIX");
            for (int i = 0; i < AA_AMOUNT; i++)
            {
                try
                {
                   outFile.println(getRecord(i , 'b'));// matrix de desviaciones
                }
                catch (Exception ex)
                {
                   Logger.getLogger(PDBWrite.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            outFile.println();
            
            outFile.println("AA     MEAN  DEVIATION");
            for (int i = 0; i < AA_AMOUNT; i++)
            {
                try
                {
                   outFile.println(getRecord(i , 'c'));// matrix resumen
                }
                catch (Exception ex)
                {
                   Logger.getLogger(PDBWrite.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            outFile.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(PDBWrite.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static public String getRecord(int AA_number, int StructureNumber)
    {
        StringBuilder sb = new StringBuilder();
        //DecimalFormat coords = new DecimalFormat("#0.000");
        //DecimalFormat df2 = new DecimalFormat("#0.00");
        DecimalFormat decimal = new DecimalFormat("#0.0000");
        DecimalFormat entero = new DecimalFormat("#0");

        String temp = ListUtil.THREE_LETTER_CODE[AA_number];
        sb.append(temp);
        sb.append("   ");

        temp = entero.format(RSMDvector[AA_number][StructureNumber].size());
        sb.append(temp);
        sb.append("    ");

        temp = decimal.format(Media[AA_number][StructureNumber]);
        sb.append(temp);
        sb.append("    ");

        /*
         temp = Double.toString(Media[AA_number][StructureNumber]);
        sb.append(temp);
        sb.append("  ");

        temp = Double.toString(DesviationVector[AA_number][StructureNumber]);
        sb.append(temp);
        sb.append(" ");
        */

        Iterator<Double> itr = RSMDvector[AA_number][StructureNumber].listIterator();
        temp = "";
        while (itr.hasNext())
        {
            temp += " " + decimal.format((Double)itr.next().doubleValue());
        }

        sb.append(temp);
        
        return sb.toString();
    }

    static public String getRecord(int AA_number, char matrix)
    {
        StringBuilder sb = new StringBuilder();
        DecimalFormat decimal = new DecimalFormat("#0.0000");
        //DecimalFormat df2 = new DecimalFormat("#0.00");

        String temp = ListUtil.THREE_LETTER_CODE[AA_number];
        
        if(matrix == 'a')
        {
           for (int i = 0; i < STRUCTURES_AMOUNT; i++)
           {
               temp += " " + decimal.format(Media[AA_number][i]);
           }
        }
        else if(matrix == 'b')
        {
           for (int i = 0; i < STRUCTURES_AMOUNT; i++)
           {
               temp += " " + decimal.format(DesviationVector[AA_number][i]);
           }
        }
        else if(matrix == 'c')
        {
           double auxMedia = 0.0, auxDesviacion = 0.0;
           for (int i = 0; i < STRUCTURES_AMOUNT; i++)
           {
               if (Media[AA_number][i] > 0)
               {
                  auxMedia += Media[AA_number][i];
                  auxDesviacion += DesviationVector[AA_number][i];
               }
           }
           temp += "   " + decimal.format(auxMedia/STRUCTURES_AMOUNT) + "  " + decimal.format(auxDesviacion/STRUCTURES_AMOUNT);
        }

        sb.append(temp);
        
        return sb.toString();
    }
    
}





