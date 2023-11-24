import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Cliente {
    private static int puerto=4001;

    public static int getPuerto() {
        return puerto;
    }
    public static void setPuerto(int puerto) {
        Cliente.puerto = puerto;
    }

    public static String obtenerIP() throws UnknownHostException {
        InetAddress ip = InetAddress.getLocalHost();
        return ip.getHostAddress();
    }

    /*
    public static void anotarse(){
        String ruta = "D:/Laboratorio/LABOA2023/Redes/src/archivoConfig.txt"; //el txt se pone para poder crear el archivo de texto - poner la ruta del cole
        File archivo = new File(ruta);
        if(!archivo.exists()){
            try {
                archivo.createNewFile(); //se crea el archivo de text
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        FileWriter fw = new FileWriter(archivo);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(String);
        bw.close();
    }
 */
    public static String nombreAenviar(){
        Scanner s1=new Scanner(System.in); //para leer
        System.out.println("nombre de a quien le quiere mandar el msj:");
        String nombre=s1.next();
        return nombre;
    }
    public static boolean verificarNombre(String nombre, Scanner lector){
        while(lector.hasNextLine()){
            String linea=lector.nextLine();
            linea=linea.split(":")[0]; //separa por los puntos convirtiendolo en array y despues iguala linea a la posicion 0 (osea el nombre)
            if(linea.equals(nombre)){
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        String ruta = "D:/Laboratorio/LABOA2023/Redes/src/archivoConfig.txt";
        File archivo = new File(ruta);
        Scanner s1=new Scanner(System.in);
        try {

            Scanner scannerLector=new Scanner(archivo);//para leer el archivoConfig.txt
            String nombre=Cliente.nombreAenviar();
            if(Cliente.verificarNombre(nombre, scannerLector)){
                System.out.println("Algo");
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}