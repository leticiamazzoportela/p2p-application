package aps;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GerenciaSolicitacaoArquivo extends Thread {
    
    private Usuario usuario;
    private DatagramSocket socket;
    
    public GerenciaSolicitacaoArquivo(Usuario usuario, DatagramSocket socket) {
        this.usuario = usuario;
        this.socket = socket;
    }
    
    private synchronized void esperaSolicitacao() throws IOException {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        
        socket.receive(packet);
        
        String conteudo = new String(packet.getData(), 0, packet.getLength());
        String[] partes = conteudo.split(",");
        
        String caminho = partes[0];
        int de = Integer.parseInt(partes[1]);
        int ate = Integer.parseInt(partes[2]);
        
        iniciaTransferencia(packet.getAddress(), packet.getPort(), caminho, de, ate);
    }
    
    private synchronized void iniciaTransferencia(InetAddress ip, int portaUDP, String caminho, int de, int ate) {
        new GerenciaUpload(this, socket, ip, portaUDP, caminho, de, ate)
            .start();
        
        try {
            this.wait();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                esperaSolicitacao();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
