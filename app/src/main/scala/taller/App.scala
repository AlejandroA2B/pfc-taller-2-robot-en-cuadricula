package taller

object App {
  def main(args: Array[String]): Unit = {
    val r = new RobotCuadricula()
    println(r.ejecutar(r.inicial, Repetir(4, Secuencia(List(Avanzar(1), Girar(Derecha))))))
    println(r.ejecutar(Estado(0, 0, Sur), Avanzar(2)))
    println(r.profundidad(Repetir(3, Avanzar(1))))
    println(r.ejecutar(r.inicial, r.rutina(3)(Izquierda)(2)))
  }
}
