from random import random
import binascii

#def maquinaTuring(estado, cinta):
def maqTuring(estado, cinta, ciclos):
    arreglo = chunkstring(estado, 16)
    dec=[]
    for m in arreglo:
        dec.append([])
        dec[len(dec)-1].append([])
        dec[len(dec)-1][0].append(int(m[0:6],2))
        dec[len(dec)-1][0].append(int(m[6:7]))
        dec[len(dec)-1][0].append('L' if m[7:8]=='0' else 'R')
        dec[len(dec)-1].append([])
        dec[len(dec)-1][1].append(int(m[8:14],2))
        dec[len(dec)-1][1].append(int(m[14:15]))
        dec[len(dec)-1][1].append('L' if m[15:16]=='0' else 'R')

    i = 1024/2 #mitad de la cinta
    estado = 0

    for e in range(ciclos):
          lado = int(cinta[i])
          reemplazo = dec[estado][lado][1]
          mov = dec[estado][lado][2]
          cinta[i] = reemplazo
          if mov == 'L':
            i = i-1
          else:
            i = i+1
          anterior = estado
          estado = dec[estado][lado][0]
          print("Estado {}, cambio {}, movimiento {}, prox estado {}, indice {}".format(anterior, reemplazo, mov, estado, i))
    print(dec)
    print('cinta {}'.format(cinta))

    return cinta

def chunkstring(string, length):
    return list((string[0+i:length+i] for i in range(0, len(string), length)))

### SOPORTE 
# text_to_bits("HOLA")
# '01001000010011110100110001000001'
def text_to_bits(text, encoding='utf-8', errors='surrogatepass'):
    bits = bin(int(binascii.hexlify(text.encode(encoding, errors)), 16))[2:]
    return bits.zfill(8 * ((len(bits) + 7) // 8))

def text_from_bits(bits, encoding='utf-8', errors='surrogatepass'):
    n = int(bits, 2)
    return int2bytes(n).decode(encoding, errors)

def int2bytes(i):
    hex_string = '%x' % i
    n = len(hex_string)
    return binascii.unhexlify(hex_string.zfill(n + (n & 1)))





if __name__ == "__main__":
    estado = ''
    for i in range(1024):
        estado = estado + ('1' if random() > .5 else '0')

    cintaEnBlanco = [0] * 1024
    ciclos = 100

    # "HOLA"

    cintaObjetivo = text_to_bits("HOLA")
    cintaMaquina = maqTuring(estado, cintaEnBlanco, ciclos)
    
    
