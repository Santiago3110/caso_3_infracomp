import java.math.BigInteger;
import java.security.*;
import java.util.Random;

public class Servidor extends Thread {

    // llaves
    private PublicKey publica;
    private PrivateKey privada;

    // extras
    private int x;
    private BigInteger gALaX;
    private Random random;

    public Servidor() throws NoSuchAlgorithmException {

        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        KeyPair keyPair = generator.generateKeyPair();
        this.publica = keyPair.getPublic();
        this.privada = keyPair.getPrivate();
        this.random = new Random();
        this.x = random.nextInt(0, 16);
    }

    @Override
    public void run() {

    }

    // FUNCIONALIDADES
    public byte[] resolverReto(Long reto, CifradoAsimetrico cifrador) {
        byte[] retoCifrado = cifrador.cifrar(privada, String.valueOf(reto));
        return retoCifrado;
    }

    public byte[] cifrar(String mensaje, CifradoAsimetrico cifrador) {
        byte[] mCifrado = cifrador.cifrar(privada, mensaje);
        return mCifrado;
    }

    public String generarDatosDH(BigInteger p, BigInteger g) {

        System.out.println("valor de x: " + this.x);
        BigInteger gALaX = g.pow(this.x);
        System.out.println("Calculado");
        String gToThePowerX = gALaX.toString();

        String data = p + "$";
        data += g;
        data += "$";
        data += gToThePowerX;
        return data;
    }

    // GETTERS
    public PublicKey getPublicKey() {
        return publica;
    }

    public PrivateKey getPrivateKey() {
        return privada;
    }

    public BigInteger getGX() {
        return this.gALaX;
    }

    // FUNCIONES DE APOYO
    public BigInteger generarBigIntegerAleatorio() {
        SecureRandom secureRandom = new SecureRandom();
        BigInteger numeroAleatorio = new BigInteger(1024, new java.security.SecureRandom());
        return numeroAleatorio;
    }

    public static byte[] generateRandomBytes(int length) {
        Random random = new Random();
        byte[] randomBytes = new byte[length];
        random.nextBytes(randomBytes);
        return randomBytes;
    }

}
