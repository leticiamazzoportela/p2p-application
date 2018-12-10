/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aps;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.System.out;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 * @author Elaine
 */
public class GerenciaDownload extends Thread {

    private Usuario user;
    private FileOutputStream fos;
    private long tamanho;
    private int pos;
    private DatagramSocket socket;

    public GerenciaDownload(Usuario user, FileOutputStream fos, long tamanho, int pos) throws SocketException {
        this.user = user;
        this.fos = fos;
        this.tamanho = tamanho;
        this.pos = pos;
        this.socket = new DatagramSocket();
    }

    public Usuario getUser() {
        return user;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public long getTamanho() {
        return tamanho;
    }

    public void setTamanho(long tamanho) {
        this.tamanho = tamanho;
    }

    public void iniciaTransferencia() throws IOException {
        int parts = (int) Math.ceil(tamanho / 1024.0);
        System.out.println("parts: " + parts);
        int tamanhoPacote = 1024;
        int lido = this.pos;
        int count = 1;
        System.out.println("Aguardando envio da primeira parte do arquivo");
        while (lido < (tamanho + this.pos)) {
            System.out.println("Tamanho Pacote: " + tamanho);
            byte[] buf = new byte[tamanhoPacote];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            byte[] pedaco = packet.getData();
            String tam = new String(pedaco, "UTF-8");
            System.out.println("\n\nPACOTE RECEBIDO: "+tam+"\n");
            
            if (tam.startsWith("dute")) {
                tamanhoPacote = Integer.parseInt(tam.split(",")[1].trim());
            } else {
                System.out.println("Recebido pedaço do arquivo do " + packet.getAddress() + ":" + packet.getPort());
                System.out.println("Receiving file: " + (count * 100 / parts) + "%");

                //fos.write(pedaco, lido, packet.getLength());
                
                System.out.println("TAMMMMM : " + tam);
                System.out.println(ByteBuffer.wrap(tam.getBytes()));
                
                
                FileChannel ch = fos.getChannel();
                ch.position(lido);
                
                ch.write(ByteBuffer.wrap(tam.getBytes()));
                
                
                
                
                
                System.out.println("Escrito pedaço do arquivo :)");

                System.out.println("Enviando respota OK para " + packet.getAddress() + ":" + packet.getPort());
               // enviaRespostaOk(packet.getAddress(), packet.getPort());

                count++;
                lido += packet.getLength();
                tamanhoPacote = 1024;
                System.out.println("Lido (bytes): " + lido);
            }

        }

        fos.close();
    }

    private void enviaRespostaOk(InetAddress ip, int porta) throws IOException {
        byte[] chunk = "OK".getBytes();
        DatagramPacket packet = new DatagramPacket(chunk, chunk.length, ip, porta);

        socket.send(packet);
        System.out.println("Enviado resposta para o outro usuário");
    }

    @Override
    public void run() {
        byte[] conteudo = (user.getCaminho() + "," + pos + "," + tamanho).getBytes();
        try {
            InetAddress ip = InetAddress.getByName(this.user.getIp());
            DatagramPacket packet = new DatagramPacket(conteudo, conteudo.length, ip, this.user.getPortaUDP());
            socket.send(packet);

            System.out.println("Enviado solicitação de download para o outro cliente");
            iniciaTransferencia();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
