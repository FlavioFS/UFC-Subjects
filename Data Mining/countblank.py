file=open("iris-dataset.txt")
text = file.read()
print "Blank: ", text.count(" ")
print "  Tab: ", text.count("\t")
print " Line: ", text.count("\n")
print "-------------------------"
print "Total: ", text.count(" ") + text.count("\t") + text.count("\n")