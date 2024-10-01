package com.example.ai_jeju.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRecommendation is a Querydsl query type for Recommendation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecommendation extends EntityPathBase<Recommendation> {

    private static final long serialVersionUID = 2050857057L;

    public static final QRecommendation recommendation = new QRecommendation("recommendation");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> recommendId = createNumber("recommendId", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QRecommendation(String variable) {
        super(Recommendation.class, forVariable(variable));
    }

    public QRecommendation(Path<? extends Recommendation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRecommendation(PathMetadata metadata) {
        super(Recommendation.class, metadata);
    }

}

