/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Algorithm;

/*
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
 */
import org.biojava.bio.structure.Structure;

//import java.util.logging.Logger;
//import java.util.logging.Level;
import pdb.PDBReader;
//import pdb.PDBWrite;
import java.util.ArrayList;
/*
import java.util.Arrays;
import org.biojava.bio.structure.AminoAcid;
import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.Group;
*/
import rotlib.io.RotamerLibraryReader;
import rotlib.RotamerLibraryCollection;
/*
import org.biojava.bio.structure.StructureException;
import org.biojava.bio.structure.Atom;
import org.biojava.bio.structure.Calc;
import utils.ListUtil;
*/
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import utils.StructureUtil;
import java.util.Date;
import java.util.Random;
import java.io.File;
import java.text.DecimalFormat;
import java.io.IOException;

/**
 *
 * @author clezcano
 */
public class Algorithm
{
    static public final String Path = "/home/clezcano/Documents/IvyBioUtils/Algorithm/";
    static public final String STRUCTURE_PATH = Path + "Structures/";
    static public final String COLLISION_PATH = Path + "Statistics/Collision/";
    static public final DecimalFormat DF_EXP_NUM = new DecimalFormat("#00");
    static public final double BOLTZMANN = 3.0;
    //static public final int INITIAL_ITER_PER_CYCLE = 60;
    static public final int INITIAL_ITER_PER_CYCLE = 120;
    //private final static double kvdW  = 5.0;
    private final static double kvdW  = 0.0;
    //private final static double kcratio  = 1000000.0;
    //private final static double kcratio  = 10000000.0;
    private final static double kcratio  = 2250000.0;
    //private final static double kcratio  = 0.0;
    //private final static double krot  = 0.35;
    //private final static double krot  = 3.5;
    private final static double krot  = 0.0;
    static public final boolean INITIAL_LOCAL_SEARCH = true;
    private RotamerLibraryCollection rlc = RotamerLibraryReader.readRotamerLibrary(RotamerLibraryCollection.DUN_IND_ROT_LIB_TYPE);
    private int Iterations_perCycle;
    private double Cooling_velocity, Min_temperature, Max_temperature;
    Random random_var, random_var_int;
    Date date_var;
    File structureDir, collisionDir;
    private boolean LocalSearch;
    Solution actualSolution = null, nextSolution = null, bestSolution = null;

