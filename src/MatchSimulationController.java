import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.Node;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class MatchSimulationController {

    @FXML private ComboBox<String> matchSelector;
    @FXML private TextArea resultArea;

    private final Map<String, Integer> matchMap = new HashMap<>();

    @FXML
    public void initialize() {
        try (Connection conn = DBConnect.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT m.match_id, t1.team_name AS team1, t2.team_name AS team2, TO_CHAR(m.match_date, 'YYYY-MM-DD') AS match_date " +
                             "FROM matches m JOIN teams t1 ON m.team1_id = t1.team_id JOIN teams t2 ON m.team2_id = t2.team_id")
        ) {
            while (rs.next()) {
                int matchId = rs.getInt("match_id");
                String label = "Match " + matchId + " - " + rs.getString("team1") + " vs " + rs.getString("team2") + " (" + rs.getString("match_date") + ")";
                matchMap.put(label, matchId);
                matchSelector.getItems().add(label);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSimulate() {
        String selected = matchSelector.getValue();
        if (selected == null || !matchMap.containsKey(selected)) {
            resultArea.setText("Please select a valid match.");
            return;
        }

        int matchId = matchMap.get(selected);

        String query = """
            SELECT t1.team_name AS team1, t2.team_name AS team2,
                   i1.total_runs AS team1_score,
                   i2.total_runs AS team2_score,
                   w.team_name AS winner
            FROM matches m
            JOIN teams t1 ON m.team1_id = t1.team_id
            JOIN teams t2 ON m.team2_id = t2.team_id
            LEFT JOIN teams w ON m.winner_team_id = w.team_id
            JOIN innings i1 ON m.match_id = i1.match_id AND i1.batting_team_id = m.team1_id
            JOIN innings i2 ON m.match_id = i2.match_id AND i2.batting_team_id = m.team2_id
            WHERE m.match_id = ?
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, matchId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String team1 = rs.getString("team1");
                String team2 = rs.getString("team2");
                int team1Score = rs.getInt("team1_score");
                int team2Score = rs.getInt("team2_score");
                String winner = rs.getString("winner");
                resultArea.setText(String.format("""
                    🔁 Match ID: %d
                    %s: %d runs
                    %s: %d runs
                    Winner: %s
                    """, matchId, team1, team1Score, team2, team2Score, winner != null ? winner : "No result"));
            } else {
                resultArea.setText("Match data not found.");
            }

        } catch (SQLException e) {
            resultArea.setText("Error fetching match data.");
            e.printStackTrace();
        }
    }

    @FXML
    private void goHome() {
        try {
            Parent home = FXMLLoader.load(getClass().getResource("welcome.fxml"));
            MainDashboardController.getContentPaneStatic().getChildren().setAll(home);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
