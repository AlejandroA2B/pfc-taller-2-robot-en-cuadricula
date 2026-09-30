package taller

object App {
  def main(args: Array[String]): Unit = {
    val r = new RobotCuadricula()
    println(r.ejecutar(r.inicial, Repetir(4, Secuencia(List(Avanzar(1), Girar(Derecha))))))
  }
}
