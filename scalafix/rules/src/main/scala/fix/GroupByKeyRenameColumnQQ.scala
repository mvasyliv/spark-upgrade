package fix
import scalafix.v1._
import scala.meta._

class GroupByKeyRenameColumnQQ
    extends SemanticRule("GroupByKeyRenameColumnQQ") {

  override val description =
    """Renaming column "value" with "key" when have Dataset.groupByKey(...).count()"""

  override val isRewrite = true

  override def fix(implicit doc: SemanticDocument): Patch = {

    println(s"~~~~~> GroupByKeyRenameColumnQQ")
    println("Tree.syntax: " + doc.tree.syntax)
    println("Tree.structure: " + doc.tree.structure)
    println("Tree.structureLabeled: " + doc.tree.structureLabeled)
    println(s"<~~~~~ GroupByKeyRenameColumnQQ")

    def isGroupByKeyAndCount(t: Term): Boolean = {
      val isGroupByKey = t.collect { case q"""groupByKey""" => true }
      val isCount = t.collect { case q"""count""" => true }
      (isGroupByKey.isEmpty.equals(false) && isGroupByKey.head.equals(
        true
      )) && (isCount.isEmpty.equals(false) && isCount.head.equals(true))
    }

    def matchOnTerm(t: Term): Patch = {
      t match {
        case q""""value"""" => Patch.replaceTree(t, q""""key"""".toString())
        case q"""'value"""  => Patch.replaceTree(t, q"""'key""".toString())
        case q"""col("value")""" =>
          Patch.replaceTree(t, q"""col("key")""".toString())
        case q"""upper(col("value"))""" =>
          Patch.replaceTree(t, q"""upper(col("key"))""".toString())
        case q"""upper(col('value))""" =>
          Patch.replaceTree(t, q"""upper(col('key))""".toString())
        case _ => Patch.empty
      }
    }

    def matchOnTree(t: Tree): Patch = {
      // TODO: Add checking only for Dataset
      t match {
        case _ @Term.Apply(tr, params) =>
          println(s"~~~~~> tr = $tr")
          println(s"~~~~~> tr signature = ${tr.symbol.info.get.signature}")
          if (isGroupByKeyAndCount(tr)) params.map(matchOnTerm).asPatch
          else Patch.empty
        case elem @ _ =>
          elem.children match {
            case Nil => Patch.empty
            case _ =>
              elem.children.map(matchOnTree).asPatch
          }
      }
    }

    matchOnTree(doc.tree)
  }
}
