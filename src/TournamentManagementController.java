import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.*;

public class TournamentManagementController {

    @FXML private TextField nameField, yearField;
    @FXML private TableView<Tournament> tournamentTable;
    @FXML private TableColumn<Tournament, Integer> idCol;
    @FXML private TableColumn<Tournament, String> nameCol;
    @FXML private TableColumn<Tournament, Integer> yearCol;

    private Connection conn;
    private final ObservableList<Tournament> tournamentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        try {
            conn = DBConnect.getConnection();
            setupTable();
            loadTournaments();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupTable() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        tournamentTable.setItems(tournamentList);
    }

    private void loadTournaments() throws SQLException {
        tournamentList.clear();
        String sql = "SELECT * FROM tournaments ORDER BY tournament_id";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tournamentList.add(new Tournament(
                        rs.getInt("tournament_id"),
                        rs.getString("tournament_name"),
                        rs.getInt("tournament_year")
                ));
            }
        }
    }

    @FXML
    private void addTournament() {
        String name = nameField.getText();
        String yearText = yearField.getText();
        if (name.isEmpty() || yearText.isEmpty()) return;

        try {
            int year = Integer.parseInt(yearText);
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO tournaments (tournament_name, tournament_year) VALUES (?, ?)");
            stmt.setString(1, name);
            stmt.setInt(2, year);
            stmt.executeUpdate();
            loadTournaments();
            clearForm();
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clearForm() {
        nameField.clear();
        yearField.clear();
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
