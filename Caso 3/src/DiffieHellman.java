import java.math.BigInteger;
import java.security.SecureRandom;

public class DiffieHellman {

    private final BigInteger p = new BigInteger(
            "00caeddecbc02d778c9d56c91c7a95d8072444b05ada909ce3c9f74c1b1c5424a034d81849fff76e950e993d8a522b6a7df27c0003ae6b91baab7eb0a85a9aa9948ff340c38e1935b8fa31556906d48d7fdc983c00daddb81212729888d3760716806ce1c0c9442b4f4294f3b61255054dd5e111bc3a31451cdfe10da5d484c317",
            16);
    private final BigInteger g = BigInteger.valueOf(2);

    public BigInteger generarLlaveMaestra(BigInteger X) {
        BigInteger y = g.modPow(X, p);
        return y;
    }

    public byte[] generarIV() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    // Getters
    public BigInteger getP() {
        return this.p;
    }

    public BigInteger getG() {
        return this.g;
    }
}
