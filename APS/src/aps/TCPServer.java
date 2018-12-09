package aps;

/**
 * TCPServer: Servidor para conexao TCP com Threads Descricao: Recebe uma
 * conexao, cria uma thread, recebe uma mensagem e finaliza a conexao
 */
import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class TCPServer {
    
    public static ArrayList<Usuario> arquivos = new ArrayList();
    
    public static void main(String args[]) {
        try {
            int serverPort = 8050; // porta do servidor

            /* cria um socket e mapeia a porta para aguardar conexao */
            ServerSocket listenSocket = new ServerSocket(serverPort);

            while (true) {
                System.out.println("Servidor aguardando conexao ...");

                /* aguarda conexoes */
                Socket clientSocket = listenSocket.accept();

                System.out.println("Cliente conectado ... Criando thread ...");

                /* cria um thread para atender a conexao */
                //LerMensagem ler = new LerMensagem(clientSocket);
                GerenciaMensagemServidor g = new GerenciaMensagemServidor(clientSocket, arquivos);

                /* inicializa a thread */
                //ler.start();
                g.start();
            } //while

        } catch (IOException e) {
            System.out.println("Listen socket:" + e.getMessage());
        } //catch
    } //main
} //class