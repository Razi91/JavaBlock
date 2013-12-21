/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javablock.flowchart.blocks;
import javablock.flowchart.blockEditors.startEditor;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javablock.*;
import javablock.flowchart.BlockEditor;
import javablock.flowchart.Flowchart;
import javablock.flowchart.JBlock;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author razi
 */
public class startBlock extends JBlock {
    public static startEditor editor=new startEditor();
    public startBlock(Flowchart parent){
        super(Type.START, parent);
        //displayComment=true;
        comment="Start";
    }
    @Override
    public boolean isSwitchable(){return false;}
    public String name="Start";
    public String arguments="";
    public boolean silent=true;
    public boolean displayName=false;


    @Override
    public boolean isEditable(){
        return true;
    }
    @Override
    public BlockEditor getEditor(){
        return editor;
    }
    @Override
    public boolean canBeConnected(JBlock b){
        return false;
    }
    @Override
    public boolean popUpEditor(){return false;}

    @Override
    public JBlock executeCode(ScriptEngine script){
        String args[]=arguments.split("[\\n]");
        String code="InputReader.reset()\n";
        //for(int i=0; i<args.length; i++){
        //    if(args[i].length()==0) break;
        //    code+="\t"+args[i]+"=InputReader.readArgument(\""+args[i]+": \")\n";
        //}
        for(Field field:fields){
            if(field.name.length()==0) break;
            switch(field.type){
                case NUMBER: code+=""+field.name+"=toFloat(InputReader.readArgument(\""+field.name+": \"))\n";
                    break;
                case INTEGER: code+=""+field.name+"=toInt(InputReader.readArgument(\""+field.name+": \"))\n";
                    break;
                case STRING: code+=""+field.name+"=str(InputReader.readArgument(\""+field.name+": \"))\n";
                    break;
                case LOGIC: code+=""+field.name+"=toLogic(InputReader.readArgument(\""+field.name+": \"))\n";
                    break;
                case CHARARRAY: code+=""+field.name+"=toCharArray(InputReader.readArgument(\""+field.name+": \"))\n";
                    break;
                default: code+=""+field.name+"=(InputReader.readArgument(\""+field.name+": \"))\n";
            }

        }
        try {
            script.eval(code);
        } catch (ScriptException ex) {
            Logger.getLogger(startBlock.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(connects.size()==1)
            return connects.get(0).n.nextExe();
        return null;
    }

    public List<Field> fields=new ArrayList();
    public void clear(){
        fields.clear();
    }
    public void addField(String name, String type){
        Field f=new Field();
        f.setName(name);
        f.setType(type);
        fields.add(f);
    }
    public String[][] getFields(){
        String f[][]=new String[fields.size()][2];
        int i=0;
        for(Field field:fields){
            f[i][0]=field.name;
            f[i][1]=field.type.toString();
            i++;
        }
        return f;
    }


    @Override
    public String getScriptFragmentForJavaScript(){
        String code="";
        //code+="\tcase "+ID+":";
        code+="\t\t\tInputReader.reset()\n";
        for(Field field:fields){
            code+="\t\t\tvar "+field.name+"=";
            switch(field.type){
                case NUMBER: code+="\t\t\ttoFloat(InputReader.readArgument(\""+field.name+": \"))\n";
                    break;
                case INTEGER: code+="\t\t\ttoInt(InputReader.readArgument(\""+field.name+": \"))\n";
                    break;
                case STRING: code+="\t\t\tstr(InputReader.readArgument(\""+field.name+": \"))\n";
                    break;
                case LOGIC: code+="\t\t\ttoLogic(InputReader.readArgument(\""+field.name+": \"))\n";
                    break;
                case CHARARRAY: code+="\t\t\ttoCharArray(InputReader.readArgument(\""+field.name+": \"))\n";
                    break;
                default: code+="\t\t\tInputReader.readArgument(\""+field.name+": \")\n";
            }
        }

        if(connects.size()==1)
            code+="\t\t\t"+flow.getName()+"_block="+connects.get(0).n.ID+"\n";
        else
            code+="\t\t\treturn 0";
        code+="\t\t\tbreak\n";
        return code;
    }
    @Override
    public String getScriptFragmentForPython(){
        String code="";
        code+="\t\t\tInputReader.reset()\n";
        for(Field field:fields){
            code+="\t\t\t"+field.name+"=";
            switch(field.type){
                case NUMBER: code+="toFloat(InputReader.readArgument(\""+field.name+": \"))\n";
                    break;
                case INTEGER: code+="toInt(InputReader.readArgument(\""+field.name+": \"))\n";
                    break;
                case STRING: code+="str(InputReader.readArgument(\""+field.name+": \"))\n";
                    break;
                case LOGIC: code+="toLogic(InputReader.readArgument(\""+field.name+": \"))\n";
                    break;
                case CHARARRAY: code+="toCharArray(InputReader.readArgument(\""+field.name+": \"))\n";
                    break;
                default: code+=" InputReader.readArgument(\""+field.name+": \")\n";
            }
        }
        if(connects.size()==1)
            code+="\t\t\t"+flow.getName()+"_block="+connects.get(0).n.nextExe().ID+"\n";
        else
            code+="\t\t\treturn 0\n";
        return code;
    }
    public String generateIntro(boolean var){
        String code="";
        int arg=0;
        for(Field field:fields){
            if(var)
                code+="\tvar "+field.name+"=";
            else
                code+="\t"+field.name+"=";
            switch(field.type){
                case NUMBER: code+="toFloat(arg"+arg+")\n";
                    break;
                case INTEGER: code+="toInt(arg"+arg+")\n";
                    break;
                case STRING: code+="str(arg"+arg+")\n";
                    break;
                case LOGIC: code+="toLogic(arg"+arg+")\n";
                    break;
                case CHARARRAY: code+="toCharArray(arg"+arg+")\n";
                    break;
                default: code+=" arg"+arg+"\n";
            }
            arg++;
        }
        return code;
    }

    /*    @Override
    public String makeScript(){
        //************************  DEFAULT
        String s="function "+flow.getName()+"_id"+ID+"(){\n";
        String args[]=arguments.split("[\\n\\s]");
        int i;
        if(args[0].length()>0){
            for(String arg:args){
                s+=arg+"=Read(\"Input argument: "+arg+":\")\n";
            }
        }
        s+="var EXIT="+flow.getName()+"_id"+connects.get(0).n.nextExe().ID+"();\n";
        s+="  return EXIT";
        s+="\n}\n\n";



        //**********************************      Funkcja dla Java
        s+="function "+flow.getName()+"_run(";
        if(args[0].length()>0){
            for(i=0; i<args.length; i++){
                if(i>0) s+=",";
                s+="arg"+i;
            }
        }
        s+= "){\n";
        i=0;
        if(args[0].length()>0){
            for(String arg:args){
                s+="  "+arg+"= arg"+i+";\n";
                i++;
            }
        }
        s+="  return "+flow.getName()+"_id"+connects.get(0).n.nextExe().ID+"();";
        s+="\n}\n\n";

        return s;
    }*/

    private String displayText(){
        String d;
        if(displayName)
            d=name;
        else
            d=flow.getName();
        String args[]=flow.getArgumentsList();
        if(args!=null)
        if(args.length>=1)
        if(args[0].length()>0){
            d+="(";
            for(int i=0; i<args.length; i++){
                d+=args[i];
                if(i<args.length-1)
                    d+=", ";
            }
            d+=")";
        }
        return d;
    }

    @Override
    public void prepareText(){
        displayComment=true;
        comment=displayText();
        super.prepareText();
    }
    @Override
    public void shape(){
        prepareText();
        Ellipse2D st1=new Ellipse2D.Double(
            bound.getX()-10,
            bound.getY()-10,
            40,
            bound.getHeight()+20
        );
        Ellipse2D st2=new Ellipse2D.Double(
            bound.getX() + bound.getWidth()-30,
            bound.getY()-10,
            40,
            bound.getHeight()+20
        );
        Rectangle2D r=new Rectangle2D.Double(
                bound.getX()+10,
                bound.getY()-10,
                bound.getWidth()-20,
                bound.getHeight()+20
                );
        Area s=new Area(st1);
        s.add(new Area(st2));
        s.add(new Area(r));
        shape=s;
        afterShape();
    }

    public static String capitalize(String s) {
        if (s.length() == 0) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
    
    @Override
    protected void specialXmlSave(Element xml){
        xml.setAttribute("name", name);
        for(Field f:fields){
            Element field=xml.getOwnerDocument().createElement("argument");
            field.setAttribute("name", f.name);
            field.setAttribute("dataType", f.type.toString());
            xml.appendChild(field);
        }
        if(displayName)
            xml.setAttribute("displayName", displayName+"");
    }
    @Override
    protected void specialXmlLoad(Element xml, boolean connect){
        if(connect) return ;
        if(xml.hasAttribute("name"))
            name=xml.getAttribute("name");
        NodeList args=xml.getElementsByTagName("argument");
        fields.clear();
        for(int i=0; i<args.getLength(); i++){
            Element arg=(Element)args.item(i);
            Field f=new Field();
            if (arg.hasAttribute("name"))
                f.setName(arg.getAttribute("name"));
            else{
                f.setName(arg.getTextContent());
            }
            if(arg.hasAttribute("dataType"))
                f.setType(arg.getAttribute("dataType"));
            else
                f.setType("ANY");
            if(f.name.isEmpty()) continue;
            fields.add(f);
        }
        if(xml.hasAttribute("displayName"))
                displayName=Boolean.parseBoolean(xml.getAttribute("displayName"));
    }

    class Field{
        DataType type=DataType.ANY;
        String name;
        Field(){
            type=DataType.INTEGER;
            name="";
        }
        public void setType(String t){
            try{
                type= DataType.valueOf(t);
            } catch(Exception e){
                type=DataType.ANY;
            }
        }
        public void setName(String n){
            name=n;
        }
    }
    public enum DataType{
        NUMBER, INTEGER, STRING, CHARARRAY, LOGIC, ANY
    }
}

