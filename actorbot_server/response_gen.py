from chatbot_retrieval.udc_predict import retrieve_response


def tokenizer_fn(iterator):
    return (x.split(" ") for x in iterator)

def generate_response(s):
    #prepare_data()
    return retrieve_response(s)
