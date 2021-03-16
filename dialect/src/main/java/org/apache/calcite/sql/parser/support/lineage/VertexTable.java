package org.apache.calcite.sql.parser.support.lineage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * vertex of table
 */
public class VertexTable extends Vertex {

    public static final String INSTANCE_NAME = "instance-";

    public static enum TableType {
        TABLE,
        VIEW,
        MATERIALIZED_VIEW,
        LATERAL_VIEW,
        INSTANCE,
        END_SELECT,
        EMPTY
    }

    public static enum InstanceType {
        WITH,
        JOIN,
        UNION,
        SUBQUERY,
        LATERAL_VIEW
    }

    private String name;
    private String alias;
    private String location;
    private TableType tableType = TableType.TABLE;
    private Boolean temporary = false;
    private Boolean external = false;
    private String expr;
    private InstanceType instanceType;
    /**
     * union/join/lateralView的关联表
     */
    private List<VertexTable> relations = new ArrayList<>();
    /**
     * lateralView字段
     */
    private List<VertexColumn> lateralViewColumns = new ArrayList<>();


    public VertexTable(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public VertexTable(String name) {
        this.name = name;
    }

    public VertexTable() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public TableType getTableType() {
        return tableType;
    }

    public void setTableType(TableType tableType) {
        this.tableType = tableType;
    }

    public Boolean getTemporary() {
        return temporary;
    }

    public void setTemporary(Boolean temporary) {
        this.temporary = temporary;
    }

    public String getExpr() {
        return expr;
    }

    public void setExpr(String expr) {
        this.expr = expr;
    }

    public Boolean getExternal() {
        return external;
    }

    public void setExternal(Boolean external) {
        this.external = external;
    }

    public InstanceType getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(InstanceType instanceType) {
        this.instanceType = instanceType;
    }

    public List<VertexTable> getRelations() {
        return relations;
    }

    public void setRelations(List<VertexTable> relations) {
        this.relations = relations;
    }

    public List<VertexColumn> getLateralViewColumns() {
        return lateralViewColumns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
       VertexTable that = (VertexTable) o;
        if (that.tableType == TableType.INSTANCE) {
            return Objects.equals(name + expr, that.name + that.expr);
        }
        return Objects.equals(name, that.name) &&
                Objects.equals(alias, that.alias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        String table = StringUtils.isBlank(alias) ? name : name + " AS " + alias;
        List<String> attributes = new ArrayList<>();
        if (tableType != TableType.TABLE) {
            attributes.add(tableType.name());
        }
        if (instanceType != null) {
            attributes.add(instanceType.name());
        }
        if (temporary) {
            attributes.add("tmp");
        }
        return CollectionUtils.isEmpty(attributes) ? table : table + "[" + StringUtils
            .join(attributes, ',') + "]";
    }



}
