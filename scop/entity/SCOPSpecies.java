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
public class SCOPSpecies {

    private int id;
    private SCOPProtein parent;
    private int sunid;
    private String description;
    private int taxid;
    private ArrayList<SCOPDomain> domains;

    public SCOPSpecies(SCOPProtein parent) {
        this.parent = parent;
        this.parent.addSpecies(this);
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
    public SCOPProtein getParent() {
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
     * @return the taxid
     */
    public int getTaxid() {
        return taxid;
    }

    /**
     * @param taxid the taxid to set
     */
    public void setTaxid(int taxid) {
        this.taxid = taxid;
    }

    /**
     * @return the domains
     */
    public ArrayList<SCOPDomain> getDomains() {
        return domains;
    }

    public void addDomain(SCOPDomain domain) {
        domains = domains == null ? new ArrayList<SCOPDomain>() : domains;
        domains.add(domain);
    }
}
