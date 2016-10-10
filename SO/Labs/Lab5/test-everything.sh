## Attempts to generate output folder
echo ""
echo "Attempting to create output folders:"
echo "    out/"
echo "    out/all the thingies/"
echo "    out/stats only/"
echo ""
mkdir -p "out/"
mkdir -p "out/all the thingies/"
mkdir -p "out/stats only/"

## User feedback
echo "Generating outputs..."
echo -en "Completion: 0%"


## Short processes only -----------------------------------------------------------------------------------------
# Stats
java -classpath class escalonador "data/comparison/short.csv" 1 FCFS      > "out/all the thingies/1.1.1. Sml stats FCFS.txt"
java -classpath class escalonador "data/comparison/short.csv" 1 SJF       > "out/all the thingies/1.1.2. Sml stats SJF.txt"
java -classpath class escalonador "data/comparison/short.csv" 1 SJFP      > "out/all the thingies/1.1.3. Sml stats SJFP.txt"
java -classpath class escalonador "data/comparison/short.csv" 1 RR 20     > "out/all the thingies/1.1.4. Sml stats RR.txt"
java -classpath class escalonador "data/comparison/short.csv" 1 Priority  > "out/all the thingies/1.1.5. Sml stats Priority.txt"
java -classpath class escalonador "data/comparison/short.csv" 1 PriorityP > "out/all the thingies/1.1.6. Sml stats PriorityP.txt"

## User feedback
echo -en "\e[0K\rCompletion: 11%"

# List
java -classpath class escalonador "data/comparison/short.csv" 2 FCFS      > "out/all the thingies/1.2.1. Sml list FCFS.txt"
java -classpath class escalonador "data/comparison/short.csv" 2 SJF       > "out/all the thingies/1.2.2. Sml list SJF.txt"
java -classpath class escalonador "data/comparison/short.csv" 2 SJFP      > "out/all the thingies/1.2.3. Sml list SJFP.txt"
java -classpath class escalonador "data/comparison/short.csv" 2 RR 20     > "out/all the thingies/1.2.4. Sml list RR.txt"
java -classpath class escalonador "data/comparison/short.csv" 2 Priority  > "out/all the thingies/1.2.5. Sml list Priority.txt"
java -classpath class escalonador "data/comparison/short.csv" 2 PriorityP > "out/all the thingies/1.2.6. Sml list PriorityP.txt"


## User feedback
echo -en "\e[0K\rCompletion: 22%"


## Long processes only ------------------------------------------------------------------------------------------
# Stats
java -classpath class escalonador "data/comparison/long.csv" 1 FCFS      > "out/all the thingies/2.1.1. Lrg stats FCFS.txt"
java -classpath class escalonador "data/comparison/long.csv" 1 SJF       > "out/all the thingies/2.1.2. Lrg stats SJF.txt"
java -classpath class escalonador "data/comparison/long.csv" 1 SJFP      > "out/all the thingies/2.1.3. Lrg stats SJFP.txt"
java -classpath class escalonador "data/comparison/long.csv" 1 RR 20     > "out/all the thingies/2.1.4. Lrg stats RR.txt"
java -classpath class escalonador "data/comparison/long.csv" 1 Priority  > "out/all the thingies/2.1.5. Lrg stats Priority.txt"
java -classpath class escalonador "data/comparison/long.csv" 1 PriorityP > "out/all the thingies/2.1.6. Lrg stats PriorityP.txt"

## User feedback
echo -en "\e[0K\rCompletion: 33%"

# List
java -classpath class escalonador "data/comparison/long.csv" 2 FCFS      > "out/all the thingies/2.2.1. Lrg list FCFS.txt"
java -classpath class escalonador "data/comparison/long.csv" 2 SJF       > "out/all the thingies/2.2.2. Lrg list SJF.txt"
java -classpath class escalonador "data/comparison/long.csv" 2 SJFP      > "out/all the thingies/2.2.3. Lrg list SJFP.txt"
java -classpath class escalonador "data/comparison/long.csv" 2 RR 20     > "out/all the thingies/2.2.4. Lrg list RR.txt"
java -classpath class escalonador "data/comparison/long.csv" 2 Priority  > "out/all the thingies/2.2.5. Lrg list Priority.txt"
java -classpath class escalonador "data/comparison/long.csv" 2 PriorityP > "out/all the thingies/2.2.6. Lrg list PriorityP.txt"


## User feedback
echo -en "\e[0K\rCompletion: 44%"


