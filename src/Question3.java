
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.*;
import java.util.TreeMap;

public class Question3 {

    public static void main(String[] args) {

        List<Delivery> Deliverydata = readDeliveriesFromCSV("/home/lokesh/Mountblue/Ipl_project/ipldatajava_project/deliveries.csv");
        List<Matches> Match = readMatchesFromCSV("/home/lokesh/Mountblue/Ipl_project/ipldatajava_project/matches.csv");
        // let's print all the person read from CSV file

        List<Integer> match_id = new ArrayList<Integer> ();
        List<Integer> match_id2015 = new ArrayList<Integer>();
        for (Matches m : Match){
            if (m.getSeason()==2016){
                match_id.add(m.getId());
            }
            if (m.getSeason()==2015){
                match_id2015.add(m.getId());
            }

        }
        //System.out.println(match_id);
        HashMap<String,Integer> hash = new HashMap<>();
        for (Delivery d : Deliverydata) {
            //  System.out.println(d.getId()+"  "+d.getBowling_team()+" "+d.getExtra_runs());
            if (match_id.contains(d.getId())){
                if (hash.containsKey(d.getBowling_team())){
                    int runs=hash.get(d.getBowling_team());
                    hash.put(d.getBowling_team(),d.getExtra_runs()+runs);

                }else{
                    hash.put(d.getBowling_team(),d.getExtra_runs());
                }
            }

        }
        System.out.println("***************Extra runs conceded by each team in 2016**********************");
        //System.out.print(hash);
        for ( Entry<String, Integer> entry : hash.entrySet()) {
            String team = entry.getKey();
            int extraruns = entry.getValue();
            System.out.println(team+"------------"+extraruns);

        }

        HashMap<String, Integer> truns = new HashMap<String, Integer>();
        HashMap<String, Integer> tballs = new HashMap<String, Integer>();
        for(Delivery d : Deliverydata){
            if( match_id2015.contains(d.getId())){
                if(truns.containsKey(d.getBowler())){
                    //System.out.println(d.getTotal_runs());
                    int r=truns.get(d.getBowler());
                    int total_r=r+d.getTotal_runs();
                    truns.put(d.getBowler(),total_r);
                }
                else{
                    truns.put(d.getBowler(),d.getTotal_runs());
                }
                if(tballs.containsKey(d.getBowler())){
                    int b =tballs.get(d.getBowler());
                    tballs.put(d.getBowler(),b+1);
                }else{
                    tballs.put(d.getBowler(),1);
                }

            }
        }
        //System.out.println(truns);
        //System.out.println(tballs);
        TreeMap<Double,String> economy = new TreeMap<Double,String>();
        for ( Entry<String, Integer> entry : truns.entrySet()) {
            String bowlr = entry.getKey();
            int x = entry.getValue();
            // do something with key and/or tab
            //System.out.println(bowlr+" "+x);
            double runs= truns.get(bowlr);
            double balls=tballs.get(bowlr);
            double eco=(runs*6)/balls;

            economy.put(eco,bowlr);

        }
        //System.out.println(economy);
        int count=1;
        System .out.println("*********Top Economy Bowlers***********");
        for ( Entry<Double, String> entry : economy.entrySet()) {
            Double eco = entry.getKey();
            String bowlr = entry.getValue();
            if (count<=10){
                System .out.println(bowlr.trim() +"---------------"+eco);
            }
            count+=1;

            economy.put(eco,bowlr);

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
            //System.out.println("buffer working");
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
