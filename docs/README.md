# Informes

Aquí van los dos informes que pide el enunciado, en Markdown:

- `proceso.md` — el informe de proceso: cómo se ejecuta cada función paso a
  paso. Para este taller, la traza de `ejecutar` sobre
  `Repetir(4, Secuencia(List(Avanzar(1), Girar(Derecha))))` desde el estado
  inicial, mostrando en cada paso el estado y la orden que queda por ejecutar,
  y el recorrido de `simplificar` sobre una orden que necesite más de una
  pasada.
- `correccion.md` — el informe de corrección: la argumentación de que cada
  función hace lo que el enunciado pide. El punto 5 pide una en concreto: que
  `simplificar` no cambia el resultado, es decir que para toda orden `o` y todo
  estado `e`, `ejecutar(e, o)` y `ejecutar(e, simplificar(o))` dejan el mismo
  estado. Se muestra regla por regla.

Los dos archivos ya existen y traen un ejemplo de lo que se espera:
`proceso.md` sigue la traza de `factorial(5)` con recursión de cola y dibuja la
pila con `mermaid`; `correccion.md` argumenta por inducción estructural la
corrección del factorial y del máximo de una lista, y con estado, invariante y
transformación la de sus versiones iterativas. El informe del taller reemplaza
ese contenido y sigue la misma forma sobre las funciones del enunciado.

El tipo `Orden` está definido por sus cuatro alternativas, así que la
inducción del ejemplo se traslada directamente. Hay un caso base por cada
orden básica, `Avanzar` y `Girar`, y un caso de inducción por cada compuesta,
`Repetir` y `Secuencia`; en estos dos últimos se puede suponer la propiedad
de las órdenes que contienen.

La notación matemática se escribe en LaTeX dentro del Markdown y los
diagramas se generan con `mermaid`. No se aceptan imágenes insertadas.
