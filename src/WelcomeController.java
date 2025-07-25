import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class WelcomeController {

    @FXML
    private void goToDashboard() {
        try {
            Node dashboard = FXMLLoader.load(getClass().getResource("dashboard_cards.fxml"));
            StackPane contentPane = MainDashboardController.getContentPaneStatic();
            contentPane.getChildren().setAll(dashboard);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        applyFadeAnimation(MainDashboardController.getContentPaneStatic());
    }

    private void applyFadeAnimation(Node node) {
        FadeTransition ft = new FadeTransition(Duration.millis(1200), node);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }
}
