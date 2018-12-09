/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aps;

import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
    }
    
    public Usuario upload() throws NoSuchAlgorithmException, UnknownHostException{        
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
    
    public Mensagem search(){
        Mensagem men = new Mensagem();
        Scanner reader = new Scanner(System.in);
        
        System.out.println("O que deseja buscar? \t");
        String busca = reader.nextLine();
        men.setTexto(busca);
        
        return men;
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
                        if(arq.isEmpty()){
                            System.out.println("\nMensagem recebida do servidor: Nenhum arquivo encontrado!");
                        }else{
                            System.out.println("\nEscolha um arquivo dos recebidos: ");
                            for(Usuario u: arq){
                                System.out.println(u.getId() + " - " + u.getTitulo());                                
                            }
                            int escolha = reader.nextInt();
                            System.out.println("Escolhido o nº: " + escolha);
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


