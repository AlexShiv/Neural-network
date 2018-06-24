import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class MainFrameController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane board;

    @FXML
    private Label answer;

    @FXML
    private Button launch;

    int height, width;

    private TestNet network;

    private int[][] boardMas;


    @FXML
    void initialize() {
        width = 28;
        height = 28;
        boardMas = new int[width][height];
        network = new TestNet(height, width, 1);
        board.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int x = (int) event.getX()/28;
                int y = (int) event.getY()/28;
                if (event.getButton() == MouseButton.PRIMARY) {
                    boardMas[x][y] = 1;
                } else boardMas[x][y] = 0;
                draw();
            }
        });
    }

    private void draw() {
        for (int i = 0; i < boardMas.length; i++) {
            for (int j = 0; j < boardMas[i].length; j++) {
                Color color;
                if (boardMas[i][j] == 0) color = Color.WHITE;
                else color = Color.BLACK;
                Rectangle rectangle = new Rectangle(20, 20, color);
                rectangle.setX(i*28);
                rectangle.setY(j*28);
                board.getChildren().add(rectangle);
            }
        }
    }
}
