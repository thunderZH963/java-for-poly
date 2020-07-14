import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PolyCompute {
    private ArrayList<String> terms = new ArrayList<String>();
    private ArrayList<String> ops = new ArrayList<String>();
    private ArrayList<PolyTerm> polyArray = new ArrayList<PolyTerm>();
    private ArrayList<PolyDerivation> polyDe = new ArrayList<PolyDerivation>();
    private ArrayList<PolyTerm> polyFinaList = new ArrayList<>();
    
    public PolyCompute() { }
    
    public PolyCompute(String strIn) {
        if (strIn.equals("")) {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
        String str = frontDeal(strIn);
        if (str.equals("")) {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
        getTermsAndOps(str);
        removeOps();
        for (int i = 0; i < terms.size(); i++) {
            polyArray.add(new PolyTerm(terms.get(i)));
        }
        for (int i = 0; i < terms.size(); i++) {
            polyDe.add(new PolyDerivation(polyArray.get(i)));
            polyFinaList.add(polyDe.get(i).getPolyDe(0));
            polyFinaList.add(polyDe.get(i).getPolyDe(1));
            polyFinaList.add(polyDe.get(i).getPolyDe(2));
        }
        merge();
        simplify();
    }
    
    public String newTrim(String strIn) {
        String str = strIn;
        while (str.length() > 0 &&
                (str.charAt(0) == ' ' || str.charAt(0) == '\t')) {
            str = str.substring(1);
        }
        return str;
    }
    
    public String frontDeal(String strIn) {
        String str = newTrim(strIn);
        if (str.equals("")) {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
        if (str.charAt(0) == '+') {
            return newTrim(str.substring(1));
        } else if (str.charAt(0) == '-') {
            str = newTrim(str.substring(1));
            if (str.length() == 0) {
                System.out.println("WRONG FORMAT!");
                System.exit(0);
            }
            if (str.charAt(0) == '-') {
                return '+' + str.substring(1);
            } else if (str.charAt(0) == '+') {
                return "-" + str.substring(1);
            } else {
                return "-" + str;
            }
        }
        return str;
    }
    
    public void getTermsAndOps(String strIn) {
        String str = strIn;
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
                + "([*][\\t ]*[+-]?[0-9]+[\\t ]*))*+");
        Matcher m = p.matcher(str);
        int nextStart = 0;
        while (str.length() != 0) {
            if (m.find() == true) {
                nextStart = m.end();
                terms.add(m.group());
                str = str.substring(nextStart);
                str = newTrim(str);
                if (str.length() != 0 &&
                        str.charAt(0) != '+' && str.charAt(0) != '-') {
                    System.out.println("WRONG FORMAT!");
                    System.exit(0);
                }
                if (str.length() == 0) {
                    break;
                }
                //System.out.println(str.charAt(0));
                ops.add("" + str.charAt(0));
                str = str.substring(1);
                str = newTrim(str);
                m = p.matcher(str);
            } else {
                System.out.println("WRONG FORMAT!");
                System.exit(0);
            }
        }
        if (terms.size() == ops.size()) {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
    }
    
    public void removeOps() {
        for (int i = 1; i < terms.size(); i++) {
            if (ops.get(i - 1).equals("-") && terms.get(i).charAt(0) == '-') {
                String sub = terms.get(i);
                sub = sub.substring(1);
                terms.set(i,sub);
            } else if (ops.get(i - 1).equals("-") &&
                    terms.get(i).charAt(0) == '+') {
                String sub = terms.get(i);
                sub = "-" + sub.substring(1);
                terms.set(i,sub);
            } else if (ops.get(i - 1).equals("-")) {
                String sub = "-" + terms.get(i);
                terms.set(i,sub);
            }
        }
    }
    
    public void merge() {
        for (int i = 0; i < polyFinaList.size(); i++) {
            for (int j = i + 1; j < polyFinaList.size(); j++) {
                if (polyFinaList.get(i).
                        getDeg().equals(polyFinaList.get(j).getDeg())
                        && polyFinaList.get(i).
                        getSinDeg().equals(polyFinaList.get(j).getSinDeg())
                        && polyFinaList.get(i).
                        getCosDeg().equals(polyFinaList.get(j).getCosDeg())) {
                    polyFinaList.get(i).
                            changeCoeff(polyFinaList.get(i).getCoeff().
                            add(polyFinaList.get(j).getCoeff()));
                    polyFinaList.remove(j);
                    j--;
                }
            }
        }
    }

    public void simplify() {
        for (int i = 0; i < polyFinaList.size(); i++) {
            if (polyFinaList.get(i).getSinDeg().
                    compareTo(new BigInteger("2")) >= 0) {
                for (int j = i + 1; j < polyFinaList.size(); j++) {
                    if (polyFinaList.get(j).getCosDeg().
                            subtract(new BigInteger("2")).
                            equals(polyFinaList.get(i).getCosDeg()) &&
                            polyFinaList.get(j).getSinDeg().
                                    equals(polyFinaList.get(i).getSinDeg().
                                            subtract(new BigInteger("2"))) &&
                            polyFinaList.get(j).getDeg().
                                    equals(polyFinaList.get(i).getDeg()) &&
                            polyFinaList.get(j).getCoeff().
                                    equals(polyFinaList.get(i).getCoeff())) {
                        polyFinaList.set(i,
                                new PolyTerm(
                                        polyFinaList.get(i).getSinDeg().
                                                subtract(new BigInteger("2")),
                                        polyFinaList.get(i).getCosDeg(),
                                        polyFinaList.get(i).getCoeff(),
                                        polyFinaList.get(i).getDeg()));
                        polyFinaList.remove(j);
                        break;
                    }
                }
            }
            if (polyFinaList.get(i).getCosDeg().
                    compareTo(new BigInteger("2")) >= 0) {
                for (int j = i + 1; j < polyFinaList.size(); j++) {
                    if (polyFinaList.get(j).
                            getSinDeg().subtract(new BigInteger("2")).
                            equals(polyFinaList.get(i).getSinDeg()) &&
                            polyFinaList.get(j).getCosDeg().
                                    equals(polyFinaList.get(i).getCosDeg().
                                            subtract(new BigInteger("2"))) &&
                            polyFinaList.get(j).getDeg().
                                    equals(polyFinaList.get(i).getDeg()) &&
                            polyFinaList.get(j).getCoeff().
                                    equals(polyFinaList.get(i).getCoeff())) {
                        polyFinaList.set(i,
                                new PolyTerm(
                                        polyFinaList.get(j).getSinDeg().
                                                subtract(new BigInteger("2")),
                                        polyFinaList.get(j).getCosDeg(),
                                        polyFinaList.get(i).getCoeff(),
                                        polyFinaList.get(i).getDeg()));
                        polyFinaList.remove(j);
                        break;
                    }
                }
            }
        }
    }
    
    public String polyResult(String str) {
        String stringOut = "";
        try {
            PolyCompute polyCompute = new PolyCompute(str);
            
            for (int i = 0; i < polyCompute.polyFinaList.size(); i++) {
                stringOut = stringOut
                        + polyCompute.polyFinaList.get(i).toString();
            }
            if (stringOut.length() == 0) {
                return "0";
            } else if (stringOut.charAt(0) == '+') {
                stringOut = stringOut.substring(1);
                return stringOut;
            } else {
                return stringOut;
            }
        } catch (Exception e) {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
        return stringOut;
    }
    
}
