package rotlib.io;

import java.io.DataInputStream;
import java.io.IOException;
import rotlib.DepRotamerLibrary;
import rotlib.Rotamer;
import rotlib.RotamerData;
import rotlib.RotamerLibrary;
import rotlib.RotamerLibraryCollection;

public class DunDepRotLibParser
{

    public DunDepRotLibParser()
    {
    }

    public static RotamerLibraryCollection parseRotamerLibrary(DataInputStream dis)
    {
        RotamerLibraryCollection rlc = new RotamerLibraryCollection();
        rlc.setRotamerLibraryType(RotamerLibraryCollection.DUN_DEP_ROT_LIB_TYPE);
        int lineNumber = 0;
        try
        {
            while (dis.available() != 0)
            {
                String line = dis.readLine();
                if (lineNumber < 100 && RotamerLibraryReader.isRotamer(line))
                {
                    String delims = "[ ]+";
                    String[] fields = line.split(delims);
                    DepRotamerLibrary drl = (DepRotamerLibrary) rlc.getRotamerLibrary(fields[0], Integer.parseInt(fields[1]), Integer.parseInt(fields[2]));
                    if (drl == null)
                    {
                        drl = new DepRotamerLibrary();
                        drl.setAminoAcidType(fields[0]);
                        drl.setPhi(Integer.parseInt(fields[1]));
                        drl.setPsi(Integer.parseInt(fields[2]));
                        rlc.addRotamerLibrary(drl);
                    }
                    drl.addRotamer(parseLine(line));
                } 
                else
                {
                    return rlc;
                }
                lineNumber++;
            }

        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return rlc;
    }

    public static Rotamer parseLine(String line)
    {
        Rotamer r = new Rotamer();
        String delims = "[ ]+";
        String[] fields = line.split(delims);
        
        for (int i = 0; i < fields.length - 4;)
        {
            RotamerData rd = new RotamerData();
            rd.setValue(fields[i++]);
            switch (i - 1)
            {
                case 0:
                    rd.setFieldName("Res");
                    break;
                case 1:
                    rd.setFieldName("phi");
                    break;
                case 2:
                    rd.setFieldName("psi");
                    break;
                case 3:
                    rd.setFieldName("N");
                    break;
                case 4:
                    rd.setFieldName("r1");
                    break;
                case 5:
                    rd.setFieldName("r2");
                    break;
                case 6:
                    rd.setFieldName("r3");
                    break;
                case 7:
                    rd.setFieldName("r4");
                    break;
                case 8:
                    rd.setFieldName(RotamerData.PROBABILITY_FIELD_NAME);
                    break;
                default:
                    rd.setFieldName(RotamerData.CHI_FIELD_NAME[i - 10]);
                    rd.setSigma(Double.parseDouble(fields[i + 3]));
            }
            r.addRotamerData(rd);
        }
        return r;
    }
}