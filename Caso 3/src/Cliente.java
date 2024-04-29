import java.security.SecureRandom;
import java.util.*;

public class Cliente extends Thread {

    // Retos
    private Long reto;

    private int consulta;

    // Sesion
    private String login;
    private int clave; // C

    // LLaves

    public Cliente() {

        SecureRandom random = new SecureRandom();
        this.reto = random.nextLong(10000);

    }

    @Override
    public void run() {

    }
}
