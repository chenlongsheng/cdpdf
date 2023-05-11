package com.jeeplus.modules.test;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;

/**
 * 
 * @author ����������
 * ��ȡApplicationContext ����Ĺ�����
 *
 */
@Controller
public class ApplicationContextUtils implements ApplicationContextAware{
	//����ɾ�̬��
	private static ApplicationContext applicationContext;
	@Override
	public void setApplicationContext(ApplicationContext ac) throws BeansException {
		// TODO �Զ����ɵķ������
		applicationContext=ac;
	}
	//��ȡ
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	//����bean��id���ƣ���ȡ��Ӧ�Ķ���
	public static Object getBean(String beanName){
		return applicationContext.getBean(beanName);
	}
	//����bean��id���ƣ���ȡ��Ӧ�Ķ���
	public static <T>  T getBean(String beanName,Class<T> clazz){
		return applicationContext.getBean(beanName,clazz);
	}
}
