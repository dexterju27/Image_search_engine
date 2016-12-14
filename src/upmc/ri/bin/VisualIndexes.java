package upmc.ri.bin;
import upmc.ri.struct.*;
import upmc.ri.utils.*;
import upmc.ri.index.*;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import upmc.ri.io.*;

public class VisualIndexes implements Serializable{


	public static DataSet<double[], String>  VisualIndexes(String file_path) throws Exception {
		Set<String> classes = ImageNetParser.classesImageNet();
		List<STrainingSample<double[], String> > train_list  = new  ArrayList<STrainingSample<double[], String> > ();
		List<STrainingSample<double[], String> > test_list = new ArrayList<STrainingSample<double[], String> >();

		for (String str : classes) {
			 List<ImageFeatures> list = ImageNetParser.getFeatures(file_path + str + ".txt");
			for (int i = 0; i < list.size(); i++) { 
				// take only 800 for dataset
				if (i < 800) {
					train_list.add(new STrainingSample<double[], String>(VIndexFactory.computeBow(list.get(i)), str));
				} else {
					test_list.add(new STrainingSample<double[], String>(VIndexFactory.computeBow(list.get(i)), str));
				}
				
			}
			
		}
		DataSet<double[], String> data = new DataSet<double[], String> (train_list, test_list);
		data = PCA.computePCA(data, 250);
		return data;
	}
}
