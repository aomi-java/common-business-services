package tech.aomi.common.util.excel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 陈奔Kylin(kylin9426 @ gmail.com) Create At 2019-03-01
 */
@Slf4j
public class ExcelUtils {

    private static final ObjectMapper defaultObjectMapper = new ObjectMapper();

    private static final int ROW_ACCESS_WINDOW_SIZE = 100;
    private static final int SHEET_MAX_ROW = 100000;

    public static Workbook createWorkbook(List<Column> columns, List<?> data) {
        Workbook workbook = new SXSSFWorkbook(ROW_ACCESS_WINDOW_SIZE);
        CellStyle headerCellStyle = getHeaderCellStyle(workbook);

        int pageRowNum = 0;
        Sheet sheet = null;

        long startTime = System.currentTimeMillis();
        LOGGER.info("开始处理excel文件。。。");

        for (int i = 0; i < data.size(); i++) {
            // 如果是每个sheet的首行
            if (i % SHEET_MAX_ROW == 0) {
                // 创建新的sheet
                sheet = createSheet(workbook, i);
                pageRowNum = 1; // 行号重置为0
                createHeader(sheet, headerCellStyle, columns);
            }
            // 创建内容
            Row row = sheet.createRow(pageRowNum++);
            createContent(workbook, row, columns, data.get(i));
        }
        LOGGER.info("处理文本耗时" + (System.currentTimeMillis() - startTime) + "ms");
        return workbook;
    }


    // 新建表格
    private static Sheet createSheet(Workbook workbook, int i) {
        int sheetNum = i / SHEET_MAX_ROW;
        workbook.createSheet("sheet" + sheetNum);
        //动态指定当前的工作表
        return workbook.getSheetAt(sheetNum);
    }

    // 设置总体表格样式
    private static CellStyle getHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    // 设置总体表格样式
    private static CellStyle getBodyCellStyle(Workbook workbook) {
        return workbook.createCellStyle();
    }


    // 创建表头
    private static void createHeader(Sheet sheet, CellStyle style, List<Column> columns) {
        Row row = sheet.createRow(0);
        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            Cell cell = row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(column.getTitle());
        }
    }

    // 创建正文
    private static void createContent(Workbook workbook, Row row, List<Column> columns, Object data) {
        JsonNode json = defaultObjectMapper.valueToTree(data);
        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);

            CellFormat cellFormat = column.getCellFormat();
            if (null == cellFormat) {
                cellFormat = CellFormat.TEXT;
            }

            Cell cell = row.createCell(i);
            CellStyle style = getBodyCellStyle(workbook);

            JsonNode value = getJsonValue(column, column.getKey(), data, json);
            if (null == value) {
                continue;
            }

            switch (cellFormat) {
                case INTEGER:
                    cell.setCellValue(value.asInt(0));
                    break;
                case DOUBLE:
                    cell.setCellValue(value.asDouble(0));
                    break;
                case PERCENT:
                    style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
                    cell.setCellValue(value.asDouble());
                    break;
                case DATETIME:
                    style.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy-MM-dd HH:mm:ss"));
                    try {
                        if (value.isLong()) {
                            cell.setCellValue(new Date(value.asLong()));
                        } else if (value.isTextual()) {
                            cell.setCellValue(DateUtils.parseDate(value.asText(), "yyyy-MM-dd HH:mm:ss"));
                        }
                    } catch (Exception e) {
                        LOGGER.error("设置时间失败: {}", e.getMessage(), e);
                    }
                    break;
                case TEXT:
                    cell.setCellValue(value.asText());
                    break;
            }
            cell.setCellStyle(style);

        }

    }

    private static JsonNode getJsonValue(Column column, String path, Object data, JsonNode json) {
        if (null != column.getGetValue()) {
            Object value = column.getGetValue().execute(data);
            if (null == value)
                return null;
            if (value instanceof JsonNode)
                return (JsonNode) value;
            Map<String, Object> tmp = new HashMap<>();
            tmp.put("tmp", value);
            return defaultObjectMapper.valueToTree(tmp).get("tmp");
        }
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        String[] keys = path.split("\\.");
        JsonNode value = json;
        for (String key : keys) {
            if (null == value)
                return null;
            value = value.get(key);
        }
        if (value instanceof NullNode) {
            return null;
        }
        return value;
    }

}
