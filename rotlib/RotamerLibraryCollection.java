package rotlib;

import java.io.File;
import java.util.ArrayList;

public class RotamerLibraryCollection
{

    public static final String DUN_DEP_ROT_LIB_TYPE = "Dunbrack's Backbone-Dependent Rotamer Library";
    //public static final String DUN_DEP_ROT_LIB_FILENAME = "F:" + File.separator + "Ivetth" + File.separator + "Documents" + File.separator + "CICESE" + File.separator + "applications" + File.separator + "rot_lib_dunbrack" + File.separator + "bbdep02.May.lib";
    public static final String DUN_DEP_ROT_LIB_FILENAME = "rot_lib_dunbrack" + File.separator + "bbdep02.May.lib";
    public static final String DUN_IND_ROT_LIB_TYPE = "Dunbrack's Independent Rotamer Library";
    //public static final String DUN_IND_ROT_LIB_FILENAME = "F:" + File.separator + "Ivetth" + File.separator + "Documents" + File.separator + "CICESE" + File.separator + "applications" + File.separator + "rot_lib_dunbrack" + File.separator + "bbind02.May.lib";
    //public static final String DUN_IND_ROT_LIB_FILENAME = "rot_lib_dunbrack" + File.separator + "bbind02.May.lib";
    public static final String DUN_IND_ROT_LIB_FILENAME = "bbind02.May.lib";
    private String rotamerLibraryType;
    private ArrayList<RotamerLibrary> mRotamerLibrary;

    public RotamerLibraryCollection()
    {
    }

    public String getRotamerLibraryType()
    {
        return rotamerLibraryType;
    }

    public void setRotamerLibraryType(String val)
    {
        this.rotamerLibraryType = val;
    }

    public ArrayList<RotamerLibrary> getRotamerLibrary()
    {
        return mRotamerLibrary;
    }

    public void setRotamerLibrary(ArrayList<RotamerLibrary> val)
    {
        this.mRotamerLibrary = val;
    }

    public void addRotamerLibrary(RotamerLibrary rl)
    {
        if (mRotamerLibrary == null)
        {
            mRotamerLibrary = new ArrayList<RotamerLibrary>();
        }
        mRotamerLibrary.add(rl);
    }

    public void print()
    {
        System.out.println();
        System.out.print(rotamerLibraryType);
        for (int i = 0; i < mRotamerLibrary.size(); i++)
        {
            mRotamerLibrary.get(i).print();
        }
    }

    public RotamerLibrary getRotamerLibrary(String aminoAcidType)
    {
        if (mRotamerLibrary != null)
        {
            for (int i = 0; i < mRotamerLibrary.size(); i++)
            {
                RotamerLibrary rl = mRotamerLibrary.get(i);
                if (rl.getAminoAcidType().equalsIgnoreCase(aminoAcidType))
                {
                    return rl;
                }
            }
        }
        return null;
    }

    public RotamerLibrary getRotamerLibrary(String aminoAcidType, int phi, int psi)
    {
        if (mRotamerLibrary != null)
        {
            for (int i = 0; i < mRotamerLibrary.size(); i++)
            {
                DepRotamerLibrary drl = (DepRotamerLibrary) mRotamerLibrary.get(i);
                if (drl.getAminoAcidType().equalsIgnoreCase(aminoAcidType) && drl.getPhi() == phi && drl.getPsi() == psi)
                {
                    return drl;
                }
            }
        }
        return null;
    }
}