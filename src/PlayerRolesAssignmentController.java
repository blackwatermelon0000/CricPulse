import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class PlayerRolesAssignmentController {

    @FXML private TextField playerIdField, tournamentIdField, roleField;

    @FXML
    private void assignRole() {
        try {
            int playerId = Integer.parseInt(playerIdField.getText());
            int tournamentId = Integer.parseInt(tournamentIdField.getText());
            String role = roleField.getText();

            try (Connection conn = DBConnect.getConnection()) {
                String query = "INSERT INTO player_roles (player_id, tournament_id, role) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, playerId);
                stmt.setInt(2, tournamentId);
                stmt.setString(3, role);
                stmt.executeUpdate();

                playerIdField.clear();
                tournamentIdField.clear();
                roleField.clear();
            }
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
