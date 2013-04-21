package ui;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import model.JottoModel;

public class BackgroundSwing extends SwingWorker {
    private JottoModel jottomod;
    private int row_index;
    private int col_index;
    private DefaultTableModel mod;
    private String guessedString;

    public BackgroundSwing() {

    }

    public BackgroundSwing(JottoModel jottomod, int row_index, int col_index,
            DefaultTableModel mod, String guessedString) {
        this.jottomod = jottomod;
        this.row_index = row_index;
        this.col_index = col_index;
        this.mod = mod;
        this.guessedString = guessedString;

    }

    @Override
    protected String[] doInBackground() throws Exception {
        String[] c = new String[2];
        try {
            c = jottomod.makeGuess(guessedString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return c;
    }

    @Override
    public void done() {
        String[] s_resp = new String[2];
        try {
            s_resp = (String[]) this.get();

        } catch (InterruptedException e0) {
            e0.printStackTrace();
        } catch (ExecutionException e0) {
            e0.printStackTrace();
        }
        mod.setValueAt(s_resp[0], row_index, col_index);
        mod.setValueAt(s_resp[1], row_index, col_index + 1);
    }
}