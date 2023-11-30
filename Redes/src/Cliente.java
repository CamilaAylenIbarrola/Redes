import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import static java.lang.Integer.parseInt;


public class Cliente {
    private static int puerto=4001;
    public static int getPuerto() {
        return puerto;
    }
    public static void setPuerto(int puerto) {
        Cliente.puerto = puerto;
    }

    public static String nombreAenviar(){
        Scanner s1=new Scanner(System.in); //para leer el nombre que se va a pasar
        System.out.println("nombre de a quien le quiere mandar el msj:");
        String nombre1=s1.next();
        return nombre1;
    }
    public static String nombrePropio(Scanner scannerLector){ //metodo para guardar el nombre propio
        String nombreP=scannerLector.next();
        return nombreP;
    }
    public static boolean verificarNombre(String nombre, Scanner lector){ //verifica si los nombres estan en el archivo
        String linea=lector.nextLine();
        for(int i=0;i<linea.split("-").length;i++){ //entre mas - haya, mas veces se va a correr, y por cada vuelta vamos a interesarnos en la persona que estemos parados
            String persona=linea.split("-")[i]; //separa por los - convirtiendolo en array, separando a las personas
            String nombrePerso=persona.split(":")[0];
            if(nombrePerso.equals(nombre)){
                return true;
            }
        }
        return false;
    }

    public static void mandarMensaje(String emisor, String mensaje, String receptor, DatagramSocket socket, Scanner lectorArchConfig){
        String mensajeCompleto=receptor + ":" + mensaje + ":" + emisor;
        InetAddress ipSiguiente;
        int puertoSiguiente;
        String linea=lectorArchConfig.nextLine();
        for(int i=0;i<linea.split("-").length;i++){ //se va a recorrer la lista con todas las personas y por cada vuelta se para en cada persona
            String persona=linea.split("-")[i]; //crea un array separando a cada persona una de otra por los -
            String nombrePerso=persona.split(":")[0];
            if(nombrePerso.equals(emisor)){
                try{
                if(i<linea.split("-").length){ //se fija si no es el ultimo en la lista
                    ipSiguiente=InetAddress.getByName(linea.split("-")[i+1].split(":")[1]); //igualas la ipSiguiente a la ip de la siguiente persona en la que estas parado
                    puertoSiguiente=parseInt(linea.split("-")[i+1].split(":")[2]); //igualas el puerto al puerto de la siguiente persona en la que estes parado
                }else{
                    ipSiguiente=InetAddress.getByName(linea.split("-")[0].split(":")[1]);
                    puertoSiguiente=parseInt(linea.split("-")[0].split(":")[2]);
                }} catch (UnknownHostException e) {
                    throw new RuntimeException(e);
                }
                byte[] b1=mensajeCompleto.getBytes(StandardCharsets.UTF_8); //se crea un buffer
                DatagramPacket paqueteEnvio=new DatagramPacket(b1, b1.length, ipSiguiente, puertoSiguiente);
                try {
                    socket.send(paqueteEnvio);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void main(String[] args){
        String rutaConfig = "D:/Redes/Redes/Redes/src/archivoConfig.txt";
        File archivoConfig = new File(rutaConfig);
        String rutaPersona = "D:/Redes/Redes/Redes/src/archivoPersona.txt";
        File archivoPersona = new File(rutaPersona);
        try{
            Scanner leerMensaje=new Scanner(System.in);
            DatagramSocket socket=new DatagramSocket(puerto);
            Scanner scannerLector=new Scanner(archivoConfig);//para leer el archivoConfig.txt
            Scanner scannerLectorP=new Scanner(archivoPersona);//para leer el archivoPersona.txt
            String nombrePersona=Cliente.nombrePropio(scannerLectorP);
            if(Cliente.verificarNombre(nombrePersona, scannerLector)) {
                System.out.println("se ha verificado tu nombre y estas en el archivo");
                while(true){
                    Thread escucha=new Thread(new ThreadRecibir(socket, scannerLector, nombrePersona));
                    escucha.start(); //se corre el hilo
                    String nombreReceptor=Cliente.nombreAenviar();
                    if(Cliente.verificarNombre(nombreReceptor, scannerLector)){
                        System.out.println("la persona esta en el archivo");
                        System.out.println("escriba su mensaje: ");
                        String mensaje= leerMensaje.next();
                        Cliente.mandarMensaje(nombrePersona, mensaje, nombreReceptor, socket, scannerLector);
                    }
                }
            }else{
                System.out.println("no estas en el archivo, no podes enviar mensajes");
            }

        } catch (FileNotFoundException e){
            throw new RuntimeException(e);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

    }
}