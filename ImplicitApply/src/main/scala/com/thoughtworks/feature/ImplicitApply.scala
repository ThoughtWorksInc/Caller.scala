package com.thoughtworks.feature

import scala.reflect.macros.whitebox
import scala.language.dynamics
import scala.language.experimental.macros
import scala.util.control.NonFatal

/** A dependent type class that apply `F` with implicit values as parameters.
  *
  * = Imports =
  *
  * {{{
  * import com.thoughtworks.feature.ImplicitApply._
  * }}}
  *
  * This will enable the [[com.thoughtworks.feature.ImplicitApply.ImplicitApplyOps.implicitApply implicitApply]] method for any functions
  *
  * @tparam F The function type to be implicitly apply
  *
  * @example Given a function `f` that requires an `Ordering[Int]`
  *
  *          {{{
  *          val f = { x: Ordering[Int] =>
  *            "OK"
  *          }
  *          }}}
  *
  *          Then `f` can implicitly apply as long as its parameter is implicitly available,
  *
  *          {{{
  *          f.implicitApply should be("OK")
  *          }}}
  *
  * @note You can optionally add an implicit modifier on the function parameter.
  *
  *       {{{
  *       val f = { implicit x: Ordering[Int] =>
  *         "OK"
  *       }
  *       f.implicitApply should be("OK")
  *       }}}
  *
  *       It is very useful when you create a curried function.
  *
  *       {{{
  *       def g[A] = { (i: A, j: A) => implicit x: Ordering[A] =>
  *         import x._
  *         if (i > j) {
  *           s"$i is greater than $j"
  *         } else {
  *           s"$i is not greater than $j"
  *         }
  *       }
  *
  *       g(1, 2).implicitApply should be("1 is not greater than 2")
  *       }}}
  *
  * @author 杨博 (Yang Bo) &lt;pop.atry@gmail.com&gt;
  */
trait ImplicitApply[F] {
  type Out

  def apply(f: F): Out
}

object ImplicitApply {

  type Aux[F, Out0] = ImplicitApply[F] {
    type Out = Out0
  }

  object Runtime {
    private[Runtime] type Id[+A] = A
    def narrow(a: Any): Id[a.type] = a
  }

  implicit final class ImplicitApplyOps[F](f: F) {
    def implicitApply(implicit implicitApply: ImplicitApply[F]): implicitApply.Out = implicitApply(f)
  }

  def apply[F](implicit implicitApply: ImplicitApply[F]): ImplicitApply.Aux[F, implicitApply.Out] = implicitApply

  implicit def materialize[F]: ImplicitApply[F] = macro Macros.materialize[F]

  private[feature] final class Macros(val c: whitebox.Context) {
    import c.universe._

    def materialize[F: WeakTypeTag]: Tree =
      try {
        val f = weakTypeOf[F]
        val applySymbol = f.member(TermName("apply")).asMethod
        val MethodType(params, localResult) = applySymbol.info
        val output = localResult.asSeenFrom(f, applySymbol.owner)
        val functionName = TermName(c.freshName("f"))

        val implicitlyTrees = params.map { param =>
          q"_root_.scala.Predef.implicitly[${param.info.asSeenFrom(f, applySymbol.owner)}]"
        }
        val result = q"""
        new _root_.com.thoughtworks.feature.ImplicitApply[$f] {
          type Out = $output
          def apply($functionName: $f): Out = {
            $functionName.apply(..$implicitlyTrees)
          }
        }
        """
        result
      } catch {
        case NonFatal(e) =>
          e.printStackTrace()
          throw e
      }

  }
}