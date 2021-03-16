package org.apache.calcite.sql.parser.support.lineage;

import java.util.List;
import java.util.Objects;
import org.apache.calcite.sql.parser.support.lineage.Edge.OpType;

public class LineageRelation<T extends Vertex> {
    /**
     * table节点
     */
    private List<T> tables;

    /**
     * table边关系
     */
    private List<Relation> tableEdges;

    /**
     * column节点
     */
    private List<T> columns;

    /**
     * table边关系
     */
    private List<Relation> columnEdges;

    public LineageRelation(List<T> tables, List<Relation> tableEdges, List<T> columns, List<Relation> columnEdges) {
        this.tables = tables;
        this.tableEdges = tableEdges;
        this.columns = columns;
        this.columnEdges = columnEdges;
    }

    public List<T> getTables() {
        return tables;
    }

    public List<Relation> getTableEdges() {
        return tableEdges;
    }

    public List<T> getColumns() {
        return columns;
    }

    public List<Relation> getColumnEdges() {
        return columnEdges;
    }

    public static class Relation {
        private List<Integer> sources;
        private List<Integer> targets;
        private String expr;
        private OpType opType;

        public Relation(List<Integer> sources, List<Integer> targets, String expr, OpType opType) {
            this.sources = sources;
            this.targets = targets;
            this.expr = expr;
            this.opType = opType;
        }

        public List<Integer> getSources() {
            return sources;
        }

        public List<Integer> getTargets() {
            return targets;
        }

        public String getExpr() {
            return expr;
        }

        public OpType getOpType() {
            return opType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Relation relation = (Relation) o;
            return Objects.equals(sources, relation.sources) &&
                    Objects.equals(targets, relation.targets);
        }

        @Override
        public int hashCode() {
            return Objects.hash(sources, targets);
        }
    }
}
