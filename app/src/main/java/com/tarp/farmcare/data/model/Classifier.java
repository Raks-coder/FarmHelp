package com.tarp.farmcare.data.model;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

import org.tensorflow.lite.Interpreter;

//import java.io.FileDescriptor;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;

public class Classifier {

    AssetManager assetManager;
    String modelPath;
    String labelPath;

    private Interpreter INTERPRETER;
    private List<String> LABEL_LIST;
    int INPUT_SIZE;
    private int PIXEL_SIZE = 3;
    private int IMAGE_MEAN = 0;
    private float IMAGE_STD = 255.0f;
    private int MAX_RESULTS = 3;
    private float THRESHOLD = 0.4f;

    public Classifier(AssetManager assetManager, String modelPath, String labelPath, int inputSize) throws IOException {
        this.INPUT_SIZE = inputSize;
        this.assetManager = assetManager;
        this.modelPath = modelPath;
        this.labelPath = labelPath;
        this.INTERPRETER = new Interpreter(this.loadModelFile(assetManager, modelPath));
        this.LABEL_LIST = this.loadLabelList(assetManager, labelPath);

    }

    private MappedByteBuffer loadModelFile(AssetManager assetManager, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = assetManager.openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private List<String> loadLabelList(AssetManager assetManager, String labelPath) throws IOException {
        BufferedReader bufReader = new BufferedReader(new InputStreamReader(assetManager.open(labelPath)));
        ArrayList<String> listOfLines = new ArrayList<>();
        String line = bufReader.readLine();
        while (line != null) {
            listOfLines.add(line);
            line = bufReader.readLine();
        }

        bufReader.close();
        return listOfLines;
    }

    public List<Recognition> recognizeImage(Bitmap bitmap)  {
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, this.INPUT_SIZE, this.INPUT_SIZE, false);
        ByteBuffer byteBuffer = this.convertBitmapToByteBuffer(scaledBitmap);
        float[][] result = new float[1][this.LABEL_LIST.size()];
        INTERPRETER.run(byteBuffer, result);
        return this.getSortedResult(result);
    }

    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
       ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * INPUT_SIZE * INPUT_SIZE * PIXEL_SIZE);
       byteBuffer.order(ByteOrder.nativeOrder());
       int[] intValues = new  int[this.INPUT_SIZE * this.INPUT_SIZE];
       bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
       int pixel = 0;
       for (int i=0;i<this.INPUT_SIZE;i++) {
            for (int j = 0; j<this.INPUT_SIZE;j++) {
                int val = intValues[pixel++];

                byteBuffer.putFloat((float)((val >> 16 & 255) - this.IMAGE_MEAN) / this.IMAGE_STD);
                byteBuffer.putFloat((float)((val >> 8 & 255) - this.IMAGE_MEAN) / this.IMAGE_STD);
                byteBuffer.putFloat((float)((val & 255) - this.IMAGE_MEAN) / this.IMAGE_STD);
            }
        }
        return byteBuffer;
    }

    private List<Recognition> getSortedResult(float[][] labelProbArray) {
//        Log.d("Classifier", "List Size:(%d, %d, %d)".format(labelProbArray.length,labelProbArray[0].size,LABEL_LIST.size));

        PriorityQueue <Recognition> pq = new PriorityQueue<>(this.MAX_RESULTS, new Recognition());

        int recognitionsSize;
        int i = 0;
        for(recognitionsSize = ((Collection)this.LABEL_LIST).size(); i < recognitionsSize; ++i) {
            float confidence = labelProbArray[0][i];
            if (confidence >= this.THRESHOLD) {
                pq.add(new Recognition("" + i, this.LABEL_LIST.size() > i ? (String)this.LABEL_LIST.get(i) : "Unknown", confidence));
            }
        }
        ArrayList<Recognition> recognitions = new ArrayList<Recognition>();

//        Log.d("Classifier", "pqsize:(%d)".format(pq.size))

//        val recognitions = ArrayList<Classifier.Recognition>()
        recognitionsSize = Math.min(pq.size(), this.MAX_RESULTS);
        for (i =0; i<recognitionsSize;i++) {
            recognitions.add(pq.poll());
        }

        return recognitions;
    }

}
