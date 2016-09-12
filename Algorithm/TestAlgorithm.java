/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Algorithm;

/*import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
*/
/*
 import org.biojava.bio.structure.Structure;
//import java.util.logging.Logger;
//import java.util.logging.Level;
 
 */
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import pdb.PDBReader;
import rotlib.RotamerLibrary;
//import rotlib.RotamerLibrary;
//import pdb.PDBWrite;
import java.util.ArrayList;
import rotlib.io.RotamerLibraryReader;
import rotlib.RotamerLibraryCollection;
//import Algorithm.Algorithm;
import scwrl4.io.SCWRL4Reader;
import utils.StructureUtil;
import opusRota.OpusRotaReader;
import pdb.PDBWrite;
import org.biojava.bio.structure.Structure;
/*import java.util.Arrays;
import org.biojava.bio.structure.AminoAcid;
import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.Group;
import java.util.Iterator;
import utils.ListUtil;
import utils.StructureUtil;
*/
//import rotlib.RotamerLibraryCollection;
//import rotlib.io.RotamerLibraryReader;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.IOException;
//import org.biojava.bio.structure.io.PDBFileReader;
//import org.biojava.bio.structure.AminoAcid;
//import org.biojava.bio.structure.Chain;
//import org.biojava.bio.structure.Group;
//import org.biojava.bio.structure.StructureException;
//import org.biojava.bio.structure.Atom;
//import java.util.Arrays;
import java.text.DecimalFormat;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author clezcano
 */
