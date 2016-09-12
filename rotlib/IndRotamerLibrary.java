package rotlib;

public class IndRotamerLibrary extends RotamerLibrary
{

    public IndRotamerLibrary()
    {
    }

    public void print()
    {
        System.out.println();
        System.out.print("Rotamer Library for " + getAminoAcidType());
        if (getRotamer() != null)
        {
            System.out.print("\tNo. Rotamers: " + getRotamer().size());
        }
        
        for (int i = 0; i < getRotamer().size(); i++)
        {
            getRotamer().get(i).print();
        }
    }
}

