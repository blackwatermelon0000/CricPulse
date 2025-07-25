import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import javafx.scene.Parent;
public class MainDashboardController {

    @FXML
    private StackPane contentPane;

    // Static reference to access from WelcomeController
    private static StackPane staticContentPane;

    // Loads any given FXML into the center pane
    private void loadView(String fxmlFile) {
        try {
            Node view = FXMLLoader.load(getClass().getResource(fxmlFile));
            contentPane.getChildren().setAll(view);
        } catch (IOException e) {
            System.err.println("❌ Failed to load: " + fxmlFile);
            e.printStackTrace();
        }
    }

    // On app launch, show welcome screen
    @FXML
    public void initialize() {
        staticContentPane = contentPane; // Set static reference
        loadView("welcome.fxml");
    }

    // Static accessor method for WelcomeController
    public static StackPane getContentPaneStatic() {
        return staticContentPane;
    }

    // When Dashboard is clicked
    @FXML
    private void showDashboard() {
        loadView("dashboard_cards.fxml");
    }

    @FXML
    private void showAddPlayer() {
        loadView("player_form.fxml");
    }

    @FXML
    private void showTopScorer() {
        loadView("top_scorer.fxml");
    }

    @FXML
    private void showPointsTable() {
        loadView("points_table.fxml");
    }
    @FXML
    private void showSettings() {
        loadView("settings.fxml");
    }
    @FXML
    private void showPlayerCompare() {
        loadView("player_compare.fxml");
    }

    @FXML
    private void showPlayerDashboard() {
        loadView("player_dashboard.fxml");
    }

    @FXML
    private void showMatchSimulation() {
        loadView("match_simulation.fxml");
    }

    @FXML
    private void showAnalytics() {
        loadView("analytics.fxml");
    }

    @FXML
    private void openPlayerManagement() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("player_management.fxml"));
            contentPane.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openTeamManagement() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("team_management.fxml"));
            contentPane.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openTournamentManagement() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("tournament_management.fxml"));
            contentPane.getChildren().setAll(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showMatchManagement() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("match_management.fxml"));
            contentPane.getChildren().setAll(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showInningsManagement() {
        loadView("innings_management.fxml");
    }
    @FXML
    private void showBallManagement() {
        loadView("ball_management.fxml");
    }
    @FXML
    private void showExtrasManagement() {
        loadView("extras_management.fxml");
    }
    @FXML
    private void showPartnershipManagement() {
        loadView("partnership_management.fxml");
    }
    @FXML
    private void showUmpireManagement() {
        loadView("umpire_management.fxml");
    }
    @FXML
    private void showPlayerRolesAssignment() {
        loadView("player_roles_assignment.fxml");
    }
    @FXML
    private void showReportsViewer() {
        loadView("query_reports.fxml");
    }
    @FXML
    private void showTopWicketTakers() {
        loadView("top_wicket_taker.fxml");
    }
    @FXML
    private void goLiveSimulation() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("live_match.fxml"));
            getContentPaneStatic().getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
