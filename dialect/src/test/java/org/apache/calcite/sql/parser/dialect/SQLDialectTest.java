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

package org.apache.calcite.sql.parser.dialect;

import java.io.Reader;

import org.apache.calcite.sql.SqlDialect.DatabaseProduct;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.SqlParserImplFactory;
import org.apache.calcite.sql.parser.dialect.hive.HiveParserImpl;
import org.apache.calcite.sql.parser.dialect.spark.SparkParserImpl;
import org.apache.calcite.sql.parser.visitor.LineageVisitor;
import org.apache.calcite.sql.parser.impl.SqlParserImpl;
import org.junit.jupiter.api.Test;

public class SQLDialectTest {



  /**
   * Implementors of custom parsing logic who want to reuse this test should
   * override this method with the factory for their extension parser.
   */
  protected SqlParserImplFactory parserImplFactory(DatabaseProduct product) {
    switch (product) {
    case HIVE:
      return HiveParserImpl.FACTORY;
    case SPARK:
      return SparkParserImpl.FACTORY;
    default:
      return SqlParserImpl.FACTORY;
    }
  }


  @Test
  void simpleSQL() throws SqlParseException {
    //String sql = "insert into newstu select id,name,age FROM (select id,name,age FROM stu) T left join T2 on T.id = T2.rid where age<20";
    //外表
    //String sql = "CREATE EXTERNAL TABLE list_bucket_multiple (col1 STRING, col2 int, col3 STRING)";
    //临时表
    String sql = "CREATE TEMPORARY TABLE list_bucket_multiple (col1 STRING, col2 int, col3 STRING)";

    //支持create comment
    //String sql ="CREATE EXTERNAL TABLE page_view(viewTime INT) COMMENT 'This is the staging page view table'";

    // 解析配置
    //SqlParser.Config config = SqlParser.configBuilder().setLex(Lex.MYSQL).build();
    SqlParser.Config config = SqlParser.config()
        //.withLex(Lex.MYSQL)
        //.withParserFactory(parserImplFactory(DatabaseProduct.MSSQL))
        .withParserFactory(parserImplFactory(DatabaseProduct.HIVE))
        .withCaseSensitive(false);
    SqlParser parser = SqlParser.create(sql, config);
    SqlNode node = parser.parseStmt();
    LineageVisitor visitor = new LineageVisitor();
    node.accept(visitor);
  }
}
