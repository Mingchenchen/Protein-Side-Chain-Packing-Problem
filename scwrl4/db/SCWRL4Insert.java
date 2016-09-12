/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scwrl4.db;

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
public class SCWRL4Insert
{

    private Connection conn = null;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public SCWRL4Insert()
    {
        conn = (new DBConnection()).getConnection();
    }

    protected void finalize()
    {
        try
        {
            conn.close();
        } 
        catch (SQLException ex)
        {
            Logger.getLogger(SCWRL4Insert.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try
        {
            super.finalize();
        } 
        catch (Throwable ex)
        {
            Logger.getLogger(SCWRL4Insert.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean insertSCPResidue(Structure sPDB, Structure sSCWRL4, int chain, int residue)
    {
        AminoAcid aa = (AminoAcid) sSCWRL4.getChain(chain).getAtomGroup(residue);
        Statement stmt = null;
        boolean insertedOk = true;
        try
        {
            StringBuilder sb = new StringBuilder();
            sb.append("insert into scp_residue values (null, get_pdb_residue_id('");
            sb.append(aa.getParent().getParent().getPDBCode());
            sb.append("', '");
            sb.append(aa.getParent().getName());
            sb.append("', '");
            sb.append(aa.getPDBCode());
            sb.append("'), 2, 1, null, null, null, ");
            sb.append(StructureUtil.getX1(sSCWRL4, chain, residue));
            sb.append(", ");
            sb.append(StructureUtil.getX2(sSCWRL4, chain, residue));
            sb.append(", ");
            sb.append(StructureUtil.getX3(sSCWRL4, chain, residue));
            sb.append(", ");
            sb.append(StructureUtil.getX4(sSCWRL4, chain, residue));
            sb.append(", ");
            sb.append(StructureUtil.getX5(sSCWRL4, chain, residue));
            sb.append(", ");
            sb.append(StructureUtil.getRMSD(sPDB, sSCWRL4, chain, residue));
            sb.append(", '");
            sb.append(sdf.format(new Date()));
            sb.append("');");
            //System.out.println(sb.toString());
            stmt = conn.createStatement();
            stmt.executeUpdate(sb.toString());
        } 
        catch (SQLException ex)
        {
            insertedOk = false;
            Logger.getLogger(SCWRL4Insert.class.getName()).log(Level.SEVERE, null, ex);
        }
        return insertedOk;
    }
}
