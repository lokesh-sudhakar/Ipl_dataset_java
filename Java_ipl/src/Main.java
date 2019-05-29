
import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.TreeMap;
import java.util.*;

public class Main {
    public static final int TOTAL_RUNS = 17;
    public static final int EXTRA_RUNS = 16;
    public static final int MATCH_ID = 0;
    public static final int Bowler = 8;
    public static final int BOWLING_TEAM = 3;
    public static final int Team1 = 4;
    public static final int Team2 = 5;
    public static final int Winners = 10;
    public static final int Season = 1;

    public static void main(String[] args) {
        List<Delivery> deliveries = readDeliveriesFromCSV("deliveries.csv");
        List<Match> matches = readMatchesFromCSV("matches.csv");

        noOfMatchesPlayedPerYear(matches);
        noOfMatchesWonByEachTeam(matches);
        extraRunsConcededBYEachTeam(matches,deliveries);
        topTenEconomicalBowlers(matches,deliveries);
    }
    private static void noOfMatchesPlayedPerYear(List<Match> matches){
        TreeMap<Integer,Integer> matchesPlayed= new TreeMap<Integer,Integer>();
        for (Match match : matches) {
            if(matchesPlayed.containsKey(match.getSeason())){
                int count= matchesPlayed.get(match.getSeason());
                matchesPlayed.put(match.getSeason(),count+1);
            }else {
                matchesPlayed.put(match.getSeason(), 1);
            }
        }
        System.out.println("***********no.of matches played per season******************");
        for ( Map.Entry<Integer, Integer> entry : matchesPlayed.entrySet()) {
            int year = entry.getKey();
            int matches_played = entry.getValue();
            System.out.println(year+"------------"+matches_played);
        }
    }
    private static void noOfMatchesWonByEachTeam(List<Match> matches){
        HashMap<String,Integer>  matchesWon= new HashMap<String,Integer>();
        // <season, <team, win_count>>
        TreeMap<Integer,HashMap<String,Integer>> eachSeason = new TreeMap<Integer,HashMap<String,Integer>>();
        for (Match match : matches) {
           if(eachSeason.containsKey(match.getSeason())){
                if (matchesWon.containsKey(match.getWinners())) {
                    int win_count = matchesWon.get(match.getWinners());
                    matchesWon.put(match.getWinners(), win_count + 1);
                } else {
                    matchesWon.put(match.getWinners(),1);
                }
               eachSeason.put(match.getSeason(),matchesWon);
            }
            else{
                matchesWon = new HashMap<>();
                eachSeason.put(match.getSeason(),matchesWon);
            }
        }
        matchesWon.remove("");
        System.out.println("\n\n*********************Matches won by each team per year********************");
        for ( Map.Entry<Integer,HashMap<String,Integer>> entry : eachSeason.entrySet()) {
            int season = entry.getKey();
            HashMap<String,Integer> iner= entry.getValue();
            System.out.println("\n\n*********"+season+"********");

            for ( Map.Entry<String,Integer> data : iner.entrySet()) {
                String team = data.getKey();
                int won_count= data.getValue();
                System.out.println(team +" won "+won_count+" matches");
            }
        }
    }
    private static void extraRunsConcededBYEachTeam(List<Match> matches,List<Delivery> deliveries) {
        List<Integer> match_id2016 = new ArrayList<Integer>();
        for (Match m : matches){
            if (m.getSeason()==2016){
                match_id2016.add(m.getId());
            }
        }
        HashMap<String,Integer> extraRuns = new HashMap<>();
        for (Delivery delivery : deliveries) {
              if (match_id2016.contains(delivery.getId())){
                if (extraRuns.containsKey(delivery.getBowling_team())){
                    int runs=extraRuns.get(delivery.getBowling_team());
                    extraRuns.put(delivery.getBowling_team(),delivery.getExtra_runs()+runs);
                }else{
                    extraRuns.put(delivery.getBowling_team(),delivery.getExtra_runs());
                }
            }
        }
        System.out.println("\n\n***************Extra runs conceded by each team in 2016**********************");
        for ( Map.Entry<String, Integer> entry : extraRuns.entrySet()) {
            String team = entry.getKey();
            int extraruns = entry.getValue();
            System.out.println(team+"------------"+extraruns);
        }
    }
    private static void topTenEconomicalBowlers(List<Match> matches , List<Delivery> deliveries) {
        List<Integer> match_id2015 = new ArrayList<Integer>();
        //totalRuns<bowler, runs_conceded>
        HashMap<String, Integer> totalRuns = new HashMap<String, Integer>();
        //totalBalls<bowler,balls_bowled>
        HashMap<String, Integer> totalBalls = new HashMap<String, Integer>();
        for (Match match : matches){
            if (match.getSeason()==2015){
                match_id2015.add(match.getId());
            }
        }
        for (Delivery delivery : deliveries) {
            if (match_id2015.contains(delivery.getId())) {
                if (totalRuns.containsKey(delivery.getBowler())) {
                    int runs = totalRuns.get(delivery.getBowler());
                    totalRuns.put(delivery.getBowler(), delivery.getTotal_runs()+runs);
                } else {
                    totalRuns.put(delivery.getBowler(), delivery.getTotal_runs());
                }
                if (totalBalls.containsKey(delivery.getBowler())) {
                    int balls = totalBalls.get(delivery.getBowler());
                    totalBalls.put(delivery.getBowler(), balls + 1);
                } else {
                    totalBalls.put(delivery.getBowler(), 1);
                }
            }
        }
        // TreeMap<economy,bowler>
        TreeMap<Double, String> economy = new TreeMap<Double, String>();
        for (Map.Entry<String, Integer> entry : totalRuns.entrySet()) {
            String bowler = entry.getKey();
            double runs = totalRuns.get(bowler);
            double balls = totalBalls.get(bowler);
            //calculating economy
            double eco = (runs * 6) / balls;
            economy.put(eco, bowler);
        }
        int count = 1;
        System.out.println("\n\n*********Top Economy Bowlers***********");
        for (Map.Entry<Double, String> entry : economy.entrySet()) {
            Double eco = entry.getKey();
            String bowler = entry.getValue();
            if (count <= 10) {
                System.out.println(bowler.trim() + "---------------" + eco);
            }
            count += 1;
            economy.put(eco, bowler);
        }
    }
    private static List<Match> readMatchesFromCSV(String fileName) {
        List<Match> matches = new ArrayList<>();
        File file = new File(fileName);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            br.readLine();
            String line = br.readLine();
            while (line != null) {
                String[] attributes = line.split(",");
                Match match = createMatch(attributes);
                matches.add(match);
                line = br.readLine();
            }
        }catch (FileNotFoundException f){
            System.out.println("filenotfound");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matches;
    }
    private static Match createMatch(String[] fields) {
        int ids = Integer.parseInt(fields[MATCH_ID]);
        int season = Integer.parseInt(fields[Season]);
        String team1 = fields[Team1];
        String team2 =  fields[Team2];
        String winners = fields[Winners];
        return new Match(ids, season, team1, team2, winners);
    }
    private static List<Delivery> readDeliveriesFromCSV(String fileName) {
        List<Delivery> deliveries = new ArrayList<>();
        File file = new File(fileName);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            br.readLine();
            String line = br.readLine();
            while (line != null) {
                String[] attributes = line.split(",");
                Delivery delivery = createDeliveries(attributes);
                 deliveries.add(delivery);
                line = br.readLine();
            }
        }catch (FileNotFoundException f){
            System.out.println("filenotfound");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return deliveries;
    }
    private static Delivery createDeliveries(String[] delivery) {
        int id =Integer.parseInt(delivery[MATCH_ID]);
        String bowling_team = delivery[BOWLING_TEAM];
        int extra_runs = Integer.parseInt(delivery[EXTRA_RUNS]);
        int total_runs = Integer.parseInt(delivery[TOTAL_RUNS]);
        String bowler = delivery[Bowler];
        return new Delivery(id, bowling_team, extra_runs,bowler,total_runs);
    }
}
