package opusRota;


import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rcorona
 */
public class TestOpusRota
{

    static public void main(String[] args)
    {
        ArrayList<String> alPDBIds = new ArrayList();
        alPDBIds.add("1atz");
        opusRota.OpusRotaRun.runOpusRota(alPDBIds, 3);
    }
}
