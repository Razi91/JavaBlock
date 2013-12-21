/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package widgets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.table.AbstractTableModel;


public class TableModelEx extends AbstractTableModel{
    private String[] columnNames;
    private List<Object[]> data=new ArrayList();
    public TableModelEx(String[] headers, Object[][] rows){
        columnNames=headers;
        data.addAll(Arrays.asList(rows));
    }
    public TableModelEx(String[] headers){
        columnNames=headers;
    }
    public TableModelEx(){
        String[] headers={"Names", "Values"};
        Object[][] rows={{" ", " "}};
        columnNames=headers;
        data.addAll(Arrays.asList(rows));
    }
    public int getRowCount() {
        return data.size();}
    public int getColumnCount() {
        return columnNames.length;}
    @Override
    public String getColumnName(int col) {
        return columnNames[col];}
    public Object getValueAt(int i, int i1) {
        return data.get(i)[i1];
    }
    @Override
    public void setValueAt(Object value, int row, int col) {
        data.get(row)[col] = value;
        fireTableCellUpdated(row, col);
    }
    public void addRow(Object[] row){
        for(Object r[]:data){
            String z=r[0].toString();
            if(z.equals(row[0].toString())){
                return;
            }
        }
        data.add(row);
    }
    public void optimize(){
        for(int i=0; i<data.size(); i++){
            String v=(String)data.get(i)[0];
            if(v.length()<1 || v.equals(" ")){
                data.remove(i);
                i--;
            }
        }
    }
};
