package rotlib.io;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
//import java.util.Arrays;
import rotlib.RotamerLibraryCollection;
import utils.ListUtil;

public class RotamerLibraryReader
{

    public RotamerLibraryReader()
    {}

    public static RotamerLibraryCollection readRotamerLibrary(String rotamerLibraryType)
    {
        RotamerLibraryCollection rlc = null;
        File f = null;
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        try
        {
            if (rotamerLibraryType.equalsIgnoreCase(RotamerLibraryCollection.DUN_IND_ROT_LIB_TYPE))
            {
                f = new File(RotamerLibraryCollection.DUN_IND_ROT_LIB_FILENAME);
                fis = new FileInputStream(f);
                bis = new BufferedInputStream(fis);
                dis = new DataInputStream(bis);
                rlc = DunIndRotLibParser.parseRotamerLibrary(dis);
            } 
            else if (rotamerLibraryType.equalsIgnoreCase(RotamerLibraryCollection.DUN_DEP_ROT_LIB_TYPE))
            {
                f = new File(RotamerLibraryCollection.DUN_DEP_ROT_LIB_FILENAME);
                fis = new FileInputStream(f);
                bis = new BufferedInputStream(fis);
                dis = new DataInputStream(bis);
                rlc = DunDepRotLibParser.parseRotamerLibrary(dis);
            }
            fis.close();
            bis.close();
            dis.close();
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        } 
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return rlc;
    }

    public static boolean isRotamer(String line)
    {
        if (line.length() < 3)
        {
            return false;
        }

        String[] fields = line.split(" ");

        if (fields.length < 13)
        {
            return false;
        }

        if (fields[0].length() != 3)
        {
            return false;
        }
        return ListUtil.isAminoAcid(fields[0]);
    }
}