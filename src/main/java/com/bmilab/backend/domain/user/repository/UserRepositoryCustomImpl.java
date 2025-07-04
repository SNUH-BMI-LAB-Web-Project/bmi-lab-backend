package com.bmilab.backend.domain.user.repository;


import com.bmilab.backend.domain.leave.entity.QUserLeave;
import com.bmilab.backend.domain.projectcategory.entity.ProjectCategory;
import com.bmilab.backend.domain.projectcategory.entity.QProjectCategory;
import com.bmilab.backend.domain.user.dto.query.UserDetailQueryResult;
import com.bmilab.backend.domain.user.dto.query.UserInfoQueryResult;
import com.bmilab.backend.domain.user.dto.query.UserSearchCondition;
import com.bmilab.backend.domain.user.entity.QUser;
import com.bmilab.backend.domain.user.entity.QUserInfo;
import com.bmilab.backend.domain.user.entity.QUserProjectCategory;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.entity.UserInfo;
import com.bmilab.backend.domain.user.enums.UserAffiliation;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<UserDetailQueryResult> findUserDetailsById(Long userId) {
        QUser user = QUser.user;
        QUserInfo userInfo = QUserInfo.userInfo;
        QUserLeave userLeave = QUserLeave.userLeave;

        UserDetailQueryResult result = queryFactory
                .select(Projections.constructor(
                        UserDetailQueryResult.class,
                        user,
                        userLeave,
                        userInfo
                ))
                .from(user)
                .innerJoin(userInfo).on(userInfo.user.eq(user))
                .innerJoin(userLeave).on(userLeave.user.eq(user))
                .where(user.id.eq(userId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Page<UserInfoQueryResult> findAllUserInfosPagination(Pageable pageable) {
        QUser user = QUser.user;
        QUserInfo userInfo = QUserInfo.userInfo;
        QUserProjectCategory userProjectCategory = QUserProjectCategory.userProjectCategory;
        QProjectCategory category = QProjectCategory.projectCategory;

        List<Long> userIds = queryFactory
                .select(user.id)
                .from(user)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if (userIds.isEmpty()) {
            PageableExecutionUtils.getPage(Collections.emptyList(), pageable, () -> 0L);
        }

        List<Tuple> rows = queryFactory
                .select(user, userInfo, category)
                .from(user)
                .innerJoin(userInfo).on(userInfo.user.eq(user))
                .leftJoin(userProjectCategory).on(userProjectCategory.user.eq(user))
                .leftJoin(category).on(userProjectCategory.category.eq(category))
                .where(user.id.in(userIds))
                .fetch();

        Map<Long, UserInfoQueryResult> resultMap = new LinkedHashMap<>();

        rows.forEach(row -> {
            User u = row.get(user);
            UserInfo ui = row.get(userInfo);
            ProjectCategory c = row.get(category);

            Long userId = Objects.requireNonNull(u).getId();
            UserInfoQueryResult existing = resultMap.get(userId);

            if (existing == null) {
                List<ProjectCategory> categories = new ArrayList<>();
                if (c != null) {
                    categories.add(c);
                }

                resultMap.put(userId, new UserInfoQueryResult(u, ui, categories));
            } else {
                if (c != null && !existing.getCategories().contains(c)) {
                    existing.getCategories().add(c);
                }
            }
        });

        List<UserInfoQueryResult> results = new ArrayList<>(resultMap.values());

        Long total = queryFactory
                .select(user.count())
                .from(user)
                .fetchOne();

        return PageableExecutionUtils.getPage(results, pageable, () -> total != null ? total : 0L);
    }


    @Override
    public List<User> searchUsersByCondition(UserSearchCondition condition) {

        QUser user = QUser.user;
        QUserInfo userInfo = QUserInfo.userInfo;
        QUserProjectCategory userProjectCategory = QUserProjectCategory.userProjectCategory;
        QProjectCategory category = QProjectCategory.projectCategory;

        BooleanBuilder conditionBuilder = new BooleanBuilder();

        String filterBy = condition.filterBy();
        String filterValue = condition.filterValue();


        if (filterBy != null && filterValue != null) {
            switch (filterBy) {
                case "name" -> conditionBuilder.and(user.name.containsIgnoreCase(filterValue));
                case "email" -> conditionBuilder.and(user.email.containsIgnoreCase(filterValue));
                case "department" -> conditionBuilder.and(user.department.containsIgnoreCase(filterValue));
                case "organization" -> conditionBuilder.and(user.organization.containsIgnoreCase(filterValue));
                case "affiliation" -> conditionBuilder.and(user.affiliation.eq(UserAffiliation.valueOf(filterValue.toUpperCase())));
                case "projectName" -> conditionBuilder.and(category.name.containsIgnoreCase(filterValue));
                case "seatNumber" -> conditionBuilder.and(userInfo.seatNumber.containsIgnoreCase(filterValue));
                case "phoneNumber" -> conditionBuilder.and(userInfo.phoneNumber.containsIgnoreCase(filterValue));
            }
        }

        OrderSpecifier<String> orderSpecifier = "desc".equalsIgnoreCase(condition.sort())
                ? user.name.desc()
                : user.name.asc();

        return queryFactory
                .select(user)
                .from(user)
                .leftJoin(userInfo).on(userInfo.user.id.eq(user.id))
                .leftJoin(userProjectCategory).on(userProjectCategory.user.id.eq(user.id))
                .leftJoin(userProjectCategory.category, category)
                .where(conditionBuilder)
                .orderBy(orderSpecifier)
                .distinct()
                .fetch();
    }

}