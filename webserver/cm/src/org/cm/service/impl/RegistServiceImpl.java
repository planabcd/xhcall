package org.cm.service.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.cm.dao.UserDao;
import org.cm.entity.User;
import org.cm.service.RegistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
public class RegistServiceImpl implements RegistService{
	@Autowired
	private  UserDao  userDao;

	@Override
	public String regist(String username, String password,String usertype) {
		//ע��ǰ�ж��û����Ƿ���ڣ���������򷵻�false
		User user=userDao.findByName(username);
		if(user!=null){
			return "no";
		}else{
			//���ݿ���û������û� �������ʾ����û������Ա�ע��,�������ִ��ע�����
			 user=new User();
			user.setName(username);
			user.setPwd(DigestUtils.md5Hex(password));
			user.setState("0");
			user.setType(usertype);
			try{
			userDao.save(user);
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("������");
			}return "yes";
		}
		
		
		
	}

}
