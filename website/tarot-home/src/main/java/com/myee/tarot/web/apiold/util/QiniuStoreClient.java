package com.myee.tarot.web.apiold.util;

import com.myee.tarot.apiold.view.UploadTokenView;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 七牛云存储接口
 */
public class QiniuStoreClient {

	private final static Logger logger = LoggerFactory.getLogger(QiniuStoreClient.class);
	
	private static String HTTP_API_URL = "http://upload.qiniu.com/";//通用接口
//	private static String ACCESSKEY = "3lIbo_FtEAGuWisBgi_xuqGNO4WpKdXel23Cn5-2";//accessKey，测试机
	private static String ACCESSKEY = "wJJB6Fvyd9T51SS98O1fMgtSu-mjEeC_h6bGSR7B";//正式机ak
//	private static String SECRETKEY = "xOSfZft5CB-hU53KupWQQ1OsHv434d2PyOKkGfAD";//secretKey，测试机
	private static String SECRETKEY = "dXO_IF4ihIVv1gt8cLAJ08o-eu7JazsoWF3IskYE";//正式机sk
//	private static String BUCKET = "shanghaizhaoke";//空间名称，测试机
	private static String BUCKET = "clever-m";//空间名称,正式机

	//文件类型
	private enum TypeOfFile{
		picture(1, "picture"),
		video(2, "video"),
		audio(3, "audio");
		
		int id;
		String fileType;
		
		TypeOfFile(int id, String fileType){
			this.id = id;
			this.fileType = fileType;
		}

		public int getId() {
			return id;
		}
		
		public String getFileType() {
			return fileType;
		}
		
		public static String fileType(int id) {
			TypeOfFile[] values = values();
			for (int i = 0; i < values.length; i++) {
				if (values[i].getId() == id) {
					return values[i].getFileType();
				}
			}
			return null;
		}
	}
	
	private QiniuStoreClient() {
	}

	/**
	 * @param userAccountId //用户账号ID
	 * @param fileType //上传的文件类型;1:图片，2:视频，3:音频
	 * @return uploadToken //返回生成的token
	 */
//	public static UploadTokenView getUploadToken(Integer userAccountId, String uploadKey, Integer fileType){
//		UploadTokenView vo = new UploadTokenView();
//		if(userAccountId == null|| fileType == null){
//			return null;
//		}
//		if(fileType.intValue() != 1 && fileType.intValue() != 2 && fileType.intValue() != 3){
//			return null;
//		}
//		if(MyStringUtils.isBlank(userAccountId.toString())
//				|| MyStringUtils.isBlank(fileType.toString())){
//			return null;
//		}
//		if(MyStringUtils.isBlank(uploadKey)){
//			uploadKey = TypeOfFile.fileType(fileType) + userAccountId + System.currentTimeMillis();//重新组装成上传的key
//		}
//		Auth auth = Auth.create(ACCESSKEY, SECRETKEY);
//		String uploadToken = auth.uploadToken(BUCKET, uploadKey, 3600, null, true);
//		String uploadTokenPreview = auth.uploadToken(BUCKET, uploadKey + PictrueSize.pictureType(1), 3600, null, true);
//		vo.setUploadToken(uploadToken);
//		vo.setUploadKey(uploadKey);
//		return vo;
//	}
	
	/**
	 * @param //userId //用户ID
	 * @param //fileType //上传的文件类型;1:图片，2:视频，3:音频
	 * @return Map<String, String> //返回生成的token和key
	 */
	public static UploadTokenView getUploadTokenAndKey(CommConfig commConfig){
//		if(userId == null|| fileType == null){
//			return null;
//		}
//		if(fileType.intValue() != 1 && fileType.intValue() != 2 && fileType.intValue() != 3){
//			return null;
//		}
//		if(MyStringUtils.isBlank(userId.toString())
//				|| MyStringUtils.isBlank(fileType.toString())){
//			return null;
//		}
//		StringBuffer uploadKey = new StringBuffer();
//		uploadKey.append(TypeOfFile.fileType(fileType));
//		uploadKey.append(clientId.toString()+"_");
//		uploadKey.append(orgId.toString()+"_");
//		uploadKey.append(userId.toString()+"_");
//		uploadKey.append(System.currentTimeMillis());

		Auth auth = Auth.create(commConfig.getQiniuAccesskey(), commConfig.getQiniuSecretkey());
		UploadTokenView view = new UploadTokenView();
		view.setUptoken(auth.uploadToken(commConfig.getQiniuBucket()));
//		view.setSave_key(uploadKey.toString());
		return view;
	}
	
	/**
	 * 
	 * @param keys 数组
	 * @return 返回, 1:成功, 0:失败
	 * @throws Exception
	 */
	public static int deleteFile(String[] keys, CommConfig commConfig){
		Auth auth = Auth.create(commConfig.getQiniuAccesskey(), commConfig.getQiniuSecretkey());
		BucketManager bucketManager = new BucketManager(auth);
		BucketManager.Batch ops = new BucketManager.Batch()
		        .delete(commConfig.getQiniuBucket(), keys);
		try {
		    Response r = bucketManager.batch(ops);
		    BatchStatus[] bs = r.jsonToObject(BatchStatus[].class);
		    for (BatchStatus b : bs) {
		    	if(b.code != 200 && b.code != 612){
		    		return 0;
		    	}else{
		    		return 1;
		    	}
		    }
		} catch (QiniuException e) {
		    Response r = e.response;
		    // 请求失败时简单状态信息
			logger.error("删除文件服务器的文件异常" + r.toString() ,e);
		    return 0;
		}
		return 0;
	}

	public static String getAccessKeyNo() {
		return ACCESSKEY;
	}

	public static String getSecretKey() {
		return SECRETKEY;
	}

	public static void main(String[] args) {
//		List<String> list = QiniuStoreClient.getUploadToken(40987, null, 1);
//		System.out.println("ssssssssssssuploadToken="+list.get(1));
//		System.out.println("ssssssssssss="+list.get(0));
		
//		Map<String, String> map = QiniuStoreClient.getUploadTokenPictrue(40987, 1);
//		System.out.println("ssssssssssssuploadToken="+map.get("uploadToken"));
//		System.out.println("ssssssssssss="+map.get("uploadKey"));
//		System.out.println("ssssssssssss2="+map.get("uploadTokenPreview"));
//		System.out.println("ssssssssssss3="+map.get("uploadPreviewKey"));

		String uploadFile = "F:/下载图片/015.jpg";
		File imgFile = new File(uploadFile);
		try{
			UploadManager uploadManager = new UploadManager();
//			UploadTokenView vo = QiniuStoreClient.getUploadTokenAndKey(100L, 100L, 100L, 1);
//			System.out.println("ssssssssssssuploadToken="+vo.getUptoken());
//			System.out.println("ssssssssssssuploadKey="+vo.getSave_key());
//			Response rp = uploadManager.put(imgFile, vo.getSave_key(), vo.getUptoken(), null, null, true);
//			System.out.println("ssssssssssssbodyString="+rp.bodyString());
		} catch (Exception e) {
			System.out.println(e.toString());
		}

//		String[] array = new String[]{""};
//		try {
//			QiniuStoreClient.deleteFile(array);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
