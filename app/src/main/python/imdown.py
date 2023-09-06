def main1(url):
    import nltk
    from newspaper import Article
    article = Article(url)
    article.download()
    article.parse()
    nltk.download('punkt')
    img=article.top_image
    return img