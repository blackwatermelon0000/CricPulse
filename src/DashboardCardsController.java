import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import java.io.IOException;
import java.sql.*;

public class DashboardCardsController {
    @FXML private Label topScorerLabel, wicketsLabel, runRateLabel;

    @FXML public void initialize() {
        try {
            fetchTopScorer();
            fetchWickets();
            fetchRunRate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchTopScorer() {
        String q = """
            SELECT p.player_name, SUM(b.runs_scored) AS total_runs
            FROM balls b JOIN players p ON b.batsman_id = p.player_id
            GROUP BY p.player_name ORDER BY total_runs DESC FETCH FIRST 1 ROWS ONLY
        """;
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(q);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next())
                topScorerLabel.setText(rs.getString("player_name") + " (" + rs.getInt("total_runs") + ")");
            else topScorerLabel.setText("No data");
        } catch (Exception e) {
            topScorerLabel.setText("DB Error");
            e.printStackTrace();
        }
    }

    private void fetchWickets() {
        String q = "SELECT COUNT(*) AS total_wickets FROM balls WHERE is_wicket = 'Y'";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(q);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next())
                wicketsLabel.setText(rs.getString("total_wickets"));
            else wicketsLabel.setText("0");
        } catch (Exception e) {
            wicketsLabel.setText("DB Error");
            e.printStackTrace();
        }
    }

    private void fetchRunRate() {
        String q = "SELECT SUM(runs_scored) AS total_runs, COUNT(DISTINCT over_no) AS overs FROM balls";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(q);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                double runs = rs.getDouble("total_runs");
                int overs = rs.getInt("overs");
                runRateLabel.setText(String.format("%.2f", overs > 0 ? runs / overs : 0));
            } else {
                runRateLabel.setText("0.00");
            }
        } catch (Exception e) {
            runRateLabel.setText("DB Error");
            e.printStackTrace(); // Make sure this line is present to see the cause in console
        }
    }

    @FXML private void goHome() {
        try {
            Node home = FXMLLoader.load(getClass().getResource("welcome.fxml"));
            MainDashboardController.getContentPaneStatic().getChildren().setAll(home);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
