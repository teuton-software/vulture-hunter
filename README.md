# JavaProjectCompare
Herramienta de comparación que determina el grado de similitud entre dos proyectos (también conocido como **El Ojo de Águila** o **Cazador de Buitres**). Está pensado para detectar si los alumnos se han copiado en proyectos de desarrollo de software, comparando el código fuente entregado.

## Uso

**JavaProjectCompare** se utiliza desde la línea de comandos. El comando es `jpc`.

### Consultar la ayuda

Para consultar la ayuda del comando, ejecutamos `jpc` sin parámetros o con las opciones `-h` o `--help`:

```bash
jpc --help
```

El resultado sería algo similar a lo siguiente:

```bash
usage: jpc
 -a,--all <folder>                compare all projects in specified folder
 -c,--compare <folder1,folder2>   compare two projects
 -h,--help                        print this message
 -s,--similarity                  degree of similarity between projects
                                  (default value: 75.0)
 -t,--threshold                   degree of similarity between files to be
                                  considered equal (default value: 80.0)
```

## Comparar dos proyectos

Para comparar dos proyectos y ver el grado de similitud entre ambos, utilizamos la opción `-c` o `--compare`:

```bash
jpc --compare path/to/project1,path/to/project2
```

## Comparar múltiples proyectos

Es posible comparar entre sí todos los proyectos almacenados en una carpeta con la opción `-a` o `--all`:

```bash
jpc --all path/to/parent/folder
```

> Esta opción está pensada para comparar proyectos entregados a través de un plataforma e-Learning como Moodle, donde es posible descargar un ZIP con todos las entregas. Una vez descomprimimos el ZIP con las entregas, podemos lanzar `jpc` con la opción `--all`.

## Opciones

Al comparar proyectos, tanto con `--compare` como con `--all`, disponemos de las siguientes opciones:

| Opción               | Descripción                                                  |
| -------------------- | ------------------------------------------------------------ |
| `-s`, `--similarity` | Porcentaje de similitud entre dos proyectos para ser considerados sospechosos. Por defecto es 75%. |
| `-t`, `--threshold`  | Porcentaje de similitud entre dos ficheros para ser considerados iguales. Por defecto es 80%. |

## Ejemplo

El siguiente comando compara los proyectos "alumno1" y "alumno2":

```bash
jpc --compare alumno1,alumno2 -t 75
```

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

