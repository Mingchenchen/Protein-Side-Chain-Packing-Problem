/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scwrl4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import pdb.PDBReader;

/**
 *
 * @author rcorona
 */
public class SCWRL4Run
{

    static public final String APP_PATH = "/home/clezcano/Documents/scwrl4";
    static public final String OUT_PATH = APP_PATH + "/pdb_out";
    static public final String LOG_PATH = APP_PATH + "/log_files";

    static public void runSCWRL4(ArrayList<String> alPDBIds)
    {

        for (int i = 0; i < alPDBIds.size(); i++)
        {
            if (!(new File(APP_PATH)).exists())
            {
                new IOException("The application path for SCWRL4 does not exist.");
            } 
            else
            {
                (new File(OUT_PATH)).mkdirs();
                (new File(LOG_PATH)).mkdirs();
            }

            FileWriter fstream = null;
            try
            {
                File f = new File("ivy_scwrl4");
                if (!f.exists())
                {
                    try
                    {
                        f.createNewFile();
                    } 
                    catch (IOException ex)
                    {
                        Logger.getLogger(SCWRL4Run.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                f.setExecutable(true);
                fstream = new FileWriter("ivy_scwrl4");
                BufferedWriter out = new BufferedWriter(fstream);
                out.write("cd " + APP_PATH + "\n");
                out.write("./Scwrl4 -i " + PDBReader.DEFAULT_PATH + File.separator + alPDBIds.get(i).toLowerCase() + ".pdb -o " + OUT_PATH + File.separator + alPDBIds.get(i).toLowerCase() + ".out -h > " + LOG_PATH + File.separator + alPDBIds.get(i).toLowerCase() + ".log\n");
                out.close();

                String[] commandArray = {"./ivy_scwrl4"};
                long initialTime = System.currentTimeMillis();
                Process p = Runtime.getRuntime().exec(commandArray);
                long finalTime = System.currentTimeMillis();
                BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = null;
                while ((line = in.readLine()) != null)
                {
                    System.out.println(line);
                }
                System.out.println("SCWRL4 - " + alPDBIds.get(i).toLowerCase() + " : " + (finalTime - initialTime) + "ms");

            }
            catch (IOException ex)
            {
                Logger.getLogger(SCWRL4Run.class.getName()).log(Level.SEVERE, null, ex);
            } 
            finally
            {
                try
                {
                    fstream.close();
                }
                catch (IOException ex)
                {
                    Logger.getLogger(SCWRL4Run.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
