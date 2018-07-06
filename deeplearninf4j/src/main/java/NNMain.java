import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.split.FileSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.SubsamplingLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.nd4j.linalg.schedule.MapSchedule;
import org.nd4j.linalg.schedule.ScheduleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class NNMain {

    private static final Logger log = LoggerFactory.getLogger(NNMain.class);
    private static String trainPath = "C:\\Users\\acer\\Desktop\\ASU\\Нейронные сети\\mnist\\mnist_png\\";

    public static void main(String[] args) throws IOException {
        int height = 28;
        int width = 28;
        int channels = 1; // single channel for grayscale images
        int outputNum = 10; // 10 digits classification
        int batchSize = 54;
        int nEpochs = 1;
        int seed = 1234;
        Random randNumGen = new Random(seed);

        // vectorization of train data
        File trainData = new File(trainPath + "\\training — rotate");
        FileSplit trainSplit = new FileSplit(trainData, NativeImageLoader.ALLOWED_FORMATS, randNumGen);
        ParentPathLabelGenerator labelMaker = new ParentPathLabelGenerator(); // parent path as the image label
        ImageRecordReader trainRR = new ImageRecordReader(height, width, channels, labelMaker);
        trainRR.initialize(trainSplit);
        DataSetIterator trainIter = new RecordReaderDataSetIterator(trainRR, batchSize, 1, outputNum);

        // pixel values from 0-255 to 0-1 (min-max scaling)
        DataNormalization scaler = new ImagePreProcessingScaler(0, 1);
        scaler.fit(trainIter);
        trainIter.setPreProcessor(scaler);

        // vectorization of test data
        File testData = new File(trainPath + "\\testing");
        FileSplit testSplit = new FileSplit(testData, NativeImageLoader.ALLOWED_FORMATS, randNumGen);
        ImageRecordReader testRR = new ImageRecordReader(height, width, channels, labelMaker);
        testRR.initialize(testSplit);
        DataSetIterator testIter = new RecordReaderDataSetIterator(testRR, batchSize, 1, outputNum);
        testIter.setPreProcessor(scaler); // same normalization for better results

        log.info("Network configuration and training...");
        Map<Integer, Double> lrSchedule = new HashMap<>();
        lrSchedule.put(0, 0.06); // iteration #, learning rate
        lrSchedule.put(200, 0.05);
        lrSchedule.put(600, 0.028);
        lrSchedule.put(800, 0.006);
        lrSchedule.put(1000, 0.001);

        // Создание сети и ее конфигурации
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .l2(0.0005)
                .updater(new Nesterovs(new MapSchedule(ScheduleType.ITERATION, lrSchedule)))
                .weightInit(WeightInit.XAVIER)
                .list()
                .layer(0, new ConvolutionLayer.Builder(5, 5)
                        .nIn(channels)
                        .stride(1, 1) // шаг фильтром по х и по у
                        .nOut(30)
                        .activation(Activation.IDENTITY)
                        .build())
                .layer(1, new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
                        .kernelSize(2, 2)
                        .stride(2, 2)
                        .build())
                .layer(2, new ConvolutionLayer.Builder(5, 5)
                        .stride(1, 1) // nIn need not specified in later layers
                        .nOut(50)
                        .activation(Activation.IDENTITY)
                        .build())
                .layer(3, new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
                        .kernelSize(2, 2)
                        .stride(2, 2)
                        .build())
                .layer(4, new DenseLayer.Builder().activation(Activation.RELU)
                        .nOut(500)
                        .build())
                .layer(5, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nOut(outputNum)
                        .activation(Activation.SOFTMAX)
                        .build())
                .setInputType(InputType.convolutionalFlat(28, 28, 1)) // InputType.convolutional for normal image
                .backprop(true).pretrain(false).build();
        MultiLayerNetwork network = new MultiLayerNetwork(conf);

        // evaluation while training (the score should go down)
        network.init();
        network.setListeners(new ScoreIterationListener(10));
        log.debug("Total num of params: {}", network.numParams());
        for (int j = 0; j < nEpochs; j++) {
            network.fit(trainIter);
            log.info("Completed epoch {}", j);
            Evaluation eval = network.evaluate(testIter);
            log.info(eval.stats());
            trainIter.reset();
            testIter.reset();
        }
        //ModelSerializer.writeModel(network, new File("C:\\Users\\alexey\\Desktop\\АГУ\\GitHub\\mnist\\minist-model2.zip"), true);
    }
}
