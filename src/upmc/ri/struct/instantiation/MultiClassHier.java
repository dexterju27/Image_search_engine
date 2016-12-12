package upmc.ri.struct.instantiation;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.WuPalmer;

import java.util.Set;

/**
 * Created by dexter on 12/12/2016.
 * Using http://ws4jdemo.appspot.com for similarity
 */
public class MultiClassHier extends MultiClass {
    private double [][] distances;
    public MultiClassHier(Set<String> label_set) {
        super(label_set);
//        compute distance
        int n = this.label_set.size();
        this.distances = new double[n][n];
        NictWordNet net = new NictWordNet();
        RelatednessCalculator calculator = new WuPalmer(net);
        double max_value = Double.MIN_VALUE;
        double min_value = Double.MAX_VALUE;
        for (String y1 : this.label_set) {
            int i = this.matching.get(y1);
            for (String y2 : this.label_set) {
                int j = this.matching.get(y2);
//                for normalization
                if (i == j) {
                    this.distances[i][j] = 0;
                } else if (i <= j) {
                    this.distances[i][j] =  1. -  calculator.calcRelatednessOfWords(y1, y2);
//                    symmetric
                    this.distances[j][i] = this.distances[i][j];
                }
                max_value = Double.max(this.distances[i][j], max_value);
                min_value = Double.min(this.distances[i][j], min_value);
            }
        }
//        normalization [0.1 , 2]
        for (String y1 : this.label_set) {
            int i = this.matching.get(y1);
            for (String y2 : this.label_set) {
                int j = this.matching.get(y2);
//                for normalization
                this.distances[i][j] = ((this.distances[i][j] - min_value)  / (max_value - min_value)) * 1.9 + 0.1 ;
                this.distances[i][j] = this.distances[j][i];
            }
        }


    }

    @Override
    public double delta(String y1, String y2) {
        int i = this.matching.get(y1);
        int j = this.matching.get(y2);
        return this.distances[i][j];
    }
}
