package scirpts;

import javablock.flowchart.blocks.*;
import javablock.flowchart.JBlock;

public class python implements langGenerator {

    public String signature(startBlock block) {
        return "";
    }

    public String signature_null(startBlock block) {
        return "";
    }

    public String normal(JBlock block) {
        String c="\t\tif _block=="+block.ID+":\n";
        return c;
    }

    public String decision(decBlock block) {
        return "";
    }

    public String end(JBlock block) {
        return "";
    }

    public String structure(structBlock block) {
        return "";
    }

    public String parseOnly(String code) {
        //return code.replaceAll("  \n", ";  ").replaceAll("\n[\\s]*", "\n\t\t\t");
        String lines[]=code.replaceAll("\n[\\s\t]", " ")
                //.replaceAll("\n[\\s\t]", " ")
                .split("\n");
        String c="\t\t\t";
        for(String l:lines){
            c+=l;
            if(!l.endsWith("  "))
                c+=" ";
            else
                c+="; ";
        }
        return c;
    }

}
