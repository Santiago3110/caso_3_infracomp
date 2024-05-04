import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import L8.cifradoSimétrico;

public class Servidor extends Thread {

    // llaves
    private PublicKey publica;
    private PrivateKey privada;
    private SecretKey secretKey;
    private SecretKey secretKeyHMAC;

    // extras
    private BigInteger x;
    private BigInteger GX;
    private BigInteger GYX;
    private Random random;
    private static Map<String, byte[]> usuarios = new HashMap<>();

    public Servidor() throws NoSuchAlgorithmException {

        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        KeyPair keyPair = generator.generateKeyPair();
        this.publica = keyPair.getPublic();
        this.privada = keyPair.getPrivate();
        this.random = new Random();
        this.x = generarBigIntegerAleatorio();
    }

    @Override
    public void run() {

    }

    // FUNCIONALIDADES

    public void registrarUsuario(String login, String contraseña, Cifrado cifrador) {
        byte[] contraseñaCifrada = this.cifrar(contraseña, cifrador);
        usuarios.put(login, contraseñaCifrada);
    }

    public boolean verificarUsuario(byte[] login, byte[] contraseña, Cifrado cifrador) {

        String loginDesifrado = new String(descifrar(login, cifrador));
        byte[] contraseñaCifrada = usuarios.get(loginDesifrado);
        if (contraseñaCifrada == null) {
            System.out.println("Usuario no encontrado");
            return false;
        }

        byte[] contraseñaDescifradaRecibida = descifrar(contraseña, cifrador);
        byte[] contraseñaDescifradaGuardada = descifrar(contraseñaCifrada, cifrador);

        if (Arrays.equals(contraseñaDescifradaRecibida, contraseñaDescifradaGuardada)) {
            return true;
        } else {
            System.out.println("Contraseña incorrecta");
            return false;
        }

    }

    public byte[] resolverReto(Long reto, Cifrado cifrador) {
        try {
            byte[] firmaReto = cifrador.firmar(privada, String.valueOf(reto));
            return firmaReto;
        } catch (Exception e) {
            return null;
        }

    }

    public byte[] firmarPGGX(BigInteger p, BigInteger g, Cifrado cifrador) {
        this.GX = g.pow(this.x.intValue());
        String SGX = GX.toString();
        String data = p + "$";
        data += g;
        data += "$";
        data += SGX;
        try {
            byte[] firma = cifrador.firmar(this.privada, data);
            return firma;
        } catch (Exception e) {
            System.out.println("La excepción que ocurrió fue: " + e);
            return null;
        }

    }

    public BigInteger generarGYX(BigInteger gy) {
        this.GYX = gy.pow(this.x.intValue());
        return this.GYX;
    }

    public void generarLLaves() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        digest.update(GYX.toString().getBytes());
        byte[] hash = digest.digest();
        int mitad = hash.length / 2;
        byte[] primeraMitad = Arrays.copyOfRange(hash, 0, mitad);
        byte[] segundaMitad = Arrays.copyOfRange(hash, mitad, hash.length);

        byte[] claveCifrado = Arrays.copyOf(primeraMitad, 32);
        byte[] claveHMAC = Arrays.copyOf(segundaMitad, 32);

        this.secretKey = new SecretKeySpec(claveCifrado, "AES");
        this.secretKeyHMAC = new SecretKeySpec(claveHMAC, "AES");
    }

    public byte[] cifrar(String contraseña, Cifrado cifrador) {
        byte[] dataCifrado = cifrador.cifrar(this.secretKey, contraseña);
        return dataCifrado;
    }

    public byte[] descifrar(byte[] textoCifrado, Cifrado cifrador) {
        byte[] textoDescifrado = cifrador.descifrar(this.secretKey, textoCifrado);
        return textoDescifrado;
    }

    // GETTERS
    public PublicKey getPublicKey() {
        return publica;
    }

    public PrivateKey getPrivateKey() {
        return privada;
    }

    public BigInteger getGX() {
        return this.GX;
    }

    // FUNCIONES DE APOYO
    public BigInteger generarBigIntegerAleatorio() {
        SecureRandom secureRandom = new SecureRandom();
        BigInteger numeroAleatorio = new BigInteger(8, secureRandom).abs();
        return (numeroAleatorio);
    }

    public static byte[] generateRandomBytes(int length) {
        Random random = new Random();
        byte[] randomBytes = new byte[length];
        random.nextBytes(randomBytes);
        return randomBytes;
    }
}
