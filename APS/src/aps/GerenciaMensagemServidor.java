/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aps;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Elaine
 */
class GerenciaMensagemServidor extends Thread {

    ObjectOutputStream out;
    ObjectInputStream in;
    ArrayList<Arquivo> arquivos;

    Socket socket;

    public GerenciaMensagemServidor(Socket socket, ArrayList<Arquivo> arquivos) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.arquivos = arquivos;
    }

    public void upload(Arquivo user) throws NoSuchAlgorithmException, UnknownHostException, IOException {
        int tam = this.arquivos.size();
        user.setId(tam);
        Mensagem m = new Mensagem();
        this.arquivos.add(user);
        m.setTexto("Arquivo adicionado com sucesso!");

        out.writeObject(m);
    }

    public void search(Mensagem men) throws IOException {
        ArrayList<Arquivo> envioUsuarios = new ArrayList();
        String texto = men.getTexto();
        for(Arquivo u: this.arquivos){
            if(u.getTitulo().contains(texto) || u.getDescricao().contains(texto)){
                envioUsuarios.add(u);
            }
        }        
        out.writeObject(envioUsuarios);
    }

    @Override
    public void run() {

        try {
            while (true) {
                Object obj = in.readObject();

                if (obj instanceof Arquivo) {
                    upload((Arquivo) obj);
                } else if (obj instanceof Mensagem) {
                    search((Mensagem) obj);
                }

            }
        } catch (EOFException eofe) {
            System.out.println("EOF:" + eofe.getMessage());
        } catch (IOException ioe) {
            System.out.println("IO:" + ioe.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GerenciaMensagemServidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(GerenciaMensagemServidor.class.getName()).log(Level.SEVERE, null, ex);
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
