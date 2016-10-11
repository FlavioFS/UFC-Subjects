load small.csv
load large.csv
load mixed.csv

disp ('Generating graphs...');

% Values S
processingS = small(:, 1);
cpuS        = small(:, 2);
throughputS = small(:, 3);
turnaroundS = small(:, 4);
waitingS    = small(:, 5);
answerS     = small(:, 6);
swapsS      = small(:, 7);

% Values L
processingL = large(:, 1);
cpuL        = large(:, 2);
throughputL = large(:, 3);
turnaroundL = large(:, 4);
waitingL    = large(:, 5);
answerL     = large(:, 6);
swapsL      = large(:, 7);

% Values M
processingM = mixed(:, 1);
cpuM        = mixed(:, 2);
throughputM = mixed(:, 3);
turnaroundM = mixed(:, 4);
waitingM    = mixed(:, 5);
answerM     = mixed(:, 6);
swapsM      = mixed(:, 7);

%% gengraph: function description
function [outputs] = gengraph(titleStr, processing, cpu, throughput, turnaround, waiting, answer, swaps)
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
     myfig = figure('units', 'normalized', 'outerposition', [0 0 1 1]);
     title(titleStr);
     set(gca, 'XTick', [], 'YTick', []);  % Clear labels

     maxY = 0;
     for i = 1:6
          if (maxY < throughput(i)) maxY = throughput(i); end;
          if (maxY < turnaround(i)) maxY = turnaround(i); end;
          if (maxY < waiting(i))    maxY = waiting(i);    end;
          if (maxY < answer(i))     maxY = answer(i);     end;
          if (maxY < swaps(i))      maxY = swaps(i);      end;
     end

     axes1 = axes('Parent', myfig, 'XTickLabel', labels);
     axis([1, 6, 0, maxY*1.3]);
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
     plot(answer, 'g-.o',...
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

     saveas(myfig, strcat('gr-stats-', titleStr, '.png'));
     close(myfig);

     %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

     % Processing and CPU Time never change
     %% Plot
     myfig = figure('units', 'normalized', 'outerposition', [0 0 1 1]);
     title(titleStr);
     set(gca, 'XTick', [], 'YTick', []);  % Clear labels

     maxY = 0;
     for i = 1:6
          pI = processing(i);
          cpuI = cpu(i);

          if (maxY < pI)   maxY = pI;   end;
          if (maxY < cpuI) maxY = cpuI; end;
     end

     axes1 = axes('Parent', myfig, 'XTickLabel', labels);
     axis([1, 6, 0, maxY*1.2]);
     hold(axes1,'all');

     % Turnaround
     plot(processing, 'y-<',...
          'LineWidth', 2,...
          'MarkerSize', 7,...
          'MarkerEdgeColor', 'k',...
          'MarkerFaceColor', 'y'
     );
     hold on;

     % Waiting Time
     plot(cpu, 'm->',...
          'LineWidth', 2,...
          'MarkerSize', 7,...
          'MarkerEdgeColor', 'k',... 
          'MarkerFaceColor', 'm'
     );
     hold on;

     daLegend = legend({'Processing Time', 'CPU Time'});
     set(daLegend,'color', 'none');
     set(daLegend,'FontSize', 10);
     set(daLegend,'FontWeight', 'bold');
     set(gca, 'color', [0.3 0.3 0.3]);  % Background color (chart area)
     set(gcf, 'color', [0.4 0.4 0.4]);  % Background color (area outside of chart)

     saveas(myfig, strcat('gr-cpu-', titleStr, '.png'));
     close(myfig);

     outputs = 0;
end;

gengraph('Small', processingS, cpuS, throughputS, turnaroundS, waitingS, answerS, swapsS);
gengraph('Large', processingL, cpuL, throughputL, turnaroundL, waitingL, answerL, swapsL);
gengraph('Mixed', processingM, cpuM, throughputM, turnaroundM, waitingM, answerM, swapsM);