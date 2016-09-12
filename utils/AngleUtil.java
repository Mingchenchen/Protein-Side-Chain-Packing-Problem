package utils;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.biojava.bio.structure.Atom;
import org.biojava.bio.structure.AtomImpl;
import org.biojava.bio.structure.Calc;
import org.biojava.bio.structure.StructureException;

public class AngleUtil
{

    public static final double EPSILON = 0.00001;

    public AngleUtil()
    {}

    public static double getDifference(double angle1, double angle2)
    {
        double dif = Math.abs(angle1 - angle2);
        return Math.min(dif, 360 - dif);
    }

    static public Atom getAtom(Atom A, Atom B, Atom C, double torsion_BC, double angle_BCD, double bond_CD)
    {
        Atom D = new AtomImpl();
        D.setX(0);
        D.setY(0);
        D.setZ(0);
        try
        {
            //angle_BCD = angle_BCD * Math.PI / 180;
            angle_BCD = (180 - angle_BCD) * Math.PI / 180;
            torsion_BC = torsion_BC * Math.PI / 180;

            Atom AB = Calc.substract(B, A);
            Atom BC = Calc.substract(C, B);
            Atom bc = Calc.unitVector(BC);
            Atom n = Calc.unitVector(Calc.vectorProduct(AB, bc));

            Atom Mx = bc;
            Atom My = Calc.unitVector(Calc.vectorProduct(n, bc));
            Atom Mz = n;

            Atom D2 = new AtomImpl();

            D2.setX(bond_CD * Math.cos(angle_BCD));
            D2.setY(bond_CD * Math.cos(torsion_BC) * Math.sin(angle_BCD));
            D2.setZ(bond_CD * Math.sin(torsion_BC) * Math.sin(angle_BCD));

            D.setX(Mx.getX() * D2.getX() + My.getX() * D2.getY() + Mz.getX() * D2.getZ() + C.getX());
            D.setY(Mx.getY() * D2.getX() + My.getY() * D2.getY() + Mz.getY() * D2.getZ() + C.getY());
            D.setZ(Mx.getZ() * D2.getX() + My.getZ() * D2.getY() + Mz.getZ() * D2.getZ() + C.getZ());
        }
        catch (StructureException ex)
        {
            Logger.getLogger(StructureUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return D;
    }

    public static double getAngle(Atom a1, Atom a2, Atom a3)
    {
        try
        {
            Atom a = Calc.substract(a1, a2);
            Atom c = Calc.substract(a3, a2);
            return Calc.angle(a, c);
        }
        catch (StructureException ex)
        {
            Logger.getLogger(StructureUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0.0;
    }
}
