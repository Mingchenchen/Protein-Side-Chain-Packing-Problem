/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.entity;

import java.text.DecimalFormat;

/**
 *
 * @author administrator
 */
public class ResultsEntity {

    private String title;
    private String pdbId;
    private int method = 0;
    private char scopClass = 0;
    private char ecNumber = 0;
    private int aminoAcidId = 0;
    private int cutoff = 0;
    private double[] rmsd = {0,0,0};
    private double[] x1 = {0,0,0};
    private double[] x2 = {0,0,0};
    private double[] x3 = {0,0,0};
    private double[] x4 = {0,0,0};

    public String print() {
        StringBuffer sb = new StringBuffer();
        DecimalFormat df = new DecimalFormat("#0.0000");
        sb.append((getCutoff()==0?"global":getCutoff()) + "\t");
        sb.append((getMethod()==2?"scwrl4":"opus rota") + "\t");
        if (scopClass!=0) sb.append(scopClass + "\t");
        if (ecNumber!=0) sb.append(ecNumber + "\t");
        if (aminoAcidId!=0) sb.append(aminoAcidId + "\t");
        sb.append(getRmsd()[0]+"\t");
        sb.append(df.format(getRmsd()[1])+"\t");
        sb.append(df.format(getRmsd()[2])+"\t");
        sb.append(getX1()[0]+"\t");
        sb.append(df.format(getX1()[1])+"\t");
        sb.append(df.format(getX1()[2])+"\t");
        sb.append(getX2()[0]+"\t");
        sb.append(df.format(getX2()[1])+"\t");
        sb.append(df.format(getX2()[2])+"\t");
        sb.append(getX3()[0]+"\t");
        sb.append(df.format(getX3()[1])+"\t");
        sb.append(df.format(getX3()[2])+"\t");
        sb.append(getX4()[0]+"\t");
        sb.append(df.format(getX4()[1])+"\t");
        sb.append(df.format(getX4()[2])+"\t");
        System.out.println(sb.toString());
        return sb.toString();
    }

    public String getMethodName(){
        return getMethod()==2?"scwrl4":"opus rota";
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the method
     */
    public int getMethod() {
        return method;
    }

    /**
     * @param method the method to set
     */
    public void setMethod(int method) {
        this.method = method;
    }

    /**
     * @return the scopClass
     */
    public char getScopClass() {
        return scopClass;
    }

    /**
     * @param scopClass the scopClass to set
     */
    public void setScopClass(char scopClass) {
        this.scopClass = scopClass;
    }

    /**
     * @return the cutoff
     */
    public int getCutoff() {
        return cutoff;
    }

    /**
     * @param cutoff the cutoff to set
     */
    public void setCutoff(int cutoff) {
        this.cutoff = cutoff;
    }

    /**
     * @return the rmsd
     */
    public double[] getRmsd() {
        return rmsd;
    }

    /**
     * @param rmsd the rmsd to set
     */
    public void setRmsd(double[] rmsd) {
        this.rmsd = rmsd;
    }

    /**
     * @return the x1
     */
    public double[] getX1() {
        return x1;
    }

    /**
     * @param x1 the x1 to set
     */
    public void setX1(double[] x1) {
        this.x1 = x1;
    }

    /**
     * @return the x2
     */
    public double[] getX2() {
        return x2;
    }

    /**
     * @param x2 the x2 to set
     */
    public void setX2(double[] x2) {
        this.x2 = x2;
    }

    /**
     * @return the x3
     */
    public double[] getX3() {
        return x3;
    }

    /**
     * @param x3 the x3 to set
     */
    public void setX3(double[] x3) {
        this.x3 = x3;
    }

    /**
     * @return the x4
     */
    public double[] getX4() {
        return x4;
    }

    /**
     * @param x4 the x4 to set
     */
    public void setX4(double[] x4) {
        this.x4 = x4;
    }

    /**
     * @return the ecNumber
     */
    public char getEcNumber() {
        return ecNumber;
    }

    /**
     * @param ecNumber the ecNumber to set
     */
    public void setEcNumber(char ecNumber) {
        this.ecNumber = ecNumber;
    }

    /**
     * @return the aminoAcidId
     */
    public int getAminoAcidId() {
        return aminoAcidId;
    }

    /**
     * @param aminoAcidId the aminoAcidId to set
     */
    public void setAminoAcidId(int aminoAcidId) {
        this.aminoAcidId = aminoAcidId;
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

}
