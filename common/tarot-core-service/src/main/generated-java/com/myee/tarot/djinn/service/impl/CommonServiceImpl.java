package com.myee.tarot.djinn.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.myee.djinn.dto.NotificationDTO;
import com.myee.djinn.dto.ShopDetail;
import com.myee.djinn.dto.VersionInfo;
import com.myee.djinn.rpc.RemoteException;
import com.myee.djinn.server.operations.CommonService;
import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.catalog.service.DeviceUsedService;
import com.myee.tarot.core.service.TransactionalAspectAware;
import com.myee.tarot.merchant.domain.Merchant;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.profile.domain.Address;
import com.myee.tarot.resource.dao.NotificationDao;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Martin on 2016/9/6.
 */
@Service
public class CommonServiceImpl implements CommonService, TransactionalAspectAware {

	private static final Logger LOG = LoggerFactory.getLogger(CommonServiceImpl.class);
	@Autowired
	private DeviceUsedService deviceUsedService;
	@Value("${cleverm.push.dirs}")
	private String DOWNLOAD_HOME;

	@Override
	public Boolean isConnection() throws RemoteException {
		return true;
	}

	@Override
	public VersionInfo latestVersion(String jsonArgs) throws RemoteException {
		LOG.info("jsonArgs= {}  DOWNLOAD_HOME={}", jsonArgs, DOWNLOAD_HOME);
		VersionInfo info = new VersionInfo();
		try {
			JSONObject object = JSON.parseObject(jsonArgs);
			String name = object.getString("name");
			String type = object.getString("type");
			String orgId = object.getString("orgId");
			StringBuilder sb = new StringBuilder();
			LOG.info("================ request info  name:{} type:{} orgId:{}", name, type, orgId);
			if ("app".equals(type) || "ipc".equals(type)) {
				sb.append(DOWNLOAD_HOME).append(File.separator).append(orgId).append(File.separator).append(type).append(File.separator).append(name).append(File.separator).append("VersionInfo.xml");
			} else {

			}
			LOG.info("========File path {} " + sb.toString());
			File file = new File(sb.toString());
			if (file.exists()) {
				info = readfile(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info("Error Message {} " + e.getMessage());
		}
		return info;
	}

	@Override
	public VersionInfo latestVersion() throws RemoteException {
		return null;
	}

	@Override
	public VersionInfo latestVersion(long orgId) throws RemoteException {
		return null;
	}

	@Override
	public ShopDetail ownerShop() throws RemoteException {
		return null;
	}

	@Override
	public ShopDetail ownerShop(String consistentId) throws RemoteException {
		LOG.info(" ownerShop consistentId = {}",consistentId);
		DeviceUsed deviceUsed = deviceUsedService.getByBoardNo(consistentId);
		MerchantStore merchantStore = deviceUsed.getStore();
//		List<TableType> tableTypeList = tableTypeService.listByStore(merchantStore.getId());
//		for (TableType tableType : tableTypeList) {
//			tableType.setStore(null);
//		}
//		Map<String, String> map = new HashMap<String, String>();
//		if (merchantStore != null) {
//			map.put("shopInfo", JSON.toJSONString(merchantStore));
//			map.put("tableType", JSON.toJSONString(tableTypeList));
//		}
		ShopDetail shopDetail =toDto(merchantStore);
		shopDetail.setOpeningTime(toHourString(merchantStore.getTimeOpen()) + "~" + toHourString(merchantStore.getTimeClose()));
		return shopDetail;
	}

	//TODO
	@Override
	public boolean receiveNotice(NotificationDTO notification) throws RemoteException {
		return false;
	}

	private VersionInfo readfile(File file) {
		SAXReader saxReader = new SAXReader();
		JSONObject object = new JSONObject();
		VersionInfo versionInfo = new VersionInfo();
		try {
			Document document = saxReader.read(file);
			Element root = document.getRootElement();
			// 迭代
			for (Iterator iter = root.elementIterator(); iter.hasNext(); ) {
				Element e = (Element) iter.next();
				object.put(e.getName(),e.getData());
			}
			versionInfo = JSON.parseObject(object.toJSONString(),VersionInfo.class);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return versionInfo;
	}

	String toHourString(Date time) {
		if(null == time){
			return "";
		}
		return new DateTime(time.getTime()).toString("HH:mm");
	}

	public ShopDetail toDto(MerchantStore merchantStore) {
		ShopDetail shopDetail = new ShopDetail();
		shopDetail.setShopId(merchantStore.getId());
		shopDetail.setShopName(merchantStore.getName());
		shopDetail.setCode(merchantStore.getCode());
		shopDetail.setPhone(merchantStore.getPhone());
		shopDetail.setPostalCode(merchantStore.getPostalCode());
		Address address = merchantStore.getAddress();
		shopDetail.setAddress(address != null ? address.toString() : "");
		shopDetail.setRatings(merchantStore.getRatings());
		shopDetail.setCpp(merchantStore.getCpp());
		shopDetail.setDescription(merchantStore.getDescription());
		shopDetail.setExperience(merchantStore.isExperience());
		shopDetail.setShopType(merchantStore.getStoreType());
		Float score = merchantStore.getScore();
		shopDetail.setScore(score == null ? 0 : score );
		Merchant merchant = merchantStore.getMerchant();
		shopDetail.setMerchant(merchant != null ? JSON.toJSONString(merchant) : "");
		shopDetail.setClientId(merchant != null ? merchant.getId() : null);
		shopDetail.setClientName(merchant != null ? merchant.getName() : "");
		shopDetail.setOpeningTime(toHourString(merchantStore.getTimeOpen()) + "~" + toHourString(merchantStore.getTimeClose()));
		return shopDetail;
	}
}
