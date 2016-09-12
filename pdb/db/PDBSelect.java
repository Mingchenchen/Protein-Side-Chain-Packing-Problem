/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pdb.db;

import db.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author administrator
 */
public class PDBSelect {

    private Connection conn = null;

    public PDBSelect() {
        conn = (new DBConnection()).getConnection();
    }

    protected void finalize() {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(PDBSelect.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            super.finalize();
        } catch (Throwable ex) {
            Logger.getLogger(PDBSelect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<String> getSelectedPDBEntries() {
        ArrayList<String> result = null;
        StringBuilder sb = new StringBuilder();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            sb.append("select pdb_id ");
            sb.append("from pdb_entry ");
            sb.append("where is_selected is true ");
            sb.append("order by pdb_id;");
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sb.toString());
            while (rs.next()) {
                result = result == null ? new ArrayList<String>() : result;
                result.add(rs.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PDBSelect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
