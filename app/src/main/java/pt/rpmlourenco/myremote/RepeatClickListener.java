package pt.rpmlourenco.myremote;

import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Rui on 22/01/2017.
 * Repeat
 */

public class RepeatClickListener implements View.OnClickListener {

    private final IRCommand repeatCmd;
    private final IRCommand cmd;
    private final ConsumerIrManager irManager;
    private boolean init;

    public RepeatClickListener(final String hex, MainActivity main) {
        this.cmd = hex2ir(hex);
        repeatCmd = hex2ir(MainActivity.CMD_SB_NEC_REPEAT);
        irManager = (ConsumerIrManager) main.getApplicationContext().getSystemService(Context.CONSUMER_IR_SERVICE);
    }

    private boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }

    @Override
    public void onClick(View v) {
        if (isInit()) {
            irManager.transmit(cmd.freq, cmd.pattern);
        } else {
            irManager.transmit(this.repeatCmd.freq, this.repeatCmd.pattern);
        }
    }

    // based on code from http://stackoverflow.com/users/1679571/randy (http://stackoverflow.com/a/25518468)
    private IRCommand hex2ir(final String irData) {
        List<String> list = new ArrayList<>(Arrays.asList(irData.split(" ")));
        list.remove(0); // dummy
        int frequency = Integer.parseInt(list.remove(0), 16); // frequency
        list.remove(0); // seq1
        list.remove(0); // seq2

        frequency = (int) (1000000 / (frequency * 0.241246));
        int pulses = 1000000 / frequency;
        int count;

        int[] pattern = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            count = Integer.parseInt(list.get(i), 16);
            pattern[i] = count * pulses;
        }

        return new IRCommand(frequency, pattern);
    }

    private class IRCommand {
        public final int freq;
        public final int[] pattern;

        private IRCommand(int freq, int[] pattern) {
            this.freq = freq;
            this.pattern = pattern;
        }
    }

}
