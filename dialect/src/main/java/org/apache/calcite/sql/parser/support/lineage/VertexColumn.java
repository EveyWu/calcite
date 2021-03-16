package org.apache.calcite.sql.parser.support.lineage;

import java.util.List;
import java.util.Objects;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * vertex of column
 */
public class VertexColumn extends Vertex {

    public static enum ColumnType {
        UNKNOWN,
        FIELD,
        CHAR,
        NUMBER,
        TIMESTAMP,
        ALL,
    }

    private VertexTable table;
    private String alias;
    private String name;
    private String type;
    private String comment;
    private String expr;
    /**
     * map values场景 friend['name']，value=name
     */
    private List<String> values;
    /**
     * 字段|表达式|ALL*
     */
    private ColumnType columnType = ColumnType.FIELD;
    /**
     * get position from 0
     */
    private Integer position;


    public VertexTable getTable() {
        return table;
    }

    public void setTable(VertexTable table) {
        this.table = table;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getExpr() {
        return expr;
    }

    public void setExpr(String expr) {
        this.expr = expr;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public ColumnType getColumnType() {
        return columnType;
    }

    public void setColumnType(ColumnType columnType) {
        this.columnType = columnType;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VertexColumn column = (VertexColumn) o;
        return Objects.equals(table, column.table) &&
                Objects.equals(name, column.name) &&
                Objects.equals(values, column.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(table, name, values);
    }

    @Override
    public String toString() {
        String columnName = table.toString() + "." + name;
        columnName = CollectionUtils.isEmpty(values) ? columnName : columnName + "[" + StringUtils.join(values, ',') + "]";
        columnName = StringUtils.isBlank(alias) ? columnName : columnName + " AS " + alias;
        return columnName + "[" + columnType + "]";
    }
}
