import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

public class SettingsController {

    @FXML
    private void handleClearTestData() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Clear All Test Match Data?");
        alert.setContentText(
                "⚠ This will permanently delete all test match data:\n\n" +
                        "• Balls table (individual deliveries)\n" +
                        "• Matches table (match summaries)\n" +
                        "• Scorecards table (player stats)\n\n" +
                        "Are you sure you want to continue?");

        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try (Connection conn = DBConnect.getConnection();
                 Statement stmt = conn.createStatement()) {

                stmt.executeUpdate("DELETE FROM balls");
                stmt.executeUpdate("DELETE FROM matches");
                stmt.executeUpdate("DELETE FROM scorecards");

                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Data Cleared");
                success.setHeaderText(null);
                success.setContentText("Test data has been successfully cleared.");
                success.showAndWait();

                System.out.println("✅ Test data cleared.");
            } catch (Exception e) {
                e.printStackTrace();
                Alert fail = new Alert(Alert.AlertType.ERROR);
                fail.setTitle("Error");
                fail.setHeaderText("Failed to clear data");
                fail.setContentText(e.getMessage());
                fail.showAndWait();
            }
        } else {
            System.out.println("❌ Data deletion cancelled.");
        }
    }

    @FXML
    private void handleExit() {
        System.exit(0);
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
