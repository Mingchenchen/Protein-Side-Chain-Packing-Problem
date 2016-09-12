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
public class SCOPClass {

    private int id;
    private int sunid;
    private char sccs;
    private String description;
    private ArrayList<SCOPFold> folds;

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
    public char getSccs() {
        return sccs;
    }

    /**
     * @param sccs the sccs to set
     */
    public void setSccs(char sccs) {
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
     * @return the folds
     */
    public ArrayList getFolds() {
        return folds;
    }

    public void addFold(SCOPFold fold) {
        folds = folds == null ? new ArrayList<SCOPFold>() : folds;
        folds.add(fold);
    }
}
