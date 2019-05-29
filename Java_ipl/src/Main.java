
import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.TreeMap;
import java.util.*;

public class Main {
    //column index
    public static final int totalRuns = 17;
    public static final int extraRuns = 16;
    public static final int matchId = 0;
    public static final int Bowler = 8;
    public static final int bowlingTeam = 3;
    public static final int Team1 = 4;
    public static final int Team2 = 5;
    public static final int Winners = 10;


    public static void main(String[] args) {

        List<Delivery> deliveries = readDeliveriesFromCSV("deliveries.csv");
        List<Match> matches = readMatchesFromCSV("matches.csv");

        noOfMatchesPlayedPerYear(matches);
        noOfMatchesWonByEachTeam(matches);
        extraRunsConcededBYEachTeam(matches,deliveries);
        topTenEconomicalBowlers(matches,deliveries);

    }

    private static void noOfMatchesPlayedPerYear(List<Match> matches){

        //TreeMap<season,match_count>
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
                //removing the matches which are tied

                eachSeason.put(match.getSeason(),matchesWon);
            }
            else{//this code executes when the season/year changes
                matchesWon = new HashMap<>();
                eachSeason.put(match.getSeason(),matchesWon);
            }

        }
        //removing the matches which are tied
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

        List<Integer> match_id = new ArrayList<Integer> ();
        //get matchIds
        for (Match m : matches){
            if (m.getSeason()==2016){
                match_id.add(m.getId());
            }
        }

        HashMap<String,Integer> extraRuns = new HashMap<>();

        for (Delivery delivery : deliveries) {
              if (match_id.contains(delivery.getId())){
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
        for (Delivery d : deliveries) {
            if (match_id2015.contains(d.getId())) {
                if (totalRuns.containsKey(d.getBowler())) {
                    int runs = totalRuns.get(d.getBowler());
                    totalRuns.put(d.getBowler(), d.getTotal_runs()+runs);
                } else {
                    totalRuns.put(d.getBowler(), d.getTotal_runs());
                }
                if (totalBalls.containsKey(d.getBowler())) {
                    int b = totalBalls.get(d.getBowler());
                    totalBalls.put(d.getBowler(), b + 1);
                } else {
                    totalBalls.put(d.getBowler(), 1);
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

    private static Match createMatch(String[] metadata) {

        int ids = Integer.parseInt(metadata[matchId]);
        int season = Integer.parseInt(metadata[1]);
        String team1 = metadata[Team1];
        String team2 =  metadata[Team2];
        String winners = metadata[Winners];

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

        int id =Integer.parseInt(delivery[matchId]);
        String bowling_team = delivery[bowlingTeam];
        int extra_runs = Integer.parseInt(delivery[extraRuns]);
        int total_runs = Integer.parseInt(delivery[totalRuns]);
        String bowler = delivery[Bowler];

        return new Delivery(id, bowling_team, extra_runs,bowler,total_runs);
    }
}
