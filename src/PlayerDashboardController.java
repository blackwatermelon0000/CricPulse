import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.*;

public class PlayerDashboardController {

    @FXML private ComboBox<String> playerSelector;
    @FXML private Label totalRunsLabel;
    @FXML private Label totalWicketsLabel;
    @FXML private Label strikeRateLabel;
    @FXML private Label averageLabel;

    private Connection conn;

    @FXML
    public void initialize() {
        try {
            conn = DBConnect.getConnection();
            loadPlayers();

            playerSelector.setOnAction(e -> {
                String playerName = playerSelector.getValue();
                if (playerName != null) {
                    loadPlayerStats(playerName);
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadPlayers() throws SQLException {
        ObservableList<String> players = FXCollections.observableArrayList();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT player_name FROM players ORDER BY player_name")) {
            while (rs.next()) {
                players.add(rs.getString("player_name"));
            }
        }
        playerSelector.setItems(players);
    }

    private void loadPlayerStats(String playerName) {
        try {
            String sql = "SELECT p.player_id, SUM(CASE WHEN b.batsman_id = p.player_id THEN b.runs_scored ELSE 0 END) AS runs, " +
                    "SUM(CASE WHEN b.bowler_id = p.player_id AND b.is_wicket = 'Y' THEN 1 ELSE 0 END) AS wickets, " +
                    "ROUND(SUM(CASE WHEN b.batsman_id = p.player_id THEN b.runs_scored ELSE 0 END) * 100.0 / COUNT(CASE WHEN b.batsman_id = p.player_id THEN 1 END), 2) AS strike_rate, " +
                    "ROUND(AVG(CASE WHEN b.batsman_id = p.player_id THEN b.runs_scored END), 2) AS average " +
                    "FROM players p LEFT JOIN balls b ON p.player_id = b.batsman_id OR p.player_id = b.bowler_id " +
                    "WHERE p.player_name = ? GROUP BY p.player_id";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, playerName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                totalRunsLabel.setText(String.valueOf(rs.getInt("runs")));
                totalWicketsLabel.setText(String.valueOf(rs.getInt("wickets")));
                strikeRateLabel.setText(String.valueOf(rs.getDouble("strike_rate")));
                averageLabel.setText(String.valueOf(rs.getDouble("average")));
            } else {
                totalRunsLabel.setText("0");
                totalWicketsLabel.setText("0");
                strikeRateLabel.setText("0.0");
                averageLabel.setText("0.0");
            }
        } catch (SQLException e) {
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
