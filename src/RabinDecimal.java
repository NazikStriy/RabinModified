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


    public static BigDecimal[] genKey(int bitLength) {
        BigDecimal p = new BigDecimal(BigInteger.probablePrime(5, r));
        BigDecimal q = new BigDecimal(BigInteger.probablePrime(5, r));
        BigDecimal z = new BigDecimal(BigInteger.probablePrime(5, r));

        /*BigDecimal p = BigDecimal.valueOf(191);
        BigDecimal q = BigDecimal.valueOf(229);
        BigDecimal z = BigDecimal.valueOf(1151);*/

        BigDecimal n = p.multiply(q).multiply(z);
        return new BigDecimal[]{n, p, q, z};
    }


    public static BigDecimal encrypt(BigDecimal word, BigDecimal n) {

        return BigDecimal.valueOf(word.toBigInteger().modPow(TWO.toBigInteger(), n.toBigInteger()).longValue());

    }

    public static BigDecimal[] decrypt(BigDecimal crypted, BigDecimal p, BigDecimal q, BigDecimal z) {

        BigDecimal n = p.multiply(q).multiply(z);
        BigDecimal radix1 = BigDecimal.ONE;
        BigDecimal radix2 = BigDecimal.ONE;
        BigDecimal radix3 = BigDecimal.ONE;


        if (k1.multiply(k1).remainder(p).compareTo(crypted.remainder(p)) != 0) {

            while (k1.multiply(k1).remainder(p).compareTo(crypted.remainder(p)) != 0) {
                k1 = k1.add(BigDecimal.ONE);
                radix1 = k1;
            }
        } else {
            radix1 = k1;
        }

        if (k2.multiply(k2).remainder(q).compareTo(crypted.remainder(q)) != 0) {

            while (k2.multiply(k2).remainder(q).compareTo(crypted.remainder(q)) != 0) {
                k2 = k2.add(BigDecimal.ONE);
                radix2 = k2;
            }
        } else {
            radix2 = k2;
        }

        if (k3.multiply(k3).remainder(z).compareTo(crypted.remainder(z)) != 0) {

            while (k3.multiply(k3).remainder(z).compareTo(crypted.remainder(z)) != 0) {
                k3 = k3.add(BigDecimal.ONE);
                radix3 = k3;
            }
        } else {
            radix3 = k3;
        }

        System.out.println("radix1=" + radix1);
        System.out.println("k1=" + k1);
        System.out.println("radix2=" + radix2);
        System.out.println("k2=" + k2);
        System.out.println("radix3=" + radix3);
        System.out.println("k3=" + k3);

        BigDecimal p_a = p.subtract(radix1);
        BigDecimal p_b = q.subtract(radix2);
        BigDecimal p_z = z.subtract(radix3);

        BigDecimal m1 = n.divide(p).multiply(Negative_One);
        BigDecimal m2 = n.divide(q);
        BigDecimal m3 = n.divide(z);

        //y_p*p*m_q + y_q*q*m_p (mod n)
        BigDecimal out1 = ((m1.multiply(radix1)).add(m2.multiply(radix2)).add(m3.multiply(radix3))).remainder(n);

        if (out1.compareTo(BigDecimal.ZERO) == -1) {
            out1 = out1.add(n);
        }

        BigDecimal out2 = ((m1.multiply(radix1)).add(m2.multiply(radix2)).add(m3.multiply(p_z))).remainder(n);

        if (out2.compareTo(BigDecimal.ZERO) == -1) {
            out2 = out2.add(n);
        }

        BigDecimal out3 = ((m1.multiply(radix1)).add(m2.multiply(p_b)).add(m3.multiply(radix3))).remainder(n);

        if (out3.compareTo(BigDecimal.ZERO) == -1) {
            out3 = out3.add(n);
        }

        BigDecimal out4 = ((m1.multiply(radix1)).add(m2.multiply(p_b)).add(m3.multiply(p_z))).remainder(n);

        if (out4.compareTo(BigDecimal.ZERO) == -1) {
            out4 = out4.add(n);
        }

        BigDecimal out5 = ((m1.multiply(p_a)).add(m2.multiply(radix2)).add(m3.multiply(radix3))).remainder(n);

        if (out5.compareTo(BigDecimal.ZERO) == -1) {
            out5 = out5.add(n);
        }

        BigDecimal out6 = ((m1.multiply(p_a)).add(m2.multiply(radix2)).add(m3.multiply(p_z))).remainder(n);

        if (out6.compareTo(BigDecimal.ZERO) == -1) {
            out6 = out6.add(n);
        }

        BigDecimal out7 = ((m1.multiply(p_a)).add(m2.multiply(p_b)).add(m3.multiply(radix3))).remainder(n);

        if (out7.compareTo(BigDecimal.ZERO) == -1) {
            out7 = out7.add(n);
        }

        BigDecimal out8 = ((m1.multiply(p_a)).add(m2.multiply(p_b)).add(m3.multiply(p_z))).remainder(n);

        if (out8.compareTo(BigDecimal.ZERO) == -1) {
            out8 = out8.add(n);
        }

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
