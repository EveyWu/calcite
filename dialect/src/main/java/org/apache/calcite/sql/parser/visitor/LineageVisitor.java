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
package org.apache.calcite.sql.parser.visitor;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlInsert;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.ddl.SqlCreateTable;
import org.apache.calcite.sql.util.SqlBasicVisitor;

import org.checkerframework.checker.nullness.qual.Nullable;

public class LineageVisitor extends SqlBasicVisitor<@Nullable Boolean> {

  public @Nullable Boolean visit(final SqlInsert call) {
    final SqlNode targetTable = call.getTargetTable();
    final SqlNode source = call.getSource();
    return false;
  }

  public @Nullable String visit(final SqlCreateTable call) {
    final String target = call.name.names.iterator().next();

    String source = null;
    if (call.query instanceof SqlSelect) {
      source = visit((SqlSelect) call.query);
    }
    if (source != null && target != null) {
      System.out.println(source + " => " + target);
    }
    return target;
  }

  public String visit(final SqlSelect call) {
    SqlNode from = call.getFrom();
    String source = null;
    String target = null;
    if (from instanceof SqlIdentifier) {
      target = ((SqlIdentifier) from).names.iterator().next();
    }
    if (from instanceof SqlBasicCall) {
      final @Nullable SqlNode[] operands = ((SqlBasicCall) from).getOperands();
      final SqlNode node0 = operands[0];
      final SqlNode node1 = operands[1];
      if (node0 instanceof SqlSelect) {
        source = visit((SqlSelect) node0);
      }
      if (node1 instanceof SqlIdentifier) {
        target = ((SqlIdentifier) node1).names.iterator().next();
      }
    }
    if (from instanceof SqlSelect) {
      target = "subquery-instance";
      source = visit((SqlSelect) from);
    }
    if (source != null && target != null) {
      System.out.println(source + " => " + target);
    }
    return target;
  }



  @Override
  public @Nullable Boolean visit(final SqlCall call) {
    if (call instanceof SqlCreateTable) {
      visit((SqlCreateTable) call);
    }
    if (call instanceof SqlInsert) {
      visit((SqlInsert) call);
    }
    if (call instanceof SqlSelect) {
      visit((SqlSelect) call);
    }
    return false;
  }
}
