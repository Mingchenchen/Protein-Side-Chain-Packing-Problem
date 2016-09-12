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
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author administrator
 */
public class ResultsSelect {

    private Connection conn = null;
    private PreparedStatement stmt = null;

    public ResultsSelect() {
        conn = (new DBConnection()).getConnection();
    }

    protected void finalize() {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(ResultsSelect.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            super.finalize();
        } catch (Throwable ex) {
            Logger.getLogger(ResultsSelect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public double[] getTotalN(int methodId) {
        double result[] = {0, 0, 0};
        StringBuilder sb = new StringBuilder();
        ResultSet rs = null;
        try {
            if (stmt == null) {
                sb.append("select count(*) n, avg(rmsd), std(rmsd) ");
                sb.append("from scp_residue_05 ");
                sb.append("where scp_method_id = ? ");
                System.out.println(sb.toString());
                stmt = conn.prepareStatement(sb.toString());
            }
            int i = 1;
            stmt.setInt(i++, methodId);
            System.out.println(new Date());
            rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("paso x aqui");
                i=0;
                result[i++] = rs.getDouble(i);
                result[i++] = rs.getDouble(i);
                result[i++] = rs.getDouble(i);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResultsSelect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public double[] getTotalX1(int methodId) {
        double result[] = {0, 0, 0};
        StringBuilder sb = new StringBuilder();
        ResultSet rs = null;
        try {
            if (stmt == null) {
                sb.append("select count(*) n, avg(x1), std(x1) ");
                sb.append("from scp_residue_05 ");
                sb.append("where scp_method_id = ? ");
                sb.append("and x1 is not null ");
                System.out.println(sb.toString());
                stmt = conn.prepareStatement(sb.toString());
            }
            int i = 1;
            stmt.setInt(i++, methodId);
            System.out.println(new Date());
            rs = stmt.executeQuery();
            if (rs.next()) {
                i=0;
                result[i++] = rs.getDouble(i);
                result[i++] = rs.getDouble(i);
                result[i++] = rs.getDouble(i);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResultsSelect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public double[] getTotalX2(int methodId) {
        double result[] = {0, 0, 0};
        StringBuilder sb = new StringBuilder();
        ResultSet rs = null;
        try {
            if (stmt == null) {
                sb.append("select count(*) n, avg(x2), std(x2) ");
                sb.append("from scp_residue_05 ");
                sb.append("where scp_method_id = ? ");
                sb.append("and x2 is not null ");
                System.out.println(sb.toString());
                stmt = conn.prepareStatement(sb.toString());
            }
            int i = 1;
            stmt.setInt(i++, methodId);
            System.out.println(new Date());
            rs = stmt.executeQuery();
            if (rs.next()) {
                i=0;
                result[i++] = rs.getDouble(i);
                result[i++] = rs.getDouble(i);
                result[i++] = rs.getDouble(i);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResultsSelect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public double[] getTotalX3(int methodId) {
        double result[] = {0, 0, 0};
        StringBuilder sb = new StringBuilder();
        ResultSet rs = null;
        try {
            if (stmt == null) {
                sb.append("select count(*) n, avg(x3), std(x3) ");
                sb.append("from scp_residue_05 ");
                sb.append("where scp_method_id = ? ");
                sb.append("and x3 is not null ");
                System.out.println(sb.toString());
                stmt = conn.prepareStatement(sb.toString());
            }
            int i = 1;
            stmt.setInt(i++, methodId);
            System.out.println(new Date());
            rs = stmt.executeQuery();
            if (rs.next()) {
                i=0;
                result[i++] = rs.getDouble(i);
                result[i++] = rs.getDouble(i);
                result[i++] = rs.getDouble(i);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResultsSelect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public double[] getTotalX4(int methodId) {
        double result[] = {0, 0, 0};
        StringBuilder sb = new StringBuilder();
        ResultSet rs = null;
        try {
            if (stmt == null) {
                sb.append("select count(*) n, avg(x4), std(x4) ");
                sb.append("from scp_residue_05 ");
                sb.append("where scp_method_id = ? ");
                sb.append("and x4 is not null ");
                System.out.println(sb.toString());
                stmt = conn.prepareStatement(sb.toString());
            }
            int i = 1;
            stmt.setInt(i++, methodId);
            System.out.println(new Date());
            rs = stmt.executeQuery();
            if (rs.next()) {
                i=0;
                result[i++] = rs.getDouble(i);
                result[i++] = rs.getDouble(i);
                result[i++] = rs.getDouble(i);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResultsSelect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public double[] getN(int methodId, int cutoff) {
        double result[] = {0, 0, 0};
        StringBuilder sb = new StringBuilder();
        ResultSet rs = null;
        try {
            if (stmt == null) {
                sb.append("select count(*) n, avg(rmsd), std(rmsd) ");
                sb.append("from scp_residue_05 ");
                sb.append("where scp_method_id = ? ");
                sb.append("and x1 is not null ");
                sb.append("and x1 <= ? ");
                System.out.println(sb.toString());
                stmt = conn.prepareStatement(sb.toString());
            }
            int i = 1;
            stmt.setInt(i++, methodId);
            stmt.setInt(i++, cutoff);
            System.out.println(new Date());
            rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("paso x aqui");
                i=0;
                result[i++] = rs.getDouble(i);
                result[i++] = rs.getDouble(i);
                result[i++] = rs.getDouble(i);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResultsSelect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public double[] getX1(int methodId, int cutoff) {
        double[] result = {0,0,0};
        StringBuilder sb = new StringBuilder();
        ResultSet rs = null;
        try {
            if (stmt == null) {
                sb.append("select count(*) n, avg(x1), std(x1) ");
                sb.append("from scp_residue_05 ");
                sb.append("where scp_method_id = ? ");
                sb.append("and x1 is not null ");
                sb.append("and x1 <= ? ");
                System.out.println(sb.toString());
                stmt = conn.prepareStatement(sb.toString());
            }
            int i = 1;
            stmt.setInt(i++, methodId);
            stmt.setInt(i++, cutoff);
            System.out.println(new Date());
            rs = stmt.executeQuery();
            if (rs.next()) {
                i=0;
                result[i++] = rs.getDouble(i);
                result[i++] = rs.getDouble(i);
                result[i++] = rs.getDouble(i);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResultsSelect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public double[] getX2(int methodId, int cutoff) {
        double[] result = {0,0,0};
        StringBuilder sb = new StringBuilder();
        ResultSet rs = null;
        try {
            if (stmt == null) {
                sb.append("select count(*) n, avg(x2), std(x2) ");
                sb.append("from scp_residue_05 ");
                sb.append("where scp_method_id = ? ");
                sb.append("and x2 is not null ");
                sb.append("and x1 <= ? ");
                sb.append("and x2 <= ? ");
                System.out.println(sb.toString());
                stmt = conn.prepareStatement(sb.toString());
            }
            int i = 1;
            stmt.setInt(i++, methodId);
            stmt.setInt(i++, cutoff);
            stmt.setInt(i++, cutoff);
            System.out.println(new Date());
            rs = stmt.executeQuery();
            if (rs.next()) {
                i=0;
                result[i++] = rs.getDouble(i);
                result[i++] = rs.getDouble(i);
                result[i++] = rs.getDouble(i);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResultsSelect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public double[] getX3(int methodId, int cutoff) {
        double[] result = {0,0,0};
        StringBuilder sb = new StringBuilder();
        ResultSet rs = null;
        try {
            if (stmt == null) {
                sb.append("select count(*) n, avg(x3), std(x3) ");
                sb.append("from scp_residue_05 ");
                sb.append("where scp_method_id = ? ");
                sb.append("and x3 is not null ");
                sb.append("and x1 <= ? ");
                sb.append("and x2 <= ? ");
                sb.append("and x3 <= ? ");
                System.out.println(sb.toString());
                stmt = conn.prepareStatement(sb.toString());
            }
            int i = 1;
            stmt.setInt(i++, methodId);
            stmt.setInt(i++, cutoff);
            stmt.setInt(i++, cutoff);
            stmt.setInt(i++, cutoff);
            System.out.println(new Date());
            rs = stmt.executeQuery();
            if (rs.next()) {
                i=0;
                result[i++] = rs.getDouble(i);
                result[i++] = rs.getDouble(i);
                result[i++] = rs.getDouble(i);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResultsSelect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public double[] getX4(int methodId, int cutoff) {
        double[] result = {0,0,0};
        StringBuilder sb = new StringBuilder();
        ResultSet rs = null;
        try {
            if (stmt == null) {
                sb.append("select count(*) n, avg(x4), std(x4) ");
                sb.append("from scp_residue_05 ");
                sb.append("where scp_method_id = ? ");
                sb.append("and x4 is not null ");
                sb.append("and x1 <= ? ");
                sb.append("and x2 <= ? ");
                sb.append("and x3 <= ? ");
                sb.append("and x4 <= ? ");
                System.out.println(sb.toString());
                stmt = conn.prepareStatement(sb.toString());
            }
            int i = 1;
            stmt.setInt(i++, methodId);
            stmt.setInt(i++, cutoff);
            stmt.setInt(i++, cutoff);
            stmt.setInt(i++, cutoff);
            stmt.setInt(i++, cutoff);
            System.out.println(new Date());
            rs = stmt.executeQuery();
            if (rs.next()) {
                i=0;
                result[i++] = rs.getDouble(i);
                result[i++] = rs.getDouble(i);
                result[i++] = rs.getDouble(i);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResultsSelect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }


    public void setStmt(PreparedStatement stmt) {
        this.stmt = stmt;
    }
}
