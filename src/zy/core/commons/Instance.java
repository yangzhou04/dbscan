package zy.core.commons;

import zy.core.clustering.Clusterable;

public class Instance implements Clusterable, Comparable<Instance>{

	private double[] coord;
	private Object[] val;

	public Instance(double[] coord, Object[] val) {
		if (coord == null)
			throw new NullPointerException("Point coordinates are null");
		this.coord = coord;
		this.val = val;
	}

	@Override
	public double[] getFeatures() {
		return this.coord;
	}

	public void setInstance(double[] coord) {
		this.coord = coord;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < coord.length; i++) {
			sb.append(coord[i]);
			sb.append(" ");
		}
		sb.append(": ");
		for (int i = 0; i < val.length; i++) {
			sb.append(val[i]);
			sb.append(" ");
		}
		return sb.toString().trim();
	}

	@Override
	public int compareTo(Instance p) {
		double[] coord1 = this.coord;
		double[] coord2 = p.coord;
		if (coord1.length < coord2.length)
			return -1;
		else if (coord1.length > coord2.length)
			return 1;
		else {
			for (int i = 0; i < coord1.length; i++) {
				// TODO  handle noisy level
				if (coord1[i] < coord2[i])
					return -1;
				else if (coord1[i] > coord2[i])
					return 1;
			}
			return 0;
		}
	}

	public double[] getCoord() {
		return coord;
	}

	public void setCoord(double[] coord) {
		this.coord = coord;
	}

	public Object[] getVal() {
		return val;
	}

	public void setVal(Object[] val) {
		this.val = val;
	}
	
}
