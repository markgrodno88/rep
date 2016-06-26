import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


public class Singleton {
		public Mat image; 
	  
		private Singleton() {
		}
	  
		public Mat getMat(){
			return image;
		}
		
		public Mat setMat(String string){
			image = Imgcodecs.imread(string);
			return image;
		}


	   private static class SingletonHolder {
	     private static final Singleton INSTANCE = new Singleton();
	   }
	 
	   public static Singleton getInstance() {
		 System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	     return SingletonHolder.INSTANCE;
	   }
	   
	   
	   public void loadGray(){  
		   Mat laplacian1 = new Mat();
		   Imgproc.Laplacian(image, laplacian1, CvType.CV_8U, 3, 100, 10000);
		   Imgcodecs.imwrite("homikLaplacian.jpg", laplacian1);
	   }
	   public void calulateLaplacian(){  
		   Mat laplacian1 = new Mat();
		   Imgproc.Laplacian(image, laplacian1, CvType.CV_8U, 3, 100, 10000);
		   Imgcodecs.imwrite("homikLaplacian.jpg", laplacian1);
	   }
}