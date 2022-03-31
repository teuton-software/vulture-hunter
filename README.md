# Vulture Hunter ![Vulture](vulture-24px.png) 

[![Maven Central](http://img.shields.io/maven-central/v/io.github.fvarrui/VultureHunter)](https://search.maven.org/artifact/io.github.fvarrui/VultureHunter) [![GPL-3.0](https://img.shields.io/badge/license-GPL--3.0-%0778B9.svg)](https://www.gnu.org/licenses/gpl-3.0.html)

Herramienta de comparación que determina el grado de similitud entre dos proyectos (también conocido como el **Cazador de Buitres**). Está pensado para detectar si los alumnos se han copiado en proyectos de desarrollo de software, comparando el código fuente entregado.

## Usar como una librería con Maven

```xml
<dependency>
	<groupId>io.github.fvarrui</groupId>
	<artifactId>VultureHunter</artifactId>
	<version>{latest.version}</version>
</dependency>
```

## Usar desde la línea de comandos

**Vulture Hunter** también se puede utilizar desde la línea de comandos. [Descarga](https://github.com/fvarrui/VultureHunter/releases) y descomprime el fichero ZIP con la aplicación, y dentro encontrarás el comando `hunt`.

### Consultar la ayuda

Para consultar la ayuda del comando, ejecutamos `hunt` sin parámetros o con las opciones `-h` o `--help`:

```bash
hunt --help
```

El resultado sería algo similar a lo siguiente:

```bash
Comparison tool to determine the degree of similarity between two projects (aka "Vulture Hunter")
usage: hunt
 -a,--all <folder>                 compare all projects in specified folder
 -b,--binary <ext1,ext2,...>       extensions of the included binary files (default value: all files)
 -c,--compare <folder1,folder2>    compare two projects
 -e,--excluded <path1,path2,...>   excluded relative paths (default value: empty)
 -h,--help                         print this message
 -s,--similarity <arg>             degree of similarity between projects (default value: 75.0)
 -t,--text <ext1,ext2,...>         extensions of the included text files (default value: empty)
 -th,--threshold <arg>             degree of similarity between files to be considered equal (default value: 80.0)
```

### Comparar dos proyectos

Para comparar dos proyectos y ver el grado de similitud entre ambos, utilizamos la opción `-c` o `--compare`:

```bash
hunt --compare path/to/project1,path/to/project2
```

### Comparar múltiples proyectos

Es posible comparar entre sí todos los proyectos almacenados en una carpeta con la opción `-a` o `--all`:

```bash
hunt --all path/to/parent/folder
```

> Esta opción está pensada para comparar proyectos entregados a través de un plataforma e-Learning como Moodle, donde es posible descargar un ZIP con todos las entregas. Una vez descomprimimos el ZIP con las entregas, podemos lanzar `hunt` con la opción `--all`.

### Opciones

Al comparar proyectos, tanto con `--compare` como con `--all`, disponemos de las siguientes opciones:

| Opción               | Descripción                                                  |
| -------------------- | ------------------------------------------------------------ |
| `-s`, `--similarity` | Porcentaje de similitud entre dos proyectos para ser considerados sospechosos. Por defecto es 75%. |
| `-th`, `--threshold` | Porcentaje de similitud entre dos ficheros para ser considerados iguales. Por defecto es 80%. |

### Ejemplo

El siguiente comando compara los proyectos "alumno1" y "alumno2", ambos de tipo Maven:

```bash
hunt --compare alumno1,alumno2 --threshold 75 --text java,xml,fxml --excluded "target/.*","\..*"
```

>   En el ejemplo anterior, se establece el umbral de similitud de ficheros en un 75%,  se comparan sólo los ficheros de texto con extensión `java`, `xml` o `fxml`, y se excluyen todas las rutas que comiencen por `target/` y las que comiencen por un `. ` (punto).

Con el siguiente resultado:

```bash
+-------------------------------+
| alumno1 compared with alumno2 |
+-------------------------------+
Compared files threshold: 75.0%
Estimated similarity between projects: 100,00%
Matches:
* [alumno1:pom.xml] compared with [alumno2:pom.xml]: files match in a 96,88%. 
* [alumno1:src/main/java/App.java] compared with [alumno2:src/main/java/App.java]: files match in a 100,00%. 
* [alumno1:src/main/java/Configuracion.java] compared with [alumno2:src/main/java/Configu.java]: files match in a 100,00%. 
* [alumno1:src/main/java/Controlador.java] compared with [alumno2:src/main/java/Controller.java]: files match in a 100,00%. 
* [alumno1:src/main/java/Main.java] compared with [alumno2:src/main/java/Main.java]: files match in a 100,00%. 
* [alumno1:src/main/java/Modelo.java] compared with [alumno2:src/main/java/Model.java]: files match in a 100,00%. 
* [alumno1:src/main/resources/fxml/Ventana.fxml] compared with [alumno2:src/main/resources/fxml/Window.fxml]: files match in a 100,00%. 

```

## Atribuciones

-   El [icono del buitre](https://www.flaticon.com/premium-icon/vulture_1747203) ![Vulture](vulture-24px.png) ha sido creado por [Freepik](https://www.freepik.com) en [www.flaticon.com](https://www.flaticon.com).

