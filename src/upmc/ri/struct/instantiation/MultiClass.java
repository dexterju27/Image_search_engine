package upmc.ri.struct.instantiation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by dexter on 26/11/2016.
 */
public class MultiClass implements IStructInstantiation<double [], String>{
    private Set<String> label_set;
    private Map<String, Integer> matching;

    public MultiClass(Set<String> label_set) {
        super();
        this.label_set = label_set;
        this.matching = matching();
    }

    public Map<String, Integer> matching(){
        Map<String, Integer> matching = new HashMap<String, Integer>();
        int index = 0;
        for(String s : label_set){
            matching.put(s, index++);
        }
        return matching;
    }

    public Map<String, Integer> generate_index(int size_bow) {
        Map<String, Integer> index_map = new HashMap<String, Integer>();
        int count = 0;
        for ( String label : this.label_set) {
            index_map.put(label, size_bow * count++);
        }
        return index_map;
    }

    @Override
    public double[] psi(double[] doubles, String s) {
        Map<String, Integer> index_map = generate_index(doubles.length);
        double [] res = new double[this.label_set.size() * doubles.length];
        int start = index_map.get(s);
        for (int i = 0; i < doubles.length; i++) {
            res[start + i] = doubles[i];
        }
        return res;
    }

    @Override
    public double delta(String y1, String y2) {
        if (y1.equals(y2)) {
            return 0;
        }
        return 1;
    }

    @Override
    public Set<String> enumerateY() {
        return this.label_set;
    }
}
