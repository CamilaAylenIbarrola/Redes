import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Scanner;

public class ThreadRecibir implements Runnable{
    // atributos = cosas pasadas x parametro
    private DatagramSocket socket;
    private Scanner lectorArchConfig;
    private String nombrePActual; //nombre propio

    @Override
    public void run(){
        byte[]buffer=new byte[256]; //buffer = array de bytes, 256 de espacio en buffer
        DatagramPacket paqueteRecibo=new DatagramPacket(buffer, buffer.length); //se crea el paquete con el buffer dentro que tendra el mensaje a enviar
        try {
            socket.receive(paqueteRecibo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String respuesta=new String(paqueteRecibo.getData()); //los datos del datagrama los convierte en string
        System.out.println("llego el mensaje");
    }
}
