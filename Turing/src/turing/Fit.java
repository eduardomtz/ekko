/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package turing;

import mineria.FitnessEGA;

/**
 *
 * @author EMARTINENE
 */
class Fit implements FitnessEGA {
    
    // int[] cinta;
    int[] cintaObjetivo;
    int ciclos = 1000;
    int longCinta = 0;
    
    public Fit(int _longCinta, int[] _cintaObjetivo){
        longCinta = _longCinta;
        // cinta = new int[longCinta];
        // for(int i=0;i<longCinta;i++) {
        //    cinta[i] = 0;
        // }
        cintaObjetivo = _cintaObjetivo;
    }
    
    @Override
    public double calificaIndividuo(String genoma) {
        int[] cintalocal = new int[longCinta];
        for(int i=0;i<longCinta;i++) {
            cintalocal[i] = 0;
        }
        
        int num_estados = 64;
        int [][] estados = new int [num_estados][6];
        
        // decodificar estados de la maquina de turing
        for(int i=0;i<num_estados;i++) {
            int inicio = i*16;
            int fin = inicio + 16;
            String substring = genoma.substring(inicio, fin);
            // caso 0: estado_siguiente, 0/1, Movimiento
            estados[i][0] = booleanStringToInt(substring.substring(0, 6)); // estado
            estados[i][1] = booleanStringToInt(substring.substring(6, 7)); // reemplazo
            estados[i][2] = booleanStringToInt(substring.substring(7, 8)); // left 0 / right 1
            // caso 1:
            estados[i][3] = booleanStringToInt(substring.substring(8, 14));
            estados[i][4] = booleanStringToInt(substring.substring(14, 15));
            estados[i][5] = booleanStringToInt(substring.substring(15, 16)); // left 0 / right 1
        }
        int estado = 0;
        int indiceCinta = cintalocal.length/2;
        for(int c=0;c<ciclos;c++){
            
            // estado 63 es halt
            if(estado == 63 || indiceCinta >= cintalocal.length || indiceCinta < 0)
                break;
            int lado = cintalocal[indiceCinta] == 1 ? 3 : 0; // indice si es estado 0 o 1
            int reemplazo = estados[estado][lado+1];
            cintalocal[indiceCinta] = reemplazo;
            if(estados[estado][lado+2] == 0){
                indiceCinta--;
            }
            else {
                indiceCinta++;
            }
            // System.out.println("Estado: " + estado + ", pasa a: " + estados[estado][lado] + ", escribe: " + reemplazo + ", indice cinta: " + indiceCinta);
            estado = estados[estado][lado];
        }
        int i = 0;
        double resultado = 0.0;
        for(int j=cintalocal.length/2;j<(cintalocal.length/2)+cintaObjetivo.length;j++){
            if(cintalocal[j] == cintaObjetivo[i])
            {
                // resultado++;
            }
            else
            {
                resultado++;
            }
            i++;
        }
        
        
        // resultado+=maquina_size;
        
        // System.out.println("INDIVIDUO CALIFICACION: " + resultado);
        return resultado;
    }
    
    private int booleanStringToInt(String cadena){
        int valor = 0;
	int i = 0;
        for (int j=cadena.length();j>0;j--){
            if (cadena.substring(j-1, j).equals("1"))
            {
                valor += Math.pow(2, i);
            }
            i++;
        }
        return valor;
    }
    
    public int[] cintaIndividuo(String genoma) {
        
        int[] cintalocal = new int[longCinta];
        for(int i=0;i<longCinta;i++) {
            cintalocal[i] = 0;
        }
        
        int num_estados = 64;
        int [][] estados = new int [num_estados][6];
        // decodificar estados de la maquina de turing
        for(int i=0;i<num_estados;i++) {
            int inicio = i*16;
            int fin = inicio + 16;
            String substring = genoma.substring(inicio, fin);
            // caso 0: estado_siguiente, 0/1, Movimiento
            estados[i][0] = booleanStringToInt(substring.substring(0, 6)); // estado
            estados[i][1] = booleanStringToInt(substring.substring(6, 7)); // reemplazo
            estados[i][2] = booleanStringToInt(substring.substring(7, 8)); // left 0 / right 1
            // caso 1:
            estados[i][3] = booleanStringToInt(substring.substring(8, 14));
            estados[i][4] = booleanStringToInt(substring.substring(14, 15));
            estados[i][5] = booleanStringToInt(substring.substring(15, 16)); // left 0 / right 1
        }
        int estado = 0;
        int indiceCinta = cintalocal.length/2;
        for(int c=0;c<ciclos;c++){
            // estado 63 es halt
            if(estado == 63 || indiceCinta >= cintalocal.length || indiceCinta < 0)
                break;
            int lado = cintalocal[indiceCinta] == 1 ? 3 : 0; // indice si es estado 0 o 1
            int reemplazo = estados[estado][lado+1];
            cintalocal[indiceCinta] = reemplazo;
            if(estados[estado][lado+2] == 0){
                indiceCinta--;
            }
            else {
                indiceCinta++;
            }
            // System.out.println("Estado: " + estado + ", pasa a: " + estados[estado][lado] + ", escribe: " + reemplazo + ", indice cinta: " + indiceCinta);
            estado = estados[estado][lado];
        }
        
        int [] cintaReturn = new int [cintaObjetivo.length];
        
        int i = 0;
        for(int j=cintalocal.length/2;j<(cintalocal.length/2)+cintaObjetivo.length;j++){
            cintaReturn[i] = cintalocal[j];
            i++;
        }
        
        return cintaReturn;
    }
    
