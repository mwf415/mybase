package cn.onlov.evaluate.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * ZipUtils
 * @author 	ZENG.XIAO.YAN
 * @date 	2017年11月19日 下午7:16:08
 * @version v1.0
 */
public class ZipUtils {
	
	private static final int  BUFFER_SIZE = 2 * 1024;
	

	
	/**
	 * 压缩成ZIP 方法2
	 * @param srcFiles 需要压缩的文件列表
	 * @param out 	        压缩文件输出流
	 * @throws RuntimeException 压缩失败会抛出运行时异常
	 */
	public static void toZip(List<File> srcFiles , OutputStream out)throws RuntimeException {
		long start = System.currentTimeMillis();
		ZipOutputStream zos = null ;
		try {
			zos = new ZipOutputStream(out);
			for (File srcFile : srcFiles) {
				byte[] buf = new byte[BUFFER_SIZE];
				zos.putNextEntry(new ZipEntry(srcFile.getName()));
				int len;
				FileInputStream in = new FileInputStream(srcFile);
				while ((len = in.read(buf)) != -1){
					zos.write(buf, 0, len);
				}
				zos.closeEntry();
				in.close();
			}
			long end = System.currentTimeMillis();
			System.out.println("压缩完成，耗时：" + (end - start) +" ms");
		} catch (Exception e) {
			throw new RuntimeException("zip error from ZipUtils",e);
		}finally{
			if(zos != null){
				try {
					zos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	
	 
	
	public static void main(String[] args) throws Exception {
		/** 测试压缩方法1  */
/*		FileOutputStream fos1 = new FileOutputStream(new File("c:/mytest01.zip"));
		ZipUtils.toZip("D:/log", fos1,true);*/
		
		/** 测试压缩方法2  */
		List<File> fileList = new ArrayList<>();
		fileList.add(new File("E:/skuSpace/text1.txt"));
		fileList.add(new File("E:/skuSpace/text2.txt"));
		FileOutputStream fos2 = new FileOutputStream(new File("E:/skuSpace/mytest.zip"));
		ZipUtils.toZip(fileList, fos2);
	}
}