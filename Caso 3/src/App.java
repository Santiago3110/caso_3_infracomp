import java.util.*;

public class App {

    static CifradoAsimetrico cifrador = new CifradoAsimetrico();

    public static void main(String[] args) throws Exception {

        Scanner input = new Scanner(System.in);
        boolean estado = true;

        while (estado) {
            System.out.println("¿Desea tener un cliente o varios clientes?");

            menu();
            System.out.print("~ ");

            int opcion = 0;
            try {
                opcion = Integer.parseInt(input.nextLine());
            } catch (Exception e) {
                opcion = 0;
                System.out.println("Selecciones un numero\n");
            }

            switch (opcion) {
                case 1:
                    Servidor servidor = new Servidor();
                    Cliente clienteUnico = new Cliente();

                    Long reto = clienteUnico.getReto();
                    byte[] retoCifrado = servidor.resolverReto(reto, cifrador);
                    String descifrado = clienteUnico.verificarReto(retoCifrado, cifrador, servidor.getPublicKey());

                    if (descifrado.equals("OK")) {
                        System.out.println("Comunicación exitosa.");

                    } else if (descifrado.equals("ERROR")) {
                        System.out.println("Fallo en la comunicación.");
                    }

                    break;

                case 2:
                    System.out.println("Número de clientes: ");
                    int numClientes = input.nextInt();
                    for (int i = 0; i < numClientes; i++) {
                        Cliente clienteMultiple = new Cliente();
                        Servidor servidorDelegado = new Servidor();
                    }
                    break;

                case 3:
                    System.out.println("Hasta luego!");
                    estado = false;
                    break;

                default:
                    System.out.println("¡SELECCIONE 1, 2 o 3!\n" + //
                            "");
                    break;
            }

        }
        input.close();

    }

    public static void menu() {
        System.out.println("1. Un cliente");
        System.out.println("2. Varios clientes");
        System.out.println("3. Salir");
    }

}
