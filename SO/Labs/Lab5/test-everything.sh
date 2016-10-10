## Attempts to generate output folder
mkdir -p out/



## Short processes only
# Stats
java -classpath class escalonador "../data/comparison/short.csv" 1 FCFS      > "out/short-stats-fcfs.txt"
java -classpath class escalonador "../data/comparison/short.csv" 1 SJF       > "out/short-stats-sjf.txt"
java -classpath class escalonador "../data/comparison/short.csv" 1 SJFP      > "out/short-stats-sjfp.txt"
java -classpath class escalonador "../data/comparison/short.csv" 1 RR 20     > "out/short-stats-rr.txt"
java -classpath class escalonador "../data/comparison/short.csv" 1 Priority  > "out/short-stats-priority.txt"
java -classpath class escalonador "../data/comparison/short.csv" 1 PriorityP > "out/short-stats-priorityp.txt"

# List
java -classpath class escalonador "../data/comparison/short.csv" 2 FCFS      > "out/short-list-fcfs.txt"
java -classpath class escalonador "../data/comparison/short.csv" 2 SJF       > "out/short-list-sjf.txt"
java -classpath class escalonador "../data/comparison/short.csv" 2 SJFP      > "out/short-list-sjfp.txt"
java -classpath class escalonador "../data/comparison/short.csv" 2 RR 20     > "out/short-list-rr.txt"
java -classpath class escalonador "../data/comparison/short.csv" 2 Priority  > "out/short-list-priority.txt"
java -classpath class escalonador "../data/comparison/short.csv" 2 PriorityP > "out/short-list-priorityp.txt"



## Long processes only
# Stats
java -classpath class escalonador "../data/comparison/long.csv" 1 FCFS      > "out/long-stats-fcfs.txt"
java -classpath class escalonador "../data/comparison/long.csv" 1 SJF       > "out/long-stats-sjf.txt"
java -classpath class escalonador "../data/comparison/long.csv" 1 SJFP      > "out/long-stats-sjfp.txt"
java -classpath class escalonador "../data/comparison/long.csv" 1 RR 20     > "out/long-stats-rr.txt"
java -classpath class escalonador "../data/comparison/long.csv" 1 Priority  > "out/long-stats-priority.txt"
java -classpath class escalonador "../data/comparison/long.csv" 1 PriorityP > "out/long-stats-priorityp.txt"

# List
java -classpath class escalonador "../data/comparison/long.csv" 2 FCFS      > "out/long-list-fcfs.txt"
java -classpath class escalonador "../data/comparison/long.csv" 2 SJF       > "out/long-list-sjf.txt"
java -classpath class escalonador "../data/comparison/long.csv" 2 SJFP      > "out/long-list-sjfp.txt"
java -classpath class escalonador "../data/comparison/long.csv" 2 RR 20     > "out/long-list-rr.txt"
java -classpath class escalonador "../data/comparison/long.csv" 2 Priority  > "out/long-list-priority.txt"
java -classpath class escalonador "../data/comparison/long.csv" 2 PriorityP > "out/long-list-priorityp.txt"



## Mixes processes
# Stats
java -classpath class escalonador "../data/comparison/mixed.csv" 1 FCFS      > "out/mixed-stats-fcfs.txt"
java -classpath class escalonador "../data/comparison/mixed.csv" 1 SJF       > "out/mixed-stats-sjf.txt"
java -classpath class escalonador "../data/comparison/mixed.csv" 1 SJFP      > "out/mixed-stats-sjfp.txt"
java -classpath class escalonador "../data/comparison/mixed.csv" 1 RR 20     > "out/mixed-stats-rr.txt"
java -classpath class escalonador "../data/comparison/mixed.csv" 1 Priority  > "out/mixed-stats-priority.txt"
java -classpath class escalonador "../data/comparison/mixed.csv" 1 PriorityP > "out/mixed-stats-priorityp.txt"

# List
java -classpath class escalonador "../data/comparison/mixed.csv" 2 FCFS      > "out/mixed-list-fcfs.txt"
java -classpath class escalonador "../data/comparison/mixed.csv" 2 SJF       > "out/mixed-list-sjf.txt"
java -classpath class escalonador "../data/comparison/mixed.csv" 2 SJFP      > "out/mixed-list-sjfp.txt"
java -classpath class escalonador "../data/comparison/mixed.csv" 2 RR 20     > "out/mixed-list-rr.txt"
java -classpath class escalonador "../data/comparison/mixed.csv" 2 Priority  > "out/mixed-list-priority.txt"
java -classpath class escalonador "../data/comparison/mixed.csv" 2 PriorityP > "out/mixed-list-priorityp.txt"