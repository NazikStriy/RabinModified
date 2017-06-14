import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.Random;

import static java.math.BigDecimal.ROUND_HALF_UP;

public class RabinDecimal {
    private static Random r = new SecureRandom();
    private static BigDecimal Negative_One = BigDecimal.valueOf(-1);
    private static BigDecimal k1 = BigDecimal.valueOf(0);
    private static BigDecimal k2 = BigDecimal.valueOf(0);
    private static BigDecimal k3 = BigDecimal.valueOf(0);
    private static BigDecimal ZERO = BigDecimal.valueOf(0);
    private static BigDecimal TWO = BigDecimal.valueOf(2);
    private static BigDecimal THREE = BigDecimal.valueOf(3);
    private static BigDecimal FOUR = BigDecimal.valueOf(4);


    public static BigDecimal[] genKey(double bitLength) {
        BigDecimal p = BigDecimal.valueOf(109);
        BigDecimal q = BigDecimal.valueOf(113);
        BigDecimal z = BigDecimal.valueOf(3079);

        /*BigInteger p = blumPrime(bitLength/2);
        BigInteger q = blumPrime(bitLength/2);
        BigInteger z = blumPrime(bitLength/2);*/
        BigDecimal N = p.multiply(q).multiply(z);
        return new BigDecimal[]{N,p,q,z};
    }


    public static BigDecimal encrypt(BigDecimal m, BigDecimal N) {

        return BigDecimal.valueOf (m.toBigInteger().modPow(TWO.toBigInteger(), N.toBigInteger()).longValue());

    }

    public static BigDecimal[] decrypt(BigDecimal crypted, BigDecimal p , BigDecimal q, BigDecimal z) {

        BigDecimal N = p.multiply(q).multiply(z);
        BigDecimal radix1;
        BigDecimal radix2;
        BigDecimal radix3;

        if (sqrt(crypted.remainder(p), 3).remainder(BigDecimal.ONE).equals(ZERO) == false) {

            while ((sqrt((crypted.remainder(p)),3).add(k1.multiply(p))).remainder(BigDecimal.ONE) != BigDecimal.ZERO) {
                k1= k1.add(BigDecimal.ONE);

            }

            radix1 = sqrt((crypted.remainder(p)),3).add(k1.multiply(p)).remainder(p);
        }
        else {

            radix1 = sqrt((crypted.remainder(p)),3).add(k1).multiply(p).remainder(p);
        }


        if (sqrt(crypted.remainder(q), 3).remainder(BigDecimal.ONE).equals(ZERO) == false) {
            while ((sqrt((crypted.remainder(q)),3).add(k2.multiply(q))).remainder(BigDecimal.ONE) != BigDecimal.ZERO) {
                k2= k2.add(BigDecimal.ONE);

            }

            radix2 = sqrt((crypted.remainder(q)),3).add(k2.multiply(q)).remainder(q);
        }
        else {

            radix2 = sqrt((crypted.remainder(q)),3).add(k2).multiply(q).remainder(q);
        }


        if (sqrt(crypted.remainder(z), 3).remainder(BigDecimal.ONE).equals(ZERO) == false) {
            while ((sqrt((crypted.remainder(z)),3).add(k3.multiply(z))).remainder(BigDecimal.ONE) != BigDecimal.ZERO) {
                k3= k3.add(BigDecimal.ONE);

            }

            radix3 = sqrt((crypted.remainder(z)),3).add(k3.multiply(z)).remainder(z);
        }
        else {

            radix3 = sqrt((crypted.remainder(z)),3).add(k3).multiply(z).remainder(z);
        }

        BigDecimal p_a = p.subtract(radix1);
        BigDecimal p_b = p.subtract(radix2);
        BigDecimal p_z = p.subtract(radix3);

        BigDecimal m1 = N.divide(p).multiply(Negative_One);
        BigDecimal m2 = N.divide(q);
        BigDecimal m3 = N.divide(z);

        //y_p*p*m_q + y_q*q*m_p (mod n)
        BigDecimal out1 = ((m1.multiply(radix1)).add(m2.multiply(radix2)).add(m3.multiply(radix3))).remainder(N);
        BigDecimal out2 =((m1.multiply(radix1)).add(m2.multiply(radix2)).add(m3.multiply(p_z))).remainder(N);
        BigDecimal out3 =((m1.multiply(radix1)).add(m2.multiply(p_b)).add(m3.multiply(radix3))).remainder(N);
        BigDecimal out4 =((m1.multiply(radix1)).add(m2.multiply(p_b)).add(m3.multiply(p_z))).remainder(N);
        BigDecimal out5 =((m1.multiply(p_a)).add(m2.multiply(radix2)).add(m3.multiply(radix3))).remainder(N);
        BigDecimal out6 =((m1.multiply(p_a)).add(m2.multiply(radix2)).add(m3.multiply(p_z))).remainder(N);
        BigDecimal out7 =((m1.multiply(p_a)).add(m2.multiply(p_b)).add(m3.multiply(radix3))).remainder(N);
        BigDecimal out8 =((m1.multiply(p_a)).add(m2.multiply(p_b)).add(m3.multiply(p_z))).remainder(N);

        return new BigDecimal[]{out1,out2,out3,out4,out5,out6,out7,out8};
    }


    public static BigDecimal sqrt(BigDecimal in, int scale){
        BigDecimal sqrt = new BigDecimal(1);
        sqrt.setScale(scale + 3, RoundingMode.FLOOR);
        BigDecimal store = new BigDecimal(in.toString());
        boolean first = true;
        do{
            if (!first){
                store = new BigDecimal(sqrt.toString());
            }
            else first = false;
            store.setScale(scale + 3, RoundingMode.FLOOR);
            sqrt = in.divide(store, scale + 3, RoundingMode.FLOOR).add(store).divide(
                    BigDecimal.valueOf(2), scale + 3, RoundingMode.FLOOR);
        }while (!store.equals(sqrt));
        return sqrt.setScale(scale, RoundingMode.FLOOR);
    }

    /*public static BigInteger sqrt(BigInteger x) {
        BigInteger div = BigInteger.ZERO.setBit(x.bitLength()/2);
        BigInteger div2 = div;
        // Loop until we hit the same value twice in a row, or wind
        // up alternating.
        for(;;) {
            BigInteger y = div.add(x.divide(div)).shiftRight(1);
            if (y.equals(div) || y.equals(div2))
                return y;
            div2 = div;
            div = y;
        }
    }*/

    public static BigInteger blumPrime(int bitLength) {
        BigInteger p;
        do {
            p=BigInteger.probablePrime(bitLength,r);
        }
        while(!p.mod(FOUR.toBigInteger()).equals(THREE));
        return p;
    }
}
