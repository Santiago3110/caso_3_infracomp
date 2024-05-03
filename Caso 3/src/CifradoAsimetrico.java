import java.security.*;
import javax.crypto.*;

public class CifradoAsimetrico {
    public CifradoAsimetrico() {

    }

    public byte[] cifrar(Key llave, String mensaje) {
        byte[] textoCifrado;
        try {
            Cipher cifrador = Cipher.getInstance("RSA");
            byte[] textoClaro = mensaje.getBytes();
            cifrador.init(Cipher.ENCRYPT_MODE, llave);
            textoCifrado = cifrador.doFinal(textoClaro);
            return textoCifrado;

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return null;
        }
    }

    // USAR AHORITA
    // String descifradoClaro = new String(descifradoSim, StandardCharsets.UTF_8);

    public byte[] descifrar(Key llave, byte[] texto) {
        byte[] textoClaro;

        try {
            Cipher cifrador = Cipher.getInstance("RSA");
            cifrador.init(Cipher.DECRYPT_MODE, llave);
            textoClaro = cifrador.doFinal(texto);
            return textoClaro;

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return null;
        }

    }

}
