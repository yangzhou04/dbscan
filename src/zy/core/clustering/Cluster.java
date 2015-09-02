package zy.core.clustering;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Cluster holding a set of {@link Clusterable} points.
 * @param <T> the type of points that can be clustered
 * @version $Id: Cluster.java 1461862 2013-03-27 21:48:10Z tn $
 * @since 3.2
 */
public class Cluster<T extends Clusterable> implements Serializable {

    /** Serializable version identifier. */
    private static final long serialVersionUID = -3442297081515880464L;

    /** The points contained in this cluster. */
    private final List<T> points;

    /**
     * Build a cluster centered at a specified point.
     */
    public Cluster() {
        points = new ArrayList<T>();
    }

    /**
     * Add a point to this cluster.
     * @param instance to add
     */
    public void addInstance(final T instance) {
        points.add(instance);
    }

    /**
     * Get the points contained in the cluster.
     * @return points contained in the cluster
     */
    public List<T> getInstances() {
        return points;
    }

}
