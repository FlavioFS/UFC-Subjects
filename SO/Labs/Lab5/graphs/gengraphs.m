load small.csv
load large.csv
load mixed.csv

%% Plot
myfig = figure();

% Labels
labels = [
	'FCFS',
	'SJF',
	'SJFP',
	'RR',
	'Priority',
	'PriorityP'
]

% Values
throughput = small(:, 3);
turnaround = small(:, 4);
waiting    = small(:, 5);
answer     = small(:, 6);
swaps      = small(:, 7);

axes1 = axes('Parent', myfig, 'XTickLabel', labels);
hold(axes1,'all');

% Turnaround
plot(turnaround, 'w-^',...
     'LineWidth', 1,...
     'MarkerSize', 7,...
     'MarkerEdgeColor', 'w',...
     'MarkerFaceColor', 'c'
);
hold on;

% Waiting Time
plot(waiting, 'k-o',...
     'LineWidth', 2,...
     'MarkerSize', 10,...
     'MarkerEdgeColor', 'k',...
     'MarkerFaceColor', 'w'
);
hold on;

% Answer Time
plot(answer, 'g:d',...
     'LineWidth', 2,...
     'MarkerSize', 7,...
     'MarkerEdgeColor', 'k',...
     'MarkerFaceColor', 'g'
);
hold on;

% Swaps
plot(swaps, 'm-v',...
     'LineWidth', 2,...
     'MarkerSize', 7,...
     'MarkerEdgeColor', 'k',...
     'MarkerFaceColor', 'm'
);
hold on;

daLegend = legend({'Turnaround', 'Waiting Time', 'Answer Time', 'Swaps'});
set(daLegend,'color', 'none');
set(daLegend,'FontSize', 10);
set(daLegend,'FontWeight', 'bold');
set(gca, 'color', [0.3 0.3 0.3]);  % Background color (chart area)
set(gcf, 'color', [0.4 0.4 0.4]);  % Background color (area outside of chart)