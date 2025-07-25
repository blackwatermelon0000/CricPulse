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

public class TopWicketTakerController {

    @FXML private TableView<TopWicketTaker> table;
    @FXML private TableColumn<TopWicketTaker, String> playerCol;
    @FXML private TableColumn<TopWicketTaker, Integer> wicketsCol;

    @FXML
    public void initialize() {
        playerCol.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        wicketsCol.setCellValueFactory(new PropertyValueFactory<>("wickets"));
        loadData();
    }

    private void loadData() {
        ObservableList<TopWicketTaker> data = FXCollections.observableArrayList();
        String q = "SELECT p.player_name, COUNT(*) AS wickets " +
                "FROM balls b JOIN players p ON b.bowler_id = p.player_id " +
                "WHERE b.is_wicket = 'Y' " +
                "GROUP BY p.player_name ORDER BY wickets DESC";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(q);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                data.add(new TopWicketTaker(rs.getString("player_name"), rs.getInt("wickets")));
            }

            table.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class TopWicketTaker {
        private final String playerName;
        private final int wickets;

        public TopWicketTaker(String playerName, int wickets) {
            this.playerName = playerName;
            this.wickets = wickets;
        }

        public String getPlayerName() { return playerName; }
        public int getWickets() { return wickets; }
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
}
