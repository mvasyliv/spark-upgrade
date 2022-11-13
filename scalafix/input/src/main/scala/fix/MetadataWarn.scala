/*
rule = MetadataWarn
 */

package fix
import org.apache.spark.sql.types.Metadata
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions.col

class MetadataWarn {
  def inSource(sparkSession: SparkSession, df: DataFrame): Unit = {
    val ndf = df.select(
      col("id"),
      col("v").as(
        "newV",
        Metadata.fromJson( // assert: MetadataWarn
          """{"desc": "replace old V"}"""
        )
      )
    )
  }
}
