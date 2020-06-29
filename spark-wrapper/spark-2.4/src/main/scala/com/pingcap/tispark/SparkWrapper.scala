/*
 * Copyright 2019 PingCAP, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pingcap.tispark

import org.apache.spark.sql._
import org.apache.spark.sql.catalyst.expressions.{Alias, AttributeReference, Expression}
import org.apache.spark.sql.catalyst.plans.logical.{LogicalPlan, SubqueryAlias}
import org.apache.spark.sql.types.{DataType, Metadata}

object SparkWrapper {
  def getVersion: String = {
    "SparkWrapper-2.4"
  }

  def newSubqueryAlias(identifier: String, child: LogicalPlan): SubqueryAlias = {
    SubqueryAlias(identifier, child)
  }

  def newAlias(child: Expression, name: String): Alias = {
    Alias(child, name)()
  }

  def newAttributeReference(
      name: String,
      dataType: DataType,
      nullable: Boolean,
      metadata: Metadata): AttributeReference = {
    AttributeReference(name, dataType, nullable, metadata)()
  }

  def callExplainCommand(df: DataFrame): String = {
    import org.apache.spark.sql.execution.command.ExplainCommand
    df.sparkSession.sessionState
      .executePlan(ExplainCommand(df.queryExecution.logical))
      .executedPlan
      .executeCollect()
      .map(_.getString(0))
      .mkString("\n")
  }
}
