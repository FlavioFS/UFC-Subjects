########################################################
#	Testing all combinations
########################################################

# All Combinations with Safety
echo    "╔═════════════════════════════════════════╗";
echo    "║                 Safety                  ║";
echo    "╚═════════════════════════════════════════╝"
echo    "File       ┃ Result";
echo    "━━━━━━━━━━━╇━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━";
echo -n "Safe       │ "; java -classpath class Safety "inputs/1.1. state-safe.csv"; echo "";
echo -n "Unsafe     │ "; java -classpath class Safety "inputs/1.2. state-unsafe.csv"; echo "";
echo -n "Deadlocked │ "; java -classpath class Safety "inputs/1.3. state-deadlocked.csv"; echo "";
echo "";

# All Combinations with Avoid
echo    "╔═════════════════════════════════════════╗";
echo    "║                 Avoid                   ║";
echo    "╚═════════════════════════════════════════╝"
echo    "State File ┃ Request File ┃ Result";
echo    "━━━━━━━━━━━╇━━━━━━━━━━━━━━╇━━━━━━━━━━━━━━━━";
echo -n "Safe       │ Safe         │ "; java -classpath class Avoid "inputs/1.1. state-safe.csv" "inputs/2.1. request-safe.csv"; echo "";
echo -n "Safe       │ Unsafe       │ "; java -classpath class Avoid "inputs/1.1. state-safe.csv" "inputs/2.2. request-unsafe.csv"; echo "";
echo -n "Safe       │ Deadlocked   │ "; java -classpath class Avoid "inputs/1.1. state-safe.csv" "inputs/2.3. request-deadlocked.csv"; echo "";
echo    "───────────┼──────────────┼────────────────";
echo -n "Unsafe     │ Safe         │ "; java -classpath class Avoid "inputs/1.2. state-unsafe.csv" "inputs/2.1. request-safe.csv"; echo "";
echo -n "Unsafe     │ Unsafe       │ "; java -classpath class Avoid "inputs/1.2. state-unsafe.csv" "inputs/2.2. request-unsafe.csv"; echo "";
echo -n "Unsafe     │ Deadlocked   │ "; java -classpath class Avoid "inputs/1.2. state-unsafe.csv" "inputs/2.3. request-deadlocked.csv"; echo "";
echo    "───────────┼──────────────┼────────────────";
echo -n "Deadlocked │ Safe         │ "; java -classpath class Avoid "inputs/1.3. state-deadlocked.csv" "inputs/2.1. request-safe.csv"; echo "";
echo -n "Deadlocked │ Unsafe       │ "; java -classpath class Avoid "inputs/1.3. state-deadlocked.csv" "inputs/2.2. request-unsafe.csv"; echo "";
echo -n "Deadlocked │ Deadlocked   │ "; java -classpath class Avoid "inputs/1.3. state-deadlocked.csv" "inputs/2.3. request-deadlocked.csv"; echo "";
echo "";

# All Combinations with Detection
echo    "╔═════════════════════════════════════════╗";
echo    "║                 Detection               ║";
echo    "╚═════════════════════════════════════════╝"
echo    "State File ┃ Request File ┃ Result";
echo    "━━━━━━━━━━━╇━━━━━━━━━━━━━━╇━━━━━━━━━━━━━━━━";
echo -n "Safe       │ Safe         │ "; java -classpath class Detection "inputs/1.1. state-safe.csv" "inputs/2.1. request-safe.csv"; echo "";
echo -n "Safe       │ Unsafe       │ "; java -classpath class Detection "inputs/1.1. state-safe.csv" "inputs/2.2. request-unsafe.csv"; echo "";
echo -n "Safe       │ Deadlocked   │ "; java -classpath class Detection "inputs/1.1. state-safe.csv" "inputs/2.3. request-deadlocked.csv"; echo "";
echo    "───────────┼──────────────┼────────────────";
echo -n "Unsafe     │ Safe         │ "; java -classpath class Detection "inputs/1.2. state-unsafe.csv" "inputs/2.1. request-safe.csv"; echo "";
echo -n "Unsafe     │ Unsafe       │ "; java -classpath class Detection "inputs/1.2. state-unsafe.csv" "inputs/2.2. request-unsafe.csv"; echo "";
echo -n "Unsafe     │ Deadlocked   │ "; java -classpath class Detection "inputs/1.2. state-unsafe.csv" "inputs/2.3. request-deadlocked.csv"; echo "";
echo    "───────────┼──────────────┼────────────────";
echo -n "Deadlocked │ Safe         │ "; java -classpath class Detection "inputs/1.3. state-deadlocked.csv" "inputs/2.1. request-safe.csv"; echo "";
echo -n "Deadlocked │ Unsafe       │ "; java -classpath class Detection "inputs/1.3. state-deadlocked.csv" "inputs/2.2. request-unsafe.csv"; echo "";
echo -n "Deadlocked │ Deadlocked   │ "; java -classpath class Detection "inputs/1.3. state-deadlocked.csv" "inputs/2.3. request-deadlocked.csv"; echo "";