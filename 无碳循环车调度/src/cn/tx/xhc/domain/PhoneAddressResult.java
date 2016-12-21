package cn.tx.xhc.domain;


public class PhoneAddressResult {
	/*{
		  "msg": "success",
		  "result": {
		    "city": "南宁市",
		    "cityCode": "0771",
		    "mobileNumber": "1330000",
		    "operator": "电信CDMA卡",
		    "province": "广西",
		    "zipCode": "530000"
		  },
		  "retCode": "200"
	}*/
	public String msg;
	public PhoneAddressResultData result;
	public String retCode;
	
	@Override
	public String toString() {
		return "PhoneAddressResult [msg=" + msg + ", result=" + result
				+ ", retCode=" + retCode + "]";
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public PhoneAddressResultData getResult() {
		return result;
	}

	public void setResult(PhoneAddressResultData result) {
		this.result = result;
	}

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public class PhoneAddressResultData{
		public String city;
		public String cityCode;
		public String mobileNumber;
		public String operator;
		public String province;
		public String zipCode;
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getCityCode() {
			return cityCode;
		}
		public void setCityCode(String cityCode) {
			this.cityCode = cityCode;
		}
		public String getMobileNumber() {
			return mobileNumber;
		}
		public void setMobileNumber(String mobileNumber) {
			this.mobileNumber = mobileNumber;
		}
		public String getOperator() {
			return operator;
		}
		public void setOperator(String operator) {
			this.operator = operator;
		}
		public String getProvince() {
			return province;
		}
		public void setProvince(String province) {
			this.province = province;
		}
		public String getZipCode() {
			return zipCode;
		}
		public void setZipCode(String zipCode) {
			this.zipCode = zipCode;
		}
		@Override
		public String toString() {
			return "PhoneAddressResultData [city=" + city + ", cityCode="
					+ cityCode + ", mobileNumber=" + mobileNumber
					+ ", operator=" + operator + ", province=" + province
					+ ", zipCode=" + zipCode + "]";
		}
		
	}
	
}
