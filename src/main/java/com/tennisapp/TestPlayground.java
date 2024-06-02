package com.tennisapp;
import java.util.List;

/**
 * This class is just for testing and debugging, not relevant to the project.
 */
public class TestPlayground {
    public static void test() {
        MatchLoader gm = new MatchLoader();
        Evaluation ev = new Evaluation(gm);
        //List<Match> results = ev.findByName("Djokovic N.");
        System.err.println(ev.calculateWinRate("Djokovic N."));
        System.err.println(ev.calculateWinRate("Federer R."));

        List<Match> results2 = ev.findCommonGames("Djokovic N.", "Federer R.");
        System.err.println(ev.calculateWinRate("Djokovic N.", results2));
        System.err.println(ev.calculateWinRate("Federer R.", results2));
        System.err.println(ev.compareWinRateCommonGames("Djokovic N.", "Federer R.")); 
        System.err.println(ev.getWinRateOverTime("Djokovic N.")); 
    }
}
