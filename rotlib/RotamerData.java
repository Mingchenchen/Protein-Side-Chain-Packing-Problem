package rotlib;

public class RotamerData
{

    public static final String PROBABILITY_FIELD_NAME = "Probability";
    public static final String[] CHI_FIELD_NAME = {"X1","X2","X3","X4"};

    private String fieldName;
    private String value;
    private double sigma;

    public RotamerData ()
    {
    }

    public String getFieldName ()
    {
        return fieldName;
    }

    public void setFieldName (String val)
    {
        this.fieldName = val;
    }

    public String getValue ()
    {
        return value;
    }

    public void setValue (String val)
    {
        this.value = val;
    }

    public double getSigma ()
    {
        return sigma;
    }

    public void setSigma (double val)
    {
        this.sigma = val;
    }

}