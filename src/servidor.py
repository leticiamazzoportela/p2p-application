import socket
import sys
import time
import threading
import Usuario

class Server(threading.Thread):

    listaArquivos = []

    def run(self):
        self.sock = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
        print("Servidor iniciado...\n")

        hostname = ''
        port = 51412
        self.sock.bind((hostname,port))
        self.sock.listen(1)
        print("Servidor rodando na porta %d\n" %port)

        (clientname, address) = self.sock.accept()
        print("Conectado em %s\n" % str(address)) 
        while 1:
            data = clientname.recv(4096)     
            msg = data.decode()
            if isinstance(msg, Usuario):
                uploadArquivo(msg)
                      
    def uploadArquivo(user):
        Usuario u = (Usuario) user
        tamanhoLista = len(listaArquivos)
        




if __name__=='__main__':
    srv = Server()
    srv.daemon = True
    print("Iniciando servidor")
    srv.start()
    time.sleep(1)
