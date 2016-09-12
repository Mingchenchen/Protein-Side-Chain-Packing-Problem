/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dssp;

/**
 *
 * @author rcorona
 */
public class DSSPResidue {

    private String record;
    private int id;
    private String residueCode;
    private char chain;
    private char aminoType;
    private String secStructure;
    private String bp1;
    private String bp2;
    private char sheetLabel;
    private int acc;
    private String nho1;
    private String ohn1;
    private String nho2;
    private String ohn2;
    private double tco,kappa,alpha;
    private double phi,psi;
    private double[] ca = new double[3];
    private DSSPEntity parent;

    public DSSPResidue(DSSPEntity parent, String record) {
        this.parent = parent;
        this.record = record;
        id = Integer.parseInt(record.substring(0,5).trim());
        residueCode = record.substring(5,11).trim();
        chain = record.charAt(11);
        aminoType = record.charAt(13);
        secStructure = record.substring(14,25);
        bp1 = record.substring(25,29).trim();
        bp2 = record.substring(29,33).trim();
        sheetLabel = record.charAt(33);
        acc = Integer.parseInt(record.substring(34, 38).trim());
        nho1 = record.substring(38,50).trim();
        ohn1 = record.substring(50, 61).trim();
        nho2 = record.substring(61,72).trim();
        ohn2 = record.substring(72, 83).trim();
        tco = Double.parseDouble(record.substring(83, 91).trim());
        kappa = Double.parseDouble(record.substring(91, 97).trim());
        alpha = Double.parseDouble(record.substring(97, 103).trim());
        phi = Double.parseDouble(record.substring(103, 109).trim());
        psi = Double.parseDouble(record.substring(109, 115).trim());
        ca[0] = Double.parseDouble(record.substring(115, 122).trim());
        ca[1] = Double.parseDouble(record.substring(122, 129).trim());
        ca[2] = Double.parseDouble(record.substring(129, 136).trim());
    }

    /**
     * @return the record
     */
    public String getRecord() {
        return record;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the residueCode
     */
    public String getResidueCode() {
        return residueCode;
    }

    /**
     * @return the chain
     */
    public char getChain() {
        return chain;
    }

    /**
     * @return the aminoType
     */
    public char getAminoType() {
        return aminoType;
    }

    /**
     * @return the secStructure
     */
    public String getSecStructure() {
        return secStructure;
    }

    /**
     * @return the bp1
     */
    public String getBp1() {
        return bp1;
    }

    /**
     * @return the bp2
     */
    public String getBp2() {
        return bp2;
    }

    /**
     * @return the sheetLabel
     */
    public char getSheetLabel() {
        return sheetLabel;
    }

    /**
     * @return the acc
     */
    public int getAcc() {
        return acc;
    }

    /**
     * @return the nho1
     */
    public String getNho1() {
        return nho1;
    }

    /**
     * @return the ohn1
     */
    public String getOhn1() {
        return ohn1;
    }

    /**
     * @return the nho2
     */
    public String getNho2() {
        return nho2;
    }

    /**
     * @return the ohn2
     */
    public String getOhn2() {
        return ohn2;
    }

    /**
     * @return the tco
     */
    public double getTco() {
        return tco;
    }

    /**
     * @return the kappa
     */
    public double getKappa() {
        return kappa;
    }

    /**
     * @return the alpha
     */
    public double getAlpha() {
        return alpha;
    }

    /**
     * @return the phi
     */
    public double getPhi() {
        return phi;
    }

    /**
     * @return the psi
     */
    public double getPsi() {
        return psi;
    }

    /**
     * @return the ca
     */
    public double[] getCa() {
        return ca;
    }

    /**
     * @return the parent
     */
    public DSSPEntity getParent() {
        return parent;
    }
}
