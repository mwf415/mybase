package cn.onlov.evaluate.util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.onlov.evaluate.util.module.annotation.CSVField;
import cn.onlov.evaluate.util.module.vo.TestVO;
import org.apache.log4j.Logger;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.google.common.collect.Lists;
 
/**
 * @Description: CSV工具类 
 * @author zhangxiaowei
 * @date 2018年8月15日
 */
public final class CSVUtils {
    
    /** 日志对象 **/
    private static final Logger logger = Logger.getLogger(CSVUtils.class);
 
    /**
     * @Description: 导出CSV文件
     * @param outputStream
     * @param beans 实体对象集合
     */
    public static <T> void exportCSVFile(OutputStream outputStream, List<T> beans) {  
        CsvWriter writer = null;
        try {
            // 生成输出流
            writer = new CsvWriter(outputStream, ',', Charset.forName("GBK"));         
            // 获取内容
            List<String[]> contents = getStringArrayFromBean(beans); 
            // 写入内容
            for (String[] each : contents) {  
                writer.writeRecord(each);  
            }  
        } catch (Exception e) {
            logger.error("导出CSV文件失败", e);
        } finally {
            if (writer != null) {
                writer.close();          
            }
            if(outputStream!=null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    logger.error("关闭文件流失败！", e);
                }
            }
        }
    }
    
    /**
     * 生成CSV文件
     * @param filePath 文件保存路径，例如：D:/temp/test.csv
     * @param beans 实体对象集合
     */
    public static <T> void createCSVFile(String filePath, List<T> beans) {  
        CsvWriter writer = null;
        try {
            File file = new File(filePath);
            if(!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            // 生成文件
            writer = new CsvWriter(filePath, ',', Charset.forName("GBK"));         
            // 获取内容
            List<String[]> contents = getStringArrayFromBean(beans); 
            // 写入内容
            for (String[] each : contents) {  
                writer.writeRecord(each);  
            }  
        } catch (Exception e) {
            logger.error("生成CSV文件失败", e);
        } finally {
            if (writer != null) {
                writer.close();          
            }
        }
    }  
 
    /**
     * 读取CSV文件内容
     * 
     * @param filePath 文件存放的路径，如：D:/csv/xxx.csv
     * @param bean 类类型
     * @return List<T>
     */
    public static <T> List<T> readCSVFile(String filePath, Class<T> bean) {
        List<String[]> dataList = new ArrayList<String[]>();  
        CsvReader reader = null;
        try {  
            // 创建CSV读对象 例如:CsvReader(文件路径，分隔符，编码格式);  
            reader = new CsvReader(filePath, ',', Charset.forName("GBK"));  
            if (reader != null) {
                // 跳过表头，如果需要表头的话，这句可以忽略  
                //reader.readHeaders();  
                // 逐行读入除表头的数据  
                while (reader.readRecord()) {  
                    dataList.add(reader.getValues());   
                }  
                if (!dataList.isEmpty()) {
                    // 数组转对象
                    return getBeanFromStringArray(dataList, bean);
                }
            }
        } catch (Exception e) {  
            logger.error("读取CSV文件失败", e);  
        } finally {
            if (reader != null) {
                reader.close(); 
            }
        }
        return Collections.emptyList();
    }
    
    /**
     * 泛型实体转换为数组
     * @param beans
     * @return List<String[]>
     */
    private static <T> List<String[]> getStringArrayFromBean(List<T> beans) {   
        List<String[]> result = new ArrayList<String[]>();        
        Class<? extends Object> cls = beans.get(0).getClass();  
        Field[] declaredFields = cls.getDeclaredFields();
        List<Field> annoFields = new ArrayList<Field>();   
        // 筛选出标有注解的字段
        for (Field field : declaredFields) {
            CSVField anno = field.getAnnotation(CSVField.class);
            if (anno != null) { 
                annoFields.add(field);  
            }
        } 
        // 获取注解的值，即内容标题
        String[] title = new String[annoFields.size()];     
        for (int i = 0; i < annoFields.size(); i++) {  
            title[i] = annoFields.get(i).getAnnotation(CSVField.class).name();
        }  
        result.add(title);  
        try {
            // 获取内容
            for (T t : beans) {  
                String[] item = new String[annoFields.size()]; 
                int index = 0;
                for (Field field : annoFields) {
                    field.setAccessible(true);
                    Object value = field.get(t);
                    if (value == null) {
                        item[index] = ""; 
                    } else {
                        item[index] = value.toString();  
                    }
                    index ++;
                }
                result.add(item);         
            }
        } catch (Exception e) {
            logger.info("实体对象转数组失败", e);
        }  
        return result;  
    }
    
    /**
     * @param dataList 数组转为对象集合
     * @param bean
     * @return List<T>
     */
    private static <T> List<T> getBeanFromStringArray(List<String[]> dataList, Class<T> bean) {
        List<T> list = new ArrayList<>();
        List<Map<String, String>> records = getRecords(dataList);
        Field[] fields = bean.getDeclaredFields();
        T entity = null;
        try {
            for(Map<String, String> record: records) {
                entity = bean.newInstance();
                for(Field field: fields) {
                    field.setAccessible(true);
                    CSVField annotation = field.getAnnotation(CSVField.class);
                    if(annotation!=null) {
                        String name = annotation.name();
                        String c = record.get(name);
                        Class<?> fieldType = field.getType();
                        if ((Integer.TYPE == fieldType)
                                || (Integer.class == fieldType)) {
                            field.set(entity, Integer.parseInt(c));
                        } else if (String.class == fieldType) {
                            field.set(entity, String.valueOf(c));
                        } else if ((Long.TYPE == fieldType)
                                || (Long.class == fieldType)) {
                            field.set(entity, Long.valueOf(c));
                        } else if ((Float.TYPE == fieldType)
                                || (Float.class == fieldType)) {
                            field.set(entity, Float.valueOf(c));
                        } else if ((Short.TYPE == fieldType)
                                || (Short.class == fieldType)) {
                            field.set(entity, Short.valueOf(c));
                        } else if ((Double.TYPE == fieldType)
                                || (Double.class == fieldType)) {
                            field.set(entity, Double.valueOf(c));
                        } else if (Character.TYPE == fieldType) {
                            if ((c != null) && (c.length() > 0)) {
                                field.set(entity, Character.valueOf(c.charAt(0)));
                            }
                        }
                    }
                }
                list.add(entity);
            }
        } catch (Exception e) {
            logger.error("创建实体失败", e);
        }
        return list;
    }
    
    /**
     * @Description: 获取表头与record的对应关系
     * @param dataList
     * @return
     */
    private static <T> List<Map<String, String>> getRecords(List<String[]> dataList) {
        List<Map<String, String>> list = new ArrayList<>();
        String[] titles = dataList.get(0);
        dataList.remove(0);
        for (String[] values : dataList) {
            Map<String, String> titleMap = new HashMap<>();
            for (int i = 0; i < values.length; i++) {
                titleMap.put(titles[i], values[i]);
            }
            list.add(titleMap);
        }
        return list;
    }
    
    public static void main(String[] args) {
        List<TestVO> list = Lists.newArrayList();
        TestVO TestVO = new TestVO();
        TestVO.setAge(25);
        TestVO.setBirthDay("1995-12-12");
        TestVO.setName("zhangxw");
        TestVO TestVO1 = new TestVO();
        TestVO1.setAge(25);
        TestVO1.setBirthDay("1995-12-12");
        TestVO1.setName("zhangxw1");
        TestVO TestVO2 = new TestVO();
        TestVO2.setAge(25);
        TestVO2.setBirthDay("1995-12-12");
        TestVO2.setName("zhangxw2");
        list.add(TestVO2);
        list.add(TestVO);
        list.add(TestVO1);
        createCSVFile("D:\\sftp\\test\\s\\test.csv", list);
        List<? extends TestVO> readFile = readCSVFile("D:\\sftp\\test\\s\\test.csv", TestVO.getClass());
        System.out.println("11");
    }
    
}