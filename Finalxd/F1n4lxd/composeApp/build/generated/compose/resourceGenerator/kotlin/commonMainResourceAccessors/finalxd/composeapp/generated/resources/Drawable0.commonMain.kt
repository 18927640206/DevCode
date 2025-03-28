@file:OptIn(org.jetbrains.compose.resources.InternalResourceApi::class)

package finalxd.composeapp.generated.resources

import kotlin.OptIn
import kotlin.String
import kotlin.collections.MutableMap
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.InternalResourceApi

private object CommonMainDrawable0 {
  public val Efectivo: DrawableResource by 
      lazy { init_Efectivo() }

  public val PayPal: DrawableResource by 
      lazy { init_PayPal() }

  public val Tarjeta: DrawableResource by 
      lazy { init_Tarjeta() }

  public val compose_multiplatform: DrawableResource by 
      lazy { init_compose_multiplatform() }

  public val rb_2151137700: DrawableResource by 
      lazy { init_rb_2151137700() }
}

@InternalResourceApi
internal fun _collectCommonMainDrawable0Resources(map: MutableMap<String, DrawableResource>) {
  map.put("Efectivo", CommonMainDrawable0.Efectivo)
  map.put("PayPal", CommonMainDrawable0.PayPal)
  map.put("Tarjeta", CommonMainDrawable0.Tarjeta)
  map.put("compose_multiplatform", CommonMainDrawable0.compose_multiplatform)
  map.put("rb_2151137700", CommonMainDrawable0.rb_2151137700)
}

internal val Res.drawable.Efectivo: DrawableResource
  get() = CommonMainDrawable0.Efectivo

private fun init_Efectivo(): DrawableResource = org.jetbrains.compose.resources.DrawableResource(
  "drawable:Efectivo",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/finalxd.composeapp.generated.resources/drawable/Efectivo.jpg", -1, -1),
    )
)

internal val Res.drawable.PayPal: DrawableResource
  get() = CommonMainDrawable0.PayPal

private fun init_PayPal(): DrawableResource = org.jetbrains.compose.resources.DrawableResource(
  "drawable:PayPal",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/finalxd.composeapp.generated.resources/drawable/PayPal.jpg", -1, -1),
    )
)

internal val Res.drawable.Tarjeta: DrawableResource
  get() = CommonMainDrawable0.Tarjeta

private fun init_Tarjeta(): DrawableResource = org.jetbrains.compose.resources.DrawableResource(
  "drawable:Tarjeta",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/finalxd.composeapp.generated.resources/drawable/Tarjeta.jpg", -1, -1),
    )
)

internal val Res.drawable.compose_multiplatform: DrawableResource
  get() = CommonMainDrawable0.compose_multiplatform

private fun init_compose_multiplatform(): DrawableResource =
    org.jetbrains.compose.resources.DrawableResource(
  "drawable:compose_multiplatform",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/finalxd.composeapp.generated.resources/drawable/compose-multiplatform.xml", -1, -1),
    )
)

internal val Res.drawable.rb_2151137700: DrawableResource
  get() = CommonMainDrawable0.rb_2151137700

private fun init_rb_2151137700(): DrawableResource =
    org.jetbrains.compose.resources.DrawableResource(
  "drawable:rb_2151137700",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/finalxd.composeapp.generated.resources/drawable/rb_2151137700.png", -1, -1),
    )
)
