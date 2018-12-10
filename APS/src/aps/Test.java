/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aps;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *
 * @author Elaine
 */
public class Test {
    public static void main(String[] args) throws SocketException, UnknownHostException {
        int tamanho = 4000;
        int pedaco = 1024;
        
        int partes = (int) Math.ceil(tamanho / pedaco);
        int sobra = tamanho % pedaco;
        System.out.println(partes);
        System.out.println(sobra);
    }
}
