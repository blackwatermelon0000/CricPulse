
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import java.sql.*;

public class AnalyticsController {
    @FXML private ComboBox<String> modeCombo;
    @FXML private StackPane chartPane;
    private Connection conn;

    @FXML
    public void initialize() {
        try {
            conn = DBConnect.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        modeCombo.getItems().addAll("Top Scorers", "Wicket Breakdown", "Run Progression", "Toss Impact", "Extras by Match");
        modeCombo.setOnAction(e -> updateChart(modeCombo.getValue()));
    }

    private void updateChart(String mode) {
        chartPane.getChildren().clear();
        switch(mode) {
            case "Top Scorers":        chartPane.getChildren().add(buildTopScorersChart()); break;
            case "Wicket Breakdown":   chartPane.getChildren().add(buildWicketPieChart());  break;
            case "Run Progression":    chartPane.getChildren().add(buildRunProgressLine()); break;
            case "Toss Impact":        chartPane.getChildren().add(buildTossImpactChart()); break;
            case "Extras by Match":    chartPane.getChildren().add(buildExtrasChart());     break;
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

    private BarChart<String, Number> buildTopScorersChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Player");
        yAxis.setLabel("Runs");
        xAxis.setTickLabelRotation(0);

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Top 5 Run Scorers");
        chart.setLegendVisible(false);
        chart.setPrefSize(850, 500);
        chart.getStyleClass().add("chart-box");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        String query = "SELECT p.player_name, SUM(b.runs_scored) AS runs FROM balls b " +
                "JOIN players p ON b.batsman_id = p.player_id " +
                "GROUP BY p.player_name ORDER BY runs DESC FETCH FIRST 5 ROWS ONLY";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                series.getData().add(new XYChart.Data<>(rs.getString("player_name"), rs.getInt("runs")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        chart.getData().add(series);
        return chart;
    }

    private PieChart buildWicketPieChart() {
        PieChart chart = new PieChart();
        chart.setTitle("Dismissal Breakdown");
        chart.setLabelsVisible(true);
        chart.setClockwise(true);
        chart.setStartAngle(90);
        chart.setPrefSize(600, 500);
        chart.getStyleClass().add("chart-box");

        String query = "SELECT dismissal_type, COUNT(*) AS total FROM balls WHERE is_wicket = 'Y' GROUP BY dismissal_type";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                chart.getData().add(new PieChart.Data(rs.getString("dismissal_type"), rs.getInt("total")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return chart;
    }

    private LineChart<Number, Number> buildRunProgressLine() {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Over");
        yAxis.setLabel("Runs");

        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Run Progression by Over");
        chart.setLegendVisible(false);
        chart.setCreateSymbols(true);
        chart.setPrefSize(900, 500);
        chart.getStyleClass().add("chart-box");

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("All Matches Combined");

        String query = "SELECT over_no, SUM(runs_scored) AS runs FROM balls GROUP BY over_no ORDER BY over_no";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                series.getData().add(new XYChart.Data<>(rs.getInt("over_no"), rs.getInt("runs")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        chart.getData().add(series);
        return chart;
    }

    private PieChart buildTossImpactChart() {
        PieChart chart = new PieChart();
        chart.setTitle("Match Wins by Toss Winner");
        chart.setLabelsVisible(true);
        chart.setClockwise(true);
        chart.setStartAngle(90);
        chart.setPrefSize(600, 500);
        chart.getStyleClass().add("chart-box");

        String query = "SELECT winner_team_id, COUNT(*) AS count FROM matches GROUP BY winner_team_id";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int teamId = rs.getInt("winner_team_id");
                String teamName = getTeamNameById(teamId);
                chart.getData().add(new PieChart.Data(teamName, rs.getInt("count")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return chart;
    }

    private BarChart<String, Number> buildExtrasChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Match ID");
        yAxis.setLabel("Total Extras");

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Extras per Match");
        chart.setLegendVisible(false);
        chart.setPrefSize(850, 500);
        chart.getStyleClass().add("chart-box");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Extras");

        String query = "SELECT b.match_id, SUM(NVL(e.runs, 0)) AS extras " +
                "FROM balls b LEFT JOIN extras e ON b.ball_id = e.ball_id " +
                "GROUP BY b.match_id ORDER BY b.match_id";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                series.getData().add(new XYChart.Data<>(rs.getString("match_id"), rs.getInt("extras")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        chart.getData().add(series);
        return chart;
    }

    private String getTeamNameById(int teamId) {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT team_name FROM teams WHERE team_id = ?")) {
            stmt.setInt(1, teamId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("team_name");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Unknown";
    }
}
