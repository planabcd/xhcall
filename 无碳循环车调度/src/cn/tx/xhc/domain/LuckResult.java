package cn.tx.xhc.domain;

public class LuckResult {
	/* {
		  "msg": "success",
		  "result": {
		    "conclusion": "时来运转，事事如意，功成名就，富贵自来吉"
		  },
		  "retCode": "200"
		}*/
	public String msg;
	public String retCode;
	public LuckResultData result;
	
	@Override
	public String toString() {
		return "LuckResult [msg=" + msg + ", retCode=" + retCode + ", result="
				+ result + "]";
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public LuckResultData getResult() {
		return result;
	}

	public void setResult(LuckResultData result) {
		this.result = result;
	}

	public class LuckResultData{
		public String conclusion;

		public String getConclusion() {
			return conclusion;
		}

		public void setConclusion(String conclusion) {
			this.conclusion = conclusion;
		}

		@Override
		public String toString() {
			return "LuckResultData [conclusion=" + conclusion + "]";
		}
		
		
	}
}
