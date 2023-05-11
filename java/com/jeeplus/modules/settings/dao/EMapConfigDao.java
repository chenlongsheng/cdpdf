package com.jeeplus.modules.settings.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jeeplus.common.persistence.MapEntity;
import com.jeeplus.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface EMapConfigDao {

	public List<Map<String, Object>> getChannelListByOrgId(@Param("orgId") String orgId);

	public List<Map<String, Object>> getDeviceListByOrgId(@Param("orgId") String orgId);

	// 获取device地图的type类型
	public List<MapEntity> eMapSelect(@Param("orgId") String orgId);

	// 获取channel地图的type类型
	public List<MapEntity> eMapChannelSelect(@Param("orgId") String orgId);

	// 获取配电房地图 设备通道类型
	List<MapEntity> getCodeList(@Param("orgId") String orgId);

	// 设备的
	List<MapEntity> devList(@Param("devTypeId") String devTypeId, @Param("orgId") String orgId);

	// 通道的查询
	List<MapEntity> chList(@Param("devTypeId") String devTypeId, @Param("typeId") String typeId,
			@Param("orgId") String orgId);

	// 修改设备xy
	void updateDevCoords(@Param("coordsX") String coordsX, @Param("coordsY") String coordsY, @Param("id") String id);

	// 修改通道xy
	void updateChCoords(@Param("coordsX") String coordsX, @Param("coordsY") String coordsY, @Param("id") String id);

	// 修改时先删除图片中设备id
	void deleteImageDevIdById(@Param("id") String id);

	// 添加设备归属图片
	void insertImageDevId(@Param("id") String id, @Param("pdfImageId") String pdfImageId,
			@Param("devChId") String devChId, @Param("typeId") String typeId);

	// 删除图片中设备id
	void deleteImageDevId(@Param("devChId") String devChId, @Param("typeId") String typeId);

	// 删除图片下小图标
	void deleteImageDev(@Param("pdfImageId") String pdfImageId);

	// 获取图片下配电房小图片集合
	List<MapEntity> selectImageDevList(@Param("pdfImageId") String pdfImageId, @Param("orgId") String orgId);

	MapEntity getImageDev(@Param("id") String id);

	void updatePdfImageContain(@Param(value = "officeImageId") String officeImageId,
			@Param(value = "containerWidth") String containerWidth,
			@Param(value = "containerHeight") String containerHeight);

	// 更新新图片
	void updateImageById(@Param(value = "image") String image, @Param(value = "imageId") String imageId);

}
