package upmc.ri.struct.model;

import javafx.collections.transformation.SortedList;
import upmc.ri.struct.STrainingSample;
import upmc.ri.struct.instantiation.IStructInstantiation;
import upmc.ri.struct.ranking.RankingFunctions;
import upmc.ri.struct.ranking.RankingOutput;
import upmc.ri.utils.VectorOperations;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by dexter on 13/12/2016.
 */
public class RankingStructModel extends LinearStructModel<List<double []>, RankingOutput> {
    public RankingStructModel (IStructInstantiation instantiation, int dimpsi) {
        super(instantiation, dimpsi);
        this.instantiation = instantiation;
    }
    public class Pair implements Comparable<Pair> {
        public final int index;
        public final double value;

        public Pair(int index, double value) {
            this.index = index;
            this.value = value;
        }

        @Override
        public int compareTo(Pair other) {
            //multiplied to -1 as the author need descending sort order
            return -1 * Double.valueOf(this.value).compareTo(other.value);
        }
    }


    @Override
    public RankingOutput predict(STrainingSample<List<double[]> , RankingOutput> ts) {
        IStructInstantiation<List<double []>, RankingOutput> instantiation = this.instantiation();
        double w [] = this.getParameters();
        int size = ts.input.size();
        Pair[] results = new Pair[size];
        for (int i = 0; i < size; i++) {
            double res = VectorOperations.dot(w, ts.input.get(i));
            results[i] = new Pair(i, res);
        }
        Arrays.sort(results);
//       predict
        List<Integer> index = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            index.add(results[i].index);
        }

        return new RankingOutput(ts.output.getNbPlus(), index, ts.output.getLabelsGT());
    }

    @Override
    public RankingOutput lai(STrainingSample<List<double[]> , RankingOutput> ts) {
        return  RankingFunctions.loss_augmented_inference(ts, this.w);
    }
}
