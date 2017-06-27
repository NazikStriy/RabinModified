import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class RabinDecimal {
    private static Random r = new SecureRandom();
    private static BigDecimal Negative_One = BigDecimal.valueOf(-1);
    private static BigDecimal k1 = BigDecimal.valueOf(0);
    private static BigDecimal k2 = BigDecimal.valueOf(0);
    private static BigDecimal k3 = BigDecimal.valueOf(0);
    private static BigDecimal ZERO = BigDecimal.valueOf(0);
    private static BigDecimal ONE = BigDecimal.valueOf(1);
    private static BigDecimal TWO = BigDecimal.valueOf(2);
    private static BigDecimal p;
    private static BigDecimal q;
    private static BigDecimal z;
    private static Boolean cond = false;
    private static BigDecimal out1;
    private static BigDecimal out2;
    private static BigDecimal out3;
    private static BigDecimal out4;
    private static BigDecimal out5;
    private static BigDecimal out6;
    private static BigDecimal out7;
    private static BigDecimal out8;


    /*Special method of such algorithm
    in order to generate special conditions prime numbers
    */
    public static BigDecimal[] genKey(int bitLength) {
        Integer rate = 10;
        while (!cond) {
            p = new BigDecimal(BigInteger.probablePrime(rate, r));
            q = new BigDecimal(BigInteger.probablePrime(rate, r));
            if (p.equals(q)) {
                q = new BigDecimal(BigInteger.probablePrime(rate, r));
            } else if (p.compareTo(q) == 1) {
                q = new BigDecimal(BigInteger.probablePrime(rate, r));
            } else {
                if (p.add((p.multiply(p).add(ONE)).divide(q.subtract(p), 1, BigDecimal.ROUND_UP)).remainder(ONE).compareTo(ZERO) == 0) {
                    z = p.add((p.multiply(p).add(ONE)).divide(q.subtract(p), 1, BigDecimal.ROUND_UP));
                    cond = true;
                } else if (p.add((p.multiply(p).subtract(ONE)).divide(q.subtract(p), 1, BigDecimal.ROUND_UP)).remainder(ONE).compareTo(ZERO) == 0) {
                    z = p.add((p.multiply(p).subtract(ONE)).divide(q.subtract(p), 1, BigDecimal.ROUND_UP));
                    cond = true;
                }
            }
        }

        BigDecimal n = p.multiply(q).multiply(z);
        return new BigDecimal[]{n, p, q, z};
    }

/*Encryption function is the same as in original algorithm*/

    public static BigDecimal encrypt(BigDecimal word, BigDecimal n) {

        return BigDecimal.valueOf(word.toBigInteger().modPow(TWO.toBigInteger(), n.toBigInteger()).longValue());

    }

    /*Decryption contain 8 results which depend directly from secured key */
    public static BigDecimal[] decrypt(BigDecimal crypted, BigDecimal p, BigDecimal q, BigDecimal z) {

        BigDecimal n = p.multiply(q).multiply(z);
        BigDecimal radix1 = ONE;
        BigDecimal radix2 = ONE;
        BigDecimal radix3 = ONE;


        if (k1.multiply(k1).remainder(p).compareTo(crypted.remainder(p)) != 0) {

            while (k1.multiply(k1).remainder(p).compareTo(crypted.remainder(p)) != 0) {
                k1 = k1.add(ONE);
                radix1 = k1;
            }
        } else {
            radix1 = k1;
        }

        if (k2.multiply(k2).remainder(q).compareTo(crypted.remainder(q)) != 0) {

            while (k2.multiply(k2).remainder(q).compareTo(crypted.remainder(q)) != 0) {
                k2 = k2.add(ONE);
                radix2 = k2;
            }
        } else {
            radix2 = k2;
        }

        if (k3.multiply(k3).remainder(z).compareTo(crypted.remainder(z)) != 0) {

            while (k3.multiply(k3).remainder(z).compareTo(crypted.remainder(z)) != 0) {
                k3 = k3.add(ONE);
                radix3 = k3;
            }
        } else {
            radix3 = k3;
        }

        BigDecimal p_a = p.subtract(radix1);
        BigDecimal p_b = q.subtract(radix2);
        BigDecimal p_z = z.subtract(radix3);

        BigDecimal m1 = n.divide(p, 0).multiply(Negative_One);
        BigDecimal m2 = n.divide(q, 0);
        BigDecimal m3 = n.divide(z, 0);

        //y_p*p*m_q + y_q*q*m_p (mod n)
        if (((m1.multiply(radix1)).add(m2.multiply(radix2)).add(m3.multiply(radix3))).compareTo(ZERO) == -1) {
            out1 = ((m1.multiply(radix1)).add(m2.multiply(radix2)).add(m3.multiply(radix3))).add(n);
        } else {
            out1 = ((m1.multiply(radix1)).add(m2.multiply(radix2)).add(m3.multiply(radix3))).remainder(n);
        }

        if (((m1.multiply(radix1)).add(m2.multiply(radix2)).add(m3.multiply(p_z))).compareTo(ZERO) == -1) {
            out2 = ((m1.multiply(radix1)).add(m2.multiply(radix2)).add(m3.multiply(p_z))).add(n);
        } else {
            out2 = ((m1.multiply(radix1)).add(m2.multiply(radix2)).add(m3.multiply(p_z))).remainder(n);
        }

        if (((m1.multiply(radix1)).add(m2.multiply(p_b)).add(m3.multiply(radix3))).compareTo(ZERO) == -1) {
            out3 = ((m1.multiply(radix1)).add(m2.multiply(p_b)).add(m3.multiply(radix3))).add(n);
        } else {
            out3 = ((m1.multiply(radix1)).add(m2.multiply(p_b)).add(m3.multiply(radix3))).remainder(n);
        }

        if (((m1.multiply(radix1)).add(m2.multiply(p_b)).add(m3.multiply(p_z))).compareTo(ZERO) == -1) {
            out4 = ((m1.multiply(radix1)).add(m2.multiply(p_b)).add(m3.multiply(p_z))).add(n);
        } else {
            out4 = ((m1.multiply(radix1)).add(m2.multiply(p_b)).add(m3.multiply(p_z))).remainder(n);
        }

        if (((m1.multiply(p_a)).add(m2.multiply(radix2)).add(m3.multiply(radix3))).compareTo(ZERO) == -1) {
            out5 = ((m1.multiply(p_a)).add(m2.multiply(radix2)).add(m3.multiply(radix3))).add(n);
        } else {
            out5 = ((m1.multiply(p_a)).add(m2.multiply(radix2)).add(m3.multiply(radix3))).remainder(n);
        }

        if (((m1.multiply(p_a)).add(m2.multiply(radix2)).add(m3.multiply(p_z))).compareTo(ZERO) == -1) {
            out6 = ((m1.multiply(p_a)).add(m2.multiply(radix2)).add(m3.multiply(p_z))).add(n);
        } else {
            out6 = ((m1.multiply(p_a)).add(m2.multiply(radix2)).add(m3.multiply(p_z))).remainder(n);
        }

        if (((m1.multiply(p_a)).add(m2.multiply(p_b)).add(m3.multiply(radix3))).compareTo(ZERO) == -1) {
            out7 = ((m1.multiply(p_a)).add(m2.multiply(p_b)).add(m3.multiply(radix3))).add(n);
        } else {
            out7 = ((m1.multiply(p_a)).add(m2.multiply(p_b)).add(m3.multiply(radix3))).remainder(n);
        }

        if (((m1.multiply(p_a)).add(m2.multiply(p_b)).add(m3.multiply(p_z))).compareTo(ZERO) == -1) {
            out8 = ((m1.multiply(p_a)).add(m2.multiply(p_b)).add(m3.multiply(p_z))).add(n);
        } else {
            out8 = ((m1.multiply(p_a)).add(m2.multiply(p_b)).add(m3.multiply(p_z))).remainder(n);
        }

        System.out.println("compare=" + BigDecimal.valueOf(-5).remainder(BigDecimal.valueOf(35)));
        System.out.println("out1=" + out1);
        System.out.println("out2=" + out2);
        System.out.println("out3=" + out3);
        System.out.println("out4=" + out4);
        System.out.println("out5=" + out5);
        System.out.println("out6=" + out6);
        System.out.println("out7=" + out7);
        System.out.println("out8=" + out8);

        return new BigDecimal[]{out1, out2, out3, out4, out5, out6, out7, out8};
    }

}
