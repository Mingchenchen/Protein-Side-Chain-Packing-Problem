/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.db;

import db.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author administrator
 */
public class CatalogsInserts {

    private Connection conn = null;

    public CatalogsInserts() {
        conn = (new DBConnection()).getConnection();
    }

    protected void finalize() {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(CatalogsInserts.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            super.finalize();
        } catch (Throwable ex) {
            Logger.getLogger(CatalogsInserts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int insertAminoAcid(char oneLC, String threeLC, String fullName, int maxAcc) {
        Statement stmt = null;
        int id = 0;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("insert into amino_acid ");
            sb.append("values (null, '");
            sb.append(oneLC);
            sb.append("', \"");
            sb.append(threeLC);
            sb.append("\", \"");
            sb.append(fullName);
            sb.append("\", ");
            sb.append(maxAcc);
            sb.append(");");
            System.out.println(sb.toString());
            stmt = conn.createStatement();
            stmt.executeUpdate(sb.toString(), Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            id = rs.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(CatalogsInserts.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(CatalogsInserts.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return id;
    }

    public int insertAtom(int aminoAcidId, String pdbCode, String elementSymbol) {
        Statement stmt = null;
        int id = 0;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("insert into atom ");
            sb.append("values (null, ");
            sb.append(aminoAcidId);
            sb.append(", \"");
            sb.append(pdbCode);
            sb.append("\", \"");
            sb.append(elementSymbol);
            sb.append("\");");
            System.out.println(sb.toString());
            stmt = conn.createStatement();
            stmt.executeUpdate(sb.toString(), Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            id = rs.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(CatalogsInserts.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(CatalogsInserts.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return id;
    }
}
