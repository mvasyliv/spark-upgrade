package fix

import scalafix.v1._
import scala.meta._

case class MetadataWarning(t: scala.meta.Tree) extends Diagnostic {
  override def position = t.pos
  override def message = """
      |In Spark 3.0, the column metadata
      |will always be propagated in the API Column.name and Column.as.
      |In Spark version 2.4 and earlier, the metadata of NamedExpression
      |is set as the explicitMetadata for the new column
      |at the time the API is called,
      |it won’t change even if the underlying NamedExpression changes metadata.
      |To restore the behavior before Spark 3.0,
      |you can use the API as(alias: String, metadata: Metadata) with explicit metadata.""".stripMargin
}

class MetadataWarn extends SemanticRule("MetadataWarn") {
  val matcher = SymbolMatcher.normalized("org.apache.spark.sql.types.Metadata")
  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree.collect { case matcher(s) =>
      Patch.lint(MetadataWarning(s))
    }
  }.asPatch
}
