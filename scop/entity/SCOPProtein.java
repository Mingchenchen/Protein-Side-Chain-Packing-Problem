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
public class SCOPProtein {

    private int id;
    private SCOPFamily parent;
    private int sunid;
    private String description;
    private ArrayList<SCOPSpecies> species;

    public SCOPProtein(SCOPFamily parent) {
        this.parent = parent;
        this.parent.addProtein(this);
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
    public SCOPFamily getParent() {
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
     * @return the species
     */
    public ArrayList<SCOPSpecies> getSpecies() {
        return species;
    }

    public void addSpecies(SCOPSpecies species) {
        this.species = this.species == null ? new ArrayList<SCOPSpecies>() : this.species;
        this.species.add(species);
    }
}
