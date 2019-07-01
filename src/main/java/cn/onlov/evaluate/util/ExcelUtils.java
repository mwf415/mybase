package cn.onlov.evaluate.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.onlov.evaluate.util.module.annotation.ExcelField;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/*
 * ExcelUtil工具类实现功能:
 * 导出时传入list<T>,即可实现导出为一个excel,其中每个对象Ｔ为Excel中的一条记录.
 * 导入时读取excel,得到的结果是一个list<T>.T是自己定义的对象.
 * 需要导出的实体对象只需简单配置注解就能实现灵活导出,通过注解您可以方便实现下面功能:
 * 1.实体属性配置了注解就能导出到excel中,每个属性都对应一列.
 * 2.列名称可以通过注解配置.
 * 3.导出到哪一列可以通过注解配置.
 * 4.鼠标移动到该列时提示信息可以通过注解配置.
 * 5.用注解设置只能下拉选择不能随意填写功能.
 * 6.用注解设置是否只导出标题而不导出内容,这在导出内容作为模板以供用户填写时比较实用.
 * 本工具类以后可能还会加功能,请关注我的博客: http://blog.csdn.net/lk_blog
 */
public final class ExcelUtils {

    public static <T> List<T> importExcel(String sheetName, InputStream input, Class<T> clazz) {
        List<T> list = new ArrayList<T>();
        try {
            Workbook book = new XSSFWorkbook(input);
            Sheet sheet = null;
            if (!sheetName.trim().equals("")) {
                sheet = book.getSheet(sheetName);// 如果指定sheet名,则取指定sheet中的内容.
            }
            if (sheet == null) {
                sheet = book.getSheetAt(0);// 如果传入的sheet名不存在则默认指向第1个sheet.
            }
            int rows = sheet.getLastRowNum();// 得到数据的行数
            if (rows > 0) {// 有数据时才处理
                Field[] allFields = clazz.getDeclaredFields();// 得到类的所有field.
                Map<Integer, Field> fieldsMap = new HashMap<Integer, Field>();// 定义一个map用于存放列的序号和field.
                for (Field field : allFields) {
                    // 将有注解的field存放到map中.
                    if (field.isAnnotationPresent(ExcelField.class)) {
                        ExcelField attr = field.getAnnotation(ExcelField.class);
                        int col = getExcelCol(attr.column());// 获得列号
                        // System.out.println(col + "====" + field.getName());
                        field.setAccessible(true);// 设置类的私有字段属性可访问.
                        fieldsMap.put(col, field);
                    }
                }
                for (int i = 1; i < rows; i++) {// 从第2行开始取数据,默认第一行是表头.
                    Row row = sheet.getRow(i);
                    short cells = row.getLastCellNum();// 得到一行中的所有单元格对象.
                    T entity = null;
                    for (int j = 0; j < cells; j++) {
                        String c = row.getCell(j).getStringCellValue();// 单元格中的内容.
                        if (c.equals("")) {
                            continue;
                        }
                        entity = (entity == null ? clazz.newInstance() : entity);// 如果不存在实例则新建.
                        // System.out.println(cells[j].getContents());
                        Field field = fieldsMap.get(j);// 从map中得到对应列的field.
                        // 取得类型,并根据对象类型设置值.
                        Class<?> fieldType = field.getType();
                        if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)) {
                            field.set(entity, Integer.parseInt(c));
                        } else if (String.class == fieldType) {
                            field.set(entity, String.valueOf(c));
                        } else if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
                            field.set(entity, Long.valueOf(c));
                        } else if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
                            field.set(entity, Float.valueOf(c));
                        } else if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {
                            field.set(entity, Short.valueOf(c));
                        } else if ((Double.TYPE == fieldType) || (Double.class == fieldType)) {
                            field.set(entity, Double.valueOf(c));
                        } else if (Character.TYPE == fieldType) {
                            if ((c != null) && (c.length() > 0)) {
                                field.set(entity, Character.valueOf(c.charAt(0)));
                            }
                        }

                    }
                    if (entity != null) {
                        list.add(entity);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return list;
    }

