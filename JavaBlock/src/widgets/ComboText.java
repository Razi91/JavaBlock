package widgets;

import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ComboText{
    String text, value, icon="";
    /**
     *
     * @param text Text that will be displayed, can be translated
     * @param value Value, get by getValue(), should NOT be translated
     */
    public ComboText(String text, String value){
        this.text=text;
        this.value=value;
    }
    public ComboText(String icon, String text, String value){
        this.text=text;
        this.value=value;
        this.icon=icon;
    }

    /**
     * 
     * @return Icon
     */
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
    /**
     *
     * @return returns displayed text <b>without</b> icon
     */
    public String getText(){
        return text;
    }
    /**
     *
     * @return returns value of field
     */
    public String getValue(){
        return value;
    }
    @Override
    public String toString(){
        return icon+" "+text;
    }
}
