package javablock.flowchart.blocks;
import javablock.flowchart.blockEditors.scriptEditor;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import javablock.*;
import javablock.flowchart.BlockEditor;
import javablock.flowchart.Flowchart;
import javablock.flowchart.JBlock;
import javablock.flowchart.connector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 *
 * @author razi
 */
public class scrBlock extends JBlock {
    static scriptEditor editor=null;
    public scrBlock(Flowchart parent){
        super(Type.SCRIPT, parent);
    }

    public String js="", py="";
    public String name="";

    @Override
    public boolean popUpEditor(){return false;}
    @Override
    public boolean oneOut(){return false;}
    @Override
    public boolean isDefinitionBlock(){
        if(connectsIn.size()>0)
            return false;
        return true;
    }
    public boolean isMethod(){
        if(this.connectsIn.size()==1)
            if(connectsIn.get(0).f.type==Type.STRUCT)
                return true;
        return false;
    }
    @Override
    public BlockEditor getEditor(){
        if(editor==null)
            editor=new scriptEditor();
        return editor;
    }
    @Override
    public boolean canBeConnected(JBlock b){
        return true;
    }

    @Override
    public connector connectTo(JBlock n){
        if(n.type==Type.STRUCT) return null;
        for(connector c: connects)
            c.n.removeConnectFrom(this);
        connects.clear();
        connector c=new connector(this, n);
        flow.historyAdd();
        connects.add(c);
        n.addInConnect(c);
        return c;
    }

    @Override
    public String getCode(){
        if(getManager().scriptEngine.equals("python"))
            return py;
        else
            return js;
        //return super.getCode();
    }
    @Override
    protected void prepareText(){
        comment=name;
        displayComment=true;
        super.prepareText();
    }

    @Override
    public void shape(){
        prepareText();
        Rectangle2D scr=new Rectangle.Double(
            bound.getX()-5,
            bound.getY()-5,
            bound.getWidth()+10,
            bound.getHeight()+10
        );
        Area script=new Area(scr);
        scr=new Rectangle.Double(
            bound.getX()-12,
            bound.getY()-5,
            5,
            bound.getHeight()+10
        );
        script.add(new Area(scr));
        scr=new Rectangle.Double(
            bound.getX()+bound.getWidth()+7.5,
            bound.getY()-5,
            5,
            bound.getHeight()+10
        );
        script.add(new Area(scr));
        shape=script;
        afterShape();
    }

    @Override
    public String getScriptFragmentForJavaScript(){
        if(isDefinitionBlock())
            code=this.js+"\n";
        String c="";
        for(String line:js.split("\n"))
            c+="\t\t\t"+line+"\n";
        return c;
    }
    @Override
    public String getScriptFragmentForPython(){
        if(isDefinitionBlock())
            code=this.py+"\n";
        String c="";
        for(String line:py.split("\n"))
            c+="\t\t\t"+line+"\n";
        return c;
    }


    @Override
    protected void specialXmlSave(Element xml){
        Document doc=xml.getOwnerDocument();
        Element name=doc.createElement("scriptName");
        name.appendChild(doc.createTextNode(this.name));
        xml.appendChild(name);
        if(js.length()>0){
                Element text=doc.createElement("javascript");
                String c=js.replaceAll(";\t", ";\t\t");
                c=c.replaceAll("\n", ";\t");
                Text Content = doc.createTextNode(c);
                text.appendChild(Content);
                xml.appendChild(text);
            }
        if(py.length()>0){
                Element text=doc.createElement("python");
                String c=py.replaceAll(";\t", ";\t\t");
                c=c.replaceAll("\n", ";\t");
                Text Content = doc.createTextNode(c);
                text.appendChild(Content);
                xml.appendChild(text);
            }

    }
    @Override
    protected void specialXmlLoad(Element xml, boolean connect){
        NodeList lines=xml.getElementsByTagName("javascript");
        js="";
        for (int i = 0; i < lines.getLength(); i++) {
            Element content = (Element) lines.item(i);
            String co = content.getTextContent();
            co = co.replaceAll(";\t", "\n");
            co = co.replaceAll(";\t\t", ";\t");
            js += co;
            if (i != lines.getLength() - 1) {
                js += "\n";
            }
        }
        lines=xml.getElementsByTagName("python");
        py="";
        for (int i = 0; i < lines.getLength(); i++) {
            Element content = (Element) lines.item(i);
            String co = content.getTextContent();
            co = co.replaceAll(";\t", "\n");
            co = co.replaceAll(";\t\t", ";\t");
            py += co;
            if (i != lines.getLength() - 1) {
                py += "\n";
            }
        }

        lines=xml.getElementsByTagName("scriptName");
        if(lines.getLength()>0)
            name=lines.item(0).getTextContent();
    }

}