public class TestAlgorithm
{
    //static public final String PruebaPath = "/home/clezcano/Documents/IvyBioUtils/Algorithm/";
    //static public final int AA_AMOUNT = 20;
    //static public final int STRUCTURES_AMOUNT = 30;
    //static public final int NUM_EXECUTIONS = 1;
    static public final String PathAlgorithm = "/home/clezcano/Documents/IvyBioUtils/Algorithm/Statistics/Structures/";
    //static ArrayList [][] RSMDvector = new ArrayList[AA_AMOUNT][STRUCTURES_AMOUNT];
    //static double[][] DesviationVector = new double[AA_AMOUNT][STRUCTURES_AMOUNT];
    //static double[][] Media = new double[AA_AMOUNT][STRUCTURES_AMOUNT];
    
    
    public static void main(String[] args) throws IOException
    {
        Structure s = null, s1 = null, s2 = null, s3 = null, s4 = null, s5 = null, s6 = null;
        Structure pdbStructure, methodStructure;
        File fileOut = null, fileOut2 = null, fileOut3 = null;
        PrintWriter outFile = null, outFile2 = null, outFile3 = null;
        String record = "", record2 = "", record3 = "";
        DecimalFormat df = new DecimalFormat("#0.0000");
        DecimalFormat df1 = new DecimalFormat("#0.0000000000");
        double relativeError, potential = 0.0, pdbPotential = 0.0, cratio = 0.0, pdbCratio = 0.0;
        String [] methods = new String [] {"Perfect", "Best", "OpusRota", "Scwrl4", "Algorithm"};
        String [] measures = new String [] {"X1", "X12", "X123"};


        ArrayList<String> alPDBIds = PDBReader.readPDBList("pdb1test");
        //ArrayList<String> alPDBIds = PDBReader.readPDBList("opusRota65");
        RotamerLibraryCollection rlc = RotamerLibraryReader.readRotamerLibrary(RotamerLibraryCollection.DUN_IND_ROT_LIB_TYPE);
        //System.out.println("Total of instances: " + alPDBIds.size());
        /*
        //---------------MAIN 1----------------------------
        System.out.println("Number of executions: " + NUM_EXECUTIONS);
         Algorithm alg = new Algorithm();
        for (int exeNum = 1; exeNum <= NUM_EXECUTIONS; exeNum++)
        {
        //for (int i = 0; i < alPDBIds.size(); i++)
        //{
            //System.out.println((i + 1) + ": " + alPDBIds.get(i));
            //s1 = PDBReader.readStructure(alPDBIds.get(i));
            
            alg.runAlgorithm(alPDBIds, exeNum);
            
            //PDBWrite.writePDB(s2, PruebaPath + "Structures/" + alPDBIds.get(i) + ".chr");
            //s2 = StructureUtil.createPerfectStructure(s1);
            //System.out.println(" 1 main ////////////////////////");
            //RSMDanalysis(s1, s2, i);
            //System.out.println(" 2 main////////////////////");
            //writeToFile(PruebaPath + "Statistics/" + alPDBIds.get(i) + ".chr", i);
        }
         */
        
        //}
        //writeToFile(PruebaPath + "Summary/global.chr");
 
        //---------------MAIN 2----------------------------
        fileOut = new File(PathAlgorithm + "AllEnergyFunctions.txt");
        outFile = new PrintWriter(new BufferedWriter(new FileWriter(fileOut)));
        Solution sol;
        Algorithm alg = new Algorithm();
        outFile.println("vdW Potential - Cratio - Rotamer Frequency ");
        for (int i = 0; i < alPDBIds.size(); i++)
        {
            outFile.println((i + 1) + ": " + alPDBIds.get(i));
            s1 = PDBReader.readStructure(alPDBIds.get(i));
            s2 = StructureUtil.createPerfectStructure(s1);
            sol = new Solution(s1);
            StructureUtil.createBestStructure(s1, sol, rlc);
            //StructureUtil.createBestStructureRSMD(s1, sol, rlc);
            StructureUtil.cisRR(sol, s1, rlc);
            sol.getEnergy();
            s3 = StructureUtil.createStructure(s1, sol, rlc);
            //s3 = StructureUtil.createBestStructure(s1, rlc);
            //s3 = StructureUtil.createBestStructureRSMD(s1, rlc);
            s4 = OpusRotaReader.readStructure(1, alPDBIds.get(i));
            s5 = SCWRL4Reader.readStructure(alPDBIds.get(i));
            //s6 = alg.runAlgorithm(s1);
            //outFile.println("Algorithm : vdW " + StructureUtil.getvdWPotential(s6));
            outFile.println("Scwrl4 : vdW " + StructureUtil.getvdWPotential(s5));
            outFile.println("Opus Rota : vdW " + StructureUtil.getvdWPotential(s4));
            //outFile.println("Best : " + StructureUtil.getvdWPotential(s3));
            outFile.println("Best :  vdW : " + sol.vdWPotential + " Cratio : " + sol.cRatio + " Rotamer Frequency : " + sol.frequency);
            outFile.println("Perfect : vdW " + StructureUtil.getvdWPotential(s2));
            outFile.println("PDB : vdW " + StructureUtil.getvdWPotential(s1));//System.out.println(" 2 main////////////////////");
                                                                                                //PDBWrite.writePDB(s1, PathAlgorithm + "Pdb/" + alPDBIds.get(i) + ".out");
            PDBWrite.writePDB(s2, PathAlgorithm + "Perfect/" + alPDBIds.get(i) + ".out");
            PDBWrite.writePDB(s3, PathAlgorithm + "Best/" + alPDBIds.get(i) + ".out");
            //PDBWrite.writePDB(s6, PathAlgorithm + "Algorithm/" + alPDBIds.get(i) + ".out");
            PDBWrite.writePDB(s2, PathAlgorithm + "PERF" + alPDBIds.get(i) + ".pdb");
            PDBWrite.writePDB(s3, PathAlgorithm + "BEST" + alPDBIds.get(i) + ".pdb");
            //PDBWrite.writePDB(s6, PathAlgorithm + "ALG" + alPDBIds.get(i) + ".out");

            StructureUtil.CollisionAnalysis(PathAlgorithm + alPDBIds.get(i) + "PDB.col", s1);
            StructureUtil.CollisionAnalysis(PathAlgorithm + alPDBIds.get(i) + "PER.col", s2);
            StructureUtil.CollisionAnalysis(PathAlgorithm + alPDBIds.get(i) + "BES.col", s3);
            StructureUtil.CollisionAnalysis(PathAlgorithm + alPDBIds.get(i) + "OPU.col", s4);
            StructureUtil.CollisionAnalysis(PathAlgorithm + alPDBIds.get(i) + "SCW.col", s5);
            //StructureUtil.CollisionAnalysis(PathAlgorithm + alPDBIds.get(i) + "ALG.col", s6);
            //System.out.println(" 2 main////////////////////");
        }//end for
        outFile.close();
    /*
    }
}


        


        //////////////////////////////////////
         // Obtener angulos de torsion chi1, chi2, etc de una Estructura
         /////////////////////////////////////////
        
        for (int i = 0; i < alPDBIds.size(); i++)
        {
            fileOut = new File(PathAlgorithm + alPDBIds.get(i) + "TorsionAnglesPDB.txt");
            outFile = new PrintWriter(new BufferedWriter(new FileWriter(fileOut)));
            outFile.println((i + 1) + ": " + alPDBIds.get(i));
            s = PDBReader.readStructure(alPDBIds.get(i));
            StructureUtil.getTorsionAnglesFromStructure(s, outFile);
            outFile.close();
            fileOut = new File(PathAlgorithm + alPDBIds.get(i) + "TorsionAnglesOPUS.txt");
            outFile = new PrintWriter(new BufferedWriter(new FileWriter(fileOut)));
            outFile.println((i + 1) + ": " + alPDBIds.get(i));
            s = OpusRotaReader.readStructure(1, alPDBIds.get(i));
            StructureUtil.getTorsionAnglesFromStructure(s, outFile);
            outFile.close();
            fileOut = new File(PathAlgorithm + alPDBIds.get(i) + "TorsionAnglesALG.txt");
            outFile = new PrintWriter(new BufferedWriter(new FileWriter(fileOut)));
            outFile.println((i + 1) + ": " + alPDBIds.get(i));
            s = StructureUtil.readStructure(alPDBIds.get(i), "Algorithm", PathAlgorithm);
            StructureUtil.getTorsionAnglesFromStructure(s, outFile);
            outFile.close();
        }
        

    }
}

         */
//StructureUtil.CollisionAnalysis(PathAlgorithm + "1a2jOPU.col", OpusRotaReader.readStructureCollision("1a2j"));
//StructureUtil.CollisionAnalysis(PathAlgorithm + "1a2jALG1.col", StructureUtil.readStructure("1A2J", "Algorithm", PathAlgorithm));
         //////////////////////////////////////
         // Calculo de numero de colisiones
         /////////////////////////////////////////
        /*
        ArrayList<String> alPDBIds = PDBReader.readPDBList("opusRota65");
        System.out.println("Total of instances: " + alPDBIds.size());
        fileOut = new File(PathAlgorithm + "OpusRotaCollisionNumber.txt");
        outFile = new PrintWriter(new BufferedWriter(new FileWriter(fileOut)));
        for (int i = 0; i < alPDBIds.size(); i++)
        {
            outFile.println((i + 1) + ") " + alPDBIds.get(i) + " : " + StructureUtil.NumberOfCollision(OpusRotaReader.readStructureCollision(alPDBIds.get(i))));
        }
        outFile.close();

        alPDBIds = PDBReader.readPDBList("scwrl4_373");
        System.out.println("Total of instances: " + alPDBIds.size());
        fileOut = new File(PathAlgorithm + "Scwrl4CollisionNumber.txt");
        outFile = new PrintWriter(new BufferedWriter(new FileWriter(fileOut)));
        for (int i = 0; i < alPDBIds.size(); i++)
        {
            outFile.println((i + 1) + ") " + alPDBIds.get(i) + " : " + StructureUtil.NumberOfCollision(SCWRL4Reader.readStructureCollision(alPDBIds.get(i))));
        }
        outFile.close();

        ArrayList<String> alPDBIds = PDBReader.readPDBList("pdb723");
        System.out.println("Total of instances: " + alPDBIds.size());
        fileOut = new File(PathAlgorithm + "PDBCollisionNumber.txt");
        outFile = new PrintWriter(new BufferedWriter(new FileWriter(fileOut)));
        int col = 0;
        for (int i = 0; i < alPDBIds.size(); i++)
        {
           System.out.println((i + 1) + ") " + alPDBIds.get(i));
           col = StructureUtil.NumberOfCollision(PDBReader.readStructure(alPDBIds.get(i)));
           System.out.println("collision : " + col);
           outFile.println((i + 1) + ") " + alPDBIds.get(i) + " : " + col);
        }
        outFile.close();
    }
}
    */


        //////////////////////////////////////
        // Busqueda Local tomando como semilla al BEST
        // VECINDARIO DEL BEST
        /////////////////////////////////////////
       
       //---------------MAIN 5----------------------------
        //File fileOut = null;
        //PrintWriter outFile = null;
        //String record = "";
        //Structure pdbStructure;//, BestStructure;
        //DecimalFormat df = new DecimalFormat("#0.0000");
      /*
        Solution BestSol, neighborSol, BestNeighborSol;
        RotamerLibrary rl;
        boolean flag = false;
        //Structure BestStructure, neighborStructure;
        double BestEnergyFound = 0, neighborEnergy = 0;
        try
        {
            for (int i = 0; i < alPDBIds.size(); i++)
            {
                fileOut = new File(PathAlgorithm + "BestNeighborhood_" + alPDBIds.get(i) + ".txt");
                outFile = new PrintWriter(new BufferedWriter(new FileWriter(fileOut)));
                outFile.println("\n\nProtein : " + alPDBIds.get(i));
                pdbStructure = PDBReader.readStructure(alPDBIds.get(i));
                BestSol = new Solution(pdbStructure);
                StructureUtil.createBestStructure(pdbStructure, BestSol, rlc);
                //neighborSol = BestSol.CopySolution();
                BestNeighborSol = BestSol;
                //BestStructure = StructureUtil.createStructure(pdbStructure, BestSol, rlc);
                //outFile.println("Best : " +  df.format(sol.getvdWPotential()));
                //BestvdW = StructureUtil.getvdWPotential(BestStructure);
                //BestSol.getEnergy();
                //BestEnergyFound = BestSol.energy;
                //outFile.println("\nBest Solution vdWPotential : " +  df.format(BestvdW));
                //record = "";
                do
                {
                     BestSol = BestNeighborSol;
                     BestSol.getEnergy();
                     BestEnergyFound = BestSol.energy;
                     neighborSol = BestSol.CopySolution();
                     outFile.println("\nBest Solution Energy : " +  df.format(BestSol.energy));
                     StructureUtil.printRotamers(BestSol, outFile);
                     flag = false;
                     for (int chain = 0; chain < neighborSol.getRomaterVector().size(); chain++)
                     {
                        for (int group = 0; group < neighborSol.getRomaterVector().get(chain).size(); group++)
                        {
                            if (!(neighborSol.getAAVector().get(chain).get(group).equalsIgnoreCase("GLY") || neighborSol.getAAVector().get(chain).get(group).equalsIgnoreCase("ALA")))
                            {
                                //if (!(group == sol.lastChangedResidue(1) || group == sol.lastChangedResidue(2)))
                                //{
                                rl = rlc.getRotamerLibrary(neighborSol.getAAVector().get(chain).get(group));
                                for (int rot = 0; rot < rl.getRotamer().size(); rot++)
                                {
                                    if (rot != Integer.parseInt(BestSol.getRomaterVector().get(chain).get(group)))
                                    {
                                        neighborSol.getRomaterVector().get(chain).set(group, Integer.toString(rot));
                                        //neighborStructure = StructureUtil.createStructure(pdbStructure, neighborSol, rlc);
                                        //neighborVdW = StructureUtil.getvdWPotential(neighborStructure);
                                        neighborSol.getEnergy();
                                        if (neighborSol.energy < BestEnergyFound)
                                        {
                                            flag = true;
                                            BestEnergyFound = neighborSol.energy;
                                            BestNeighborSol = neighborSol.CopySolution();
                                            outFile.println("Neighbor Solution Energy : " + df.format(neighborSol.energy));
                                            StructureUtil.printRotamers(neighborSol, outFile);
                                        }
                                        //record += df.format(neighborSol.getvdWPotential()) + "\t";
                                    }
                                 }
                                 neighborSol.getRomaterVector().get(chain).set(group, BestSol.getRomaterVector().get(chain).get(group));
                             //}
                           }
                        }
                     }
               }
               while(flag == true);
               //while(BestEnergyFound < BestSol.energy);
               //outFile.println(record);
               outFile.close();
               PDBWrite.writePDB(StructureUtil.createStructure(pdbStructure, BestSol, rlc), PathAlgorithm + "Algorithm/" + alPDBIds.get(i) + ".out");
            }
            //outFile.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(TestAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
        }
    //////////////////////////////////////
    //  FIN VECINDARIO DEL BEST
    /////////////////////////////////////////
*/


 /*
        //--------------------------------------------------
        //Lectura de una solucion a partir de sus rotameros
        //--------------------------------------------------
        File f = null;
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        Solution sol;
        for (int i = 0; i < alPDBIds.size(); i++)
        {
           pdbStructure = PDBReader.readStructure(alPDBIds.get(i));
           sol = new Solution(pdbStructure);
           f = new File("rotamers.txt");
           fis = new FileInputStream(f);
           bis = new BufferedInputStream(fis);
           dis = new DataInputStream(bis);
           //int lineNumber = 0;
           try
           {
               while (dis.available() != 0)
               {
                   String line = dis.readLine();
                  
                      String delims = "[\t]+";
                      String[] fields = line.split(delims);
                      System.out.println("leido : " + fields.length);
                      //int counter = 0;
                      for (int j = 0; j < fields.length; j++)
                      {//counter++;
                        System.out.print(fields[j] + "\t");
                        sol.getRomaterVector().get(0).set(j, fields[j]);
                      }
                       System.out.println("\nfirst : " + fields[0]);
                       System.out.println("\nsec : " + fields[1]);
                       System.out.println("\nthird : " + fields[2]);
                      System.out.println("\ntiene : " + sol.getRomaterVector().get(0).size());
                      
                      for (int chain = 0; chain < sol.getRomaterVector().size(); chain++)
                      {
                             for (int group = 0; group < sol.getRomaterVector().get(chain).size(); group++)
                             {
                                  //outFile.print(sol.getRomaterVector().get(chain).get(group) + "\t");
                                  System.out.print(sol.getRomaterVector().get(chain).get(group) + "\t");
                             }
                      }
               }

           }
           catch (IOException ex)
           {
                ex.printStackTrace();
           }
           PDBWrite.writePDB(StructureUtil.createStructure(pdbStructure, sol, rlc), PathAlgorithm + "Algorithm/" + alPDBIds.get(i) + ".out");
        }
   */
       /*
        //--------------------------------------------------
        //lectura de las distancias R y angulos Teta entre atomos de un mismo residuo del opusrota y pdb
        //--------------------------------------------------
       
        for (int i = 0; i < alPDBIds.size(); i++)
        {
            fileOut = new File(PathAlgorithm + alPDBIds.get(i) + "DistancesFromPDB.txt");
            outFile = new PrintWriter(new BufferedWriter(new FileWriter(fileOut)));
            outFile.println((i + 1) + ": " + alPDBIds.get(i));
            fileOut2 = new File(PathAlgorithm + alPDBIds.get(i) + "AnglesFromPDB.txt");
            outFile2 = new PrintWriter(new BufferedWriter(new FileWriter(fileOut2)));
            outFile2.println((i + 1) + ": " + alPDBIds.get(i));
            fileOut3 = new File(PathAlgorithm + alPDBIds.get(i) + "RoundDistancesFromPDB.txt");
            outFile3 = new PrintWriter(new BufferedWriter(new FileWriter(fileOut3)));
            outFile3.println((i + 1) + ": " + alPDBIds.get(i));
            StructureUtil.getDistancesAndAnglesFromStructure(PDBReader.readStructure(alPDBIds.get(i)), outFile, outFile2, outFile3);
            outFile.close();
            outFile2.close();
            outFile3.close();
            fileOut = new File(PathAlgorithm + alPDBIds.get(i) + "DistancesFromOpusRota.txt");
            outFile = new PrintWriter(new BufferedWriter(new FileWriter(fileOut)));
            outFile.println((i + 1) + ": " + alPDBIds.get(i));
            fileOut2 = new File(PathAlgorithm + alPDBIds.get(i) + "AnglesFromOpusRota.txt");
            outFile2 = new PrintWriter(new BufferedWriter(new FileWriter(fileOut2)));
            outFile2.println((i + 1) + ": " + alPDBIds.get(i));
            fileOut3 = new File(PathAlgorithm + alPDBIds.get(i) + "RoundDistancesFromOpusRota.txt");
            outFile3 = new PrintWriter(new BufferedWriter(new FileWriter(fileOut3)));
            outFile3.println((i + 1) + ": " + alPDBIds.get(i));
            StructureUtil.getDistancesAndAnglesFromStructure(OpusRotaReader.readStructure(1, alPDBIds.get(i)), outFile, outFile2, outFile3);
            outFile.close();
            outFile2.close();
            outFile3.close();
        }
        
    }
}
*/
  
        try
        {
            fileOut = new File(PathAlgorithm + "RelativeError.txt");
            outFile = new PrintWriter(new BufferedWriter(new FileWriter(fileOut)));
            fileOut2 = new File(PathAlgorithm + "vdWPotential.txt");
            outFile2 = new PrintWriter(new BufferedWriter(new FileWriter(fileOut2)));
            outFile.println("AA\t\tPERFECT\t\tBEST\t\tOPUS ROTA\tSCWRL4\t\tALGORITHM");
            outFile2.println("AA\t\tPERFECT\t\tBEST\t\tOPUS ROTA\tSCWRL4\t\tALGORITHM\t\tPDB");
            fileOut3 = new File(PathAlgorithm + "Cratio.txt");
            outFile3 = new PrintWriter(new BufferedWriter(new FileWriter(fileOut3)));
            outFile3.println("AA\t\tPERFECT\t\tBEST\t\tOPUS ROTA\tSCWRL4\t\tALGORITHM\t\tPDB");
            System.out.println("\nRelativeError ");
            for (int i = 0; i < alPDBIds.size(); i++)
            {
                //System.out.println("hola1 ");
                System.out.println("Protein : " + alPDBIds.get(i));
                pdbPotential = StructureUtil.getvdWPotential(PDBReader.readStructure(alPDBIds.get(i)));
                pdbCratio = StructureUtil.getCRatio(PDBReader.readStructure(alPDBIds.get(i)));
                record = alPDBIds.get(i);
                record2 = alPDBIds.get(i);
                record3 = alPDBIds.get(i);
                //System.out.println("hola2 ");
                for (int j = 0; j < methods.length; j++)
                {
                  //  System.out.println("hola3 ");
                   potential = StructureUtil.getvdWPotential(StructureUtil.readStructure(alPDBIds.get(i), methods[j], PathAlgorithm));
                   cratio = StructureUtil.getCRatio(StructureUtil.readStructure(alPDBIds.get(i), methods[j], PathAlgorithm));
                   //System.out.println("hola4 ");
                   relativeError = ((potential - pdbPotential)/pdbPotential) * 100;
                   //System.out.println("hola5 ");
                   record += "\t\t" + df.format(relativeError);
                   record2 += "\t\t" + df.format(potential);
                   record3 += "\t\t" + df1.format(cratio);
                   //record += "\t" + (relativeError);
                }
                //System.out.println("hola6 ");
                outFile.println(record);
                record2 += "\t\t" + df.format(pdbPotential);
                outFile2.println(record2);
                record3 += "\t\t" + df1.format(pdbCratio);
                outFile3.println(record3);
            }
            outFile.close();
            outFile2.close();
            outFile3.close();
        
            fileOut = new File(PathAlgorithm + "Absolute Accuracy.txt");
            outFile = new PrintWriter(new BufferedWriter(new FileWriter(fileOut)));
            System.out.println("\nAbsolute Accuracy ");
            for (int i = 0; i < alPDBIds.size(); i++)
            {
                outFile.println("\n\nProtein : " + alPDBIds.get(i));
                System.out.println("Protein : " + alPDBIds.get(i));
                outFile.println("METHOD\t\tX1\tX1+2\tX1+2+3\tRSMD");
                pdbStructure = PDBReader.readStructure(alPDBIds.get(i));
                for (int j = 0; j < methods.length; j++)
                {   //System.out.println("METHOD : " + methods[j]);
                    record = methods[j];
                    methodStructure = StructureUtil.readStructure(alPDBIds.get(i), methods[j], PathAlgorithm);
                    for (int z = 0; z < measures.length; z++)
                    {
                        record += "\t" + df.format(StructureUtil.AbsoluteAccuracy(pdbStructure, methodStructure, measures[z]));
                    }
                    record += "\t" + df.format(StructureUtil.RMSD(pdbStructure, methodStructure));
                    outFile.println(record);
                }
            }
            outFile.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(TestAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    
    }
}

    /*
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
    /*
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
                             isCorrect = false;///System.out.println("sale");//System.out.println("correcto : FALSE ");
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
    */
    /*
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
    */
    /*
    private static double RMSD(Structure pdbStructure, Structure methodStructure)
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
    */
    /*
    static public Structure readStructure(String pdbId, String method)
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
                return OpusRotaReader.readStructure(1, pdbId);
            }
            else if (method.equalsIgnoreCase("Scwrl4"))
            {
                return SCWRL4Reader.readStructure(pdbId);
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
            Logger.getLogger(OpusRotaReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return struc;
    }
    */
   



    



