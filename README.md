# Density-based spatial clustering of applications with noise(DBSCAN)

A fast dbscan algorithm based on Kd-tree nearest neighbor search

Invoking way:

```java
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
```
