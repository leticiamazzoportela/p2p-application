/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aps;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *
 * @author Elaine
 */
public class Test {
    public static void main(String[] args) throws SocketException, UnknownHostException, FileNotFoundException, IOException {
//        int tamanho = 4000;
//        int pedaco = 1024;
//        
//        int partes = (int) Math.ceil(tamanho / pedaco);
//        int sobra = tamanho % pedaco;
//        System.out.println(partes);
//        System.out.println(sobra);

        File file = new File("C:\\Users\\Elaine\\Documents\\GitHub\\p2p-application\\arquivosEnvio\\cachorros.txt");
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos;
        
        BufferedWriter bw;
        
        
        byte[] buffer = new byte[55];
        fis.skip(27);
        System.out.println(fis.read(buffer, 0, 27));
        for (byte b : buffer) {
            System.out.print((char) b);
        }
        // System.out.println("buffer: " + new String(buffer));
        
        fis.close();
    }
}
