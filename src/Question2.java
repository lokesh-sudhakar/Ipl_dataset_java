
import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Question2 {

    public static void main(String[] args) {

        List<Matches> Match = readMatchesFromCSV("/home/lokesh/Mountblue/Ipl_project/ipldatajava_project/matches.csv");


        HashMap<String,Integer>  hm1= new HashMap<String,Integer>();
        TreeMap<Integer,HashMap<String,Integer>> hm = new TreeMap<Integer,HashMap<String,Integer>>();
        for (Matches m : Match) {
            if(hm.containsKey(m.getSeason())){


                if (hm1.containsKey(m.getWinners())) {
                    int count = hm1.get(m.getWinners());
                    hm1.put(m.getWinners(), count + 1);

                } else {
                    hm1.put(m.getWinners(),1);
                }
                hm1.remove("");
                hm.put(m.getSeason(),hm1);
            }
            else{
                hm1 = new HashMap<>();
                hm.put(m.getSeason(),hm1);

            }

        }
        hm1.remove("");
        //System.out.println(hm);
        for ( Map.Entry<Integer,HashMap<String,Integer>> entry : hm.entrySet()) {
            int season = entry.getKey();
            HashMap<String,Integer> iner= entry.getValue();
            System.out.println(season+"********");
            for ( Map.Entry<String,Integer> data : iner.entrySet()) {
                String team = data.getKey();
                int won= data.getValue();
                System.out.println(team +" won "+won+" matches");
            }
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
            System.out.println("buffer working");
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
}