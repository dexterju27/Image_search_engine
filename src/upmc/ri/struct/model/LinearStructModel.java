package upmc.ri.struct.model;

import upmc.ri.struct.STrainingSample;
import upmc.ri.struct.instantiation.IStructInstantiation;

/**
 * Created by dexter on 26/11/2016.
 */
public abstract class  LinearStructModel<X, Y> implements IStructModel<X, Y> {
    public IStructInstantiation<X, Y> instantiation;
    public double [] w;

    public LinearStructModel(IStructInstantiation<X, Y> instantiation, int dimpsi) {
        super();
        this.w = new double[dimpsi];
        this.instantiation = instantiation;

    }
    @Override
    public IStructInstantiation<X, Y> instantiation() {
        return instantiation;
    }

    @Override
    public double[] getParameters() {
        return w;
    }
}