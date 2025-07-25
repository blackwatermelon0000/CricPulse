import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class UmpireManagementController {

    @FXML private TextField nameField, nationalityField;

    @FXML
    private void addUmpire() {
        String name = nameField.getText();
        String nationality = nationalityField.getText();

        if (name.isEmpty() || nationality.isEmpty()) return;

        try (Connection conn = DBConnect.getConnection()) {
            String query = "INSERT INTO umpires (umpire_name, nationality) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, nationality);
            stmt.executeUpdate();

            nameField.clear();
            nationalityField.clear();
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
