import java.io.*;
import java.net.*;
import java.util.Scanner;


public class Cliente {
    private static int puerto=4001;
    public static int getPuerto() {
        return puerto;
    }
    public static void setPuerto(int puerto) {
        Cliente.puerto = puerto;
    }
    private DatagramSocket socket; //socket upd

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
        while(lector.hasNextLine()){
            String linea=lector.nextLine();
            linea=linea.split(":")[0]; //separa por los puntos convirtiendolo en array y despues iguala "linea" a la posicion 0 (osea el nombre)
            if(linea.equals(nombre)){
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        String rutaConfig = "D:/Laboratorio/LABOA2023/Redes/src/archivoConfig.txt";
        File archivoConfig = new File(rutaConfig);
        String rutaPersona = "D:/Laboratorio/LABOA2023/Redes/src/archivoPersona.txt";
        File archivoPersona = new File(rutaPersona);
        try {
            Scanner scannerLector=new Scanner(archivoConfig);//para leer el archivoConfig.txt
            Scanner scannerLectorP=new Scanner(archivoPersona);//para leer el archivoPersona.txt
            String nombrePersona=Cliente.nombrePropio(scannerLectorP);
            if(Cliente.verificarNombre(nombrePersona, scannerLector)){
                System.out.println("se ha verificado tu nombre y estas en el archivo");
                String nombre=Cliente.nombreAenviar();
                if(Cliente.verificarNombre(nombre, scannerLector)) {
                    System.out.println("la persona esta en el archivo");
                }
            }else{
                System.out.println("no estas en el archivo, no podes enviar mensajes");
            }
        } catch (FileNotFoundException e){
            throw new RuntimeException(e);
        }

    }
}