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
public class HistogramSelect {

    private Connection conn = null;
    private PreparedStatement stmt = null;

    public HistogramSelect() {
        conn = (new DBConnection()).getConnection();
    }

    protected void finalize() {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(HistogramSelect.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            super.finalize();
        } catch (Throwable ex) {
            Logger.getLogger(HistogramSelect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<double[]> getHistogram(int decimalPlaces, int methodId) {
        ArrayList<double[]> result = null;
        StringBuilder sb = new StringBuilder();
        ResultSet rs = null;
        try {
            if (stmt == null) {
                sb.append("select x1, count(*) n ");
                sb.append("from( ");
                sb.append("select round(x1,?) x1 ");
                sb.append("from scp_residue_05 ");
                sb.append("where scp_method_id = ? ");
                sb.append("and x1 is not null ");
                sb.append(") scp ");
                sb.append("group by x1 ");
                System.out.println(sb.toString());
                stmt = conn.prepareStatement(sb.toString());
            }
            int i = 1;
            stmt.setInt(i++, decimalPlaces);
            stmt.setInt(i++, methodId);
            System.out.println(new Date());
            rs = stmt.executeQuery();
            while (rs.next()) {
                result = result == null ? new ArrayList<double[]>() : result;
                double[] tmp = {0, 0};
                i = 0;
                tmp[i++] = rs.getDouble(i);
                tmp[i++] = rs.getDouble(i);
                result.add(tmp);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResultsSelect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public ArrayList<double[]> getHistogramRMSD(int decimalPlaces) {
        ArrayList<double[]> result = null;
        StringBuilder sb = new StringBuilder();
        ResultSet rs = null;
        try {
            if (stmt == null) {
                sb.append("select method, aa, rmsd, count(*) n ");
                sb.append("from( ");
                sb.append("select scp_method_id method, aa, round(rmsd,?) rmsd ");
                sb.append("from scp_residue_05 ");
                sb.append(") scp ");
                sb.append("group by method, aa, rmsd ");
                System.out.println(sb.toString());
                stmt = conn.prepareStatement(sb.toString());
            }
            int i = 1;
            stmt.setInt(i++, decimalPlaces);
            System.out.println(new Date());
            rs = stmt.executeQuery();
            while (rs.next()) {
                result = result == null ? new ArrayList<double[]>() : result;
                double[] tmp = {0, 0, 0, 0};
                i = 0;
                tmp[i++] = rs.getDouble(i);
                tmp[i++] = rs.getDouble(i);
                tmp[i++] = rs.getDouble(i);
                tmp[i++] = rs.getDouble(i);
                result.add(tmp);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResultsSelect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public ArrayList<double[]> getRmsdAA() {
        ArrayList<double[]> result = null;
        StringBuilder sb = new StringBuilder();
        ResultSet rs = null;
        try {
            if (stmt == null) {
                sb.append("select scp_method_id method, aa amino_acid_type,  ");
                sb.append("avg(rmsd)+1.96*std(rmsd)/sqrt(count(*)) open,  ");
                sb.append("max(rmsd) high, min(rmsd) low,  ");
                sb.append("avg(rmsd)-1.96*std(rmsd)/sqrt(count(*)) close ");
                sb.append("from scp_residue_05 ");
                sb.append("group by scp_method_id, aa ");
                System.out.println(sb.toString());
                stmt = conn.prepareStatement(sb.toString());
            }
            int i = 1;
            System.out.println(new Date());
            rs = stmt.executeQuery();
            while (rs.next()) {
                result = result == null ? new ArrayList<double[]>() : result;
                double[] tmp = {0, 0, 0, 0, 0, 0};
                i = 0;
                tmp[i++] = rs.getDouble(i);
                tmp[i++] = rs.getDouble(i);
                tmp[i++] = rs.getDouble(i);
                tmp[i++] = rs.getDouble(i);
                tmp[i++] = rs.getDouble(i);
                tmp[i++] = rs.getDouble(i);
                result.add(tmp);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResultsSelect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public ArrayList<String> getPDBAcc() {
        ArrayList<String> result = null;
        StringBuilder sb = new StringBuilder();
        ResultSet rs = null;
        try {
            if (stmt == null) {
                sb.append("select pdb_id, sum(acc)/count(*) acc ");
                sb.append("from scp_residue_05 ");
                sb.append("where scp_method_id = 2 ");
                sb.append("group by pdb_id ");
                sb.append("order by 1 ");
                System.out.println(sb.toString());
                stmt = conn.prepareStatement(sb.toString());
            }
            int i = 1;
            System.out.println(new Date());
            rs = stmt.executeQuery();
            while (rs.next()) {
                result = result == null ? new ArrayList<String>() : result;
                result.add(rs.getString(1) + "\t" + rs.getDouble(2));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResultsSelect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
