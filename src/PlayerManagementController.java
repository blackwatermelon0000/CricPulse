import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.*;

public class PlayerManagementController {

    @FXML private TableView<Player> playerTable;
    @FXML private TableColumn<Player, Integer> idCol, ageCol;
    @FXML private TableColumn<Player, String> nameCol, teamCol, battingCol, bowlingCol, roleCol;

    private Connection conn;

    @FXML
    public void initialize() {
        try {
            conn = DBConnect.getConnection();
            setupTable();
            loadPlayers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupTable() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
        teamCol.setCellValueFactory(new PropertyValueFactory<>("team"));
        battingCol.setCellValueFactory(new PropertyValueFactory<>("batting"));
        bowlingCol.setCellValueFactory(new PropertyValueFactory<>("bowling"));
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
    }

    private void loadPlayers() throws SQLException {
        ObservableList<Player> players = FXCollections.observableArrayList();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT player_id, player_name, age, " +
                "(SELECT team_name FROM teams WHERE team_id = p.team_id), batting_style, bowling_style, role FROM players p");

        while (rs.next()) {
            players.add(new Player(
                    rs.getInt(1), rs.getString(2), rs.getInt(3),
                    rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)
            ));
        }
        playerTable.setItems(players);
    }

    @FXML
    private void goHome() {
        try {
            Parent home = FXMLLoader.load(getClass().getResource("welcome.fxml"));
            MainDashboardController.getContentPaneStatic().getChildren().setAll(home);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
