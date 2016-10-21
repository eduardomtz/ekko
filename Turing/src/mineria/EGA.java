/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mineria;

/**
 *
 * @author eduardomartinez
 */
import java.io.*;
import java.math.*;

public class EGA {
    int tamañoCinta = 0;
   static int numVects=10;
   static double Vects [][] = new double [numVects][2];
   static int Clase [] = new int [numVects];
  int E,D,V,N,N_2,L,L_2,FN=1,G,MM,B2M,Nx2,iTmp,Best,n;
   String Resp;
  double Pc, Pm;
   double Norm, fTmp;
   double data []=new double [317]; 
/*
 *		LA SIGUIENTE VARIABLE REGULA EL NÚMERO DE FUNCIONES DEFINIDAS
 */
   int maxF=5, minF=1;
   int maxN=500,minN=1;
   int maxE=15, minE=0;
   int maxD=60, minD=0;
   int maxV=70, minV=1;
   double maxPc=1f, minPc=.01f;
   double maxPm=1f, minPm=.001f;
   int maxG=10000, minG=1;
   int maxM=1,  minM=0;
   int maxStringsInDeceptive=65536;
//
  public  double Var[][]=new double [maxN][maxV]; // matrix con individuos
  public  String genoma [];
  public  double fitness[];
  public  BufferedReader Fbr,Kbr;
  

  private void PoblacionInicial(String genoma[]){
	/*
	 *Genera N individuos aleatoriamente
	 */
  	for (int i=0;i<N;i++){
  		genoma[i]="";
                // cada uno de longitud L
		for (int j=1;j<=L;j++){
			if (Math.random()<0.5)
				genoma[i]=genoma[i].concat("0");
			else
		  		genoma[i]=genoma[i].concat("1");
		  	//endIf
		}//endFor
  	}//endFor
  }//endPoblacionInicial

  // fenotipo es la codificacion en bits para cada individuo
  private String GetGenoma(int i){
	String G=genoma[i];
	return G;
  }//endFenotipo

  // Dada una funcion de fitness, evalua a cada uno de los individuos
  private double Evalua(int num_individuo, FitnessEGA fitness) {
	double F=0.0;
        String individuo = GetGenoma(num_individuo);
        F = fitness.calificaIndividuo(individuo);
        // System.out.println("Calificación individuo " + num_individuo + ": " + F);
	return F;
  }//endEvalua

  private void Duplica(double fitness[],String genoma[]){
	for (int i=0;i<N;i++){
		genoma [N+i]=genoma [i];
		fitness[N+i]=fitness[i];
	}//endFor
  }//endCopia
  
/*		Selecciona los mejores N individuos
 *
 */
  private void Selecciona(double fitness[],String genoma[]) {
  	double fitnessOfBest,fTmp;
  	String sTmp;
	int indexOfBest;
	if (MM==0){					// Minimiza
	  	for (int i=0;i<N;i++){
		  	fitnessOfBest=fitness[i];
			indexOfBest  =i;
	  		for (int j=i+1;j<Nx2;j++){
  				if (fitness[j]<fitnessOfBest){
  					fitnessOfBest=fitness[j];
	  				indexOfBest  =j;
  				}//endIf
  			}//endFor
	  		if (indexOfBest!=i){
  				sTmp=genoma[i];
  				genoma[i]=genoma[indexOfBest];
  				genoma[indexOfBest]=sTmp;
	 			fTmp=fitness[i];
 				fitness[i]=fitness[indexOfBest];
 				fitness[indexOfBest]=fTmp;
	  		}//endIf
	  	}//endFor
	}else{						// Maximiza
	  	for (int i=0;i<N;i++){
		  	fitnessOfBest=fitness[i];
			indexOfBest  =i;
	  		for (int j=i+1;j<Nx2;j++){
  				if (fitness[j]>fitnessOfBest){
  					fitnessOfBest=fitness[j];
	  				indexOfBest  =j;
  				}//endIf
  			}//endFor
	  		if (indexOfBest!=i){
  				sTmp=genoma[i];
  				genoma[i]=genoma[indexOfBest];
  				genoma[indexOfBest]=sTmp;
	 			fTmp=fitness[i];
 				fitness[i]=fitness[indexOfBest];
 				fitness[indexOfBest]=fTmp;
	  		}//endIf
	  	}//endFor
	}//endIf
	return;
  }//endSelecciona

  private void Cruza(String genoma[]){
  	int N_i,P;
	String LI,MI,RI,LN,MN,RN;
	for (int i=0;i<N_2;i++){
		if (Math.random()>Pc) continue;
		N_i=N-i-1;
		
                P=0; while (!(1<=P&P<=L_2-1)) P=(int)(Math.random()*L_2);
                
		LI=genoma[i  ].substring(0,P);
		MI=genoma[i  ].substring(P,P+L_2);
		RI=genoma[i  ].substring(P+L_2);
		LN=genoma[N_i].substring(0,P);
		MN=genoma[N_i].substring(P,P+L_2);
		RN=genoma[N_i].substring(P+L_2);
		genoma[i  ]=LI.concat(MN).concat(RI);
		genoma[N_i]=LN.concat(MI).concat(RN);
	}//endFor
  }//endCruza

