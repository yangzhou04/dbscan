package zy.core.clustering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

import org.apache.commons.lang.NullArgumentException;

import zy.core.commons.Instance;
import zy.core.distance.CosineDistance;
import zy.core.distance.DistanceMeasure;
import zy.util.kdtree.KDTree;

public class Dbscan<T extends Clusterable> extends ClusteringAlgorithm<T> {

    /** Maximum radius of the neighborhood to be considered. */
    private double eps;

    /** Minimum number of points needed for a cluster. */
    private int minPts;

    /** Status of a point during the clustering process. */
    private enum InstanceStatus {
        /** The point has is considered to be noise. */
        NOISE,
        /** The point is already part of a cluster. */
        PART_OF_CLUSTER
    }

    private KDTree kdtree;

    /**
     * Creates a new instance of a DBSCANClusterer.
     * <p>
     * The euclidean distance will be used as default distance measure.
     *
     * @param eps maximum radius of the neighborhood to be considered
     * @param minPts minimum number of points needed for a cluster
     * @throws NotPositiveException if {@code eps < 0.0} or {@code minPts < 0}
     */
    public Dbscan(final double eps, final int minPts) {
    	this(eps, minPts, new CosineDistance());
    }

    /**
     * Creates a new instance of a DBSCANClusterer.
     *
     * @param eps maximum radius of the neighborhood to be considered
     * @param minPts minimum number of points needed for a cluster
     * @param measure the distance measure to use
     * @throws NotPositiveException if {@code eps < 0.0} or {@code minPts < 0}
     */
	public Dbscan(final double eps, final int minPts,
			final DistanceMeasure measure) {
		super(measure);

		if (eps < 0.0d) {
			throw new IllegalArgumentException(String.valueOf(eps));
		}
		if (minPts < 0) {
			throw new IllegalArgumentException(String.valueOf(minPts));
		}
		this.eps = eps;
		this.minPts = minPts;
	}

    /**
     * Returns the maximum radius of the neighborhood to be considered.
     * @return maximum radius of the neighborhood
     */
    public double getEps() {
        return eps;
    }

    /**
     * Returns the minimum number of points needed for a cluster.
     * @return minimum number of points needed for a cluster
     */
    public int getMinPts() {
        return minPts;
    }

    /**
     * Performs DBSCAN cluster analysis.
     *
     * @param instances the points to cluster
     * @return the list of clusters
     * @throws NullArgumentException if the data points are null
     */
    @Override
    public List<Cluster<T>> cluster(final Collection<T> instances) {
        if (instances == null || instances.isEmpty())
        	throw new IllegalArgumentException("Instances does not exists!");

        int dimension = instances.iterator().next().getFeatures().length;
        kdtree = new KDTree(dimension);
        Random rand = new Random();
        final int RANDOM_LEVEL = 6;
        for (final T instance : instances) {
        	double[] coord = instance.getFeatures();
        	// handle duplicate key condition
        	for (int i = 0; i < dimension; i++) {
        		coord[i] += rand.nextFloat() / Math.pow(10, RANDOM_LEVEL);;
        	}
        	kdtree.insert(coord,  instance);
        }

        final List<Cluster<T>> clusters = new ArrayList<Cluster<T>>();
        final Map<Clusterable, InstanceStatus> visited = new HashMap<Clusterable, InstanceStatus>();

        for (final T instance : instances) {
            if (visited.get(instance) != null) {
                continue;
            }
            final List<T> neighbors = getNeighbors(instance, instances);
            if (neighbors.size()-1 >= minPts) {
                // DBSCAN does not care about center points
                final Cluster<T> cluster = new Cluster<T>();
                clusters.add(expandCluster(cluster, instance, neighbors, instances, visited));
            } else {
                visited.put(instance, InstanceStatus.NOISE);
            }
        }

        return clusters;
    }

    /**
     * Expands the cluster to include density-reachable items.
     *
     * @param cluster Cluster to expand
     * @param instance Point to add to cluster
     * @param neighbors List of neighbors
     * @param points the data set
     * @param visited the set of already visited points
     * @return the expanded cluster
     */
    private Cluster<T> expandCluster(final Cluster<T> cluster,
                                     final T instance,
                                     final List<T> neighbors,
                                     final Collection<T> points,
                                     final Map<Clusterable, InstanceStatus> visited) {
        cluster.addInstance(instance);
        visited.put(instance, InstanceStatus.PART_OF_CLUSTER);

        Queue<T> seeds = new LinkedList<T>(neighbors);
        while (!seeds.isEmpty()) {
        	final T current = seeds.poll();
        	InstanceStatus pStatus = visited.get(current);
        	// only check non-visited points
            if (pStatus == null) {
            	final List<T> currentNeighbors = getNeighbors(current, points);
            	if (currentNeighbors.size()-1 >= minPts) {
            		seeds.add(current);
            	}
            }

            if (pStatus != InstanceStatus.PART_OF_CLUSTER) {
                visited.put(current, InstanceStatus.PART_OF_CLUSTER);
                cluster.addInstance(current);
            }
        }

        return cluster;
    }

    /**
     * Returns a list of density-reachable neighbors of a {@code point}.
     *
     * @param point the point to look for
     * @param points possible neighbors
     * @return the List of neighbors
     */
	@SuppressWarnings("unchecked")
	private List<T> getNeighbors(final T point, final Collection<T> points) {
		double step = eps;
		int dimension = point.getFeatures().length;
		double[] lowk = new double[dimension];
		double[] uppk = new double[dimension];
		for (int i = 0; i < dimension; i++) {
			lowk[i] = point.getFeatures()[i] - step;
			uppk[i] = point.getFeatures()[i] + step;
		}

		Object[] nearest = kdtree.range(lowk, uppk);
		return (List<T>) Arrays.asList(nearest);
	}

	public static void main(String[] args) {
		Dbscan<Clusterable> dbscan = new Dbscan<Clusterable>(0.02, 1);
		List<Clusterable> instances = new LinkedList<Clusterable>();
		
		instances.add(new Instance(new double[] {120.1, 30.2}, new Object[] {1,2,3}));
		instances.add(new Instance(new double[] {120.1, 30.2}, new Object[] {4,5,6}));
		
		List<Cluster<Clusterable>> clusters = dbscan.cluster(instances);
		int clusterId = 0;
		for (Cluster<Clusterable> cluster : clusters) {
			System.out.println(clusterId++);
			for (Clusterable instance : cluster.getInstances()) {
				System.out.println(instance);
			}
		}
	}
}
