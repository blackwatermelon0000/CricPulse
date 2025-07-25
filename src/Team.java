public class Team {
    private int id;
    private String name;
    private String country;
    private String coach;

    public Team(int id, String name, String country, String coach) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.coach = coach;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCountry() { return country; }
    public String getCoach() { return coach; }
}
