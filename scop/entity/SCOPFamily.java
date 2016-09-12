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
public class SCOPFamily {

    private int id;
    private SCOPSuperfamily parent;
    private int sunid;
    private int sccs;
    private String description;
    private ArrayList<SCOPProtein> proteins;

    public SCOPFamily(SCOPSuperfamily parent) {
        this.parent = parent;
        this.parent.addFamily(this);
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
    public SCOPSuperfamily getParent() {
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
     * @return the proteins
     */
    public ArrayList<SCOPProtein> getProteins() {
        return proteins;
    }

    public void addProtein(SCOPProtein protein) {
        proteins = proteins == null ? new ArrayList<SCOPProtein>() : proteins;
        proteins.add(protein);
    }
}
