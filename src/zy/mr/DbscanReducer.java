package zy.mr;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.aliyun.odps.data.Record;
import com.aliyun.odps.mapred.ReducerBase;

import zy.core.clustering.Cluster;
import zy.core.clustering.Dbscan;
import zy.core.commons.Instance;
import zy.util.CommonUtils;
import zy.util.Flag;


public class DbscanReducer extends ReducerBase {
	private Record result;
	private int minPts;
	private float eps;
	private int noisy;
	private List<Integer> featIndex;

	@Override
	public void setup(TaskContext context) throws IOException {
		result = context.createOutputRecord();
		eps = context.getJobConf().getFloat(Flag.E, 0.02f);
		minPts = context.getJobConf().getInt(Flag.M, 2);
		noisy = context.getJobConf().getInt(Flag.N, Integer.MAX_VALUE);
		featIndex = CommonUtils.getColumnIndex(context.getJobConf().get(Flag.F));
		System.err.println(featIndex);
	}

	@Override
	public void reduce(Record key, Iterator<Record> values,
			TaskContext context) throws IOException {
		
		Collection<Instance> instances = new LinkedList<Instance>();
		while (values.hasNext()) {
			Record val = values.next();
			double[] coord = new double[featIndex.size()];
			System.err.println("val's column count = " + val.getColumnCount());
			for (int i = 0, j = 0; i < val.getColumnCount(); i++) {
				if (featIndex.contains(i)) {
					System.err.println("feature : " + val.get(i));
					double t = val.getDouble(i);
					coord[j++] = t;
				}
			}
			instances.add(new Instance(coord, val.toArray()));
			
			if (instances.size() > noisy) {
				return;
			}
		}
		
		Dbscan<Instance> dbscan = new Dbscan<Instance>(eps, minPts);
		List<Cluster<Instance>> clusters = dbscan.cluster(instances);
		
		int clusterIndex = 0;
		for (Cluster<Instance> cluster : clusters) {
			for (Instance instance : cluster.getInstances()) {
				int index = 0;
				for (Object obj : instance.getVal()) {
					result.set(index++, obj);
				}
				result.set(index++, clusterIndex);
				context.write(result);
			}
			clusterIndex++;
		}
	}
}
