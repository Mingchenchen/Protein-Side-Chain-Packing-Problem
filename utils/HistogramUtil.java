/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;

import utils.db.HistogramSelect;

/**
 *
 * @author administrator
 */
public class HistogramUtil
{

    static public void main(String[] args)
    {
        HistogramSelect select = new HistogramSelect();
        ArrayList<double[]> hist = select.getHistogramRMSD(1);
        for (int i = 0; i < hist.size(); i++)
        {
            for (int j = 0; j < hist.get(i).length; j++)
            {
                System.out.print(hist.get(i)[j] + "\t");
            }
            System.out.println();
        }
    }

    static public void mainRMSDAA(String[] args)
    {
        HistogramSelect select = new HistogramSelect();
        ArrayList<double[]> rmsdAA = select.getRmsdAA();
        for (int i = 0; i < rmsdAA.size(); i++)
        {
            for (int j = 0; j < rmsdAA.get(i).length; j++)
            {
                System.out.print(rmsdAA.get(i)[j] + "\t");
            }
            System.out.println();
        }
    }

    static public void mainHistogram(String[] args)
    {
        HistogramSelect select = new HistogramSelect();
        ArrayList<double[]> scwrl4 = select.getHistogram(0, 2);
        System.out.println("SCWRL4");
        for (int i = 0; i < scwrl4.size(); i++)
        {
            System.out.println(scwrl4.get(i)[0] + "\t" + scwrl4.get(i)[1]);
        }
        ArrayList<double[]> opusRota = select.getHistogram(0, 3);
        System.out.println("OPUS ROTA");
        for (int i = 0; i < opusRota.size(); i++)
        {
            System.out.println(opusRota.get(i)[0] + "\t" + opusRota.get(i)[1]);
        }
    }
}
