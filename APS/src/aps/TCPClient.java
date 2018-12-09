package aps;

/**
 * * TCPClient: Cliente para conexao TCP Descricao: Envia uma informacao ao *
 * servidor e recebe confirmações ECHO Ao enviar "PARAR", a conexão é *
 * finalizada.
 */
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class TCPClient {

    public static void main(String args[]) throws IOException {
        Socket clientSocket = null; // socket do cliente        
        Scanner reader = new Scanner(System.in); // ler mensagens via teclado        
        try {
            /* Endereço e porta do servidor */ int serverPort = 8050;
            InetAddress serverAddr = InetAddress.getByName("127.0.0.1");
            /* conecta com o servidor */ 
            clientSocket = new Socket(serverAddr, serverPort);
            //LerMensagem ler = new LerMensagem(clientSocket);
            GerenciaMensagemCliente g = new GerenciaMensagemCliente(clientSocket);
            g.start();
            //ler.start();
        } catch (UnknownHostException ue) {
            System.out.println("Socket:" + ue.getMessage());
        }
    } //main
} //class
