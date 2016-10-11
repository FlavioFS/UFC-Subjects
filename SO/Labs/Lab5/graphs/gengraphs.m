load small.csv
load large.csv
load mixed.csv

disp ('Generating graphs...');

% Values S
throughputS = small(:, 3);
turnaroundS = small(:, 4);
waitingS    = small(:, 5);
answerS     = small(:, 6);
swapsS      = small(:, 7);

% Values L
throughputL = large(:, 3);
turnaroundL = large(:, 4);
waitingL    = large(:, 5);
answerL     = large(:, 6);
swapsL      = large(:, 7);

% Values M
throughputM = mixed(:, 3);
turnaroundM = mixed(:, 4);
waitingM    = mixed(:, 5);
answerM     = mixed(:, 6);
swapsM      = mixed(:, 7);

%% gengraph: function description
function [outputs] = gengraph(throughput, turnaround, waiting, answer, swaps)
     % Labels
     labels = [
          'FCFS',
          'SJF',
          'SJFP',
          'RR',
          'Priority',
          'PriorityP'
     ];

     %% Plot
     myfig = figure();

     max = 0;
     for i = 1:6
          if (max < throughput(i)) max = throughput(i); end;
          if (max < turnaround(i)) max = turnaround(i); end;
          if (max < waiting(i))    max = waiting(i);    end;
          if (max < answer(i))     max = answer(i);     end;
          if (max < swaps(i))      max = swaps(i);      end;
     end

     axes1 = axes('Parent', myfig, 'XTickLabel', labels);
     axis([1, 6, 0, max*1.3]);
     hold(axes1,'all');

     % Turnaround
     plot(turnaround, 'w-^',...
          'LineWidth', 2,...
          'MarkerSize', 7,...
          'MarkerEdgeColor', 'k',...
          'MarkerFaceColor', 'w'
     );
     hold on;

     % Waiting Time
     plot(waiting, 'r-s',...
          'LineWidth', 2,...
          'MarkerSize', 7,...
          'MarkerEdgeColor', 'k',... 
          'MarkerFaceColor', 'r'
     );
     hold on;

     % Answer Time
     plot(answer, 'g:o',...
          'LineWidth', 2,...
          'MarkerSize', 7,...
          'MarkerEdgeColor', 'k',...
          'MarkerFaceColor', 'g'
     );
     hold on;

     % Swaps
     plot(swaps, 'c-v',...
          'LineWidth', 2,...
          'MarkerSize', 7,...
          'MarkerEdgeColor', 'k',...
          'MarkerFaceColor', 'c'
     );
     hold on;

     daLegend = legend({'Turnaround', 'Waiting Time', 'Answer Time', 'Swaps'});
     set(daLegend,'color', 'none');
     set(daLegend,'FontSize', 10);
     set(daLegend,'FontWeight', 'bold');
     set(gca, 'color', [0.3 0.3 0.3]);  % Background color (chart area)
     set(gcf, 'color', [0.4 0.4 0.4]);  % Background color (area outside of chart)

outputs = 0;
end;

gengraph(throughputS, turnaroundS, waitingS, answerS, swapsS);
gengraph(throughputL, turnaroundL, waitingL, answerL, swapsL);
gengraph(throughputM, turnaroundM, waitingM, answerM, swapsM);