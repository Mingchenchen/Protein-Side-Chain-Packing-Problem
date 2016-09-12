/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scop.entity;

/**
 *
 * @author administrator
 */
public class PDBSection {

    private int id;
    private SCOPDomain parent;
    private String pdbId;
    private char chain;
    private String resNumStart = "0";
    private String resNumEnd = "0";
    private boolean isAllChain;

    public PDBSection(SCOPDomain parent) {
        this.parent = parent;
        this.parent.addPDBSection(this);
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
    public SCOPDomain getParent() {
        return parent;
    }

    /**
     * @return the pdbId
     */
    public String getPdbId() {
        return pdbId;
    }

    /**
     * @param pdbId the pdbId to set
     */
    public void setPdbId(String pdbId) {
        this.pdbId = pdbId;
    }

    /**
     * @return the chain
     */
    public char getChain() {
        return chain;
    }

    /**
     * @param chain the chain to set
     */
    public void setChain(char chain) {
        this.chain = chain;
    }

    /**
     * @return the isAllChain
     */
    public boolean isIsAllChain() {
        return isAllChain;
    }

    /**
     * @param isAllChain the isAllChain to set
     */
    public void setIsAllChain(boolean isAllChain) {
        this.isAllChain = isAllChain;
    }

    /**
     * @return the resNumStart
     */
    public String getResNumStart() {
        return resNumStart;
    }

    /**
     * @param resNumStart the resNumStart to set
     */
    public void setResNumStart(String resNumStart) {
        this.resNumStart = resNumStart;
    }

    /**
     * @return the resNumEnd
     */
    public String getResNumEnd() {
        return resNumEnd;
    }

    /**
     * @param resNumEnd the resNumEnd to set
     */
    public void setResNumEnd(String resNumEnd) {
        this.resNumEnd = resNumEnd;
    }
}
