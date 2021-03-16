package org.apache.calcite.sql.parser.support.lineage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.calcite.sql.parser.support.lineage.Edge.OpType;
import org.springframework.beans.BeanUtils;

/**
 * lineage graph info
 */
public class LineageGraph {
    //用来标识是否打包成功
    public final static int VERSION = 8;

    public static enum Type {
        COLUMN,
        TABLE
    }

    /**
     * lineage of table edges
     */
    private List<Edge> tableEdges;
    /**
     * lineage of column edges
     */
    private List<Edge> columnEdges;

    /**
     * 适配 insert as WITH  AS语句
     */
    private Map<String, VertexTable> withNameMap;


    public LineageGraph() {
        this.tableEdges = new ArrayList<>();
        this.columnEdges = new ArrayList<>();
    }

    public LineageGraph(List<Edge> tableEdges, List<Edge> columnEdges) {
        this.tableEdges = tableEdges != null ? tableEdges : Collections.emptyList();
        this.columnEdges = columnEdges != null ? columnEdges : Collections.emptyList();
    }

    public List<Edge> getTableEdges() {
        Map<String, VertexTable> withNameMap = getWithTable();
        if (!withNameMap.isEmpty()) {
            for (Edge edge : tableEdges) {
                updateWithTable(edge.getSources(), withNameMap);
                updateWithTable(edge.getTargets(), withNameMap);
            }
        }
        return tableEdges;
    }

    public List<Edge> getColumnEdges() {
        Map<String, VertexTable> withNameMap = getWithTable();
        if (!withNameMap.isEmpty()) {
            for (Edge edge : columnEdges) {
                updateWithColumn(edge.getSources(), withNameMap);
                updateWithColumn(edge.getTargets(), withNameMap);
            }
        }
        return columnEdges;
    }


    private Map<String, VertexTable> getWithTable() {
        if (withNameMap != null) {
            return withNameMap;
        }
        withNameMap = new HashMap<>();
        for (Edge edge : tableEdges) {
            if (edge.getOpType() != OpType.WITH) {
                continue;
            }
            Set<VertexTable> tables = edge.getTargets();
            for (VertexTable table : tables) {
                withNameMap.put(table.getAlias(), table);
            }
        }
        return withNameMap;
    }


    private void updateWithTable(Set<VertexTable> tables, Map<String, VertexTable> withNameMap) {
        Set<String> withNames = withNameMap.keySet();
        for (VertexTable table : tables) {
            if (withNames.contains(table.getName())) {
                VertexTable with = withNameMap.get(table.getName());
                BeanUtils.copyProperties(with, table);
            }
        }
    }

    private void updateWithColumn(Set<VertexColumn> columns, Map<String, VertexTable> withNameMap) {
        Set<String> withNames = withNameMap.keySet();
        for (VertexColumn column : columns) {
            if (withNames.contains(column.getTable().getName())) {
                VertexTable with = withNameMap.get(column.getTable().getName());
                column.setTable(with);
            }
        }
    }
}
