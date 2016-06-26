import org.opencv.core.Mat;

public class Posr {
	Singleton s = Singleton.getInstance();
	public Mat getMat(){
		 return s.image;
	 }
	
	 public void createMet(double x){
		 s.setMat("zam.jpg");
		 if(x>=5)
			 s.calulateLaplacian();
		 else 
		 	System.out.println("Nie to");
	 }
}
