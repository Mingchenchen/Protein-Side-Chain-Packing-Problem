package rotlib;

public class DepRotamerLibrary extends RotamerLibrary
{

    private int phi;
    private int psi;

    public DepRotamerLibrary()
    {
    }

    public int getPhi()
    {
        return phi;
    }

    public void setPhi(int val)
    {
        this.phi = val;
    }

    public int getPsi()
    {
        return psi;
    }

    public void setPsi(int val)
    {
        this.psi = val;
    }

    public void print()
    {
        System.out.println();
        System.out.print("Rotamer Library: " + getAminoAcidType());
        System.out.print("\t"+getPhi());
        System.out.print("\t"+getPsi());
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