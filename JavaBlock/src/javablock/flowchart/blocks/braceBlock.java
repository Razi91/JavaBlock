package javablock.flowchart.blocks;
import config.global;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import javablock.*;
import javablock.flowchart.Flowchart;
import javablock.flowchart.JBlock;
import javablock.flowchart.connector;
import javax.script.ScriptEngine;
import org.w3c.dom.*;

public class braceBlock extends JBlock {
    JBlock next=null;
    boolean open;
    public braceBlock(Flowchart parent){
        super(Type.BRACE, parent);
        font=global.monospacedFont;
        code="{";
        open=true;
    }
    @Override
    public boolean isSwitchable(){return false;}
    @Override
    public void addedToScene(){
        if(flow.loading) return;
        if(linkTo!=null){
            //flow.selected.add(linkTo);
            code=comment="}";
            return ;
        }
        else
        code=comment="{";
        linkTo=new braceBlock(flow);
        linkTo.linkTo=this;
        ((braceBlock)(linkTo)).open=false;
        flow.addBlock(linkTo);
        linkTo.translate(0, 100);
    }
    public void setOpen(boolean set){
        open=set;
        if(open) code=comment="{";
        else code=comment="}";
    }
    public void setLinkTo(JBlock link){
        linkTo=link;
        ((braceBlock)link).linkTo=this;
    }

    @Override
    public boolean moveWhenAdded(){
        return false;
    }
    @Override
    public boolean oneOut(){return false;}
    @Override
    public boolean canBeConnected(JBlock b){
        if(open)
            return b.type==JBlock.Type.DECISION;
        return true;
    }
    @Override
    public connector connectTo(JBlock n){
        connector c=null;
        if(true){
            for(connector co: connects)
                co.n.removeConnectFrom(this);
            connects.clear();
            c=new connector(this, n);
            flow.historyAdd();
            connects.add(c);
            n.addInConnect(c);
        }
        return c;
    }

    @Override
    public void delete(){
        super.delete();
        if(linkTo!=null){
            linkTo.linkTo=null;
            linkTo.delete();
        }
    }

    @Override
    public void shape(){
        prepareText();
        //shape=new Ellipse2D.Double(-5,-5,10,10);
        shape=new Ellipse2D.Double(
                bound.getX()-5,
                bound.getY()-5,
                bound.getWidth()+10,
                bound.getHeight()+10
                );
        afterShape();
    }
    @Override
    public void draw(Graphics2D g2d){
        if(ID<linkTo.ID){
            g2d.setStroke(global.strokeSelection);
            Line2D l=new Line2D.Double(posX, posY, linkTo.posX, linkTo.posY);
            g2d.setColor(borderColor);
            g2d.draw(l);
            g2d.setStroke(global.strokeNormal);
        }
        super.draw(g2d);
    }

    @Override
    public void prepareToExe(){
        nowExecute=true;
        makeGradient();
        linkTo.nowExecute=true;
        ((braceBlock)(linkTo)).makeGradient();
    }

    @Override
    public JBlock executeCode(ScriptEngine script){
        next=null;
        if(!open) {
            if(this.connects.size()==1)
                next=connects.get(0).n;
            else if(linkTo.connectsIn.size() == 1)
                next=linkTo.connectsIn.get(0).f;
        }
        else {
            if(this.connects.size()==1)
                next=connects.get(0).n;
        }
        return next;
    }

    @Override
    public String getScriptFragmentForJavaScript(){
        String code="";
        //code+="case "+ID+":\n";
        JBlock n=executeCode(null);
        if(n!=null)
            code+="\t\t\t"+flow.getName()+"_block="+n.nextExe().ID+"\n";
        else
            code+="\t\t\treturn 0;\n";
        code+="\t\t\tbreak;\n";
        return code;
    }
    @Override
    public String getScriptFragmentForPython(){
        String code="";
        JBlock n=executeCode(null);
        if(n!=null)
            code+="\t\t\t"+flow.getName()+"_block="+n.nextExe().ID+"\n";
        else
            code+="\t\t\treturn 0;\n";
        return code;
    }

    @Override
    public JBlock nextExe(){
        JBlock n=null;
        if(open){
            if(global.highlightLinks)
                return this;
            else if(connects.size() == 1)
                n=connects.get(0).n.nextExe();
        }
        else{
            if(global.highlightLinks)
                return this;
            else{
                if(connects.size() == 1)
                    n=connects.get(0).n.nextExe();
                else{
                    if(linkTo.connectsIn.size()==1)
                        return linkTo.connectsIn.get(0).f;
                }
            }
        }
        return n;
    }

    @Override
    public JBlock execute(ScriptEngine script, boolean highlight){
        JBlock r=executeCode(script);
        if(highlight && r!=null){
            r.prepareToExe();
            flow.update();
        }
        releaseFromExe();
        linkTo.releaseFromExe();
        return r;
    }

    @Override
    public void setColor(Color c){
        color=c;
        linkTo.color=c;
        ((braceBlock)(linkTo)).makeGradient();
        makeGradient();
    }

    @Override
    protected void specialXmlSave(Element xml){
        if(linkTo==null) return ;
        Element link=xml.getOwnerDocument().createElement("link");
        link.setAttribute("ID", linkTo.ID+"");
        link.setAttribute("open", open+"");
        xml.appendChild(link);
    }
    @Override
    protected void specialXmlLoad(Element xml, boolean connect){
        if(connect){
            NodeList lines=xml.getElementsByTagName("link");
            Element l=(Element)lines.item(0);
            linkTo=(braceBlock) flow.getBlockById(
                    Integer.parseInt(l.getAttribute("ID")));
            open=Boolean.parseBoolean(l.getAttribute("open"));

            linkTo.linkTo=this;
        }
    }
}
