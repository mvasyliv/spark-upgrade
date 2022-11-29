package fix

import scalafix.v1._
import scala.meta._

case class GroupByKeyWarning(tr: Term) extends Diagnostic {
  override def position: Position = tr.pos
  override def message: String =
    """In Spark 2.4 and below,
      |Dataset.groupByKey results to a grouped dataset with key attribute is wrongly named as “value”,
      |if the key is non-struct type, for example, int, string, array, etc.
      |This is counterintuitive and makes the schema of aggregation queries unexpected.
      |For example, the schema of ds.groupByKey(...).count() is (value, count).
      |Since Spark 3.0, we name the grouping attribute to “key”.
      |The old behavior is preserved under a newly added configuration
      |spark.sql.legacy.dataset.nameNonStructGroupingKeyAsValue with a default value of false.""".stripMargin
}

class GroupByKeyWarnQQ extends SemanticRule("GroupByKeyWarnQQ") {
  override val description = "GroupByKey warning."
  val matcher =
    SymbolMatcher.normalized("groupByKey")
//  org.apache.spark.sql.Dataset.
  override def fix(implicit doc: SemanticDocument): Patch = {
    println(s"~~~~~>")
    println("Tree.syntax: " + doc.tree.syntax)
    println("Tree.structure: " + doc.tree.structure)
    println("Tree.structureLabeled: " + doc.tree.structureLabeled)
    println(s"<~~~~~")
    def isDSGroupByKeyCount(t: Tree): Boolean = {
      val isToDS = t.collect { case q"""toDS""" => true }
      val isGroupByKey = t.collect { case q"""groupByKey""" => true }
      val isCount = t.collect { case q"""count""" => true }
      (isToDS.isEmpty.equals(false) && isToDS.head.equals(
        true
      )) && (isGroupByKey.isEmpty.equals(false) && isGroupByKey.head.equals(
        true
      )) && (isCount.isEmpty.equals(false) && isCount.head.equals(true))
    }

//    doc.tree match {
//      case _ @Term.Apply(tr, params) =>
//        println(s"~~~~~> tr = $tr")
//        println(s"~~~~~> params = $params")
//        if (isDSGroupByKeyCount(doc.tree)) {
//          println(s"~~~~~> isDSGroupByKeyCount = true")
//          Patch.lint(GroupByKeyWarning(doc.tree))
//        } else Patch.empty
//    }

    def isGroupByKeyAndCount(t: Term): Boolean = {
      val isGroupByKey = t.collect { case q"""groupByKey""" => true }
      val isCount = t.collect { case q"""count""" => true }
      (isGroupByKey.isEmpty.equals(false) && isGroupByKey.head.equals(
        true
      )) && (isCount.isEmpty.equals(false) && isCount.head.equals(true))
    }

    def matchOnTerm(t: Term): Patch = {
      t match {
        case q"""groupByKey""" =>
          println(s"~~~~~> groupByKey - is in")
          Patch.lint(GroupByKeyWarning(q"""groupByKey"""))
        case _ => Patch.empty
      }
    }

    def matchOnTree(t: Tree): Patch = {
      t match {
        case _ @Term.Apply(tr, args) =>
          println(s"~~~~~> tr = $tr")
//        println(s"~~~~~> args = $args")
//        args.collect { case l: Lit => Patch.lint(GroupByKeyWarning(l)) }
          if (isGroupByKeyAndCount(tr)) args.map(matchOnTerm).asPatch
          else Patch.empty
      }
    }

    matchOnTree(doc.tree)
  }

}
