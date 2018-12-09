import sys
import hashlib

class Usuario:
    id = 0
    titulo = ''
    descricao = ''
    caminho = ''
    ip = ''
    porta = '8050'
    md5 = ''

    def __init__(self, id, titulo, desc, caminho, ip):
        self.id = id
        self.titulo = titulo
        self.descricao = desc
        self.caminho = caminho
        self.ip = ip

        h = hashlib.md5()
        h.update(titulo + desc)
        self.md5 = h.hexdigest()
        
