package fix
import scalafix.v1._

import scala.meta.{Stat, _}
class RuleQuasiquotes extends SemanticRule("RuleQuasiquotes") {

  override val description = """Quasiquotes in Scalafix"""
  override val isRewrite = true
  override def fix(implicit doc: SemanticDocument): Patch = {
    def matchOnTree(t: Tree): Patch = {
      t.collect { case tt: Term =>
        tt match {
          case q""""value"""" =>
            Patch.replaceTree(tt, q""""key"""".toString())
          case q"""'value""" =>
            Patch.replaceTree(tt, q"""'key""".toString())
          case _ => Patch.empty
        }
      }.asPatch
    }
    matchOnTree(doc.tree)
  }
}
