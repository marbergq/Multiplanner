package com.berka.multiplanner.Models.Travel;

import java.util.List;

public class Direction {

	private String direction;
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public List<Remark> getRemarks() {
		return remarks;
	}
	public void setRemarks(List<Remark> remarks) {
		this.remarks = remarks;
	}
	private List<Remark> remarks;
}
