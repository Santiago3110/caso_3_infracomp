import java.math.BigInteger;

public class DiffieHellman {
    // Valores de p y g proporcionados
    private static final BigInteger p = new BigInteger(
            "00caeddecbc02d778c9d56c91c7a95d8072444b05ada909ce3c9f74c1b1c5424a034d81849fff76e950e993d8a522b6a7df27c0003ae6b91baab7eb0a85a9aa9948ff340c38e1935b8fa31556906d48d7fdc983c00daddb8121272988d3760716806ce1c0c9442b4f4294f3b61255054dd5e111bc3a31451cdf...171a31",
            16);
    private static final BigInteger g = BigInteger.valueOf(2);

    // Método para generar la llave maestra
    public static BigInteger generarLlaveMaestra(BigInteger clavePrivada) {
        // Calcular la llave pública usando el valor de g y p
        BigInteger clavePublica = g.modPow(clavePrivada, p);
        return clavePublica;
    }

    public static byte[] generarIV() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return iv;
    }
    // BigInteger clavePrivada = new BigInteger(1024, new
    // java.security.SecureRandom());
    // BigInteger clavePublica = generarLlaveMaestra(clavePrivada);

}
