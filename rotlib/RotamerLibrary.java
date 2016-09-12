package rotlib;

import java.util.ArrayList;

public abstract class RotamerLibrary
{

    private String aminoAcidType;
    private ArrayList<Rotamer> mRotamer;

    public String getAminoAcidType ()
    {
        return aminoAcidType;
    }

    public void setAminoAcidType (String val)
    {
        this.aminoAcidType = val;
    }

    public ArrayList<Rotamer> getRotamer ()
    {
        return mRotamer;
    }

    public void setRotamer (ArrayList<Rotamer> val)
    {
        this.mRotamer = val;
    }

    public void addRotamer (Rotamer r)
    {
        if (mRotamer == null)
        {
            mRotamer = new ArrayList<Rotamer>();
        }
        mRotamer.add(r);
    }

    public abstract void print ();
}