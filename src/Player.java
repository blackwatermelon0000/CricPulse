public class Player {
    private int id;
    private String name;
    private int age;
    private String team;
    private String batting;
    private String bowling;
    private String role;

    public Player(int id, String name, int age, String team, String batting, String bowling, String role) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.team = team;
        this.batting = batting;
        this.bowling = bowling;
        this.role = role;
    }

    // Getters (required for TableView binding)
    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getTeam() { return team; }
    public String getBatting() { return batting; }
    public String getBowling() { return bowling; }
    public String getRole() { return role; }
}
