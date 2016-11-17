/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package turing;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import mineria.*;

/**
 *
 * @author EMARTINENE
 */
public class Turing {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /*
	N = individuos;
	Pc= porcentaje_cruza;
	Pm= porcentaje_muta;
	G = generaciones;
        T = Tamaño de la cinta
	MM= minimiza; 0 minimiza / 1 maximiza
        */
        
        int N = 500;
        int E = 1024;
        int D = 0;
        int V = 1;
        double Pc = 0.9;
        double Pm = 0.01;
        int G = 500;
        int MM = 0;
        int tamaño_cinta=3000;
        
        BufferedReader Fbr,Kbr;
        Kbr = new BufferedReader(new InputStreamReader(System.in));
        
        EGA ega = new EGA();
        ega.setParams(N, E, D, V, Pc, Pm, G, MM, tamaño_cinta);
        
        System.out.println("Calcular la complejidad de Kolmogorov para una cadena indicada");
        System.out.println("Parametros del algoritmo genético ");
        String cad;
        try {
            do{
                ega.DispParams();
                System.out.println("");
                System.out.print("Introduce una cadena o @Parametro NuevoValor: ");
                cad = Kbr.readLine();
                if(cad.contains("@")){
                    String[] valores = cad.split(" ");
                    int num = Integer.parseInt(valores[0].substring(1));
                    double val = Double.parseDouble(valores[1]);
                    ega.setParams(num, val);
                }
            }while (cad.contains("@"));
            // String cad = "ABCDABCDABCD";
            int [] cadenaObjetivo = cadenaABinario(cad);
        
            Fit f = new Fit(tamaño_cinta, cadenaObjetivo);
            mineria.Resultado res = ega.ejecutarAlgoritmoGenetico(f);
            
            System.out.println("Mejor Fitness: " + res.fitnessSemental);
            System.out.println("Cadena objetivo: " + BinarioACadena(cadenaObjetivo));
            System.out.println("Cadena original, codificada:");
            for(int i=0;i<cadenaObjetivo.length;i++){
                System.out.print("" + cadenaObjetivo[i]);
            }
            System.out.println("");

            int[] cintaSemental = f.cintaIndividuo(res.genomaSemental);
            System.out.println("Cadena final: " + BinarioACadena(cintaSemental));
            System.out.println("Cadena final, codificada: ");
            for(int i=0;i<cintaSemental.length;i++){
                System.out.print("" + cintaSemental[i]);
            }
            System.out.println("");
            
            f.imprimirMaquina(res.genomaSemental);
            System.out.println("");
        }
        catch(Exception ex){
            System.out.println("Excepcion cadena no valida: " + ex.getMessage());
        }
        
        
    }
    
    public static int[] cadenaABinario(String cadena){
        byte[] bytes = cadena.getBytes(StandardCharsets.US_ASCII);
        int[] binario = new int[bytes.length*8];
        int[] nums = {128,64,32,16,8,4,2,1};
        for(int i=0;i<bytes.length;i++){
            int residuo = bytes[i], entero = 0;
            for(int j=0;j<nums.length;j++){
                int temp = residuo % nums[j]; 
                entero = residuo/nums[j];
                if(entero>0){
                    residuo = temp;
                }
                binario[i*8+j]=entero;
            }
        }
        //ByteBuffer bytes = ByteBuffer.wrap(cadena.getBytes(StandardCharsets.UTF_8));
        // you must specify a charset
        //IntBuffer ints = bytes.asIntBuffer();
        //int numInts = ints.remaining();
        //int[] result = new int[numInts];
        //ints.get(result);
        return binario;
    }
    
    public static String BinarioACadena(int[] binario){
        int caracteres = binario.length / 8;
        char[] chars = new char[caracteres];
        int[] nums = {128,64,32,16,8,4,2,1};
        int val = 0;
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<caracteres;i++){
            val = 0;
            val += binario[i*8] * nums[0];
            val += binario[i*8+1] * nums[1];
            val += binario[i*8+2] * nums[2];
            val += binario[i*8+3] * nums[3];
            val += binario[i*8+4] * nums[4];
            val += binario[i*8+5] * nums[5];
            val += binario[i*8+6] * nums[6];
            val += binario[i*8+7] * nums[7];
            sb.append((char)val);
        }
        return sb.toString();
    }
}
