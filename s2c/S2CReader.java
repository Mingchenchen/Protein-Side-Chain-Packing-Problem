/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package s2c;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rcorona
 */
public class S2CReader
{

    static private final String DEFAULT_PATH = "/home/clezcano/Documents/s2c";

    static public S2CEntity readS2CFile(String pdbId)
    {
        S2CEntity result = null;
        FileInputStream fis = null;
        DataInputStream dis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try
        {
            fis = new FileInputStream(DEFAULT_PATH + "/" + pdbId.toLowerCase() + ".sc");
            dis = new DataInputStream(fis);
            isr = new InputStreamReader(dis);
            br = new BufferedReader(isr);
            String strLine = null;
            while ((strLine = br.readLine()) != null)
            {
                if (strLine.startsWith("HEADSC"))
                {
                    result = new S2CEntity(strLine.substring(7, 11));
                } 
                else if (strLine.startsWith("SEQCRD"))
                {
                    result.addSeqCrdRecord(strLine);
                } 
                else if (strLine.startsWith("PARAME"))
                {
                    result.addParameter(strLine);
                }
            }
            br.close();
            isr.close();
            dis.close();
            fis.close();
        } 
        catch (IOException ex)
        {
            Logger.getLogger(S2CReader.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally
        {
            try
            {
                if (br != null)
                {
                    br.close();
                }
            } 
            catch (IOException ex)
            {
                Logger.getLogger(S2CReader.class.getName()).log(Level.SEVERE, null, ex);
            }
            try
            {
                if (isr != null)
                {
                    isr.close();
                }
            } 
            catch (IOException ex)
            {
                Logger.getLogger(S2CReader.class.getName()).log(Level.SEVERE, null, ex);
            }
            try
            {
                if (dis != null)
                {
                    dis.close();
                }
            } 
            catch (IOException ex)
            {
                Logger.getLogger(S2CReader.class.getName()).log(Level.SEVERE, null, ex);
            }
            try
            {
                if (fis != null)
                {
                    fis.close();
                }
            }
            catch (IOException ex)
            {
                Logger.getLogger(S2CReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
}
