import socket
import sys
import time
import threading

class Server(threading.Thread):
    def run(self):
        self.sock = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
        self.sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        print("Servidor iniciado\n")

        hostname = ''
        port = 51412
        self.sock.bind((hostname,port))
        self.sock.listen(1)
        print("Servidor rodando na porta %d\n" %port)

        (clientname,address)=self.sock.accept()
        print("Conectado em %s\n" % str(address)) 
        while 1:
            chunk = clientname.recv(4096)            
            print(address, ':', chunk)

class Client(threading.Thread):    
    def connect(self,host,port):
        self.sock.connect((host,port))

    def client(self,host,port,msg):               
        sent = self.sock.send(format(msg).encode('utf-8'))           
        print("Enviado\n")

    def run(self):
        self.sock = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
        try:
            host = input("Informe o nome do host\n>>")            
            port = int(input("Informe a porta\n>>"))
        except EOFError:
            print("Erro")
            return 1
        
        print("Conectando\n")
        self.connect(host,port)
        print("Conectado\n")

        while 1:            
            print("Digite sua mensagem\n")
            msg = input('>>')
            if msg == 'exit':
                break
            if msg == '':
                continue
            print("Enviando\n")
            self.client(host,port,msg)
        return(1)
        
if __name__=='__main__':
    srv = Server()
    srv.daemon = True
    print("Iniciando servidor")
    srv.start()
    time.sleep(1)

    print("Iniciando cliente")
    cli = Client()
    print("Cliente conectado com sucesso")
    cli.start()