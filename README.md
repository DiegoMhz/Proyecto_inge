Aquí tienes el contenido completo del **`README.md`** optimizado con sintaxis limpia de Markdown, jerarquía visual clara mediante íconos y formato listo para copiar y pegar directamente en tu archivo:

```markdown
# 🔐 Sistema de Autenticación en Java

> Proyecto académico desarrollado en **Java 17 (OpenJDK)** y **Maven**, implementando una interfaz gráfica interactiva con **Swing** y pruebas unitarias con **JUnit 5** bajo la arquitectura **MVC (Modelo - Vista)**.

---

## 🛠️ Tecnologías y Herramientas

| Componente | Tecnología |
| :--- | :--- |
| **Lenguaje** | OpenJDK v17 |
| **Interfaz Gráfica** | Java Swing |
| **Pruebas Unitarias** | JUnit 5 |
| **Gestor de Proyecto** | Apache Maven |
| **Control de Versiones** | Git & GitHub |
| **Editor de Código** | Visual Studio Code (con extensión *Git Graph*) |

---

## 📂 Estructura del Proyecto

```text
mi-proyecto/
├── .gitignore
├── README.md
├── pom.xml
└── src/
    ├── main/
    │   └── java/
    │       ├── model/
    │       │   ├── Usuario.java
    │       │   └── AutenticacionService.java
    │       ├── view/
    │       │   ├── LoginFrame.java
    │       │   └── RegistroFrame.java
    │       └── Main.java
    └── test/
        └── java/
            └── model/
                └── AutenticacionServiceTest.java

```

---

## 📌 Guía de Carpetas y Componentes

### 1️⃣ Archivos de Configuración (Raíz)

* **`pom.xml`**: Define la versión de Java (17), la codificación en UTF-8 y las dependencias del proyecto (JUnit 5).
* **`.gitignore`**: Evita la inclusión en el repositorio de archivos compilados (`target/`), configuraciones locales del editor (`.vscode/`) y temporales del SO.
* **`README.md`**: Documentación principal del sistema, guía de carpetas y distribución de trabajo.

### 2️⃣ Código Fuente (`src/main/java/`)

* 📦 **`model/` (Backend — Lógica de Negocio y Datos)**
* `Usuario.java`: Entidad de dominio que representa la información de un usuario (atributos y acceso encapsulado).
* `AutenticacionService.java`: Servicio encargado del procesamiento del registro, inicio de sesión y validaciones de reglas de negocio.


* 📦 **`view/` (Frontend — Interfaz Gráfica)**
* `LoginFrame.java`: Formulario e interfaz gráfica de inicio de sesión con Swing.
* `RegistroFrame.java`: Formulario e interfaz gráfica para la creación de nuevas cuentas de usuario.


* 🚀 **`Main.java`**
* Punto de entrada ejecutable de la aplicación. Inicializa el servicio de autenticación y lanza la interfaz visual en el hilo de eventos de Swing (*Event Dispatch Thread*).



### 3️⃣ Pruebas Unitarias (`src/test/java/`)

* 🧪 **`model/`**
* `AutenticacionServiceTest.java`: Conjunto de pruebas automatizadas con **JUnit 5** para verificar la precisión de la lógica (registro exitoso, usuarios duplicados y credenciales válidas/inválidas).



---

## 👥 Reparto de Responsabilidades

| Integrante | Rol | Módulos Asignados |
| --- | --- | --- |
| **Estudiante 1** | Backend & Pruebas Unitarias | Paquete `model/` (`Usuario.java`, `AutenticacionService.java`) y test automatizados en `AutenticacionServiceTest.java`. |
| **Estudiante 2** | Frontend (Vistas Swing) | Paquete `view/` (`LoginFrame.java`, `RegistroFrame.java`). |
| **Estudiante 3** | Integración & Administración de Git | `Main.java`, configuración de `pom.xml`, archivos raíz (`.gitignore`, `README.md`) y flujo de ramas en GitHub. |

---

## ⚡ Comandos de Ejecución

Abre tu consola o la terminal de VS Code en la raíz del proyecto y utiliza los siguientes comandos de Maven:

1. **Compilar el código fuente:**
```bash
mvn compile

```


2. **Ejecutar la suite de pruebas unitarias:**
```bash
mvn test

```


3. **Lanzar la aplicación gráfica:**
```bash
mvn exec:java -Dexec.mainClass="Main"

