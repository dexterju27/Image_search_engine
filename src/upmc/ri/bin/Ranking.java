package upmc.ri.bin;

import upmc.ri.struct.DataSet;
import upmc.ri.struct.Evaluator;
import upmc.ri.struct.STrainingSample;
import upmc.ri.struct.instantiation.MultiClass;
import upmc.ri.struct.instantiation.RankingInstantiation;
import upmc.ri.struct.model.LinearStructModel_Ex;
import upmc.ri.struct.model.RankingStructModel;
import upmc.ri.struct.ranking.RankingFunctions;
import upmc.ri.struct.ranking.RankingOutput;
import upmc.ri.struct.training.SGDTrainer;
import upmc.ri.utils.Drawing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by dexter on 13/12/2016.
 */
public class Ranking {
    public static void main(String args[]) throws Exception {
        // load data
        String file_name = "/Users/dexter/Downloads/sbow/";
        DataSet<double [],  String> data = VisualIndexes.VisualIndexes(file_name);
        Set<String> labels = data.outputs();
        List<Double> scores = new ArrayList<>();
        for (String query: labels) {
            DataSet<List<double []>, RankingOutput> data_converted = RankingFunctions.convertClassif2Ranking(data, query);
            int size = data_converted.listtrain.get(0).input.get(0).length;
            RankingInstantiation instantiation = new RankingInstantiation();
            RankingStructModel model = new RankingStructModel(instantiation, size);
            Evaluator<List<double[]>, RankingOutput> evaluator = new Evaluator<>();
            evaluator.setListtrain(data_converted.listtrain);
            evaluator.setListtest(data_converted.listtest);
            evaluator.setModel(model);
            SGDTrainer<List<double[]> , RankingOutput> trainer = new SGDTrainer<> (evaluator, 50, 10., 0.000001);
            trainer.train(data_converted.listtrain, model);
            double precesion_recall[][] = RankingFunctions.recalPrecisionCurve(model.predict(data_converted.listtest.get(0)));
            int postive = data_converted.listtest.get(0).output.getNbPlus();
            BufferedImage figure = Drawing.traceRecallPrecisionCurve(postive, precesion_recall);
            File file = new File("./" + query + ".png");
            ImageIO.write(figure, "PNG", file);
            double score = RankingFunctions.averagePrecision(model.predict(data_converted.listtest.get(0)));
            scores.add(score);
            System.out.println("AP score of"  + query + " : "+ score);
        }
        double sum = 0;
        for (double d : scores) sum += d;
        System.out.println("MAP score : "+ sum / (1.0 * scores.size()));




        // initial instantiation
//        MultiClass instantiation = new MultiClass(data.outputs());
////        the size of w is the number of class * number of features
//        LinearStructModel_Ex<double[], String> linear_model = new LinearStructModel_Ex(instantiation, 250 * data.outputs().size());
//        Evaluator<double[], String> evaluator = new Evaluator<>();
//        evaluator.setListtrain(data.listtrain);
//        evaluator.setListtest(data.listtest);
//        evaluator.setModel(linear_model);
//        SGDTrainer<double[], String> trainer = new SGDTrainer<> (evaluator, 100, 0.01, 0.000001);
//        trainer.train(data.listtrain, linear_model);
//        ArrayList<String> predictions = new ArrayList<String>();
//        ArrayList<String> gt = new ArrayList<String>();
//        for (STrainingSample<double[], String> ts : data.listtest) {
//            predictions.add(linear_model.predict(ts));
//            gt.add(ts.output);
//        }
//        instantiation.confusionMatrix(predictions, gt);


    }
}
