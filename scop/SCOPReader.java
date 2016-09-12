/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scop;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import scop.db.SCOPInserts;
import scop.entity.PDBSection;
import scop.entity.SCOPClass;
import scop.entity.SCOPDomain;
import scop.entity.SCOPFamily;
import scop.entity.SCOPFold;
import scop.entity.SCOPProtein;
import scop.entity.SCOPSpecies;
import scop.entity.SCOPSuperfamily;

/**
 *
 * @author administrator
 */
public class SCOPReader {

    static private final String DEFAULT_PATH = "/home/administrator/Documents/scop";
    static private final String DES_FILE = DEFAULT_PATH + "/dir.des.scop.txt_1.75";
    static private final String CLA_FILE = DEFAULT_PATH + "/dir.cla.scop.txt_1.75";
    static private final String HIE_FILE = DEFAULT_PATH + "/dir.hie.scop.txt_1.75";

    static public void readSCOPDescriptionFile() {
        FileInputStream fis = null;
        DataInputStream dis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        SCOPInserts scopInserts = new SCOPInserts();
        try {
            fis = new FileInputStream(DES_FILE);
            dis = new DataInputStream(fis);
            isr = new InputStreamReader(dis);
            br = new BufferedReader(isr);
            String strLine = null;
            SCOPClass cl = null;
            SCOPFold cf = null;
            SCOPSuperfamily sf = null;
            SCOPFamily fa = null;
            SCOPProtein dm = null;
            SCOPSpecies sp = null;
            SCOPDomain px = null;
            int id = 0;
            while ((strLine = br.readLine()) != null) {
                if (strLine.charAt(0) != '#') {
                    String[] fields = strLine.split("\t");
                    if (fields[1].equalsIgnoreCase("cl")) {
                        cl = new SCOPClass();
                        cl.setSunid(Integer.parseInt(fields[0]));
                        cl.setSccs(fields[2].charAt(0));
                        cl.setDescription(fields[4].replace("\"", "\\\""));
                        id = scopInserts.insertSCOPClass(cl);
                        cl.setId(id);
                    } else if (fields[1].equalsIgnoreCase("cf")) {
                        cf = new SCOPFold(cl);
                        cf.setSunid(Integer.parseInt(fields[0]));
                        cf.setSccs(Integer.parseInt(fields[2].substring(fields[2].lastIndexOf('.') + 1, fields[2].length())));
                        cf.setDescription(fields[4].replace("\"", "\\\""));
                        id = scopInserts.insertSCOPFold(cf);
                        cf.setId(id);
                    } else if (fields[1].equalsIgnoreCase("sf")) {
                        sf = new SCOPSuperfamily(cf);
                        sf.setSunid(Integer.parseInt(fields[0]));
                        sf.setSccs(Integer.parseInt(fields[2].substring(fields[2].lastIndexOf('.') + 1, fields[2].length())));
                        sf.setDescription(fields[4].replace("\"", "\\\""));
                        id = scopInserts.insertSCOPSuperfamily(sf);
                        sf.setId(id);
                    } else if (fields[1].equalsIgnoreCase("fa")) {
                        fa = new SCOPFamily(sf);
                        fa.setSunid(Integer.parseInt(fields[0]));
                        fa.setSccs(Integer.parseInt(fields[2].substring(fields[2].lastIndexOf('.') + 1, fields[2].length())));
                        fa.setDescription(fields[4].replace("\"", "\\\""));
                        id = scopInserts.insertSCOPFamily(fa);
                        fa.setId(id);
                    } else if (fields[1].equalsIgnoreCase("dm")) {
                        dm = new SCOPProtein(fa);
                        dm.setSunid(Integer.parseInt(fields[0]));
                        dm.setDescription(fields[4].replace("\"", "\\\""));
                        id = scopInserts.insertSCOPProtein(dm);
                        dm.setId(id);
                    } else if (fields[1].equalsIgnoreCase("sp")) {
                        sp = new SCOPSpecies(dm);
                        sp.setSunid(Integer.parseInt(fields[0]));
                        String desc = fields[4];
                        if (desc.contains("TaxId")) {
                            String taxid = desc.substring(desc.lastIndexOf(":") + 2, desc.length() - 1);
                            sp.setTaxid(Integer.parseInt(taxid));
                            desc = desc.substring(0, desc.indexOf('[')).trim();
                        }
                        sp.setDescription(desc.replace("\"", "\\\""));
                        id = scopInserts.insertSCOPSpecies(sp);
                        sp.setId(id);
                    } else if (fields[1].equalsIgnoreCase("px")) {
                        px = new SCOPDomain(sp);
                        px.setSunid(Integer.parseInt(fields[0]));
                        px.setSid(fields[3]);
                        id = scopInserts.insertSCOPDomain(px);
                        px.setId(id);
                        String desc = fields[4];
                        String pdbId = desc.substring(0, 4);
                        String[] pdbSections = desc.substring(5, desc.length()).split(",");
                        for (int i = 0; i < pdbSections.length; i++) {
                            PDBSection px1 = new PDBSection(px);
                            px1.setPdbId(pdbId);
                            px1.setChain(pdbSections[i].charAt(0));
                            if (pdbSections[i].length() > 2) {
                                px1.setIsAllChain(false);
                                String temp = pdbSections[i].substring(2, pdbSections[i].length());
                                String resnum1 = temp.substring(0, temp.indexOf("-", 1));
                                String resnum2 = temp.substring(temp.indexOf("-", 1) + 1, temp.length());
                                px1.setResNumStart(resnum1);
                                px1.setResNumEnd(resnum2);
                            } else {
                                px1.setIsAllChain(true);
                            }
                            scopInserts.insertPDBSection(px1);
                        }
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(SCOPReader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(SCOPReader.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                isr.close();
            } catch (IOException ex) {
                Logger.getLogger(SCOPReader.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                dis.close();
            } catch (IOException ex) {
                Logger.getLogger(SCOPReader.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(SCOPReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    static public void main(String[] args) {
        readSCOPDescriptionFile();
    }
}
