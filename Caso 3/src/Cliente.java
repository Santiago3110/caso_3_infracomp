import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.sound.sampled.BooleanControl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Cliente extends Thread {

    static Random aleatorio = new Random();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    // Retos
    private Long reto;
    private int consulta;

    // Sesion
    private String login;
    private String clave;

    // C
    private BigInteger gy;
    private BigInteger y;

    // LLaves
    private SecretKey secretKey;
    private SecretKey secretKeyHMAC;

    // Extras
    private BigInteger GXY;

    public Cliente() {

        SecureRandom random = new SecureRandom();
        this.reto = random.nextLong(10000);
        this.clave = generateRandomString(8);
        this.consulta = random.nextInt();

        this.login = UUID.randomUUID().toString();
        this.clave = generarClaveAleatoria();

    }

    public boolean verificarReto(PublicKey llavePublica, byte[] firmaByte, Cifrado cifrador) {
        try {
            String retoString = String.valueOf(reto);
            Boolean respuesta = cifrador.verificarFirma(llavePublica, retoString, firmaByte);
            return respuesta;

        } catch (Exception e) {
            return false;
        }

    }

    public Boolean verificarFirma(byte[] firmaPGGX, BigInteger p, BigInteger g, BigInteger gx, byte[] iv,
            PublicKey llavePublica, Cifrado cifrador) {

        String data = p + "$";
        data += g;
        data += "$";
        data += gx;
        try {
            Boolean respuesta = cifrador.verificarFirma(llavePublica, data, firmaPGGX);
            return respuesta;
        } catch (Exception e) {
            System.out.println("La excepción dada fue: " + e);
            return null;
        }

    }

    public BigInteger generarGY(BigInteger p, BigInteger g, BigInteger gx) {
        this.y = gx.mod(p);
        this.gy = g.pow(this.y.intValue());
        return this.gy;
    }

    public void generarGXY(BigInteger gx) {
        this.GXY = gx.pow(this.y.intValue());
    }

    public void generarLLaves() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        digest.update(GXY.toString().getBytes());
        byte[] hash = digest.digest();
        int mitad = hash.length / 2;
        byte[] primeraMitad = Arrays.copyOfRange(hash, 0, mitad);
        byte[] segundaMitad = Arrays.copyOfRange(hash, mitad, hash.length);

        byte[] claveCifrado = Arrays.copyOf(primeraMitad, 32);
        byte[] claveHMAC = Arrays.copyOf(segundaMitad, 32);

        this.secretKey = new SecretKeySpec(claveCifrado, "AES");
        this.secretKeyHMAC = new SecretKeySpec(claveHMAC, "AES");
    }

    public byte[] cifrarLogin(String login, Cifrado cifrador) {
        byte[] dataCifrado = cifrador.cifrar(this.secretKey, login);
        return dataCifrado;
    }

    public byte[] cifrarClave(String clave, Cifrado cifrador) {
        byte[] dataCifrado = cifrador.cifrar(this.secretKey, clave);
        return dataCifrado;
    }

    // GETTERS
    public Long getReto() {
        return this.reto;
    }

    public String getLogin() {
        return this.login;
    }

    public String getClave() {
        return this.clave;
    }

    @Override
    public void run() {

    }

    // FUNCIONES DE APOYO
    public static String generateRandomString(int length) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }

        return stringBuilder.toString();
    }

    public BigInteger generarBigIntegerAleatorio() {
        SecureRandom secureRandom = new SecureRandom();
        this.numeroAleatorio = new BigInteger(1024, new java.security.SecureRandom());
        return numeroAleatorio;
    }

    private String generarClaveAleatoria() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            char randomChar = (char) ('a' + Math.random() * ('z' - 'a' + 1));
            sb.append(randomChar);
        }
        return sb.toString();
    }
}
