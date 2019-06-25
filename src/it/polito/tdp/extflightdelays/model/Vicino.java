package it.polito.tdp.extflightdelays.model;

public class Vicino implements Comparable<Vicino>{

	private Airport vicino;
	private double distanza;
	public Vicino(Airport vicino, double distanza) {
		super();
		this.vicino = vicino;
		this.distanza = distanza;
	}
	public Airport getVicino() {
		return vicino;
	}
	public void setVicino(Airport vicino) {
		this.vicino = vicino;
	}
	public double getDistanza() {
		return distanza;
	}
	public void setDistanza(double distanza) {
		this.distanza = distanza;
	}
	@Override
	public int compareTo(Vicino o) {
		// TODO Auto-generated method stub
		return Double.compare(o.getDistanza(), this.getDistanza());
	}
}
