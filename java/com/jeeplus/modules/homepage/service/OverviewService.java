package com.jeeplus.modules.homepage.service;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.jeeplus.common.persistence.MapEntity;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.homepage.dao.ChartDao;
import com.jeeplus.modules.homepage.dao.OverviewDao;
import com.jeeplus.modules.homepage.dao.StatisticsDao;
import com.jeeplus.modules.homepage.entity.*;
import com.jeeplus.modules.settings.dao.TOrgDao;
import com.jeeplus.modules.settings.entity.TDevice;
import com.jeeplus.modules.settings.entity.TOrg;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.mysql.fabric.xmlrpc.base.Array;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2018-12-20.
 */
@Service
@Transactional(readOnly = true)
public class OverviewService extends CrudService<OverviewDao, Statistics> {
	@Autowired
	OverviewDao overviewDao;
	@Autowired
	private RedisTemplate redisTemplate;

	public MapEntity overviewDate() {

		String userId = UserUtils.getUser().getId();
		MapEntity entity = new MapEntity();
		entity.put("codeList", overviewDao.codeList());// 设备类型集合
		entity.put("pdfListById", overviewDao.pdfListById(userId)); // 用户归属配电房集合
		entity.put("devCountTop5", overviewDao.devCountTop5(userId)); // 设备数量top5
		entity.put("statisticsCount", overviewDao.statisticsCount(userId)); // 配电房统计

		return entity;
	}
	
	
	public MapEntity pdfListById() {
	    
		String userId = UserUtils.getUser().getId();
		MapEntity entity = new MapEntity();
		entity.put("pdfListById", overviewDao.pdfListById(userId)); // 用户归属配电房集合

//		List<MapEntity> pdfListById = overviewDao.pdfListById(userId);
//		for (MapEntity en : pdfListById) {
//			Object objRealData = redisTemplate.opsForValue().get("cdpdf_devdataproc_realdata_" + "6651749099468251136");
//			String status = "0";
//			String allwarn = "0";
//			if (objRealData != null) {
//				JSONObject jobj_real_data = JSONObject.parseObject(objRealData.toString());
//				String real_value = jobj_real_data.getString("real_value");
//				Long l_real_value = Long.parseLong(real_value);
//				status = l_real_value + "";
//			}
//			en.put("status", status);
//			
//           //获取所有通道报警
//			List<MapEntity> chIds = overviewDao.getChIds((String) en.get("id"));//获取所的配电房下chId
//			for (MapEntity mapEntity : chIds) {
//				Object objchData = redisTemplate.opsForValue()
//						.get("cdpdf_devdataproc_realdata_" + mapEntity.get("chId"));
//				if (objRealData != null) {
//					JSONObject jobj_real_data = JSONObject.parseObject(objRealData.toString());
//					String warn = jobj_real_data.getString("warn");
//					if (!warn.equals("0")) {
//						allwarn = warn;
//					}
//				}
//			}
//			en.put("warn", allwarn);
//		}
		return entity;
	}

	public List<MapEntity> alarmCountTop10(String beginDate, String endDate) {

		return overviewDao.alarmCountTop10(UserUtils.getUser().getId(), beginDate, endDate);// 设备告警top10
	}

	public List<MapEntity> getDetailsByOrgId(String orgId) {

		return overviewDao.getDetailsByOrgId(orgId);
	}

}
