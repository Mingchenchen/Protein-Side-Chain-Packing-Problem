package rotlib.io;

import java.io.DataInputStream;
import java.io.IOException;
import rotlib.IndRotamerLibrary;
import rotlib.Rotamer;
import rotlib.RotamerData;
import rotlib.RotamerLibrary;
import rotlib.RotamerLibraryCollection;

public class DunIndRotLibParser
{

    public DunIndRotLibParser()
    {}

    public static RotamerLibraryCollection parseRotamerLibrary(DataInputStream dis)
    {
        RotamerLibraryCollection rlc = new RotamerLibraryCollection();
        rlc.setRotamerLibraryType(RotamerLibraryCollection.DUN_IND_ROT_LIB_TYPE);
        int lineNumber = 0;
        try
        {
            while (dis.available() != 0)
            {
                String line = dis.readLine();
                if (lineNumber > 80 && RotamerLibraryReader.isRotamer(line))
                {
                    String delims = "[ ]+";
                    String[] fields = line.split(delims);
                    RotamerLibrary rl = rlc.getRotamerLibrary(fields[0]);
                    if (rl == null)
                    {
                        rl = new IndRotamerLibrary();
                        rl.setAminoAcidType(fields[0]);
                        rlc.addRotamerLibrary(rl);
                    }
                    rl.addRotamer(parseLine(line));
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

        for (int i = 0; i < fields.length;)
        {
            RotamerData rd = new RotamerData();
            rd.setValue(fields[i++]);
            switch (i - 1)
            {
                case 0:
                    rd.setFieldName("Amino Acid");
                    break;
                case 1:
                    rd.setFieldName("r1");
                    break;
                case 2:
                    rd.setFieldName("r2");
                    break;
                case 3:
                    rd.setFieldName("r3");
                    break;
                case 4:
                    rd.setFieldName("r4");
                    break;
                case 5:
                    rd.setFieldName("n(r1)");
                    break;
                case 6:
                    rd.setFieldName("n(r1234)");
                    break;
                case 7:
                    rd.setFieldName(RotamerData.PROBABILITY_FIELD_NAME);
                    rd.setSigma(Double.parseDouble(fields[i++]));
                    break;
                case 9:
                    rd.setFieldName("p(r234|r1)");
                    rd.setSigma(Double.parseDouble(fields[i++]));
                    break;
                default:
                    rd.setFieldName(RotamerData.CHI_FIELD_NAME[(i - 11) / 2]);
                    rd.setSigma(Double.parseDouble(fields[i++]));
            }
            r.addRotamerData(rd);
        }
        return r;
    }
}

