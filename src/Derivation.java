import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Derivation {
    public Derivation() {}
    
    public boolean illegalPower(String term) {
        Stack<Character> stack = new Stack<>();
        Pattern p = Pattern.compile("^\\(.*\\)\\^[+-]?\\d+$");
        Pattern p1 = Pattern.compile("^\\(.*\\)");
        Matcher m1 = p1.matcher(term);
        int end = -1;
        if (m1.find()) {
            end = m1.end();
        }
        if (p.matcher(term).find()) {
            for (int i = 0;i < term.length(); i++) {
                if (term.charAt(i) == '(') {
                    stack.push(term.charAt(i));
                } else if (term.charAt(i) == ')') {
                    stack.pop();
                    if (stack.empty()) {
                        if (i == end - 1) {
                            return true;
                        }
                        break;
                    }
                }
            }
        } else {
            return false;
        }
        return false;
    }
    
    public ArrayList<String> split(String str, int mode) {
        ArrayList<String> terms = new ArrayList<String>();
        Stack<Character> stack = new Stack<>();
        int start = 0;
        int i = 0;
        for (;i < str.length() && (str.charAt(i) == '+' ||
                str.charAt(i) == '-'); i++) { }
        for (; i < str.length(); i++) {
            if (stack.empty() && str.charAt(i) != '(' && str.charAt(i) != ')') {
                if (mode == 1) {
                    if ((str.charAt(i) == '+' || str.charAt(i) == '-')
                            && str.charAt(i - 1) != '*'
                            && str.charAt(i - 1) != '^') {
                        if (illegalPower(str.substring(start, i))) {
                            WrongExit exprHasPower = new WrongExit();
                        }
                        terms.add(str.substring(start, i));
                        start = i;
                        while (i < str.length() && (str.charAt(i) == '+' ||
                                str.charAt(i) == '-')) {
                            i++;
                        }
                        i--;
                    }
                } else if (mode == 2) {
                    if (str.charAt(i) == '*') {
                        Pattern p = Pattern.compile("^[+-]\\d+");
                        if (str.substring(i + 1).length() == 0
                                || ((str.substring(i + 1).charAt(0) == '+'
                                || str.substring(i + 1).charAt(0) == '-')
                                && !p.matcher(str.substring(i + 1)).find())) {
                            WrongExit symbolInEnd = new WrongExit();
                        } else {
                            if (illegalPower(str.substring(start, i))) {
                                WrongExit exprHasPower = new WrongExit();
                            } else {
                                terms.add(str.substring(start, i));
                                start = i + 1;
                            }
                        }
                    }
                }
            } else if (str.charAt(i) == '(') {
                stack.push(str.charAt(i));
            } else if (str.charAt(i) == ')') {
                stack.pop();
            }
        }
        if (stack.empty()) {
            if (illegalPower(str.substring(start))) {
                WrongExit illegalPower = new WrongExit();
            } else {
                terms.add(str.substring(start));
            }
        } else {
            WrongExit stackNotEmpty = new WrongExit();
        }
        return terms;
    }
    
    public String derivation(String strIn) {
        DeriMethod method = new DeriMethod();
        String str = strIn;
        //System.out.println(str);
        if (illegalPower(str)) {
            WrongExit illegalPow = new WrongExit();
        }
        while (!str.equals(strip(str))) {
            str = strip(str);
            //System.out.println(str);
        }
        if (!matchBasic(str).equals("")) {
            PolyCompute polyCompute = new PolyCompute();
            return polyCompute.polyResult(str);
        }
        ArrayList<String> term = new ArrayList<String>();
        term = split(str,1);
        if (term.size() > 1) {
            return method.plusRules(term);
        }
        String returnString = "";
        term = split(str,2);
        int i = 0;
        for (i = 0; i < term.size(); i++) {
            if (!matchPower(term.get(i)).equals("")) {
                returnString = returnString + method.multForPowerRules(term,i);
            } else if (matchTrig(term.get(i),1) != -1) {
                returnString = returnString + method.multForSin(term,i);
                //System.out.println(returnString+"*1");
            } else if (matchTrig(term.get(i),2) != -1) {
                returnString = returnString + method.multForCos(term,i);
                //System.out.println(returnString+"*2");
            } else if (!matchBasic(term.get(i)).equals("") || term.size() > 1) {
                returnString = returnString + method.multForBasic(term,i);
                //System.out.println(returnString+"*4");
            } else {
                WrongExit illegalExpr = new WrongExit();
            }
        }
        return returnString;
    }
    
    public String strip(String strIn) {
        String str = strIn;
        int flag = 0;
        Stack<Character> stack = new Stack<>();
        Pattern p = Pattern.compile("^[+-]{1,2}\\(.*\\)$");
        Matcher  m = p.matcher(str);
        int i;
        for (i = 0;i < str.length(); i++) {
            if (str.charAt(i) == '(') {
                stack.push(str.charAt(i));
            } else if (str.charAt(i) == ')') {
                stack.pop();
                if (stack.empty()) {
                    if (i == str.length() - 1) {
                        flag = 1;
                    }
                    break;
                }
            }
        }
        if (str.charAt(0) == '(' &&
                str.charAt(str.length() - 1) == ')' && flag == 1) {
            str = str.substring(1,str.length() - 1);
        } else if (m.find() && flag == 1) {
            if ((str.charAt(0) == '+' && str.charAt(1) == '-')
                    || (str.charAt(0) == '-' && str.charAt(1) == '+')) {
                str = "-1*" + str.substring(2);
            } else if (str.charAt(0) == '+' && str.charAt(1) == '(') {
                str = str.substring(2,str.length() - 1);
            } else if (str.charAt(0) == '-' && str.charAt(1) == '(') {
                str = "-1*" + str.substring(1);
            } else {
                str = str.substring(3,str.length() - 1);
            }
        }
        return str;
    }
    
    public  String matchBasic(String str) {
        Pattern p = Pattern.compile("([+-]?[\\t ]*(([+-]?[\\t ]*("
                + "(sin[\\t ]*\\([\\t ]*x[\\t ]*\\))|"
                + "(cos[\\t ]*\\([\\t ]*x[\\t ]*\\)))"
                + "[\\t ]*[\\^][\\t ]*[+-]?[0-9]+[\\t ]*)"
                + "|([+-]?[ \\t]*((sin[\\t ]*\\([\\t ]*x[\\t ]*\\))"
                + "|(cos[\\t ]*\\([\\t ]*x[\\t ]*\\)))[\\t ]*)|"
                + "([+-]?[0-9]+[\\t ]*[*][\\t ]*x[\\t ]*"
                + "[\\^][\\t ]*[+-]?[0-9]+[\\t ]*)|"
                + "([+-]?[\\t ]*x[\\t ]*[\\^][\\t ]*[+-]?[0-9]+[\\t ]*)|"
                + "([+-]?[0-9]+[\\t ]*[*][\\t ]*x[\\t ]*)|"
                + "([+-]?[\\t ]*x[\\t ]*)|"
                + "([+-]?[\\t ]*[+-]?[0-9]+[\\t ]*))"
                + "(([*][\\t ]*((sin[\\t ]*\\([\\t ]*x[\\t ]*\\))"
                + "|(cos[\\t ]*\\([\\t ]*x[\\t ]*\\)))[\\t ]*"
                + "[\\^][\\t ]*[+-]?[0-9]+[\\t ]*)|"
                + "([*][\\t ]*((sin[\\t ]*\\([\\t ]*x[\\t ]*\\))"
                + "|(cos[\\t ]*\\([\\t ]*x[\\t ]*\\)))[\\t ]*)|"
                + "([*][\\t ]*[+-]?[0-9]+[\\t ]*[*][\\t ]*x"
                + "[\\t ]*[\\^][\\t ]*[+-]?[0-9]+[\\t ]*)|"
                + "([*][\\t ]*x"
                + "[\\t ]*[\\^][\\t ]*[+-]?[0-9]+[\\t ]*)|"
                + "([*][\\t ]*[+-]?[0-9]+[\\t ]*[*][\\t ]*x[\\t ]*)|"
                + "([*][\\t ]*x[\\t ]*)|"
                + "([*][\\t ]*[+-]?[0-9]+[\\t ]*))*+)*");
        Matcher m = p.matcher(str);
        if (m.find()) {
            String pstr = m.group();
            if (pstr.length() == str.length()) {
                return pstr;
            }
        }
        return "";
    }
    
    public String matchPower(String str) {
        Pattern p1 = Pattern.compile("\\)\\^[+-]?\\d+$");
        Pattern p2 = Pattern.compile("[+-]?\\d+$");
        Matcher m1 = p1.matcher(str);
        Matcher m2 = p2.matcher(str);
        String returnStr = "";
        if (m1.find()) {
            if (m2.find()) {
                returnStr = m2.group();
                if (Integer.valueOf(returnStr) > 10000) {
                    WrongExit bigPower = new WrongExit();
                }
            }
        }
        return returnStr;
    }
    
    public int matchTrig(String str,int mode) {
        int pos = -1;
        String substr;
        Pattern p;
        if (mode == 1) {
            p = Pattern.compile("^[+-]?[+-]?sin\\(");
        } else {
            p = Pattern.compile("^[+-]?[+-]?cos\\(");
        }
        Matcher m = p.matcher(str);
        if (m.find()) {
            pos = m.end();
            substr = str.substring(pos,str.length() - 1);
            p = Pattern.compile("^[+-]?\\d+$");
            if ((substr.charAt(0) == '+' || substr.charAt(0) == '-')
                    && !p.matcher(substr).find()) {
                WrongExit illegalExpr = new WrongExit();
            }
            if (split(substr,1).size() > 1 || split(substr,2).size() > 1) {
                WrongExit illegalExpr = new WrongExit();
            }
        }
        return pos;
    }
}
