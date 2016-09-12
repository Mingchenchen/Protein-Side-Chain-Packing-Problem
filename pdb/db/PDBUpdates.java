/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pdb.db;

import db.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.biojava.bio.structure.AminoAcid;

/**
 *
 * @author administrator
 */
public class PDBUpdates {

    private Connection conn = null;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public PDBUpdates() {
        conn = (new DBConnection()).getConnection();
    }

    protected void finalize() {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(PDBInserts.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            super.finalize();
        } catch (Throwable ex) {
            Logger.getLogger(PDBInserts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean updateCheckedResidue(AminoAcid aa, boolean complete) {
        Statement stmt = null;
        boolean insertedOk = true;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("update pdb_residue set is_complete = ");
            sb.append(complete);
            sb.append(", updated_on = '");
            sb.append(sdf.format(new Date()));
            sb.append("' where pdb_chain_id = get_pdb_chain_id('");
            sb.append(aa.getParent().getParent().getPDBCode());
            sb.append("','");
            sb.append(aa.getParent().getName());
            sb.append("') and atom_res_num = '");
            sb.append(aa.getPDBCode());
            sb.append("';");
            stmt = conn.createStatement();
            stmt.executeUpdate(sb.toString());
        } catch (SQLException ex) {
            insertedOk = false;
            Logger.getLogger(PDBInserts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return insertedOk;
    }

    public boolean setSelectedPDBEntry(String pdbId) {
        Statement stmt = null;
        boolean insertedOk = true;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("update pdb_entry set ");
            sb.append("is_selected = 1, ");
            sb.append("updated_on = '");
            sb.append(sdf.format(new Date()));
            sb.append("' where pdb_id = '");
            sb.append(pdbId);
            sb.append("';");
            stmt = conn.createStatement();
            stmt.executeUpdate(sb.toString());
        } catch (SQLException ex) {
            insertedOk = false;
            Logger.getLogger(PDBInserts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return insertedOk;
    }
}
