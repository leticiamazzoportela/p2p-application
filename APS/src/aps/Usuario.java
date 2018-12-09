/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aps;

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
    private String porta = "8050";
    private byte[] md5;

    public Usuario(String titulo, String descricao, String caminho) throws NoSuchAlgorithmException, UnknownHostException {
        this.titulo = titulo;
        this.descricao = descricao;
        this.caminho = caminho;
        this.ip = InetAddress.getLocalHost().getHostAddress();

        MessageDigest md = MessageDigest.getInstance("MD5");
        String texto = titulo + descricao;
        md.update(texto.getBytes());
        byte[] hashMd5 = md.digest();
        this.md5 = hashMd5;        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getMd5() {
        return md5;
    }

    public void setMd5(byte[] md5) {
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

    public String getPorta() {
        return porta;
    }

    public void setPorta(String porta) {
        this.porta = porta;
    }


}
