import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;

public class RabinTestDecimal {
    public static void main(String[] args) throws UnsupportedEncodingException {
        int counter = 0;
        int trials = 1;
        for (int i = 0; i < trials; i++) {
            BigDecimal[] key = RabinDecimal.genKey(512);
            BigDecimal n = key[0];
            BigDecimal p = key[1];
            BigDecimal q = key[2];
            BigDecimal z = key[3];
            String s = "naz";
            BigInteger word = new BigInteger(s.getBytes(Charset.forName("ascii")));
            BigDecimal crypted = RabinDecimal.encrypt(BigDecimal.valueOf(word.longValue()), n);

            System.out.println("word=" + word);
            System.out.println("crypted=" + crypted);
            boolean worked = false;
            BigDecimal[] m2 = RabinDecimal.decrypt(crypted, p, q, z);

            for (BigDecimal b : m2) {
                String dec = new String(b.toBigInteger().toByteArray(), Charset.forName("ascii"));
                if (dec.equals(s)) {
                    worked = true;

                }
            }
            if (worked) counter++;
        }

        System.out.println("worked " + counter + "/" + trials + " times");
    }
}
