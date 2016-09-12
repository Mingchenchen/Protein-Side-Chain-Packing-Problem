/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package s2c.db;

import db.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import s2c.S2CResidue;

/**
 *
 * @author administrator
 */
public class S2CInserts {

    private Connection conn = null;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public S2CInserts() {
        conn = (new DBConnection()).getConnection();
    }

    protected void finalize() {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(S2CInserts.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            super.finalize();
        } catch (Throwable ex) {
            Logger.getLogger(S2CInserts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean insertS2C(S2CResidue s2c) {
        Statement stmt = null;
        boolean insertedOk = true;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("insert into pdb_residue values(null, get_pdb_chain_id('");
            sb.append(s2c.getParent().getHeadSC());
            sb.append("', '");
            sb.append(s2c.getChain());
            sb.append("'), ");
            sb.append(s2c.getSeqresResNum());
            sb.append(", get_amino_acid_id('");
            sb.append(s2c.getSeqresThreeLetterCode());
            sb.append("'), null, null, get_ss_main_id('");
            sb.append(s2c.getPdbSS());
            sb.append("'), get_stride_id('");
            sb.append(s2c.getStrideSS());
            sb.append("'), null, null, false, '");
            sb.append(sdf.format(new Date()));
            sb.append("');");
            stmt = conn.createStatement();
            stmt.executeUpdate(sb.toString());
        } catch (SQLException ex) {
            insertedOk = false;
            Logger.getLogger(S2CInserts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
