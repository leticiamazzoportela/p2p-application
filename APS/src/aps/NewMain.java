/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aps;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author Elaine
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
//        RandomAccessFile fos = new RandomAccessFile("C:\\Users\\Elaine\\Documents\\GitHub\\p2p-application\\arquivosRecebidos\\teste.txt", "rw");
//        String teste = "cachorro";
//        fos.skipBytes(4);
//        fos.write(teste.getBytes());
//        fos.close();

        FileInputStream fis = new FileInputStream("C:\\\\Users\\\\Elaine\\\\Documents\\\\GitHub\\\\p2p-application\\\\arquivosRecebidos\\\\teste.txt");
        byte[] buffer = new byte[4];
        fis.read(buffer);
        System.out.println(new String(buffer));
    }
    
}
