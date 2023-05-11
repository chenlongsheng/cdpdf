package com.jeeplus.common.mq.rabbitmq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.persistence.MapEntity;
import com.jeeplus.common.utils.HttpUtils;
import com.jeeplus.modules.settings.entity.TChannel;
import com.jeeplus.modules.settings.entity.TDevice;
import com.jeeplus.modules.settings.entity.TDeviceDetail;
import com.jeeplus.modules.settings.entity.TOrg;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RabbitMQCustomer {

	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@SuppressWarnings("unused")
	private ServiceFacade serviceFacade;

	public void setServiceFacade(ServiceFacade serviceFacade) {
		this.serviceFacade = serviceFacade;
	}

	private ConnectionFactory factory = new ConnectionFactory();

	// 创建一个新的连接
	private Connection connection = null;

	private String rabbitmqHost;

	private String rabbitmqUsername;

	private String rabbitmqPassword;

	private int rabbitmqPort;

	private String rabbitmqVirtualHost;

	public String getRabbitmqHost() {
		return rabbitmqHost;
	}

	public void setRabbitmqHost(String rabbitmqHost) {
		this.rabbitmqHost = rabbitmqHost;
	}

	public String getRabbitmqUsername() {
		return rabbitmqUsername;
	}

	public void setRabbitmqUsername(String rabbitmqUsername) {
		this.rabbitmqUsername = rabbitmqUsername;
	}

	public String getRabbitmqPassword() {
		return rabbitmqPassword;
	}

	public void setRabbitmqPassword(String rabbitmqPassword) {
		this.rabbitmqPassword = rabbitmqPassword;
	}

	public int getRabbitmqPort() {
		return rabbitmqPort;
	}

	public void setRabbitmqPort(int rabbitmqPort) {
		this.rabbitmqPort = rabbitmqPort;
	}

	public String getRabbitmqVirtualHost() {
		return rabbitmqVirtualHost;
	}

	public void setRabbitmqVirtualHost(String rabbitmqVirtualHost) {
		this.rabbitmqVirtualHost = rabbitmqVirtualHost;
	}

	public static BlockingQueue<JSONObject> devAlarmQueue = new LinkedBlockingQueue<JSONObject>();
	public static BlockingQueue<JSONObject> devDataQueue = new LinkedBlockingQueue<JSONObject>();

	private final static String DEV_ALARM_QUEUE = "dev.alarm";
	private final static String DEV_DATA_QUEUE = "dev.status";

	class ChDataThread extends Thread {
		public ChDataThread() {
		}

		public void run() {
			while (true) {
				JSONObject devData = null;
				try {
					devData = devDataQueue.take();
					System.out.println("take a devData");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (devData != null) {
					String devid = devData.getString("dev_id").toString();
					String status = devData.getString("status");
					String occurTime = devData.getString("occur_time").toString();

					/*
					 * 通道数组暂不处理 List<ChData> channellist = devData.getItems(); for(ChData chdata :
					 * channellist) { String chid = chdata.getCh_id().toString();
					 * if(JedisUtils.exists(chid)) { String value = JedisUtils.get(chid); if(value
					 * != chdata.getStatus().toString()) { //更新数据库
					 * 
					 * //更新缓存 JedisUtils.set(chdata.getCh_id().toString(),
					 * chdata.getStatus().toString(),3600*24); } } else { //更新数据库
					 * 
					 * //插入缓存 JedisUtils.set(chdata.getCh_id().toString(),
					 * chdata.getStatus().toString(),3600*24); } }
					 */
				}
			}
		}
	}

	class AlarmDataThread extends Thread {
		public AlarmDataThread() {
		}

		public void run() {
			while (true) {
				JSONObject devAlarm = null;
				try {
					devAlarm = devAlarmQueue.take();

				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}
	}

	public void startProccessData() {
		this.factory.setHost(rabbitmqHost);
		this.factory.setUsername(rabbitmqUsername);
		this.factory.setPassword(rabbitmqPassword);
		this.factory.setPort(rabbitmqPort);
		this.factory.setVirtualHost(rabbitmqVirtualHost);
		// 关键所在，指定线程池
		ExecutorService service = Executors.newFixedThreadPool(10);
		factory.setSharedExecutor(service);
		// 设置自动恢复
		factory.setAutomaticRecoveryEnabled(true);
		factory.setNetworkRecoveryInterval(2);// 设置 每10s ，重试一次
		factory.setTopologyRecoveryEnabled(false);// 设置不重新声明交换器，队列等信息。
		try {
			connection = factory.newConnection();

			/** 报警消息 */
			// 创建一个通道
			Channel devAlarmChannel = connection.createChannel();
			// 声明要关注的队列
			devAlarmChannel.queueDeclare(DEV_ALARM_QUEUE, true, false, false, null);
			System.out.println("Customer Alarm Waiting Received messages");
			// DefaultConsumer类实现了Consumer接口，通过传入一个频道，
			// 告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery
			Consumer alarmConsumer = new DefaultConsumer(devAlarmChannel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					String message = new String(body, "UTF-8");
					System.out.println("Customer Received '" + message + "'");
					JSONObject json = JSONObject.parseObject(message);// 以下接收报警数据999999999999
					if (json != null) {
						JSONArray items = json.getJSONArray("items");
						for (int i = 0; i < items.size(); i++) {
							JSONObject item = items.getJSONObject(i);
							String chId = item.getString("ch_id");

							logger.info("Mq发回22222的的chId=========" + chId);
						}
					}
					try {
						devAlarmQueue.put(json);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
			// 自动回复队列应答 -- RabbitMQ中的消息确认机制
			devAlarmChannel.basicConsume(DEV_ALARM_QUEUE, true, alarmConsumer);
			new AlarmDataThread().start();

			/** 数据消息 */
			// 创建一个通道
			Channel devDataChannel = connection.createChannel();
			// 声明要关注的队列
			devDataChannel.queueDeclare(DEV_DATA_QUEUE, true, false, false, null);
			System.out.println("Customer Data Waiting Received messages");
			// DefaultConsumer类实现了Consumer接口，通过传入一个频道，
			// 告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery
			Consumer devDataConsumer = new DefaultConsumer(devDataChannel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					String message = new String(body, "UTF-8");
					System.out.println("Customer Received '" + message + "'");
					JSONObject json = JSONObject.parseObject(message);
					try {
						devDataQueue.put(json);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
			// 自动回复队列应答 -- RabbitMQ中的消息确认机制
			devDataChannel.basicConsume(DEV_DATA_QUEUE, true, devDataConsumer);
			new ChDataThread().start();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

}
