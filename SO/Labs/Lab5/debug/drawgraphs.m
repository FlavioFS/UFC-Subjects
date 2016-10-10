load small.csv
load large.csv
load mixed.csv

% figure();

throughput = small(:, 3);
turnaround = small(:, 4);
waiting    = small(:, 5);
answer     = small(:, 6);
swaps      = small(:, 7);
