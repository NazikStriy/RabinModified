import java.math.BigDecimal;
import java.math.BigInteger;
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
    private static BigDecimal ONE = BigDecimal.valueOf(1);
    private static BigDecimal TWO = BigDecimal.valueOf(2);
    private static BigDecimal p;
    private static BigDecimal q;
    private static BigDecimal z;
    private static Boolean cond = false;

    /*Special method of such algorithm
    in order to generate special conditions prime numbers
    */
    public static BigDecimal[] genKey(int bitLength) {
        Integer rate = 512;
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
        BigDecimal b1 = crypted.remainder(p);
        BigDecimal b2 = crypted.remainder(q);
        BigDecimal b3 = crypted.remainder(z);

        while (sqrt(b1.add(k1.multiply(p)), 1).remainder(ONE).compareTo(ZERO) != 0) {
            if (sqrt(b1.add(k1.multiply(p)), 1).remainder(ONE).compareTo(ZERO) != 0) {

                k1 = k1.add(ONE);
            }
        }
        radix1 = sqrt(b1.add(k1.multiply(p)), 1).remainder(p);

        while (sqrt(b2.add(k2.multiply(q)), 1).remainder(ONE).compareTo(ZERO) != 0) {
            if (sqrt(b2.add(k2.multiply(q)), 1).remainder(ONE).compareTo(ZERO) != 0) {

                k2 = k2.add(ONE);
            }
        }
        radix2 = sqrt(b2.add(k2.multiply(q)), 1).remainder(q);

        while (sqrt(b3.add(k3.multiply(z)), 1).remainder(ONE).compareTo(ZERO) != 0) {
            if (sqrt(b3.add(k3.multiply(z)), 1).remainder(ONE).compareTo(ZERO) != 0) {

                k3 = k3.add(ONE);
            }
        }
        radix3 = sqrt(b3.add(k3.multiply(z)), 1).remainder(z);

        BigDecimal p_a = p.subtract(radix1);
        BigDecimal p_b = q.subtract(radix2);
        BigDecimal p_z = z.subtract(radix3);

        BigDecimal m1 = z.multiply(q).multiply(Negative_One);
        BigDecimal m2 = z.multiply(p);
        BigDecimal m3 = p.multiply(q);

        BigDecimal out1;
        BigDecimal out2;
        BigDecimal out3;
        BigDecimal out4;
        BigDecimal out5;
        BigDecimal out6;
        BigDecimal out7;
        BigDecimal out8;

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

        System.out.println("p=" + p);
        System.out.println("q=" + q);
        System.out.println("z=" + z);
        System.out.println("n=" + n);

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

    public static BigDecimal sqrt(BigDecimal A, final int SCALE) {
        BigDecimal x0 = new BigDecimal("0");
        BigDecimal x1 = new BigDecimal(Math.sqrt(A.doubleValue()));
        while (!x0.equals(x1)) {
            x0 = x1;
            x1 = A.divide(x0, SCALE, ROUND_HALF_UP);
            x1 = x1.add(x0);
            x1 = x1.divide(TWO, SCALE, ROUND_HALF_UP);

        }
        return x1;
    }

}
