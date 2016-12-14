package upmc.ri.struct.instantiation;

import upmc.ri.struct.ranking.RankingFunctions;
import upmc.ri.struct.ranking.RankingOutput;
import upmc.ri.utils.VectorOperations;

import java.util.List;
import java.util.Set;

/**
 * Created by dexter on 13/12/2016.
 */
public class RankingInstantiation implements IStructInstantiation<List<double [] >, RankingOutput> {
    @Override
    public double[] psi(List<double[]> list, RankingOutput rankingOutput) {
        double [] result = new double[list.get(0).length];
        List<Integer> binary = rankingOutput.getLabelsGT();
        List<Integer> position = rankingOutput.getPositionningFromRanking();
        int coutPosive = 0;
//        for optimization
        for (int i = 0; i < binary.size(); i++) {
            if (binary.get(i) != 1) {
                continue;
            }
            coutPosive++;
            int countNegtive = 0;
            for (int j = 0; j < binary.size(); j++) {
                if (binary.get(j) != -1) {
                    continue;
                }
                countNegtive++;
                int sign = position.get(i) < position.get(j) ? 1 : -1;
                for (int k = 0; k < result.length; k++) {
                    result[k] += sign * (list.get(i)[k] - list.get(j)[k]);
                }
                if (countNegtive == binary.size() -  rankingOutput.getNbPlus()) {
                    break;
                }
            }
            if (coutPosive == rankingOutput.getNbPlus()) {
                break;
            }
        }
        return result;
    }

    @Override
    public double delta(RankingOutput y1, RankingOutput y2) {
        return 1. - RankingFunctions.averagePrecision(y2);
    }

    @Override
    public Set<RankingOutput> enumerateY() {
        return null;
    }
}
