package zuna.refactoring;

public class Metric{
	double csm = 0.0;
	double ich = 0.0;
	double cerm = 0.0;
	double ecd = 0.0;
	double ias = 0.0;
	double idc = 0.0;
	double ims = 0.0;
	double iic = 0.0;
	double eic = 0.0;
	double rs = 0.0;
	
	public Metric(double csm, double ich, double cerm) {
		this.csm = csm;
		this.ich = ich;
		this.cerm = cerm;
	}
	public Metric(){
		
	}
	
	
	public double getRs() {
		return rs;
	}
	public void setRs(double rs) {
		this.rs = rs;
	}
	public double getIic() {
		return iic;
	}
	public void setIic(double iic) {
		this.iic = iic;
	}
	public double getEic() {
		return eic;
	}
	public void setEic(double eic) {
		this.eic = eic;
	}
	public double getIas() {
		return ias;
	}
	public void setIas(double ias) {
		this.ias = ias;
	}
	public double getIdc() {
		return idc;
	}
	public void setIdc(double idc) {
		this.idc = idc;
	}
	public double getIms() {
		return ims;
	}
	public void setIms(double ims) {
		this.ims = ims;
	}
	public double getEcd() {
		return ecd;
	}
	public void setEcd(double ecd) {
		this.ecd = ecd;
	}
	public double getCsm() {
		return csm;
	}
	public void setCsm(double csm) {
		this.csm = csm;
	}
	public double getIch() {
		return ich;
	}
	public void setIch(double ich) {
		this.ich = ich;
	}
	public double getCerm() {
		return cerm;
	}
	public void setCerm(double cerm) {
		this.cerm = cerm;
	}
		
	
	
}
