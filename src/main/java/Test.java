import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

public class Test {

    static{System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}

    private static Mat thresh_img = new Mat();
    private static JLabel imgDisplay = new JLabel(), resultDisplay = new JLabel(),cornerDisplay = new JLabel();


    public static void main(String[] args) throws InterruptedException, TesseractException {

        Mat gray = new Mat();

        Mat img = Imgcodecs.imread("C:\\Users\\afck9\\IdeaProjects\\grp11_solitaire\\src\\main\\resources\\test.PNG", Imgcodecs.IMREAD_COLOR);
        Mat template = Imgcodecs.imread("C:\\Users\\afck9\\IdeaProjects\\grp11_solitaire\\src\\main\\resources\\template.PNG", Imgcodecs.IMREAD_COLOR);

        if (img.empty() || template.empty()) {
            System.out.println("Can't read one of the images");
            System.exit(-1);
        }

        createJFrame();

        Mat img_display = new Mat();
        img.copyTo(img_display);


        Imgproc.cvtColor(img_display,gray,Imgproc.COLOR_BGR2GRAY);

        Imgproc.threshold(gray, thresh_img,170,255,Imgproc.THRESH_BINARY);

        List<MatOfPoint> points = new ArrayList<MatOfPoint>();
        Mat result = new Mat();

        Imgproc.findContours(thresh_img,points,result,Imgproc.RETR_TREE,Imgproc.CHAIN_APPROX_SIMPLE);



        for (int contourIdx = 0; contourIdx < points.size(); contourIdx++)
        {
            double contourArea = Imgproc.contourArea(points.get(contourIdx));
            System.out.println(contourArea);

            if (contourArea > 18000 && contourArea < 21000 ){

                Imgproc.drawContours(img_display, points, contourIdx, new Scalar(0,255,0), 2);

                // Get corner points
                double length = Imgproc.arcLength(new MatOfPoint2f(points.get(contourIdx).toArray()),true);
                MatOfPoint2f approx = new MatOfPoint2f();
                Imgproc.approxPolyDP(new MatOfPoint2f(points.get(contourIdx).toArray()),approx,0.01*length,true);
                MatOfPoint cornerPoints = new MatOfPoint(approx.toArray());

                // Create corner rectangle
                Rect rect = Imgproc.boundingRect(cornerPoints);


                //Imgproc.line(img_display,topLeft,topRight,new Scalar(0,255,0),2);
                Imgproc.rectangle(img_display,rect.tl(),rect.br(),new Scalar(20,20,20),-1,4,0);

            }else{

            }

        }

        Image tmpImg = HighGui.toBufferedImage(img_display);
        ImageIcon icon = new ImageIcon(tmpImg);
        imgDisplay.setIcon(icon);

        thresh_img.convertTo(thresh_img, CvType.CV_8UC1, 255.0);
        tmpImg = HighGui.toBufferedImage(thresh_img);
        icon = new ImageIcon(tmpImg);
        resultDisplay.setIcon(icon);

    }

   static private void createJFrame() {
        String title = "Source image; Control; Result image";
        JFrame frame = new JFrame(title);
        frame.setLayout(new GridLayout(2, 2));

        frame.add(imgDisplay);
        frame.add(resultDisplay);
        frame.setSize(1200,1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
