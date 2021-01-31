package me.kjs.mall.common.dto;

import com.querydsl.core.QueryResults;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CommonPage<T> {
    private long contentCount;
    private long totalCount;
    private int nowPage;
    private long maxPage;
    private List<T> contents;

    public CommonPage(QueryResults<T> results, int page) {
        contents = results.getResults();
        nowPage = page;
        totalCount = results.getTotal();
        contentCount = results.getLimit();
        maxPage = ((results.getTotal() - 1) / results.getLimit());
        if (maxPage < 0) {
            maxPage = 0;
        }
    }

    public CommonPage updateContent(List list) {
        return new CommonPage(contentCount, totalCount, nowPage, maxPage, list);
    }
}
