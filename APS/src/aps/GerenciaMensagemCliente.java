/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aps;

import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Elaine
 */
class GerenciaMensagemCliente extends Thread {

    ObjectOutputStream out;
    ObjectInputStream in;

    Socket socket;

    public GerenciaMensagemCliente(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        try {
            iniciaSocketUDP(new Usuario());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Usuario upload() throws NoSuchAlgorithmException, UnknownHostException, FileNotFoundException {
        Scanner reader = new Scanner(System.in);

        System.out.println("Informe o titulo: \t");
        String titulo = reader.nextLine();

        System.out.println("Informe a descrição: \t");
        String descricao = reader.nextLine();

        System.out.println("Informe o caminho do arquivo: \t");
        String caminho = reader.nextLine();

        Usuario user = new Usuario(titulo, descricao, caminho);

        return user;
    }

    private void iniciaSocketUDP(Usuario usuario) throws IOException {
        DatagramSocket atagram = new DatagramSocket(usuario.getPortaUDP());

        new GerenciaSolicitacaoArquivo(usuario, atagram)
                .start();
    }

    public Mensagem search() {
        Mensagem men = new Mensagem();
        Scanner reader = new Scanner(System.in);

        System.out.println("O que deseja buscar? \t");
        String busca = reader.nextLine();
        men.setTexto(busca);

        return men;
    }

    public void solicitaArquivo(ArrayList<Usuario> users, int qtd, long qtdRestante) throws SocketException, FileNotFoundException {
        int ultimoByteSolicitado = 0;
        for (int i = 0; i < users.size(); i++) {
            
            Usuario user = users.get(i);
            long tamanho = qtd;
            if (i == users.size() - 1) {
                tamanho = qtdRestante;
            }
            File file = new File("C:\\Users\\Elaine\\Documents\\GitHub\\p2p-application\\arquivosRecebidos\\" + user.getNome());
            FileOutputStream fos = new FileOutputStream(file);
            
            System.out.println("OIIIII EU ENTREEEIIIIIII");
            System.out.println("usuario: "+user.getId());
            System.out.println("inicia: "+ ultimoByteSolicitado);
            System.out.println("tamanho: "+ tamanho);
            
            new GerenciaDownload(user, fos, tamanho, ultimoByteSolicitado)
                .start();

            ultimoByteSolicitado += tamanho ;

        }
    }

    public void baixaArquivo(ArrayList<Usuario> users, int escolha) throws SocketException, FileNotFoundException {
        ArrayList<Usuario> usuariosDownload = new ArrayList();
        Usuario usuarioEscolhido = users.get(escolha);
        for (Usuario u : users) {
            if (u.getMd5().equals(usuarioEscolhido.getMd5())) {
                usuariosDownload.add(u);
            }
        }
        int qtdUsuarios = usuariosDownload.size();
        int qtd = (int) Math.ceil(usuarioEscolhido.getTamanho() / qtdUsuarios);
        long qtdRestante = qtd + (usuarioEscolhido.getTamanho() % qtdUsuarios);
        solicitaArquivo(usuariosDownload, qtd, qtdRestante);
    }

    @Override
    public void run() {
        Scanner reader = new Scanner(System.in);
        int opcao;

        try {
            while (true) {
                System.out.println("\nMenu: \n 1 - UPLOAD \n 2 - SEARCH \n 3 - SAIR");
                opcao = reader.nextInt();
                switch (opcao) {
                    case 1:
                        out.writeObject(upload());
                        Mensagem m = (Mensagem) in.readObject();
                        System.out.println("\nMensagem recebida do servidor: \t" + m.getTexto());
                        break;
                    case 2:
                        out.writeObject(search());
                        ArrayList<Usuario> arq = (ArrayList<Usuario>) in.readObject();
                        if (arq.isEmpty()) {
                            System.out.println("\nMensagem recebida do servidor: Nenhum arquivo encontrado!");
                        } else {
                            System.out.println("\nEscolha um arquivo dos recebidos: ");
                            for (Usuario u : arq) {
                                System.out.println(u.getId() + " - " + u.getTitulo());
                            }
                            int escolha = reader.nextInt();
                            baixaArquivo(arq, escolha);
                        }
                    case 3:
                        return;
                    default:
                        System.out.println("Opção inválida.");
                }
            }
        } catch (EOFException eofe) {
            System.out.println("EOF:" + eofe.getMessage());
        } catch (IOException ioe) {
            System.out.println("IO:" + ioe.getMessage());
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(GerenciaMensagemCliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GerenciaMensagemCliente.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                socket.close();
                out.close();
                in.close();
            } catch (IOException ioe) {
                System.out.println("IO: " + ioe);
            }
        }

    }

}
