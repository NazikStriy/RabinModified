import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;

public class RabinTestDecimal {
    public static void main(String[] args) throws UnsupportedEncodingException {
        int counter = 0;
        int trials = 1;
        for(int i=0;i<trials;i++) {
            BigDecimal[] key = RabinDecimal.genKey(512);
            BigDecimal N = key[0];
            BigDecimal p = key[1];
            BigDecimal q = key[2];
            BigDecimal z = key[3];

            String s = "A";
            BigInteger m = new BigInteger(s.getBytes(Charset.forName("ascii")));
            BigDecimal crypted = RabinDecimal.encrypt(BigDecimal.valueOf(m.longValue()), N);

            boolean worked = false;
            BigDecimal[] m2 = RabinDecimal.decrypt(crypted, p, q, z);
           /* System.out.println("worked "+worked);
            System.out.println("N="+N);
            System.out.println("m="+m);
            System.out.println("p="+p);
            System.out.println("q="+q);
            System.out.println("z="+z);
            System.out.println("crypted="+crypted);*/
            for(BigDecimal b:m2) {
                String dec = new String(b.toBigInteger().toByteArray(), Charset.forName("ascii"));
                if(dec.equals(s)) {
                    worked = true;
                    System.out.println("N="+N);
                    System.out.println("m="+m);
                    System.out.println("p="+p);
                    System.out.println("q="+q);
                    System.out.println("z="+z);
                }
            }
            if(worked)counter++;
        }

        System.out.println("worked "+counter+"/"+trials+" times");
    }
}
