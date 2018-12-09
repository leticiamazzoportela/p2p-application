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
class GerenciaMensagemServidor extends Thread {

    ObjectOutputStream out;
    ObjectInputStream in;
    ArrayList<Usuario> arquivos;

    Socket socket;

    public GerenciaMensagemServidor(Socket socket, ArrayList<Usuario> arquivos) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.arquivos = arquivos;
    }

    public void upload(Usuario user) throws NoSuchAlgorithmException, UnknownHostException, IOException {
        int tam = this.arquivos.size();
        user.setId(tam);
        Mensagem m = new Mensagem();
        this.arquivos.add(user);
        m.setTexto("Arquivo adicionado com sucesso!");

        out.writeObject(m);
    }

    public void search(Mensagem men) {

    }

    @Override
    public void run() {

        try {
            while (true) {
                Object obj = in.readObject();

                if (obj instanceof Usuario) {
                    upload((Usuario) obj);
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
