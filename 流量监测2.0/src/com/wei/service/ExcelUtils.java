package com.wei.service;



/**
 * 输出Excel
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import android.content.Context;
import android.widget.Toast;

public class ExcelUtils {
	public static WritableFont arial14font = null;

	public static WritableCellFormat arial14format = null;
	public static WritableFont arial10font = null;
	public static WritableCellFormat arial10format = null;
	public static WritableFont arial12font = null;
	public static WritableCellFormat arial12format = null;

	public final static String UTF8_ENCODING = "UTF-8";
	public final static String GBK_ENCODING = "GBK";

	public static void format() {
		try {
			arial14font = new WritableFont(WritableFont.ARIAL, 50, WritableFont.BOLD);
			arial14font.setColour(jxl.format.Colour.LIGHT_BLUE);
			arial14format = new WritableCellFormat(arial14font);
			arial14format.setAlignment(jxl.format.Alignment.CENTRE);
			arial14format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
			arial14format.setBackground(jxl.format.Colour.VERY_LIGHT_YELLOW);
			arial10font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
			arial10format = new WritableCellFormat(arial10font);
			arial10format.setAlignment(jxl.format.Alignment.CENTRE);
			arial10format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
			arial10format.setBackground(jxl.format.Colour.LIGHT_BLUE);
			arial12font = new WritableFont(WritableFont.ARIAL, 12);
			arial12format = new WritableCellFormat(arial12font);
			arial12format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
		}
		catch (WriteException e) {

			e.printStackTrace();
		}
	}

	/**
	 * 初始化Excel，创建表名称，有什么内容名称
	 * @param fileName
	 * @param colName
	 */
	public static void initExcel(String fileName, String[] colName) {
		format();
		WritableWorkbook workbook = null;//要创建一个可读写的工作簿（WritableWorkbook）
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			workbook = Workbook.createWorkbook(file);//创建一个可读写的副本
			WritableSheet sheet = workbook.createSheet("流量使用情况", 0);//excel中的表单名称
			sheet.addCell((WritableCell) new Label(0, 0, fileName, arial14format));//arial14format为格式
			for (int col = 0; col < colName.length; col++) {
				sheet.addCell(new Label(col, 0, colName[col], arial10format));//excel中每一项的名称（日期，食物支出等）
			}
			workbook.write();//写入，相当于刷新
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (workbook != null) {
				try {
					workbook.close();
				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 真正把数据写进Excel中
	 * @param objList
	 * @param fileName
	 * @param c
	 */
	@SuppressWarnings("unchecked")
	public static <T> void writeObjListToExcel(List<T> objList, String fileName, Context c) {
		if (objList != null && objList.size() > 0) {
			WritableWorkbook writebook = null;
			InputStream in = null;
			try {
				WorkbookSettings setEncode = new WorkbookSettings();
				setEncode.setEncoding(UTF8_ENCODING);
				in = new FileInputStream(new File(fileName));
				Workbook workbook = Workbook.getWorkbook(in);
				writebook = Workbook.createWorkbook(new File(fileName), workbook);
				WritableSheet sheet = writebook.getSheet(0);
				for (int j = 0; j < objList.size(); j++) {
					ArrayList<String> list=(ArrayList<String>) objList.get(j);
					for (int i = 0; i < list.size(); i++) {
						sheet.addCell(new Label(i, j+1, list.get(i), arial12format));
					}
				}
				writebook.write();
				Toast.makeText(c, "保存成功", Toast.LENGTH_SHORT).show();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				if (writebook != null) {
					try {
						writebook.close();
					}
					catch (Exception e) {
						e.printStackTrace();
					}

				}
				if (in != null) {
					try {
						in.close();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
	}

}
