package tech.aomi.common.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 内部封装的分页对象
 *
 * @author Sean createAt 2016/1/18
 */
@Getter
@Setter
public class PageResult<T> implements java.io.Serializable {

    private static final long serialVersionUID = -5309387050012144808L;

    private List<T> content = new ArrayList<>();

    private Long totalElements;

    private Integer totalPages;

    private Integer number;

    private Integer numberOfElements;

    private Integer size;

    private Boolean first;

    private Boolean last;

    private Object value;

    public PageResult() {
    }

    public PageResult(Page<T> page) {
        if (CollectionUtils.isEmpty(page.getContent())) {
            this.content = Collections.emptyList();
        } else {
            this.content = page.getContent();
        }
        this.first = page.isFirst();
        this.last = page.isLast();
        this.number = page.getNumber();
        this.numberOfElements = page.getNumberOfElements();
        this.size = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }
}
