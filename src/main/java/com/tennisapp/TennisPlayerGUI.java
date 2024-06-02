package com.tennisapp;

//import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TennisPlayerGUI extends JFrame {
    MatchLoader gm = new MatchLoader();
    Evaluation ev = new Evaluation(gm);

    // TODO: unschöne Lösung, sollte umstruktuiert werden! part1
    private String player1Name;
    private String player2Name;

    public TennisPlayerGUI() {
        setTitle("Tennis Player Stats");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel singlePlayerPanel = createSinglePlayerPanel(1);
        tabbedPane.addTab("Single Player", singlePlayerPanel);

        JPanel twoPlayerPanel = createTwoPlayerPanel();
        tabbedPane.addTab("Two Player Comparison", twoPlayerPanel);

        add(tabbedPane);
    }

    private JPanel createSinglePlayerPanel(Integer id) {
        JTextField nameInputField;
        JButton submitButton;
        JLabel nameLabel;
        JLabel winRateLabel;
        JList<String> lastGamesList;
        JPanel chartPanel;

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        nameLabel = new JLabel("Player Name");
        winRateLabel = new JLabel("Win Rate: ");

        nameInputField = new JTextField(20);
        submitButton = new JButton("Search");

        lastGamesList = new JList<>();

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        JFreeChart lineChart = ChartFactory.createLineChart(
                "Win Rate Over Time",
                "Date",
                "Win Rate",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);



        /* Beim verwenden der "FlatDarkLaf" Theme matcht das Aussehen der jfreechart Charts nicht dem Dark Theme
         * Hier habe ich versucht den jfreechart Teil diesem Theme anzupassen, klappt aber sieht dennoch nicht so schön aus
         * Deswegen vorrest auskommentiert 

        lineChart.setBackgroundPaint(Color.DARK_GRAY);
        lineChart.getTitle().setPaint(Color.WHITE);

        CategoryPlot plot = (CategoryPlot) lineChart.getPlot();
        plot.setBackgroundPaint(Color.BLACK);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, Color.CYAN);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setTickLabelPaint(Color.WHITE);
        domainAxis.setLabelPaint(Color.WHITE);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setTickLabelPaint(Color.WHITE);
        rangeAxis.setLabelPaint(Color.WHITE);
        */
        chartPanel = new ChartPanel(lineChart);

        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        winRateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        lastGamesList.setAlignmentX(Component.CENTER_ALIGNMENT);
        chartPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameInputField.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String playerName = nameInputField.getText();
                nameLabel.setText(playerName);

                //TODO: unschöne Lösung, sollte umstruktuiert werden! part2
                if (id == 1)
                    player1Name = playerName;
                if (id == 2)
                    player2Name = playerName;

                // Set Match History
                List<Match> playerMatches = ev.findByName(playerName);
                List<Match> last5Matches = playerMatches.subList(Math.max(playerMatches.size() - 5, 0),
                        playerMatches.size());
                List<String> last5GamesList = last5Matches.stream()
                        .map(match -> match.toString())
                        .collect(Collectors.toList());
                String[] last5GamesListAsArray = last5GamesList.toArray(new String[0]);
                lastGamesList.setListData(last5GamesListAsArray);

                // Set WinRate
                winRateLabel.setText(Double.toString(ev.calculateWinRate(playerName) * 100) + " %");

                // Set WinRateOverTime
                dataset.clear();
                Map<String, Double> winRatesOverTime = ev.getWinRateOverTime(playerName);
                for (Map.Entry<String, Double> entry : winRatesOverTime.entrySet()) {
                    dataset.addValue(entry.getValue(), "Win Rate", entry.getKey());
                }
            }
        });

        panel.add(nameLabel);
        panel.add(winRateLabel);
        panel.add(new JScrollPane(lastGamesList));
        panel.add(chartPanel);
        panel.add(nameInputField);
        panel.add(submitButton);

        return panel;
    }

    private JPanel createComparePanel() {
        JButton submitButton;

        JLabel winRateCompareLabel = new JLabel("Compare based on winrate: ");
        JLabel winRateCompare = new JLabel("");

        JLabel commonMatchesCompareLabel = new JLabel("Compare based on common match history: ");
        JLabel commonMatchesCompare = new JLabel("");

        JLabel compareIndirectCommonOpponentsLabel = new JLabel("Compare based on common opponents: ");
        JLabel compareIndirectCommonOpponents = new JLabel("");

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        submitButton = new JButton("Compare");

        winRateCompareLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        winRateCompare.setAlignmentX(Component.CENTER_ALIGNMENT);
        commonMatchesCompareLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        commonMatchesCompare.setAlignmentX(Component.CENTER_ALIGNMENT);
        compareIndirectCommonOpponentsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        compareIndirectCommonOpponents.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                winRateCompare.setText(ev.compareWinRate(player1Name, player2Name));
                commonMatchesCompare.setText(ev.compareWinRateCommonGames(player1Name, player2Name));
                compareIndirectCommonOpponents.setText(ev.compareCommonOpponentsStats(player1Name, player2Name));
            }
        });

        panel.add(winRateCompareLabel);
        panel.add(winRateCompare);
        panel.add(commonMatchesCompareLabel);
        panel.add(commonMatchesCompare);
        panel.add(compareIndirectCommonOpponentsLabel);
        panel.add(compareIndirectCommonOpponents);
        panel.add(submitButton);

        return panel;
    }

    private JPanel createTwoPlayerPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3));

        panel.add(createSinglePlayerPanel(1));
        panel.add(createSinglePlayerPanel(2));
        panel.add(createComparePanel());

        return panel;
    }

    public static void main(String[] args) {
        // Set Theme
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            TennisPlayerGUI gui = new TennisPlayerGUI();
            gui.setVisible(true);
        });
    }
}