    public void imprimirMaquina(String genoma) {
        System.out.println("La máquina resultante es:");
        int num_estados = 64;
        int[] cintalocal = new int[longCinta];
        for(int i=0;i<longCinta;i++) {
            cintalocal[i] = 0;
        }
        
        int[] estadoVisitado = new int[num_estados];
        for(int i=0;i<num_estados;i++){
            estadoVisitado[i] = 0;
        }
        
        
        int [][] estados = new int [num_estados][6];
        // decodificar estados de la maquina de turing
        for(int i=0;i<num_estados;i++) {
            int inicio = i*16;
            int fin = inicio + 16;
            String substring = genoma.substring(inicio, fin);
            // caso 0: estado_siguiente, 0/1, Movimiento
            estados[i][0] = booleanStringToInt(substring.substring(0, 6)); // estado
            estados[i][1] = booleanStringToInt(substring.substring(6, 7)); // reemplazo
            estados[i][2] = booleanStringToInt(substring.substring(7, 8)); // left 0 / right 1
            // caso 1:
            estados[i][3] = booleanStringToInt(substring.substring(8, 14));
            estados[i][4] = booleanStringToInt(substring.substring(14, 15));
            estados[i][5] = booleanStringToInt(substring.substring(15, 16)); // left 0 / right 1
        }
        int estado = 0;
        int indiceCinta = cintalocal.length/2;
        
        for(int c=0;c<ciclos;c++){
            if(estadoVisitado[estado]==0){
                estadoVisitado[estado]=1;
                // impresion de la maquina
                String mov0 = estados[estado][2] == 0 ? "I" : "D";
                String mov1 = estados[estado][5] == 0 ? "I" : "D";
                System.out.println("Estado: " + estado + ", Valor cinta: 0, Escribe: " + estados[estado][1] + ", Mueve: " + mov0 + ", Sig. estado: " + estados[estado][0] + 
                    ", Valor cinta: 1, Escribe: " + estados[estado][4] + ", Mueve: " + mov1 + ", Sig. estado: " + estados[estado][3]);
            }
            
            // estado 63 es halt
            if(estado == 63 || indiceCinta >= cintalocal.length || indiceCinta < 0)
                break;
            int lado = cintalocal[indiceCinta] == 1 ? 3 : 0; // indice si es estado 0 o 1
            int reemplazo = estados[estado][lado+1];
            cintalocal[indiceCinta] = reemplazo;
            if(estados[estado][lado+2] == 0){ // izquierda
                indiceCinta--;
            }
            else { // derecha
                indiceCinta++;
            }
            // System.out.println("Estado: " + estado + ", pasa a: " + estados[estado][lado] + ", escribe: " + reemplazo + ", indice cinta: " + indiceCinta);
            estado = estados[estado][lado];
        }
        
        int [] cintaReturn = new int [cintaObjetivo.length];
        
        int i = 0;
        for(int j=cintalocal.length/2;j<(cintalocal.length/2)+cintaObjetivo.length;j++){
            cintaReturn[i] = cintalocal[j];
            i++;
        }
        
        int maquina_size = 0;
        for(i=0;i<num_estados;i++){
            maquina_size+=estadoVisitado[i];
        }
        
        i = 0;
        double resultado = 0.0;
        for(int j=cintalocal.length/2;j<(cintalocal.length/2)+cintaObjetivo.length;j++){
            if(cintalocal[j] == cintaObjetivo[i])
            {
                // resultado++;
            }
            else
            {
                resultado++;
            }
            i++;
        }
        
        System.out.println("");
        System.out.println("Estados de la MT: " + maquina_size  );
        System.out.println("La complejidad de Kolmogorov, es de: " + maquina_size * 16 + " bits" );
    }
}
