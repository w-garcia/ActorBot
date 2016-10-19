import cherrypy
import socket
import string



def get_ip_address():
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s.connect(("8.8.8.8", 80))
    return s.getsockname()[0]


class ActorBot_Server(object):
    def generate(self, b):
        return b + "c"

    def index(self):
        return "testicles"

    index.exposed = True
    generate.exposed = True


cherrypy.server.socket_host = get_ip_address()
cherrypy.quickstart(ActorBot_Server())