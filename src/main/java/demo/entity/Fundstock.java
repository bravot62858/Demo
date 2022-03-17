package demo.entity;

public class Fundstock {
	private Integer sid;
	private Integer fid;
	private String symbol;
	private Integer share;
	private Fund fund;
	
	public Integer getSid() {
		return sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	public Integer getFid() {
		return fid;
	}
	public void setFid(Integer fid) {
		this.fid = fid;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public Integer getShare() {
		return share;
	}
	public void setShare(Integer share) {
		this.share = share;
	}
	public Fund getFund() {
		return fund;
	}
	public void setFund(Fund fund) {
		this.fund = fund;
	}
	
	@Override
	public String toString() {
		return "Fundstock [sid=" + sid + ", fid=" + fid + ", symbol=" + symbol + ", share=" + share + ", fund=" + fund
				+ "]";
	}
	
}
