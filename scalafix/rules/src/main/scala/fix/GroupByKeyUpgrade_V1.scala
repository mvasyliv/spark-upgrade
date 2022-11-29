package fix
import scalafix.v1._

import scala.meta._
import scala.reflect.runtime.universe.showRaw

/*
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
! Diff:                                             !
! List(Literal(Constant("value")))                  !
! vs                                                !
! Lit(String("value"))                              !
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

===
 * println(showRaw(q"""col("value")"""))
Apply(Ident(TermName("col")), List(Literal(Constant("value"))))

 *
 *
 * =====
"fun": {
  "type": "Term.Name",
  "pos": {
    "start": 221,
    "end": 224
  },
  "value": "col"
},
"args": [
  {
    "type": "Lit.String",
    "pos": {
      "start": 225,
      "end": 232
    },
    "value": "value",
    "syntax": "\"value\""
  }
]
}*/

class GroupByKeyUpgrade_V1 extends SemanticRule("GroupByKeyUpgrade_V1") {
  override val description: String =
    """Renamed column "value" to "key" when groupByKey.
      |Using Quasiquotes """.stripMargin
  override val isRewrite = true

  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree.collect { case tt @ _ =>
//      println(s"~~~~~> tt = $tt")
      Patch.empty
    }.asPatch
  }
}
