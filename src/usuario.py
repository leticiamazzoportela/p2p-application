import sys
import hashlib

class Usuario:
    PORTA = 8050

    def _init_(self):
        self.id = id
        self.titulo = ''
        self.desc = ''
        self.caminho = ''
        self.ip = ''
        self.porta = 0

        h = hashlib.md5()
        h.update(self.titulo + self.desc)
        self.md5 = h.hexdigest()
