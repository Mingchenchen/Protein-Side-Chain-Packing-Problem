package scwrl4;

import java.util.ArrayList;
import pdb.PDBReader;
import scwrl4.entity.SCWRL4LogEntity;
import scwrl4.io.SCWRL4Reader;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author rcorona
 */
public class TestSCWRL4
{

    static public void main(String[] args)
    {
        ArrayList<String> alPDBIds = PDBReader.readPDBList("pdbIds03");
        scwrl4.SCWRL4Run.runSCWRL4(alPDBIds);

      /*
        ArrayList<String> alPDBIds = PDBReader.readPDBList("pdbIds03");
        for (int i = 0; i < alPDBIds.size(); i++)
        {
            SCWRL4LogEntity log = SCWRL4Reader.readLogFile(alPDBIds.get(i).toLowerCase());
            System.out.println(alPDBIds.get(i) + "\t" + log.getTotalTime() + " s\t" + log.getTime("Scwrl4_Execution") + " s");
        }
       */
    }
}
