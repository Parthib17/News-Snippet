import spacy
import en_core_web_sm
from spacy.lang.en.stop_words import STOP_WORDS
from string import punctuation
from heapq import nlargest
import nltk
from newspaper import Article
from textblob import TextBlob

def main(url):
    url1=url
    article = Article(url1)
    article.download()
    try:
        article.parse()
    except:
        pass
    nltk.download('punkt')
    rawtext = article.text
    original_len = len(rawtext.split())
    stopwords=list(STOP_WORDS)   #stop words
    #print(stopwords)
    nlp = en_core_web_sm.load()      #small module of spacy
    doc = nlp(rawtext)
    #print(doc)
    tokens = [token.text for token in doc]
    # print(tokens)
    word_freq={}
    for word in doc:
        if word.text.lower() not in stopwords and word.text.lower() not in punctuation:
            if word.text not in word_freq.keys():
                word_freq[word.text]=1
            else:
                word_freq[word.text]+=1

    # print(word_freq)
    try:
        max_freq= max(word_freq.values())
    except:
        max_freq=0

    for word in word_freq.keys():
        word_freq[word]=word_freq[word]/max_freq

    # print(word_freq)

    sen_tokens=[sent for sent in doc.sents]
    # print(sen_tokens)
    sent_scores={}
    for sent in sen_tokens:
        for word in sent:
            if word.text in word_freq.keys():
                if sent not in sent_scores.keys():
                    sent_scores[sent]=word_freq[word.text]
                else:
                    sent_scores[sent]+=word_freq[word.text]

    # print(sent_scores)
    if original_len >1199:
        select_len =int(len(sen_tokens)*0.05)
    elif original_len<1200 and original_len>799:
        select_len =int(len(sen_tokens)*0.08)
    elif original_len<800 and original_len>499:
        select_len =int(len(sen_tokens)*0.15)
    elif original_len<500 and original_len>199:
        select_len =int(len(sen_tokens)*0.30)
    elif original_len <200:
        select_len =int(len(sen_tokens)*0.50)
    else:
        select_len =int(len(sen_tokens)*0.20)

    summary = nlargest(select_len, sent_scores, key=sent_scores.get)
    final_summary = [word.text for word in summary]
    summary =' '.join(final_summary)
    summary10=summary.replace('\n',' ')
    summary11=summary10.replace('ADVERTISEMENT',' ')
    summary111=summary11.replace('advertisement',' ')
    summary1=summary111.replace('Advertisement',' ')
    blob=TextBlob(summary1)
    polarity = blob.sentiment.polarity
    if polarity> 0.02 and polarity !=0:
        senti = "Positive"
    elif polarity< 0.03 and polarity !=0:
        senti = "Negative"
    else:
        senti = "Neutral"

    return summary1+"999"+senti



