from udc_predict import retrieve_response
from scripts.prepare_data import prepare_data
from threading import Thread

def tokenizer_fn(iterator):
    return (x.split(" ") for x in iterator)


def generate_response(s):
    if s == 'prepare data':
        t = Thread(target=prepare_data, args=())
        t.start()
        #prepare_data()
        return 'Preparing data'
    response = retrieve_response(s)
    eou_index = response.find('__eou__')
    if eou_index != -1:
        response = response[:eou_index]
    return response
