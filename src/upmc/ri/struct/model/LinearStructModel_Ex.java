package upmc.ri.struct.model;

import upmc.ri.struct.STrainingSample;
import upmc.ri.struct.instantiation.IStructInstantiation;
import upmc.ri.utils.VectorOperations;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by dexter on 26/11/2016.
 */
public class LinearStructModel_Ex<X, Y> extends LinearStructModel<X, Y> {
    public LinearStructModel_Ex(IStructInstantiation<X, Y> instantiation, int dimpsi) {
        super(instantiation, dimpsi);

    }
    @Override
    public Y predict(STrainingSample<X, Y> ts) {
        IStructInstantiation<X, Y> instantiation = this.instantiation();
        double w [] = this.getParameters();

        Set<Y> Y_range = instantiation.enumerateY();
                // loop set
        Y y_pre = null;
        Double max_value = Double.MIN_VALUE;
        // initial max value
        Iterator<Y> itr = Y_range.iterator();
        while (itr.hasNext()) {
            Y y = itr.next();
            double value = VectorOperations.dot(w, instantiation.psi(ts.input, y));
            // check argmax
            if (value > max_value) {
                y_pre = y;
                max_value = value;
            }
        }
        return y_pre;
    }

    @Override
    public Y lai(STrainingSample<X, Y> ts) {
        IStructInstantiation<X, Y> instantiation = this.instantiation();
        double w [] = this.getParameters();

        Set<Y> Y_range = instantiation.enumerateY();
        // loop set
        Y y_pre = null;
        Double max_value = Double.MIN_VALUE;
        // initial max value
        Iterator<Y> itr = Y_range.iterator();
        while (itr.hasNext()) {
            Y y = itr.next();
            double value = VectorOperations.dot(w, instantiation.psi(ts.input, y)) + instantiation.delta(ts.output,y);
            // check argmax
            if (value > max_value) {
                y_pre = y;
                max_value = value;
            }
        }
        return y_pre;
    }
}
