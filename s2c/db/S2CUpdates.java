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
import s2c.S2CEntity;
import s2c.S2CResidue;

/**
 *
 * @author administrator
 */
public class S2CUpdates {

    private Connection conn = null;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public S2CUpdates() {
        conn = (new DBConnection()).getConnection();
    }

    protected void finalize() {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(S2CUpdates.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            super.finalize();
        } catch (Throwable ex) {
            Logger.getLogger(S2CUpdates.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean updateS2C(S2CEntity s2c) {
        Statement stmt = null;
        boolean insertedOk = true;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("update pdb_entry set resolution =");
            sb.append(s2c.getResolution());
            sb.append(", r_factor =");
            sb.append(s2c.getrFactor());
            sb.append(", updated_on ='");
            sb.append(sdf.format(new Date()));
            sb.append("' where pdb_id = '");
            sb.append(s2c.getHeadSC());
            sb.append("';");
            //System.out.println(sb.toString());
            stmt = conn.createStatement();
            stmt.executeUpdate(sb.toString());
        } catch (SQLException ex) {
            insertedOk = false;
            Logger.getLogger(S2CUpdates.class.getName()).log(Level.SEVERE, null, ex);
        }
        return insertedOk;
    }

    public boolean updateS2C(S2CResidue s2c) {
        Statement stmt = null;
        boolean insertedOk = true;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("update pdb_residue set seqres_res_num = ");
            sb.append(s2c.getSeqresResNum());
            sb.append(", seqres_aa_id = get_amino_acid_id('");
            sb.append(s2c.getSeqresThreeLetterCode());
            sb.append("'), pdb_ss_id = get_ss_main_id('");
            sb.append(s2c.getPdbSS());
            sb.append("'), stride_ss_id = get_stride_id('");
            sb.append(s2c.getStrideSS());
            sb.append("'), updated_on ='");
            sb.append(sdf.format(new Date()));
            sb.append("' where pdb_chain_id = get_pdb_chain_id('");
            sb.append(s2c.getParent().getHeadSC());
            sb.append("','");
            sb.append(s2c.getChain());
            sb.append("') and atom_res_num = '");
            sb.append(s2c.getAtomResNum());
            sb.append("';");
            //System.out.println(sb.toString());
            stmt = conn.createStatement();
            stmt.executeUpdate(sb.toString());
        } catch (SQLException ex) {
            insertedOk = false;
            Logger.getLogger(S2CUpdates.class.getName()).log(Level.SEVERE, null, ex);
        }
        return insertedOk;
    }
}
