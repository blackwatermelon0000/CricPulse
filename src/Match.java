public class Match {
    private int matchId;
    private String tournament;
    private String team1;
    private String team2;
    private String matchDate;
    private String venue;

    public Match(int matchId, String tournament, String team1, String team2, String matchDate, String venue) {
        this.matchId = matchId;
        this.tournament = tournament;
        this.team1 = team1;
        this.team2 = team2;
        this.matchDate = matchDate;
        this.venue = venue;
    }

    public int getMatchId() { return matchId; }
    public String getTournament() { return tournament; }
    public String getTeam1() { return team1; }
    public String getTeam2() { return team2; }
    public String getMatchDate() { return matchDate; }
    public String getVenue() { return venue; }
}
