import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

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

    private GraphicsContext gc;

    private Canvas canvas;

    @FXML
    void initialize() {
        width = 28;
        height = 28;
        boardMas = new int[width][height];
        network = new TestNet(height, width, 1);
        canvas = new Canvas(board.getWidth(), board.getHeight());
        //gc = canvas.getGraphicsContext2D();
        board.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int x = (int) event.getX()/20;
                int y = (int) event.getY()/20;
                if (event.getButton() == MouseButton.PRIMARY) {
                    boardMas[x][y] = 1;
                } else boardMas[x][y] = 0;
                draw();
            }
        });
        board.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int x = (int) event.getX()/20;
                int y = (int) event.getY()/20;
                if (event.getButton() == MouseButton.PRIMARY) {
                    boardMas[x][y] = 1;
                } else boardMas[x][y] = 0;
                draw();
            }
        });
        board.getChildren().add(canvas);
    }

    private void draw() {
        gc = canvas.getGraphicsContext2D();
        for (int i = 0; i < boardMas.length; i++) {
            for (int j = 0; j < boardMas[i].length; j++) {
                Color color;
                if (boardMas[i][j] == 0) color = Color.WHITE;
                else color = Color.BLACK;
                gc.setFill(color);
                gc.fillRect(i*20, j*20, i*20+20, j*20+20);
            }
        }

    }
}
