package com.tennisapp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class provides various methods to evaluate player statistics from match history.
 */
public class Evaluation {

    private List<Match> matchHistory;

    /**
     * Constructor to initialize the Evaluation with a MatchLoader.
     * 
     * @param ml MatchLoader object to load match history.
     */
    public Evaluation(MatchLoader ml) {
        this.matchHistory = ml.getMatchHistory();
    }

    /**
     * Finds matches played by a given player.
     * 
     * @param name The name of the player.
     * @return A list of matches played by the specified player.
     */
    public List<Match> findByName(String name) {
        return matchHistory.stream()
                .filter(game -> game.getPlayer1().equals(name) || game.getPlayer2().equals(name))
                .toList();
    }

    /**
     * Finds matches where both specified players participated.
     * 
     * @param name1 The name of the first player.
     * @param name2 The name of the second player.
     * @return A list of matches where both players participated.
     */
    public List<Match> findCommonGames(String name1, String name2) {
        return matchHistory.stream()
                .filter(game -> (game.getPlayer1().equals(name1) || game.getPlayer1().equals(name2)) &&
                        (game.getPlayer2().equals(name1) || game.getPlayer2().equals(name2)))
                .toList();
    }

    /**
     * Calculates the win rate of a given player.
     * 
     * @param playerName The name of the player.
     * @return The win rate of the player.
     */
    public double calculateWinRate(String playerName) {
        List<Match> playerGames = this.findByName(playerName);
        int totalGames = playerGames.size();
        long wins = playerGames.stream().filter(game -> game.getWinner().equals(playerName)).count();
        double winPercentage = totalGames > 0 ? (double) wins / totalGames : 0.0;
        return winPercentage;
    }

    /**
     * Calculates the win rate of a given player based on a custom list of matches.
     * 
     * @param playerName The name of the player.
     * @param customList The custom list of matches.
     * @return The win rate of the player.
     */
    public double calculateWinRate(String playerName, List<Match> customList) {
        int totalGames = customList.size();
        long wins = customList.stream().filter(game -> game.getWinner().equals(playerName)).count();
        double winPercentage = totalGames > 0 ? (double) wins / totalGames : 0.0;
        return winPercentage;
    }

        /**
     * Compares the win rates of two players over the whole match history.
     * 
     * @param player1 The name of the first player.
     * @param player2 The name of the second player.
     * @return The player with a higher win rate in all matches or "Tie" if their win rates are equal.
     */
    public String compareWinRate(String player1, String player2) {
        double player1Winrate = calculateWinRate(player1);
        double player2Winrate = calculateWinRate(player2);

        if (player1Winrate == player2Winrate)
            return "Tie";

        return player1Winrate > player2Winrate ? player1 : player2;
    }

    /**
     * Compares the win rates of two players in matches where they both participated.
     * 
     * @param player1 The name of the first player.
     * @param player2 The name of the second player.
     * @return The player with a higher win rate in common matches or "Tie" if their win rates are equal.
     */
    public String compareWinRateCommonGames(String player1, String player2) {
        List<Match> gamesTogether = findCommonGames(player1, player2);
        int player1Wins = 0;
        int player2Wins = 0;

        for (Match game : gamesTogether) {
            if (game.getWinner().equals(player1))
                player1Wins++;
            if (game.getWinner().equals(player2))
                player2Wins++;
        }

        if (player1Wins == player2Wins)
            return "Tie";

        return player1Wins > player2Wins ? player1 : player2;
    }

    /**
     * Retrieves statistics of common opponents for two players.
     * 
     * @param player1 The name of the first player.
     * @param player2 The name of the second player.
     * @return A map containing common opponents and the player who performs better against each opponent.
     */
    public Map<String, String> getCommonOpponentsStats(String player1, String player2) {
        List<Match> gamesPlayer1 = findByName(player1);
        List<Match> gamesPlayer2 = findByName(player2);

        Set<String> opponentsPlayer1 = gamesPlayer1.stream()
                .map(game -> game.getPlayer1().equals(player1) ? game.getPlayer2() : game.getPlayer1())
                .collect(Collectors.toSet());
        Set<String> opponentsPlayer2 = gamesPlayer2.stream()
                .map(game -> game.getPlayer1().equals(player2) ? game.getPlayer2() : game.getPlayer1())
                .collect(Collectors.toSet());

        //Player 1 Opps x Player 2 Opps
        Set<String> commonOpponents = new HashSet<>(opponentsPlayer1);
        commonOpponents.retainAll(opponentsPlayer2);

        Map<String, String> performanceAgainstCommonOpponents = new HashMap<>();

        for (String opponent : commonOpponents) {
            List<Match> gamesAgainstOpponentPlayer1 = gamesPlayer1.stream()
                    .filter(game -> game.getPlayer1().equals(opponent) || game.getPlayer2().equals(opponent))
                    .collect(Collectors.toList());
            List<Match> gamesAgainstOpponentPlayer2 = gamesPlayer2.stream()
                    .filter(game -> game.getPlayer1().equals(opponent) || game.getPlayer2().equals(opponent))
                    .collect(Collectors.toList());

            double winRatePlayer1 = calculateWinRate(player1, gamesAgainstOpponentPlayer1);
            double winRatePlayer2 = calculateWinRate(player2, gamesAgainstOpponentPlayer2);

            String betterPerformer = winRatePlayer1 > winRatePlayer2 ? player1 : player2;
            if (winRatePlayer1 == winRatePlayer2) {
                betterPerformer = "Tie";
            }

            performanceAgainstCommonOpponents.put(opponent, betterPerformer);
        }

        return performanceAgainstCommonOpponents;
    }

    /**
     * Compares the performance of two players against common opponents.
     * 
     * @param player1 The name of the first player.
     * @param player2 The name of the second player.
     * @return The player who performs better against common opponents or "Tie" if their performances are equal.
     */
    public String compareCommonOpponentsStats(String player1, String player2) {
        Map<String, String> commonOpponentsStats = getCommonOpponentsStats(player1, player2);
        int player1Wins = 0;
        int player2Wins = 0;

        for (String performance : commonOpponentsStats.values()) {
            if (performance.equals(player1)) {
                player1Wins++;
            } else if (performance.equals(player2)) {
                player2Wins++;
            }
        }

        if (player1Wins > player2Wins) {
            return player1;
        } else if (player2Wins > player1Wins) {
            return player2;
        } else {
            return "Tie.";
        }
    }

    /**
     * Calculates the win rate of a player over time.
     * 
     * @param player The name of the player.
     * @return A map where keys are match dates and values are win rates up to that match.
     */
    public Map<String, Double> getWinRateOverTime(String player) {
        List<Match> playerMatches = findByName(player);
        Map<String, Double> winRateOverTime = new LinkedHashMap<>();

        for (int i = 1; i <= playerMatches.size(); i++) {
            List<Match> matchesUpToCurrent = playerMatches.subList(0, i);
            double winRate = calculateWinRate(player, matchesUpToCurrent);
            winRateOverTime.put(playerMatches.get(i - 1).getDate(), winRate);
        }

        return winRateOverTime;
    }
}