    /**
     * @param <T>
     * @Description: 对list数据源将其里面的数据导入到excel表单
     * @param list
     * @param sheetName
     *            工作表的名称
     * @param sheetSize
     *            每个sheet中数据的行数,此数值必须小于65536
     * @param output
     *            java输出流
     * @return boolean
     */
    public static <T> boolean exportExcel(List<T> list, String sheetName, int sheetSize, OutputStream output) {

        Class<? extends Object> clazz = list.get(0).getClass();
        Field[] allFields = clazz.getDeclaredFields();// 得到所有定义字段
        List<Field> fields = new ArrayList<Field>();
        Map<String, Field> fieldsMap = Maps.newHashMap();
        // 得到所有field并存放到一个list中.
        for (Field field : allFields) {
            if (field.isAnnotationPresent(ExcelField.class)) {
                fields.add(field);
                fieldsMap.put(field.getName(), field);
            }
        }

        XSSFWorkbook workbook = new XSSFWorkbook();// 产生工作薄对象
        XSSFCreationHelper creationHelper = workbook.getCreationHelper();

        CellStyle hlink_style = workbook.createCellStyle();
        Font hlink_font = workbook.createFont();
        hlink_font.setUnderline(Font.U_SINGLE);
        hlink_font.setColor(IndexedColors.BLUE.getIndex());
        hlink_style.setFont(hlink_font);

        // excel2003中每个sheet中最多有65536行,为避免产生错误所以加这个逻辑.
        if (sheetSize > 65536 || sheetSize < 1) {
            sheetSize = 65536;
        }
        double sheetNo = Math.ceil(list.size() / sheetSize);// 取出一共有多少个sheet.
        for (int index = 0; index <= sheetNo; index++) {
            XSSFSheet sheet = workbook.createSheet();// 产生工作表对象
            workbook.setSheetName(index, sheetName + index);// 设置工作表的名称.
            XSSFRow row;
            XSSFCell cell;// 产生单元格

            row = sheet.createRow(0);// 产生一行
            // 写入各个字段的列头名称
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                ExcelField attr = field.getAnnotation(ExcelField.class);
                int col = getExcelCol(attr.column());// 获得列号
                cell = row.createCell(col);// 创建列
                cell.setCellType(CellType.STRING);// 设置列中写入内容为String类型
                cell.setCellValue(attr.name());
                // 如果设置了提示信息则鼠标放上去提示.
                if (!attr.prompt().trim().equals("")) {
                    setHSSFPrompt(sheet, "", attr.prompt(), 1, 100, col, col);// 这里默认设了2-101列提示.
                }
                // 如果设置了combo属性则本列只能选择不能输入
                if (attr.combo().length > 0) {
                    setHSSFValidation(sheet, attr.combo(), 1, 100, col, col);// 这里默认设了2-101列只能选择不能输入.
                }

            }

            int startNo = index * sheetSize;
            int endNo = Math.min(startNo + sheetSize, list.size());
            // 写入各条记录,每条记录对应excel表中的一行
            for (int i = startNo; i < endNo; i++) {
                row = sheet.createRow(i + 1 - startNo);
                T vo = (T) list.get(i); // 得到导出对象.
                for (int j = 0; j < fields.size(); j++) {
                    Field field = fields.get(j);// 获得field.
                    field.setAccessible(true);// 设置实体类私有属性可访问
                    ExcelField attr = field.getAnnotation(ExcelField.class);
                    try {
                        // 根据ExcelVOAttribute中设置情况决定是否导出,有些情况需要保持为空,希望用户填写这一列.
                        if (attr.isExport()) {
                            cell = row.createCell(getExcelCol(attr.column()));// 创建cell
                            cell.setCellType(CellType.STRING);
                            cell.setCellValue(field.get(vo) == null ? "" : String.valueOf(field.get(vo)));// 如果数据存在就填入,不存在填入空格.
                            // 加入超链接
                            String hlink = attr.hlink().trim();
                            if (!"".equals(hlink)) {
                                XSSFHyperlink hyperlink = creationHelper.createHyperlink(HyperlinkType.URL);
                                if (hlink.indexOf("?") != -1) {
                                    String[] split = hlink.split("\\?");
                                    String uri = split[0];
                                    String paramsStr = split[1];
                                    String[] paramsArr = paramsStr.split("&");
                                    List<String> paramList = Lists.newArrayList();
                                    for (String p : paramsArr) {
                                        String[] split2 = p.split("=");
                                        String paramKey = split2[0];
                                        String fieldName = split2[1];
                                        Field f = fieldsMap.get(fieldName);
                                        String paramValue = f.get(vo) == null ? "" : f.get(vo).toString();
                                        paramList.add(paramKey + "=" + paramValue);
                                    }
                                    hlink = uri + "?" + StringUtils.join(paramList, "&");
                                }
                                hyperlink.setAddress(hlink);
                                cell.setCellStyle(hlink_style);
                                cell.setHyperlink(hyperlink);
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        try {
            output.flush();
            workbook.write(output);
            output.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Output is closed ");
            return false;
        }

    }

    /**
     * @Description: map导出工具类
     * @param list
     * @param fieldName
     * @param columnIt
     * @param sheetName
     * @param sheetSize
     * @param output
     * @return boolean
     */
    public static boolean exportExcel(List list, String[] fieldName, Object[] columnIt, String sheetName, int sheetSize,
            OutputStream output) {
        Workbook workbook = new XSSFWorkbook();// 产生工作薄对象
        if (sheetSize >= 65536) {
            sheetSize = 65536;
        }
        double sheetNo = Math.ceil(list.size() / sheetSize);
        for (int index = 0; index <= sheetNo; index++) {
            Sheet sheet = workbook.createSheet();// 产生工作表对象
            workbook.setSheetName(index, sheetName + index);// 设置工作表的名称.
            Row row = sheet.createRow(0);// 产生一行
            Cell cell;// 产生单元格

            // 写入各个字段的名称
            for (int i = 0; i < fieldName.length; i++) {
                cell = row.createCell(i); // 创建第一行各个字段名称的单元格
                cell.setCellType(CellType.STRING); // 设置单元格内容为字符串型
                // cell.setEncoding(XSSFCell.ENCODING_UTF_16);
                // //为了能在单元格中输入中文,设置字符集为UTF_16
                cell.setCellValue(fieldName[i]); // 给单元格内容赋值
            }

            int startNo = index * sheetSize;
            int endNo = Math.min(startNo + sheetSize, list.size());
            // 写入各条记录,每条记录对应excel表中的一行
            for (int i = startNo; i < endNo; i++) {
                row = sheet.createRow(i + 1 - startNo);
                HashMap map = (HashMap) list.get(i);
                for (int j = 0; j < columnIt.length; j++) {
                    cell = row.createCell(j);
                    cell.setCellType(CellType.STRING);
                    // cell.setEncoding(XSSFCell.ENCODING_UTF_16);
                    Object value = map.get(columnIt[j]);
                    if (value != null) {
                        cell.setCellValue(map.get(columnIt[j]).toString());
                    } else
                        cell.setCellValue("");
                }
            }
        }
        try {
            output.flush();
            workbook.write(output);
            output.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Output is closed ");
            return false;
        }

    }

    /**
     * 将EXCEL中A,B,C,D,E列映射成0,1,2,3
     * 
     * @param col
     */
    public static int getExcelCol(String col) {
        col = col.toUpperCase();
        // 从-1开始计算,字母重1开始运算。这种总数下来算数正好相同。
        int count = -1;
        char[] cs = col.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            count += (cs[i] - 64) * Math.pow(26, cs.length - 1 - i);
        }
        return count;
    }

    /**
     * 设置单元格上提示
     * 
     * @param sheet
     *            要设置的sheet.
     * @param promptTitle
     *            标题
     * @param promptContent
     *            内容
     * @param firstRow
     *            开始行
     * @param endRow
     *            结束行
     * @param firstCol
     *            开始列
     * @param endCol
     *            结束列
     * @return 设置好的sheet.
     */
    public static XSSFSheet setHSSFPrompt(XSSFSheet sheet, String promptTitle, String promptContent, int firstRow,
            int endRow, int firstCol, int endCol) {
        // 构造constraint对象
        DataValidationHelper dataValidationHelper = sheet.getDataValidationHelper();
        DataValidationConstraint dvConstraint = dataValidationHelper.createCustomConstraint("DD1");
        // 四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        // 数据有效性对象
        DataValidation data_validation_view = dataValidationHelper.createValidation(dvConstraint, regions);
        data_validation_view.createPromptBox(promptTitle, promptContent);
        data_validation_view.setShowPromptBox(true);
        sheet.addValidationData(data_validation_view);
        return sheet;
    }

    /**
     * 设置某些列的值只能输入预制的数据,显示下拉框.
     * 
     * @param sheet
     *            要设置的sheet.
     * @param textlist
     *            下拉框显示的内容
     * @param firstRow
     *            开始行
     * @param endRow
     *            结束行
     * @param firstCol
     *            开始列
     * @param endCol
     *            结束列
     * @return 设置好的sheet.
     */
    public static XSSFSheet setHSSFValidation(XSSFSheet sheet, String[] textlist, int firstRow, int endRow,
            int firstCol, int endCol) {
        // 加载下拉列表内容
        DataValidationHelper dataValidationHelper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = dataValidationHelper.createExplicitListConstraint(textlist);
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        // 数据有效性对象
        DataValidation data_validation_list = dataValidationHelper.createValidation(constraint, regions);
        sheet.addValidationData(data_validation_list);
        return sheet;
    }

    public static void main(String[] args) throws FileNotFoundException {
        // List<UserVO> list = Lists.newArrayList();
        // UserVO user1 = new UserVO();
        // user1.setId(1L);
        // user1.setName("张三");
        // user1.setNation("汉族");
        // user1.setPhone("18323111111");
        // user1.setSex("男");
        // user1.setPassword("123");
        // UserVO user2 = new UserVO();
        // user2.setId(3L);
        // user2.setName("李四");
        // user2.setNation("回族");
        // user2.setPhone("18323111112");
        // user2.setSex("");
        // user2.setPassword("1233");
        // list.add(user1);
        // list.add(user2);
        // FileOutputStream fileOutputStream = new FileOutputStream(new
        // File("D:\\upload\\export_report.xlsx"));
        // ExcelUtils.exportExcel(list, "导出报表", 100, fileOutputStream);
    }

}
