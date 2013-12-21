function Write(a){
    //outStream.append(a)
    InputReader.Write(a)
}
function Writeln(a){
    //outStream.append(a+'\n')
    InputReader.Write(a+'\n')
}
function Print(a){
    //outStream.append(a)
    InputReader.Write(a)
}
function Println(a){
    //outStream.append(a+'\n')
    InputReader.Write(a+'\n')
}
function Read(){
    return InputReader.readString("Input")
}
function Read(m){
    return ReadM(m)
}
function ReadM(message){
    if(typeof(message)!="string") message="Input"
    var jstr1=InputReader.readString(message)
    return ""+jstr1
}

function ReadNumber(m){
    if(typeof(m)!="string") m="Input number"
    return InputReader.readNumber(m)
}
function ReadInteger(m){
    if(typeof(m)!="string") m="Input integer"
    return InputReader.readInteger(m)
}
function ReadLogic(m){
    if(typeof(m)!="string") m="Input true/false"
    return InputReader.readLogic(m)
}
function ReadCharArray(m){
    if(typeof(m)!="string") m="Input char array"
    return InputReader.readCharArray(m)
}

function stringToArray(str){
    var len=str.length
    var tmp=new Array(len)
    if(!(len>=0))
        len=str.length
    for(i=0; i<len; i++){
        tmp[i]=str.charCodeAt(i)
    }
    return tmp
}
function arrayToString(arr){
    var str=""
    for(i=0; i<arr.length; i++){
        str+=String.fromCharCode(arr[i])
    }
    return str
}

function ReadCharArray(){
    tmp=InputReader.readString("input string")
    return stringToArray(tmp)
}
function fromCharCode(c){
    String.fromCharCode(c)
}
function charCodeAt(c){
    String.charCodeAt(c)
}
PI=Math.PI
pi=PI
function toRadians(a){return (a/180)*PI}
function toDegrees(a){return (a/PI)*180}
function sin(a){return Math.sin(a)}
function cos(a){return Math.cos(a)}
function tg(a){return Math.tg(a)}
function atan(a){return Math.atan(a)}
function atan2(y,x){return Math.atan2(y,x)}
function abs(a){return Math.abs(a)}
function sqr(a){return Math.sqr(a)}
function sqrt(a){return Math.pow(a, 0.5)}
function pow(a, b){return Math.pow(a, b)}
function root(a, b){return Math.pow(a, 1.0/b)}
function rand(from, to){
    return Math.round(from+(Math.random()*(to-from)))
}
function round(a, p){
    var TMP=Math.round(a/p)
    return TMP*p
}
function log(a){return Math.log(a)}
function log10(a){return Math.log(a)/Math.log(10)}
function log2(a){return Math.log(a)/Math.log(2)}
function logx(a, x){return Math.log(a)/Math.log(x)}
function floor(a){return Math.floor(a)}
function ceil(a){return Math.ceil(a)}

function newArray(n){
    var T=new Array(n)
    return T
}

function toChar(a){
    String.fromCharCode(a)
}
function toInt(a){
    return parseInt(a)
}
function toFloat(a){
    return parseFloat(a)
}
function toLogic(a){
    if(a==true) return true
    if(a!=0) return true
    return false
}
function str(a){
    return ""+a
}

function getValueOf(n){
    if(typeof(n) == "undefined")
        return "undef"
    if(typeof(n) == "object")
        if(typeof(n.length) == "number"){
            var strasdfghjkl="["
            var l=n.length
            if(typeof(l)!="number")
                l=n.length()
            for(asdfghjkl=0; asdfghjkl<l; asdfghjkl++){
                strasdfghjkl+=getValueOf(n[asdfghjkl])
                if(asdfghjkl+1<n.length)
                    strasdfghjkl+=", "
            }
            strasdfghjkl+="]"
            return strasdfghjkl
        }
        else if(typeof(n.length()) == "number"){
            var strasdfghjkl="["
            for(asdfghjkl=0; asdfghjkl<n.length(); asdfghjkl++){
                strasdfghjkl+=getValueOf(n[asdfghjkl])
                if(asdfghjkl+1<n.length)
                    strasdfghjkl+=", "
            }
            strasdfghjkl+="]"
            return strasdfghjkl
        }
    return n
}
