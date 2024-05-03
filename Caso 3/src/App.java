import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.math.BigInteger;

public class App {

    static CifradoSimetrico cifradoSimetrico = new CifradoSimetrico();
    static CifradoAsimetrico cifrador = new CifradoAsimetrico();
    static DiffieHellman diffieHellman = new DiffieHellman();

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

                        // Diffie Hellman datos
                        System.out.println("Sacando datos DH");
                        BigInteger p = diffieHellman.getP();
                        BigInteger g = diffieHellman.getG();
                        byte[] iv = diffieHellman.generarIV();

                        System.out.println("generando datos DH");
                        String infoDH = servidor.generarDatosDH(p, g);
                        System.out.println("Datos DH: " + infoDH);

                        System.out.println("Cifrando datos DH");
                        byte[] infoDHCifrada = servidor.cifrar(infoDH, cifrador);

                        System.out.println("Verificando datos DH");
                        String respuestaVerificacionDH = clienteUnico.verificarDH(infoDHCifrada, p, g,
                                servidor.getGX(), iv, servidor.getPublicKey(), cifrador);

                        if (respuestaVerificacionDH.equals("OK")) {
                            System.out.println("Verificación exitosa de DH.");

                        } else if (respuestaVerificacionDH.equals("ERROR")) {
                            System.out.println("Fallo en la verficacion DH.");
                        }

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
