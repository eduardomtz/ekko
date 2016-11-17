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
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class AGF {
  
  public int novars;
  public double data [][];
  
  public void LeerDatos(String archivo, int nodatos)
  {
      BufferedReader Fbr;
	try{
            Fbr=new BufferedReader(new InputStreamReader(new FileInputStream(new File(archivo))));
        }
	catch (Exception e){
            System.out.println("No se encuentra <proyectofinal.txt> ");
            return;
        }
	int n=0;//Contador de lineas
	String sn;
        data = new double [nodatos][novars+1];
        try{
            int i = 0;
            while ((sn = Fbr.readLine()) != null) {
                String [] input=null;
                input=sn.split("\t");//Separa los campos deben de ser por tabulador
		for(int j=0;j<novars;j++){
			data[i][j]=Double.parseDouble(input[j]);
		}
                i++;
		//data[i][novars] = -1;
            }
        }// endFor
        catch(Exception e) { System.out.println("Error al leer linea " + e.getMessage());
            data= new double[1][2];
        }
	//return n;
  }
  
  public AGF(int _novars)
  {
      this.novars = _novars;
      data= new double[2][this.novars+1];
      for(int i=0;i<data.length;i++)
      {
          for(int j=0;j<data[i].length;j++)
              data[i][j]=0;
      }
  }
  
  //centros trae el número de atributos por el número de clusters
  //hay que identificar de que indice a que indice va cada cluster
  public double FitnessDistanciaMinima(double Centros[], int vars) 
  {
      double[] distancia = new double[data.length];
      double total = 0.0;
      for(int i=0;i<data.length;i++)
      {
          int clusters = vars/novars;
          double[] intracluster = new double[clusters];
          //para cada dato multiplicar por cada uno de los centros
          for(int j=0;j<clusters;j++)
          {
              int idxinicio = j*novars;
              double temp = 0.0;
              for(int k=0;k<novars;k++)
              {
                  temp += Math.pow(data[i][k] - Centros[idxinicio+k],2);
              }
              intracluster[j] = Math.sqrt(temp);
          }
          distancia[i]=minValue(intracluster);
          total += distancia[i];
      }
      return total;
  }
  
  //centros trae el número de atributos por el número de clusters
  //hay que identificar de que indice a que indice va cada cluster
  public double FitnessDistanciaMinimaConVNN(double Centros[], int vars) 
  {
      double[] distancia = new double[data.length];
      double total = 0.0;
      int clusters = vars/novars;
      for(int i=0;i<data.length;i++)
      {
          double[] intracluster = new double[clusters];
          //para cada dato multiplicar por cada uno de los centros
          for(int j=0;j<clusters;j++)
          {
              int idxinicio = j*novars;
              double temp = 0.0;
              for(int k=0;k<novars;k++)
              {
                  temp += Math.pow(data[i][k] - Centros[idxinicio+k],2);
              }
              intracluster[j] = Math.sqrt(temp);
          }
          distancia[i]=minValue(intracluster); //minValue(intracluster);
          total += distancia[i];
      }
      
      return total + FitnessVNN(Centros,vars);
  }
  
  public double FitnessVNN(double Centros[], int vars) 
  {
      int clusters = vars/novars;
      
      int[] clasificacion = etiqueta(Centros, clusters);
      int[] elementosEnCluster = totalElementos(clasificacion, clusters); //cardinalidad
      
      double[] distanciaNN = new double[data.length];
      
      double[] mediadNN = new double[clusters];
      double[] varianza = new double[clusters];
      
      for(int i=0;i<clusters;i++)
      {
          mediadNN[i]=0.0;
          varianza[i]=0.0;
      }
      
      for(int i=0;i<data.length;i++)
      {
          int cardinalidad = elementosEnCluster[clasificacion[i]];
          if(cardinalidad<2)
          {
              int detener=0;
          }
          
          if(cardinalidad>1)
          {
            double[] distxiVSSimilares = new double[cardinalidad-1]; //todos excepto el mismo
            int l = 0;
            for(int j=0;j<data.length;j++)
            {
                if(i!=j && clasificacion[i]==clasificacion[j]) //si son elementos diferentes y pertenecen al mismo cluster
                {
                  double temp = 0.0;
                  for(int k=0;k<novars;k++)
                  {
                      temp += Math.pow(data[i][k] - data[j][k],2);
                  }
                  distxiVSSimilares[l] = Math.sqrt(temp);
                  l++;
                }
            }
            distanciaNN[i]=minValue(distxiVSSimilares);
            mediadNN[clasificacion[i]] += distanciaNN[i];
          }
      }
      
      //calcular la varianza de cada cluster
      for(int i=0;i<clusters;i++)
      {
          mediadNN[i] =  mediadNN[i]/elementosEnCluster[i];
      }
      
      double totalVarianza = 0.0;
      for (int i=0;i<distanciaNN.length;i++)
      {
            varianza[clasificacion[i]] += Math.pow(distanciaNN[i]-mediadNN[clasificacion[i]],2);
      }
      
      for(int i=0;i<clusters;i++)
      {
          int cardinalidad = elementosEnCluster[i];
          //double c = 1/(cardinalidad-1);
          //if(cardinalidad!=1)
          varianza[i] = varianza[i]/(cardinalidad-1);
          totalVarianza += varianza[i];
      }
      
      return totalVarianza;
  }
  
  public double FitnessDunnDunn(double Centros[], int vars)
  {
      int clusters = vars/novars;
      int[] clasificacion = etiqueta(Centros, clusters);
      int[] elementosEnCluster = totalElementos(clasificacion, clusters); //cardinalidad
      
      
      double[] distMax = new double[clusters];
      //radio del cluster, distancia máxima
      for(int j=0;j<data.length;j++)
      {
          int numcluster = clasificacion[j];
          int idxinicio = numcluster*novars;
          double temp = distanciaDatoCluster(data[j], Centros, novars, idxinicio);
          if(temp > distMax[clasificacion[j]])
              distMax[clasificacion[j]]=temp;
      }
      
      //radio máximo de clusters
      double radioMaximo = 0.0;
      for(int i=0;i<distMax.length;i++)
      {
          if(distMax[i]>radioMaximo)
          {
              radioMaximo = distMax[i];
          }
      }
      
      //distancia entre clusters
      //double[] dissimilarity = new double[clusters];
      double[] resultado = new double[clusters-1];
      for(int i=0; i<clusters-1;i++)
      {
          //cluster i vs otros clusters
          double[] divisionMinima = new double[clusters-1];
          int p = 0;
          for(int j=i+1;j<clusters;j++)
          {
              if(i!=j)
              {
                if(elementosEnCluster[j] > 0 && elementosEnCluster[i] >0)
                {
                    //distancias minimas de todos los i
                    double[] distanciasDatosI = new double[elementosEnCluster[i]];
                    int o = 0;
                    for(int k=0;k<data.length;k++)
                    {
                        //el primer dato pertenece al cluster i
                        if(clasificacion[k]==i)
                        {
                            //ese dato k vs todos los demas
                            double[] distanciasDatosJ = new double[elementosEnCluster[j]];
                            int n = 0;
                            for(int l=0;l<data.length;l++)
                            {
                              if(clasificacion[l]==j)
                              {
                                  double temp = 0.0;
                                  for(int m=0;m<novars;m++)
                                  {
                                      temp += Math.pow(data[k][m] - data[l][m],2);
                                  }
                                  temp = Math.sqrt(temp);
                                  distanciasDatosJ[n]=temp;
                                  n++;
                              }
                            }
                            distanciasDatosI[o]=minValue(distanciasDatosJ);
                            o++;
                        }
                    }
                    double dissimilarity = minValue(distanciasDatosI);
                    double division = dissimilarity/radioMaximo;
                    divisionMinima[p] = division;
                    p++;
                  }
                  else
                  {
                      //divisionMinima = new double[1];
                      //divisionMinima[0] = 10000;
                      return 10000;
                  }
              }
          }
          resultado[i] = minValue(divisionMinima);
      }
      return minValue(resultado);
      
      /*
      for(int i=0;i<clusters-1;i++)
      {
          int idxinicio_i = i*novars;
          double min = 0.0;
          double[] distanciaClusters = new double[clusters-1];
          int k=0;
          for(int j=i+1;j<clusters;j++)
          {
              int idxinicio_j = j*novars;
                  // distancia minima entre clusters i,j
                  double temp = distanciaClusterCluster(Centros, novars, idxinicio_i,idxinicio_j);
                  temp = temp/radioMaximo;
                  distanciaClusters[k] = temp;
                  k++;
          }
          distanciaClusters2[i] = minValue(distanciaClusters);
      }
      */
  }

    private double distanciaDatoCluster(double[] dato, double[] cluster, int vars,int inicioCluster)
    {
        double temp = 0.0;
        for(int k=0;k<vars;k++)
        {
            temp += Math.pow(dato[k] - cluster[inicioCluster+k],2);
        }
        temp = Math.sqrt(temp);
        return temp;
    }
  
  private double minValue(double[] vars) {
      if(vars.length>0)
      {
            double min = vars[0];
            for (int i = 0; i < vars.length; i++) {
                    if (vars[i] < min) {
                            min = vars[i];
                    }
            }
            return min;
      }
      else
      {
          return 0;
      }
  }
  
  private double avgValue(double[] vars) {
      if(vars.length>0)
      {
            double total = 0.0;
            for (int i = 0; i < vars.length; i++) {
                    total += vars[i];
            }
            return total/vars.length;
      }
      else
      {
          return 0;
      }
  }
  
  private int minIndex(double[] vars) {
	double min = vars[0];
	int index = 0;
	for (int i = 0; i < vars.length; i++) {
		if (vars[i] < min) {
			min = vars[i];
			index = i;
		}
	}
	return index;
  }
  
  private int[] etiqueta(double centros[], int clus){
    int[] clases = new int[data.length];
    for(int i=0;i<data.length;i++){
        int clusters = clus;
	double[] intracluster = new double[clusters];
  	for(int j=0;j<clusters;j++){ 
            int idxinicio = j*novars;
            double temp = 0.0;
            for(int l=0;l<novars;l++) {
                temp += Math.pow(data[i][l]-centros[idxinicio+l],2);
            }
            intracluster[j] = Math.sqrt(temp);
  	}
	clases[i]=minIndex(intracluster);
     }
     return clases;
  }
  
  private int[] totalElementos(int clasificacion[], int clusters){
      int[] totales = new int[clusters];
      
      for(int i=0;i<clusters;i++)
      {
          totales[i]=0;
      }
      
      
  	for(int j=0;j<clasificacion.length;j++){ 
            totales[clasificacion[j]]++;
        }
     
     return totales;
  }
  
  
  public double F01(double X,double Y){
/*
 * (1)
 *
 *	Minimizar:	
 *		X^2-2XY+Y^3+X+Y-2
 *	X=-16
 *	Y=-16
 *	F(X,Y)=-4,386
 *
 *	Maximizar
 *		X^2-2XY+Y^3+X+Y-2
 *	X=-16
 *	Y=+16
 *	F(X,Y)=+4,862
 *
 *	N=200
 *	E=4
 *	D=40
 *	Pc=0.95
 *	Pm=0.005
 *	G=1000
 * 
 */
	double Z;
	Z=Math.pow(X,2)-2*X*Y+Math.pow(Y,3)+X+Y-2; 
	return Z;
  }//endF01

  public double F02(double X){
  double Y;
  /*
   * (2)
   *
   *	Resolver la siguiente ecuación cúbica:
   *
   *	Y=X^3+2.5X^2-2X+1
   *
   *	MINIMIZAR CON LAS RESTRICCIONES
   *
   *  X=-3.2180565 --> Y=0.0000000
   *  N=200
   *  E=4
   *  D=40
   *  Pc=1.000
   *  Pm=.005
   *  G=1000
   */
   Y=X*X*X+2.5*X*X-2*X+1;
   if (Y>0&&Y<=1) return Y;
   if (Y<1)     return 1000d;
   if (Y<10)    return 10000d;
   if (Y<100)   return 100000d;
   if (Y<1000)  return 1000000d;
   if (Y<10000) return 100000000d;
   return 10000000000d;
  }//endF02

  public double F03(double X,double Y){
/*
 * (3)
 *
 *	Maximizar
 *
 *	N=200
 *	E=4
 *	D=40
 *	Pc=1
 *	Pm=.005
 *	G=5000
 *
 *	X=5
 *	Y=3
 *	F(X,Y)=0
 *
 */
 	return -(X-5)*(X-5)-(Y-3)*(Y-3);
  }//endF03

  public double F04(double X,double Y){
/*
 * (4)
 *
 *	Minimizar
 *
 *	Z --> 0
 *	N=200
 *	E=4
 *	D=40
 *	Pc=1
 *	Pm=.005
 *	G=1000
 *
 *	(X,Y) = (1.198231,0.706163); Z=0.00000
 *
 *
 */
	double Z;
	Z=Math.pow(X,2)-2*X*Y+Math.pow(Y,3)+X+Y-2; 
	if (Z<0){
		if (Z>-100)
  			return 100000;
  		else
  			if (Z>-1000)
  				return 1000000;
  			else
  				if (Z>-10000)
  					return 100000000;
  				else
  					return 1000000000;
  				//endif
  			//endif
  		//endif
	}//endif
	return Z;
  }//endF04

  public double F05(double X, double Y){
/*
 * (5)
 *
 */
//  2X+3Y-13=0
//   X-2Y+ 4=0
//  ----------
//  3X+ Y- 9=0
//  X=2; Y=3
/*
 *	USAR:
 *		  2 BITS ENTEROS
 *		 40 BITS DECIMALES
 *		200 INDIVIDUOS
 *		400 GENERACIONES
 *		Pc: 0.9
 *		Pm: 0.02
 */
 	double R1,R2;
	int C=0;
	R1=2*X+3*Y-13;
	R2=X-2*Y+4;
	if (R1>=0) C=C+1;
	if (R2>=0) C=C+1;
	if (C==2) return 3*X+Y-9;
	if (C==1) return 5000000d;
	return 10000000d;
  }//endF05
} //endClass
