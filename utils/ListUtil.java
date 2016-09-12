package utils;

import java.util.Arrays;
import utils.db.CatalogsInserts;

public class ListUtil
{

    public static final String[][] ATOM_LIST =
    {
        {"N", "CA", "C", "O", "CB"}, //ALA
        {"N", "CA", "C", "O", "CB", "CG", "CD", "NE", "CZ", "NH1", "NH2"}, //ARG
        {"N", "CA", "C", "O", "CB", "CG", "OD1", "ND2"}, //ASN
        {"N", "CA", "C", "O", "CB", "CG", "OD1", "OD2"}, //ASP
        {"N", "CA", "C", "O", "CB", "SG"}, //CYS
        {"N", "CA", "C", "O", "CB", "CG", "CD", "OE1", "NE2"}, //GLN
        {"N", "CA", "C", "O", "CB", "CG", "CD", "OE1", "OE2"}, //GLU
        {"N", "CA", "C", "O"}, //GLY
        {"N", "CA", "C", "O", "CB", "CG", "ND1", "CD2", "CE1", "NE2"}, //HIS
        {"N", "CA", "C", "O", "CB", "CG1", "CG2", "CD1"}, //ILE
        {"N", "CA", "C", "O", "CB", "CG", "CD1", "CD2"}, //LEU
        {"N", "CA", "C", "O", "CB", "CG", "CD", "CE", "NZ"}, //LYS
        {"N", "CA", "C", "O", "CB", "CG", "SD", "CE"}, //MET
        {"N", "CA", "C", "O", "CB", "CG", "CD1", "CD2", "CE1", "CE2", "CZ"}, //PHE
        {"N", "CA", "C", "O", "CB", "CG", "CD"}, //PRO
        {"N", "CA", "C", "O", "CB", "OG"}, //SER
        {"N", "CA", "C", "O", "CB", "OG1", "CG2"}, //THR
        {"N", "CA", "C", "O", "CB", "CG", "CD1", "CD2", "NE1", "CE2", "CE3", "CZ2", "CZ3", "CH2"}, //TRP
        {"N", "CA", "C", "O", "CB", "CG", "CD1", "CD2", "CE1", "CE2", "CZ", "OH"}, //TYR
        {"N", "CA", "C", "O", "CB", "CG1", "CG2"} //VAL
    };
    public static final String[][] SIDE_CHAIN_ATOM_LIST =
    {                                                       
        {"N", "CA", "CB"}, //ALA                            
        {"N", "CA", "CB", "CG", "CD", "NE", "CZ"}, //ARG    
        {"N", "CA", "CB", "CG", "OD1"}, //ASN               
        {"N", "CA", "CB", "CG", "OD1"}, //ASP
        {"N", "CA", "CB", "SG"}, //CYS
        {"N", "CA", "CB", "CG", "CD", "OE1"}, //GLN
        {"N", "CA", "CB", "CG", "CD", "OE1"}, //GLU
        {"N", "CA"}, //GLY
        {"N", "CA", "CB", "CG", "ND1"}, //HIS
        {"N", "CA", "CB", "CG1", "CD1"}, //ILE
        {"N", "CA", "CB", "CG", "CD1"}, //LEU
        {"N", "CA", "CB", "CG", "CD", "CE", "NZ"}, //LYS
        {"N", "CA", "CB", "CG", "SD", "CE"}, //MET
        {"N", "CA", "CB", "CG", "CD1"}, //PHE
        {"N", "CA", "CB", "CG", "CD"}, //PRO
        {"N", "CA", "CB", "OG"}, //SER
        {"N", "CA", "CB", "OG1"}, //THR
        {"N", "CA", "CB", "CG", "CD1"}, //TRP
        {"N", "CA", "CB", "CG", "CD1"}, //TYR
        {"N", "CA", "CB", "CG1"} //VAL
    };
    public static final String[] THREE_LETTER_CODE = {"ALA", "ARG", "ASN",
        "ASP", "CYS", "GLN", "GLU", "GLY", "HIS", "ILE", "LEU", "LYS", "MET",
        "PHE", "PRO", "SER", "THR", "TRP", "TYR", "VAL"};
    public static final char[] ONE_LETTER_CODE = {'A', 'R', 'N', 'D', 'C', 'Q',
        'E', 'G', 'H', 'I', 'L', 'K', 'M', 'F', 'P', 'S', 'T', 'W', 'Y', 'V'};
    public static final String[] AA_NAME = {"alanine", "arginine",
        "asparagine", "aspartic acid", "cysteine", "glutamine", "glutamic acid",
        "glycine", "histidine", "isoleucine", "leucine", "lysine", "methionine",
        "phenilalanine", "proline", "serine", "threonine", "triptophan",
        "tyrosine", "valine"};

    //ORDER BY THREE_LETTER_CODE
    //Miller, S., Janin, J., Lesk, A.M. & Chothia, C.
    //Interior and surface of monomeric proteins.
    //Journal of Molecular Biology 196, 641-656 (1987).
    public static final int[] AA_MAX_ACC = {113, 241, 158, 151, 140, 189, 183, 85, 194, 182, 180, 211, 204, 218, 143, 122, 146, 259, 229, 160};

    public ListUtil()
    {}

    public static boolean isAminoAcid(String aminoAcid)
    {
        int i = Arrays.binarySearch(THREE_LETTER_CODE, aminoAcid);
        
        if (i < 0 || i > 19)
        {
           return false;
        }
        else
        {
           return THREE_LETTER_CODE[i].equalsIgnoreCase(aminoAcid);
        }
    }

    static public void main(String[] args)
    {
        CatalogsInserts catIns = new CatalogsInserts();
        for (int i = 0; i < THREE_LETTER_CODE.length; i++)
        {
            int aminoAcidId = catIns.insertAminoAcid(ONE_LETTER_CODE[i], THREE_LETTER_CODE[i], AA_NAME[i], AA_MAX_ACC[i]);
            for (int j = 0; j < ATOM_LIST[i].length; j++)
            {
                catIns.insertAtom(aminoAcidId, ATOM_LIST[i][j], ATOM_LIST[i][j].charAt(0) + "");
            }
        }
    }
}

