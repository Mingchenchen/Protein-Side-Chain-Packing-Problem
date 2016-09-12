/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opusRota;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import pdb.PDBReader;

/**
 *
 * @author ivetth
 */
public class OpusRotaRun
{
    static public final String APP_PATH = "/home/clezcano/Documents/OPUS_Rota";
    static public final String OUT_PATH = APP_PATH + "/pdb_out";
    static public final String LOG_PATH = APP_PATH + "/log_files";
    static public final DecimalFormat DF_EXP_NUM = new DecimalFormat("#00");

    static public void runOpusRota(ArrayList<String> alPDBIds, int expNumber)
    {
        writeScriptFile();
        writePDBList(alPDBIds);        
        File outDir = new File(OUT_PATH + File.separator + DF_EXP_NUM.format(expNumber));
        File logDir = new File(LOG_PATH + File.separator + DF_EXP_NUM.format(expNumber));
        outDir.mkdirs();
        logDir.mkdirs();
        writeConfigFile(outDir.getPath());
        writeLogFile(logDir.getPath(), runOpusRota());
    }

    static private void writeConfigFile(String outPath)
    {
        FileWriter fstream = null;
        try
        {
            File f = new File(APP_PATH + File.separator + "config.rota");
            if (!f.exists())
            {
                try
                {
                    f.createNewFile();
                }
                catch (IOException ex)
                {
                    Logger.getLogger(OpusRotaRun.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            fstream = new FileWriter(f);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("pdbdir = " + PDBReader.DEFAULT_PATH + File.separator + "\n");
            out.write("energy_dir = ./data/\n");
            out.write("filelist = opus_rota.lst\n");
            out.write("out_dir = " + outPath + File.separator + "\n");
            out.write("t_run = 100\n");
            out.write("temp = 5.\n");
            out.write("r_rot =  2.00\n");
            out.write("r_lj =  2.00\n");
            out.write("r_psp =  0.30\n");
            out.write("r_sas =  0.20\n");
            out.write("r_repul = 1.6\n");
            out.write("r_clash = 0.7\n");
            out.write("tor = 40.\n");
            out.write("sas_cutoff = 0.17\n");
            out.write("nkeep = 0\n");
            out.write("nflex = 0\n");
            out.close();
        } 
        catch (IOException ex)
        {
            Logger.getLogger(OpusRotaRun.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                fstream.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(OpusRotaRun.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    static private void writePDBList(ArrayList<String> alPDBIds)
    {
        FileWriter fstream = null;
        try
        {
            File f = new File(PDBReader.DEFAULT_PATH + File.separator + "opus_rota.lst");
            if (!f.exists())
            {
                try
                {
                    f.createNewFile();
                }
                catch (IOException ex)
                {
                    Logger.getLogger(OpusRotaRun.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            fstream = new FileWriter(f);
            BufferedWriter out = new BufferedWriter(fstream);
            for (int i = 0; i < alPDBIds.size(); i++)
            {
                out.write(alPDBIds.get(i) + ".pdb\n");
            }
            out.close();
        } 
        catch (IOException ex)
        {
            Logger.getLogger(OpusRotaRun.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                fstream.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(OpusRotaRun.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    static private String runOpusRota()
    {
        StringBuilder sb = new StringBuilder();
        try
        {
            String[] commandArray = {"./ivy_opus_rota"};
            Process p = Runtime.getRuntime().exec(commandArray);
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null)
            {
                sb.append(line);
                sb.append("\n");
                System.out.println(line);
            }
        } 
        catch (IOException ex)
        {
            Logger.getLogger(OpusRotaRun.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb.toString();
    }

    static private void writeLogFile(String logPath, String log)
    {
        try {
            File f = null;
            PrintWriter outFile = null;
            f = new File(logPath + File.separator + (new SimpleDateFormat("yyMMddHHmmssSSS")).format(new Date()) + ".log");
            if (!f.exists()) {
                try {
                    f.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(OpusRotaRun.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            outFile = new PrintWriter(new BufferedWriter(new FileWriter(f)));
            outFile.print(log);
            outFile.close();
        } catch (IOException ex) {
            Logger.getLogger(OpusRotaRun.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static private void writeScriptFile()
    {
        FileWriter fstream = null;
        try
        {
            File f = new File("ivy_opus_rota");
            if (!f.exists())
            {
                try
                {
                    f.createNewFile();
                }
                catch (IOException ex)
                {
                    Logger.getLogger(OpusRotaRun.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            f.setExecutable(true);
            fstream = new FileWriter(f);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("cd " + APP_PATH.replace(" ", "\\ ") + "\n");
            out.write("./opus_rota");
            out.close();
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(OpusRotaRun.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                fstream.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(OpusRotaRun.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
