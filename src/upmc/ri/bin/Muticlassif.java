package upmc.ri.bin;
import upmc.ri.bin.VisualIndexes;
import upmc.ri.struct.DataSet;
import upmc.ri.struct.Evaluator;
import upmc.ri.struct.instantiation.MultiClass;
import upmc.ri.struct.model.LinearStructModel_Ex;
import upmc.ri.struct.training.SGDTrainer;


/**
 * Created by dexter on 26/11/2016.
 */
public class Muticlassif {
    public static void main(String args[]) throws Exception {
        // load data
        String file_name = "/Users/dexter/Downloads/sbow/";
        DataSet<double [],  String> data = VisualIndexes.VisualIndexes(file_name);
        // initial instantiation
        MultiClass instantiation = new MultiClass(data.outputs());
        LinearStructModel_Ex<double[], String> linear_model = new LinearStructModel_Ex(instantiation, 250);
        Evaluator<double[], String> evaluator = new Evaluator<>();
        evaluator.setListtrain(data.listtrain);
        evaluator.setListtest(data.listtest);
        evaluator.setModel(linear_model);
        SGDTrainer<double[], String> trainer = new SGDTrainer<> (evaluator, 100, 0.01, 0.000001);
        trainer.train(data.listtrain, linear_model);



    }
}
