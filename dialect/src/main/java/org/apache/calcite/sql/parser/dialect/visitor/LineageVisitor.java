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
package org.apache.calcite.sql.parser.dialect.visitor;

import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlInsert;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.ddl.SqlCreateTable;
import org.apache.calcite.sql.parser.dialect.hive.ddl.HiveCreateTable;
import org.apache.calcite.sql.util.SqlBasicVisitor;

import org.checkerframework.checker.nullness.qual.Nullable;

public class LineageVisitor extends SqlBasicVisitor<@Nullable Boolean> {

  public @Nullable Boolean visit(final SqlInsert call) {
    final SqlNode targetTable = call.getTargetTable();
    final SqlNode source = call.getSource();
    return false;
  }

  public @Nullable Boolean visit(final SqlCreateTable call) {
    if (call instanceof HiveCreateTable) {
      System.out.println("EXTERNAL=" + ((HiveCreateTable) call).external);
    }
    return false;
  }


  @Override
  public @Nullable Boolean visit(final SqlCall call) {
    // Handler creates a new copy of 'call' only if one or more operands
    // change.
    if (call instanceof SqlInsert) {
      visit((SqlInsert) call);
    }
    if (call instanceof SqlCreateTable) {
      visit((SqlCreateTable) call);
    }
    return false;
  }
}
