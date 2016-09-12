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
import java.util.ArrayList;
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
public class SCOPSelects {

    Connection conn = null;

    public SCOPSelects() {
        conn = (new DBConnection()).getConnection();
    }

    protected void finalize() {
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

    public SCOPClass getSCOPClass(int id) {
        SCOPClass result = null;
        StringBuilder sb = new StringBuilder();
        Statement stmt = null;
        ResultSet rs = null;
        int i = 1;
        sb.append("select * ");
        sb.append("from scop_class ");
        sb.append("where id = ");
        sb.append(id);
        sb.append(";");
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sb.toString());
            rs.next();
            result = new SCOPClass();
            result.setId(rs.getInt(i++));
            result.setSunid(rs.getInt(i++));
            result.setSccs(rs.getString(i++).charAt(0));
            result.setDescription(rs.getString(i++));
        } catch (SQLException ex) {
            Logger.getLogger(SCOPSelects.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public SCOPFold getSCOPFold(int id) {
        SCOPFold result = null;
        StringBuilder sb = new StringBuilder();
        Statement stmt = null;
        ResultSet rs = null;
        int i = 1;
        sb.append("select * ");
        sb.append("from scop_fold ");
        sb.append("where id = ");
        sb.append(id);
        sb.append(";");
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sb.toString());
            rs.next();
            result = new SCOPFold(getSCOPClass(rs.getInt(2)));
            result.setId(rs.getInt(i++));
            rs.getString(i++); //scop_class_id
            result.setSunid(rs.getInt(i++));
            result.setSccs(rs.getString(i++).charAt(0));
            result.setDescription(rs.getString(i++));
        } catch (SQLException ex) {
            Logger.getLogger(SCOPSelects.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public SCOPSuperfamily getSCOPSuperfamily(int id) {
        SCOPSuperfamily result = null;
        StringBuilder sb = new StringBuilder();
        Statement stmt = null;
        ResultSet rs = null;
        int i = 1;
        sb.append("select * ");
        sb.append("from scop_superfamily ");
        sb.append("where id = ");
        sb.append(id);
        sb.append(";");
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sb.toString());
            rs.next();
            result = new SCOPSuperfamily(getSCOPFold(rs.getInt(2)));
            result.setId(rs.getInt(i++));
            rs.getString(i++); //scop_fold_id
            result.setSunid(rs.getInt(i++));
            result.setSccs(rs.getString(i++).charAt(0));
            result.setDescription(rs.getString(i++));
        } catch (SQLException ex) {
            Logger.getLogger(SCOPSelects.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public SCOPFamily getSCOPFamily(int id) {
        SCOPFamily result = null;
        StringBuilder sb = new StringBuilder();
        Statement stmt = null;
        ResultSet rs = null;
        int i = 1;
        sb.append("select * ");
        sb.append("from scop_family ");
        sb.append("where id = ");
        sb.append(id);
        sb.append(";");
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sb.toString());
            rs.next();
            result = new SCOPFamily(getSCOPSuperfamily(rs.getInt(2)));
            result.setId(rs.getInt(i++));
            rs.getString(i++); //scop_superfamily_id
            result.setSunid(rs.getInt(i++));
            result.setSccs(rs.getString(i++).charAt(0));
            result.setDescription(rs.getString(i++));
        } catch (SQLException ex) {
            Logger.getLogger(SCOPSelects.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public SCOPProtein getSCOPProtein(int id) {
        SCOPProtein result = null;
        StringBuilder sb = new StringBuilder();
        Statement stmt = null;
        ResultSet rs = null;
        int i = 1;
        sb.append("select * ");
        sb.append("from scop_protein ");
        sb.append("where id = ");
        sb.append(id);
        sb.append(";");
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sb.toString());
            rs.next();
            result = new SCOPProtein(getSCOPFamily(rs.getInt(2)));
            result.setId(rs.getInt(i++));
            rs.getString(i++); //scop_superfamily_id
            result.setSunid(rs.getInt(i++));
            result.setDescription(rs.getString(i++));
        } catch (SQLException ex) {
            Logger.getLogger(SCOPSelects.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public SCOPSpecies getSCOPSpecies(int id) {
        SCOPSpecies result = null;
        StringBuilder sb = new StringBuilder();
        Statement stmt = null;
        ResultSet rs = null;
        int i = 1;
        sb.append("select * ");
        sb.append("from scop_species ");
        sb.append("where id = ");
        sb.append(id);
        sb.append(";");
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sb.toString());
            rs.next();
            result = new SCOPSpecies(getSCOPProtein(rs.getInt(2)));
            result.setId(rs.getInt(i++));
            rs.getString(i++); //scop_superfamily_id
            result.setSunid(rs.getInt(i++));
            result.setDescription(rs.getString(i++));
            result.setTaxid(rs.getInt(i++));
        } catch (SQLException ex) {
            Logger.getLogger(SCOPSelects.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public SCOPDomain getSCOPDomain(int id) {
        SCOPDomain result = null;
        StringBuilder sb = new StringBuilder();
        Statement stmt = null;
        ResultSet rs = null;
        int i = 1;
        sb.append("select * ");
        sb.append("from scop_domain ");
        sb.append("where id = ");
        sb.append(id);
        sb.append(";");
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sb.toString());
            rs.next();
            result = new SCOPDomain(getSCOPSpecies(rs.getInt(2)));
            result.setId(rs.getInt(i++));
            rs.getString(i++); //scop_superfamily_id
            result.setSunid(rs.getInt(i++));
            result.setSid(rs.getString(i++));
        } catch (SQLException ex) {
            Logger.getLogger(SCOPSelects.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public PDBSection getPDBSection(int id) {
        PDBSection result = null;
        StringBuilder sb = new StringBuilder();
        Statement stmt = null;
        ResultSet rs = null;
        int i = 1;
        sb.append("select * ");
        sb.append("from pdb_section ");
        sb.append("where id = ");
        sb.append(id);
        sb.append(";");
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sb.toString());
            rs.next();
            result = new PDBSection(getSCOPDomain(rs.getInt(2)));
            result.setId(rs.getInt(i++));
            rs.getString(i++); //scop_superfamily_id
            result.setPdbId(rs.getString(i++));
            result.setChain(rs.getString(i++).charAt(0));
            result.setResNumStart(rs.getString(i++));
            result.setResNumEnd(rs.getString(i++));
            if (result.getResNumStart().equalsIgnoreCase(result.getResNumEnd())) {
                result.setIsAllChain(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SCOPSelects.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public ArrayList<PDBSection> getPDBSections(String pdbId) {
        ArrayList<PDBSection> result = null;
        StringBuilder sb = new StringBuilder();
        Statement stmt = null;
        ResultSet rs = null;
        sb.append("select id ");
        sb.append("from pdb_section ");
        sb.append("where pdb_id = \"");
        sb.append(pdbId);
        sb.append("\";");
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sb.toString());
            while (rs.next()) {
                result = result == null ? new ArrayList<PDBSection>() : result;
                result.add(getPDBSection(rs.getInt(1)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SCOPSelects.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public ArrayList<String> getMonoDomainPDBIds() {
        ArrayList<String> result = null;
        StringBuilder sb = new StringBuilder();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            sb.append("select pdb_id from (");
            sb.append("select pdb_id, count(sid) domain_num from (");
            sb.append("select distinct pdb_id, sid from pdb_section ");
            sb.append("inner join scop_domain ");
            sb.append("on pdb_section.scop_domain_id = scop_domain.id) tab1 ");
            sb.append("group by pdb_id) tab2 ");
            sb.append("where tab2.domain_num = 1;");
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sb.toString());
            while (rs.next()) {
                result = result == null ? new ArrayList<String>() : result;
                result.add(rs.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SCOPSelects.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public ArrayList<String> getMissingMonoDomainPDBIds(int limit) {
        ArrayList<String> result = null;
        StringBuilder sb = new StringBuilder();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            sb.append("select tab2.pdb_id from (");
            sb.append("select pdb_id, count(sid) domain_num from (");
            sb.append("select distinct pdb_id, sid from pdb_section ");
            sb.append("inner join scop_domain ");
            sb.append("on pdb_section.scop_domain_id = scop_domain.id) tab1 ");
            sb.append("group by pdb_id) tab2 ");
            sb.append("left outer join pdb_entry ");
            sb.append("on tab2.pdb_id = pdb_entry.pdb_id ");
            sb.append("where tab2.domain_num = 1 ");
            sb.append("and pdb_entry.pdb_id is null ");
            sb.append("limit ");
            sb.append(limit);
            sb.append(";");
            System.out.println(sb.toString());
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sb.toString());
            while (rs.next()) {
                result = result == null ? new ArrayList<String>() : result;
                result.add(rs.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SCOPSelects.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public ArrayList<String> getMissingPDBIdsToCheck(int limit) {
        ArrayList<String> result = null;
        StringBuilder sb = new StringBuilder();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            sb.append("select pdb_id from ( ");
            sb.append("select pdb_id, count(*) chain_num, sum(chain_length) pdb_length ");
            sb.append("from pdb_entry inner join pdb_chain ");
            sb.append("on pdb_entry.id = pdb_entry_id ");
            sb.append("where 0 <= resolution and resolution <= 2 ");
            sb.append("and 0 <= r_factor and r_factor <= 0.2 ");
            sb.append("group by pdb_id )tab1 ");
            sb.append("where chain_num = 1 ");
            sb.append("and 0 < pdb_length and pdb_length <= 1000 ");
            sb.append("and pdb_id in (");
            sb.append("select pdb_id ");
            sb.append("from scop ");
            sb.append("where (cl = 'a' or cl = 'b' or cl = 'c' or cl = 'd')) ");
            sb.append("and pdb_id in (");
            sb.append("select distinct pdb_id from pdb where is_complete is null) ");
            sb.append("order by pdb_id ");
            sb.append("limit ");
            sb.append(limit);
            sb.append(";");
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sb.toString());
            while (rs.next()) {
                result = result == null ? new ArrayList<String>() : result;
                result.add(rs.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SCOPSelects.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public ArrayList<String> getPDBIdsToCheck() {
        ArrayList<String> result = null;
        StringBuilder sb = new StringBuilder();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            sb.append("select pdb_id from ( ");
            sb.append("select pdb_id, count(*) chain_num, sum(chain_length) pdb_length ");
            sb.append("from pdb_entry inner join pdb_chain ");
            sb.append("on pdb_entry.id = pdb_entry_id ");
            sb.append("where 0 <= resolution and resolution <= 2 ");
            sb.append("and 0 <= r_factor and r_factor <= 0.2 ");
            sb.append("group by pdb_id )tab1 ");
            sb.append("where chain_num = 1 ");
            sb.append("and 40 <= pdb_length and pdb_length <= 400 ");
            sb.append("and pdb_id in (");
            sb.append("select pdb_id ");
            sb.append("from scop ");
            sb.append("where (cl = 'a' or cl = 'b' or cl = 'c' or cl = 'd')) ");
            sb.append("order by pdb_id;");
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sb.toString());
            while (rs.next()) {
                result = result == null ? new ArrayList<String>() : result;
                result.add(rs.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SCOPSelects.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
