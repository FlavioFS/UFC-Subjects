########################################################
#	Testing all combinations
########################################################

# All Combinations with Safety
echo    "╔═════════════════════════════════════════╗";
echo    "║                 Safety                  ║";
echo    "╚═════════════════════════════════════════╝"
echo    "State File ┃ Result";
echo    "━━━━━━━━━━━╇━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━";
echo -n "Safe       │ "; java -classpath class Safety "inputs/1.1. state-safe.csv"; echo "";
echo -n "Unsafe     │ "; java -classpath class Safety "inputs/1.2. state-unsafe.csv"; echo "";
echo "";

# All Combinations with Avoid
echo    "╔═════════════════════════════════════════╗";
echo    "║                 Avoid                   ║";
echo    "╚═════════════════════════════════════════╝"
echo    "State File ┃ Request File ┃ Result";
echo    "━━━━━━━━━━━╇━━━━━━━━━━━━━━╇━━━━━━━━━━━━━━━━";
echo -n "Safe       │ OK           │ "; java -classpath class Avoid "inputs/1.1. state-safe.csv" "inputs/2.1. request-ok.csv"; echo "";
echo -n "Safe       │ Wait         │ "; java -classpath class Avoid "inputs/1.1. state-safe.csv" "inputs/2.2. request-wait.csv"; echo "";
echo -n "Safe       │ Exceed       │ "; java -classpath class Avoid "inputs/1.1. state-safe.csv" "inputs/2.3. request-exceed.csv"; echo "";
echo -n "Safe       │ Denied       │ "; java -classpath class Avoid "inputs/1.1. state-safe.csv" "inputs/2.4. request-denied.csv"; echo "";
echo    "───────────┼──────────────┼────────────────";
echo -n "Unsafe     │ OK           │ "; java -classpath class Avoid "inputs/1.2. state-unsafe.csv" "inputs/2.1. request-ok.csv"; echo "";
echo -n "Unsafe     │ Wait         │ "; java -classpath class Avoid "inputs/1.2. state-unsafe.csv" "inputs/2.2. request-wait.csv"; echo "";
echo -n "Unsafe     │ Exceed       │ "; java -classpath class Avoid "inputs/1.2. state-unsafe.csv" "inputs/2.3. request-exceed.csv"; echo "";
echo -n "Unsafe     │ Denied       │ "; java -classpath class Avoid "inputs/1.2. state-unsafe.csv" "inputs/2.4. request-denied.csv"; echo "";
echo "";

# All Combinations with Detection
echo    "╔═════════════════════════════════════════╗";
echo    "║                 Detection               ║";
echo    "╚═════════════════════════════════════════╝"
echo    "State File ┃ Result";
echo    "━━━━━━━━━━━╇━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━";
echo -n "Safe       │ "; java -classpath class Detection "inputs/1.1. state-safe.csv"; echo "";
echo -n "Unsafe     │ "; java -classpath class Detection "inputs/1.2. state-unsafe.csv"; echo "";