/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javablock.flowchart.blocks;

import javablock.flowchart.blockEditors.structEditor;
import config.global;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import javablock.*;
import javablock.flowchart.BlockEditor;
import javablock.flowchart.Flowchart;
import javablock.flowchart.JBlock;
import javablock.flowchart.connector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author razi
 */
public class structBlock extends JBlock {
    static structEditor editor=new structEditor();
    public structBlock(Flowchart parent){
        super(Type.STRUCT, parent);
    }
    String html="";
    @Override
    public boolean isSwitchable(){return false;}
    @Override
    public boolean popUpEditor(){return false;}
    @Override
    public boolean oneOut(){return false;}
    @Override
    public boolean isDefinitionBlock(){return true;}
    @Override
    public BlockEditor getEditor(){
        return editor;
    }
    @Override
    public boolean canBeConnected(JBlock b){
        return false;
    }
    
    @Override
    public connector connectTo(JBlock n){
        if(n.type!=Type.START) return null;
        for(connector c: connects)
            c.n.removeConnectFrom(this);
        connects.clear();
        connector c=new connector(this, n);
        flow.historyAdd();
        connects.add(c);
        n.addInConnect(c);
        return c;
    }

    List<Field> fields=new ArrayList();
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

    public static String capitalize(String s) {
        if (s.length() == 0) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    @Override
    protected void prepareText(){
        displayComment=true;
        commentIsHTML=true;
        if(global.useJLabels){
            comment="<table cellpadding=\"0\" cellspacing=\"0\">"
                    + "<tr><th colspan=\"2\" align=\"left\">"
                    + "<font size=\"5\">"+name+"</font></th></tr>";
            String fi[][]=getFields();
            for(String f[]:fi){
                comment+="<tr><td align=\"right\"><i>"+f[0]+"</i> :</td><td>"+capitalize(f[1])+"</td></tr>";
            }
            comment+="</table>";
        }
        else{
            comment=""+name+":\n";
            String fi[][]=getFields();
            for(String f[]:fi){
                comment+=" "+f[0]+" :"+capitalize(f[1])+"\n";
            }
        }
        super.prepareText();
    }

    public String name="";
    public boolean genInterface=true;
    @Override
    public String getScriptFragmentForJavaScript(){
        if(fields.isEmpty()) return "";
        if(name.length()==0) return "";
        String code="function "+name+"(){\n";
        code+="\treturn {\n";
        int i=0;
        for(Field field:fields){
            switch(field.type){
                case INTEGER:   code+="\t\t"+field.name+" : 0,\n";
                    break;
                case NUMBER:    code+="\t\t"+field.name+" : 0.0,\n";
                    break;
                case STRING:    code+="\t\t"+field.name+" : \"\",\n";
                    break;
                case CHARARRAY: code+="\t\t"+field.name+" : new Array(1),\n";
                    break;
                case LOGIC:     code+="\t\t"+field.name+" : True,\n";
                    break;
                case ANY:       code+="\t\t"+field.name+" : 0,\n";
                    break;
            }
        }
        if(genInterface)
        for(Field field:fields){
            switch(field.type){
                case INTEGER:   code+="\t\tset"+capitalize(field.name)+" : "
                        + "function(val){\n\t\t\tthis."+field.name+"= parseInt(val)\n\t\t},\n"
                        +"\t\tget"+capitalize(field.name)+" : "
                        + "function(){\n\t\t\treturn parseInt(this."+field.name+")},\n";
                    break;
                case NUMBER:    code+="\t\tset"+capitalize(field.name)+" : "
                        + "function(val){\n\t\t\tthis."+field.name+"= parseFloat(val)\n\t\t},\n"
                        +"\t\tget"+capitalize(field.name)+" : "
                        + "function(){\n\t\t\treturn parseInt(this."+field.name+")},\n";
                    break;
                case STRING:    code+="\t\tset"+capitalize(field.name)+" : "
                        + "function(val){\n\t\t\tthis."+field.name+"= \"\"+val\n\t\t},\n"
                        +"\t\tget"+capitalize(field.name)+" : "
                        + "function(){\n\t\t\treturn \"\"+this."+field.name+"},\n";
                    break;
                case CHARARRAY: code+="\t\tset"+capitalize(field.name)+" : "
                        + "function(val){\n\t\t\tthis."+field.name+"= \"\"+val\n\t\t},\n"
                        +"\t\tget"+capitalize(field.name)+" : "
                        + "function(){\n\t\t\treturn \"\"+this."+field.name+"},\n";
                    break;
                case LOGIC:     code+="\t\tset"+capitalize(field.name)+" : "
                        + "function(val){\n\t\t\tthis."+field.name+"= Boolean(val)\n\t\t},\n"
                        +"\t\tget"+capitalize(field.name)+" : "
                        + "function(){\n\t\t\treturn Boolean(this."+field.name+")},\n";
                    break;
                case ANY:       code+="\t\tset"+capitalize(field.name)+" : "
                        + "function(val){\n\t\t\tthis."+field.name+"= val},\n"
                        +"\t\tget"+capitalize(field.name)+" : "
                        + "function(){\n\t\t\treturn this."+field.name+"},\n";
                    break;
            }
        }
        code+="\t\tclassName: function(){return \""+name+"\"}\n";
        code+="\t}\n}\n";
        return code;
    }
    @Override
    public String getScriptFragmentForPython(){
        if(fields.isEmpty()) return "";
        if(name.length()==0) return "";
        String code="class "+name+":\n";
        code+="\tdef __init__(self):\n";
        for(Field field:fields){
            switch(field.type){
                case INTEGER:   code+="\t\tself."+field.name+"=0\n";
                    break;
                case NUMBER:    code+="\t\tself."+field.name+"=0.0\n";
                    break;
                case STRING:    code+="\t\tself."+field.name+"=\"\"\n";
                    break;
                case CHARARRAY: code+="\t\tself."+field.name+"=[]\n";
                    break;
                case LOGIC:     code+="\t\tself."+field.name+"=True\n";
                    break;
                case ANY:       code+="\t\tself."+field.name+"=0\n";
                    break;
            }
        }
        if(genInterface)
        for(Field field:fields){
            switch(field.type){
                case INTEGER:
                    code+="\tdef set"+capitalize(field.name)+"(self, val): "+
                          "\n\t\tself."+field.name+"=int(val)\n" +
                          "\tdef get"+capitalize(field.name)+"(self): "+
                          "\n\t\treturn int(self."+field.name+")\n";
                    break;
                case NUMBER:
                    code+="\tdef set"+capitalize(field.name)+"(self, val): "+
                          "\n\t\tself."+field.name+"=float(val)\n" +
                          "\tdef get"+capitalize(field.name)+"(self): "+
                          "\n\t\treturn float(self."+field.name+")\n";
                    break;
                case STRING:
                    code+="\tdef set"+capitalize(field.name)+"(self, val): "+
                          "\n\t\tself."+field.name+"=str(val)\n" +
                          "\tdef get"+capitalize(field.name)+"(self): "+
                          "\n\t\treturn str(self."+field.name+")\n";
                    break;
                case CHARARRAY:
                    code+="\tdef set"+capitalize(field.name)+"(self, val): "+
                          "\n\t\tself."+field.name+"=str(val)\n" +
                          "\tdef get"+capitalize(field.name)+"(self): "+
                          "\n\t\treturn (self."+field.name+")\n";
                    break;
                case LOGIC:
                    code+="\tdef set"+capitalize(field.name)+"(self, val): "+
                          "\n\t\tself."+field.name+"=bool(val)\n" +
                          "\tdef get"+capitalize(field.name)+"(self): "+
                          "\n\t\treturn bool(self."+field.name+")\n";
                    break;
                case ANY:
                    code+="\tdef set"+capitalize(field.name)+"(self, val): "+
                          "\n\t\tself."+field.name+"val\n" +
                          "\tdef get"+capitalize(field.name)+"(self): "+
                          "\n\t\treturn self."+field.name+"\n";
                    break;
            }
        }
        code+="\tdef className():\n\t\treturn \""+name+"\"\n";
        code+="\n";
        return code;
    }
    @Override
    public void shape(){
        prepareText();
        GeneralPath path=new GeneralPath();
        path.moveTo(bound.getX()-10, bound.getY()-10);

        path.lineTo(bound.getX()+bound.getWidth()+10, bound.getY()-10);
        path.lineTo(bound.getX()+bound.getWidth()+20, bound.getY());

        path.lineTo(bound.getX()+bound.getWidth()+20, bound.getY()+bound.getHeight());
        path.lineTo(bound.getX()+bound.getWidth()+10, bound.getY()+bound.getHeight()+10);

        path.lineTo(bound.getX()-10, bound.getY()+bound.getHeight()+10);
        path.lineTo(bound.getX()-20, bound.getY()+bound.getHeight());

        path.lineTo(bound.getX()-20, bound.getY());
        path.lineTo(bound.getX()-10, bound.getY()-10);
        shape=path;
        afterShape();
    }



    @Override
    protected void specialXmlSave(Element xml){
        Document doc=xml.getOwnerDocument();
        xml.setAttribute("name", name);
        if(genInterface)
            xml.setAttribute("generateInterface", "true");
        for(String f[]:getFields()){
            Element e=doc.createElement("field");
            e.setAttribute("name", f[0]);
            e.setAttribute("type", f[1]);
            xml.appendChild(e);
        }
    }
    @Override
    protected void specialXmlLoad(Element xml, boolean connect){
        if(!connect) return ;
        name=xml.getAttribute("name");
        if(xml.hasAttribute("generateInterface"))
            genInterface=xml.getAttribute("generateInterface").equals("true");
        NodeList l=xml.getElementsByTagName("field");
        for(int i=0; i<l.getLength(); i++){
            Element f=(Element)l.item(i);
            Field field=new Field();
            field.name=f.getAttribute("name");
            field.setType(f.getAttribute("type"));
            fields.add(field);
        }
    }


    class Field{
        DataType type;
        String name;
        Field(){
            type=DataType.INTEGER;
            name="";
        }
        public void setType(String t){
            try{
                type= DataType.valueOf(t);
            } catch(Exception e){
                type=DataType.INTEGER;
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
