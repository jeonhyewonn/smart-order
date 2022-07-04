package com.example.smartorder.item.repository;

import com.example.smartorder.item.domain.Item;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.smartorder.item.domain.QItem.item;


@RequiredArgsConstructor
@Repository
public class ItemCustomRepositoryImpl implements ItemCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Item> findAll(Pageable pageable) {
        List<Item> results = this.queryFactory
                .selectFrom(item)
                .where(item.isDeleted.eq(false))
                .orderBy(item.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = this.queryFactory
                .select(item.count())
                .from(item)
                .where(item.isDeleted.eq(false));

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }
}
