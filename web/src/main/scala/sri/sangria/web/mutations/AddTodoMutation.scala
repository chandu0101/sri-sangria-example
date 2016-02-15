package sri.sangria.web.mutations

import sri.relay.mutation.RelayMutation
import sri.relay.query.RelayQL
import sri.relay.tools.RelayTypes.MutationFragment

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{literal => json}
import scala.scalajs.js.annotation.ScalaJSDefined
import scala.scalajs.js.{Any, Array, Dictionary, UndefOr}

@ScalaJSDefined
class AddTodoMutation(input: js.Dynamic) extends RelayMutation(input) {

  override def getMutation(): MutationFragment = {
    js.eval(RelayQL( """mutation{ addTodo }"""))
  }

  override def getVariables(): js.Object = json("text" -> props.text.toString)

  override def getFatQuery(): Any = js.eval(RelayQL(
    """
       fragment on AddTodoPayload {
              todoEdge
            }
    """))

//  override def getOptimisticResponse(): UndefOr[js.Object] = {
//    json("todoEdge" -> json("node" -> js.Dictionary("text" -> props.text))
//    )
//  }

  override def getConfigs(): Array[Dictionary[Any]] = {
    js.Array(
      js.Dictionary("type" -> "RANGE_ADD",
        "parentID" -> props.viewer.id,
        "connectionName" -> "todos",
        "edgeName" -> "todoEdge",
        "rangeBehaviors" -> js.Dictionary("" -> "prepend"))
    )
  }
}