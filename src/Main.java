
import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.TreeMap;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        // write your code here
        noOfMatchesPlayedPerYear();
        noOfMatchesWonByEachTeam();
        extraRunsConcededBYEachTeam();
        topTenEconomicalBowlers();

    }

    private static void noOfMatchesPlayedPerYear(){
        //reading the csv file and storing in list of Matches
        List<Matches> Match = readMatchesFromCSV("/home/lokesh/Mountblue/Ipl_project/ipldatajava_project/matches.csv");
        //Tree map to store corresponding season and matches played
        TreeMap<Integer,Integer> hashmap= new TreeMap<Integer,Integer>();
        //looping through all matches
        for (Matches m : Match) {
            //if hashmap contains season then increment the matches played in that season
            if(hashmap.containsKey(m.getSeason())){
                //retrieving the previous matches played count
                int count= hashmap.get(m.getSeason());
                // incrementing to that count and pushing into hashmap
                hashmap.put(m.getSeason(),count+1);

            }else {
                //if hashmap does not contain season then push season and initialize the count with 1
                hashmap.put(m.getSeason(), 1);
            }
        }
        System.out.println("***********no.of matches played per season******************");
        //looping through the key value pairs
        for ( Map.Entry<Integer, Integer> entry : hashmap.entrySet()) {
            //getting key values
            int year = entry.getKey();
            int matches_played = entry.getValue();
            //printing
            System.out.println(year+"------------"+matches_played);

        }

    }
    private static void noOfMatchesWonByEachTeam(){
        //reading match data from csv file
        List<Matches> Match = readMatchesFromCSV("/home/lokesh/Mountblue/Ipl_project/ipldatajava_project/matches.csv");

        //to store matches won per team
        HashMap<String,Integer>  matchesWon= new HashMap<String,Integer>();
        //to store  each season in which teams have won
        // Format-> (season-->(team-->win_count))
        TreeMap<Integer,HashMap<String,Integer>> eachSeason = new TreeMap<Integer,HashMap<String,Integer>>();
        //looping through each match
        for (Matches m : Match) {
            //if it contains season
            if(eachSeason.containsKey(m.getSeason())){

                //check if winning is present in keys
                //if present increment the win count
                if (matchesWon.containsKey(m.getWinners())) {
                    int win_count = matchesWon.get(m.getWinners());
                    //increment and push the data to hashmap
                    matchesWon.put(m.getWinners(), win_count + 1);

                } else {
                    //if winning team is not present in hashmap then add and initialize the win count with 1
                    matchesWon.put(m.getWinners(),1);
                }
                //removing the matches which are tied
                matchesWon.remove("");
                //adding the matches won by each team data into corresponding season
                eachSeason.put(m.getSeason(),matchesWon);
            }
            else{//this code executes when the season/year changes
                //if the year is not present in the eachSeason hashmap
                //add new empty matcheswon hash map with corresping season
                matchesWon = new HashMap<>();
                //pushing
                eachSeason.put(m.getSeason(),matchesWon);

            }

        }
        matchesWon.remove("");
        System.out.println("\n\n*********************Matches won by each team per year********************");
        for ( Map.Entry<Integer,HashMap<String,Integer>> entry : eachSeason.entrySet()) {
            int season = entry.getKey();
            HashMap<String,Integer> iner= entry.getValue();
            System.out.println("\n\n*********"+season+"********");
            //looping through the inner hash map
            for ( Map.Entry<String,Integer> data : iner.entrySet()) {
                String team = data.getKey();
                int won_count= data.getValue();
                System.out.println(team +" won "+won_count+" matches");
            }
        }


    }
    private static void extraRunsConcededBYEachTeam() {
        //reading both delivery data and matches data and storing in the list
        List<Delivery> Deliverydata = readDeliveriesFromCSV("/home/lokesh/Mountblue/Ipl_project/ipldatajava_project/deliveries.csv");
        List<Matches> Match = readMatchesFromCSV("/home/lokesh/Mountblue/Ipl_project/ipldatajava_project/matches.csv");
        // let's print all the person read from CSV file

        //for storing the match id's corresponding to the 2106 year from matches data
        List<Integer> match_id = new ArrayList<Integer> ();
        //loping and storing match id's
        for (Matches m : Match){
            if (m.getSeason()==2016){
                match_id.add(m.getId());
            }
        }
        //System.out.println(match_id);
        //to store extra runs per team
        HashMap<String,Integer> extraRuns = new HashMap<>();
        //looping through each delivery
        for (Delivery d : Deliverydata) {
            //  System.out.println(d.getId()+"  "+d.getBowling_team()+" "+d.getExtra_runs());
            if (match_id.contains(d.getId())){
                if (extraRuns.containsKey(d.getBowling_team())){
                    //getting extraRuns conceded by the bowling team
                    int runs=extraRuns.get(d.getBowling_team());
                    //adding extra runs to previous extra runs conceded by that bowling team
                    extraRuns.put(d.getBowling_team(),d.getExtra_runs()+runs);

                }else{
                    extraRuns.put(d.getBowling_team(),d.getExtra_runs());
                }
            }

        }
        System.out.println("\n\n***************Extra runs conceded by each team in 2016**********************");
        //System.out.print(hash);
        for ( Map.Entry<String, Integer> entry : extraRuns.entrySet()) {
            String team = entry.getKey();
            int extraruns = entry.getValue();
            System.out.println(team+"------------"+extraruns);

        }
    }
    private static void topTenEconomicalBowlers() {
        // reading matches and deliveries data
        List<Delivery> Deliverydata = readDeliveriesFromCSV("/home/lokesh/Mountblue/Ipl_project/ipldatajava_project/deliveries.csv");
        List<Matches> Match = readMatchesFromCSV("/home/lokesh/Mountblue/Ipl_project/ipldatajava_project/matches.csv");
        // let's print all the person read from CSV file
        List<Integer> match_id2015 = new ArrayList<Integer>();


        //hashmaps to calculate total runs and total balls bowled by bowler
        HashMap<String, Integer> truns = new HashMap<String, Integer>();
        HashMap<String, Integer> tballs = new HashMap<String, Integer>();
        //to store match ids corresponding to one season
        for (Matches m : Match){
            if (m.getSeason()==2015){
                match_id2015.add(m.getId());
            }
        }

        for (Delivery d : Deliverydata) {
            if (match_id2015.contains(d.getId())) {
                if (truns.containsKey(d.getBowler())) {
                    //System.out.println(d.getTotal_runs());
                    int r = truns.get(d.getBowler());
                    int total_r = r + d.getTotal_runs();
                    truns.put(d.getBowler(), total_r);
                } else {
                    truns.put(d.getBowler(), d.getTotal_runs());
                }
                if (tballs.containsKey(d.getBowler())) {
                    int b = tballs.get(d.getBowler());
                    tballs.put(d.getBowler(), b + 1);
                } else {
                    tballs.put(d.getBowler(), 1);
                }

            }
        }
        //System.out.println(truns);
        //System.out.println(tballs);
        TreeMap<Double, String> economy = new TreeMap<Double, String>();
        for (Map.Entry<String, Integer> entry : truns.entrySet()) {
            String bowlr = entry.getKey();
            int x = entry.getValue();
            // do something with key and/or tab
            //System.out.println(bowlr+" "+x);
            double runs = truns.get(bowlr);
            double balls = tballs.get(bowlr);
            double eco = (runs * 6) / balls;

            economy.put(eco, bowlr);

        }
        //System.out.println(economy);
        int count = 1;
        System.out.println("*********Top Economy Bowlers***********");
        for (Map.Entry<Double, String> entry : economy.entrySet()) {
            Double eco = entry.getKey();
            String bowlr = entry.getValue();
            if (count <= 10) {
                System.out.println(bowlr.trim() + "---------------" + eco);
            }
            count += 1;

            economy.put(eco, bowlr);
        }
    }

    private static List<Matches> readMatchesFromCSV(String fileName) {
        List<Matches> match1 = new ArrayList<>();
        //Path pathToFile = Paths.get(fileName);
        File file = new File(fileName);
        // create an instance of BufferedReader
        // using try with resource, Java 7 feature to close resources
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            br.readLine();
            String line = br.readLine();

            while (line != null) {
                //System.out.println(line);
                String[] attributes = line.split(",");
                //System.out.println( Integer.parseInt(attributes[0]));
                Matches onematch = createMatch(attributes);

                // adding book into ArrayList
                match1.add(onematch);

                // read next line before looping
                // if end of file reached, line would be null
                line = br.readLine();
            }

        }catch (FileNotFoundException f){
            System.out.println("filenotfound");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return match1;
    }
    private static Matches createMatch(String[] metadata) {
        //System.out.println(metadata[0]);
        int ids = Integer.parseInt(metadata[0]);
        int season = Integer.parseInt(metadata[1]);

        String team1 = metadata[4];
        String team2 =  metadata[5];
        String winners = metadata[10];



        // create and return book of this metadata
        return new Matches(ids, season, team1, team2, winners);

    }


    private static List<Delivery> readDeliveriesFromCSV(String fileName) {
        List<Delivery> delivery1 = new ArrayList<>();
        //Path pathToFile = Paths.get(fileName);
        File file = new File(fileName);
        // create an instance of BufferedReader
        // using try with resource, Java 7 feature to close resources
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            //System.out.println("buffer working");
            br.readLine();
            String line = br.readLine();

            while (line != null) {
                //System.out.println(line);
                String[] attributes = line.split(",");
                //System.out.println( Integer.parseInt(attributes[0]));
                Delivery onedelivery = createDeliveries(attributes);

                // adding book into ArrayList
                delivery1.add(onedelivery);

                // read next line before looping
                // if end of file reached, line would be null
                line = br.readLine();
            }

        }catch (FileNotFoundException f){
            System.out.println("filenotfound");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return delivery1;
    }
    private static Delivery createDeliveries(String[] metadata) {
        //System.out.println(metadata[0]);
        int id =Integer.parseInt(metadata[0]);
        String bowling_team = metadata[3];
        int extra_runs = Integer.parseInt(metadata[16]);
        int total_runs = Integer.parseInt(metadata[17]);
        String bowler = metadata[8];



        // create and return book of this metadata
        return new Delivery(id, bowling_team, extra_runs,bowler,total_runs);

    }

}


