import socket
import sys
import time
import threading
import Usuario

class Server(threading.Thread):
    def run(self):
        self.sock = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
        print("Servidor iniciado...\n")

        hostname = ''
        port = 51412
        self.sock.bind((hostname,port))
        self.sock.listen(1)
        print("Servidor rodando na porta %d\n" %port)

        (clientname,address) = self.sock.accept()
        print("Conectado em %s\n" % str(address)) 
        while 1:
            chunk = clientname.recv(4096)            
            print(address, ':', chunk)
            

if __name__=='__main__':
    srv = Server()
    srv.daemon = True
    print("Iniciando servidor")
    srv.start()
    time.sleep(1)
