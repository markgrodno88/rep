import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.swing.border.Border;
import org.omg.CORBA.CTX_RESTRICT_SCOPE;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Range;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;





public class Main {
	public final static double RED_C = 0.3, GREEN_C = 0.59, BLUE_C = 0.11;
	public final static int COUNT_OF_LEVEL = 255;
	public final static int COUNT_ROUND = 100000;
	
	public static void main(String[] args) {

		Main main = new Main();
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat im = Imgcodecs.imread("lenaC.jpg");

		System.out.println(main.getMeanFromRedChannel(im));
		System.out.println(main.getMeanFromGreenChannel(im));
		System.out.println(main.getMeanFromBlueChannel(im));
 
		//System.out.println(main.convertRGBpixelToHSVpixel(121,200,11)[0]);
		//System.out.println(main.convertRGBpixelToHSVpixel(121,200,11)[1]);
		//System.out.println(main.convertRGBpixelToHSVpixel(121,200,11)[2]);
		
		System.out.println(main.calculateSumOfHSVParameters(im, false));



		
		//Imgcodecs.imwrite("wynik.jpg", main.drawHistogramRGB(image1, 1000, 1000) );
		//Imgcodecs.imwrite("wynik_hsv.jpg", hsv);
		
	}	
	public static float getMedianAndStdFromMat(Mat originalImage){
		MatOfDouble mean = new MatOfDouble();
		MatOfDouble stddev = new MatOfDouble();
		Core.meanStdDev(originalImage, mean , stddev);
		float d, std;
		if(originalImage.channels()>1){
			d = (float)((0.11*mean.get(0,0)[0]+0.59*mean.get(1,0)[0]+0.3*mean.get(2,0)[0])/255); //b-g-r
			
			std = (float)((0.11*stddev.get(0,0)[0]+0.59*stddev.get(1,0)[0]+0.3*stddev.get(2,0)[0])/255);
		}else{
			d = (float)((mean.get(0,0)[0])/255);
			
			std = (float)((stddev.get(0,0)[0])/(255));
		}
		return round(d);
	}

	private int mHistSizeNum = 256;
	private List <Mat> listOfChannels;
	private float v;
	private float []val;
	
	private List<Mat> getListOfChannels(Mat originalImage){
		
		MatOfInt mHistSize = new MatOfInt(mHistSizeNum);
		List<Mat> hsv_planes = new ArrayList<Mat>();
		//if(originalImage.channels()>1){
			Core.split(originalImage, hsv_planes);
		
			MatOfInt mChannels[] = new MatOfInt[] { new MatOfInt(0), new MatOfInt(1),new MatOfInt(2) };
			MatOfFloat histogramRanges = new MatOfFloat(0f, 256f);
			
			Mat hist_r = new Mat();
			Mat hist_g = new Mat();
			Mat hist_b = new Mat();
			Imgproc.calcHist(hsv_planes, mChannels[0],new Mat(), hist_r,mHistSize, histogramRanges);
			Imgproc.calcHist(hsv_planes, mChannels[1],new Mat(), hist_g,mHistSize, histogramRanges);		
			Imgproc.calcHist(hsv_planes, mChannels[2],new Mat(), hist_b,mHistSize, histogramRanges);
			
			listOfChannels = new ArrayList<>(3);
			listOfChannels.add(hist_r);
			listOfChannels.add(hist_g);
			listOfChannels.add(hist_b);

		return listOfChannels;	
	}

	public  Mat drawHistogramRGB(Mat originalImage, int hist_w, int hist_h){
		List <Mat> listOfChannels = getListOfChannels(originalImage);
	   
	    long bin_w = Math.round((double) hist_w / 256);
	    Mat histImage = new Mat( hist_h, hist_w, CvType.CV_8UC3, new Scalar( 0,0,0,0));
	    Mat hist_r=listOfChannels.get(0);
	    Mat hist_g=listOfChannels.get(1);
	    Mat hist_b=listOfChannels.get(2);
	    Core.normalize(hist_r, hist_r, histImage.rows(), 0, Core.NORM_INF);
	    Core.normalize(hist_g, hist_g, histImage.rows(), 0, Core.NORM_INF);
	    Core.normalize(hist_b, hist_b, histImage.rows(), 0, Core.NORM_INF);  
	    Point p1, p2;
	    for(int i = 1; i < 256; i++){
	    	/*
	    	p1 = new Point(bin_w * (i - 1),hist_h -(Math.round(0.3*hist_r.get(i - 1, 0)[0])+Math.round(0.59*hist_g.get(i - 1, 0)[0])+Math.round(0.11*hist_b.get(i - 1, 0)[0])));
	        p2 = new Point(bin_w * (i), hist_h - (Math.round(0.3*hist_r.get(i, 0)[0])+Math.round(0.59*hist_g.get(i, 0)[0])+Math.round(0.11*hist_b.get(i, 0)[0])));
	    	Imgproc.line(histImage, p1, p2, new Scalar(255,0,0), 2, 8, 0);
	    	*/
	    	p1 = new Point(bin_w * (i - 1),(hist_h - Math.round(hist_r.get(i - 1, 0)[0])));
	        p2 = new Point(bin_w * (i), (hist_h - Math.round(hist_r.get(i, 0)[0])));
	        Imgproc.line(histImage, p1, p2, new Scalar(255,0,0), 2, 8, 0);
	    		    	
	    	Point p3 = new Point(bin_w * (i - 1), (hist_h - Math.round(hist_g.get(i - 1, 0)[0])));
	        Point p4 = new Point(bin_w * (i), (hist_h - Math.round(hist_g.get(i, 0)[0])));
	        Imgproc.line(histImage, p3, p4, new Scalar(0, 255,0), 2, 8, 0);
	
	        Point p5 = new Point(bin_w * (i - 1), hist_h - Math.round(hist_b.get(i - 1, 0)[0]));
	        Point p6 = new Point(bin_w * (i), hist_h - Math.round(hist_b.get(i, 0)[0]));
	        Imgproc.line(histImage, p5, p6, new Scalar(0, 0, 255), 2, 8, 0);
	        
	    } 
	    return histImage;
	}
	
