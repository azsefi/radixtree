package com.rklaehn.radixtree

import reftree.core.RefTree
import language.implicitConversions
import reftree.core._

object RefTreeImplicits {

  private def id(x: AnyRef): String = if(x ne null) System.identityHashCode(x).toString else "null"

  private def stringNode(text: String): RefTree =
    RefTree.Ref(text, System.identityHashCode(text).toString, Seq.empty, true, false)

  implicit val unitRadixTreeToRefTree: ToRefTree[RadixTree[String, Unit]] =
    ToRefTree(tree => unitRadixTreeConvert(tree))

  private def unitRadixTreeConvert(tree: RadixTree[String, Unit]): RefTree = {
    tree match {
      case null =>
        RefTree.Null()
      case tree if tree.children.isEmpty =>
        RefTree.Ref("Node", id(tree), Seq(stringNode(tree.prefix)), false, false)
      case tree =>
        RefTree.Ref("Node", id(tree), Seq(stringNode(tree.prefix)) ++ tree.children.map(unitRadixTreeConvert), false, false)
    }
  }
}

object SimplifiedRefTreeImplicits {

  private def id(x: AnyRef): String = if(x ne null) System.identityHashCode(x).toString else "null"

  implicit val stringRadixTreeToRefTree: ToRefTree[RadixTree[String, String]] =
    ToRefTree(tree => stringRadixTreeConvert(tree))

  private def stringNode(text: String): RefTree =
    RefTree.Ref(text, System.identityHashCode(text).toString, Seq.empty, false, false)

  private def stringRadixTreeConvert(tree: RadixTree[String, String]): RefTree = {
    tree match {
      case null =>
        RefTree.Null()
      case tree if tree.children.isEmpty =>
        val valueChild = tree.valueOpt.toOption.toSeq.map(stringNode)
        RefTree.Ref(tree.prefix, id(tree), valueChild, true, false)
      case tree =>
        val valueChild = tree.valueOpt.toOption.toSeq.map(stringNode)
        RefTree.Ref(tree.prefix, id(tree), valueChild ++ tree.children.map(stringRadixTreeConvert), !valueChild.isEmpty, false)
    }
  }

  implicit val unitRadixTreeToRefTree: ToRefTree[RadixTree[String, Unit]] =
    ToRefTree(tree => unitRadixTreeConvert(tree))

  private def unitRadixTreeConvert(tree: RadixTree[String, Unit]): RefTree = {
    tree match {
      case null =>
        RefTree.Null()
      case tree if tree.children.isEmpty =>
        RefTree.Ref(tree.prefix, id(tree), Seq.empty, false, false)
      case tree =>
        RefTree.Ref(tree.prefix, id(tree), tree.children.map(unitRadixTreeConvert), false, false)
    }
  }
}


