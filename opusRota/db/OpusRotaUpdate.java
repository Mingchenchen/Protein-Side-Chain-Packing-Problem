/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opusRota.db;

import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.biojava.bio.structure.AminoAcid;
import org.biojava.bio.structure.Structure;
import utils.StructureUtil;

/**
 *
 * @author administrator
 */
public class OpusRotaUpdate {

    private Connection conn = null;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private PreparedStatement stmt = null;

    public OpusRotaUpdate() {
        conn = (new DBConnection()).getConnection();
    }

    protected void finalize() {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(OpusRotaInsert.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            super.finalize();
        } catch (Throwable ex) {
            Logger.getLogger(OpusRotaInsert.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //public boolean updateRMSD(Structure sPDB, Structure sOPUSRota, int chain, int residue, int expNum) {
    public boolean updateRMSD(AminoAcid aa1, AminoAcid aa2, AminoAcid aa3, int expNum) {
        //AminoAcid aa = (AminoAcid) sPDB.getChain(chain).getAtomGroup(residue);
        boolean insertedOk = true;
        try {
            if (stmt == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("update scp_residue ");
                sb.append("set rmsd = ? ");
                sb.append(", updated_on = ? ");
                sb.append("where pdb_residue_id = ");
                sb.append("get_pdb_residue_id(?,?,?) ");
                sb.append("and exp_number = ? ");
                sb.append("and scp_method_id = 2 ");
                System.out.println("sb = " + sb.toString());
                stmt = conn.prepareStatement(sb.toString());
            }
            int i = 1;
            stmt.setDouble(i++, Double.parseDouble(StructureUtil.getRMSD(aa1, aa3)));
            stmt.setString(i++, sdf.format(new Date()));
            stmt.setString(i++, aa1.getParent().getParent().getPDBCode());
            stmt.setString(i++, aa1.getParent().getName());
            stmt.setString(i++, aa1.getPDBCode());
            stmt.setInt(i++, 1);
            return stmt.executeUpdate()==1;
        } catch (SQLException ex) {
            insertedOk = false;
            Logger.getLogger(OpusRotaInsert.class.getName()).log(Level.SEVERE, null, ex);
        }
        return insertedOk;
    }
}
