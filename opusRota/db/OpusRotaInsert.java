/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opusRota.db;

import db.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
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
public class OpusRotaInsert {

    private Connection conn = null;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public OpusRotaInsert() {
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

    public boolean insertSCPResidue(Structure sPDB, Structure sOPUSRota, int chain, int residue, int expNum) {
        AminoAcid aa = (AminoAcid) sPDB.getChain(chain).getAtomGroup(residue);
        //AminoAcid aa2 = (AminoAcid) sOPUSRota.getChain(chain).getAtomGroup(residue);

        //System.out.println("aa = " + aa);
        //System.out.println("aa2 = " + aa2);

        Statement stmt = null;
        boolean insertedOk = true;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("insert into scp_residue values (null, get_pdb_residue_id('");
            sb.append(aa.getParent().getParent().getPDBCode());
            sb.append("', '");
            sb.append(aa.getParent().getName());
            sb.append("', '");
            sb.append(aa.getPDBCode());
            sb.append("'), 5, ");
            sb.append(expNum);
            sb.append(", null, null, null, ");
            sb.append(StructureUtil.getX1(sOPUSRota, chain, residue));
            sb.append(", ");
            sb.append(StructureUtil.getX2(sOPUSRota, chain, residue));
            sb.append(", ");
            sb.append(StructureUtil.getX3(sOPUSRota, chain, residue));
            sb.append(", ");
            sb.append(StructureUtil.getX4(sOPUSRota, chain, residue));
            sb.append(", ");
            sb.append(StructureUtil.getX5(sOPUSRota, chain, residue));
            sb.append(", ");
            sb.append(StructureUtil.getRMSD(sPDB, sOPUSRota, chain, residue));
            sb.append(", '");
            sb.append(sdf.format(new Date()));
            sb.append("');");
            //System.out.println(sb.toString());
            stmt = conn.createStatement();
            stmt.executeUpdate(sb.toString());
        } catch (SQLException ex) {
            insertedOk = false;
            Logger.getLogger(OpusRotaInsert.class.getName()).log(Level.SEVERE, null, ex);
        }
        return insertedOk;
    }
}
