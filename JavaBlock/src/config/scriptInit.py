#!/usr/bin/python

from math import *
import random
def Write(a):
    InputReader.Write(str(a))
def Writeln(a):
    InputReader.Write(a+'\n')

def Print(a):
    InputReader.Write(str(a))
def Println(a):
    InputReader.Write(a+'\n')
def Read(str="Input string:"):
    if len(str)==0: str="Input string:"
    return InputReader.readString(str)
def ReadM(str="Input string"):
    if len(str)==0: str="Input string:"
    return InputReader.readString(str)
def ReadNumber(str="Input number"):
    if len(str)==0: str="Input number"
    return float(InputReader.readNumber(str))
def ReadInteger(str="Input integer:"):
    if len(str)==0: str="Input integer:"
    return int(InputReader.readNumber(str))
def ReadLogic(str="Input Logic:"):
    if len(str)==0: str="Input Logic:"
    return bool(InputReader.readLogic(str))
def ReadCharArray(str="Input char array:"):
    if len(str)==0: str="Input char array:"
    s=Read(str)
    t=[]
    for C in s:
        t.append(ord(C))
    return t

def rand(f, t):
    return random.randint(f,t)

def round(a):
    if a-floor(a)>=0.5:
        return ceil(a)
    return floor(a)

def round(a, p=1):
    a/=p;
    if abs(a-floor(a))>=0.5:
      a=ceil(a)
    else:
      a=floor(a)
    return a*p

PI=pi
def toRadians(a):
    return (a/180)*PI
def toDegrees(a):
    return (a/PI)*180

def getValueOf(a):
    if a.__class__=="".__class__:
        return a
    if a.__class__==True.__class__:
        if(a==True): return str('T')
        else: return str('F')
    if a.__class__==[].__class__:
        Sret123="[";
        for i in range(len(a)):
            Sret123+=getValueOf(a[i])
            if i<len(a)-1: Sret123+=","
        Sret123+="]"
        return Sret123
    return str(a)

def Array(n):
    TAB=[]
    while n>0:
        TAB.append(0);
        n-=1
    return TAB
def stringToArray(s):
    T=[]
    for i in range(len(s)):
        T.append=ord(s[i])
    return T
def arrayToString(s):
    T=""
    for i in s:
        T+=chr(i)
    return T
def toChar(x):
    return char(x)
def newArray(n):
    TAB=[]
    while n>0:
        TAB.append(0);
        n-=1
    return TAB


def toInt(a):
    return int(a)
def toFloat(a):
    return float(a)
def toLogic(a):
    if a==True: return True
    if a=="true": return True
    if a!=0: return True
    return False;

true=True
false=False