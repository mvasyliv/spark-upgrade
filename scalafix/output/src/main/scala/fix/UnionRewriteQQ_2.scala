package fix

import org.apache.spark.sql.Dataset

class UnionRewriteQQ_2() {
  def inSource1(ds1: Dataset[String], ds2: Dataset[String]): Unit = {
    val res = ds1.unionAll(ds2)
  }
}
