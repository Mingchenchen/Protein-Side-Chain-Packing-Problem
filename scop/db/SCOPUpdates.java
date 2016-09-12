/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scop.db;

import db.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import scop.entity.PDBSection;
import scop.entity.SCOPFamily;

/**
 *
 * @author administrator
 */
class SCOPUpdates {

    static boolean updateSCOPFamily(SCOPFamily fa) {
        int executeUpdate = 0;
        Connection conn = null;
        Statement stmt = null;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("update scop_family set scop_superfamily_id=");
            sb.append(fa.getParent().getId());
            sb.append(", sunid=");
            sb.append(fa.getSunid());
            sb.append(", sccs=");
            sb.append(fa.getSccs());
            sb.append(", description=\"");
            sb.append(fa.getDescription());
            sb.append("\" where id=");
            sb.append(fa.getId());
            sb.append(";");
            System.out.println(sb.toString());
            conn = (new DBConnection()).getConnection();
            stmt = conn.createStatement();
            executeUpdate = stmt.executeUpdate(sb.toString());
        } catch (SQLException ex) {
            Logger.getLogger(SCOPInserts.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(SCOPInserts.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(SCOPInserts.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return executeUpdate > 0;
    }

    static boolean updatePDBSection(PDBSection px) {
        int executeUpdate = 0;
        Connection conn = null;
        Statement stmt = null;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("update pdb_section set scop_domain_id=");
            sb.append(px.getParent().getId());
            sb.append(", pdb_id=\"");
            sb.append(px.getPdbId());
            sb.append("\", chain='");
            sb.append(px.getChain());
            sb.append("', res_num_start=");
            sb.append(px.getResNumStart());
            sb.append(", res_num_end=");
            sb.append(px.getResNumEnd());
            sb.append(" where id=");
            sb.append(px.getId());
            sb.append(";");
            System.out.println(sb.toString());
            conn = (new DBConnection()).getConnection();
            stmt = conn.createStatement();
            executeUpdate = stmt.executeUpdate(sb.toString());
        } catch (SQLException ex) {
            Logger.getLogger(SCOPInserts.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(SCOPInserts.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(SCOPInserts.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return executeUpdate > 0;
    }
}
