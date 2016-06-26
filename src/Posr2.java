import org.opencv.core.Mat;

public class Posr2 {
	Singleton s = Singleton.getInstance();
	
	
	 public Mat getMat(){
		 return s.image;
	 }
}
