package javablock.flowchart;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javablock.flowchart.*;
import config.*;
import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javablock.FlowchartManager;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 * This class contains all in-canvas popup menus
 * @author razi
 */
public class popupMenus implements ActionListener, PopupMenuListener {
    public JPopupMenu blockMenu, selectedMenu, nullMenu, codeEditor,
            otherBlocksMenu;
    FlowchartManager action;
    public popupMenus(FlowchartManager action){
        this.action=action;
        initNull();
        initBlock();
        initSelected();
        initOthers();
    }
    
    private JMenuItem newMenuItem(String name, String icon, String tooltip, String action){
        JMenuItem m=new JMenuItem(translator.get(name));
        if(icon!=null)
            if(!icon.equals(""))
                m.setIcon(new javax.swing.ImageIcon(getClass().
                    getResource("/icons/blocks/"+icon+".png")));
        if(tooltip!=null)
            m.setToolTipText(translator.tooltips.getString(tooltip));
        m.setActionCommand(action);
        m.addActionListener(this);
        return m;
    }
    
    private JMenuItem newMenuItem(String name, BufferedImage icon, String tooltip, String action){
        JMenuItem m=new JMenuItem(translator.get(name));
        if(icon!=null)
            m.setIcon(new ImageIcon((Image)icon));
        if(tooltip!=null)
            if(translator.tooltips.containsKey(tooltip))
                m.setToolTipText(translator.tooltips.getString(tooltip));
        m.setActionCommand(action);
        m.addActionListener(this);
        return m;
    }
    
    
    private JCheckBox codeBased;
    public void initNull(){
        JMenuItem item;
        
        nullMenu=new JPopupMenu();
        nullMenu.add(new JLabel(translator.get("block.insert")));
        nullMenu.addSeparator();

        item=newMenuItem("block.IO", JBlock.getIcon(JBlock.Type.IO), "io", "add/IO");
        nullMenu.add(item);

        item=newMenuItem("block.CPU", JBlock.getIcon(JBlock.Type.CPU), "cpu", "add/CPU");
        nullMenu.add(item);

        item=newMenuItem("block.DECISION", JBlock.getIcon(JBlock.Type.DECISION), "decision", "add/DECISION");
        nullMenu.add(item);

        item=newMenuItem("block.SCRIPT", JBlock.getIcon(JBlock.Type.SCRIPT), "script", "add/SCRIPT");
        nullMenu.add(item);
        item=newMenuItem("block.STRUCT", JBlock.getIcon(JBlock.Type.STRUCT), "struct", "add/STRUCT");
        nullMenu.add(item);

        item=newMenuItem("block.RETURN", JBlock.getIcon(JBlock.Type.RETURN), "return", "add/RETURN");
        nullMenu.add(item);

        item=newMenuItem("block.COMMENT", JBlock.getIcon(JBlock.Type.COMMENT), "comment", "add/COMMENT");
        nullMenu.add(item);
        nullMenu.addSeparator();

        item=newMenuItem("block.LINK", JBlock.getIcon(JBlock.Type.LINK), "link", "add/LINK");
        nullMenu.add(item);

        item=newMenuItem("block.CLIP", JBlock.getIcon(JBlock.Type.BRACE), "clips", "add/CLIP");
        nullMenu.add(item);

        item=newMenuItem("block.JUMP", JBlock.getIcon(JBlock.Type.JUMP), "jump", "add/JUMP");
        nullMenu.add(item);

        nullMenu.addSeparator();
        if(true){
            JMenu addons=new JMenu("Addons");
            item=newMenuItem("Logo", JBlock.getIcon(JBlock.Type.LOGO), "logo", "add/LOGO");
            addons.add(item);
            item=newMenuItem("Canvas2D", JBlock.getIcon(JBlock.Type.CANVAS2D), "canvas2d", "add/CANVAS2D");
            addons.add(item);
            addons.addSeparator();
            for (Class<JBlock> c : JBlock.getCustomTypes()) {
                try {
                    String typeName=(String) c.getMethod("getName").invoke(null);
                    item=new JMenuItem(typeName);
                    item.addActionListener(this);
                    item.setActionCommand("add/"+typeName);
                    item.setIcon(
                            new ImageIcon((BufferedImage)c.getMethod("getIcon").invoke(null)));
                    addons.add(item);
                } catch (Exception ex) {
                    Logger.getLogger(popupMenus.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }
            
            nullMenu.add(addons);
        }

        nullMenu.addSeparator();
        codeBased=new JCheckBox(translator.bundle.getString("block.codebased"));
        codeBased.setSelected(true);
        nullMenu.add(codeBased);
    }
    
    public void initOthers(){
        JMenuItem item;
        otherBlocksMenu=new JPopupMenu();
        otherBlocksMenu.add(new JLabel(translator.get("block.insert")));
        otherBlocksMenu.addSeparator();

        item=newMenuItem("block.IO", JBlock.getIcon(JBlock.Type.IO), "io", "add/IO");
        otherBlocksMenu.add(item);

        for (JBlock.Type T : JBlock.HelpingTypes) {
            item = newMenuItem(T.toString(),
                    JBlock.getIcon(T), T.toString().toLowerCase(), "add/" + T.toString());
            //b.setToolTipText(T.toString());
            item.setToolTipText(translator.tooltips.getString(T.toString() + ".help"));
            item.setActionCommand("add/" + T.toString());
            item.setIcon(new javax.swing.ImageIcon(JBlock.getIcon(T)));
            otherBlocksMenu.add(item);
        }
    }
    
    JTextArea Code=new JTextArea();
    JBlock selected=null;
    boolean onBlock=false;
    public void showPopupOnBlock(Component c, int x, int y, JBlock b){
        blockMenu.show(c, x, y);
        if(b.popUpEditor()){
            Code.setVisible(true);
            Code.setText(b.code);
        }
        else
            Code.setVisible(false);
        blockMenu.revalidate();
        selected=b;
        onBlock=true;
    }
    public void hidePopupOnBlock(){
        if(selected==null)
            return ;
        selected.setCode(Code.getText());
        selected.getEditor().setEditedBlock(selected);
        selected.shape();
        selected.flow.update();
        onBlock=false;
    }
    public void initBlock(){
        JMenuItem item;
        Code.setColumns(20);
        Code.setRows(4);
        
        blockMenu=new JPopupMenu();
        blockMenu.add(Code);
        blockMenu.addSeparator();
        blockMenu.addPopupMenuListener(this);

        item=newMenuItem("command.moveUp", "", null, "move/up");
        blockMenu.add(item);
        item=newMenuItem("command.moveDown", "", null, "move/down");
        blockMenu.add(item);

        blockMenu.addSeparator();
        item=newMenuItem("connections.deleteOut", "", null, "connections/deleteOut");
        blockMenu.add(item);
        item=newMenuItem("connections.deleteIn", "", null, "connections/deleteIn");
        blockMenu.add(item);
        item=newMenuItem("connections.deleteAll", "", null, "connections/deleteAll");
        blockMenu.add(item);


        blockMenu.addSeparator();

        item=new JMenuItem("Delete Block");
        item.setActionCommand("delete/all");
        item.addActionListener(this);
        blockMenu.add(item);
    }
    public void initSelected(){
        JMenuItem item;
        selectedMenu=new JPopupMenu();
        item=newMenuItem("command.moveUp", "", null, "move/up");
        selectedMenu.add(item);
        item=newMenuItem("command.moveDown", "", null, "move/down");
        selectedMenu.add(item);
        selectedMenu.addSeparator();
        item=newMenuItem("connections.deleteAll", "", null, "connections/deleteAll");
        selectedMenu.add(item);
        item=new JMenuItem("Delete selected blocks");
        item.setActionCommand("delete/all");
        item.addActionListener(this);
        selectedMenu.add(item);
        selectedMenu.addSeparator();
        
        item=newMenuItem("command.group", "", null, "add/GROUP");
        selectedMenu.add(item);
        selectedMenu.addSeparator();

        item=new JMenuItem("Align vertical");
        item.setActionCommand("align/ver");
        item.addActionListener(this);
        selectedMenu.add(item);
        item=new JMenuItem("Align horisontal");
        item.setActionCommand("align/hor");
        item.addActionListener(this);
        selectedMenu.add(item);

    }

    public void actionPerformed(ActionEvent e) {
        String[] ex=e.getActionCommand().split("/");
        if(ex[0].equals("showOthers")){
            otherBlocksMenu.show((Component)e.getSource(), 0, 0);
        }
        else if(ex[0].equals("add")){
            if(codeBased.isSelected())
                ((Flowchart)action.flow).addBlock(ex[1]);
            else
                ((Flowchart)action.flow).addNonCodeBlock(ex[1]);
        }
        else if(ex[0].equals("align"))
            ((Flowchart)action.flow).align(ex[1]);
        else if(ex[0].equals("delete")){
            if(ex[1].equals("all"))
                ((Flowchart)action.flow).deleteSelectedBlocks();}
        else if(ex[0].equals("connections"))
                ((Flowchart)action.flow).deleteConnections(ex[1]);
        else action.actionPerformed(e);
    }

    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
    }

    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        if(e.getSource()==blockMenu){
            hidePopupOnBlock();
        }
    }

    public void popupMenuCanceled(PopupMenuEvent e) {
    }
}
