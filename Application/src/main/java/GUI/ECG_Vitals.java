package GUI;

import javafx.application.Platform;
import javafx.geometry.Pos;
import simulation.BPM;
import simulation.Value_Counter;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutorService;

public class ECG_Vitals extends Vital_Values_Display {
    private BPM ecg_val_counter;
    private String value;
    private int i_value;
    private boolean a;
    private int status;

    public ECG_Vitals(Dimension vitals_panel_dim, BPM obj, ExecutorService exe){
        super(vitals_panel_dim, "label-ECG", exe);
        super.status_msg.setText("STABLE");
        super.vital_type.setText("ECG");
        super.units_label.setText("BPM");
        super.units_label.setAlignment(Pos.CENTER_RIGHT);

        ecg_val_counter = obj;
        a = false;
    }

    public void Set_Displayed_Value(){
        Platform.runLater(() ->{
            value = String.valueOf(ecg_val_counter.BPM_number());
            super.vital_value.setText(value);
            i_value = Integer.parseInt(value);
            if (i_value != 0)
                a = true;
            if(a) {
                CheckStatus();
                statusInMinute(getStatus());
                minuteClock(getMean());
            }
        });
    }

    public double getMean(){
        return ecg_val_counter.getMean(0);
    }

    protected void CheckStatus() {
        if ((i_value > 80 && i_value < 120) || (i_value < 50 && i_value > 40)) {
            status = 2;
            warning();
        }
        else if ((i_value <= 40) || (i_value >= 120)){
            status = 3;
            urgent();
        }
        else{
            status = 1;
            stable();
        }
    }

    public int getBPM(){
        return  i_value;
    }

    public int getStatus(){
        return status;
    }

}
