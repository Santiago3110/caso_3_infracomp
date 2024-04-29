import java.util.*;

public class App {

    CifradoAsimetrico cifradoAsimetrico = new CifradoAsimetrico();

    public static void main(String[] args) throws Exception {

        System.out.println("Bienvenido al programa de .......");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Número de clientes: ");
        int numClientes = scanner.nextInt();
        Servidor servidor = new Servidor();
        for (int i = 0; i < numClientes; i++) {
            Cliente cliente = new Cliente();

        }

    }
}
