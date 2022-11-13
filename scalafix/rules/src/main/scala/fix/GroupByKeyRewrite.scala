package fix

import scalafix.v1._
import scala.meta._

class GroupByKeyRewrite extends SemanticRule("GroupByKeyRewrite") {
  override val isRewrite = true
  override def fix(implicit doc: SemanticDocument): Patch = {
    val grpByKey = "groupByKey"
    val funcToDS = "toDS"
    val agrFunCount = "count"
    val oprCol = "withColumnRenamed"
    val colNameOld = "value"
    val colNameNew = "key"

    doc.tree.collect {
      case Term.Apply(
            Term.Select(
              Term.Apply(
                Term.Select(
                  Term.Apply(
                    Term.Select(
                      Term.Apply(
                        Term.Select(
                          _,
                          _ @Term.Name(fName)
                        ),
                        _
                      ),
                      _ @Term.Name(grpByKeyName)
                    ),
                    _
                  ),
                  _ @Term.Name(oprName)
                ),
                _
              ),
              _ @Term.Name(oprColumnName)
            ),
            List(oldColName @ Lit.String(valueOld), _)
          )
          if grpByKey
            .equals(grpByKeyName) && funcToDS.equals(fName) && agrFunCount
            .equals(oprName) && oprCol.equals(oprColumnName) && colNameOld
            .equals(valueOld) =>
        Patch.replaceTree(oldColName, "\"".concat(colNameNew).concat("\""))
    }.asPatch
  }
}
