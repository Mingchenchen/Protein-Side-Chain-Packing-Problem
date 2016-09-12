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
public class SCOPSuperfamily {

    private int id;
    private SCOPFold parent;
    private int sunid;
    private int sccs;
    private String description;
    private ArrayList<SCOPFamily> families;

    public SCOPSuperfamily(SCOPFold parent) {
        this.parent = parent;
        this.parent.addSuperfamily(this);
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
    public SCOPFold getParent() {
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
     * @return the sccs
     */
    public int getSccs() {
        return sccs;
    }

    /**
     * @param sccs the sccs to set
     */
    public void setSccs(int sccs) {
        this.sccs = sccs;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the families
     */
    public ArrayList<SCOPFamily> getFamilies() {
        return families;
    }

    public void addFamily(SCOPFamily family) {
        families = families == null ? new ArrayList<SCOPFamily>() : families;
        families.add(family);
    }
}