  private void Muta(String genoma[]) {
	int nInd, nBit;
	for (int i=1;i<=B2M;i++){
		nInd=-1; while (nInd<0|nInd>=N) nInd=(int)(Math.random()*N);
		nBit=-1; while (nBit<0|nBit>=L) nBit=(int)(Math.random()*L);
/*
 *		** Mutation **
 */
		String mBit="0";
		String G=genoma[nInd];
		if (nBit!=0&nBit!=L-1){
		 if (G.substring(nBit,nBit+1).equals("0")) mBit="1";
		 genoma[nInd]=G.substring(0,nBit).concat(mBit).concat(G.substring(nBit+1));
		 continue;
		}//endif
		if (nBit==0){
			if (G.substring(0,1).equals("0")) mBit="1";
			genoma[nInd]=mBit.concat(G.substring(1));
			continue;
		}//endif
//		if (nBit==L-1){
			if (G.substring(L-1).equals("0")) mBit="1";
			genoma[nInd]=G.substring(0,L-1).concat(mBit);
//		}endIf
	}//endFor
  }//endMuta

  
   public void CreaParams() throws Exception {
	  try {
		Fbr=new BufferedReader(new InputStreamReader(new FileInputStream(new File("AGParams.dat"))));
	  }//endTry
	  catch (Exception e){
	    PrintStream Fps=new PrintStream(new FileOutputStream(new File("AGParams.dat")));
		Fps.println("1");	//1) Funcion
		Fps.println("50");	//2) Individuos
		Fps.println("4");	//3) Bits para Enteros
		Fps.println("25");	//4) Bits para Decimales
		Fps.println("2");	//5) Variables
		Fps.println("0.9");	//6) Pc //proba cruzamiento
		Fps.println("0.01");    //7) Pm //proba mutacion
		Fps.println("100");	//8) Generaciones
		Fps.println("0");	//9) Minimiza
	  }//endCatch
  }//endCreaParams
  
  public void setParams(int individuos, int bits_enteros, int bits_decimales,
                        int variables, double porcentaje_cruza, double porcentaje_muta,
                        int generaciones, int minimiza, int cinta) {
	  N =individuos;
	  E =bits_enteros;
	  D =bits_decimales;
	  V =variables;
	  Pc=porcentaje_cruza;
	  Pm=porcentaje_muta;
	  G =generaciones;
	  MM=minimiza;
          tamañoCinta= cinta;
          CalcParams();
  }//endsetParams
  
  public void DispParams() {
	System.out.println();
	System.out.println("1) Numero de individuos:    "+ N);
	System.out.println("** Long. del genoma:        "+ L);
	System.out.printf ("2) Prob. de cruzamiento:    %8.6f\n",Pc);
	System.out.printf ("3) Prob. de mutacion:       %8.6f\n",Pm);
	System.out.println("4) Numero de generaciones:  "+ G);
        System.out.println("5) Tamaño de la cinta: " + tamañoCinta);
	System.out.println("5) Minimiza[0]/Maximiza[1]: "+MM);
  }//endDispParams

  private boolean CheckParams(int Opcion) {
	switch(Opcion) {
		case 1: {FN=iTmp; if (FN<minF|FN>maxF)   return false; break;}
		case 2: {N =iTmp; if (N<minN|N>maxN)     return false; break;}
		case 3: {E =iTmp; if (E<minE|E>maxE)     return false; break;}
		case 4: {D =iTmp; if (D<minD|D>maxD)     return false; break;}
		case 5: {V =iTmp; if (V<minV|V>maxV)     return false; break;}
		case 6: {Pc=fTmp; if (Pc<minPc|Pc>maxPc) return false; break;}
		case 7: {Pm=fTmp; if (Pm<minPm|Pm>maxPm) return false; break;}
		case 8: {G =iTmp; if (G<minG|G>maxG)     return false; break;}
		case 9: {MM=iTmp; if (MM<minM|MM>maxM)   return false; break;}
	}//endSwitch
	return true;
  }//endCheckParams

  private void CalcParams() {
	N_2=N/2;
	Nx2=N*2;
	genoma = new String [Nx2];
	fitness= new double [Nx2];
	Norm=Math.pow(2,D);
	L=V*(E+D);
	L_2=L/2;
	B2M=(int)((double)N*(double)L*Pm);				//Bits to Mutate
  }//endCalcParams
  
  public Resultado ejecutarAlgoritmoGenetico(FitnessEGA funcionFitness)
  {
      Resultado res = new Resultado();
      System.out.println("Inicia algoritmo genético");
      try{
	// 							//Calcula parametros adicionales, a partir de los parametros
        //EMPIEZA EL ALGORITMO GENETICO
	int Gtemp=G;
        
 	PoblacionInicial(genoma);				//Genera la poblacion inicial
	for (int i=0;i<N;i++){                          //Evalua los primeros N
                fitness[i] = Evalua(i, funcionFitness);
        }				
	for (int g=1;g<=G;g++){
            
            
            Duplica(fitness,genoma);			//Duplica los primeros N
            Cruza(genoma);						//Cruza los primeros N
            Muta(genoma);						//Muta los primeros N
            
            for (int i=0;i<N;i++){ //para cada individuo
                fitness[i] = Evalua(i, funcionFitness);
            }
            
            Selecciona(fitness,genoma);			//Selecciona los mejores N
            
            System.out.println("Generación: " + g + ", fitness: " + fitness[0]);
	}//endFor
        
        System.out.println("Finaliza algoritmo genético\n\n");
               
        res.fitnessSemental = fitness[0];
        res.genomaSemental = genoma[0];
        
	G=Gtemp;
      }
      catch(Exception e)
      {
        System.out.println("Excepcion: " + e.getMessage());
      }
      
      return res;
  }
}
//endClass

