package sri.sangria.mobile.styles

import sri.universal.styles.UniversalStyleSheet


object Theme extends UniversalStyleSheet {

  val flexOneAndCenter = style(flex := 1,
    alignItems.center,
    justifyContent.center)

  val flexOneAndDirectionRow = style(flex := 1, flexDirection.row)

  val bigText = style(fontWeight := "500",color := "white",
    fontSize := 20)

  val whiteText500 = style(color := "white",fontWeight._500)

}
