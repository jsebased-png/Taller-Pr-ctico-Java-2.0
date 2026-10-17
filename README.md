# Taller Práctico Java 2.0

Repositorio con dos proyectos de Programación Orientada a Objetos en **Java 21**. Son independientes entre sí: cada uno vive en su propia carpeta, se compila por separado y trabaja un tema distinto.

| Proyecto | Carpeta | Tema | Clase(s) con `main` |
|---|---|---|---|
| Taller POO | [`TallerPOO_JuanDominguez/`](./TallerPOO_JuanDominguez) | Clases, constructores sobrecargados, wrappers vs. primitivos, métodos estáticos | `Principal`, `SistemaGestionCuentas` |
| Simulador de Batalla RPG | [`SimuladorDeBatalla_RPG/`](./SimuladorDeBatalla_RPG) | Encapsulamiento, validaciones y combate por turnos en consola | `Main` |

## Requisitos

- JDK 21
- Una terminal (los comandos de abajo son de PowerShell y funcionan igual en Windows, macOS y Linux con pequeños cambios de sintaxis)
- Opcional: IntelliJ IDEA o VS Code con el *Extension Pack for Java*

Para comprobar la instalación:

```powershell
java -version
javac -version
```

---

## 1. Taller POO

**Paquete:** `com.taller.poo`

Cinco clases de dominio y dos ejecutables que muestran cómo se usan.

### Clases

| Clase | Descripción |
|---|---|
| `Vehiculo` | Marca, modelo, kilometraje, velocidad máxima y estado del motor. Usa tipos **primitivos** y documenta por qué en un comentario de diseño. |
| `Estudiante` | Identificación, nombre, promedio y matrícula. `obtenerReporteAcademicos()` construye el reporte con `StringBuilder` y lo devuelve (no imprime). |
| `CuentaBancaria` | Número, titular, saldo (`double`) y límite de crédito (`Double`). Tiene tres constructores sobrecargados, `depositar` y `retirar`. Incluye las preguntas de análisis sobre memoria y `NullPointerException` con sus respuestas. |
| `ConversorMoneda` | Clase utilitaria con métodos estáticos y constructor privado: `convertirDolaresAEuros` y `formatearMoneda`. |
| `Principal` | Demostración sin interacción: crea objetos con cada constructor, opera con las cuentas e imprime reportes. |
| `SistemaGestionCuentas` | Menú interactivo para depositar, retirar y ver un reporte consolidado de 3 cuentas precargadas. |

### Reglas de negocio de `CuentaBancaria`

- Un depósito solo se acepta si el monto es mayor a 0.
- Un retiro requiere monto positivo y que `saldo + límite de crédito` alcance. Si el límite es `null`, solo cuenta el saldo.
- Ambas operaciones devuelven `true` o `false` según se hayan realizado.

### Cuentas de prueba (`SistemaGestionCuentas`)

| Número | Titular | Saldo | Crédito |
|---|---|---|---|
| 1001 | Carlos Pérez | 1000.00 | 500.00 |
| 1002 | Ana Gómez | 0.00 | 100.00 |
| 1003 | Luis Ramírez | 250.50 | 200.00 |

### Cómo ejecutarlo

Desde la raíz del repositorio:

```powershell
cd TallerPOO_JuanDominguez
mkdir out -Force
javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName

# Demostración automática
java -cp out com.taller.poo.Principal

# Menú interactivo de cuentas
java -cp out com.taller.poo.SistemaGestionCuentas
```

---

## 2. Simulador de Batalla RPG ("Arena de Héroes")

**Paquetes:** `co.edu.puj.rpg.principal`, `co.edu.puj.rpg.modelo`, `co.edu.puj.rpg.modelo.enemigo`

Juego de combate por turnos en consola entre un héroe y un enemigo. El foco del taller es el **encapsulamiento**: todos los atributos son privados, los cambios pasan por métodos que validan, y los valores inválidos se rechazan o se corrigen en vez de romper el estado del objeto.

