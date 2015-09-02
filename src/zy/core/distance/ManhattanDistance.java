package zy.core.distance;


public class ManhattanDistance implements DistanceMeasure {

	private static final long serialVersionUID = -4829955877422380150L;

	@Override
	public double compute(double[] a, double[] b) {
		if (a.length != b.length)
			throw new IllegalArgumentException();

		double sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += Math.abs(a[i] - b[i]);
        }
        return sum;
	}

}
