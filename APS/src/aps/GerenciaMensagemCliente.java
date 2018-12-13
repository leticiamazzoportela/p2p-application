package aps;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

class GerenciaMensagemCliente extends Thread {

    ObjectOutputStream out;
    ObjectInputStream in;

    Socket socket;

    public GerenciaMensagemCliente(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        try {
            iniciaSocketUDP(new Arquivo());
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Arquivo upload() throws NoSuchAlgorithmException, UnknownHostException, FileNotFoundException {
        Scanner reader = new Scanner(System.in);

        System.out.print("Informe o titulo: ");
        String titulo = reader.nextLine();

        System.out.print("Informe a descrição: ");
        String descricao = reader.nextLine();

        System.out.print("Informe o caminho do arquivo: ");
        String caminho = reader.nextLine();

        Arquivo arquivo = new Arquivo(titulo, descricao, caminho);

        return arquivo;
    }

    private void iniciaSocketUDP(Arquivo usuario) throws IOException {
        DatagramSocket atagram = new DatagramSocket(usuario.getPortaUDP());

        new GerenciaSolicitacaoArquivo(usuario, atagram)
                .start();
    }

    public Mensagem search() {
        Mensagem men = new Mensagem();
        Scanner reader = new Scanner(System.in);

        System.out.print("O que deseja buscar? ");
        String busca = reader.nextLine();
        men.setTexto(busca);

        return men;
    }

    public void baixaArquivo(ArrayList<Arquivo> arquivos, int escolha) throws SocketException, FileNotFoundException {
        ArrayList<Arquivo> downloads = new ArrayList();
        Arquivo arquivoSelecionado = arquivos.get(escolha);
        
        for (Arquivo arquivo : arquivos) {
            if (arquivo.checksum.equals(arquivoSelecionado.checksum)) {
                downloads.add(arquivo);
            }
        }
        
        int quantidade = downloads.size();
        
        int tamanhoPorUsuario = (int) Math.ceil(arquivoSelecionado.getTamanho() / quantidade);
        System.out.println("Cada usuário deve enviar: " + tamanhoPorUsuario);
        
        long tamanhoUltimoUsuario = tamanhoPorUsuario + (arquivoSelecionado.getTamanho() % quantidade);
        System.out.println("O último usuário deve enviar: " + tamanhoUltimoUsuario);
        
        
        RandomAccessFile fos = new RandomAccessFile("C:\\Users\\Elaine\\Documents\\GitHub\\p2p-application\\arquivosRecebidos\\" + arquivoSelecionado.getNome(), "rw");
        
        for (int index = 0; index < downloads.size(); index++) {
            boolean ehUltimoUsuario = (index == downloads.size() - 1);
            
            long de = (index * tamanhoPorUsuario);
            long ate = (de + tamanhoPorUsuario);
            if (ehUltimoUsuario) {
                ate = (de + tamanhoUltimoUsuario);
            }
            
            Arquivo download = downloads.get(index);
            new GerenciaDownload((index + 1), download, fos, de, ate)
                .start();
        }
    }

    @Override
    public void run() {
        Scanner reader = new Scanner(System.in);
        int opcao;

        try {
            while (true) {
                System.out.println("\nMenu: \n 1 - UPLOAD \n 2 - SEARCH \n 3 - SAIR");
                System.out.print("Escolha uma opção: ");
                opcao = reader.nextInt();
                switch (opcao) {
                    case 1:
                        out.writeObject(upload());
                        Mensagem m = (Mensagem) in.readObject();
                        System.out.println("\nMensagem recebida do servidor: \t" + m.getTexto());
                        break;
                    case 2:
                        out.writeObject(search());
                        ArrayList<Arquivo> arq = (ArrayList<Arquivo>) in.readObject();
                        if (arq.isEmpty()) {
                            System.out.println("\nMensagem recebida do servidor: Nenhum arquivo encontrado!");
                        } else {
                            System.out.println("\nEscolha um arquivo dos recebidos: ");
                            for (Arquivo u : arq) {
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
