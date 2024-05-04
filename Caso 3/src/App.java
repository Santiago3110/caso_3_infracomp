import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;

import javax.sound.sampled.UnsupportedAudioFileException;

import java.math.BigInteger;

public class App {

    static DiffieHellman diffieHellman = new DiffieHellman();

    public static void main(String[] args) throws Exception {
        Cifrado cifrador = new Cifrado();
        Scanner input = new Scanner(System.in);
        boolean estado = true;

        while (estado) {
            menuClientes();
            int opcion = 0;
            try {
                opcion = Integer.parseInt(input.nextLine());
            } catch (Exception e) {
                opcion = 0;
                System.out.println("Selecciones un numero\n");
            }

            switch (opcion) {
                case 1:
                    manejarUnCliente(cifrador, input);
                    break;
                case 2:
                    manejarVariosClientes(input);
                    break;
                case 3:
                    System.out.println("Hasta luego!");
                    estado = false;
                    break;
                default:
                    System.out.println("¡SELECCIONE 1, 2 o 3!\n");
                    break;
            }
        }
        input.close();
    }

    public static void manejarUnCliente(Cifrado cifrador, Scanner input) throws Exception {
        Servidor servidor = new Servidor();
        Cliente clienteUnico = new Cliente();

        Long reto = clienteUnico.getReto();
        byte[] retoCifrado = servidor.resolverReto(reto, cifrador);
        boolean respuesta = clienteUnico.verificarReto(servidor.getPublicKey(), retoCifrado, cifrador);

        if (respuesta) {
            System.out.println("OK 2");
            BigInteger p = diffieHellman.getP();
            BigInteger g = diffieHellman.getG();
            byte[] iv = diffieHellman.generarIV();

            byte[] firmaPGGX = servidor.firmarPGGX(p, g, cifrador);
            BigInteger gx = servidor.getGX();

            Boolean respuestaDH = clienteUnico.verificarFirma(firmaPGGX, p, g, gx, iv,
                    servidor.getPublicKey(), cifrador);

            if (respuestaDH) {
                System.out.println("OK 7");
                BigInteger gy = clienteUnico.generarGY(p, g, gx);

                servidor.generarGYX(gy);
                servidor.generarLLaves();
                clienteUnico.generarGXY(gx);
                clienteUnico.generarLLaves();
                System.out.println("Continuar");

                manejarMenuIniciarSesion(servidor, clienteUnico, cifrador, input);

            } else {
                System.out.println("ERROR 7");
            }
        } else {
            System.out.println("ERROR 2");
        }
    }

    public static void manejarVariosClientes(Scanner input) {
        System.out.println("Número de clientes: ");
        int numClientes = input.nextInt();
        for (int i = 0; i < numClientes; i++) {
            Cliente clienteMultiple = new Cliente();
            Servidor servidorDelegado = new Servidor();
        }
    }

    public static void manejarMenuIniciarSesion(Servidor servidor, Cliente cliente, Cifrado cifrador, Scanner input)
            throws Exception {
        boolean estadoIniciarSesion = true;

        while (estadoIniciarSesion) {
            menuIniciarSesion();
            int opcion2 = 0;

            try {
                opcion2 = Integer.parseInt(input.nextLine());
            } catch (Exception e) {
                opcion2 = 0;
                System.out.println("Selecciones un numero\n");
            }

            switch (opcion2) {
                case 1:
                    System.out.println("Ingrese su usuario;");
                    System.out.print("~ ");
                    String loginCrear = input.nextLine();

                    System.out.println("Ingrese su contraseña;");
                    System.out.print("~ ");
                    String claveCrear = input.nextLine();
                    servidor.registrarUsuario(loginCrear, claveCrear, cifrador);
                    System.out.println("Usuario registrado con éxito: " + loginCrear);
                    break;

                case 2:
                    System.out.println("Ingrese su usuario;");
                    System.out.print("~ ");
                    String login = input.nextLine();
                    byte[] loginCifrado = cliente.cifrarLogin(login, cifrador);

                    System.out.println("Ingrese su contraseña;");
                    System.out.print("~ ");
                    String clave = input.nextLine();
                    byte[] claveCifrada = cliente.cifrarClave(clave, cifrador);

                    Boolean acceso = servidor.verificarUsuario(loginCifrado, claveCifrada,
                            cifrador);

                    if (acceso) {
                        System.out.println("OK 16");

                    } else {
                        System.out.println("Error 16");

                    }

                    break;

                case 3:
                    System.out.println("Hasta luego!");
                    estadoIniciarSesion = false;
                    break;

                default:
                    System.out.println("¡SELECCIONE 1, 2 o 3!\n");
                    break;
            }
        }
    }

    public static void menuClientes() {
        System.out.println("¿Desea tener un cliente o varios clientes?");
        System.out.println("====Menu 1=====");
        System.out.println("1. Un cliente");
        System.out.println("2. Varios clientes");
        System.out.println("3. Salir");
        System.out.println("===============");
        System.out.print("~ ");
    }

    public static void menuIniciarSesion() {
        System.out.println("\n====Menu 2 ====");
        System.out.println("1. Crear Sesion");
        System.out.println("2. Iniciar Sesion");
        System.out.println("3. Salir");
        System.out.println("=============");
        System.out.print("~ ");
    }
}