/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.db;

import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author administrator
 */
public class ResultsSelectPDBID {

    private Connection conn = null;
    private PreparedStatement stmt = null;

    public ResultsSelectPDBID() {
        conn = (new DBConnection()).getConnection();
    }

    protected void finalize() {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(ResultsSelectPDBID.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            super.finalize();
        } catch (Throwable ex) {
            Logger.getLogger(ResultsSelectPDBID.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<String> getTotalN() {
        ArrayList<String> result = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        ResultSet rs = null;
        try {
            if (stmt == null) {
                sb.append("select scp_method_id, pdb_id, count(*) n, avg(rmsd), std(rmsd) ");
                sb.append("from scp_residue_05 ");
                sb.append("where (scp_method_id = 2 ");
                sb.append("or scp_method_id = 3) ");
                sb.append("group by scp_method_id, pdb_id ");
                sb.append("order by scp_method_id, pdb_id ");
                System.out.println(sb.toString());
                setStmt(conn.prepareStatement(sb.toString()));
            }
            int i = 1;
            System.out.println(new Date());
            rs = stmt.executeQuery();
            while (rs.next()) {
                sb = new StringBuilder();
                sb.append("global\t");
                i = 1;
                sb.append(rs.getInt(i++));
                sb.append("\t");
                sb.append(rs.getString(i++));
                sb.append("\t");
                sb.append(rs.getDouble(i++));
                sb.append("\t");
                sb.append(rs.getDouble(i++));
                sb.append("\t");
                sb.append(rs.getDouble(i++));
                sb.append("\t");
                result.add(sb.toString());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResultsSelectAA.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public void getTotalX1(ArrayList<String> pdbIds, ArrayList<String> result) {
        StringBuilder sb = new StringBuilder();
        ResultSet rs = null;
        try {
            if (stmt == null) {
                sb.append("select scp_method_id, pdb_id, count(*) n, avg(rmsd), std(rmsd) ");
                sb.append("from scp_residue_05 ");
                sb.append("where x1 is not null ");
                sb.append("and (scp_method_id = 2 ");
                sb.append("or scp_method_id = 3) ");
                sb.append("group by scp_method_id, pdb_id ");
                sb.append("order by scp_method_id, pdb_id ");
                System.out.println(sb.toString());
                setStmt(conn.prepareStatement(sb.toString()));
            }
            int i = 1;
            System.out.println(new Date());
            rs = stmt.executeQuery();
            while (rs.next()) {
                int arrayIndex = pdbIds.indexOf(rs.getString(2));
                arrayIndex = rs.getInt(1) == 3 ? arrayIndex + 770 : arrayIndex;
                sb = new StringBuilder(result.get(arrayIndex));
                i = 3;
                sb.append(rs.getDouble(i++));
                sb.append("\t");
                sb.append(rs.getDouble(i++));
                sb.append("\t");
                sb.append(rs.getDouble(i++));
                sb.append("\t");
                result.set(arrayIndex, sb.toString());
                arrayIndex++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResultsSelectAA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getTotalX2(ArrayList<String> pdbIds, ArrayList<String> result) {
        StringBuilder sb = new StringBuilder();
        ResultSet rs = null;
        try {
            if (stmt == null) {
                sb.append("select scp_method_id, pdb_id, count(*) n, avg(rmsd), std(rmsd) ");
                sb.append("from scp_residue_05 ");
                sb.append("where x2 is not null ");
                sb.append("and (scp_method_id = 2 ");
                sb.append("or scp_method_id = 3) ");
                sb.append("group by scp_method_id, pdb_id ");
                sb.append("order by scp_method_id, pdb_id ");
                System.out.println(sb.toString());
                setStmt(conn.prepareStatement(sb.toString()));
            }
            int i = 1;
            System.out.println(new Date());
            rs = stmt.executeQuery();
            while (rs.next()) {
                int arrayIndex = pdbIds.indexOf(rs.getString(2));
                arrayIndex = rs.getInt(1) == 3 ? arrayIndex + 770 : arrayIndex;
                sb = new StringBuilder(result.get(arrayIndex));
                i = 3;
                sb.append(rs.getDouble(i++));
                sb.append("\t");
                sb.append(rs.getDouble(i++));
                sb.append("\t");
                sb.append(rs.getDouble(i++));
                sb.append("\t");
                result.set(arrayIndex, sb.toString());
                arrayIndex++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResultsSelectAA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getTotalX3(ArrayList<String> pdbIds, ArrayList<String> result) {
        StringBuilder sb = new StringBuilder();
        ResultSet rs = null;
        try {
            if (stmt == null) {
                sb.append("select scp_method_id, pdb_id, count(*) n, avg(rmsd), std(rmsd) ");
                sb.append("from scp_residue_05 ");
                sb.append("where x3 is not null ");
                sb.append("and (scp_method_id = 2 ");
                sb.append("or scp_method_id = 3) ");
                sb.append("group by scp_method_id, pdb_id ");
                sb.append("order by scp_method_id, pdb_id ");
                System.out.println(sb.toString());
                setStmt(conn.prepareStatement(sb.toString()));
            }
            int i = 1;
            System.out.println(new Date());
            rs = stmt.executeQuery();
            while (rs.next()) {
                int arrayIndex = pdbIds.indexOf(rs.getString(2));
                arrayIndex = rs.getInt(1) == 3 ? arrayIndex + 770 : arrayIndex;
                sb = new StringBuilder(result.get(arrayIndex));
                i = 3;
                sb.append(rs.getDouble(i++));
                sb.append("\t");
                sb.append(rs.getDouble(i++));
                sb.append("\t");
                sb.append(rs.getDouble(i++));
                sb.append("\t");
                result.set(arrayIndex, sb.toString());
                arrayIndex++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResultsSelectAA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getTotalX4(ArrayList<String> pdbIds, ArrayList<String> result) {
        StringBuilder sb = new StringBuilder();
        ResultSet rs = null;
        try {
            if (stmt == null) {
                sb.append("select scp_method_id, pdb_id, count(*) n, avg(rmsd), std(rmsd) ");
                sb.append("from scp_residue_05 ");
                sb.append("where x4 is not null ");
                sb.append("and (scp_method_id = 2 ");
                sb.append("or scp_method_id = 3) ");
                sb.append("group by scp_method_id, pdb_id ");
                sb.append("order by scp_method_id, pdb_id ");
                System.out.println(sb.toString());
                setStmt(conn.prepareStatement(sb.toString()));
            }
            int i = 1;
            System.out.println(new Date());
            rs = stmt.executeQuery();
            while (rs.next()) {
                int arrayIndex = pdbIds.indexOf(rs.getString(2));
                arrayIndex = rs.getInt(1) == 3 ? arrayIndex + 770 : arrayIndex;
                sb = new StringBuilder(result.get(arrayIndex));
                i = 3;
                sb.append(rs.getDouble(i++));
                sb.append("\t");
                sb.append(rs.getDouble(i++));
                sb.append("\t");
                sb.append(rs.getDouble(i++));
                sb.append("\t");
                result.set(arrayIndex, sb.toString());
                arrayIndex++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResultsSelectAA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<String> getN(int cutoff) {
        ArrayList<String> result = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        ResultSet rs = null;
        try {
            if (stmt == null) {
                sb.append("select scp_method_id, pdb_id, count(*) n, avg(rmsd), std(rmsd) ");
                sb.append("from scp_residue_05 ");
                sb.append("where x1 <= ? ");
                sb.append("and (scp_method_id = 2 ");
                sb.append("or scp_method_id = 3) ");
                sb.append("group by scp_method_id, pdb_id ");
                sb.append("order by scp_method_id, pdb_id ");
                System.out.println(sb.toString());
                setStmt(conn.prepareStatement(sb.toString()));
            }
            int i = 1;
            stmt.setInt(i++, cutoff);
            System.out.println(new Date());
            rs = stmt.executeQuery();
            while (rs.next()) {
                sb = new StringBuilder();
                sb.append(cutoff);
                sb.append("\t");
                i = 1;
                sb.append(rs.getInt(i++));
                sb.append("\t");
                sb.append(rs.getString(i++));
                sb.append("\t");
                sb.append(rs.getDouble(i++));
                sb.append("\t");
                sb.append(rs.getDouble(i++));
                sb.append("\t");
                sb.append(rs.getDouble(i++));
                sb.append("\t");
                result.add(sb.toString());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResultsSelectAA.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public void getX1(ArrayList<String> pdbIds, ArrayList<String> result, int cutoff) {
        StringBuilder sb = new StringBuilder();
        ResultSet rs = null;
        try {
            if (stmt == null) {
                sb.append("select scp_method_id, pdb_id, count(*) n, avg(rmsd), std(rmsd) ");
                sb.append("from scp_residue_05 ");
                sb.append("where x1 <= ? ");
                sb.append("and (scp_method_id = 2 ");
                sb.append("or scp_method_id = 3) ");
                sb.append("group by scp_method_id, pdb_id ");
                sb.append("order by scp_method_id, pdb_id ");
                System.out.println(sb.toString());
                setStmt(conn.prepareStatement(sb.toString()));
            }
            int i = 1;
            stmt.setInt(i++, cutoff);
            System.out.println(new Date());
            rs = stmt.executeQuery();
            while (rs.next()) {
                int arrayIndex = pdbIds.indexOf(rs.getString(2));
                arrayIndex = rs.getInt(1) == 3 ? arrayIndex + 770 : arrayIndex;
                sb = new StringBuilder(result.get(arrayIndex));
                i = 3;
                sb.append(rs.getDouble(i++));
                sb.append("\t");
                sb.append(rs.getDouble(i++));
                sb.append("\t");
                sb.append(rs.getDouble(i++));
                sb.append("\t");
                result.set(arrayIndex, sb.toString());
                arrayIndex++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResultsSelectAA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getX2(ArrayList<String> pdbIds, ArrayList<String> result, int cutoff) {
        StringBuilder sb = new StringBuilder();
        ResultSet rs = null;
        try {
            if (stmt == null) {
                sb.append("select scp_method_id, pdb_id, count(*) n, avg(rmsd), std(rmsd) ");
                sb.append("from scp_residue_05 ");
                sb.append("where x1 <= ? ");
                sb.append("and x2 <= ? ");
                sb.append("and (scp_method_id = 2 ");
                sb.append("or scp_method_id = 3) ");
                sb.append("group by scp_method_id, pdb_id ");
                sb.append("order by scp_method_id, pdb_id ");
                System.out.println(sb.toString());
                setStmt(conn.prepareStatement(sb.toString()));
            }
            int i = 1;
            stmt.setInt(i++, cutoff);
            stmt.setInt(i++, cutoff);
            System.out.println(new Date());
            rs = stmt.executeQuery();
            while (rs.next()) {
                int arrayIndex = pdbIds.indexOf(rs.getString(2));
                arrayIndex = rs.getInt(1) == 3 ? arrayIndex + 770 : arrayIndex;
                sb = new StringBuilder(result.get(arrayIndex));
                i = 3;
                sb.append(rs.getDouble(i++));
                sb.append("\t");
                sb.append(rs.getDouble(i++));
                sb.append("\t");
                sb.append(rs.getDouble(i++));
                sb.append("\t");
                result.set(arrayIndex, sb.toString());
                arrayIndex++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResultsSelectAA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getX3(ArrayList<String> pdbIds, ArrayList<String> result, int cutoff) {
        StringBuilder sb = new StringBuilder();
        ResultSet rs = null;
        try {
            if (stmt == null) {
                sb.append("select scp_method_id, pdb_id, count(*) n, avg(rmsd), std(rmsd) ");
                sb.append("from scp_residue_05 ");
                sb.append("where x1 <= ? ");
                sb.append("and x2 <= ? ");
                sb.append("and x3 <= ? ");
                sb.append("and (scp_method_id = 2 ");
                sb.append("or scp_method_id = 3) ");
                sb.append("group by scp_method_id, pdb_id ");
                sb.append("order by scp_method_id, pdb_id ");
                System.out.println(sb.toString());
                setStmt(conn.prepareStatement(sb.toString()));
            }
            int i = 1;
            stmt.setInt(i++, cutoff);
            stmt.setInt(i++, cutoff);
            stmt.setInt(i++, cutoff);
            System.out.println(new Date());
            rs = stmt.executeQuery();
            while (rs.next()) {
                int arrayIndex = pdbIds.indexOf(rs.getString(2));
                arrayIndex = rs.getInt(1) == 3 ? arrayIndex + 770 : arrayIndex;
                sb = new StringBuilder(result.get(arrayIndex));
                i = 3;
                sb.append(rs.getDouble(i++));
                sb.append("\t");
                sb.append(rs.getDouble(i++));
                sb.append("\t");
                sb.append(rs.getDouble(i++));
                sb.append("\t");
                result.set(arrayIndex, sb.toString());
                arrayIndex++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResultsSelectAA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getX4(ArrayList<String> pdbIds, ArrayList<String> result, int cutoff) {
        StringBuilder sb = new StringBuilder();
        ResultSet rs = null;
        try {
            if (stmt == null) {
                sb.append("select scp_method_id, pdb_id, count(*) n, avg(rmsd), std(rmsd) ");
                sb.append("from scp_residue_05 ");
                sb.append("where x1 <= ? ");
                sb.append("and x2 <= ? ");
                sb.append("and x3 <= ? ");
                sb.append("and x4 <= ? ");
                sb.append("and (scp_method_id = 2 ");
                sb.append("or scp_method_id = 3) ");
                sb.append("group by scp_method_id, pdb_id ");
                sb.append("order by scp_method_id, pdb_id ");
                System.out.println(sb.toString());
                setStmt(conn.prepareStatement(sb.toString()));
            }
            int i = 1;
            stmt.setInt(i++, cutoff);
            stmt.setInt(i++, cutoff);
            stmt.setInt(i++, cutoff);
            stmt.setInt(i++, cutoff);
            System.out.println(new Date());
            rs = stmt.executeQuery();
            while (rs.next()) {
                int arrayIndex = pdbIds.indexOf(rs.getString(2));
                arrayIndex = rs.getInt(1) == 3 ? arrayIndex + 770 : arrayIndex;
                sb = new StringBuilder(result.get(arrayIndex));
                i = 3;
                sb.append(rs.getDouble(i++));
                sb.append("\t");
                sb.append(rs.getDouble(i++));
                sb.append("\t");
                sb.append(rs.getDouble(i++));
                sb.append("\t");
                result.set(arrayIndex, sb.toString());
                arrayIndex++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResultsSelectAA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param stmt the stmt to set
     */
    public void setStmt(PreparedStatement stmt) {
        this.stmt = stmt;
    }
}
