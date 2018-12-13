package aps;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class GerenciaDownload extends Thread {
    
    public int id = 0;

    private Arquivo arquivo;
    private final RandomAccessFile raf;
    public long de;
    public long ate;
    public long tamanho;
    
    private int pos;
    private DatagramSocket socket;

    public GerenciaDownload(int id, Arquivo arquivo, RandomAccessFile raf, long de, long ate) throws SocketException {
        this.id = id;
        
        this.arquivo = arquivo;
        this.raf = raf;
        this.de = de;
        this.ate = ate;
        this.tamanho = this.ate - this.de;
        // this.pos = pos;
        this.socket = new DatagramSocket();
    }

    public Arquivo getUser() {
        return arquivo;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public void setUser(Arquivo user) {
        this.arquivo = user;
    }

    public long getTamanho() {
        return ate;
    }

    public void setTamanho(long tamanho) {
        this.ate = tamanho;
    }
    
    private void download() throws IOException {
        System.out.println("Thread " + id + " precisa receber de " + this.de + " até " + this.ate);
        
        int tamanhoPacote = 1024;
        
        long recebido = this.de;
        while (recebido < this.ate) {
            long faltante = this.ate - recebido;
            if (faltante < tamanhoPacote) {
                // Quando recebe um pacote menor do que o tamanho original
                tamanhoPacote = (int) faltante;
            }
            
            System.out.println("Recebido: " + recebido);
            byte[] buffer = new byte[tamanhoPacote];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            
            byte[] pedaco = packet.getData();
            System.out.println("Tamanho pedaco: " + pedaco.length);
            System.out.println("Tamanho recebido: " + packet.getLength());
            
            System.out.println("Thread " + id + " moveu o ponteiro para " + recebido + " para escrever até");
            raf.seek(recebido);
            raf.write(pedaco);
            
            enviaRespostaOk(socket);
            
            recebido += packet.getLength();
        }
    }
    
    private void enviaRespostaOk(DatagramSocket socket) throws IOException {
        byte[] resposta = "OK".getBytes();
        InetAddress ip = InetAddress.getByName(this.arquivo.getIp());
        DatagramPacket packet = new DatagramPacket(resposta, resposta.length, ip, this.arquivo.getPortaUDP());
        socket.send(packet);
    }

    @Override
    public void run() {
        byte[] conteudo = (arquivo.getCaminho() + "," + this.de + "," + this.ate).getBytes();
        try {
            InetAddress ip = InetAddress.getByName(this.arquivo.getIp());
            DatagramPacket packet = new DatagramPacket(conteudo, conteudo.length, ip, this.arquivo.getPortaUDP());
            socket.send(packet);

            System.out.println("Enviado solicitação de download para o outro cliente");
            
            download();

        } catch (IOException ex) {
            ex.printStackTrace();
            
        } finally {
            System.out.println("Vamos testar isso :D");
        }
    }

}
