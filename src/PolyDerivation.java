import java.math.BigInteger;

public class PolyDerivation {
    private PolyTerm[] polyDerivation = new PolyTerm[3];
    
    public PolyDerivation(PolyTerm poly) {
        if (!poly.getDeg().equals(new BigInteger("0"))) {
            polyDerivation[0] = new PolyTerm(poly.getSinDeg(), poly.getCosDeg(),
                    poly.getCoeff().multiply(poly.getDeg()),
                    poly.getDeg().subtract(new BigInteger("1")));
        } else {
            polyDerivation[0] = new PolyTerm(new BigInteger("0"));
        }
        if (!poly.getSinDeg().equals(new BigInteger("0"))) {
            polyDerivation[1] = new PolyTerm(poly.getSinDeg().
                    subtract(new BigInteger("1")),
                    poly.getCosDeg().add(new BigInteger("1")),
                    poly.getCoeff().multiply(poly.getSinDeg()), poly.getDeg());
        } else {
            polyDerivation[1] = new PolyTerm(new BigInteger("0"));
        }
        if (!poly.getCosDeg().equals(new BigInteger("0"))) {
            polyDerivation[2] = new PolyTerm(poly.getSinDeg().
                    add(new BigInteger("1")),
                    poly.getCosDeg().subtract(new BigInteger("1")),
                    poly.getCoeff().multiply(poly.getCosDeg()).
                            multiply(new BigInteger("-1")), poly.getDeg());
        } else {
            polyDerivation[2] = new PolyTerm(new BigInteger("0"));
        }
    }
    
    public PolyTerm getPolyDe(int i) {
        return polyDerivation[i];
    }
}
