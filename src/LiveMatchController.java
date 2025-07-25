import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.util.Duration;
import java.io.IOException;

import java.sql.*;
import java.util.*;

public class LiveMatchController {

    @FXML private ComboBox<String> matchSelector;
    @FXML private Label scoreLabel, overLabel, wicketLabel;
    @FXML private TextArea commentaryArea;

    private final Map<String, Integer> matchMap = new HashMap<>();
    private List<BallData> balls = new ArrayList<>();
    private Timeline timeline;
    private int currentBallIndex = 0;
    private int totalRuns = 0;
    private int totalWickets = 0;

    static class BallData {
        int over, ball, runs;
        boolean isWicket;
        String batsman, bowler;

        BallData(int over, int ball, int runs, boolean isWicket, String batsman, String bowler) {
            this.over = over;
            this.ball = ball;
            this.runs = runs;
            this.isWicket = isWicket;
            this.batsman = batsman;
            this.bowler = bowler;
        }
    }

    @FXML
    public void initialize() {
        try (Connection conn = DBConnect.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("""
                SELECT m.match_id, t1.team_name AS team1, t2.team_name AS team2, TO_CHAR(m.match_date, 'YYYY-MM-DD') AS match_date
                FROM matches m
                JOIN teams t1 ON m.team1_id = t1.team_id
                JOIN teams t2 ON m.team2_id = t2.team_id
                """)) {
            while (rs.next()) {
                int matchId = rs.getInt("match_id");
                String label = "Match " + matchId + ": " + rs.getString("team1") + " vs " + rs.getString("team2") + " (" + rs.getString("match_date") + ")";
                matchMap.put(label, matchId);
                matchSelector.getItems().add(label);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void startSimulation() {
        String selected = matchSelector.getValue();
        if (selected == null || !matchMap.containsKey(selected)) {
            commentaryArea.setText("Please select a valid match.");
            return;
        }

        int matchId = matchMap.get(selected);
        loadBalls(matchId);

        if (balls.isEmpty()) {
            commentaryArea.setText("No ball-by-ball data found for this match.");
            return;
        }

        resetSimulation();
        startTimeline();
    }

    private void loadBalls(int matchId) {
        balls.clear();
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement("""
                SELECT b.over_no, b.ball_no, b.runs_scored, b.is_wicket,
                       bat.player_name AS batsman, bowl.player_name AS bowler
                FROM balls b
                JOIN players bat ON b.batsman_id = bat.player_id
                JOIN players bowl ON b.bowler_id = bowl.player_id
                WHERE b.match_id = ?
                ORDER BY b.over_no, b.ball_no
            """)) {
            stmt.setInt(1, matchId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                balls.add(new BallData(
                        rs.getInt("over_no"),
                        rs.getInt("ball_no"),
                        rs.getInt("runs_scored"),
                        "Y".equals(rs.getString("is_wicket")),
                        rs.getString("batsman"),
                        rs.getString("bowler")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void resetSimulation() {
        totalRuns = 0;
        totalWickets = 0;
        currentBallIndex = 0;
        scoreLabel.setText("Score: 0");
        overLabel.setText("Over: 0.0");
        wicketLabel.setText("Wickets: 0");
        commentaryArea.clear();
    }

    private void startTimeline() {
        if (timeline != null) timeline.stop();

        timeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> playNextBall()));
        timeline.setCycleCount(balls.size());
        timeline.play();
    }

    private void playNextBall() {
        if (currentBallIndex >= balls.size()) return;

        BallData b = balls.get(currentBallIndex++);
        totalRuns += b.runs;
        if (b.isWicket) totalWickets++;

        scoreLabel.setText("Score: " + totalRuns);
        overLabel.setText("Over: " + b.over + "." + b.ball);
        wicketLabel.setText("Wickets: " + totalWickets);

        commentaryArea.appendText("Over " + b.over + "." + b.ball + ": " +
                b.batsman + " faced " + b.bowler + " - " +
                (b.isWicket ? "OUT!" : b.runs + " run(s)") + "\n");
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