### Estructura

```
SimuladorDeBatalla_RPG/
├── pom.xml
└── src/main/java/co/edu/puj/rpg/
    ├── principal/Main.java        # bucle del juego, menú, tienda e historial
    └── modelo/
        ├── Personaje.java         # héroe del jugador
        └── enemigo/Enemigo.java   # rival controlado por el juego
```

### Cómo se juega

Al iniciar, escribe el nombre de tu héroe (si lo dejas vacío, se llama *Aragorn*). Empiezas con 100 PV, 15 de ataque, 50 de oro y 2 Pociones Grandes, y te enfrentas a un **Orco Guerrero** (80 PV, 12 de ataque, nivel 2).

| Opción | Acción | ¿Consume turno? |
|---|---|---|
| 1 | Atacar al enemigo | Sí |
| 2 | Usar una Poción Grande (+50 PV) | Sí, si se pudo usar |
| 3 | Ver el estado de ambos combatientes | No |
| 4 | Huir del combate | No (termina la partida) |
| 5 | Tienda: comprar Poción Grande por 30 de oro | No |

Cuando consumes turno, el enemigo contraataca si sigue vivo y se imprime el historial de batalla.

### Reglas del juego

- **Golpe crítico:** 15 % de probabilidad de infligir el doble de daño.
- **Victoria:** recibes el oro del enemigo y experiencia (75 × nivel del enemigo).
- **Subida de nivel:** necesitas `100 × nivel` de experiencia. Cada nivel da +20 de vida máxima y +3 de ataque, y restaura la vida por completo.
- **No puedes** usar una poción sin tener ninguna, ni con la vida al máximo, ni comprar sin oro suficiente.

### Decisiones de encapsulamiento

- Constantes de balance (`PROBABILIDAD_CRITICO`, `BONO_VIDA_POR_NIVEL`, etc.) como `private static final`.
- `setPuntosVida` mantiene la vida entre 0 y la vida máxima y actualiza `estaVivo` automáticamente.
- `setOro` devuelve `boolean`: rechaza valores negativos, `NaN` o infinitos, y así la tienda sabe si la compra fue válida.
- `setNivel` es `protected` y `subirNivel` es `private`: el nivel solo cambia ganando experiencia.
- Los setters críticos son `final` para que una subclase no pueda saltarse las validaciones.
- Los constructores corrigen valores inválidos (vida o ataque menores a 1, nombre vacío).

### Cómo ejecutarlo

Desde la raíz del repositorio:

```powershell
cd SimuladorDeBatalla_RPG
mkdir out -Force
javac -d out (Get-ChildItem -Recurse src/main/java -Filter *.java).FullName
java -cp out co.edu.puj.rpg.principal.Main
```

También puedes abrir la carpeta en IntelliJ y ejecutar `Main` con el triángulo verde junto a `public static void main`.

---

## Organización del repositorio

```
Taller-Pr-ctico-Java-2.0/
├── README.md
├── TallerPOO_JuanDominguez/      # Proyecto 1
└── SimuladorDeBatalla_RPG/       # Proyecto 2
```

Cada proyecto tiene su propio `.gitignore` que excluye `.idea/`, `out/`, `target/` y `*.iml`, así que los archivos del IDE y los `.class` compilados no se suben.

### Convención de commits

El historial sigue [Conventional Commits](https://www.conventionalcommits.org/es/):

```
tipo(alcance): descripción en minúscula y sin punto final
```

| Tipo | Uso |
|---|---|
| `feat` | Funcionalidad o clase nueva |
| `fix` | Corrección de un error |
| `chore` | Configuración (Maven, `.gitignore`) |
| `docs` | Documentación |
| `test` | Pruebas |
| `refactor` | Cambio interno sin alterar el comportamiento |

Ejemplo: `feat(personaje): agregar clase Personaje con vida, ataque, nivel y experiencia`

## Autor

**Juan Sebastián Domínguez**: [@jsebased-png](https://github.com/jsebased-png)
