package upmc.ri.struct.training;

import java.util.List;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import upmc.ri.struct.Evaluator;
import upmc.ri.struct.STrainingSample;
import upmc.ri.struct.model.IStructModel;
import upmc.ri.struct.instantiation.*;
import upmc.ri.utils.VectorOperations;

public class SGDTrainer<X, Y> implements ITrainer<X, Y> {
    private Random rand = new Random();
    private  int loop;
    private double lambda;
    private double learning_rate;
    private Evaluator<X, Y> evaluator;

    public SGDTrainer(Evaluator<X, Y> evaluator, int loop, double learning_rate,
                      double lambda) {
        super();
        this.evaluator = evaluator;
        this.loop = loop;
        this.learning_rate = learning_rate;
        this.lambda = lambda;
    }
    public double convex_loss(List<STrainingSample<X, Y>> lts, IStructModel<X, Y> model) {
        // compute loss in the train set
        double [] w = model.getParameters();
        IStructInstantiation<X, Y> instantiation = model.instantiation();
        double result = lambda * VectorOperations.norm2(w) / 2.;
        double sum = 0.0;
        for (int i = 0; i < lts.size(); i++) {
            STrainingSample<X, Y> sample = lts.get(i);
            Y y_pre = model.lai(sample);
            sum += instantiation.delta(sample.output, y_pre) + VectorOperations.dot(instantiation.psi(sample.input, y_pre), w);
            sum -= VectorOperations.dot(instantiation.psi(sample.input, sample.output), w);
        }
        sum /= lts.size();
        return result + sum;

    }

    public void train(List<STrainingSample<X, Y>> lts, IStructModel<X, Y> model) {
		// TODO Auto-generated method stub
		// initial
		double [] w = model.getParameters();
        IStructInstantiation<X, Y> instantiation = model.instantiation();

//		for (int i = 0; i < size_w; i++) {
//			w[i] = 0;
//		}
		// initial
		for (int i = 0; i < loop; i++) {
		    if (i % 10 == 0) {
                evaluator.evaluate();
                System.out.println("Err train: " + evaluator.getErr_train());
                System.out.println("Err test:  " + evaluator.getErr_test());
            }
			for (int k = 0; k < lts.size(); k++) {
	            // current sample
                STrainingSample<X, Y> sample = lts.get(rand.nextInt(lts.size()));
//                Set<Y> Y_range = instantiation.enumerateY();
//                // loop set
//                Y y_pre;
//                Double max_value = Double.MIN_VALUE;
//                // initial max value
//                Iterator<Y> itr = Y_range.iterator();
//                while (itr.hasNext()) {
//                    Y y = itr.next();
//                    Double value = instantiation.delta(y, sample.output);
//                    double [] vector = instantiation.psi(sample.input, y);
//                    for (int k = 0; k < size_w; k++) {
//                        value += w[i] * vector[i];
//                    }
//                    // check argmax
//                    if (value > max_value) {
//                        y_pre = y;
//                        max_value = value;
//                    }
//
//                }
                Y y_pre = model.lai(sample);
                // found y_pre continue
                double [] gi = VectorOperations.minus(instantiation.psi(sample.input, y_pre), instantiation.psi(sample.input, sample.output));

//                for (int j = 0; j < w.length; j++) {
//                    gi[j] = (lambda * w[j] + gi[j]) * learning_rate;
//                }
//                w = VectorOperations.minus(w, gi);
                for (int j = 0; j < w.length; j++) {
                    w[j] = w[j] - this.learning_rate * (this.lambda * w[j] + gi[j]);
                }
			}

		}
	}

}
