import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeriMethod {
    public DeriMethod() {}
    
    public String plusRules(ArrayList<String> term) {
        String str = "";
        Derivation de = new Derivation();
        for (int i = 0; i < term.size(); i++) {
            String temp = de.derivation(term.get(i));
            if (!temp.equals("0")) {
                str = str + "+" + temp;
            }
        }
        if (str.length() == 0) {
            return "";
        }
        return str;
    }
    
    public String multRules(ArrayList<String> term, int i,String strIn) {
        String str = strIn;
        for (int j = 0; j < term.size(); j++) {
            if (term.get(j).equals("0")) {
                return  "";
            }
            if (j != i && !term.get(j).equals("1")) {
                str = str + '*' + term.get(j);
            }
        }
        if (str.length() == 0) {
            return "";
        }
        return str;
    }
    
    public String multForPowerRules(ArrayList<String> term, int i) {
        String str = "";
        Derivation de = new Derivation();
        String deg = de.matchPower(term.get(i));
        String temp = de.derivation(term.get(i).
                substring(0, term.get(i).length() - deg.length() - 1));
        deg = String.valueOf(new BigInteger(deg));
        if (temp.equals("") || temp.equals("0")) {
            return "";
        } else if (deg.equals("1") && de.split(temp,1).size() != 1) {
            str = str + '+' + "(" + temp + ")";
        } else if (deg.equals("1") && de.split(temp,1).size() == 1) {
            str = str + "+" + temp;
        } else if (deg.equals("2") && de.split(temp,1).size() != 1) {
            str = str + '+' + deg + '*' + term.get(i)
                    .substring(0, term.get(i).length() - deg.length() - 1)
                    + "*(" + temp + ")";
        } else if (deg.equals("2")  && de.split(temp,1).size() == 1) {
            str = str + '+' + deg + '*' + term.get(i)
                    .substring(0, term.get(i).length() - deg.length() - 1)
                    + "*" + temp;
        } else if (de.split(temp,1).size() != 1) {
            str = str + '+' + deg + '*' + term.get(i)
                    .substring(0, term.get(i).length() - deg.length())
                    + (Integer.valueOf(deg) - 1) + "*(" + temp + ")";
        } else if (de.split(temp,1).size() == 1) {
            str = str + '+' + deg + '*' + term.get(i)
                    .substring(0, term.get(i).length() - deg.length())
                    + (Integer.valueOf(deg) - 1) + "*" + temp;
        }
        return multRules(term,i,str);
    }
    
    public String multForSin(ArrayList<String> term, int i) {
        Pattern p = Pattern.compile("^[+-]{1,2}");
        Matcher m = p.matcher(term.get(i));
        String font = "";
        if (m.find()) {
            font = m.group();
            font = font.replaceAll("(\\-\\-)|(\\+\\+)","+");
            font = font.replaceAll("(\\+-)|(-\\+)","-");
        }
        String str = "";
        Derivation de = new Derivation();
        int startPos = de.matchTrig(term.get(i),1);
        String temp = de.derivation(term.get(i)
                .substring(startPos, term.get(i).length() - 1));
        if (temp.equals("0") || temp.equals("")) {
            return "";
        } else if (!temp.equals("1") && de.split(temp,1).size() > 1) {
            str = str + font + "+cos" + term.get(i).substring(startPos - 1)
                    + "*(" + temp + ")";
        } else if (!temp.equals("1") && de.split(temp,1).size() == 1) {
            str = str + font + "+cos" + term.get(i).substring(startPos - 1)
                    + "*" + temp;
        } else if (temp.equals("1")) {
            str = str + font + "+cos" + term.get(i).substring(startPos - 1);
        }
        return multRules(term,i,str);
    }
    
    public String multForCos(ArrayList<String> term, int i) {
        Pattern p = Pattern.compile("^[+-]{1,2}");
        Matcher m = p.matcher(term.get(i));
        String font = "";
        if (m.find()) {
            font = m.group();
            font = font.replaceAll("(\\-\\-)|(\\+\\+)","+");
            font = font.replaceAll("(\\+-)|(-\\+)","-");
        }
        String str = "";
        Derivation de = new Derivation();
        int startPos = de.matchTrig(term.get(i),2);
        String temp = de.derivation(
                term.get(i).substring(startPos, term.get(i).length() - 1));
        if (temp.equals("0") || temp.equals("")) {
            return "";
        } else if (!temp.equals("1") && de.split(temp,1).size() > 1) {
            str = str + font + "-sin" + term.get(i).substring(startPos - 1)
                    + "*(" + temp + ")";
        } else if (!temp.equals("1") && de.split(temp,1).size() == 1) {
            str = str + font + "-sin" + term.get(i).substring(startPos - 1)
                    + "*" + temp;
        } else if (temp.equals("1")) {
            str = str + font + "-sin" + term.get(i).substring(startPos - 1);
        }
        return multRules(term,i,str);
    }
    
    public String multForBasic(ArrayList<String> term, int i) {
        String str = "";
        Derivation de = new Derivation();
        String temp = de.derivation(term.get(i));
        if (temp.equals("0") || temp.equals("")) {
            return "";
        } if (de.split(temp,1).size() > 1) {
            str = str + "+(" + temp + ')';
        } else if (temp.equals("1")) {
            if (multRules(term,i,"1").length() == 0) {
                return "";
            }
            if (multRules(term,i,"1").length() > 2) {
                return '+' + multRules(term,i,"1").substring(2);
            } else {
                return "1";
            }
        } else if (de.split(temp,1).size() == 1) {
            str = str + "+" + temp;
        }
        return multRules(term,i,str);
    }
}
