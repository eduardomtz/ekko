# ekko
Obtener la máquina de turing mínima para cierta cadena, utilizando algoritmos genéticos (aproximación de la complejidad de Kolmogorov)

# Compilacion
ant compile <- compila el proyecto
ant jar <- genera el jar

## Si no se quiere utilizar ant
Extraer Manifest del proyecto
jar xf Turing/dist/Turing.jar META-INF/MANIFEST.MF
Generar todos los archivos .java
javac -encoding UTF8 src/turing/Turing.java -classpath build/classes/
Crear jar
jar cmvf META-INF/MANIFEST.MF Turing.jar ./

## Archivo META-INF/MANIFEST.MF
Manifest-Version: 1.0
Ant-Version: Apache Ant 1.9.4
Created-By: 1.8.0_60-b27 (Oracle Corporation)
Class-Path: 
X-COMMENT: Main-Class will be added automatically by build
Main-Class: turing.Turing

## Contenido del jar
META-INF/
META-INF/MANIFEST.MF
classes/
classes/mineria/
classes/mineria/AGF.class
classes/mineria/EGA.class
classes/mineria/FitnessEGA.class
classes/mineria/Resultado.class
classes/turing/
classes/turing/Fit.class
classes/turing/Turing.class

## Ejecucion
java -jar Turing.jar
