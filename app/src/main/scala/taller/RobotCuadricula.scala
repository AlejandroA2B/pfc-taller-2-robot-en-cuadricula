package taller

/**
 * Taller 2 — órdenes para un robot en una cuadrícula infinita.
 *
 * El tipo `Orden` es recursivo: `Repetir` y `Secuencia` contienen otras
 * órdenes. Toda función que lo recorra lo es también, con un caso por cada
 * alternativa del tipo.
 *
 * Tal como está, el proyecto compila y las pruebas quedan en rojo.
 */
class RobotCuadricula {

  /** El robot empieza en el origen mirando al norte. */
  val inicial = Estado(0, 0, Norte)

  // Punto 1 -------------------------------------------------------------------

  /** La dirección que queda tras girar noventa grados hacia el sentido dado. */
  def girar(d: Direccion, s: Sentido): Direccion = {

    (d,s) match {
        //Se manejaron las 8 combinaciones posibles es decir se manejaron todos los casos
      case (Norte,Derecha)=> Este
      case (Norte,Izquierda)=> Oeste
      case (Oeste,Derecha)=> Norte
      case (Oeste,Izquierda)=> Sur
      case (Sur,Izquierda)=> Este
      case (Sur,Derecha)=> Oeste
      case (Este,Derecha)=> Sur
      case (Este,Izquierda)=> Norte

    }

  }

  // Punto 2 -------------------------------------------------------------------

  /** El estado que resulta de aplicarle la orden al estado dado. */
  def ejecutar(e: Estado, o: Orden): Estado = {

    o match {
      //programacion orientacion
    case Avanzar(n: Int)=>
    e.mira match { //El robot avanza o retrocede en 2 dimensiones
      case Norte => e.copy(y = e.y + n)  //eje X y Y
      case Sur   => e.copy(y = e.y - n)
      case Este  => e.copy(x = e.x + n)
      case Oeste => e.copy(x = e.x - n)
    }
      //se implementa el sentido del robot sabe a donde gira
    case Girar(sentido) =>
      e.copy(mira = girar(e.mira, sentido))

    case Repetir(veces, orden) =>
      if (veces <= 0) {
        e
      } else {  //Parte recursiva
        ejecutar(ejecutar(e, orden), Repetir(veces - 1, orden))
      }

    case Secuencia(ordenes) =>
      ordenes.foldLeft(e) {
        case (estado, orden) =>
          ejecutar(estado, orden)
      }

     }
  }

  // Punto 3 -------------------------------------------------------------------

  /** Cuántos Avanzar y Girar llega a ejecutar la orden, contando repeticiones. */
  def cuantasBasicas(o: Orden): Int ={

    o match {
      case Avanzar(n) => 1

      case Girar(sentido)   => 1

      case Repetir(veces, orden) =>
        if (veces <= 0) //Si se llega a repetir cero o valores menor devuelve un 0
          0
        else //Recursion de la orden por veces en que se pidio ejecutarse las ordenes
          veces * cuantasBasicas(orden)

      case Secuencia(ordenes) =>
        ordenes.map(cuantasBasicas).sum //Aqui al final se suman los resultados
    }

  }

  /** Niveles de anidamiento: una básica tiene 1, una compuesta uno más que la más honda. */
  def profundidad(o: Orden): Int = {

    o match {
      case Avanzar(n) => 1

      case Girar(sentido)   => 1

      case Repetir(veces, orden) =>
        1 + profundidad(orden)

      case Secuencia(ordenes) =>
        if (ordenes.isEmpty) {//La secuencia que es vacia sigue siendo una orden nivel 1
          1
        } else{
          1 + ordenes.map(profundidad).max //Agarra la maxima honda y se le suma 1
        } //Calcula la profundidad de cada orden interna

    }

  }

  // Punto 4 -------------------------------------------------------------------

  /** La secuencia de las dos órdenes. */
  def componer(o1: Orden, o2: Orden): Orden = {
    Secuencia(List(o1, o2))//Devuelve la secuencia de las dos ordenes a ejecutar
  }

