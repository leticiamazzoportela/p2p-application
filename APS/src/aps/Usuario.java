/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Elaine
 */
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String titulo;
    private String descricao;
    private String caminho;
    private String ip;
    private int porta = 8050;
    private int portaUDP = 9071;
    private String md5;
    private long tamanho;
    private String nome;
    private String extensao;

    public Usuario() {
    }
       
    public Usuario(String titulo, String descricao, String caminho) throws NoSuchAlgorithmException, UnknownHostException, FileNotFoundException {
        this.titulo = titulo;
        this.descricao = descricao;
        this.caminho = caminho;
        this.ip = InetAddress.getLoopbackAddress().getHostAddress();

        MessageDigest md = MessageDigest.getInstance("MD5");
        String texto = titulo + descricao;
        md.update(texto.getBytes());
        byte[] hashMd5 = md.digest();
        
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < hashMd5.length; i++) {
            int parteAlta = ((hashMd5[i] >> 4) & 0xf) << 4;
            int parteBaixa = hashMd5[i] & 0xf;
            if (parteAlta == 0) s.append('0');
            s.append(Integer.toHexString(parteAlta | parteBaixa));
        }
        this.md5 = s.toString();

        File file = new File(caminho);
        this.nome = file.getName();
        tamanho = file.length();

        int i = this.nome.lastIndexOf('.');
        if (i > 0) {
            this.extensao = this.nome.substring(i + 1);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPorta() {
        return porta;
    }

    public void setPorta(int porta) {
        this.porta = porta;
    }

    public int getPortaUDP() {
        return portaUDP;
    }

    public void setPortaUDP(int portaUDP) {
        this.portaUDP = portaUDP;
    }

    public long getTamanho() {
        return tamanho;
    }

    public void setTamanho(long tamanho) {
        this.tamanho = tamanho;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getExtensao() {
        return extensao;
    }

    public void setExtensao(String extensao) {
        this.extensao = extensao;
    }
    
    

}
