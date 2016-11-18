# =================================================================================================
#     Text Parsing and Tokenizing
# =================================================================================================
import re

NOT_FOUND = -1

# This will store categories in the first column and news in the second column
categories = []
texts = []

# Reads the file all at once
newsFile = open("news_data.xml", 'r')
rawText = newsFile.read()
newsFile.close()

# Keyword length
CATEGORY_LENGTH = len('category="')
TEXT_LENGTH = len("<text>")

# A cursor to read through the file
textPointer = 0                                         # Starts at the first character of file
textPointer = rawText.find("category=", textPointer)    # Searches for the next category

# Iterates through the text searching for categories and non-empty texts
# Stores everything at the variable "data"
while textPointer != NOT_FOUND:
    # Caches the new category
    textPointer += CATEGORY_LENGTH
    categoryEnd = rawText.find('"', textPointer)
    currentCategory = rawText[textPointer : categoryEnd]

    # Searches for the text
    textPointer = rawText.find("<text>", categoryEnd) + TEXT_LENGTH
    textEnd = rawText.find("</text>", textPointer)
    currentText = rawText[textPointer: textEnd].lower().decode("utf-8")

    ## Tokenizes
    # Removes punctuation
    currentText = re.sub('[\)\(@#$|,!.:;?]', ' ', currentText)

    # Converts to list and filters some noise
    currentText = currentText.split()
    if currentText != []:
        currentText = filter(lambda a: (a != '-'), currentText) # Filters some noise

        # Stores results
        categories.append(currentCategory)
        texts.append(currentText)

    # Prepares for next cycle
    textPointer = rawText.find("category=", textPointer)




# =================================================================================================
#     Stop Word Removal and Stemming
# =================================================================================================
import nltk

# N_TOTAL = int(0.2 * len(texts))
N_TOTAL = len(texts)

# Stop Words
for stopword in nltk.corpus.stopwords.words('portuguese'):
    for i in xrange(N_TOTAL):
        texts[i] = filter(lambda a: (a.lower() != stopword.lower()), texts[i])

# Stemming
PTsTemmer = nltk.stem.RSLPStemmer()
for i in xrange(N_TOTAL):
    for j in xrange(len(texts[i])):
        texts[i][j] = PTsTemmer.stem(texts[i][j])


# =================================================================================================
#     Defining Features
# =================================================================================================
CATEGORY_COLUMN = 0
TEXT_COLUMN = 1


WORD_COLUMN = 0
FREQUENCY_COLUMN = 1
FEATURE_COUNT = 2

N_FIT = int(0.5 * N_TOTAL)

def getFeatures (text):
    fd = nltk.FreqDist(word for word in text)

    # Most frequent words
    wordFreq = []
    for word in list(fd.keys()[:20]):
        wordFreq.append([word, fd[word]])

    # Sorts by frequency (highest frequency first)
    wordFreq = sorted(wordFreq, key=lambda tuple: -tuple[FREQUENCY_COLUMN])[:FEATURE_COUNT]

    features = {}
    for i in xrange(min(FEATURE_COUNT, len(wordFreq))):
        features['w%d' % i] = wordFreq[i][WORD_COLUMN]
        features['f%d' % i] = wordFreq[i][FREQUENCY_COLUMN]

    return features

# Set Features
featureSets= [(getFeatures(text), category) for (text, category) in zip(texts, categories)]

trainSet, testSet = featureSets[:N_FIT], featureSets[N_FIT:]

naiveBayes = nltk.NaiveBayesClassifier.train(trainSet)
print nltk.classify.accuracy(naiveBayes, testSet)

maxEntropy = nltk.MaxentClassifier.train(trainSet)
print nltk.classify.accuracy(maxEntropy, testSet)