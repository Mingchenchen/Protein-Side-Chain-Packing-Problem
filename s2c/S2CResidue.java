/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package s2c;

import java.util.logging.Level;
import java.util.logging.Logger;
import utils.ListUtil;

/**
 *
 * @author rcorona
 */
public class S2CResidue
{

    private String record;
    private char chain;
    private char oneLetterResCode;
    private String seqresThreeLetterCode;
    private String atomThreeLetterCode;
    private int seqresResNum;
    private String atomResNum;
    private char pdbSS;
    private char strideSS;
    private String errorFlags;
    private S2CEntity parent;

    public S2CResidue(S2CEntity parent, String record)
    {
        this.parent = parent;
        this.record = record;
        chain = record.charAt(7);
        oneLetterResCode = record.charAt(9);
        seqresThreeLetterCode = record.substring(11, 14);
        atomThreeLetterCode = record.substring(15, 18);


        try
        {
            seqresResNum = Integer.parseInt(record.substring(19, 24).trim());
        } 
        catch (NumberFormatException ex)
        {
            Logger.getLogger(S2CReader.class.getName()).log(Level.SEVERE, null, ex);
            seqresResNum = -1;
        }
        atomResNum = record.substring(25, 31).trim();
        pdbSS = record.charAt(32);//PDB secondary structure
        strideSS = record.charAt(34);
        errorFlags = record.substring(36);
    }

    public boolean isStdAACode()
    {
        return getErrorFlags().indexOf('1') == -1;
    }

    public boolean seqresAtomNamesEqual()
    {
        return getErrorFlags().indexOf('2') == -1;
    }

    public boolean hasAtomRecord()
    {
        return getErrorFlags().indexOf('3') == -1;
    }

    public boolean pdbStrideSSEqual()
    {
        return getErrorFlags().indexOf('4') == -1;////ATENCION  SEQRES and ATOM residue numbers differ

    }

    public boolean hasPdbSS()
    {
        return getErrorFlags().indexOf('5') == -1;////ATENCION  PDB and STRIDE secondary structures differ

    }

    public boolean hasStrideSS()
    {
        return getErrorFlags().indexOf('6') == -1;////ATENCION  PDB secondary structure is absent

    }
                                                  ////ATENCION  Falta el error 7
    /**
     * @return the record
     */
    public String getRecord()
    {
        return record;
    }

    /**
     * @return the chain
     */
    public char getChain()
    {
        return chain;
    }

    /**
     * @return the oneLetterResCode
     */
    public char getOneLetterResCode()
    {
        return oneLetterResCode;
    }

    /**
     * @return the seqresThreeLetterCode
     */
    public String getSeqresThreeLetterCode()
    {
        return seqresThreeLetterCode;
    }

    /**
     * @return the atomThreeLetterCode
     */
    public String getAtomThreeLetterCode()
    {
        return atomThreeLetterCode;
    }

    /**
     * @return the seqresResNum
     */
    public int getSeqresResNum()
    {
        return seqresResNum;
    }

    /**
     * @return the atomResNum
     */
    public String getAtomResNum()
    {
        return atomResNum;
    }

    /**
     * @return the pdbSS
     */
    public char getPdbSS()
    {
        return pdbSS;
    }

    /**
     * @return the strideSS
     */
    public char getStrideSS()
    {
        return strideSS;
    }

    /**
     * @return the errorFlags
     */
    public String getErrorFlags()
    {
        return errorFlags;
    }

    /**
     * @return the parent
     */
    public S2CEntity getParent()
    {
        return parent;
    }

    public boolean isAAstandard()
    {
        return ListUtil.isAminoAcid(seqresThreeLetterCode);
    }


}
