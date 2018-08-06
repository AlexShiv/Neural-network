package mainApp;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

    @FXML
    private Button clearButton;

    @FXML
    private Button uploadButton;

    @FXML
    private Canvas canvas;

    private int height, width;

    private TestNet network;

    private int[][] boardMas;

    private GraphicsContext gc;

    @FXML
    void initialize() {
        width = 28;
        height = 28;
        boardMas = new int[width][height];
        network = new TestNet(height, width, 1);
        gc = canvas.getGraphicsContext2D();

        network.buildNet();

        // drawing
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            int x = (int) event.getX() / 20;
            int y = (int) event.getY() / 20;
            if (event.getButton() == MouseButton.PRIMARY) {
                boardMas[x][y] = 1;
            } else boardMas[x][y] = 0;
            draw(x, y);
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            int x = (int) event.getX() / 20;
            int y = (int) event.getY() / 20;
            try {
                if (event.getButton() == MouseButton.PRIMARY) {
                    boardMas[x][y] = 1;
                } else boardMas[x][y] = 0;
            } catch (ArrayIndexOutOfBoundsException e) {
                return;
            }
            draw(x, y);
        });

        // clear
        clearButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            gc.clearRect(0, 0, width * 20, height * 20);
            for (int i = 0; i < boardMas.length; i++) {
                for (int j = 0; j < boardMas[i].length; j++) {
                    boardMas[i][j] = 0;
                }
            }
        });

        // launch
        launch.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (boardMas[i][j] == 1) {
                        image.setRGB(i, j, -1);
                    } else image.setRGB(i, j, -16777216);
                }
            }
            answer.setText("Анализ...");

            File file = new File("C:\\Users\\acer\\Desktop\\ASU\\Нейронные сети\\Neural-network\\deeplearninf4j\\src\\main\\resources\\TestShape.png");
            try {
                ImageIO.write(image, "png", file);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            answer.setText("DONE");

            String result = network.inNet(file);
            if (result != null) {
                answer.setText(result);
            } else answer.setText("Что то пошло не так :(");
            // скармливание изображения сети и вывод результата
        });

        // load file
        uploadButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            JFileChooser fileChooser = new JFileChooser();
            File file;
            int res = fileChooser.showOpenDialog(new JFrame());
            if (res == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
                String result = network.inNet(file);
                if (result != null) {
                    answer.setText(result);
                } else answer.setText("Что то пошло не так :(");
            }
            fileChooser.cancelSelection();
        });
    }

    private void draw(int x, int y) {
        if (boardMas[x][y] == 0) {
            gc.clearRect(x * 20, y * 20, 20, 20);
        } else {
            gc.setFill(Color.BLACK);
            gc.fillRect(x * 20, y * 20, 20, 20);
        }

    }
}
