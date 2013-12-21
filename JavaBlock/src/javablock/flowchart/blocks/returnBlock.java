/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javablock.flowchart.blocks;
import config.global;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import javablock.*;
import javablock.flowchart.Flowchart;
import javablock.flowchart.JBlock;
import javablock.flowchart.connector;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author razi
 */
public class returnBlock extends JBlock {
    public returnBlock(Flowchart parent){
        super(Type.RETURN, parent);
        this.displayComment=true;
        this.comment=global.translate.misc.getString("End");
    }
    @Override
    public boolean isSwitchable() {
        return false;
    }
    @Override
    public boolean isEditable(){
        return true;
    }

    @Override
    public JBlock executeCode(ScriptEngine script){
        if(this.code.length()==0)
            return null;
        else
            try {
                script.eval(getCode());
            } catch (ScriptException ex) {
                Logger.getLogger(returnBlock.class.getName()).log(Level.SEVERE, null, ex);
            }
        return null;
    }

    @Override
    public boolean oneOut(){return false;}
    
    @Override
    public void prepareText(){
        displayComment=true;
        if(comment.length()==0)
            this.comment="End";
        super.prepareText();
    }

    @Override
    public String getScriptFragmentForJavaScript(){
        String code="";
        ///code+="case "+ID+":\n";
        String c=getCode();
        if(c.length()>0)
            if (c.startsWith("return")) {
                code += "\t\t\t" + c + "\n";
            } else {
                code += "\t\t\treturn " + c+ "\n";
            }
        else
            code+="\t\t\treturn 0;\n";
        //code+="break;\n";
        return code;
    }
    @Override
    public String getScriptFragmentForPython(){
        String code="";
        String c[]=getCode().split("\n");
        boolean is=false;
        for(String l: c){
            if(l.length()>0){
                while(l.charAt(0)==' '||l.charAt(0)=='\t')
                    l=l.substring(1);
                if(l.startsWith("return"))
                    code+="\t\t\t"+l+"\n";
                else
                    code+="\t\t\treturn "+l+"\n";
                is=true;
            }
        }
        if(!is)
            code+="\t\t\treturn 0;\n";
        return code;
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

    @Override
    public connector connectTo(JBlock n){
        return null;
    }

    @Override
    protected void specialXmlSave(Element xml){
        //if (code.length()>0) {
        //    Element ex = xml.getOwnerDocument().createElement("extra");
        //    xml.appendChild(ex);
        //}
    }
    @Override
    protected void specialXmlLoad(Element xml, boolean connect){
        //NodeList l=xml.getElementsByTagName("extra");
        //if(l.getLength()>0) extra=true;
        //else code="";
        //extra=true;
    }
}
