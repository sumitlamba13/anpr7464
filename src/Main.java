
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.ListIterator;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class Main {
	public static void main(String args[]) {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String inputfile = "D:\\my documents\\java\\NUmberPlateDetection\\src\\Process\\car.jpg";
		Mat img=Imgcodecs.imread(inputfile);
		Mat grey = new Mat();
		Imgproc.resize(img, img, new Size(640,380));
		Imgproc.GaussianBlur(img,grey, new Size(5,5),0);
		Imgproc.cvtColor(img,grey,Imgproc.COLOR_BGR2GRAY );
//		Imgproc.cvtColor(grey,grey, Imgproc.COLOR_BGR2HSV);
		Mat gsTopHat=new Mat();
		Mat gsEnhance=new Mat();
		Mat dst=new Mat();
		
		
		//*****************************************ENHANCING************************************************//
		Mat structure= Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(5,5));
		Mat topHat=new Mat();
		Mat blackHat=new Mat();
	    
	    Imgproc.morphologyEx(grey, topHat, Imgproc.MORPH_TOPHAT, structure);
	    Imgproc.morphologyEx(grey, blackHat, Imgproc.MORPH_BLACKHAT, structure);
	    Core.add(grey, topHat, gsTopHat);
	    Core.subtract(gsTopHat, blackHat,gsEnhance);
	      //************************************************************************//
	    
	    
	    Imgproc.Sobel(gsEnhance, dst,CvType.CV_8U, 1, 0);
	    Imgproc.threshold(dst, dst, 0, 255, Imgproc.THRESH_BINARY+Imgproc.THRESH_OTSU);
	    
	    
	    
	    //*****************************Extracting Contours***********************************************//
	    
	    Mat struc= Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(17,3));
	    Mat copy=new Mat();
	    copy=dst.clone();
	    Imgproc.morphologyEx(dst, dst,Imgproc.MORPH_CLOSE, struc);
	   
	    ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Imgproc.findContours(dst, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
		
		
	     //**********************************************************************************************//
		//**************************************Clean and Read Plate*****contours* this.img**********************************//
		
			
		//*****************************************************************************************************//
		  
		
		  for(int i=0; i< contours.size();i++){
		        System.out.println(Imgproc.contourArea(contours.get(i)));
		        if (Imgproc.contourArea(contours.get(i)) > 50 ){
		            Rect rect = Imgproc.boundingRect(contours.get(i));
		            System.out.println(rect.height);
		            
		            if (rect.height>28&&rect.height<rect.width){
		            //System.out.println(rect.x +","+rect.y+","+rect.height+","+rect.width);
		            Imgproc.rectangle(img, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height),new Scalar(0,0,255));
		    
		            }
		        }
		    }
			LoadImage imageframe = new LoadImage((MatToBuffered(img)));
			imageframe.setVisible(true);
		  ArrayList<RotatedRect> rects = new  ArrayList<RotatedRect>();
		  ListIterator<MatOfPoint> itc = contours.listIterator();
		  Mat crop_img=new Mat();
		  while(itc.hasNext())
		  {
		       MatOfPoint2f mp2f = new MatOfPoint2f(itc.next().toArray());
		       RotatedRect mr = Imgproc.minAreaRect(mp2f);
		       double area = Math.abs(Imgproc.contourArea(mp2f));
		       Point[] pt=new Point[4];
		       double bbArea= mr.size.area();  
		       double ratio = area / bbArea;  
		       if( (ratio < 0.45) || (bbArea < 5000) )
		       {  
		           itc.remove();  // other than deliberately making the program slow,
		                          // does erasing the contour have any purpose?
		       }
		       else
		       {  
		           rects.add(mr);
		           mr.points(pt);
		           for(Point p:pt)
		          	 System.out.println(p);
		          Rect rect =mr.boundingRect();
		          Imgproc.rectangle(img,pt[0],pt[2],new Scalar(255,255,0),5);
		          crop_img=grey.submat(rect);
					LoadImage imageframe2 = new LoadImage((MatToBuffered(crop_img)));
					imageframe2.setVisible(true);
	    
		       }
		       
		  }

		
		
		/*System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String inputfile = "D:\\my documents\\java\\NUmberPlateDetection\\src\\Process\\car2.jpg";

		// String input="C:\\Users\\HP\\Pictures\\numberplate.jpg";
		// Mat file= Imgcodecs.imread(input);

		String file = "D:\\my documents\\java\\NUmberPlateDetection\\src\\Process\\car1.jpg";
		Mat matrix = Imgcodecs.imread(file);
		System.out.println(matrix.rows());
		Mat grey = new Mat();
		Imgproc.cvtColor(matrix, grey, Imgproc.COLOR_RGB2GRAY);
		Mat filter_img = new Mat();
		Imgproc.bilateralFilter(grey, filter_img, 15, 80, 80);
		Mat contrast_img = new Mat();
		Imgproc.equalizeHist(filter_img, contrast_img);
		Mat morph_openimg = new Mat();
		Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(99, 99));
		Imgproc.morphologyEx(filter_img, morph_openimg, Imgproc.MORPH_OPEN, kernel);
		Mat subtract_img = new Mat();
		Core.subtract(contrast_img, morph_openimg, subtract_img);
		Mat binary_img = new Mat();
		Imgproc.threshold(filter_img, binary_img, 100, 255, Imgproc.THRESH_BINARY_INV);
		ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Imgproc.findContours(binary_img, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
		Mat edge_img = new Mat();
		Imgproc.Canny(binary_img, edge_img, 90, 90, 5, true);
		Mat struc = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(17, 3));
		Mat copy = new Mat();
		Mat dst = new Mat();
		copy = dst.clone();
		// Imgproc.morphologyEx(dst, dst, Imgproc.MORPH_CLOSE, struc);
		for (int i = 0; i < contours.size(); i++) {
			System.out.println(Imgproc.contourArea(contours.get(i)));
			if (Imgproc.contourArea(contours.get(i)) > 50) {
				Rect rect = Imgproc.boundingRect(contours.get(i));
				System.out.println(rect.height);

				if (rect.height > 28 && rect.height < rect.width) {
					// System.out.println(rect.x
					// +","+rect.y+","+rect.height+","+rect.width);
					Imgproc.rectangle(edge_img, new Point(rect.x, rect.y),
							new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 255));

				}
			}
		}
		LoadImage imageframe = new LoadImage((MatToBuffered(edge_img)));
		imageframe.setVisible(true);
		// cleanandread.cleanRead(this.img, contours);
		ArrayList<RotatedRect> rects = new ArrayList<RotatedRect>();
		ListIterator<MatOfPoint> itc = contours.listIterator();
		Mat crop_img = new Mat();
		while (itc.hasNext()) {
			MatOfPoint2f mp2f = new MatOfPoint2f(itc.next().toArray());
			RotatedRect mr = Imgproc.minAreaRect(mp2f);
			double area = Math.abs(Imgproc.contourArea(mp2f));
			Point[] pt = new Point[4];
			double bbArea = mr.size.area();
			double ratio = area / bbArea;
			if ((ratio < 0.45) || (bbArea < 5000)) {
				itc.remove(); // other than deliberately making the program
								// slow,
								// does erasing the contour have any purpose?
			} else {
				try {
					rects.add(mr);
					mr.points(pt);
					for (Point p : pt)
						System.out.println(p);
					Rect rect = mr.boundingRect();
					Imgproc.rectangle(binary_img, pt[0], pt[2], new Scalar(255, 255, 0), 5);
					crop_img = grey.submat(rect);

					LoadImage imageframe2 = new LoadImage((MatToBuffered(crop_img)));
					imageframe2.setVisible(true);
				} catch (Exception ex) {
				}
			}
		}
		BufferedImage buff_image = MatToBuffered(binary_img);
		LoadImage imageframe3 = new LoadImage(buff_image);
		imageframe3.setVisible(true);
		// System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		// String inputfile = "D:\\my
		// documents\\java\\NUmberPlateDetection\\src\\Process\\pg1.jpg";
		// Mat file= Imgcodecs.imread(inputfile);
		BufferedImage image = MatToBuffered(edge_img);
		Tesseract tesseract = new Tesseract();
		tesseract.setDatapath("C:\\Users\\HP\\Downloads\\Tess4J-3.4.8-src\\Tess4J\\tessdata");
		try {
			System.out.println(tesseract.doOCR(image));
		} catch (TesseractException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}*/

	}

	public static BufferedImage MatToBuffered(Mat m) {
		int type = BufferedImage.TYPE_BYTE_GRAY;
		if (m.channels() > 1) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}
		int bufferSize = m.channels() * m.cols() * m.rows();
		byte[] b = new byte[bufferSize];
		m.get(0, 0, b); // get all the pixels
		BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(b, 0, targetPixels, 0, b.length);
		return image;
	}
}
