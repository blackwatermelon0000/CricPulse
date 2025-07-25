import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PointsTableController {

    @FXML private TableView<TeamStanding> pointsTable;
    @FXML private TableColumn<TeamStanding, String> teamCol;
    @FXML private TableColumn<TeamStanding, Integer> playedCol;
    @FXML private TableColumn<TeamStanding, Integer> winCol;
    @FXML private TableColumn<TeamStanding, Integer> lossCol;
    @FXML private TableColumn<TeamStanding, Integer> pointsCol;

    @FXML
    public void initialize() {
        teamCol.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        playedCol.setCellValueFactory(new PropertyValueFactory<>("matchesPlayed"));
        winCol.setCellValueFactory(new PropertyValueFactory<>("wins"));
        lossCol.setCellValueFactory(new PropertyValueFactory<>("losses"));
        pointsCol.setCellValueFactory(new PropertyValueFactory<>("points"));

        loadPointsTable();
    }

    private void loadPointsTable() {
        ObservableList<TeamStanding> data = FXCollections.observableArrayList();

        String query = """
                SELECT t.team_name,
                       COUNT(m.match_id) AS matches_played,
                       SUM(CASE WHEN m.winner_team_id = t.team_id THEN 1 ELSE 0 END) AS wins,
                       SUM(CASE WHEN m.winner_team_id != t.team_id AND m.winner_team_id IS NOT NULL THEN 1 ELSE 0 END) AS losses,
                       SUM(CASE WHEN m.winner_team_id = t.team_id THEN 2 ELSE 0 END) AS points
                FROM teams t
                LEFT JOIN matches m ON t.team_id IN (m.team1_id, m.team2_id)
                GROUP BY t.team_name
                ORDER BY points DESC
                """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String team = rs.getString("team_name");
                int played = rs.getInt("matches_played");
                int wins = rs.getInt("wins");
                int losses = rs.getInt("losses");
                int points = rs.getInt("points");

                data.add(new TeamStanding(team, played, wins, losses, points));
            }

            pointsTable.setItems(data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goHome() {
        try {
            Node home = FXMLLoader.load(getClass().getResource("welcome.fxml"));
            MainDashboardController.getContentPaneStatic().getChildren().setAll(home);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class TeamStanding {
        private final String teamName;
        private final int matchesPlayed;
        private final int wins;
        private final int losses;
        private final int points;

        public TeamStanding(String teamName, int matchesPlayed, int wins, int losses, int points) {
            this.teamName = teamName;
            this.matchesPlayed = matchesPlayed;
            this.wins = wins;
            this.losses = losses;
            this.points = points;
        }

        public String getTeamName() { return teamName; }
        public int getMatchesPlayed() { return matchesPlayed; }
        public int getWins() { return wins; }
        public int getLosses() { return losses; }
        public int getPoints() { return points; }
    }
}
