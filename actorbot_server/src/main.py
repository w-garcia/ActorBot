import cherrypy
import socket


def get_ip_address():
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s.connect(("8.8.8.8", 80))
    return s.getsockname()[0]


class HelloWorld(object):
    def index(self):
        return "Hello World!"

    index.exposed = True


cherrypy.server.socket_host = get_ip_address()
cherrypy.quickstart(HelloWorld())
