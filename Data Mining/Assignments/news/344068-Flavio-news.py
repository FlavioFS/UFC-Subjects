# =================================================================================================
#     Text Preprocessing
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
    # currentText = re.split("\W+", rawText[textPointer : textEnd])
    currentText = rawText[textPointer : textEnd].split()
    currentText = filter(lambda a: (a != '-') and (a != ';') and (a!= '.') and (a!='|'), currentText)

    # Stores the results
    if currentText != []:
        categories.append(currentCategory)
        texts.append((currentText))

    # Prepares for next cycle
    textPointer = rawText.find("category=", textPointer)

# Debug
# for element in texts[:83]:
#     print element


# =================================================================================================
#     Applying Language Processors
# =================================================================================================
CATEGORY_COLUMN = 0
TEXT_COLUMN = 1

import nltk

test = ["A", "B", "C", "D"]

# Not Found corpus.stopwords!!
# ptWords = nltk.corpus.stopwords.words('portuguese')
# print ptWords[:10]

# Removing stop words
# for stopword in nltk.corpus.stopwords.words('portuguese'):
#     for index in xrange(len(texts)):
#         if texts[index].find(stopword) != NOT_FOUND:
#             texts.remove(index)
#
# for element in texts[:20]:
#     print element