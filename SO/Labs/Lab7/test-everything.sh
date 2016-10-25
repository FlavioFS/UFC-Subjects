java -classpath class Safety "inputs/system-state1.csv"
java -classpath class Safety "inputs/system-state2.csv"
java -classpath class Safety "inputs/system-state3.csv"

java -classpath class Avoid "inputs/system-state1.csv" "inputs/request1.csv"
java -classpath class Avoid "inputs/system-state2.csv" "inputs/request1.csv"
java -classpath class Avoid "inputs/system-state3.csv" "inputs/request1.csv"