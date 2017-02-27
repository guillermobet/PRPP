'''
        Programa auxiliar para generar las corridas del programa principal.
        
'''
from subprocess import check_output
import os
from glob import glob
import re


folders = glob("./Instances/*")


def func(path):
	return check_output("java Project {}".format(path), shell=True).decode()

def natural_sort(l): 
    convert = lambda text: int(text) if text.isdigit() else text.lower() 
    alphanum_key = lambda key: [ convert(c) for c in re.split('([0-9]+)', key) ] 
    return sorted(l, key = alphanum_key)

def extract(stri):
	res = re.search(".*Best overall reward: ([0-9]*).*", stri)
	res2 = re.search(".*Time elapsed: ([0-9]*).*", stri)
	print(res.group(1) + " " + res2.group(1))

def limpiar():
        for f in folders:
                first = glob("{}/*.txt".format(f))
                for archivo in first:
                        os.remove(archivo)      


#limpiar()
                
for f in folders:
	first = glob("{}/*".format(f))
	print("Siguiente carpeta {}".format(f))
	for archivo in natural_sort(first):
		#print(archivo)
		res = func(archivo)
		extract(res)
		
limpiar()
