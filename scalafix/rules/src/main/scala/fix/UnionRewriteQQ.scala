package fix

import scalafix.v1._
import scala.meta._

class UnionRewriteQQ extends SemanticRule("UnionRewriteQQ") {
  override val description =
    """Quasiquotes in Scalafix. Replacing unionAll with union"""
  override val isRewrite = true

  override def fix(implicit doc: SemanticDocument): Patch = {

//    println("~~~~~>")
//    println("Tree.syntax: " + doc.tree.syntax)
//    println("Tree.structure: " + doc.tree.structure)
//    println("Tree.structureLabeled: " + doc.tree.structureLabeled)
//    println("<~~~~~")

    def getType(t: Tree): Unit = {
      t.traverse { case t @ q"..$mods val ..$patsnel: $tpeopt = $expropt" =>
//        println(s"~~~~~> t = $t")
        val tt = t.symbol.info.get.signature
//        println(
//          s"~~~~~> t.symbol.info.get.signature = ${t.symbol.info.get.signature}"
//        )
      }
    }

    def matchOnTree(t: Tree): Patch = {
      t.collect { case tt: Term =>
        getType(tt)
        tt match {
          case q"""unionAll""" =>
            Patch.replaceTree(tt, q"""union""".toString())
          case _ => Patch.empty
        }
      }.asPatch
    }

//    println(s"~~~~~> start getType ")
//    getType(doc.tree)

    println(s"~~~~~> start matchOnTree ")

    matchOnTree(doc.tree)
  }

}