    public Algorithm()
    {
       Iterations_perCycle = INITIAL_ITER_PER_CYCLE;
       LocalSearch = INITIAL_LOCAL_SEARCH;
       Cooling_velocity = 0.85;
       Max_temperature = 5;
       Min_temperature = 0.01;
       random_var = new Random();
       random_var_int = new Random();
       date_var = new Date();
    }
/*
    public void runAlgorithm(ArrayList<String> allPDBIds, int output_folder) throws IOException
    {
        //Solution sol;
        Structure s1, s2;

        //structureDir = new File(STRUCTURE_PATH + DF_EXP_NUM.format(output_folder));
        //collisionDir = new File(COLLISION_PATH + DF_EXP_NUM.format(output_folder));
        //structureDir.mkdirs();
        //collisionDir.mkdirs();
        for (int i = 0; i < allPDBIds.size(); i++)
        {
            System.out.println((i + 1) + ": " + allPDBIds.get(i));
            s1 = PDBReader.readStructure(allPDBIds.get(i));
            s2 = runAlgorithm(s1);
            //System.out.println("Algorithm : " + StructureUtil.getvdWPotential(s2));
            //PDBWrite.writePDB(s2, STRUCTURE_PATH + DF_EXP_NUM.format(output_folder) + "/" + allPDBIds.get(i) + ".chr");
            //StructureUtil.CollisionAnalysis(COLLISION_PATH + DF_EXP_NUM.format(output_folder) + "/" + allPDBIds.get(i) + ".chr", s2);
            //StructureUtil.RMSDAnalysis(Path + "/Statistics/RMSD/" + output_folder + "/" + allPDBIds.get(i) + ".chr", s1, s2);
        }
    }
*/
    /*
    public Structure runAlgorithm(Structure s1) throws IOException
    {
        double actualSolution_energy, nextSolution_energy, temperature;//, actualSolution_vdWPotential, actualSolution_RotamerFrequency;
        int iterations;
       
        File fileOut = new File(Path + "Statistics/Structures/Iterations" + s1.getPDBCode() + ".txt");
        PrintWriter outFile = new PrintWriter(new BufferedWriter(new FileWriter(fileOut)));
        Iterations_perCycle = INITIAL_ITER_PER_CYCLE;
        LocalSearch = INITIAL_LOCAL_SEARCH;
        temperature = Max_temperature;
        random_var.setSeed(date_var.getTime());
        
        actualSolution = Generate_InitialSolution(s1);
        bestSolution = actualSolution;
        while(Temperature_StopCondition(temperature, outFile))
        {
             outFile.println("\nTemperatura: " + temperature + "\n----------------------------------------------------------------------------------");
             System.out.println("\nTemperatura: " + temperature + "\n----------------------------------------------------------------------------------");
             iterations = 0;
             while(Iterations_StopCondition(iterations))
             {
                  System.out.println("Iteracion : " + (iterations + 1) + "\t");
                  outFile.println("Iteracion : " + (iterations + 1) + "\t");
                  //actualSolution_vdWPotential = actualSolution.getvdWPotential();
                  //actualSolution_RotamerFrequency = actualSolution.getRotamerFrequency();
                  //actualSolution_energy =  actualSolution_vdWPotential + krot * actualSolution_RotamerFrequency;
                  actualSolution_energy =  actualSolution.getEnergy();
                  outFile.println("vdW : " + kvdW + " * " + actualSolution.vdWPotential + " + CRatio : " + kcratio + " * " + actualSolution.cRatio + " + FRE : " + krot + " * " + actualSolution.frequency + " = " + actualSolution_energy);
                  System.out.println("vdW : " + kvdW + " * " + actualSolution.vdWPotential + " + CRatio : " + kcratio + " * " + actualSolution.cRatio + " + FRE : " + krot + " * " + actualSolution.frequency + " = " + actualSolution_energy);
                  //outFile.println("vdW : " + actualSolution_vdWPotential + " + ROT : " + krot + " * " + actualSolution_RotamerFrequency + " = " + actualSolution_energy);
                  //System.out.println("vdW : " + actualSolution_vdWPotential + " + ROT : " + krot + " * " + actualSolution_RotamerFrequency + " = " + actualSolution_energy);
                  if (temperature <= 0.0312) //20% de la temperatura
                  {
                     outFile.println("Llego al 20%");
                     System.out.println("Llego al 20%");
                     nextSolution = Choose_neighbord(actualSolution, 1);
                  }
                  else
                  {
                     nextSolution = Choose_neighbord(actualSolution, 2);
                  }
                  nextSolution_energy = nextSolution.getEnergy();
                  //System.out.println(nextSolution_energy);
                  //System.out.println("Random : " + getRandom());
                  if (nextSolution_energy <= actualSolution_energy)
                  {
                     actualSolution = nextSolution;
                     bestSolution = nextSolution;
                  }
                  else if((getRandom() < Math.exp(-(nextSolution_energy - actualSolution_energy) / (BOLTZMANN * temperature))) && LocalSearch)
                  {
                     outFile.println("Metropolis : " + Math.exp(-(nextSolution_energy - actualSolution_energy) / (BOLTZMANN * temperature)));
                     System.out.println("Metropolis : " + Math.exp(-(nextSolution_energy - actualSolution_energy) / (BOLTZMANN * temperature)));
                     actualSolution = nextSolution;
                  }
                  iterations++;
             }
             temperature *= Cooling_velocity;
        }
        printRotamersAndEnergy(bestSolution, outFile);
        outFile.close();
        return StructureUtil.createStructure(s1, bestSolution, rlc);
    }
  */

