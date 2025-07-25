import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TopScorerController {

    @FXML private TableView<PlayerStat> scorerTable;
    @FXML private TableColumn<PlayerStat, String> nameCol;
    @FXML private TableColumn<PlayerStat, Integer> runsCol;

    @FXML
    public void initialize() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        runsCol.setCellValueFactory(new PropertyValueFactory<>("totalRuns"));
        loadTopScorers();
    }

    private void loadTopScorers() {
        ObservableList<PlayerStat> data = FXCollections.observableArrayList();

        String query = "SELECT p.player_name, SUM(b.runs_scored) AS total_runs " +
                "FROM balls b JOIN players p ON b.batsman_id = p.player_id " +
                "GROUP BY p.player_name ORDER BY total_runs DESC";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("player_name");
                int runs = rs.getInt("total_runs");
                data.add(new PlayerStat(name, runs));
            }

            scorerTable.setItems(data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleBack() {
        // TODO: implement navigation back to main dashboard
        System.out.println("Back clicked");
    }

    // Simple POJO class
    public static class PlayerStat {
        private final String playerName;
        private final int totalRuns;

        public PlayerStat(String playerName, int totalRuns) {
            this.playerName = playerName;
            this.totalRuns = totalRuns;
        }

        public String getPlayerName() {
            return playerName;
        }

        public int getTotalRuns() {
            return totalRuns;
        }
    }
    @FXML
    private void goHome() {
        try {
            Node welcomeView = FXMLLoader.load(getClass().getResource("welcome.fxml"));
            MainDashboardController.getContentPaneStatic().getChildren().setAll(welcomeView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
