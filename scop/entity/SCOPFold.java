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
public class SCOPFold {

    private int id;
    private SCOPClass parent;
    private int sunid;
    private int sccs;
    private String description;
    private ArrayList<SCOPSuperfamily> superfamilies;

    public SCOPFold(SCOPClass parent) {
        this.parent = parent;
        this.parent.addFold(this);
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
    public SCOPClass getParent() {
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
     * @return the superfamilies
     */
    public ArrayList<SCOPSuperfamily> getSuperfamilies() {
        return superfamilies;
    }

    public void addSuperfamily(SCOPSuperfamily superfamily) {
        superfamilies = superfamilies == null ? new ArrayList<SCOPSuperfamily>() : superfamilies;
        superfamilies.add(superfamily);
    }
}
