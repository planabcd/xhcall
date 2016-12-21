package org.cm.service.impl;

import java.security.MessageDigest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.Md5Crypt;
import org.aspectj.bridge.MessageUtil;
import org.cm.dao.UserDao;
import org.cm.entity.User;
import org.cm.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
public class LoginServiceImpl  implements LoginService{
	@Autowired
	private UserDao userDao;

	@Override
	public boolean  login(String username, String password,String usertype) {
		System.out.println("mima:"+DigestUtils.md5Hex(password));
		//�ж��Ƿ������û���
		if(username==null||"".equals(username)||usertype==null){
			return false;
		}
		//�����û����Ƿ�Ϸ�
		User user=userDao.findByName(username)
;
		if(user==null){
			return false;
		}else if(!user.getPwd().equals( DigestUtils.md5Hex(password))||(!usertype.equals(user.getType()))){//�ж���������������ݿ��е������Ƿ���ͬ
			//�������ͬ����false 
			
			return false;
		}else{
			//����user��״̬state��ΪY
			
			userDao.updateState( username,"1");
			return true;
		}
	
		
		
	}

	@Override
	public boolean logout(String username) {
		try{
		userDao.updateState(username, "0");
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
