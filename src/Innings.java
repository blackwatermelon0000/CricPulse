public class Innings {
    private int id;
    private int matchId;
    private String battingTeam;
    private String bowlingTeam;
    private int totalRuns;
    private int wickets;
    private double overs;

    public Innings(int id, int matchId, String battingTeam, String bowlingTeam, int totalRuns, int wickets, double overs) {
        this.id = id;
        this.matchId = matchId;
        this.battingTeam = battingTeam;
        this.bowlingTeam = bowlingTeam;
        this.totalRuns = totalRuns;
        this.wickets = wickets;
        this.overs = overs;
    }

    public int getId() { return id; }
    public int getMatchId() { return matchId; }
    public String getBattingTeam() { return battingTeam; }
    public String getBowlingTeam() { return bowlingTeam; }
    public int getTotalRuns() { return totalRuns; }
    public int getWickets() { return wickets; }
    public double getOvers() { return overs; }
}
