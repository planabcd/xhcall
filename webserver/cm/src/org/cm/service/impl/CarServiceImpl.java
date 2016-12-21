package org.cm.service.impl;

import java.util.List;

import org.cm.dao.CarDao;
import org.cm.entity.Car;
import org.cm.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class CarServiceImpl implements CarService {
	@Autowired
	private CarDao carDao;
	public boolean distributeRoteToCar(int circuitId, int carId) {
		Car car = new Car();
		System.out.println(circuitId+carId);
		car.setId(carId);
		car.setCurrent_id(circuitId);
		if(carDao.updateCarById(car)==0)
			return false;
		return true;
	}
	
	public int addCar() {
		Car car = new Car();
		//Ĭ��ֵ
		car.setCurrent_id(1);
		car.setCurrent_location("����");
		car.setDeparture_time(0l);
		car.setDirection("����");
		car.setRunning_type(1);
		if(carDao.addCar(car)==0)
		  return -1;//���ʧ��
		return carDao.getMaxId();
	}
	@Override
	public boolean removeCarById(int carId) {
		if(carDao.removeCarById(carId)==0)
			return false;//ʧ��
		return true;
	}

	@Override
	public boolean changeDepartureTime(int carId, long time) {
		Car car = carDao.findById(carId);
		if(car==null)
			return false;//�޸�ʧ��,����id������
		car.setDeparture_time(time);
		if(carDao.updateCarById(car)==0)
			return false;
		return true;
	}
	
	public List<Car> getCarsInfoByRunningType(int runningType){
		List<Car> result = carDao.findByRunningType(runningType);
		return result;
	}

}
