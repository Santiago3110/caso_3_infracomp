import java.security.*;
import java.security.NoSuchAlgorithmException;

public class Servidor extends Thread {

    private PublicKey publica;
    private PrivateKey privada;

    public Servidor() throws NoSuchAlgorithmException {

        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        KeyPair keyPair = generator.generateKeyPair();
        this.publica = keyPair.getPublic();
        this.privada = keyPair.getPrivate();

    }

    @Override
    public void run() {

    }

    public PublicKey getPublicKey() {
        return publica;
    }

    public PrivateKey getPrivateKey() {
        return privada;
    }

    public byte[] resolverReto(Long reto, CifradoAsimetrico cifrador) {
        byte[] retoCifrado = cifrador.cifrar(privada, reto);
        return retoCifrado;
    }

    public static byte[] generateRandomBytes(int length) {
        Random random = new Random();
        byte[] randomBytes = new byte[length];
        random.nextBytes(randomBytes);
        return randomBytes;
    }

}
