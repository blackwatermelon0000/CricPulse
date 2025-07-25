import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
        import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.*;

public class MatchManagementController {

    @FXML private ComboBox<String> tournamentCombo, team1Combo, team2Combo;
    @FXML private TextField dateField, venueField;
    @FXML private TableView<Match> matchTable;
    @FXML private TableColumn<Match, Integer> idCol;
    @FXML private TableColumn<Match, String> tournamentCol, team1Col, team2Col, dateCol, venueCol;

    private Connection conn;
    private ObservableList<Match> matchList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        try {
            conn = DBConnect.getConnection();
            loadCombos();
            setupTable();
            loadMatches();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadCombos() throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            ResultSet tRes = stmt.executeQuery("SELECT tournament_name FROM tournaments ORDER BY tournament_name");
            while (tRes.next()) tournamentCombo.getItems().add(tRes.getString(1));

            ResultSet tmRes = stmt.executeQuery("SELECT team_name FROM teams ORDER BY team_name");
            while (tmRes.next()) {
                String name = tmRes.getString(1);
                team1Combo.getItems().add(name);
                team2Combo.getItems().add(name);
            }
        }
    }

    private void setupTable() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("matchId"));
        tournamentCol.setCellValueFactory(new PropertyValueFactory<>("tournament"));
        team1Col.setCellValueFactory(new PropertyValueFactory<>("team1"));
        team2Col.setCellValueFactory(new PropertyValueFactory<>("team2"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("matchDate"));
        venueCol.setCellValueFactory(new PropertyValueFactory<>("venue"));
        matchTable.setItems(matchList);
    }

    private void loadMatches() throws SQLException {
        matchList.clear();
        String sql = "SELECT m.match_id, t.tournament_name, t1.team_name, t2.team_name, TO_CHAR(m.match_date, 'YYYY-MM-DD'), m.venue FROM matches m " +
                "JOIN tournaments t ON m.tournament_id = t.tournament_id " +
                "JOIN teams t1 ON m.team1_id = t1.team_id " +
                "JOIN teams t2 ON m.team2_id = t2.team_id";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                matchList.add(new Match(
                        rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)
                ));
            }
        }
    }

    @FXML
    private void addMatch() {
        String tournament = tournamentCombo.getValue();
        String team1 = team1Combo.getValue();
        String team2 = team2Combo.getValue();
        String date = dateField.getText();
        String venue = venueField.getText();

        if (tournament == null || team1 == null || team2 == null || date.isEmpty() || venue.isEmpty()) return;

        try {
            String sql = "INSERT INTO matches (tournament_id, team1_id, team2_id, match_date, venue) " +
                    "VALUES ((SELECT tournament_id FROM tournaments WHERE tournament_name = ?), " +
                    "(SELECT team_id FROM teams WHERE team_name = ?), " +
                    "(SELECT team_id FROM teams WHERE team_name = ?), TO_DATE(?, 'YYYY-MM-DD'), ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, tournament);
            stmt.setString(2, team1);
            stmt.setString(3, team2);
            stmt.setString(4, date);
            stmt.setString(5, venue);
            stmt.executeUpdate();
            loadMatches();
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
