package utils;

import java.util.Arrays;

public class BondUtil
{

    static public final String[][] ATOM_TYPE =
    {
        {"NH1", "CH1E", "CH3E"}, //ALA
        {"NH1", "CH1E", "CH2E", "CH2E", "CH2E", "NH1", "C"}, //ARG
        {"NH1", "CH1E", "CH2E", "C", "O"}, //ASN
        {"NH1", "CH1E", "CH2E", "C", "OC"}, //ASP
        {"NH1", "CH1E", "CH2E", "SH1E"}, //CYS
        {"NH1", "CH1E", "CH2E", "CH2E", "C", "O"}, //GLN
        {"NH1", "CH1E", "CH2E", "CH2E", "C", "OC"}, //GLU
        {"NH1", "CH2G"}, //GLY
        {"NH1", "CH1E", "CH2E", "C5", "NR"}, //HIS
        {"NH1", "CH1E", "CH1E", "CH2E", "CH3E"}, //ILE
        {"NH1", "CH1E", "CH2E", "CH1E", "CH3E"}, //LEU
        {"NH1", "CH1E", "CH2E", "CH2E", "CH2E", "CH2E", "NH3"}, //LYS
        {"NH1", "CH1E", "CH2E", "CH2E", "SM", "CH3E"}, //MET
        {"NH1", "CH1E", "CH2E", "CF", "CR1E"}, //PHE
        {"N", "CH1E", "CH2E", "CH2P", "CH2P"}, //PRO
        {"NH1", "CH1E", "CH2E", "OH1"}, //SER
        {"NH1", "CH1E", "CH1E", "OH1"}, //THR
        {"NH1", "CH1E", "CH2E", "C5W", "CR1E"}, //TRP
        {"NH1", "CH1E", "CH2E", "CY", "CR1E"}, //TYR
        {"NH1", "CH1E", "CH1E", "CH3E"}   //VAL
    };
   
    
    static public final double[][] BOND_LENGTH =
    {
        {}, //ALA
        {1.52, 1.52, 1.46, 1.329}, //ARG
        {1.516, 1.231}, //ASN
        {1.516, 1.249}, //ASP
        {1.808}, //CYS
        {1.53, 1.516, 1.231}, //GLN
        {1.52, 1.516, 1.249}, //GLU
        {}, //GLY
        {1.497, 1.371}, //HIS
        {1.53, 1.513}, //ILE
        {1.53, 1.521}, //LEU
        {1.52, 1.52, 1.52, 1.489}, //LYS
        {1.52, 1.803, 1.791}, //MET
        {1.502, 1.384}, //PHE
        {1.492, 1.503}, //PRO
        {1.417}, //SER
        {1.433}, //THR
        {1.498, 1.365}, //TRP
        {1.512, 1.389}, //TYR
        {1.521}   //VAL
    };
    static public final double[][] BOND_ANGLE =
    {
        {}, //ALA
        {114.1, 111.3, 112.0, 116.5}, //ARG
        {112.6, 120.8}, //ASN
        {112.6, 118.4}, //ASP
        {114.4}, //CYS
        {114.1, 112.6, 120.8}, //GLN
        {114.1, 112.6, 118.4}, //GLU
        {}, //GLY
        {113.8, 121.6}, //HIS
        {110.4, 114.1}, //ILE
        {116.3, 110.7}, //LEU
        {114.1, 111.3, 111.3, 111.9}, //LYS
        {114.1, 112.7, 100.9}, //MET
        {113.8, 120.7}, //PHE
        {104.5, 106.1}, //PRO
        {111.1}, //SER
        {109.6}, //THR
        {113.6, 126.9}, //TRP
        {113.9, 120.8}, //TYR
        {110.5}   //VAL
    };
    
/*
    static public final double[][] BOND_LENGTH =
    {
        {}, //ALA
        {1.52, 1.52, 1.46, 1.329}, //ARG                   ok
        {1.516, 1.231}, //ASN                              ok
        {1.516, 1.249}, //ASP                              ok
        {1.808}, //CYS                                     ok
        {1.52, 1.516, 1.231}, //GLN                    NOT ok
        {1.52, 1.516, 1.249}, //GLU                        ok
        {}, //GLY
        {1.497, 1.377}, //HIS                          NOT ok
        {1.53, 1.513}, //ILE                               ok
        {1.53, 1.521}, //LEU                               ok
        {1.52, 1.52, 1.52, 1.489}, //LYS                   ok
        {1.52, 1.803, 1.791}, //MET                        ok
        {1.502, 1.384}, //PHE                               ok
        {1.492, 1.503}, //PRO                               ok
        {1.417}, //SER                                      ok
        {1.433}, //THR                                      ok
        {1.498, 1.365}, //TRP                               ok
        {1.512, 1.389}, //TYR                               ok
        {1.521}   //VAL                                     ok
    };
    static public final double[][] BOND_ANGLE =
    {
        {}, //ALA
        {114.1, 111.3, 112.0, 124.2}, //ARG          NOT ok
        {112.6, 120.8}, //ASN                        ok
        {112.6, 118.4}, //ASP                        ok
        {114.4}, //CYS                               ok
        {114.1, 112.6, 120.8}, //GLN                 ok
        {114.1, 112.6, 118.4}, //GLU                 ok
        {}, //GLY
        {113.8, 122.7}, //HIS                        NOT ok
        {110.4, 113.8}, //ILE                        NOT ok
        {116.3, 110.7}, //LEU                        ok
        {114.1, 111.3, 111.3, 111.9}, //LYS          ok
        {114.1, 112.7, 100.9}, //MET                 ok
        {113.8, 120.7}, //PHE                        ok
        {104.5, 106.1}, //PRO                        ok
        {111.1}, //SER                               ok
        {109.6}, //THR                               ok
        {113.6, 126.9}, //TRP                        ok
        {113.9, 120.8}, //TYR                        ok
        {110.4}   //VAL                              NOT ok
    };
*/



    public BondUtil()
    {}

    public static double getBondAngle(String aminoAcid, int index)
    {
        int aminoAcidIndex = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aminoAcid);
        return BOND_ANGLE[aminoAcidIndex][index - 1];
    }

    public static double getBondLength(String aminoAcid, int index)
    {
        int aminoAcidIndex = Arrays.binarySearch(ListUtil.THREE_LETTER_CODE, aminoAcid);
        return BOND_LENGTH[aminoAcidIndex][index - 1];
    }
}