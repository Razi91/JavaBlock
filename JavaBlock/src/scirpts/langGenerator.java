package scirpts;

import javablock.flowchart.blocks.*;
import javablock.flowchart.JBlock;

public interface langGenerator {
    public String signature(startBlock block);
    public String signature_null(startBlock block);
    public String normal(JBlock block);
    public String decision(decBlock block);
    public String end(JBlock block);
    public String structure(structBlock block);
    public String parseOnly(String code);
}
