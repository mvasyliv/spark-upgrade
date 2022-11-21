/*
rule=RuleQuasiquotes
 */
package fix

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{col, upper}

object RuleQuasiquotes {
  def inSource_1(sparkSession: SparkSession): Unit = {
    import sparkSession.implicits._

    val ds = List("Person 1", "Person 2", "User 1", "User 3", "test")
      .toDS()
      .groupByKey(i => i.substring(0, 3))
      .count()
      .select(col("value"))

    val ds1 =
      List("Paerson 1", "Person 2", "User 1", "User 2", "test", "gggg")
        .toDS()
        .groupByKey(l => l.substring(0, 3))
        .count()
        .withColumnRenamed("value", "newName")

    val ds2 =
      List("Paerson 1", "Person 2", "User 1", "User 2", "test", "gggg")
        .toDS()
        .groupByKey(l => l.substring(0, 3))
        .count()
        .select('value, $"count(1)")

    val ds6 =
      List("Paerson 1", "Person 2", "User 1", "User 2", "test", "gggg")
        .toDS()
        .groupByKey(l => l.substring(0, 3))
        .count()
        .withColumn("value", upper(col("value")))
  }
}
