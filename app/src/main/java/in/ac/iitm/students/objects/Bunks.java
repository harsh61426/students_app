package in.ac.iitm.students.objects;

import android.inputmethodservice.Keyboard;

import java.util.ArrayList;

/**
 * Created by SAM10795 on 19-06-2017.
 */

public class Bunks extends Course {

    private int bunk_tot;
    private int bunk_done;
    private ArrayList<RowCol> rowCols;

    public int getBunk_done() {
        return bunk_done;
    }

    public int getBunk_tot() {
        return bunk_tot;
    }

    public void setBunk_done(int bunk_done) {
        this.bunk_done = bunk_done;
    }

    public void setBunk_tot(int bunk_tot) {
        this.bunk_tot = bunk_tot;
    }

    public void setRowCols(ArrayList<RowCol> rowCols) {
        this.rowCols = rowCols;
    }

    public ArrayList<RowCol> getRowCols() {
        return rowCols;
    }
}
