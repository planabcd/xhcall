package cn.tx.xhc.domain;

public class OrderInsertReslult {
	public int carId;
	public int currentId;
	public boolean flag;
	public int getCarId() {
		return carId;
	}
	public void setCarId(int carId) {
		this.carId = carId;
	}
	public int getCurrentId() {
		return currentId;
	}
	public void setCurrentId(int currentId) {
		this.currentId = currentId;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	@Override
	public String toString() {
		return "OrderInsertReslult [carId=" + carId + ", currentId="
				+ currentId + ", flag=" + flag + "]";
	}
	
	
}
