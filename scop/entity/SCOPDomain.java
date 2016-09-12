/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scop.entity;

import java.util.ArrayList;

/**
 *
 * @author administrator
 */
public class SCOPDomain {

    private int id;
    private SCOPSpecies parent;
    private int sunid;
    private String sid;
    private ArrayList<PDBSection> pdbSections;

    public SCOPDomain(SCOPSpecies parent) {
        this.parent = parent;
        this.parent.addDomain(this);
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the parent
     */
    public SCOPSpecies getParent() {
        return parent;
    }

    /**
     * @return the sunid
     */
    public int getSunid() {
        return sunid;
    }

    /**
     * @param sunid the sunid to set
     */
    public void setSunid(int sunid) {
        this.sunid = sunid;
    }

    /**
     * @return the sid
     */
    public String getSid() {
        return sid;
    }

    /**
     * @param sid the sid to set
     */
    public void setSid(String sid) {
        this.sid = sid;
    }

    /**
     * @return the pdbSections
     */
    public ArrayList<PDBSection> getPDBSections() {
        return pdbSections;
    }

    public void addPDBSection(PDBSection pdbSection) {
        pdbSections = pdbSections == null ? new ArrayList<PDBSection>() : pdbSections;
        pdbSections.add(pdbSection);
    }
}
