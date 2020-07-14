import java.math.BigInteger;
//import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            String str = scanner.nextLine();
            //replace
            Pattern p = Pattern.compile(
                    "([x\\t \\d\\+\\-\\^\\(\\)\\*]|(sin)|(cos))+");
            Matcher m = p.matcher(str);
            Pattern p1 = Pattern.compile(
                    "([+-])[ \\t]*([+-])[\\t ]*([+-])" +
                            "[\\t ]+\\d+|\\d+[\\t ]\\d+" +
                            "|\\^[ \\t]*[+-][\\t ]+\\d+");
            if (m.find() == false || m.group().length() != str.length()
                    || p1.matcher(str).find()) {
                WrongExit illegalChar = new WrongExit();
            }
            str = str.replaceAll("[ \\t]", "");
            if (str.length() == 0) {
                WrongExit nullInput = new WrongExit();
            }
            //begin derivation
            Derivation derivation = new Derivation();
            String stringOut = derivation.derivation(str);
            Main main = new Main();
            stringOut = main.simply(stringOut);
            // stringOut = main.strip(stringOut);
            //stringOut = main.merge(stringOut);
            if (stringOut.length() == 0) {
                stringOut = "0";
            }
            if (stringOut.charAt(0) == '+') {
                stringOut = stringOut.substring(1);
            }
            System.out.println(stringOut);
        } catch (Exception e) {
            WrongExit nullInput = new WrongExit();
        }
    }
    
    public String simply(String str) {
        String stringOut = str;
        if (str.length() == 0) {
            stringOut = "0";
        }
        if (stringOut.length() > 2) {
            stringOut = stringOut.replaceAll("(\\+\\+)|(\\-\\-)", "+");
            stringOut = stringOut.replaceAll("(\\+\\-)|(\\-\\+)", "-");
            stringOut = stringOut.replaceAll("(\\+\\+)|(\\-\\-)", "+");
            stringOut = stringOut.replaceAll("(\\+\\-)|(\\-\\+)", "-");
            stringOut = stringOut.replaceAll("(\\*\\+)", "*");
            stringOut = stringOut.replaceAll("\\*\\+", "*1*");
            stringOut = stringOut.replaceAll("\\*\\-", "*-1*");
            stringOut = stringOut.replaceAll("\\(\\+", "(");
        }
        Pattern p2 = Pattern.compile("\\d+");
        Matcher m2 = p2.matcher(stringOut);
        while (m2.find()) {
            stringOut = stringOut.replaceAll(
                    m2.group(),String.valueOf(new BigInteger(m2.group())));
        }
        if (stringOut.charAt(0) == '+') {
            stringOut = stringOut.substring(1);
        }
        return stringOut;
    }
    /*
    public String strip(String strIn) {
        Derivation de = new Derivation();
        ArrayList<String> terms = de.split(strIn,1);
        for (int i = 0; i < terms.size(); i++) {
            ArrayList<String> term = de.split(terms.get(i),2);
            for (int j = 0; j < term.size();j++) {
                String temp = term.get(j);
                while(!temp.equals(de.strip(temp))) {
                    temp = de.strip(temp);
                }
                term.set(j,temp);
            }
            String temp1 = term.get(0);
            for (int k = 1; k < term.size(); k++) {
                temp1 = temp1 + '*' + term.get(k);
            }
            terms.set(i,temp1);
        }
        String str = terms.get(0);
        for (int i = 1; i < terms.size(); i++) {
            if (terms.get(i).length() != 0 &&
                    (terms.get(i).charAt(0) == '+'
                            || terms.get(i).charAt(0) == '-')) {
                str = str + terms.get(i);
            } else {
                str = str + '+' + terms.get(i);
            }
        }
        str = str.replaceAll("(\\*\\+)", "*");
        str = str.replaceAll("\\*\\+", "*1*");
        str = str.replaceAll("\\*\\-", "*-1*");
        return str;
    }
    
    public String merge(String strIn) {
        Derivation de = new Derivation();
        ArrayList<String> terms = de.split(strIn,1);
        Pattern p = Pattern.compile("((^[+-]?[\\t ]*("
                + "(sin[\\t ]*\\([\\t ]*x[\\t ]*\\))|"
                + "(cos[\\t ]*\\([\\t ]*x[\\t ]*\\)))"
                + "[\\t ]*[\\^][\\t ]*[+-]?[0-9]+[\\t ]*)"
                + "|(^[+-]?[ \\t]*((sin[\\t ]*\\([\\t ]*x[\\t ]*\\))"
                + "|(cos[\\t ]*\\([\\t ]*x[\\t ]*\\)))[\\t ]*)|"
                + "(^[+-]?[0-9]+[\\t ]*[*][\\t ]*x[\\t ]*"
                + "[\\^][\\t ]*[+-]?[0-9]+[\\t ]*)|"
                + "(^[+-]?[\\t ]*x[\\t ]*[\\^][\\t ]*[+-]?[0-9]+[\\t ]*)|"
                + "(^[+-]?[0-9]+[\\t ]*[*][\\t ]*x[\\t ]*)|"
                + "(^[+-]?[\\t ]*x[\\t ]*)|"
                + "(^[+-]?[\\t ]*[+-]?[0-9]+[\\t ]*))"
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
                + "([*][\\t ]*[+-]?[0-9]+[\\t ]*))*+$");
        Matcher m;
        for (int i = 0; i < terms.size(); i++) {
            m = p.matcher(terms.get(i));
            if (m.find()) {
                PolyTerm term = new PolyTerm(terms.get(i));
                terms.set(i,term.toString());
            }
        }
        String str = terms.get(0);
        for (int i = 1; i < terms.size(); i++) {
            if (terms.get(i).length() != 0 &&
                    (terms.get(i).charAt(0) == '+'
                            || terms.get(i).charAt(0) == '-')) {
                str = str + terms.get(i);
            } else {
                str = str + '+' + terms.get(i);
            }
        }
        return str;
    }*/
}
