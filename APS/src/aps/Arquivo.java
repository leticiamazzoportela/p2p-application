package aps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import utils.FileUtils;


public class Arquivo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String titulo;
    private String descricao;
    private String caminho;
    private String ip;
    private int porta = 8050;
    private int portaUDP = 9090;
    private String md5;
    private long tamanho;
    private String nome;
    private String extensao;
    
    public String checksum;

    public Arquivo() {
    }
       
    public Arquivo(String titulo, String descricao, String caminho) throws NoSuchAlgorithmException, UnknownHostException, FileNotFoundException {
        this.ip = InetAddress.getLoopbackAddress().getHostAddress();
        
        this.titulo = titulo;
        this.descricao = descricao;
        this.caminho = caminho;
        
        File file = new File(caminho);
        this.tamanho = file.length();
        this.nome = file.getName();
        this.extensao = FileUtils.getFileExtension(file.getName());
        this.checksum = FileUtils.getFileChecksum(file, "MD5");
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
