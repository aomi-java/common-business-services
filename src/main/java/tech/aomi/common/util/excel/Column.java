package tech.aomi.common.util.excel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@Accessors(chain = true)
public class Column {

    /**
     * 标题
     */
    private String title;

    /**
     * 数据key
     */
    private String key;

    /**
     * 该列数据格式
     */
    private CellFormat cellFormat = CellFormat.TEXT;

    /**
     * 标题的首行坐标
     */
    private int width = 1;

    /**
     * 标题的末行坐标
     */
    private int height = 1;

    private Callback getValue;

    public Column() {
    }

    public Column(String title, String key) {
        this(title, key, CellFormat.TEXT);
    }

    public Column(String title, Callback getValue) {
        this(title, (String) null);
        this.getValue = getValue;
    }

    public Column(String title, String key, CellFormat cellFormat) {
        this.title = title;
        this.key = key;
        this.cellFormat = cellFormat;
    }


    public interface Callback {

        /**
         * @param record 当前行的数据类型
         * @return 返回的数据
         */
        Object execute(Object record);

    }
}
