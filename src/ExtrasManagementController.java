import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ExtrasManagementController {

    @FXML private TextField ballIdField, wideField, noBallField, byeField, legByeField, penaltyField;

    private Connection conn;

    @FXML
    public void initialize() {
        try {
            conn = DBConnect.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addExtras() {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO extras (ball_id, wide, no_ball, bye, leg_bye, penalty) VALUES (?, ?, ?, ?, ?, ?)"
            );
            stmt.setInt(1, Integer.parseInt(ballIdField.getText()));
            stmt.setInt(2, parseOrDefault(wideField.getText()));
            stmt.setInt(3, parseOrDefault(noBallField.getText()));
            stmt.setInt(4, parseOrDefault(byeField.getText()));
            stmt.setInt(5, parseOrDefault(legByeField.getText()));
            stmt.setInt(6, parseOrDefault(penaltyField.getText()));

            stmt.executeUpdate();
            clearForm();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int parseOrDefault(String val) {
        try {
            return Integer.parseInt(val);
        } catch (Exception e) {
            return 0;
        }
    }

    @FXML
    private void clearForm() {
        ballIdField.clear();
        wideField.clear();
        noBallField.clear();
        byeField.clear();
        legByeField.clear();
        penaltyField.clear();
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
