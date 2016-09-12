/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package s2c;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rcorona
 */
public class S2CEntity
{

    private String headSC;
    private ArrayList<S2CResidue> seqCrdRecords;
    private String method;
    private double resolution;
    private double rFactor;
    private double bFactor;

    public S2CEntity(String headSC)
    {
        this.headSC = headSC;
    }

    public void addSeqCrdRecord(String record)
    {
        seqCrdRecords = seqCrdRecords == null ? new ArrayList<S2CResidue>() : seqCrdRecords;
        seqCrdRecords.add(new S2CResidue(this, record));
    }

    /**
     * @return the headSC
     */
    public String getHeadSC()
    {
        return headSC;
    }

    /**
     * @return the seqCrdRecords
     */
    public ArrayList<S2CResidue> getSeqCrdRecords()
    {
        return seqCrdRecords;
    }

    /**
     * @return the method
     */
    public String getMethod()
    {
        return method;
    }

    /**
     * @return the resolution
     */
    public double getResolution()
    {
        return resolution;
    }

    /**
     * @return the rFactor
     */
    public double getrFactor()
    {
        return rFactor;
    }

    /**
     * @return the bFactor
     */
    public double getbFactor()
    {
        return bFactor;
    }

    public void addParameter(String record)
    {
        String value = record.substring(21);
        record = record.substring(10);
        if (record.startsWith("method"))
        {
            method = value;
        } 
        else if (record.startsWith("resolution"))
        {
            try
            {
                resolution = Double.parseDouble(value.trim());
            } 
            catch (NumberFormatException ex)
            {
                Logger.getLogger(S2CReader.class.getName()).log(Level.SEVERE, null, ex);
                resolution = -1;
            }
        } 
        else if (record.startsWith("R-factor"))
        {
            try
            {
                rFactor = Double.parseDouble(value.trim());
            }
            catch (NumberFormatException ex)
            {
                Logger.getLogger(S2CReader.class.getName()).log(Level.SEVERE, null, ex);
                rFactor = -1;
            }
        } 
     /*   else if (record.startsWith("B-factor"))
        {
            try
            {
                bFactor = Double.parseDouble(value.trim());
            } 
            catch (NumberFormatException ex)
            {
                Logger.getLogger(S2CReader.class.getName()).log(Level.SEVERE, null, ex);
                bFactor = -1;
            }
        }*/
    }
}