    public Structure runAlgorithm(Structure s1) throws IOException
    {
        double /*actualSolution_energy, nextSolution_energy,*/ temperature;//, actualSolution_vdWPotential, actualSolution_RotamerFrequency;
        int iterations;

        File fileOut = new File(Path + "Statistics/Structures/Iterations" + s1.getPDBCode() + ".txt");
        PrintWriter outFile = new PrintWriter(new BufferedWriter(new FileWriter(fileOut)));
        Iterations_perCycle = INITIAL_ITER_PER_CYCLE;
        LocalSearch = INITIAL_LOCAL_SEARCH;
        temperature = Max_temperature;
        random_var.setSeed(date_var.getTime());
        random_var_int.setSeed(date_var.getTime());

        actualSolution = Generate_InitialSolution(s1);
        actualSolution.getEnergy();
        bestSolution = actualSolution;
        printRotamersAndEnergy(bestSolution, outFile);
        
        while(Temperature_StopCondition(temperature, outFile))
        {
             outFile.println("\nTemperatura: " + temperature + "\n----------------------------------------------------------------------------------");
             System.out.println("\nTemperatura: " + temperature + "\n----------------------------------------------------------------------------------");
             iterations = 0;
             while(Iterations_StopCondition(iterations))
             {
                  System.out.println("Iteracion : " + (iterations + 1) + "\t");
                  outFile.println("Iteracion : " + (iterations + 1) + "\t");
                  //actualSolution_vdWPotential = actualSolution.getvdWPotential();
                  //actualSolution_RotamerFrequency = actualSolution.getRotamerFrequency();
                  //actualSolution_energy =  actualSolution_vdWPotential + krot * actualSolution_RotamerFrequency;
                  //actualSolution_energy =  actualSolution.getEnergy();
                  outFile.println("vdW : " + kvdW + " * " + actualSolution.vdWPotential + " + CRatio : " + kcratio + " * " + actualSolution.cRatio + " + FRE : " + krot + " * " + actualSolution.frequency + " = " + actualSolution.energy);
                  System.out.println("vdW : " + kvdW + " * " + actualSolution.vdWPotential + " + CRatio : " + kcratio + " * " + actualSolution.cRatio + " + FRE : " + krot + " * " + actualSolution.frequency + " = " + actualSolution.energy);
                  //outFile.println("vdW : " + actualSolution_vdWPotential + " + ROT : " + krot + " * " + actualSolution_RotamerFrequency + " = " + actualSolution_energy);
                  //System.out.println("vdW : " + actualSolution_vdWPotential + " + ROT : " + krot + " * " + actualSolution_RotamerFrequency + " = " + actualSolution_energy);
                  
                  if (temperature <= 0.0312) //20% de la temperatura
                  {
                     //outFile.println("Llego al 20%");
                     //System.out.println("Llego al 20%");
                     nextSolution = Choose_neighbord(actualSolution, 1);
                  }
                  else
                  {
                     nextSolution = Choose_neighbord(actualSolution, 2);
                  }
                  //nextSolution_energy = nextSolution.getEnergy();
                  //nextSolution = Choose_neighbord(actualSolution);
                  //nextSolution = Choose_neighbord(actualSolution, 1);
                  nextSolution.getEnergy();
                  //System.out.println("NEXT vdW : " + kvdW + " * " + nextSolution.vdWPotential + " + CRatio : " + kcratio + " * " + nextSolution.cRatio + " + FRE : " + krot + " * " + nextSolution.frequency + " = " + nextSolution.energy);
                  //System.out.println(nextSolution_energy);
                  //System.out.println("Random : " + getRandom());
                  if (nextSolution.energy <= actualSolution.energy)
                  {  //System.out.println("SI");
                     actualSolution = nextSolution;
                     bestSolution = nextSolution;
                  }
                  else if((getRandom() < Math.exp(-(nextSolution.energy - actualSolution.energy) / (BOLTZMANN * temperature))) && LocalSearch)
                  {
                     outFile.println("Metropolis : " + Math.exp(-(nextSolution.energy - actualSolution.energy) / (BOLTZMANN * temperature)));
                     System.out.println("Metropolis : " + Math.exp(-(nextSolution.energy - actualSolution.energy) / (BOLTZMANN * temperature)));
                     actualSolution = nextSolution;
                  }
                  iterations++;
             }
             temperature *= Cooling_velocity;
        }
        //printRotamersAndEnergy(bestSolution, outFile);
        //StructureUtil.cisRR(bestSolution, s1, rlc);
        printRotamersAndEnergy(bestSolution, outFile);
        outFile.close();
        return StructureUtil.createStructure(s1, bestSolution, rlc);
    }

    private void printRotamersAndEnergy(Solution sol, PrintWriter outFile)
    {
        outFile.println("\nBest Solution Rotamers and Energy : ");
        System.out.println("\nBest Solution Rotamers and Energy : ");
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

    private double getRandom()
    {
        return random_var.nextDouble();
    }

    private Solution Choose_neighbord(Solution actualSolution, int numPivots)
    {
        Solution sol = actualSolution.CopySolution();
        sol.Generate_NeighbordSolution(numPivots, random_var_int);
        return sol;
    }

    /*
    private Solution Choose_neighbord(Solution actualSolution)
    {
        Solution sol = actualSolution.CopySolution();
        sol.Generate_NeighbordSolution();
        return sol;
    }
*/
    private boolean Temperature_StopCondition(double temperature, PrintWriter outFile)
    {
        if(temperature > Min_temperature)
        {
            return true;
        }
        else if(LocalSearch)
        {
            Iterations_perCycle = 500;
            LocalSearch = false;
            printRotamersAndEnergy(bestSolution, outFile);
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean Iterations_StopCondition(int iterations)
    {
        if(iterations < Iterations_perCycle)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private Solution Generate_InitialSolution(Structure s1)
    {
        Solution sol = new Solution(s1);
        sol.Generate_RandomRotamers(random_var_int);
        //sol.Generate_RandomRotamers();
        //sol.setMaxProb();
        //sol.AssignLastRotamer();
        return sol;
    }
    
    
    
    
}





