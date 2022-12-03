package fix
import scalafix.v1._

import scala.meta._
import scala.tools.asm.Type.getType
class UnionRewriteQQ_2 extends SemanticRule("UnionRewriteQQ_2") {
  override val description =
    """Replacing unionAll with union only for Dataset and DataFrame"""
  override val isRewrite = true
  override def fix(implicit doc: SemanticDocument): Patch = {
//    println("~~~~~> UnionRewriteQQ_2")
//    println("Tree.syntax: " + doc.tree.syntax)
//    println("Tree.structure: " + doc.tree.structure)
//    println("Tree.structureLabeled: " + doc.tree.structureLabeled)
//    println("<~~~~~ UnionRewriteQQ_2")

    def getClassMethods(symbol: Symbol): Set[SymbolInformation] = {
//      println(s"~~~~~> symbol = $symbol")
      symbol.info.get.signature match {
        case ClassSignature(_, parents, _, declarations) =>
          val methods = declarations.filter(_.isMethod)
          val mName =
            declarations.map(m => m.displayName).filter(i => i != "<init>")
//          println(s"~~~~~> mName = $mName")
          methods.toSet
//          methods.toSet ++ parents.collect { case TypeRef(_, symbol, _) =>
//            getClassMethods(symbol)
//          }.flatten
        case _ => Set.empty
      }
    }

    def getMethodParameters(symbol: Symbol): Option[Map[String, Signature]] = {
      symbol.info.get.signature match {
        case signature @ MethodSignature(typeParameters, parameterLists, _) =>
          if (parameterLists.nonEmpty)
            Some(
              parameterLists
                .flatMap(p => p.map(p1 => p1.displayName -> p1.signature))
                .toMap
            )
          else None
        case _ => None
      }
    }

    var methods: Set[SymbolInformation] = Set.empty

    var vrlb: Option[Map[String, Signature]] = None

    def matchOnTree(t: Tree): Patch = {

      t.collect {
        case cls @ Defn.Class(
              _,
              className @ _,
              _,
              _,
              _
            ) =>
          methods = getClassMethods(className.symbol)
//          println(s"~~~~~> methods = $methods")
//          methods.foreach(println(_))

        case apply @ Defn.Def(
              _,
              unn @ Term.Name("inSource1"),
              _,
              params,
              _,
              _
            ) =>
//          println("~~~~~> start Defn.Def")
//          println(s"~~~~~> unn.symbol = ${unn.symbol}")
          vrlb = getMethodParameters(unn.symbol)
//          println("~~~~~> end")
        case ta @ Term.Apply(Term.Select(a, trm @ q"unionAll"), b) =>
//          println(s"~~~~~> b = $b")
//          println(s"~~~~~> vrlb = $vrlb")
          val v = vrlb.getOrElse(Map.empty)
//          val aType = if (v.nonEmpty) v.get(a.toString()) else None
//          println(s"~~~~~> aType = $aType for ${a.toString()}")
//          val bType = if (v.nonEmpty) v.get(b.head.toString()) else None
//          println(s"~~~~~> bType = $bType for ${b.head.toString()}")
//
//          if (
//            aType.get.toString().substring(0, 7).equals("Dataset") && bType.get
//              .toString()
//              .substring(0, 7)
//              .equals("Dataset")
//          ) {
//            println(
//              s"~~~~~> ${aType.get.toString().substring(0, 7)} and ${bType.get
//                .toString()
//                .substring(0, 7)} "
//            )
//            Patch.replaceTree(trm, q"""union""".toString())
//          } else Patch.empty

        case _ => Patch.empty
      }

      Patch.empty
    }

    matchOnTree(doc.tree)
  }

}
