import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.sql.*;

public class MatchScorecardController {

    @FXML
    private TabPane inningsTabs;

    private Connection conn;

    @FXML
    public void initialize() {
        try {
            conn = DBConnect.getConnection();
            loadMatchScorecard();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadMatchScorecard() {
        try (Statement stmt = conn.createStatement()) {
            ResultSet inningsRs = stmt.executeQuery("SELECT DISTINCT innings_no FROM balls ORDER BY innings_no");

            while (inningsRs.next()) {
                int inningsNo = inningsRs.getInt("innings_no");

                VBox content = new VBox(20);
                content.setStyle("-fx-padding: 20;");

                TableView<ObservableList<String>> battingTable = createTable("SELECT p.player_name, SUM(b.runs_scored) AS runs FROM balls b JOIN players p ON b.batsman_id = p.player_id WHERE innings_no = " + inningsNo + " GROUP BY p.player_name");

                TableView<ObservableList<String>> bowlingTable = createTable("SELECT p.player_name, COUNT(*) AS balls, SUM(b.runs_scored) AS runs_conceded FROM balls b JOIN players p ON b.bowler_id = p.player_id WHERE innings_no = " + inningsNo + " GROUP BY p.player_name");

                Label battingLabel = new Label("Batting Summary");
                battingLabel.setStyle("-fx-font-size: 18; -fx-text-fill: #FFBF00; -fx-font-weight: bold;");
                Label bowlingLabel = new Label("Bowling Summary");
                bowlingLabel.setStyle("-fx-font-size: 18; -fx-text-fill: #FFBF00; -fx-font-weight: bold;");

                content.getChildren().addAll(battingLabel, battingTable, bowlingLabel, bowlingTable);

                Tab tab = new Tab("Innings " + inningsNo);
                tab.setContent(content);
                inningsTabs.getTabs().add(tab);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private TableView<ObservableList<String>> createTable(String sql) {
        TableView<ObservableList<String>> table = new TableView<>();
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
            table.getColumns().clear();

            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int colIndex = i;
                TableColumn<ObservableList<String>, String> col = new TableColumn<>(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(row -> new javafx.beans.property.SimpleStringProperty(row.getValue().get(colIndex)));
                table.getColumns().add(col);
            }

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }

            table.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return table;
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
