## Attempts to generate output folder
echo ""
echo "Attempting to create output folder:"
echo "    graphs/"
echo ""
mkdir -p "graphs/"

## User feedback
echo "Generating outputs..."
echo -en "Completion: 0%"

## Stats only ----------------------------------------------------------------------------------------------
# Small
echo "# <Processing>, <CPU>, <Throughput>, <Turn>, <Wait>, <Ans>, <Swaps>, <No. of Process>" > "graphs/small.csv"
java -classpath class escalonador "data/comparison/small.csv" 3 FCFS      >> "graphs/small.csv"
java -classpath class escalonador "data/comparison/small.csv" 3 SJF       >> "graphs/small.csv"
java -classpath class escalonador "data/comparison/small.csv" 3 SJFP      >> "graphs/small.csv"
java -classpath class escalonador "data/comparison/small.csv" 3 RR 20     >> "graphs/small.csv"
java -classpath class escalonador "data/comparison/small.csv" 3 Priority  >> "graphs/small.csv"
java -classpath class escalonador "data/comparison/small.csv" 3 PriorityP >> "graphs/small.csv"

## User feedback
echo -en "\e[0K\rCompletion: 33%"

# Large
echo "# <Processing>, <CPU>, <Throughput>, <Turn>, <Wait>, <Ans>, <Swaps>, <No. of Process>" > "graphs/large.csv"
java -classpath class escalonador "data/comparison/long.csv" 3 FCFS      >> "graphs/large.csv"
java -classpath class escalonador "data/comparison/long.csv" 3 SJF       >> "graphs/large.csv"
java -classpath class escalonador "data/comparison/long.csv" 3 SJFP      >> "graphs/large.csv"
java -classpath class escalonador "data/comparison/long.csv" 3 RR 20     >> "graphs/large.csv"
java -classpath class escalonador "data/comparison/long.csv" 3 Priority  >> "graphs/large.csv"
java -classpath class escalonador "data/comparison/long.csv" 3 PriorityP >> "graphs/large.csv"

## User feedback
echo -en "\e[0K\rCompletion: 66%"

# Mixed
echo "# <Processing>, <CPU>, <Throughput>, <Turn>, <Wait>, <Ans>, <Swaps>, <No. of Process>" > "graphs/mixed.csv"
java -classpath class escalonador "data/comparison/mixed.csv" 3 FCFS      >> "graphs/mixed.csv"
java -classpath class escalonador "data/comparison/mixed.csv" 3 SJF       >> "graphs/mixed.csv"
java -classpath class escalonador "data/comparison/mixed.csv" 3 SJFP      >> "graphs/mixed.csv"
java -classpath class escalonador "data/comparison/mixed.csv" 3 RR 20     >> "graphs/mixed.csv"
java -classpath class escalonador "data/comparison/mixed.csv" 3 Priority  >> "graphs/mixed.csv"
java -classpath class escalonador "data/comparison/mixed.csv" 3 PriorityP >> "graphs/mixed.csv"


## User feedback
echo -e "\e[0K\rCompletion: 100%"
echo "Done! Please check the folder 'graphs/'"
echo ""