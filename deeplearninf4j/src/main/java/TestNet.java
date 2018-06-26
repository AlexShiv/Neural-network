import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class TestNet {

    private final Logger log = LoggerFactory.getLogger(TestNet.class);
    private String trainPath = "C:\\Users\\alexey\\AppData\\Local\\Temp\\mnist";
    private MultiLayerNetwork network;
    private int height;
    private int width;
    private int channels;

    public TestNet(int height, int width, int channels) {
        this.network = null;
        this.height = height;
        this.width = width;
        this.channels = channels;
    }

    public  void buildNet(){
        // Создание сети и ее конфигурации

        log.info("************LOAD NETWORK CONFIG*****************");
        try {
            network = ModelSerializer.restoreMultiLayerNetwork(new File(trainPath + "/minist-model.zip"));
        } catch (IOException ex) {
            log.error("ОШИБКА фАЙЛЕ ЗАГРУЗКИ");
            System.exit(0);
        }

        // инициализация сети
        network.init();
        //File picData = new File(trainPath + "\\mnist_png\\data\\2.png");
    }

    public String inNet(File f) {
        NativeImageLoader loader = new NativeImageLoader(height, width, channels);
        INDArray img;
        try {
            img = loader.asMatrix(f);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        DataNormalization scalerPros = new ImagePreProcessingScaler(0, 1);
        scalerPros.transform(img);

        INDArray outputRes = network.output(img);
        String out = outputRes.toString();
        log.info(out);
        return out;
    }
}
