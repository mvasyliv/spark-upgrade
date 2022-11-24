package fix
import scalafix.v1._

import scala.meta.{Term, _}
class RuleQuasiquotes extends SemanticRule("RuleQuasiquotes") {

  override val description =
    """Quasiquotes in Scalafix. Renaming column "value" with "key" when have Dataset.groupByKey(...).count()"""
  override val isRewrite = true
  override def fix(implicit doc: SemanticDocument): Patch = {
//    println("~~~~~>")
//    println("Tree.syntax: " + doc.tree.syntax)
//    println("Tree.structure: " + doc.tree.structure)
//    println("Tree.structureLabeled: " + doc.tree.structureLabeled)
//    println("<~~~~~")

    val datasetMatcher =
      SymbolMatcher.normalized(
        "org.apache.spark.sql.{DataFrame, Dataset, SparkSession}"
      )

    def matchOnTerm(t: Term): Patch = {
      t match {
        case q""""value"""" =>
          Patch.replaceTree(t, q""""key"""".toString())
        case q"""upper(col("value"))""" =>
          Patch.replaceTree(t, q"""upper(col("key"))""".toString())
        case q"""col("value")""" =>
          Patch.replaceTree(t, q"""col("key")""".toString())
        case q"""'value""" =>
          Patch.replaceTree(t, q"""'key""".toString())
        case _ => Patch.empty
      }
    }

    def matchOnTree(t: Tree): Patch = {
      t match {
        case _ @Term.Apply(_, params) =>
          println(s"~~~~~>RQ params = $params")
          params.map(matchOnTerm).asPatch
        case elem @ _ =>
          elem.children match {
            case Nil => Patch.empty
            case _ =>
              elem.children.map { i =>
                println(s"~~~~~> elem.children i = $i")
                println("<~~~~~")
                val isGroupByKey = i.collect {
                  case trm @ Term.Name("groupByKey") =>
                    println(s"~~~~~> trm groupByKey = $trm")
                    trm match {
                      case q"""groupByKey""" => true
                      case _                 => false
                    }
                }
                val isCount = i.collect { case trm @ Term.Name("count") =>
                  println(s"~~~~~> trm count = $trm")
                  trm match {
                    case q"""count""" => true
                    case _            => false
                  }
                }

                println(
                  s"~~~~~> isGroupByKey =  $isGroupByKey  isCount = $isCount"
                )

                if (
                  isGroupByKey.equals(List(true)) && isCount.equals(List(true))
                ) matchOnTree(i)
                else Patch.empty
              }.asPatch
          }
      }

    }

    def isGroupByKeyAndCount(t: Term): Boolean = {
      val isGroupByKey = t.collect { case q"groupByKey" =>
        true
      }
      val isCount = t.collect { case _ @ q"count" => true }
      (isGroupByKey.isEmpty.equals(false) && isGroupByKey.head.equals(
        true
      )) && (isCount.isEmpty.equals(false) && isCount.head.equals(true))
    }

    def matchOnTreeT(t: Tree): Patch = {
      t match {
        case _ @Term.Apply(f, params) =>
          if (isGroupByKeyAndCount(f))
            params.map(matchOnTerm).asPatch
          else Patch.empty
        case elem @ _ =>
          elem.children match {
            case Nil => Patch.empty
            case _   => elem.children.map(matchOnTreeT).asPatch
          }
      }

    }

    matchOnTreeT(doc.tree)
//    matchOnTree(doc.tree)

  }
}
