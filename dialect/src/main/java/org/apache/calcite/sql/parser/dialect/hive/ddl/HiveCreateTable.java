/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.calcite.sql.parser.dialect.hive.ddl;

import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.ddl.SqlCreateTable;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.checkerframework.checker.nullness.qual.Nullable;

public class HiveCreateTable extends SqlCreateTable {

  public static enum Type {
    GLOBAL_TEMPORARY, LOCAL_TEMPORARY, TEMPORARY, SHADOW
  }


  /**
   * Whether "EXTERNAL TABLE" was specified.
   */
  public final boolean external;
  /**
   * Whether "TEMPORARY TABLE" was specified.
   */
  public final boolean temporary;

  /**
   * Creates a SqlCreateTable.
   *
   * @param pos
   * @param replace
   * @param external
   * @param temporary
   * @param ifNotExists
   * @param name
   * @param columnList
   * @param query
   */
  protected HiveCreateTable(SqlParserPos pos, boolean replace, boolean external,
                            boolean temporary, boolean ifNotExists, SqlIdentifier name,
                            @Nullable SqlNodeList columnList,
                            @Nullable SqlNode query) {
    super(pos, replace, ifNotExists, name, columnList, query);
    this.external = external;
    this.temporary = temporary;
  }

  @Override
  public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
    writer.keyword("CREATE");
    if (external) {
      writer.keyword("EXTERNAL");
    }
    if (temporary) {
      writer.keyword("TEMPORARY");
    }
    writer.keyword("TABLE");
    if (ifNotExists) {
      writer.keyword("IF NOT EXISTS");
    }
    name.unparse(writer, leftPrec, rightPrec);
    if (columnList != null) {
      SqlWriter.Frame frame = writer.startList("(", ")");
      for (SqlNode c : columnList) {
        writer.sep(",");
        c.unparse(writer, 0, 0);
      }
      writer.endList(frame);
    }
    if (query != null) {
      writer.keyword("AS");
      writer.newlineAndIndent();
      query.unparse(writer, 0, 0);
    }
  }
}
