package com.tennisapp;
import java.io.BufferedReader;
//import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is responsible for loading match history data.
 */
public class MatchLoader {

    //TODO: In the Future transfor the MatchLoader into a Singleton, there is no reaone for more than 1 Instance of MatchLoader
    
    private List<Match> matchHistory;

    public MatchLoader() {
        matchHistory = new ArrayList<>();
        this.load();
    }

    private List<List<String>> readAndParseFile() {
        List<List<String>> records = new ArrayList<>();
        //TODO: In the future outsure path to file to a env variable or select the File trough the GUI
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("data/atp_tennis.csv");
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                records.add(Arrays.asList(values));
            }
        } catch (Exception e) {
            System.out.println("Oopsie, Fehler :(");
        }
        return records;
    }

    private void load() {
        List<List<String>> records = this.readAndParseFile();
        records.forEach(el -> {
            //i[1] Datum
            //i[7] Spieler 1
            //i[8] Spieler 2
            //i[9] Gewinner 
            Match gameToAdd = new Match(el.get(1), el.get(7), el.get(8), el.get(9));
            matchHistory.add(gameToAdd);
        });
    }

    /**
     * Gets the match history.
     * 
     * @return The list of matches.
     */
    public List<Match> getMatchHistory(){
        return this.matchHistory;
    }
}
