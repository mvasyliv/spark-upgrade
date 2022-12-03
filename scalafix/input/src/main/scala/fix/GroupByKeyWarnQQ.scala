/*
rule=GroupByKeyWarnQQ
 */
package fix

import org.apache.spark.sql.SparkSession

class GroupByKeyWarnQQ {
  def inSource(sparkSession: SparkSession): Unit = {
    import sparkSession.implicits._
    val ds1 = List(
      "Person 1",
      "Person 2",
      "User 1",
      "User 2",
      "User 3",
      "Test",
      "Test Test"
    ).toDS()
      .groupByKey(l => l.substring(0, 3))
      .count()
  }
}
