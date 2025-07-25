import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class PartnershipManagementController {

    @FXML private TextField matchIdField, player1IdField, player2IdField, runsScoredField;

    @FXML
    private void addPartnership() {
        String matchId = matchIdField.getText();
        String p1 = player1IdField.getText();
        String p2 = player2IdField.getText();
        String runs = runsScoredField.getText();

        if (matchId.isEmpty() || p1.isEmpty() || p2.isEmpty() || runs.isEmpty()) return;

        try (Connection conn = DBConnect.getConnection()) {
            String query = "INSERT INTO partnerships (match_id, player1_id, player2_id, runs_scored) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, Integer.parseInt(matchId));
            stmt.setInt(2, Integer.parseInt(p1));
            stmt.setInt(3, Integer.parseInt(p2));
            stmt.setInt(4, Integer.parseInt(runs));
            stmt.executeUpdate();

            matchIdField.clear();
            player1IdField.clear();
            player2IdField.clear();
            runsScoredField.clear();
        } catch (Exception e) {
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
