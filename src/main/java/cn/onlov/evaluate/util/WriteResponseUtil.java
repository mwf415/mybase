package cn.onlov.evaluate.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

public class WriteResponseUtil {

	/**
	 * 文件名编码
	 * @param request
	 * @param fileName
	 * @return
	 */
	public static String encodeFileName(HttpServletRequest request, String fileName){
		try {
			if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > -1) {  
			 fileName = URLEncoder.encode(fileName, "UTF-8");
			} else {  
				fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");  
			}  
		 } catch (UnsupportedEncodingException e) {
				e.printStackTrace();
		 }  
		return fileName;
	}
	

	
	
	/**
	 * 下载
	 * @param request
	 * @param response
	 * @param fileList
	 * @param fileName
	 */
	public static void writeZipResponse(HttpServletRequest request, HttpServletResponse response, List<File> fileList, String fileName) {

	 	String zipPath = request.getSession().getServletContext().getRealPath("/")+fileName+".zip";  
		 	
	 	File zipFile = new File(zipPath);
			 	
		InputStream is = null;  
		FileOutputStream fos2 = null;
		try {
		    fos2 = new FileOutputStream(zipFile);
		    //压缩
			ZipUtils.toZip(fileList, fos2);
			is = new FileInputStream(zipFile);   
			response.reset();   
			response.setContentType("multipart/form-data;charset=UTF-8");   
			fileName = encodeFileName(request,fileName);
			response.addHeader("Content-Disposition", "attachment;filename="+fileName+".zip");            
			byte[] b = new byte[1024];   
			int len;   
			while ((len=is.read(b)) >0) {   
			response.getOutputStream().write(b,0,len);   
			}  
			response.getOutputStream().flush();   
			response.getOutputStream().close();    
		} catch (Exception e) {     
			e.printStackTrace();     
		}finally{  
			if(is!=null){  
				try {  
					is.close();  
				} catch (IOException e) {  
					e.printStackTrace();  
				}   
			}  
			if(fos2!=null){
				try {
					fos2.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(zipFile!=null){  
				zipFile.delete();  
			}  
			for(File file:fileList){
				if(file!=null){
					file.delete();
				}
			}
		}  
	}  
	 

	//删除文件夹
	public static void delFolder(String folderPath) {  
	     try {  
	        delAllFile(folderPath); //删除完里面所有内容  
	        String filePath = folderPath;  
	        filePath = filePath.toString();  
	        File myFilePath = new File(filePath);
	        myFilePath.delete(); //删除空文件夹  
	     } catch (Exception e) {  
	       e.printStackTrace();   
	     }  
	}  
	
	//删除所有文件
	public static boolean delAllFile(String path) {  
	       boolean flag = false;  
	       File file = new File(path);  
	       if (!file.exists()) {  
	         return flag;  
	       }  
	       if (!file.isDirectory()) {  
	         return flag;  
	       }  
	       String[] tempList = file.list();  
	       File temp = null;  
	       for (int i = 0; i < tempList.length; i++) {  
	          if (path.endsWith(File.separator)) {  
	             temp = new File(path + tempList[i]);  
	          } else {  
	              temp = new File(path + File.separator + tempList[i]);  
	          }  
	          if (temp.isFile()) {  
	             temp.delete();  
	          }  
	          if (temp.isDirectory()) {  
	             delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件  
	             delFolder(path + "/" + tempList[i]);//再删除空文件夹  
	             flag = true;  
	          }  
	       }  
	       return flag;  
	     }  
	
	
	}  
	




