package Tuning;

import Graphing.Overall_Graph;
import javafx.scene.control.Slider;
import javafx.util.StringConverter;

public class Sliding extends Slider {
    Overall_Graph graphs_panel;
    String which;
    int vital_selector;

    public Sliding(String which, Overall_Graph graphs_panel, int which_vital){
        this.graphs_panel = graphs_panel;
        this.which = which;
        vital_selector = which_vital;

        if (which.equals("time_window")) {
            setUpTimeWindow();
            setActionTimeWindow();
        }

        if (which.equals("abnormality")){
            setUpAbnormality();
            setActionAbnormality();
        }

    }

    private void setUpTimeWindow() {
        setMin(5);
        setMax(60);
        setValue(5);
        setShowTickLabels(true);
        setShowTickMarks(false);
        setMajorTickUnit(1);

        //Set up labels of slider
        setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double aDouble) {
                if (aDouble == 20 || aDouble == 40 || aDouble == 5 || aDouble == 60)
                    return Integer.toString((int)aDouble.doubleValue()) ;
                else
                    return null;
            }

            @Override
            public Double fromString(String s) {
                return null;
            }
        });
    }

    private void setUpAbnormality() {
        setMin(-50);
        setMax(50);
        setValue(0);
        setShowTickLabels(true);
        setShowTickMarks(false);
        setMajorTickUnit(1);

        //Set up labels of slider
        setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double aDouble) {
                if (aDouble == -50 || aDouble == 50)
                    return "U";
                else if (aDouble == -25 || aDouble == 25)
                    return "W";
                else if (aDouble == 0)
                    return "S";
                else
                    return null;
            }
            @Override
            public Double fromString(String s) {
                return null;
            }
        });
    }

    private void setActionTimeWindow() {

        valueProperty().addListener((obs, oldval, newval) -> {
            if (oldval!=newval) {
                if (newval.intValue() < 13)
                    setValue(5);
                else if (newval.intValue() < 30)
                    setValue(20);
                else if (newval.intValue() < 50)
                    setValue(40);
                else
                    setValue(60);
                graphs_panel.changeTimeWindow((int) getValue());
            }
        });
//        slider.setMinorTickCount(5);
//        timeWindow.setBlockIncrement(10);
    }

    private void setActionAbnormality() {
        valueProperty().addListener((obs, oldval, newval) -> {
            int abnormalityValue;

            if (oldval!=newval) {
                if (newval.intValue() < -37) {
                    setValue(-50);
                    abnormalityValue = -2;
                }
                else if (newval.intValue() < -12){
                    setValue(-25);
                    abnormalityValue = -1;
                }

                else if (newval.intValue() < 13) {
                    setValue(0);
                    abnormalityValue = -0;
                }
                else if (newval.intValue() < 37) {
                    setValue(25);
                    abnormalityValue = 1;
                }
                else {
                    setValue(50);
                    abnormalityValue = 2;
                }
                action(abnormalityValue);
            }
        });
    }

    private void action(int type){
        graphs_panel.changeECGAbnormality(type, vital_selector);
    }

}
