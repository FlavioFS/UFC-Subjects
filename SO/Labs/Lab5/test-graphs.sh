## Attempts to generate output folder
echo ""
echo "Attempting to create output folder:"
echo "    debug/"
echo ""
mkdir -p "debug/"

## User feedback
echo "Generating outputs..."
echo -en "Completion: 0%"

## Stats only ----------------------------------------------------------------------------------------------
# Small
echo "# <Processing>, <CPU>, <Throughput>, <Turn>, <Wait>, <Ans>, <Swaps>, <No. of Process>" > "debug/small.csv"
java -classpath class escalonador "data/comparison/small.csv" 3 FCFS      >> "debug/small.csv"
java -classpath class escalonador "data/comparison/small.csv" 3 SJF       >> "debug/small.csv"
java -classpath class escalonador "data/comparison/small.csv" 3 SJFP      >> "debug/small.csv"
java -classpath class escalonador "data/comparison/small.csv" 3 RR 20     >> "debug/small.csv"
java -classpath class escalonador "data/comparison/small.csv" 3 Priority  >> "debug/small.csv"
java -classpath class escalonador "data/comparison/small.csv" 3 PriorityP >> "debug/small.csv"

## User feedback
echo -en "\e[0K\rCompletion: 33%"

# Large
echo "# <Processing>, <CPU>, <Throughput>, <Turn>, <Wait>, <Ans>, <Swaps>, <No. of Process>" > "debug/large.csv"
java -classpath class escalonador "data/comparison/long.csv" 3 FCFS      >> "debug/large.csv"
java -classpath class escalonador "data/comparison/long.csv" 3 SJF       >> "debug/large.csv"
java -classpath class escalonador "data/comparison/long.csv" 3 SJFP      >> "debug/large.csv"
java -classpath class escalonador "data/comparison/long.csv" 3 RR 20     >> "debug/large.csv"
java -classpath class escalonador "data/comparison/long.csv" 3 Priority  >> "debug/large.csv"
java -classpath class escalonador "data/comparison/long.csv" 3 PriorityP >> "debug/large.csv"

## User feedback
echo -en "\e[0K\rCompletion: 66%"

# Mixed
echo "# <Processing>, <CPU>, <Throughput>, <Turn>, <Wait>, <Ans>, <Swaps>, <No. of Process>" > "debug/mixed.csv"
java -classpath class escalonador "data/comparison/mixed.csv" 3 FCFS      >> "debug/mixed.csv"
java -classpath class escalonador "data/comparison/mixed.csv" 3 SJF       >> "debug/mixed.csv"
java -classpath class escalonador "data/comparison/mixed.csv" 3 SJFP      >> "debug/mixed.csv"
java -classpath class escalonador "data/comparison/mixed.csv" 3 RR 20     >> "debug/mixed.csv"
java -classpath class escalonador "data/comparison/mixed.csv" 3 Priority  >> "debug/mixed.csv"
java -classpath class escalonador "data/comparison/mixed.csv" 3 PriorityP >> "debug/mixed.csv"


## User feedback
echo -e "\e[0K\rCompletion: 100%"
echo "Done! Please check the folder 'debug/'"
echo ""