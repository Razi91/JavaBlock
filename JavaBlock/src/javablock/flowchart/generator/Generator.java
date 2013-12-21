/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javablock.flowchart.generator;

import javablock.flowchart.Flowchart;
import javablock.flowchart.JBlock;

/**
 *
 * @author razi
 */
public interface Generator {
    public JBlock[] get(Flowchart f);
}
