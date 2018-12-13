package aps;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class GerenciaUpload extends Thread {
    
    private int id;

    private final Thread main;
    private DatagramSocket socket;
    private InetAddress ipOutroUsuario;
    private int portaUDPOutroUsuario;
    private String caminho;
    public int de;
    public int ate;
    public int tamanho;

    public GerenciaUpload(int id, Thread main, DatagramSocket socket, InetAddress ip, int portaUDP, String caminho, int de, int ate) {
        this.id = id;
        
        this.main = main;
        this.socket = socket;
        this.ipOutroUsuario = ip;
        this.portaUDPOutroUsuario = portaUDP;
        this.caminho = caminho;
        this.de = de;
        this.ate = ate;
        
        this.tamanho = this.ate - this.de;
    }

    private void iniciaTransferenciaArquivo(FileInputStream fis) throws IOException {
        int enviado = de;
        int total = de + ate;
        int falta = -1;
        System.out.println("Solicitado de " + de + " mais " + ate + "bytes");
        if(total >= 1024) {
            total = 1024;
            falta = ate;
        }else{
            total = ate;
        }
        long aux = 0;
        int totalMil = 0;
        
        fis.skip(enviado);
        while (aux < ate) {
            

            if(falta < 1024 && (de + ate) >= 1024){
                total = falta;
            }
                  
            byte[] pedaco = new byte[total];
          
            System.out.println(pedaco.length);
            int read = fis.read(pedaco, 0, pedaco.length);
            if (read < 0) {
                System.out.println("File end reached.");
                break;
            }
            if(read == 1024){
                totalMil++;
            }
            
            String t = "dute" + "," + String.valueOf(read);
            DatagramPacket packetTam = new DatagramPacket(t.getBytes(), t.getBytes().length, ipOutroUsuario, portaUDPOutroUsuario);
            socket.send(packetTam);

            System.out.println("Enviado (" + read + ") do arquivo para " + ipOutroUsuario.toString() + ":" + portaUDPOutroUsuario);
            DatagramPacket packet = new DatagramPacket(pedaco, pedaco.length, ipOutroUsuario, portaUDPOutroUsuario);
            socket.send(packet);
            
            aux+= packet.getLength();
            

            System.out.println("Sending file: " + enviado + "%");

            enviado += read;
            if (total == 1024){
                falta -= 1024;
            }
        }
        
        fis.close();
        System.out.println("TOTAL ENVIADO: "+ totalMil);
    }
    
    private void upload(FileInputStream fis) throws IOException {
        System.out.println("Primeira solicitação deve enviar de " + this.de + " até " + this.ate);
        
        int tamanhoPacote = 1024;
        if (this.tamanho < 1024) {
            tamanhoPacote = (int) this.tamanho;
        }
        
        // Move o curso para o local que deve começar a ler
        fis.skip(this.de);
        
        long enviado = this.de;
        while (enviado < this.ate) {
            long faltante = this.ate - enviado;
            if (faltante < tamanhoPacote) {
                // Quando envia um pacote menor do que o tamanho original
                tamanhoPacote = (int) faltante;
            }
            byte[] pedaco = new byte[tamanhoPacote];
            int lido = fis.read(pedaco);
            
            if (lido < 0) {
                System.out.println("Final do arquivo encontrado");
            }
            
            System.out.println("Lido " + lido + " do arquivo");
            DatagramPacket packet = new DatagramPacket(pedaco, pedaco.length, ipOutroUsuario, portaUDPOutroUsuario);
            socket.send(packet);
            
            System.out.println("Enviado " + lido + " bytes do arquivo para " + ipOutroUsuario.toString() + ":" + portaUDPOutroUsuario);
            
            if (!esperaRespostaOk(socket)) {
                System.out.println("A resposta não foi OK");
                continue;
            }
            
            System.out.println("Recebido resposta OK");
            enviado += lido;
        }
        
        fis.close();
        System.out.println("Final do envio. Arquivo fechado");
    }
    
    private boolean esperaRespostaOk(DatagramSocket socket) throws IOException {
        byte[] buffer = new byte[2];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        
        String content = new String(packet.getData(), 0, packet.getLength());
        return "OK".equals(content);
    }

    @Override
    public void run() {
        try {
            System.out.println("Iniciado upload do arquivo: " + caminho);
            File file = new File(caminho);
            FileInputStream fis = new FileInputStream(file);
            
            upload(fis);

            // iniciaTransferenciaArquivo(fis);

            fis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            System.out.println("Vamos testar esse aqui também :D");
        }
    }
}
