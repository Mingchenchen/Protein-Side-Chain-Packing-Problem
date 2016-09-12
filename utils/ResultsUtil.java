/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.db.HistogramSelect;
import utils.db.ResultsSelect;
import utils.db.ResultsSelectAA;
import utils.db.ResultsSelectEC;
import utils.db.ResultsSelectPDBID;
import utils.db.ResultsSelectSCOP;
import utils.entity.ResultsEntity;

/**
 *
 * @author administrator
 */
public class ResultsUtil
{

    static int iniCutoff = 40;
    static int endCutoff = 10;
    static int stepSize = 10;
    static int iniMethod = 2;
    static int endMethod = 3;

    static public void main(String[] args)
    {
        mainAll("resultsALL007.ivy");
        mainSCOP("resultsSCOP07.ivy");
        mainEC("resultsEC0007.ivy");
        mainAA("resultsAA0007.ivy");
        mainPDBID("resultsPDBID7.ivy");
        //mainPBDAcc();
    }

    static public void mainPBDAcc()
    {
        HistogramSelect select = new HistogramSelect();
        ArrayList<String> acc = select.getPDBAcc();
        for (int i = 0; i < acc.size(); i++)
        {
            System.out.println(acc.get(i));
        }
    }

    static public void mainPDBID(String filename)
    {
        try
        {
            File fileOut = null;
            PrintWriter outFile = null;
            fileOut = new File(filename);
            outFile = new PrintWriter(new BufferedWriter(new FileWriter(fileOut)));

            ResultsSelectPDBID select = new ResultsSelectPDBID();
            ArrayList<String> result = select.getTotalN();

            ArrayList<String> pdbIds = new ArrayList<String>();
            for (int i = 0; i < result.size(); i++)
            {
                pdbIds.add(result.get(i).split("\t")[2]);
            }

            select.setStmt(null);
            select.getTotalX1(pdbIds, result);
            select.setStmt(null);
            select.getTotalX2(pdbIds, result);
            select.setStmt(null);
            select.getTotalX3(pdbIds, result);
            select.setStmt(null);
            select.getTotalX4(pdbIds, result);
            for (int i = 0; i < result.size(); i++)
            {
                outFile.println(result.get(i));
                //System.out.println(result.get(i));
            }


            for (int cutoff = iniCutoff; cutoff >= endCutoff; cutoff -= stepSize)
            {
                select.setStmt(null);
                result = select.getN(cutoff);
                pdbIds = new ArrayList<String>();
                for (int i = 0; i < result.size(); i++)
                {
                    pdbIds.add(result.get(i).split("\t")[2]);
                }
                select.setStmt(null);
                select.getX1(pdbIds, result, cutoff);
                select.setStmt(null);
                select.getX2(pdbIds, result, cutoff);
                select.setStmt(null);
                select.getX3(pdbIds, result, cutoff);
                select.setStmt(null);
                select.getX4(pdbIds, result, cutoff);
                for (int i = 0; i < result.size(); i++)
                {
                    outFile.println(result.get(i));
                    //System.out.println(result.get(i));
                }
            }

            outFile.close();
        } 
        catch (IOException ex)
        {
            Logger.getLogger(ResultsUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static public void mainAA(String filename)
    {
        try
        {
            File fileOut = null;
            PrintWriter outFile = null;
            fileOut = new File(filename);
            outFile = new PrintWriter(new BufferedWriter(new FileWriter(fileOut)));
            ResultsSelectAA select = new ResultsSelectAA();
            ArrayList<ResultsEntity> results = new ArrayList<ResultsEntity>();

            int ini = 1;
            int end = 20;

            for (int method = iniMethod; method <= endMethod; method++)
            {
                for (int aa = ini; aa <= end; aa++)
                {
                    ResultsEntity global = new ResultsEntity();
                    global.setMethod(method);
                    global.setAminoAcidId(aa);
                    global.setTitle(global.getMethodName() + "[" + aa + "]");
                    global.setRmsd(select.getTotalN(method, global.getAminoAcidId()));
                    results.add(global);
                }
            }

            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                ResultsEntity global = results.get(i);
                global.setX1(select.getTotalX1(global.getMethod(), global.getAminoAcidId()));
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                ResultsEntity global = results.get(i);
                global.setX2(select.getTotalX2(global.getMethod(), global.getAminoAcidId()));
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                ResultsEntity global = results.get(i);
                global.setX3(select.getTotalX3(global.getMethod(), global.getAminoAcidId()));
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                ResultsEntity global = results.get(i);
                global.setX4(select.getTotalX4(global.getMethod(), global.getAminoAcidId()));
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                outFile.println(results.get(i).print());
            }

            results = new ArrayList<ResultsEntity>();
            select.setStmt(null);
            for (int i = iniCutoff; i >= endCutoff; i -= stepSize)
            {
                for (int method = iniMethod; method <= endMethod; method++)
                {
                    for (int aa = ini; aa <= end; aa++)
                    {
                        ResultsEntity re = new ResultsEntity();
                        re.setCutoff(i);
                        re.setMethod(method);
                        re.setAminoAcidId(aa);
                        re.setTitle(re.getMethodName() + "[" + re.getAminoAcidId() + "](" + re.getCutoff() + ")");
                        re.setRmsd(select.getN(method, i, re.getAminoAcidId()));
                        results.add(re);
                    }
                }
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                ResultsEntity re = results.get(i);
                re.setX1(select.getX1(re.getMethod(), re.getCutoff(), re.getAminoAcidId()));
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                ResultsEntity re = results.get(i);
                re.setX2(select.getX2(re.getMethod(), re.getCutoff(), re.getAminoAcidId()));
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                ResultsEntity re = results.get(i);
                re.setX3(select.getX3(re.getMethod(), re.getCutoff(), re.getAminoAcidId()));
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                ResultsEntity re = results.get(i);
                re.setX4(select.getX4(re.getMethod(), re.getCutoff(), re.getAminoAcidId()));
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                outFile.println(results.get(i).print());
            }

            outFile.close();
        } 
        catch (IOException ex)
        {
            Logger.getLogger(ResultsUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static public void mainEC(String filename)
    {
        try
        {
            File fileOut = null;
            PrintWriter outFile = null;
            fileOut = new File(filename);
            outFile = new PrintWriter(new BufferedWriter(new FileWriter(fileOut)));
            ResultsSelectEC select = new ResultsSelectEC();
            ArrayList<ResultsEntity> results = new ArrayList<ResultsEntity>();

            char ini = '1';
            char end = '6';

            for (int method = iniMethod; method <= endMethod; method++)
            {
                for (char ec = ini; ec <= end; ec++)
                {
                    ResultsEntity global = new ResultsEntity();
                    global.setMethod(method);
                    global.setEcNumber(ec);
                    global.setTitle(global.getMethodName() + "[" + ec + "]");
                    global.setRmsd(select.getTotalN(method, ec));
                    results.add(global);
                }
            }

            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                ResultsEntity global = results.get(i);
                global.setX1(select.getTotalX1(global.getMethod(), global.getEcNumber()));
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                ResultsEntity global = results.get(i);
                global.setX2(select.getTotalX2(global.getMethod(), global.getEcNumber()));
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                ResultsEntity global = results.get(i);
                global.setX3(select.getTotalX3(global.getMethod(), global.getEcNumber()));
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                ResultsEntity global = results.get(i);
                global.setX4(select.getTotalX4(global.getMethod(), global.getEcNumber()));
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                outFile.println(results.get(i).print());
            }

            results = new ArrayList<ResultsEntity>();
            select.setStmt(null);
            for (int i = iniCutoff; i >= endCutoff; i -= stepSize)
            {
                for (int method = iniMethod; method <= endMethod; method++)
                {
                    for (char ec = ini; ec <= end; ec++)
                    {
                        ResultsEntity re = new ResultsEntity();
                        re.setCutoff(i);
                        re.setMethod(method);
                        re.setEcNumber(ec);
                        re.setTitle(re.getMethodName() + "[" + re.getEcNumber() + "](" + re.getCutoff() + ")");
                        re.setRmsd(select.getN(method, i, ec));
                        results.add(re);
                    }
                }
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                ResultsEntity re = results.get(i);
                re.setX1(select.getX1(re.getMethod(), re.getCutoff(), re.getEcNumber()));
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                ResultsEntity re = results.get(i);
                re.setX2(select.getX2(re.getMethod(), re.getCutoff(), re.getEcNumber()));
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                ResultsEntity re = results.get(i);
                re.setX3(select.getX3(re.getMethod(), re.getCutoff(), re.getEcNumber()));
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                ResultsEntity re = results.get(i);
                re.setX4(select.getX4(re.getMethod(), re.getCutoff(), re.getEcNumber()));
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                outFile.println(results.get(i).print());
            }

            outFile.close();
        } 
        catch (IOException ex)
        {
            Logger.getLogger(ResultsUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static public void mainSCOP(String filename)
    {
        try
        {
            File fileOut = null;
            PrintWriter outFile = null;
            fileOut = new File(filename);
            outFile = new PrintWriter(new BufferedWriter(new FileWriter(fileOut)));
            ResultsSelectSCOP select = new ResultsSelectSCOP();
            ArrayList<ResultsEntity> results = new ArrayList<ResultsEntity>();

            char ini = 'a';
            char end = 'd';

            for (int method = iniMethod; method <= endMethod; method++)
            {
                for (char scopClass = ini; scopClass <= end; scopClass++)
                {
                    ResultsEntity global = new ResultsEntity();
                    global.setMethod(method);
                    global.setScopClass(scopClass);
                    global.setTitle(global.getMethodName() + "[" + scopClass + "]");
                    global.setRmsd(select.getTotalN(method, scopClass));
                    results.add(global);
                }
            }

            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                ResultsEntity global = results.get(i);
                global.setX1(select.getTotalX1(global.getMethod(), global.getScopClass()));
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                ResultsEntity global = results.get(i);
                global.setX2(select.getTotalX2(global.getMethod(), global.getScopClass()));
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                ResultsEntity global = results.get(i);
                global.setX3(select.getTotalX3(global.getMethod(), global.getScopClass()));
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                ResultsEntity global = results.get(i);
                global.setX4(select.getTotalX4(global.getMethod(), global.getScopClass()));
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                outFile.println(results.get(i).print());
            }

            results = new ArrayList<ResultsEntity>();
            select.setStmt(null);
            for (int i = iniCutoff; i >= endCutoff; i -= stepSize)
            {
                for (int method = iniMethod; method <= endMethod; method++)
                {
                    for (char scopClass = ini; scopClass <= end; scopClass++)
                    {
                        ResultsEntity re = new ResultsEntity();
                        re.setCutoff(i);
                        re.setMethod(method);
                        re.setScopClass(scopClass);
                        re.setTitle(re.getMethodName() + "[" + re.getScopClass() + "](" + re.getCutoff() + ")");
                        re.setRmsd(select.getN(method, i, scopClass));
                        results.add(re);
                    }
                }
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                ResultsEntity re = results.get(i);
                re.setX1(select.getX1(re.getMethod(), re.getCutoff(), re.getScopClass()));
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                ResultsEntity re = results.get(i);
                re.setX2(select.getX2(re.getMethod(), re.getCutoff(), re.getScopClass()));
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                ResultsEntity re = results.get(i);
                re.setX3(select.getX3(re.getMethod(), re.getCutoff(), re.getScopClass()));
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                ResultsEntity re = results.get(i);
                re.setX4(select.getX4(re.getMethod(), re.getCutoff(), re.getScopClass()));
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                outFile.println(results.get(i).print());
            }

            outFile.close();
        } 
        catch (IOException ex)
        {
            Logger.getLogger(ResultsUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static public void mainAll(String filename)
    {
        try
        {
            File fileOut = null;
            PrintWriter outFile = null;
            fileOut = new File(filename);
            outFile = new PrintWriter(new BufferedWriter(new FileWriter(fileOut)));
            ResultsSelect select = new ResultsSelect();
            ArrayList<ResultsEntity> results = new ArrayList<ResultsEntity>();

            for (int method = iniMethod; method <= endMethod; method++)
            {
                ResultsEntity global = new ResultsEntity();
                global.setMethod(method);
                global.setTitle(global.getMethodName());
                global.setRmsd(select.getTotalN(method));
                results.add(global);
            }

            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                ResultsEntity global = results.get(i);
                global.setX1(select.getTotalX1(global.getMethod()));
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                ResultsEntity global = results.get(i);
                global.setX2(select.getTotalX2(global.getMethod()));
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                ResultsEntity global = results.get(i);
                global.setX3(select.getTotalX3(global.getMethod()));
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                ResultsEntity global = results.get(i);
                global.setX4(select.getTotalX4(global.getMethod()));
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                outFile.println(results.get(i).print());
            }

            results = new ArrayList<ResultsEntity>();
            select.setStmt(null);
            for (int i = iniCutoff; i >= endCutoff; i -= stepSize)
            {
                for (int method = iniMethod; method <= endMethod; method++)
                {
                    ResultsEntity re = new ResultsEntity();
                    re.setCutoff(i);
                    re.setMethod(method);
                    re.setTitle(re.getMethodName() + "(" + re.getCutoff() + ")");
                    re.setRmsd(select.getN(method, i));
                    results.add(re);
                }
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                ResultsEntity re = results.get(i);
                re.setX1(select.getX1(re.getMethod(), re.getCutoff()));
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                ResultsEntity re = results.get(i);
                re.setX2(select.getX2(re.getMethod(), re.getCutoff()));
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                ResultsEntity re = results.get(i);
                re.setX3(select.getX3(re.getMethod(), re.getCutoff()));
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                ResultsEntity re = results.get(i);
                re.setX4(select.getX4(re.getMethod(), re.getCutoff()));
            }
            select.setStmt(null);
            for (int i = 0; i < results.size(); i++)
            {
                outFile.println(results.get(i).print());
            }

            outFile.close();
        } 
        catch (IOException ex)
        {
            Logger.getLogger(ResultsUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
