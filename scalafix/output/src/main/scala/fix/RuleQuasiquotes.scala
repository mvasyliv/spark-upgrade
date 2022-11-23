package fix

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{col, upper}

object RuleQuasiquotes {
  def inSource_1(sparkSession: SparkSession): Unit = {
    import sparkSession.implicits._

    val ds = List("Person 1", "Person 2", "User 1", "User2", "Test").toDS()

    val ds11 =
      ds.groupByKey(c => c.substring(0, 3)).count().select(col("key"))

    val ds09 = List("Person 1", "Person 2", "User 1", "User 3", "test")
      .toDS()
      .groupByKey(i => i.substring(0, 3))
      .count()
      .select(col("key"))

    val ds00 = List("Person 1", "Person 2", "User 1", "User 3", "test")
      .toDS()
      .select(col("value"))

    val c = List("Person 1", "Person 2", "User 1", "User 3", "test")
      .toDS()
      .withColumnRenamed("value", "newColName")
      .count()

    val ds10 = List("Person 1", "Person 2", "User 1", "User 3", "test")
      .toDS()
      .groupByKey(i => i.substring(0, 3))
      .count()
      .select(col("key"))

    val ds1 = List("Person 1", "Person 2", "User 1", "User 3", "test")
      .toDS()
      .groupByKey(i => i.substring(0, 3))
      .count()
      .select('key)

    val ds2 = List("Person 1", "Person 2", "User 1", "User 3", "test")
      .toDS()
      .groupByKey(i => i.substring(0, 3))
      .count()
      .withColumn("key", upper(col("key")))

    val ds3 =
      List("Paerson 1", "Person 2", "User 1", "User 2", "test", "gggg")
        .toDS()
        .groupByKey(l => l.substring(0, 3))
        .count()
        .withColumnRenamed("key", "newName")

    val ds5 =
      List("Paerson 1", "Person 2", "User 1", "User 2", "test", "gggg")
        .toDS()
        .groupByKey(l => l.substring(0, 3))
        .count()
        .withColumn("newNameCol", upper(col("key")))

    val s = "value"
  }

  def inSource_2(sparkSession: SparkSession): Unit = {
    import sparkSession.implicits._

    val c = List("Person 1", "Person 2", "User 1", "User 3", "test")
      .toDS()
      .withColumnRenamed("value", "newColName")
      .count()

    val s = "value"
  }
}
