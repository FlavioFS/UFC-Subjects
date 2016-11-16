# =================================================================================================
#     Text Preprocessing
# =================================================================================================

# This will store categories in the first column and news in the second column
data = []

# Reads the file all at once
newsFile = open("news_data.xml")
rawText = newsFile.read()

# Keyword length
CATEGORY_LENGTH = len('category="')
TEXT_LENGTH = len("<text>")

# A cursor to read through the file
textPointer = 0                                         # Starts at the first character of file
textPointer = rawText.find("category=", textPointer)    # Searches for the next category

# Iterates through the text searching for categories and non-empty texts
# Stores everything at the variable "data"
while textPointer > -1:
    # Caches the new category
    textPointer += CATEGORY_LENGTH
    categoryEnd = rawText.find('"', textPointer)
    currentCategory = rawText[textPointer : categoryEnd]

    # Searches for the text
    textPointer = rawText.find("<text>", categoryEnd) + TEXT_LENGTH
    textEnd = rawText.find("</text>", textPointer)
    currentText = rawText[textPointer : textEnd]

    # Stores the results
    if currentText != "":
        data.append([currentCategory, currentText])

    # Prepares for next cycle
    textPointer = rawText.find("category=", textPointer)

# Debug
# for element in data:
#     print element


# =================================================================================================
#     Applying Language Processors
# =================================================================================================
