package taller

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class RobotCuadriculaTest extends AnyFunSuite {

  val r = new RobotCuadricula()
  import r._

  val cuadrado = Repetir(4, Secuencia(List(Avanzar(1), Girar(Derecha))))

  // Punto 1 -------------------------------------------------------------------

  test("girar: Norte a la derecha da Este") { assert(girar(Norte, Derecha) == Este) }
  test("girar: Norte a la izquierda da Oeste") { assert(girar(Norte, Izquierda) == Oeste) }
  test("girar: Oeste a la derecha da Norte") { assert(girar(Oeste, Derecha) == Norte) }
  test("girar: Sur a la izquierda da Este") { assert(girar(Sur, Izquierda) == Este) }

  test("girar: cuatro giros al mismo lado vuelven al principio") {
    val cuatro = List(Derecha, Derecha, Derecha, Derecha)
      .foldLeft(Norte: Direccion)((d, s) => girar(d, s))
    assert(cuatro == Norte)
  }

  test("girar: un giro y su contrario se cancelan") {
    assert(girar(girar(Este, Derecha), Izquierda) == Este)
  }
  //Casos de Prueba
  test("girar: Este a la derecha da Sur") {
    assert(girar(Este, Derecha) == Sur)
  }

  test("girar: Este a la izquierda da Norte") {
    assert(girar(Este, Izquierda) == Norte)
  }

  test("girar: Sur a la derecha da Oeste") {
    assert(girar(Sur, Derecha) == Oeste)
  }

  test("girar: Oeste a la izquierda da Sur") {
    assert(girar(Oeste, Izquierda) == Sur)
  }

  test("girar: izquierda y derecha desde el mismo estado dan resultados opuestos") {
    assert(girar(girar(Norte, Derecha), Izquierda) == Norte)
  }

  test("girar: cuatro giros a la izquierda vuelven al punto de partida") {
    val cuatro = List(Izquierda, Izquierda, Izquierda, Izquierda)
      .foldLeft(Norte: Direccion)((d, s) => girar(d, s))
    assert(cuatro == Norte)
  }


  // Punto 2 -------------------------------------------------------------------

  test("ejecutar: avanzar tres al norte") {
    assert(ejecutar(inicial, Avanzar(3)) == Estado(0, 3, Norte))
  }

  test("ejecutar: una secuencia de avance, giro y avance") {
    assert(ejecutar(inicial, Secuencia(List(Avanzar(2), Girar(Derecha), Avanzar(4)))) ==
      Estado(4, 2, Este))
  }

  test("ejecutar: el cuadrado vuelve al punto de partida") {
    assert(ejecutar(inicial, cuadrado) == Estado(0, 0, Norte))
  }

  test("ejecutar: avanzar con paso negativo retrocede") {
    assert(ejecutar(inicial, Avanzar(-2)) == Estado(0, -2, Norte))
  }

  test("ejecutar: repetir cero veces no hace nada") {
    assert(ejecutar(inicial, Repetir(0, Avanzar(5))) == inicial)
  }

  test("ejecutar: la secuencia vacía no hace nada") {
    assert(ejecutar(inicial, Secuencia(Nil)) == inicial)
  }

  test("ejecutar: avanzar al sur disminuye y") {
    assert(ejecutar(Estado(5, 5, Sur), Avanzar(2)) == Estado(5, 3, Sur))
  }

  test("ejecutar: avanzar al oeste disminuye x") {
    assert(ejecutar(Estado(5, 5, Oeste), Avanzar(2)) == Estado(3, 5, Oeste))
  }

  //Casos de prueba
  test("ejecutar: avanzar al este aumenta x") {
    assert(ejecutar(Estado(0, 0, Este), Avanzar(3)) == Estado(3, 0, Este))
  }

  test("ejecutar: avanzar con cero no mueve") {
    assert(ejecutar(Estado(5, 5, Norte), Avanzar(0)) == Estado(5, 5, Norte))
  }

  test("ejecutar: repetir negativo no hace nada") {
    assert(ejecutar(Estado(0, 0, Norte), Repetir(-2, Avanzar(5))) == Estado(0, 0, Norte))
  }

  test("ejecutar: repetir una vez equivale a la orden sola") {
    assert(ejecutar(Estado(0, 0, Norte), Repetir(1, Avanzar(4))) ==
      ejecutar(Estado(0, 0, Norte), Avanzar(4)))
  }

  test("ejecutar: secuencia de solo giros no mueve") {
    assert(ejecutar(Estado(0, 0, Norte), Secuencia(List(Girar(Derecha), Girar(Derecha)))) ==
      Estado(0, 0, Sur))
  }

  test("ejecutar: repetir anidado multiplica los avances") {
    assert(ejecutar(Estado(0, 0, Norte), Repetir(2, Repetir(3, Avanzar(1)))) ==
      Estado(0, 6, Norte))
  }

  // Punto 3 -------------------------------------------------------------------

  test("cuantasBasicas: una orden básica cuenta uno") {
    assert(cuantasBasicas(Avanzar(3)) == 1)
  }

  test("cuantasBasicas: el cuadrado ejecuta ocho básicas") {
    assert(cuantasBasicas(cuadrado) == 8)
  }

  test("cuantasBasicas: repetir cero veces no ejecuta nada") {
    assert(cuantasBasicas(Repetir(0, cuadrado)) == 0)
  }

  test("cuantasBasicas: la secuencia vacía no ejecuta nada") {
    assert(cuantasBasicas(Secuencia(Nil)) == 0)
  }

  test("profundidad: una orden básica tiene profundidad 1") {
    assert(profundidad(Avanzar(3)) == 1)
  }

  test("profundidad: una secuencia de básicas tiene profundidad 2") {
    assert(profundidad(Secuencia(List(Avanzar(1), Girar(Derecha)))) == 2)
  }

  test("profundidad: el cuadrado tiene profundidad 3") {
    assert(profundidad(cuadrado) == 3)
  }

  test("profundidad: la secuencia vacía tiene profundidad 1") {
    assert(profundidad(Secuencia(Nil)) == 1)
  }

  test("profundidad: manda la rama más honda") {
    assert(profundidad(Secuencia(List(Avanzar(1), cuadrado))) == 4)
  }

  //Casos de prueba
  //Cuantas Basicas
  test("cuantasBasicas: Girar cuenta como una básica") {
    assert(cuantasBasicas(Girar(Izquierda)) == 1)
  }

  test("cuantasBasicas: Repetir multiplica las básicas de su orden interna") {
    assert(cuantasBasicas(Repetir(3, Avanzar(2))) == 3)
  }

  test("cuantasBasicas: Repetir con veces negativo no cuenta nada") {
    assert(cuantasBasicas(Repetir(-2, Avanzar(5))) == 0)
  }

  test("cuantasBasicas: Secuencia suma las básicas de sus órdenes") {
    assert(cuantasBasicas(Secuencia(List(Avanzar(1), Girar(Derecha), Avanzar(2)))) == 3)
  }

  test("cuantasBasicas: Repetir anidado multiplica los conteos") {
    assert(cuantasBasicas(Repetir(2, Repetir(3, Avanzar(1)))) == 6)
  }

  test("cuantasBasicas: Repetir de una Secuencia multiplica la suma") {
    assert(cuantasBasicas(Repetir(5, Secuencia(List(Avanzar(2), Girar(Derecha))))) == 10)
  }

  //Profundidad
  test("profundidad: Repetir añade un nivel a su orden interna") {
    assert(profundidad(Repetir(3, Avanzar(2))) == 2)
  }

  test("profundidad: Repetir anidado suma niveles") {
    assert(profundidad(Repetir(2, Repetir(3, Avanzar(1)))) == 3)
  }

  test("profundidad: Secuencia toma la rama más honda y suma uno") {
    assert(profundidad(Secuencia(List(Avanzar(1), Repetir(2, Avanzar(1))))) == 3)
  }

  test("profundidad: Secuencia con una sola básica tiene profundidad 2") {
    assert(profundidad(Secuencia(List(Girar(Derecha)))) == 2)
  }

  test("profundidad: Repetir de una Secuencia vacía tiene profundidad 2") {
    assert(profundidad(Repetir(3, Secuencia(Nil))) == 2)
  }

  test("profundidad: Secuencia anidada dentro de Secuencia suma niveles") {
    assert(profundidad(Secuencia(List(Secuencia(List(Avanzar(1)))))) == 3)
  }

  // Punto 4 -------------------------------------------------------------------

  test("componer: deja las dos órdenes en secuencia") {
    assert(componer(Avanzar(1), Girar(Derecha)) ==
      Secuencia(List(Avanzar(1), Girar(Derecha))))
  }

  test("componer: ejecutar la compuesta es ejecutar una y después la otra") {
    assert(ejecutar(inicial, componer(Avanzar(2), Girar(Derecha))) ==
      ejecutar(ejecutar(inicial, Avanzar(2)), Girar(Derecha)))
  }

  test("rutina: currificada, el cuadrado de cuatro vuelve al origen") {
    val cuadradoDe = rutina(1)(Derecha) _
    assert(ejecutar(inicial, cuadradoDe(4)) == Estado(0, 0, Norte))
  }

  test("rutina: con los mismos argumentos construye el cuadrado del enunciado") {
    assert(rutina(1)(Derecha)(4) == cuadrado)
  }

  test("aplicarA: ejecuta la misma orden sobre varios estados") {
    assert(aplicarA(List(inicial, Estado(5, 5, Sur)), Avanzar(2)) ==
      List(Estado(0, 2, Norte), Estado(5, 3, Sur)))
  }

  test("aplicarA: sobre la lista vacía no hay nada que hacer") {
    assert(aplicarA(Nil, Avanzar(2)) == Nil)
  }

  test("transformar: duplicar los avances deja los giros como estaban") {
    val doble = transformar(cuadrado, {
      case Avanzar(n) => Avanzar(2 * n)
      case otra => otra
    })
    assert(doble == Repetir(4, Secuencia(List(Avanzar(2), Girar(Derecha)))))
  }

  test("transformar: con la identidad no cambia nada") {
    assert(transformar(cuadrado, (o: Orden) => o) == cuadrado)
  }

  //Casos de Prueba
  //Componer
  test("componer: dos avances se ejecutan en orden") {
    assert(ejecutar(inicial, componer(Avanzar(2), Avanzar(3))) == Estado(0, 5, Norte))
  }

  test("componer: giro y avance mueven en la nueva dirección") {
    assert(ejecutar(inicial, componer(Girar(Derecha), Avanzar(3))) == Estado(3, 0, Este))
  }

  test("componer: dos giros a la derecha equivalen a girar 180 grados") {
    assert(ejecutar(inicial, componer(Girar(Derecha), Girar(Derecha))) == Estado(0, 0, Sur))
  }

  test("componer: componer con secuencia vacía no altera") {
    assert(ejecutar(inicial, componer(Secuencia(Nil), Avanzar(2))) == Estado(0, 2, Norte))
  }

  test("componer: componer dos secuencias las concatena como una sola") {
    val o = componer(Secuencia(List(Avanzar(1))), Secuencia(List(Avanzar(2))))
    assert(ejecutar(inicial, o) == Estado(0, 3, Norte))
  }

  test("componer: el orden importa (avanzar y girar no es girar y avanzar)") {
    val o1 = componer(Avanzar(2), Girar(Derecha))
    val o2 = componer(Girar(Derecha), Avanzar(2))
    assert(ejecutar(inicial, o1) != ejecutar(inicial, o2))
  }
  //
  //Rutina
  test("rutina: cero repeticiones no hace nada") {
    assert(ejecutar(inicial, rutina(1)(Derecha)(0)) == inicial)
  }

  test("rutina: una repetición avanza y gira una vez") {
    assert(ejecutar(inicial, rutina(2)(Izquierda)(1)) == Estado(0, 2, Oeste))
  }

  test("rutina: pasos negativos retroceden") {
    assert(ejecutar(inicial, rutina(-2)(Derecha)(1)) == Estado(0, -2, Este))
  }

  test("rutina: cuatro repeticiones con giro a la izquierda forman un cuadrado") {
    assert(ejecutar(inicial, rutina(1)(Izquierda)(4)) == Estado(0, 0, Norte))
  }

  test("rutina: fijar pasos y giro devuelve una función que espera veces") {
    val f: Int => Orden = rutina(2)(Derecha) _
    assert(f(3) == Repetir(3, Secuencia(List(Avanzar(2), Girar(Derecha)))))
  }

  //AplicarA
  test("aplicarA: lista vacía devuelve lista vacía") {
    assert(aplicarA(Nil, Avanzar(2)) == Nil)
  }

  test("aplicarA: un solo estado") {
    assert(aplicarA(List(inicial), Girar(Derecha)) == List(Estado(0, 0, Este)))
  }

  test("aplicarA: no modifica la lista original") {
    val estados = List(Estado(0, 0, Norte))
    val resultado = aplicarA(estados, Avanzar(5))
    assert(estados == List(Estado(0, 0, Norte)))
    assert(resultado == List(Estado(0, 5, Norte)))
  }

  test("aplicarA: aplica una secuencia compuesta a varios estados") {
    val o = Secuencia(List(Avanzar(1), Girar(Derecha), Avanzar(1)))
    val estados = List(Estado(0, 0, Norte), Estado(0, 0, Sur))
    assert(aplicarA(estados, o) == List(Estado(1, 1, Este), Estado(-1, -1, Oeste)))
  }

  test("aplicarA: aplica Repetir a varios estados") {
    val o = Repetir(2, Avanzar(1))
    val estados = List(Estado(0, 0, Norte), Estado(0, 0, Este))
    assert(aplicarA(estados, o) == List(Estado(0, 2, Norte), Estado(2, 0, Este)))
  }

  test("aplicarA: la longitud del resultado es la misma que la de entrada") {
    val estados = List(inicial, Estado(1, 1, Sur), Estado(-2, 3, Oeste))
    assert(aplicarA(estados, Avanzar(1)).length == estados.length)
  }

  //Transformar
  test("transformar: invierte el sentido de los giros") {
    val f: Orden => Orden = {
      case Girar(Derecha)   => Girar(Izquierda)
      case Girar(Izquierda) => Girar(Derecha)
      case otra             => otra
    }
    assert(transformar(Girar(Derecha), f) == Girar(Izquierda))
  }

  test("transformar: aplica f a Avanzar dentro de Secuencia") {
    val f: Orden => Orden = { case Avanzar(n) => Avanzar(n + 1); case otra => otra }
    assert(transformar(Secuencia(List(Avanzar(1), Avanzar(2))), f) ==
      Secuencia(List(Avanzar(2), Avanzar(3))))
  }

  test("transformar: no aplica f a compuestas") {
    val f: Orden => Orden = { case Avanzar(n) => Avanzar(n * 10); case otra => otra }
    assert(transformar(Repetir(2, Avanzar(1)), f) == Repetir(2, Avanzar(10)))
  }

  test("transformar: respeta estructura de Secuencia anidada") {
    val f: Orden => Orden = { case Avanzar(n) => Avanzar(n * 2); case otra => otra }
    val o = Secuencia(List(Secuencia(List(Avanzar(1))), Avanzar(2)))
    assert(transformar(o, f) == Secuencia(List(Secuencia(List(Avanzar(2))), Avanzar(4))))
  }

  test("transformar: convertir todos los Avanzar en Girar") {
    val f: Orden => Orden = { case Avanzar(_) => Girar(Derecha); case otra => otra }
    assert(transformar(Avanzar(5), f) == Girar(Derecha))
  }

  // Punto 5 -------------------------------------------------------------------

  test("simplificar: dos avances seguidos se juntan") {
    assert(simplificar(Secuencia(List(Avanzar(2), Avanzar(3)))) == Avanzar(5))
  }

  test("simplificar: repetir una vez es la orden misma") {
    assert(simplificar(Repetir(1, Avanzar(4))) == Avanzar(4))
  }

  test("simplificar: avanzar cero desaparece de la secuencia") {
    assert(simplificar(Secuencia(List(Avanzar(0), Girar(Derecha)))) == Girar(Derecha))
  }

  test("simplificar: junta solo los avances contiguos") {
    assert(simplificar(Secuencia(List(Avanzar(1), Avanzar(2), Girar(Derecha), Avanzar(3)))) ==
      Secuencia(List(Avanzar(3), Girar(Derecha), Avanzar(3))))
  }

  test("simplificar: repetir cero veces no hace nada") {
    assert(simplificar(Repetir(0, Avanzar(9))) == Secuencia(Nil))
  }

  test("simplificar: dos avances que se cancelan desaparecen") {
    assert(simplificar(Secuencia(List(Avanzar(2), Avanzar(-2)))) == Secuencia(Nil))
  }

  test("simplificar: es idempotente") {
    val ordenes = List(cuadrado,
      Secuencia(List(Avanzar(1), Avanzar(2), Girar(Derecha), Avanzar(3))),
      Repetir(1, Secuencia(List(Avanzar(0), Avanzar(4)))),
      Secuencia(List(Secuencia(Nil), Repetir(0, Avanzar(3)))))
    assert(ordenes.forall(o => simplificar(simplificar(o)) == simplificar(o)))
  }

  test("simplificar: no cambia el resultado de ejecutar") {
    val estados = List(inicial, Estado(3, -2, Este), Estado(-1, 4, Sur))
    val ordenes = List(cuadrado,
      Secuencia(List(Avanzar(2), Avanzar(3))),
      Secuencia(List(Avanzar(1), Avanzar(2), Girar(Derecha), Avanzar(3))),
      Repetir(1, Avanzar(4)),
      Repetir(0, Avanzar(9)),
      Secuencia(List(Avanzar(0), Girar(Derecha))),
      Secuencia(List(Avanzar(2), Avanzar(-2))),
      Repetir(3, Secuencia(List(Repetir(1, Avanzar(2)), Girar(Izquierda)))))
    assert(ordenes.forall(o =>
      estados.forall(e => ejecutar(e, o) == ejecutar(e, simplificar(o)))))
  }

  test("simplificar: el anidamiento interior también se reduce") {
    assert(simplificar(Repetir(3, Repetir(1, Avanzar(2)))) == Repetir(3, Avanzar(2)))
  }
  //Casos de prueba
  //Simplificar
  test("simplificar: fusiona tres avances consecutivos") {
    assert(simplificar(Secuencia(List(Avanzar(1), Avanzar(2), Avanzar(3)))) == Avanzar(6))
  }

  test("simplificar: no fusiona avances separados por un giro") {
    assert(simplificar(Secuencia(List(Avanzar(1), Girar(Derecha), Avanzar(2)))) ==
      Secuencia(List(Avanzar(1), Girar(Derecha), Avanzar(2))))
  }

  test("simplificar: quita varias órdenes vacías de una secuencia") {
    assert(simplificar(Secuencia(List(Avanzar(0), Girar(Derecha), Repetir(0, Avanzar(9))))) ==
      Girar(Derecha))
  }

  test("simplificar: simplifica dentro de un Repetir") {
    assert(simplificar(Repetir(3, Secuencia(List(Avanzar(1), Avanzar(2))))) ==
      Repetir(3, Avanzar(3)))
  }

  test("simplificar: Repetir una vez desenvuelve aunque sea compuesta") {
    assert(simplificar(Repetir(1, Secuencia(List(Avanzar(1), Avanzar(2))))) == Avanzar(3))
  }

  test("simplificar: una secuencia que queda con un solo elemento se desenvuelve") {
    assert(simplificar(Secuencia(List(Avanzar(0), Avanzar(4)))) == Avanzar(4))
  }

}
