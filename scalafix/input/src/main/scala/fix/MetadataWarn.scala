/*
rule=MetadataWarn
 */
package fix
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.types.Metadata // assert: MetadataWarn
class MetadataWarn {
  def inSource(df: DataFrame): Unit = {
    val newDF = df.select(
      col("id"),
      col("val")
        .as(
          "newNameVal",
          Metadata.fromJson( // assert: MetadataWarn
            """{"desc": "replace old V"}"""
          )
        )
    )
  }
}
