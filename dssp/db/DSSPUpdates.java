/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dssp.db;

import db.DBConnection;
import dssp.DSSPResidue;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author administrator
 */
public class DSSPUpdates {

    private Connection conn = null;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DSSPUpdates() {
        conn = (new DBConnection()).getConnection();
    }

    protected void finalize() {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DSSPUpdates.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            super.finalize();
        } catch (Throwable ex) {
            Logger.getLogger(DSSPUpdates.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean updateDSSP(DSSPResidue dssp) {
        Statement stmt = null;
        boolean insertedOk = true;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("update pdb_residue set dssp_ss_id = get_dssp_id('");
            sb.append(dssp.getSecStructure().charAt(0));
            sb.append("'), dssp_acc = ");
            sb.append(dssp.getAcc());
            sb.append(", updated_on ='");
            sb.append(sdf.format(new Date()));
            sb.append("' where pdb_chain_id = get_pdb_chain_id('");
            sb.append(dssp.getParent().getPdbId());
            sb.append("','");
            sb.append(dssp.getChain());
            sb.append("') and atom_res_num = '");
            sb.append(dssp.getResidueCode());
            sb.append("';");
            //System.out.println(sb.toString());
            stmt = conn.createStatement();
            stmt.executeUpdate(sb.toString());
        } catch (SQLException ex) {
            insertedOk = false;
            Logger.getLogger(DSSPUpdates.class.getName()).log(Level.SEVERE, null, ex);
        }
        return insertedOk;
    }
}
