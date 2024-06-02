package com.tennisapp;
/**
 * This class represents a tennis match between two players.
 */
public class Match {
    private String date; // Datum des Spiels
    private String player1; // Name des ersten Spielers
    private String player2; // Name des zweiten Spielers
    private int player1Score; // Punktzahl des ersten Spielers
    private int player2Score; // Punktzahl des zweiten Spielers
    private String winner; // Gewinner des Spiels

    /**
     * Constructor to initialize a Match.
     * 
     * @param player1 The name of the first player.
     * @param player2 The name of the second player.
     * @param score The score of the match.
     * @param date The date of the match.
     */
    public Match(String date, String player1, String player2, String winner) {
        this.date = date;
        this.player1 = player1;
        this.player2 = player2;
        this.winner = winner;
    }

    //TODO: JavaDoc in Zukunft
    public Match(String date, String player1, String player2, int player1Score, int player2Score, String winner) {
        this.date = date;
        this.player1 = player1;
        this.player2 = player2;
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        this.winner = winner;
    }

    /**
     * Gets the date of the match.
     * 
     * @return The date of the match.
     */
    public String getDate() {
        return date;
    }

    /**
     * Gets the name of the first player.
     * 
     * @return The name of the first player.
     */
    public String getPlayer1() {
        return player1;
    }

    /**
     * Gets the name of the second player.
     * 
     * @return The name of the second player.
     */
    public String getPlayer2() {
        return player2;
    }

    // TODO: JavaDoc in zukunft
    public int getPlayer1Score() {
        return player1Score;
    }

    // Getter-Methode für player2Score
    public int getPlayer2Score() {
        return player2Score;
    }

    /**
     * Returns the winner of the match.
     * 
     * @return The name of the winning player.
     */
    public String getWinner() {
        return winner;
    }

    @Override
    public String toString() {
        return "Game {" +
                "date='" + this.date + '\'' +
                ", player1='" + this.player1 + '\'' +
                ", player2='" + this.player2 + '\'' +
                ", player1Score=" + this.player1Score +
                ", player2Score=" + this.player2Score +
                ", winner='" + this.winner + '\'' +
                '}';
    }
}
