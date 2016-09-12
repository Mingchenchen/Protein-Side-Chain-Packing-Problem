/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dssp;

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
public class DSSPReader {

    static private final String DEFAULT_PATH = "/home/administrator/Documents/dssp";
    static private final String START_LINE = "  #  RESIDUE AA STRUCTURE BP1 BP2  ACC     N-H-->O    O-->H-N    N-H-->O    O-->H-N    TCO  KAPPA ALPHA  PHI   PSI    X-CA   Y-CA   Z-CA ";

    static public DSSPEntity readDSSPFile(String pdbId) {
        DSSPEntity result = null;
        FileInputStream fis = null;
        DataInputStream dis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            fis = new FileInputStream(DEFAULT_PATH + "/" + pdbId.toLowerCase() + ".dssp");
            dis = new DataInputStream(fis);
            isr = new InputStreamReader(dis);
            br = new BufferedReader(isr);
            String strLine = null;
            boolean isResidueLine = false;
            while ((strLine = br.readLine()) != null) {
                if (isResidueLine && !strLine.contains("!")) {
                    result = result == null ? new DSSPEntity(pdbId) : result;
                    result.addDSSPResidue(strLine);
                }
                if (strLine.equalsIgnoreCase(START_LINE)) {
                    isResidueLine = true;
                }
            }
            br.close();
            isr.close();
            dis.close();
            fis.close();
        } catch (IOException ex) {
            Logger.getLogger(DSSPReader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (br!=null) br.close();
            } catch (IOException ex) {
                Logger.getLogger(DSSPReader.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (isr!=null) isr.close();
            } catch (IOException ex) {
                Logger.getLogger(DSSPReader.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (dis!=null) dis.close();
            } catch (IOException ex) {
                Logger.getLogger(DSSPReader.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (fis!=null) fis.close();
            } catch (IOException ex) {
                Logger.getLogger(DSSPReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
}
