import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import java.io.IOException;

public class PlayerFormController {

    @FXML private TextField nameField;
    @FXML private TextField ageField;
    @FXML private ComboBox<String> teamCombo;
    @FXML private TextField battingStyleField;
    @FXML private TextField bowlingStyleField;
    @FXML private TextField roleField;
    @FXML private Button submitButton;

    private Connection conn;

    public PlayerFormController() {
        try {
            String jdbcUrl = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
            String username = "Ayesha";
            String password = "Ayesha123";
            conn = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        // Populate teams from DB
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT team_name FROM teams");
            while (rs.next()) {
                teamCombo.getItems().add(rs.getString("team_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAddPlayer() {
        String name = nameField.getText();
        int age = Integer.parseInt(ageField.getText());
        String team = teamCombo.getValue();
        String batting = battingStyleField.getText();
        String bowling = bowlingStyleField.getText();
        String role = roleField.getText();

        try {
            // Get team_id from team name
            PreparedStatement teamStmt = conn.prepareStatement("SELECT team_id FROM teams WHERE team_name = ?");
            teamStmt.setString(1, team);
            ResultSet teamRs = teamStmt.executeQuery();

            if (teamRs.next()) {
                int teamId = teamRs.getInt("team_id");

                PreparedStatement ps = conn.prepareStatement("INSERT INTO players (player_name, age, team_id, batting_style, bowling_style, role) VALUES (?, ?, ?, ?, ?, ?)");
                ps.setString(1, name);
                ps.setInt(2, age);
                ps.setInt(3, teamId);
                ps.setString(4, batting);
                ps.setString(5, bowling);
                ps.setString(6, role);

                ps.executeUpdate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Player added successfully!");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not add player");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
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
