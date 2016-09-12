/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pdb.db;

import db.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.biojava.bio.structure.AminoAcid;
import org.biojava.bio.structure.Atom;
import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.Structure;
import utils.StructureUtil;

/**
 *
 * @author administrator
 */
public class PDBInserts {

    private Connection conn = null;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public PDBInserts() {
        conn = (new DBConnection()).getConnection();
    }

    protected void finalize() {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(PDBInserts.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            super.finalize();
        } catch (Throwable ex) {
            Logger.getLogger(PDBInserts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean insertStructure(Structure s) {
        Statement stmt = null;
        boolean insertedOk = true;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("insert into pdb_entry ");
            sb.append("values (null, '");
            sb.append(s.getPDBCode());
            sb.append("', null, null, null,'");
            sb.append(sdf.format(new Date()));
            sb.append("');");
            stmt = conn.createStatement();
            stmt.executeUpdate(sb.toString());
        } catch (SQLException ex) {
            insertedOk = false;
            Logger.getLogger(PDBInserts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return insertedOk;
    }

    public boolean insertChain(Chain chain) {
        Statement stmt = null;
        boolean insertedOk = true;
        try {
            conn.setAutoCommit(false);
            StringBuilder sb = new StringBuilder();
            sb.append("insert into pdb_chain ");
            sb.append("values (null, get_pdb_entry_id('");
            sb.append(chain.getParent().getPDBCode());
            sb.append("'), '");
            sb.append(chain.getName());
            sb.append("',");
            sb.append(chain.getSeqResLength());
            sb.append(",'");
            sb.append(sdf.format(new Date()));
            sb.append("');");
            stmt = conn.createStatement();
            stmt.executeUpdate(sb.toString());
        } catch (SQLException ex) {
            insertedOk = false;
            Logger.getLogger(PDBInserts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return insertedOk;
    }

    public boolean insertResidue(AminoAcid aa) {
        Statement stmt = null;
        boolean insertedOk = true;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("insert into pdb_residue values(null, get_pdb_chain_id('");
            sb.append(aa.getParent().getParent().getPDBCode());
            sb.append("', '");
            sb.append(aa.getParent().getName());
            sb.append("'), null, null, '");
            sb.append(aa.getPDBCode());
            sb.append("', get_amino_acid_id('");
            sb.append(aa.getPDBName());
            sb.append("'), null, null, null, null, null, '");
            sb.append(sdf.format(new Date()));
            sb.append("');");
            stmt = conn.createStatement();
            stmt.executeUpdate(sb.toString());
        } catch (SQLException ex) {
            insertedOk = false;
            Logger.getLogger(PDBInserts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return insertedOk;
    }

    public boolean insertAtom(Atom a) {
        Statement stmt = null;
        boolean insertedOk = true;
        try {
            conn.setAutoCommit(false);
            StringBuilder sb = new StringBuilder();
            sb.append("insert into pdb_atom ");
            sb.append("values (null, ");
            sb.append("get_atom_id('");
            sb.append(a.getParent().getPDBName());
            sb.append("', '");
            sb.append(a.getName());
            sb.append("'), get_pdb_residue_id('");
            sb.append(a.getParent().getParent().getParent().getPDBCode());
            sb.append("', '");
            sb.append(a.getParent().getParent().getName());
            sb.append("', '");
            sb.append(a.getParent().getPDBCode());
            sb.append("'), ");
            sb.append(a.getPDBserial());
            sb.append(", '");
            sb.append(a.getAltLoc());
            sb.append("', ");
            sb.append(a.getX());
            sb.append(", ");
            sb.append(a.getY());
            sb.append(", ");
            sb.append(a.getZ());
            sb.append(", ");
            sb.append(a.getOccupancy());
            sb.append(", ");
            sb.append(a.getTempFactor());
            sb.append(", '");
            //sb.append(a.getCharge()); falta obtener la carga :P
            sb.append("', '");
            sb.append(sdf.format(new Date()));
            sb.append("');");
            stmt = conn.createStatement();
            stmt.executeUpdate(sb.toString());
        } catch (SQLException ex) {
            insertedOk = false;
            Logger.getLogger(PDBInserts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return insertedOk;
    }

    public boolean insertECNumbers(String pdbId, String ecNumber) {
        Statement stmt = null;
        boolean insertedOk = true;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("insert into ec_numbers values( null, get_pdb_entry_id('");
            sb.append(pdbId);
            sb.append("'), '");
            sb.append(ecNumber);
            sb.append("', '");
            sb.append(sdf.format(new Date()));
            sb.append("');");
            stmt = conn.createStatement();
            stmt.executeUpdate(sb.toString());
        } catch (SQLException ex) {
            insertedOk = false;
            Logger.getLogger(PDBInserts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return insertedOk;
    }

    public boolean insertSCPResidue(Structure sPDB, int chain, int residue) {
        AminoAcid aa = (AminoAcid) sPDB.getChain(chain).getAtomGroup(residue);
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
            sb.append("'), 1, 1, null, null, null, ");
            sb.append(StructureUtil.getX1(sPDB, chain, residue));
            sb.append(", ");
            sb.append(StructureUtil.getX2(sPDB, chain, residue));
            sb.append(", ");
            sb.append(StructureUtil.getX3(sPDB, chain, residue));
            sb.append(", ");
            sb.append(StructureUtil.getX4(sPDB, chain, residue));
            sb.append(", ");
            sb.append(StructureUtil.getX5(sPDB, chain, residue));
            sb.append(", 0, '");
            sb.append(sdf.format(new Date()));
            sb.append("');");
            stmt = conn.createStatement();
            stmt.executeUpdate(sb.toString());
        } catch (SQLException ex) {
            insertedOk = false;
            Logger.getLogger(PDBInserts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return insertedOk;
    }

    public boolean insertSCPResidue(Structure sIn, Structure sOut, int chain, int residue) {
        AminoAcid aa = (AminoAcid) sOut.getChain(chain).getAtomGroup(residue);
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
            sb.append("'), 4, 1, null, null, null, ");
            sb.append(StructureUtil.getX1(sOut, chain, residue));
            sb.append(", ");
            sb.append(StructureUtil.getX2(sOut, chain, residue));
            sb.append(", ");
            sb.append(StructureUtil.getX3(sOut, chain, residue));
            sb.append(", ");
            sb.append(StructureUtil.getX4(sOut, chain, residue));
            sb.append(", ");
            sb.append(StructureUtil.getX5(sOut, chain, residue));
            sb.append(", ");
            sb.append(StructureUtil.getRMSD(sIn, sOut, chain, residue));
            sb.append(", '");
            sb.append(sdf.format(new Date()));
            sb.append("');");
            //System.out.println(sb.toString());
            stmt = conn.createStatement();
            stmt.executeUpdate(sb.toString());
        } catch (SQLException ex) {
            insertedOk = false;
            Logger.getLogger(PDBInserts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return insertedOk;
    }
}
