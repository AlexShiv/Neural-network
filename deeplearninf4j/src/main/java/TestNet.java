import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TestNet {

    private static final Logger log = LoggerFactory.getLogger(TestNet.class);
    private static String trainPath = "C:\\Users\\alexey\\AppData\\Local\\Temp\\mnist";

    public static void main(String[] args) throws IOException {
        int height = 28;
        int width = 28;
        int channels = 1; // single channel for grayscale images
        int outputNum = 10; // 10 digits classification
        List<Integer> lableList = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        int seed = 1234;
        Random randNumGen = new Random(seed);

        // Создание сети и ее конфигурации
        MultiLayerNetwork network = null;


        log.info("************LOAD NETWORK CONFIG*****************");
        try {
            network = ModelSerializer.restoreMultiLayerNetwork(new File(trainPath + "/minist-model.zip"));
        } catch (FileNotFoundException ex) {
            log.error("ОШИБКА фАЙЛЕ ЗАГРУЗКИ");
            System.exit(0);
        }

        network.init();

        // evaluation while training (the score should go down)
        log.info("***********TESTING DATA************");

        File picData = new File(trainPath + "\\mnist_png\\data\\2.png");
        NativeImageLoader loader = new NativeImageLoader(height, width, channels);
        INDArray img = loader.asMatrix(picData);
        DataNormalization scalerPros = new ImagePreProcessingScaler(0, 1);
        scalerPros.transform(img);

        INDArray outputRes = network.output(img);
        log.info(lableList.toString());
        log.info(outputRes.toString());
    }
}
