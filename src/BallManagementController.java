import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class BallManagementController {

    @FXML private TextField matchIdField, inningsIdField, overNoField, ballNoField;
    @FXML private TextField batsmanIdField, bowlerIdField, runsField, isWicketField;

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
    private void addBall() {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO balls (match_id, innings_id, over_no, ball_no, batsman_id, bowler_id, runs_scored, is_wicket) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            );
            stmt.setInt(1, Integer.parseInt(matchIdField.getText()));
            stmt.setInt(2, Integer.parseInt(inningsIdField.getText()));
            stmt.setInt(3, Integer.parseInt(overNoField.getText()));
            stmt.setInt(4, Integer.parseInt(ballNoField.getText()));
            stmt.setInt(5, Integer.parseInt(batsmanIdField.getText()));
            stmt.setInt(6, Integer.parseInt(bowlerIdField.getText()));
            stmt.setInt(7, Integer.parseInt(runsField.getText()));
            stmt.setInt(8, Integer.parseInt(isWicketField.getText()));

            stmt.executeUpdate();
            clearForm();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clearForm() {
        matchIdField.clear();
        inningsIdField.clear();
        overNoField.clear();
        ballNoField.clear();
        batsmanIdField.clear();
        bowlerIdField.clear();
        runsField.clear();
        isWicketField.clear();
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
