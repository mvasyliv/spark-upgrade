/*
rule = SparkAutoUpgrade
 */
package fix

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.col

object GroupByKeyUpgrade_V1 {
  def inSource_1(sparkSession: SparkSession): Unit = {
    import sparkSession.implicits._

    val ds = List("Person 1", "Person 2", "User 1", "User 3", "test")
      .toDS()
      .groupByKey(i => i.substring(0, 3))
      .count()
      .select(col("value"))
  }
}
