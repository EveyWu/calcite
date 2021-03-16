package org.apache.calcite.sql.parser.support.lineage;

import java.util.Collections;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class Edge<T extends Vertex> {

    public static enum OpType {
        UNKNOWN,
        QUERY,
        SUBQUERY,
        SELECT,
        CREATE,
        INSERT,
        UPDATE,
        MERGE,
        ALTER,
        DROP,
        TRUNCATE,
        LOAD,
        WITH,
        JOIN,
        UNION,
        LATERAL_VIEW,
        /**
         * 二元运算
         */
        BINARYOP,
        /**
         * 字段计算
         */
        METHOD,
    }


    private Set<T> sources;
    private Set<T> targets;
    private String expr;
    private OpType opType;


    public Edge(T source, T target, OpType opType) {
        this.sources = source != null ? Collections.singleton(source) : Collections.emptySet();
        this.targets = target != null ? Collections.singleton(target) : Collections.emptySet();
        this.opType = opType;
    }


    public Edge(Set<T> sources, Set<T> targets, OpType opType) {
        this.sources = sources != null ? sources : Collections.emptySet();
        this.targets = targets != null ? targets : Collections.emptySet();
        this.opType = opType;
    }


    public Edge(Set<T> sources, Set<T> targets, String expr, OpType opType) {
        this.sources = sources != null ? sources : Collections.emptySet();
        this.targets = targets != null ? targets : Collections.emptySet();
        this.expr = expr;
        this.opType = opType;
    }

    public Set<T> getSources() {
        return sources;
    }

    public Set<T> getTargets() {
        return targets;
    }

    public String getExpr() {
        return expr;
    }

    public OpType getOpType() {
        return opType;
    }

    @Override
    public String toString() {
        String sourceStr = StringUtils.join(sources, '|');
        String targetStr = StringUtils.join(targets, '|');
        return "[" + opType + "]" + sourceStr + " => " + targetStr;
    }
}