## Mixed processes ----------------------------------------------------------------------------------------------
# Stats
java -classpath class escalonador "data/comparison/mixed.csv" 1 FCFS      > "out/all the thingies/3.1.1. Mix stats FCFS.txt"
java -classpath class escalonador "data/comparison/mixed.csv" 1 SJF       > "out/all the thingies/3.1.2. Mix stats SJF.txt"
java -classpath class escalonador "data/comparison/mixed.csv" 1 SJFP      > "out/all the thingies/3.1.3. Mix stats SJFP.txt"
java -classpath class escalonador "data/comparison/mixed.csv" 1 RR 20     > "out/all the thingies/3.1.4. Mix stats RR.txt"
java -classpath class escalonador "data/comparison/mixed.csv" 1 Priority  > "out/all the thingies/3.1.5. Mix stats Priority.txt"
java -classpath class escalonador "data/comparison/mixed.csv" 1 PriorityP > "out/all the thingies/3.1.6. Mix stats PriorityP.txt"


## User feedback
echo -en "\e[0K\rCompletion: 55%"


# List
java -classpath class escalonador "data/comparison/mixed.csv" 2 FCFS      > "out/all the thingies/3.2.1. Mix list FCFS.txt"
java -classpath class escalonador "data/comparison/mixed.csv" 2 SJF       > "out/all the thingies/3.2.2. Mix list SJF.txt"
java -classpath class escalonador "data/comparison/mixed.csv" 2 SJFP      > "out/all the thingies/3.2.3. Mix list SJFP.txt"
java -classpath class escalonador "data/comparison/mixed.csv" 2 RR 20     > "out/all the thingies/3.2.4. Mix list RR.txt"
java -classpath class escalonador "data/comparison/mixed.csv" 2 Priority  > "out/all the thingies/3.2.5. Mix list Priority.txt"
java -classpath class escalonador "data/comparison/mixed.csv" 2 PriorityP > "out/all the thingies/3.2.6. Mix list PriorityP.txt"


## Stats only ----------------------------------------------------------------------------------------------
## User feedback
echo -en "\e[0K\rCompletion: 66%"

# Small
java -classpath class escalonador "data/comparison/short.csv" 1 FCFS      >> "out/stats only/Sml stats.txt"
java -classpath class escalonador "data/comparison/short.csv" 1 SJF       >> "out/stats only/Sml stats.txt"
java -classpath class escalonador "data/comparison/short.csv" 1 SJFP      >> "out/stats only/Sml stats.txt"
java -classpath class escalonador "data/comparison/short.csv" 1 RR 20     >> "out/stats only/Sml stats.txt"
java -classpath class escalonador "data/comparison/short.csv" 1 Priority  >> "out/stats only/Sml stats.txt"
java -classpath class escalonador "data/comparison/short.csv" 1 PriorityP >> "out/stats only/Sml stats.txt"

## User feedback
echo -en "\e[0K\rCompletion: 77%"

# Large
java -classpath class escalonador "data/comparison/long.csv" 1 FCFS      >> "out/stats only/Lrg stats.txt"
java -classpath class escalonador "data/comparison/long.csv" 1 SJF       >> "out/stats only/Lrg stats.txt"
java -classpath class escalonador "data/comparison/long.csv" 1 SJFP      >> "out/stats only/Lrg stats.txt"
java -classpath class escalonador "data/comparison/long.csv" 1 RR 20     >> "out/stats only/Lrg stats.txt"
java -classpath class escalonador "data/comparison/long.csv" 1 Priority  >> "out/stats only/Lrg stats.txt"
java -classpath class escalonador "data/comparison/long.csv" 1 PriorityP >> "out/stats only/Lrg stats.txt"

## User feedback
echo -en "\e[0K\rCompletion: 88%"

# Mixed
java -classpath class escalonador "data/comparison/mixed.csv" 1 FCFS      >> "out/stats only/Mix stats.txt"
java -classpath class escalonador "data/comparison/mixed.csv" 1 SJF       >> "out/stats only/Mix stats.txt"
java -classpath class escalonador "data/comparison/mixed.csv" 1 SJFP      >> "out/stats only/Mix stats.txt"
java -classpath class escalonador "data/comparison/mixed.csv" 1 RR 20     >> "out/stats only/Mix stats.txt"
java -classpath class escalonador "data/comparison/mixed.csv" 1 Priority  >> "out/stats only/Mix stats.txt"
java -classpath class escalonador "data/comparison/mixed.csv" 1 PriorityP >> "out/stats only/Mix stats.txt"


## User feedback
echo -e "\e[0K\rCompletion: 100%"
echo "Done! Please check the folder 'out/'"
echo ""