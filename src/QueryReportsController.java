import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleObjectProperty;
import java.sql.*;
import java.util.*;

public class QueryReportsController {

    @FXML private ComboBox<String> querySelector;
    @FXML private TableView<Map<String, Object>> resultTable;

    private static final Map<String, String> queryMap = new LinkedHashMap<>();

    static {
        queryMap.put("Top Run Scorers", "SELECT p.player_name, SUM(b.runs_scored) AS total_runs FROM balls b JOIN players p ON b.batsman_id = p.player_id GROUP BY p.player_name ORDER BY total_runs DESC");
        queryMap.put("Top Wicket Takers", "SELECT p.player_name, COUNT(*) AS wickets FROM balls b JOIN players p ON b.bowler_id = p.player_id WHERE b.is_wicket = 'Y' GROUP BY p.player_name ORDER BY wickets DESC");
        queryMap.put("Match Winners", "SELECT m.match_id, t.team_name AS winner FROM matches m JOIN teams t ON m.winner_team_id = t.team_id");
        // Add more queries as needed
    }

    @FXML
    public void initialize() {
        querySelector.setItems(FXCollections.observableArrayList(queryMap.keySet()));
    }

    @FXML
    private void runSelectedQuery() {
        resultTable.getColumns().clear();
        resultTable.getItems().clear();

        String selectedKey = querySelector.getValue();
        if (selectedKey == null) return;

        String sql = queryMap.get(selectedKey);
        try (Connection conn = DBConnect.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                final String columnName = meta.getColumnLabel(i);
                TableColumn<Map<String, Object>, Object> column = new TableColumn<>(columnName);
                column.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().get(columnName)));
                resultTable.getColumns().add(column);
            }
            ObservableList<Map<String, Object>> data = FXCollections.observableArrayList();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(meta.getColumnLabel(i), rs.getObject(i));
                }
                data.add(row);
            }
            resultTable.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goHome() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("welcome.fxml"));
            MainDashboardController.getContentPaneStatic().getChildren().setAll(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
