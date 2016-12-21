package org.cm.controller;

import org.cm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OrderController {
	@Autowired
	private OrderService orderService;
	@RequestMapping(value = "/order.do",produces = "application/json;charset=utf-8")  
	@ResponseBody
	public String  order(String from ,String to,int currentLine,long startTime){
		orderService.order(from, to, currentLine, startTime);
		return "ok";
		
	}
	

}
