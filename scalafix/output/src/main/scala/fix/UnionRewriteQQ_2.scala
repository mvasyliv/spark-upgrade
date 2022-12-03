package fix

import org.apache.spark.sql.{Dataset, SparkSession}

class UnionRewriteQQ_2 {
  def isSource_1(ds1: Dataset[String], ds2: Dataset[String]): Unit = {
    val res = ds1.union(ds2)
  }

  def inSource2(spark: SparkSession): Unit = {
    import spark.implicits._
    val df1 = Seq("Test 1", "Test 2").toDF("col1")
    val df2 = Seq("User 1", "User 2").toDF("col1")
    val res = df1.union(df2)
  }

}
