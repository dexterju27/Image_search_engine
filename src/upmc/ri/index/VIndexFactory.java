package upmc.ri.index;

import java.util.List;

public class VIndexFactory {
	public static double[] computeBow(ImageFeatures ib) {
		List<Integer> words = ib.getwords();
		int [] histogram = new int[ImageFeatures.tdico];
		double [] result = new double [ImageFeatures.tdico];
		int sum = 0;
		 for (int i : words) { 
			 sum += 1;
		     histogram[i] += 1;

		   }
		 for (int i = 0; i < ImageFeatures.tdico; i++) {
			 result[i] = histogram[i] / (1.0 * sum );
		 }
		return result;
		
	}
}
