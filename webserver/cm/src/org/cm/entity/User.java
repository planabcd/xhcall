package org.cm.entity;

import java.io.Serializable;

public class User implements Serializable{
	private Integer id;//�û��ı�ţ������ݿ�����������
	private String name;
	private String pwd;
	private String type;//�û������ͣ�Admin������Customer
	private String state;//��½��״̬
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	


	

}
