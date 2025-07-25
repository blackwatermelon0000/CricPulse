import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.*;

public class TeamManagementController {

    @FXML private TextField nameField, countryField, coachField;
    @FXML private TableView<Team> teamTable;
    @FXML private TableColumn<Team, Integer> idCol;
    @FXML private TableColumn<Team, String> nameCol, countryCol, coachCol;

    private Connection conn;
    private final ObservableList<Team> teamList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        try {
            conn = DBConnect.getConnection();
            setupTable();
            loadTeams();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupTable() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        coachCol.setCellValueFactory(new PropertyValueFactory<>("coach"));
        teamTable.setItems(teamList);

    }

    private void loadTeams() throws SQLException {
        teamList.clear();
        String sql = "SELECT * FROM teams ORDER BY team_id";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                teamList.add(new Team(
                        rs.getInt("team_id"),
                        rs.getString("team_name"),
                        rs.getString("country"),
                        rs.getString("coach")
                ));
            }
        }
    }

    @FXML
    private void addTeam() {
        String name = nameField.getText();
        String country = countryField.getText();
        String coach = coachField.getText();
        if (name.isEmpty() || country.isEmpty()) return;

        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO teams (team_name, country, coach) VALUES (?, ?, ?)");
            stmt.setString(1, name);
            stmt.setString(2, country);
            stmt.setString(3, coach);
            stmt.executeUpdate();
            loadTeams();
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clearForm() {
        nameField.clear();
        countryField.clear();
        coachField.clear();
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
