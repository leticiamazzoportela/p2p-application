package aps;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class GerenciaUpload extends Thread {

    private final Thread main;
    private DatagramSocket socket;
    private InetAddress ipOutroUsuario;
    private int portaUDPOutroUsuario;
    private String caminho;
    private int de;
    private int ate;

    public GerenciaUpload(Thread main, DatagramSocket socket, InetAddress ip, int portaUDP, String caminho, int de, int ate) {
        this.main = main;
        this.socket = socket;
        this.ipOutroUsuario = ip;
        this.portaUDPOutroUsuario = portaUDP;
        this.caminho = caminho;
        this.de = de;
        this.ate = ate;
    }

    private void iniciaTransferenciaArquivo(FileInputStream fis) throws IOException {
        int enviado = de;
        int total = de + ate;
        int falta = -1;
        if(total > 1024){
            total = 1024;
            falta = de + ate;
        }
        
        
       // 2060 sobra 12
        while (enviado < (de + ate)) {
            if(falta < 1024 && (de + ate) >= 1024){
                total = falta;
            }
            byte[] pedaco = new byte[total];
            int read = fis.read(pedaco, enviado, ate);
            if (read < 0) {
                System.out.println("File end reached.");
                break;
            }
            String t = "dute" + "," + String.valueOf(read);
            DatagramPacket packetTam = new DatagramPacket(t.getBytes(), t.getBytes().length, ipOutroUsuario, portaUDPOutroUsuario);
            socket.send(packetTam);

            System.out.println("Enviado (" + read + ") do arquivo para " + ipOutroUsuario.toString() + ":" + portaUDPOutroUsuario);
            DatagramPacket packet = new DatagramPacket(pedaco, pedaco.length, ipOutroUsuario, portaUDPOutroUsuario);
            socket.send(packet);

            /*if (!esperaRespostaOkParteArquivo(socket)) {
                // TODO: Volta no loop e reenvia o pedaço
                continue;
            }*/

            System.out.println("Sending file: " + enviado + "%");

            enviado += read;
            if (total == 1024){
                falta -= 1024;
            }
        }
        
        fis.close();
        synchronized(main) {
            main.notify();
        }
    }

    private boolean esperaRespostaOkParteArquivo(DatagramSocket socket) throws IOException {
        byte[] buf = new byte[2];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        System.out.println("Esperando mensagem de respota OK do cliente");
        socket.receive(packet);

        String message = new String(packet.getData(), 0, packet.getLength());
        return "OK".equals(message.trim());
    }

    @Override
    public void run() {
        try {
            System.out.println("Iniciado upload do arquivo: " + caminho);
            File file = new File(caminho);
            FileInputStream fis = new FileInputStream(file);

            iniciaTransferenciaArquivo(fis);

            fis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
