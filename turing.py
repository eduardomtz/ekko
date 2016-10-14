
from random import random
 
estado = ''
for i in range(1024):
    estado = estado + ('1' if random()>.5 else '0')
 
def chunkstring(string, length):
    return list((string[0+i:length+i] for i in range(0, len(string), length)))
 
cinta = '0' * 1024
ciclosEjecucion = 1000

#def maquinaTuring(estado, cinta):
arreglo = chunkstring(estado, 16)
dec=[]
for m in arreglo:
    dec.append([])
    dec[len(dec)-1].append([])
    dec[len(dec)-1][0].append(int(m[0:6],2))
    dec[len(dec)-1][0].append(m[6:7])
    dec[len(dec)-1][0].append('L' if m[7:8]=='0' else 'R')
    dec[len(dec)-1].append([])
    dec[len(dec)-1][1].append(int(m[8:14],2))
    dec[len(dec)-1][1].append(m[14:15])
    dec[len(dec)-1][1].append('L' if m[15:16]=='0' else 'R')
    
i = 1024/2 #mitad de la cinta
estado = 0

for e in range(ciclosEjecucion):
  lado = int(cinta[i])
  reemplazo = dec[estado][lado][1]
  mov = dec[estado][lado][2]
  cinta[i] = reemplazo
  if mov == 'L':
    i = i-1
  else:
    i = i+1
  i = dec[estado][lado][0]

