/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scop.db;

import db.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import scop.entity.PDBSection;
import scop.entity.SCOPClass;
import scop.entity.SCOPDomain;
import scop.entity.SCOPFamily;
import scop.entity.SCOPFold;
import scop.entity.SCOPProtein;
import scop.entity.SCOPSpecies;
import scop.entity.SCOPSuperfamily;

/**
 *
 * @author administrator
 */
public class SCOPInserts {

    Connection conn = null;
    Statement stmt = null;

    public SCOPInserts() {
        try {
            conn = (new DBConnection()).getConnection();
            stmt = conn.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(SCOPInserts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    protected void finalize(){
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
        try {
            super.finalize();
        } catch (Throwable ex) {
            Logger.getLogger(SCOPInserts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int insertSCOPClass(SCOPClass cl) {
        int id = 0;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("insert into scop_class ");
            sb.append("values (null, ");
            sb.append(cl.getSunid());
            sb.append(", '");
            sb.append(cl.getSccs());
            sb.append("', \"");
            sb.append(cl.getDescription());
            sb.append("\"); ");
            //System.out.println(sb.toString());
            stmt.executeUpdate(sb.toString(), Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            id = rs.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(SCOPInserts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public int insertSCOPFold(SCOPFold cf) {
        int id = 0;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("insert into scop_fold ");
            sb.append("values (null, ");
            sb.append(cf.getParent().getId());
            sb.append(", ");
            sb.append(cf.getSunid());
            sb.append(", ");
            sb.append(cf.getSccs());
            sb.append(", \"");
            sb.append(cf.getDescription());
            sb.append("\"); ");
            //System.out.println(sb.toString());
            stmt.executeUpdate(sb.toString(), Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            id = rs.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(SCOPInserts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public int insertSCOPSuperfamily(SCOPSuperfamily sf) {
        int id = 0;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("insert into scop_superfamily ");
            sb.append("values (null, ");
            sb.append(sf.getParent().getId());
            sb.append(", ");
            sb.append(sf.getSunid());
            sb.append(", ");
            sb.append(sf.getSccs());
            sb.append(", \"");
            sb.append(sf.getDescription());
            sb.append("\"); ");
            //System.out.println(sb.toString());
            stmt.executeUpdate(sb.toString(), Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            id = rs.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(SCOPInserts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public int insertSCOPFamily(SCOPFamily fa) {
        int id = 0;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("insert into scop_family ");
            sb.append("values (null, ");
            sb.append(fa.getParent().getId());
            sb.append(", ");
            sb.append(fa.getSunid());
            sb.append(", ");
            sb.append(fa.getSccs());
            sb.append(", \"");
            sb.append(fa.getDescription());
            sb.append("\"); ");
            //System.out.println(sb.toString());
            stmt.executeUpdate(sb.toString(), Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            id = rs.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(SCOPInserts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public int insertSCOPProtein(SCOPProtein dm) {
        int id = 0;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("insert into scop_protein ");
            sb.append("values (null, ");
            sb.append(dm.getParent().getId());
            sb.append(", ");
            sb.append(dm.getSunid());
            sb.append(", \"");
            sb.append(dm.getDescription());
            sb.append("\"); ");
            //System.out.println(sb.toString());
            stmt.executeUpdate(sb.toString(), Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            id = rs.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(SCOPInserts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public int insertSCOPSpecies(SCOPSpecies sp) {
        int id = 0;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("insert into scop_species ");
            sb.append("values (null, ");
            sb.append(sp.getParent().getId());
            sb.append(", ");
            sb.append(sp.getSunid());
            sb.append(", \"");
            sb.append(sp.getDescription());
            sb.append("\", ");
            sb.append(sp.getTaxid());
            sb.append(");");
            //System.out.println(sb.toString());
            stmt.executeUpdate(sb.toString(), Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            id = rs.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(SCOPInserts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public int insertSCOPDomain(SCOPDomain px) {
        int id = 0;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("insert into scop_domain ");
            sb.append("values (null, ");
            sb.append(px.getParent().getId());
            sb.append(", ");
            sb.append(px.getSunid());
            sb.append(", \"");
            sb.append(px.getSid());
            sb.append("\");");
            //System.out.println(sb.toString());
            stmt.executeUpdate(sb.toString(), Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            id = rs.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(SCOPInserts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public int insertPDBSection(PDBSection px) {
        int id = 0;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("insert into pdb_section ");
            sb.append("values (null, ");
            sb.append(px.getParent().getId());
            sb.append(", \"");
            sb.append(px.getPdbId());
            sb.append("\", '");
            sb.append(px.getChain());
            sb.append("', \"");
            sb.append(px.getResNumStart());
            sb.append("\", \"");
            sb.append(px.getResNumEnd());
            sb.append("\");");
            //System.out.println(sb.toString());
            stmt.executeUpdate(sb.toString(), Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            id = rs.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(SCOPInserts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }
}
