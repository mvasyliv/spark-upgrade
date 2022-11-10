package fix

import org.apache.spark.sql.{SparkSession, Dataset}

class GroupByKeyWarn {
  def inSource(sparkSession: SparkSession): Unit = {
    import sparkSession.implicits._
    val ds1 = List("Person 1", "Person 2", "User 1", "User 2", "User 3", "Test", "Test Test").toDS()
  }
}
