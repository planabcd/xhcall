package org.cm.entity;

import java.io.Serializable;

public class OrderResult implements Serializable{
	
private int carId;//�ɹ����س�����id
private int currentId;//�ɹ����س�����ʻ��·��
private boolean flag;//�ж��µ��Ƿ�ɹ�
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

}
