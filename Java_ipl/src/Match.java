
public class Match{
    private int id;
    private int season;
    private String team1;
    private String team2;
    private String winners;

    Match(int id, int season, String team1, String team2, String winners) {
        this.id = id;
        this.season = season;
        this.team1 = team1;
        this.team2 = team2;
        this.winners = winners;

    }

    public int getId() {
        return id;
    }

    public int getSeason() {
        return season;
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public String getWinners() {
        return winners;
    }



    @Override
    public String toString() {
        return "Match [id=" + id + ", season=" + season + ", team1=" + team1+", team2="+team2+", winner"+winners+
                "]";
    }

}

