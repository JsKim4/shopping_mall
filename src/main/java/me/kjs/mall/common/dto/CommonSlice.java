package me.kjs.mall.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.type.QueryCondition;
import me.kjs.mall.common.type.YnType;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CommonSlice<T> {
    private int contentCount;
    private int nowPage;
    private YnType hasNext;
    private List<T> contents;

    public CommonSlice(List<T> contents, QueryCondition queryCondition) {
        nowPage = queryCondition.getPage();
        contentCount = queryCondition.getContents();
        hasNext = queryCondition.getContents() < contents.size() ? YnType.Y : YnType.N;
        if (hasNext == YnType.Y) {
            this.contents = contents.subList(0, contentCount);
        } else {
            this.contents = contents;
        }
    }

    public CommonSlice updateContent(List list) {
        return new CommonSlice(contentCount, nowPage, hasNext, list);
    }
}
