package rotlib;

import java.util.ArrayList;

public class Rotamer
{

    private ArrayList<RotamerData> mRotamerData;

    public Rotamer ()
    {}

    public ArrayList<RotamerData> getRotamerData ()
    {
        return mRotamerData;
    }

    public void setRotamerData (ArrayList<RotamerData> val)
    {
        this.mRotamerData = val;
    }

    public void addRotamerData (RotamerData rd)
    {
        if (mRotamerData == null)
        {
            mRotamerData = new ArrayList<RotamerData>();
        }
        mRotamerData.add(rd);
    }

    public void print ()
    {
        System.out.println();
        System.out.print(getProbabilityValue());
        for (int i = 0; i < 4; i++)
        {
            System.out.print("\t" + getChiValue(i + 1));
        }
    }

    public double getChiValue (int chiIndex)
    {
        for (int i = 0; mRotamerData != null && i < mRotamerData.size(); i++)
        {
            if (mRotamerData.get(i).getFieldName().equalsIgnoreCase(RotamerData.CHI_FIELD_NAME[chiIndex - 1]))
            {
                return Double.parseDouble(mRotamerData.get(i).getValue());
            }
        }
        return 0.0;
    }

    public double getProbabilityValue ()
    {
        for (int i = 0; mRotamerData != null && i < mRotamerData.size(); i++)
        {
            if (mRotamerData.get(i).getFieldName().equalsIgnoreCase(RotamerData.PROBABILITY_FIELD_NAME))
            {
                return Double.parseDouble(mRotamerData.get(i).getValue());
            }
        }
        return 0.0;
    }

}