import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.sql.*;

public class PlayerComparisonController {

    @FXML private ComboBox<String> player1Combo;
    @FXML private ComboBox<String> player2Combo;
    @FXML private GridPane comparisonGrid;

    private Connection conn;

    @FXML
    public void initialize() {
        try {
            conn = DBConnect.getConnection();
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery("SELECT player_name FROM players");
            while (rs.next()) {
                String name = rs.getString("player_name");
                player1Combo.getItems().add(name);
                player2Combo.getItems().add(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCompare() {
        String p1 = player1Combo.getValue();
        String p2 = player2Combo.getValue();
        if (p1 == null || p2 == null) return;

        comparisonGrid.getChildren().clear();
        addHeader();
        addStatsRow("Total Runs", p1, p2, "SELECT player_name, SUM(runs_scored) FROM balls JOIN players ON batsman_id = player_id WHERE player_name = ? GROUP BY player_name");
        addStatsRow("Wickets", p1, p2, "SELECT player_name, COUNT(*) FROM balls JOIN players ON bowler_id = player_id WHERE is_wicket = 'Y' AND player_name = ? GROUP BY player_name");
        addStatsRow("Strike Rate", p1, p2, "SELECT player_name, ROUND(SUM(runs_scored)/(COUNT(*))*100,2) FROM balls JOIN players ON batsman_id = player_id WHERE player_name = ? GROUP BY player_name");
    }

    private void addHeader() {
        Label metric = new Label("Metric");
        Label p1 = new Label("Player 1");
        Label p2 = new Label("Player 2");
        metric.getStyleClass().add("metric-label");
        p1.getStyleClass().add("metric-label");
        p2.getStyleClass().add("metric-label");

        comparisonGrid.add(metric, 0, 0);
        comparisonGrid.add(p1, 1, 0);
        comparisonGrid.add(p2, 2, 0);
    }


    private void addStatsRow(String label, String p1, String p2, String sql) {
        try {
            var ps = conn.prepareStatement(sql);
            ps.setString(1, p1);
            var rs1 = ps.executeQuery();
            String val1 = rs1.next() ? rs1.getString(2) : "0";

            ps.setString(1, p2);
            var rs2 = ps.executeQuery();
            String val2 = rs2.next() ? rs2.getString(2) : "0";

            int row = comparisonGrid.getRowCount();
            comparisonGrid.add(new Label(label), 0, row);
            comparisonGrid.add(new Label(val1), 1, row);
            comparisonGrid.add(new Label(val2), 2, row);
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
