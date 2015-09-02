package zy.core.commons;


public class ODPSInstance  {

//	private Object[] kcols;
//	private Object[] xcols;
//
//	public ODPSInstance(Object[] kcols, double[] fcols, Object[] xcols) {
//		super(fcols);
//		this.kcols = kcols;
//		this.xcols = xcols;
//	}
//
//	@Override
//	public String toString() {
//		StringBuffer sb = new StringBuffer();
//		
//		for (int i = 0; i < kcols.length; i++) {
//			sb.append(kcols[i]);
//			sb.append(",");
//		}
//		sb.append(super.toString());
//		sb.append(",");
//		for (int i = 0; i < xcols.length; i++) {
//			sb.append(xcols[i]);
//			sb.append(",");
//		}
//
//		return sb.substring(0, sb.length()-1);
//	}
//
//	@Override
//	public ODPSInstance clone() {
//		Object[] copyKcols = new Object[kcols.length];
//		double[] copyFcols = new double[super.getFeatures().length];
//		Object[] copyXcols = new Object[xcols.length];
//		System.arraycopy(kcols, 0, copyKcols, 0, kcols.length);
//		System.arraycopy(super.getFeatures(), 0, copyFcols, 0, super.getFeatures().length);
//		System.arraycopy(xcols, 0, copyXcols, 0, xcols.length);
//		return new ODPSInstance(copyKcols, copyFcols, copyXcols);
//	}
//	
//	public Object[] getKcols() {
//		return kcols;
//	}
//
//	public void setKcols(Object[] kcols) {
//		this.kcols = kcols;
//	}
//
//	public Object[] getXcols() {
//		return xcols;
//	}
//
//	public void setXcols(Object[] xcols) {
//		this.xcols = xcols;
//	}
//
//	public double[] getFcols() {
//		return super.getFeatures();
//	}
//	
//	public void setFcols(double[] fcols) {
//		super.setInstance(fcols);
//	}

}
