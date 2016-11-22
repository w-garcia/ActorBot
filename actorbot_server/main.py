import cherrypy
import socket
import string
from response_gen import generate_response


class bcolors:
    HEADER = '\033[95m'
    OKBLUE = '\033[94m'
    OKGREEN = '\033[92m'
    WARNING = '\033[93m'
    FAIL = '\033[91m'
    ENDC = '\033[0m'
    BOLD = '\033[1m'
    UNDERLINE = '\033[4m'


def get_ip_address():
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s.connect(("8.8.8.8", 80))
    return s.getsockname()[0]


class ActorBot_Server(object):
    def generate(self, phrase):
        print(bcolors.WARNING + "Generating response to {}.".format(phrase) + bcolors.OKBLUE)
        return generate_response(phrase)

    def index(self):
        return "test"

    index.exposed = True
    generate.exposed = True


cherrypy.server.socket_host = get_ip_address()
cherrypy.quickstart(ActorBot_Server())