	public static float round(float in){
		in *= 100000;
		in = Math.round(in);
		in /= 100000;
		return in;
	}
	/*
	public static float getMedianValueFromMat(Mat originalImage) {
		Scalar scalar = Core.mean(originalImage);
		float mean;
		if(originalImage.channels()>1){
			mean = round((float)(0.11*scalar.val[0]+0.59*scalar.val[1]+0.3*scalar.val[2]));
		}else{
			mean = round((float)scalar.val[0]/255);
		}
		return mean;
	}*/
	public float getMedianValueFromMat(Mat originalImage){
		
		Scalar scalar = Core.mean(originalImage);
		float mean = 0;
		
		if(originalImage.channels()== 3){
			mean = round((float)(0.11*scalar.val[0]
								+ 0.59*scalar.val[1]
								+ 0.3*scalar.val[2])/255);
		}else if(originalImage.channels()==1){
			mean = round((float)scalar.val[0]/255);
		}
		return mean;
	}
	private List<Float> calculateMeanFromRGB(Mat originalMat) {
		Scalar scalar = Core.mean(originalMat);
		float meanR = 0;
		float meanG = 0;
		float meanB = 0;
		if(originalMat.channels()== 3){
			meanR = round((float)(scalar.val[2]));
			meanG = round((float)(scalar.val[1]));
			meanB =	round((float)(scalar.val[0]));										
		}
		List <Float> listOfChannels = new ArrayList<>();
		listOfChannels.add(meanR);
		listOfChannels.add(meanG);
		listOfChannels.add(meanB);
		return listOfChannels;
	}
	public float getMeanFromRedChannel(Mat originalMat){		
		return calculateMeanFromRGB(originalMat).get(0);
	}
	public float getMeanFromGreenChannel(Mat originalMat){		
		return calculateMeanFromRGB(originalMat).get(1);
	}
	public float getMeanFromBlueChannel(Mat originalMat){		
		return calculateMeanFromRGB(originalMat).get(2);
	}
	private final static int HUE_INDEX = 0,
							 SATURATION_INDEX = 1,
							 VALUE_INDEX = 2,
							 HSV_HUE = 360,
							 RED_INDEX = 2,
							 GREEN_INDEX = 1,
							 BLUE_INDEX = 0;
	

	private List<List<Float>> getListHSVParametersFromHSV(Mat mat){
		List<Float> listOfHue = new ArrayList<>();
		List<Float> listOfSaturation = new ArrayList<>();
		List<Float> listOfValue = new ArrayList<>();
		float [] hsv;
		for(int i = 0; i < mat.cols(); i++){
			for(int j = 0; j < mat.rows(); j++){
				hsv = convertRGBpixelToHSVpixel((int)mat.get(i, j)[RED_INDEX],
												(int)mat.get(i, j)[GREEN_INDEX],
												(int)mat.get(i, j)[BLUE_INDEX]);
				listOfHue.add(hsv[0]);
				listOfSaturation.add(hsv[1]);
				listOfValue.add(hsv[2]);
			}
		}	
		List<List<Float>> listParametersOfHSV = new ArrayList<>();
		listParametersOfHSV.add(listOfHue);
		listParametersOfHSV.add(listOfSaturation);
		listParametersOfHSV.add(listOfValue);
		return listParametersOfHSV;	
	}
	
	public List<Float> calculateSumOfHSVParameters(Mat mat, boolean normalisationValue){
		List<List<Float>> listParametersOfHSV = getListHSVParametersFromHSV(mat);
		double sumH = 0, sumS = 0, sumV = 0;
		for(int i = 0; i < listParametersOfHSV.get(HUE_INDEX).size(); i++){
			sumH += listParametersOfHSV.get(HUE_INDEX).get(i);
			sumS += listParametersOfHSV.get(SATURATION_INDEX).get(i);
			sumV += listParametersOfHSV.get(VALUE_INDEX).get(i);
		}
		List<Float> list0fSum = new ArrayList<>(3);
		if(normalisationValue == true){
			list0fSum.add((float)sumH/calculateRangeToNormalisation(mat));
			list0fSum.add((float)sumS/calculateRangeToNormalisation(mat));
			list0fSum.add((float)sumV/calculateRangeToNormalisation(mat));
		}else{
			list0fSum.add((float)sumH/calculateCoutOfPixels(mat));
			list0fSum.add((float)sumS/calculateCoutOfPixels(mat));
			list0fSum.add((float)sumV/calculateCoutOfPixels(mat));
		}
		return list0fSum;
		
	}
	private int calculateRangeToNormalisation(Mat mat){	
		return calculateCoutOfPixels(mat)*HSV_HUE;
	}
	private int calculateCoutOfPixels(Mat mat){
		return mat.cols()*mat.rows();
	}
	//int color = bitmap.getPixel(x, y);
		public float[] convertRGBpixelToHSVpixel(int r, int g, int b){

			float[] hsv = new float[3];
			Color.RGBtoHSB(r, g, b, hsv);		
			return hsv;
		}
}
