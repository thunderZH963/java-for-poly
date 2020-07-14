import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PolyTerm {
    private String term = "";
    private BigInteger sinDeg = new BigInteger("0");
    private BigInteger cosDeg = new BigInteger("0");
    private BigInteger coeff = new BigInteger("1");
    private BigInteger deg = new BigInteger("0");
    
    public PolyTerm(BigInteger in) {
        coeff = in;
    }
    
    public  PolyTerm(BigInteger sinDegIn, BigInteger cosDegIn,
                     BigInteger coeffIn, BigInteger degIn) {
        this.sinDeg = sinDegIn;
        this.cosDeg = cosDegIn;
        this.coeff = coeffIn;
        this.deg = degIn;
    }
    
    public PolyTerm(String termIn) {
        this.term = termIn.replaceAll("[\t ]","");
        matchSinDeg();
        matchCosDeg();
        matchCoeff();
        matchDeg();
    }
    
    public void matchSinDeg() {
        String str = this.term;
        Pattern p = Pattern.compile("(sin\\(x\\))\\^[+-]?[0-9]+|(sin\\(x\\))");
        Matcher m = p.matcher(str);
        while (m.find())  {
            if (m.group().length() == 6) {
                this.sinDeg = this.sinDeg.add(new BigInteger("1"));
            } else {
                BigInteger nowDeg = new BigInteger(m.group().substring(7));
                if (nowDeg.compareTo(new BigInteger("10000")) > 0) {
                    WrongExit bigPower = new WrongExit();
                }
                this.sinDeg =
                        this.sinDeg.add(nowDeg);
            }
            str = str.substring(m.end());
            m = p.matcher(str);
        }
    }
    
    public void matchCosDeg() {
        String str = this.term;
        Pattern p = Pattern.compile("(cos\\(x\\))\\^[+-]?[0-9]+|(cos\\(x\\))");
        Matcher m = p.matcher(str);
        while (m.find()) {
            if (m.group().length() == 6) {
                this.cosDeg = this.cosDeg.add(new BigInteger("1"));
            } else {
                BigInteger nowDeg = new BigInteger(m.group().substring(7));
                if (nowDeg.compareTo(new BigInteger("10000")) > 0) {
                    WrongExit bigPower = new WrongExit();
                }
                this.cosDeg =
                        this.cosDeg.add(nowDeg);
            }
            str = str.substring(m.end());
            m = p.matcher(str);
        }
    }
    
    public void matchDeg() {
        String str = this.term;
        str = str.replaceAll("(sin\\(x\\))\\^[+-]?[0-9]+|(sin\\(x\\))","");
        str = str.replaceAll("(cos\\(x\\))\\^[+-]?[0-9]+|(cos\\(x\\))","");
        Pattern p = Pattern.compile("(x\\^[+-]?[0-9]+)|x");
        Matcher m = p.matcher(str);
        while (m.find()) {
            if (m.group().length() == 1) {
                this.deg = this.deg.add(new BigInteger("1"));
            } else {
                BigInteger nowDeg = new BigInteger(m.group().substring(2));
                if (nowDeg.compareTo(new BigInteger("10000")) > 0) {
                    WrongExit bigPower = new WrongExit();
                }
                this.deg = this.deg.add(nowDeg);
            }
            str = str.substring(m.end());
            m = p.matcher(str);
        }
    }
    
    public void matchCoeff() {
        String str = this.term;
        str = str.replaceAll("(sin\\(x\\))\\^[+-]?[0-9]+|(sin\\(x\\))","");
        str = str.replaceAll("(cos\\(x\\))\\^[+-]?[0-9]+|(cos\\(x\\))","");
        str = str.replaceAll("(x\\^[+-]?[0-9]+)|x","");
        Pattern p = Pattern.compile("[+-]?[0-9]+|[-]");
        Matcher m = p.matcher(str);
        while (m.find()) {
            if (m.group().equals("-")) {
                this.coeff = this.coeff.multiply(new BigInteger("-1"));
            } else {
                this.coeff = this.coeff.multiply(new BigInteger(m.group()));
                str = str.substring(m.end());
                m = p.matcher(str);
            }
        }
    }
    
    public BigInteger getCoeff() {
        return this.coeff;
    }
    
    public BigInteger getDeg() {
        return this.deg;
    }
    
    public  BigInteger getSinDeg() {
        return this.sinDeg;
    }
    
    public BigInteger getCosDeg() {
        return this.cosDeg;
    }
    
    public void changeCoeff(BigInteger coeffIn) {
        this.coeff = coeffIn;
    }
    
    public String toString() {
        String output = "";
        if (this.coeff.equals(new BigInteger("0"))) {
            return "";
        }
        if (this.deg.equals(new BigInteger("1"))) {
            output = output + "x";
        } else if (!this.deg.equals(new BigInteger("0"))) {
            output = output + "x^" + String.valueOf(this.deg);
        }
        if (this.sinDeg.equals(new BigInteger("1"))) {
            if (!output.equals("")) {
                output = output + "*sin(x)";
            } else if (!this.sinDeg.equals(new BigInteger("0"))) {
                output = output + "sin(x)";
            }
        } else if (!this.sinDeg.equals(new BigInteger("0"))) {
            if (!output.equals("")) {
                output = output + "*sin(x)^" + String.valueOf(this.sinDeg);
            } else {
                output = output + "sin(x)^" + String.valueOf(this.sinDeg);
            }
        }
        if (this.cosDeg.equals(new BigInteger("1"))) {
            if (!output.equals("")) {
                output = output + "*cos(x)";
            } else {
                output = output + "cos(x)";
            }
        } else if (!this.cosDeg.equals(new BigInteger("0"))) {
            if (!output.equals("")) {
                output = output + "*cos(x)^" + String.valueOf(this.cosDeg);
            } else {
                output = output + "cos(x)^" + String.valueOf(this.cosDeg);
            }
        }
        if (output.equals("")) {
            if (this.coeff.compareTo(new BigInteger("0")) > 0) {
                output = output + "+" + String.valueOf(this.coeff);
            } else {
                output = output + String.valueOf(this.coeff);
            }
        } else if (this.coeff.equals(new BigInteger("1"))) {
            output = '+' + output;
        } else if (this.coeff.equals(new BigInteger("-1"))) {
            output = '-' + output;
        } else if (this.coeff.compareTo(new BigInteger("0")) > 0) {
            output = '+' + String.valueOf(this.coeff) + '*' + output;
        } else {
            output = String.valueOf(this.coeff) + '*' + output;
        }
        return output;
    }
    
}
