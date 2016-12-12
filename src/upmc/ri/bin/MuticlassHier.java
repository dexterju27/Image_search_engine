package upmc.ri.bin;

import upmc.ri.struct.DataSet;
import upmc.ri.struct.Evaluator;
import upmc.ri.struct.STrainingSample;
import upmc.ri.struct.instantiation.MultiClass;
import upmc.ri.struct.instantiation.MultiClassHier;
import upmc.ri.struct.model.LinearStructModel_Ex;
import upmc.ri.struct.training.SGDTrainer;

import java.util.ArrayList;

/**
 * Created by dexter on 12/12/2016.
 */
public class MuticlassHier {
    public static void main(String args[]) throws Exception {
        // load data
        String file_name = "/Users/dexter/Downloads/sbow/";
        DataSet<double [],  String> data = VisualIndexes.VisualIndexes(file_name);
        // initial instantiation
        MultiClassHier instantiation = new MultiClassHier(data.outputs());
//        the size of w is the number of class * number of features
        LinearStructModel_Ex<double[], String> linear_model = new LinearStructModel_Ex(instantiation, 250 * data.outputs().size());
        Evaluator<double[], String> evaluator = new Evaluator<>();
        evaluator.setListtrain(data.listtrain);
        evaluator.setListtest(data.listtest);
        evaluator.setModel(linear_model);
        SGDTrainer<double[], String> trainer = new SGDTrainer<> (evaluator, 100, 0.01, 0.000001);
        trainer.train(data.listtrain, linear_model);
        ArrayList<String> predictions = new ArrayList<String>();
        ArrayList<String> gt = new ArrayList<String>();
        for (STrainingSample<double[], String> ts : data.listtest) {
            predictions.add(linear_model.predict(ts));
            gt.add(ts.output);
        }
        instantiation.confusionMatrix(predictions, gt);

    }
}
