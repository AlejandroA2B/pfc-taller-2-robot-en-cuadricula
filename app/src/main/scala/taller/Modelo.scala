package taller

/** Modelo de datos del taller 2, tal como lo entrega el material. */
sealed trait Direccion
case object Norte extends Direccion
case object Este extends Direccion
case object Sur extends Direccion
case object Oeste extends Direccion

sealed trait Sentido
case object Izquierda extends Sentido
case object Derecha extends Sentido

sealed trait Orden
case class Avanzar(n: Int) extends Orden
case class Girar(sentido: Sentido) extends Orden
case class Repetir(veces: Int, orden: Orden) extends Orden
case class Secuencia(ordenes: List[Orden]) extends Orden

case class Estado(x: Int, y: Int, mira: Direccion)
