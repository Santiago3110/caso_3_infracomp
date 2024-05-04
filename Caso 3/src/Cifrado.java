import java.crypto.*;
import java.security.*;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public class Cifrado {
    private static final String PADDING = "AES/ECB/PKCS5Padding";

    public Cifrado() {

    }

    public byte[] firmar(PrivateKey clavePrivada, String informacion) throws Exception {
        byte[] datos = informacion.getBytes();
        Signature firma = Signature.getInstance("SHA256withRSA");
        firma.initSign(clavePrivada);
        firma.update(datos);
        return firma.sign();
    }

    public boolean verificarFirma(PublicKey clavePublica, String informacion, byte[] firmaByte) throws Exception {
        byte[] datos = informacion.getBytes();
        Signature firma = Signature.getInstance("SHA256withRSA");
        firma.initVerify(clavePublica);
        firma.update(datos);
        return firma.verify(firmaByte);
    }

    public byte[] cifrar(SecretKey llave, String texto) {
        byte[] textoCifrado;

        try {
            Cipher cifrador = Cipher.getInstance(PADDING);
            byte[] textoClaro = texto.getBytes();

            cifrador.init(Cipher.ENCRYPT_MODE, llave);
            textoCifrado = cifrador.doFinal(textoClaro);

            return textoCifrado;
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return null;
        }
    }

    public byte[] descifrar(SecretKey llave, byte[] texto) {
        byte[] textoClaro;

        try {
            Cipher cifrador = Cipher.getInstance(PADDING);
            cifrador.init(Cipher.DECRYPT_MODE, llave);
            textoClaro = cifrador.doFinal(texto);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return null;
        }
        return textoClaro;
    }

    // hash no funcional votos a favor 3 en contra 2
    // public byte[] crearHash(byte[] datos) throws NoSuchAlgorithmException {
    // MessageDigest digest = MessageDigest.getInstance("SHA-256");
    // return digest.digest(datos);
    // }

    // public static boolean verificarHash(String textoOriginal, byte[]
    // hashEntrante) {
    // try {
    // byte[] hashCalculado = calcularHashSHA1(textoOriginal);
    // return Arrays.equals(hashEntrante, hashCalculado);
    // } catch (NoSuchAlgorithmException e) {
    // e.printStackTrace();
    // return false;
    // }
    // }

}