  /** Currificada: fijados los pasos y el giro, queda una función de las veces. */
  def rutina(pasos: Int)(giro: Sentido)(veces: Int): Orden = {

    Repetir(veces, Secuencia //Repetir tiene las veces que se va ejecutar la lista de instrucciones
        (List(Avanzar(pasos),//Secuencia reciba las ordenes en este caso una lista con avanzar los pasos pasados por parametro
      Girar(giro)))    //y girar lo pasado por parametro
    )

  }

  /** La misma orden ejecutada sobre cada uno de los estados. */
  def aplicarA(estados: List[Estado], o: Orden): List[Estado] = {

    estados.map(q => ejecutar(q,o))//Map transforma aplica la funcion ejecutar a cada estado de la lista

  }

  /** Reemplaza cada orden básica por f(orden), respetando la estructura de las compuestas. */
  def transformar(o: Orden, f: Orden => Orden): Orden = {

    o match {
      case Avanzar(n) =>
        f(Avanzar(n)) //Se aplica f ala orden basica de avanzar y girar tantas veces se ha solicitado

      case Girar(sentido) =>

        f(Girar(sentido))
      case Repetir(veces, orden) =>//No se aplica f directamente en repartir
        Repetir(veces, transformar(orden, f))//sino que transforme su orden interna

      case Secuencia(ordenes) => //Transforma cada orden de la lista de forma recuriva
        Secuencia(
          ordenes.map(q => transformar(q, f))
        )
    }

  }

  // Punto 5 -------------------------------------------------------------------

  /** Una orden equivalente más corta, con las reglas del enunciado. */
  def simplificar(o: Orden): Orden = {

    def esVacia(o: Orden): Boolean = o match {
      case Secuencia(Nil)=> true//Secuencia vacia no va hacer nada

      case Avanzar(0)=> true//si se avanza 0 casillas no se hace nada

      case Repetir(veces, orden) if veces <= 0
      => true //Si se repite cero o menos veces no hay ejecucion

      case otro => false //cualquier otra orden donde haya avanze > 0 se ejecuta
    }

    def auxFusionAvanzar(ordenes: List[Orden]): List[Orden] = ordenes match {
      //Si hay dos avanzar seguidos lo que se hace es que se reemplazan por uno el cual contiene la suma y luego se sigue fusionando
      case Avanzar(n1) :: Avanzar(n2) :: resto =>
        auxFusionAvanzar(Avanzar(n1 + n2) :: resto)
      case head :: tail =>//Cualquier otra orden se deja tal cual y se sigue con el resto(tail)
        head :: auxFusionAvanzar(tail)
      case Nil => //No hay nada que fusionar
        Nil
    }

    def auxSimplicarUnaVez(o: Orden): Orden = o match {
      case Avanzar(0) => Secuencia(Nil)
      case Avanzar(n) => Avanzar(n)
      case Girar(sentido)   => Girar(sentido)

      //Aqui se simplifica la orden interna y se aplica la regla 1 y 3
      case Repetir(veces, orden) =>
        val ordenSimpli = auxSimplicarUnaVez(orden)
        if (veces <= 0) {
          Secuencia(Nil) //
        } else if (veces == 1) {
          ordenSimpli
        } else {
          Repetir(veces, ordenSimpli)
        }


      case Secuencia(ordenes) =>//Aqui se aplica regla 5 respecto a quitar las que no hacen nada y se simplifica cada orden interna
        val simplificadas = ordenes.map(auxSimplicarUnaVez).filterNot(esVacia)
        val fusionadas   = auxFusionAvanzar(simplificadas)
        fusionadas match {
          case Nil =>
            Secuencia(Nil)
          case List(unica) =>
            unica //se aplica la regla 4 en la cual una secuncia de un solo elemento se vuelve ese elemento
          case varias =>
            Secuencia(varias)//Mantener secuencia ya que son varios elementos
        }
    }

    //Punto Fijo
    def auxPuntoFijo(actual: Orden): Orden = {
      val next = auxSimplicarUnaVez(actual)

      if (next == actual) {
        actual // ya no hay cambios por lo tanto aqui finaliza
      } else {
        auxPuntoFijo(next)//Se sigue simplificando
      }

    }
    //Este es el punto de entrada donde se simplifica hasta que no haya cambios
    auxPuntoFijo(o)

  }
}
