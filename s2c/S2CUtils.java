/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package s2c;

import s2c.db.S2CInserts;
import s2c.db.S2CUpdates;

/**
 *
 * @author administrator
 */
public class S2CUtils
{

    static public void insertS2C(String pdbId)
    {
        S2CEntity s2c = S2CReader.readS2CFile(pdbId);
        if (s2c != null)
        {
            insertS2C(s2c);
        }
    }

    static public void insertS2C(S2CEntity s2c)
    {
        S2CUpdates update = new S2CUpdates();
        S2CInserts insert = new S2CInserts();
        update.updateS2C(s2c);
        for (int i = 0; s2c.getSeqCrdRecords() != null && i < s2c.getSeqCrdRecords().size(); i++)
        {
            if (s2c.getSeqCrdRecords().get(i).hasAtomRecord())
            {
                update.updateS2C(s2c.getSeqCrdRecords().get(i));
            } 
            else
            {
                insert.insertS2C(s2c.getSeqCrdRecords().get(i));
            }
        }
    }
}
