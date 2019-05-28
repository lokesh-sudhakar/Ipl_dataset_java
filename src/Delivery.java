
public class Delivery {

    private int id;
    private String bowling_team;
    private int extra_runs;
    private String bowler;
    private int total_runs;


    public int getTotal_runs() {
        return total_runs;
    }

    public int getId() {
        return id;
    }
    public String getBowler() {
        return bowler;
    }

    public String getBowling_team() {
        return bowling_team;
    }

    public int getExtra_runs() {
        return extra_runs;
    }

    public Delivery(int id, String bowling_team, int extra_runs, String bowler,int total_runs) {
        this.id = id;
        this.bowling_team = bowling_team;
        this.extra_runs = extra_runs;
        this.bowler=bowler;
        this.total_runs=total_runs;
    }
}
