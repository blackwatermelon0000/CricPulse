import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.*;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

public class InningsManagementController {

    @FXML private ComboBox<Integer> matchIdCombo;
    @FXML private ComboBox<String> battingCombo, bowlingCombo;
    @FXML private TextField runsField, wicketsField, oversField;
    @FXML private TableView<Innings> inningsTable;
    @FXML private TableColumn<Innings, Integer> idCol, matchCol, runsCol, wicketsCol;
    @FXML private TableColumn<Innings, String> batCol, bowlCol;
    @FXML private TableColumn<Innings, Double> oversCol;

    private Connection conn;
    private ObservableList<Innings> inningsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        try {
            conn = DBConnect.getConnection();
            loadCombos();
            setupTable();
            loadInnings();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadCombos() throws SQLException {
        try (Statement st = conn.createStatement()) {
            ResultSet rs1 = st.executeQuery("SELECT match_id FROM matches");
            while (rs1.next()) matchIdCombo.getItems().add(rs1.getInt(1));

            rs1 = st.executeQuery("SELECT team_name FROM teams");
            while (rs1.next()) {
                battingCombo.getItems().add(rs1.getString(1));
                bowlingCombo.getItems().add(rs1.getString(1));
            }
        }
    }

    private void setupTable() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        matchCol.setCellValueFactory(new PropertyValueFactory<>("matchId"));
        batCol.setCellValueFactory(new PropertyValueFactory<>("battingTeam"));
        bowlCol.setCellValueFactory(new PropertyValueFactory<>("bowlingTeam"));
        runsCol.setCellValueFactory(new PropertyValueFactory<>("totalRuns"));
        wicketsCol.setCellValueFactory(new PropertyValueFactory<>("wickets"));
        oversCol.setCellValueFactory(new PropertyValueFactory<>("overs"));
        inningsTable.setItems(inningsList);
    }

    private void loadInnings() throws SQLException {
        inningsList.clear();
        String query = "SELECT i.innings_id, i.match_id, t1.team_name, t2.team_name, i.total_runs, i.wickets_lost, i.overs " +
                "FROM innings i " +
                "JOIN teams t1 ON i.batting_team_id = t1.team_id " +
                "JOIN teams t2 ON i.bowling_team_id = t2.team_id";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                inningsList.add(new Innings(
                        rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4),
                        rs.getInt(5), rs.getInt(6), rs.getDouble(7)
                ));
            }
        }
    }

    @FXML
    private void addInnings() {
        try {
            PreparedStatement st = conn.prepareStatement(
                    "INSERT INTO innings (match_id, batting_team_id, bowling_team_id, total_runs, wickets_lost, overs) " +
                            "VALUES (?, (SELECT team_id FROM teams WHERE team_name = ?), (SELECT team_id FROM teams WHERE team_name = ?), ?, ?, ?)"
            );
            st.setInt(1, matchIdCombo.getValue());
            st.setString(2, battingCombo.getValue());
            st.setString(3, bowlingCombo.getValue());
            st.setInt(4, Integer.parseInt(runsField.getText()));
            st.setInt(5, Integer.parseInt(wicketsField.getText()));
            st.setDouble(6, Double.parseDouble(oversField.getText()));
            st.executeUpdate();
            loadInnings();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
