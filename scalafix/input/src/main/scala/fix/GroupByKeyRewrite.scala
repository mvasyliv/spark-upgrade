/*
rule = GroupByKeyRewrite
 */
package fix

import org.apache.spark.sql.SparkSession

object GroupByKeyRewrite {
  def isSource1(sparkSession: SparkSession): Unit = {
    import sparkSession.implicits._
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
        .select($"value", $"count(1)")
  }
}
