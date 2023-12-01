import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class ThreadRecibir implements Runnable{
    // atributos = cosas pasadas x parametro
    private DatagramSocket socket;
    private String lectorArchConfig;
    private String nombrePActual;

    public ThreadRecibir(DatagramSocket socket, String lectorArchConfig, String nombrePActual) {
        this.socket = socket;
        this.lectorArchConfig = lectorArchConfig;
        this.nombrePActual = nombrePActual;
    }

    public static void mandarAlSiguiente(String emisor, String mensaje, String receptor, DatagramSocket socket, String linea, String nombrePActual){
        String mensajeCompleto=receptor + ":" + mensaje + ":" + emisor;
        InetAddress ipSiguiente;
        int puertoSiguiente;
        for(int i=0;i<linea.split("-").length;i++){
            String persona=linea.split("-")[i];
            String nombrePerso=persona.split(":")[0];
            if(nombrePerso.equals(nombrePActual)){
                try{
                    if(i<linea.split("-").length-1){
                        ipSiguiente=InetAddress.getByName(linea.split("-")[i+1].split(":")[1]);
                        puertoSiguiente=parseInt(linea.split("-")[i+1].split(":")[2]);
                    }else{
                        ipSiguiente=InetAddress.getByName(linea.split("-")[0].split(":")[1]);
                        puertoSiguiente=parseInt(linea.split("-")[0].split(":")[2]);
                    }} catch (UnknownHostException e) {
                    throw new RuntimeException(e);
                }
                byte[] b1=mensajeCompleto.getBytes(StandardCharsets.UTF_8); //se crea un buffer y se pone el mensajecompleto en bytes
                DatagramPacket paqueteEnvio=new DatagramPacket(b1, b1.length, ipSiguiente, puertoSiguiente);
                try {
                    socket.send(paqueteEnvio);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void run(){
        while(true) {
            byte[] buffer = new byte[256]; //buffer = array de bytes
            DatagramPacket paqueteRecibo = new DatagramPacket(buffer, buffer.length); //se crea el paquete con el buffer dentro que tendra el mensaje a enviar
            try {
                socket.receive(paqueteRecibo);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String respuesta = new String(paqueteRecibo.getData()); //los datos del datagrama los convierte en string
            System.out.println("llego un mensaje");
            if (respuesta.split(":")[0].equals(nombrePActual)) {
                System.out.println("te llego el mensaje: " + respuesta.split(":")[1] + " /de parte de: " + respuesta.split(":")[2]);
                System.out.println("nombre de a quien le quiere mandar el msj: ");
            } else {
                System.out.println("este mensaje no es para mi, reenvio al siguiente");
                mandarAlSiguiente(respuesta.split(":")[2], respuesta.split(":")[1], respuesta.split(":")[0], socket, lectorArchConfig, nombrePActual);
                System.out.println("nombre de a quien le quiere mandar un msj: ");
            }
        }
    }
}
