package com.elinxer.springcloud.platform.core.entity;

/**
 * 排序实体
 */
public interface SortSupportEntity extends Comparable<SortSupportEntity>, Entity {

    Long getSortIndex();

    void setSortIndex(Long sortIndex);

    @Override
    default int compareTo(SortSupportEntity support) {
        if (support == null) {
            return -1;
        }
        return Long.compare(getSortIndex() == null ? 0 : getSortIndex(),
                support.getSortIndex() == null ? 0 : support.getSortIndex());
    }
}
