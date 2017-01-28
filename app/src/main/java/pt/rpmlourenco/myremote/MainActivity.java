/*
 * Copyright 2015 Thomas Hoffmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pt.rpmlourenco.myremote;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {

    // IR Commands for Samsung TV UE32H...
    // from http://www.remotecentral.com/cgi-bin/codes/samsung/tv_functions/

    public final static String CMD_SB_NEC_REPEAT =
            "0000 006b 0000 0002 015a 0056 0015 0e66";
    private final static String CMD_TV_POWER =
            "0000 006d 0022 0003 00a9 00a8 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0040 0015 0015 0015 003f 0015 003f 0015 003f 0015 003f 0015 003f 0015 003f 0015 0702 00a9 00a8 0015 0015 0015 0e6e";
    // http://www.remotecentral.com/cgi-bin/mboard/prontopro/thread.cgi?2990
    private final static String CMD_TV_HDMI1 =
            "0000 006D 0000 0022 00AC 00AB 0015 0041 0015 0041 0015 0041 0015 0016 0015 0016 0015 0016 0015 0016 0015 0016 0015 0041 0015 0041 0015 0041 0015 0016 0015 0016 0015 0016 0015 0016 0015 0016 0015 0041 0015 0016 0015 0016 0015 0041 0015 0016 0015 0041 0015 0041 0015 0041 0015 0016 0015 0041 0015 0041 0015 0016 0015 0041 0015 0016 0015 0016 0015 0016 0015 0689";
    private final static String CMD_TV_HDMI2 =
            "0000 006D 0000 0022 00AC 00AB 0015 0041 0015 0041 0015 0041 0015 0016 0015 0016 0015 0016 0015 0016 0015 0016 0015 0041 0015 0041 0015 0041 0015 0016 0015 0016 0015 0016 0015 0016 0015 0016 0015 0016 0015 0041 0015 0041 0015 0041 0015 0041 0015 0041 0015 0016 0015 0041 0015 0041 0015 0016 0015 0016 0015 0016 0015 0016 0015 0016 0015 0041 0015 0016 0015 0689";
    //private final static String CMD_TV_HDMI4 =
    //        "0000 006D 0000 0022 00AC 00AB 0015 0041 0015 0041 0015 0041 0015 0016 0015 0016 0015 0016 0015 0016 0015 0016 0015 0041 0015 0041 0015 0041 0015 0016 0015 0016 0015 0016 0015 0016 0015 0016 0015 0041 0015 0016 0015 0041 0015 0016 0015 0016 0015 0016 0015 0041 0015 0041 0015 0016 0015 0041 0015 0016 0015 0041 0015 0041 0015 0041 0015 0016 0015 0016 0015 0689";
    // from http://irdb.tk/codes/
    private final static String CMD_TV_HDMI3 =
            "0000 006D 0000 0022 00AC 00AB 0015 0041 0015 0041 0015 0041 0015 0016 0015 0016 0015 0016 0015 0016 0015 0016 0015 0041 0015 0041 0015 0041 0015 0016 0015 0016 0015 0016 0015 0016 0015 0016 0015 0016 0015 0041 0015 0016 0015 0016 0015 0016 0015 0016 0015 0041 0015 0041 0015 0041 0015 0016 0015 0041 0015 0041 0015 0041 0015 0041 0015 0016 0015 0016 0015 0689";

    // IR Commands for Samsung Soundbar HW-H450
    // from https://www.remotecentral.com/cgi-bin/forums/viewpost.cgi?932443
    private final static String CMD_TV_ENTER =
            "0000 006C 0000 0022 00AD 00AD 0016 0041 0016 0041 0016 0041 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0041 0016 0041 0016 0041 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0041 0016 0016 0016 0041 0016 0041 0016 0016 0016 0041 0016 0041 0016 0041 0016 0016 0016 0041 0016 0016 0016 0016 0016 0041 0016 06FB";
    private final static String CMD_SB_POWER =
            "0000 006b 0023 0002 0109 080b 015a 00ac 0015 0040 0015 0040 0015 0040 0015 0015 0015 0015 0015 0015 0015 0015 0015 0040 0015 0015 0015 0015 0015 0040 0015 0040 0015 0040 0015 0040 0015 0040 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0040 0015 0040 0015 0040 0015 0040 0015 0040 0015 0040 0015 0040 0015 0040 0015 0015 0015 05cf 015a 0056 0015 0e66";
    private final static String CMD_SB_VOLUP =
            "0000 006b 0023 0002 00fe 0810 015a 00ac 0015 0040 0015 0040 0015 0040 0015 0015 0015 0015 0015 0015 0015 0015 0015 0040 0015 0015 0015 0015 0015 0040 0015 0040 0015 0040 0015 0040 0015 0040 0015 0015 0015 0015 0015 0015 0015 0015 0015 0040 0015 0015 0015 0015 0015 0015 0015 0040 0015 0040 0015 0040 0015 0040 0015 0015 0015 0040 0015 0040 0015 0040 0015 0015 0015 05cf 015a 0056 0015 0e66";
    private final static String CMD_SB_VOLDOWN =
            "0000 006b 0023 0002 00fe 0815 015a 00ac 0015 0040 0015 0040 0015 0040 0015 0015 0015 0015 0015 0015 0015 0015 0015 0040 0015 0015 0015 0015 0015 0040 0015 0040 0015 0040 0015 0040 0015 0040 0015 0015 0015 0015 0015 0015 0015 0040 0015 0040 0015 0015 0015 0015 0015 0015 0015 0040 0015 0040 0015 0040 0015 0015 0015 0015 0015 0040 0015 0040 0015 0040 0015 0015 0015 05cf 015a 0056 0015 0e66";
    private final static String CMD_SB_VOLMUTE =
            "0000 006b 0023 0002 00fe 081c 015a 00ac 0015 0040 0015 0040 0015 0040 0015 0015 0015 0015 0015 0015 0015 0015 0015 0040 0015 0015 0015 0015 0015 0040 0015 0040 0015 0040 0015 0040 0015 0040 0015 0015 0015 0015 0015 0015 0015 0040 0015 0015 0015 0040 0015 0015 0015 0015 0015 0040 0015 0040 0015 0040 0015 0015 0015 0040 0015 0015 0015 0040 0015 0040 0015 0015 0015 05cf 015a 0056 0015 0e66";
    private final static String CMD_SB_AUX =
            "0000 006b 0023 0002 00fb 0862 015a 00ac 0015 0040 0015 0040 0015 0040 0015 0015 0015 0015 0015 0015 0015 0015 0015 0040 0015 0015 0015 0015 0015 0040 0015 0040 0015 0040 0015 0040 0015 0040 0015 0015 0015 0040 0015 0040 0015 0015 0015 0040 0015 0040 0015 0015 0015 0015 0015 0040 0015 0015 0015 0015 0015 0040 0015 0015 0015 0015 0015 0040 0015 0040 0015 0015 0015 05cf 015a 0056 0015 0e66";
    private final static String CMD_SB_VIDEO =
            "0000 006b 0023 0002 00fb 087a 015a 00ac 0015 0040 0015 0040 0015 0040 0015 0015 0015 0015 0015 0015 0015 0015 0015 0040 0015 0015 0015 0015 0015 0040 0015 0040 0015 0040 0015 0040 0015 0040 0015 0015 0015 0015 0015 0040 0015 0015 0015 0015 0015 0015 0015 0015 0015 0040 0015 0040 0015 0040 0015 0015 0015 0040 0015 0040 0015 0040 0015 0040 0015 0015 0015 0015 0015 05cf 015a 0056 0015 0e66";
    // Movistar
    private final static String CMD_ST_POWER =
            "0000 004A 0011 0000 0036 0037 0024 0048 0012 0024 0012 0036 0012 0037 0012 0036 0012 0036 0012 0036 0012 0037 0012 0036 0012 005A 0012 0012 0012 0036 0012 0037 0012 005A 0012 0024 0012 0900";
    private final static String CMD_ST_CHUP =
            "0000 004A 0011 0000 0035 0037 0024 0048 0012 0024 0012 0036 0012 0036 0012 0036 0012 0036 0012 0036 0012 0036 0012 006C 0012 0024 0012 0024 0012 0024 0012 0036 0012 006C 0012 0012 0012 0900";
    private final static String CMD_ST_CHDOWN =
            "0000 004A 0011 0000 0035 0037 0024 0048 0012 0024 0012 0036 0012 0036 0012 0036 0012 0036 0012 0036 0012 0048 0012 005A 0012 0036 0012 0024 0012 0012 0012 0036 0012 0048 0012 0048 0012 0900";
    private final static String CMD_ST_GUIDE =
            "0000 004A 0011 0000 0035 0037 0024 0048 0012 0024 0012 0036 0012 0036 0012 0036 0012 0036 0012 0036 0012 0048 0012 0048 0012 0036 0012 0036 0012 0012 0012 0036 0012 0036 0012 0048 0012 0900";
    private final static String CMD_ST_BACK =
            "0000 0049 0000 0010 0037 0036 0023 004A 0012 0024 0012 0037 0012 0037 0012 0037 0012 0037 0012 0037 0012 0049 0012 0037 0012 0049 0012 0049 0023 0037 0012 0037 0012 0049 0012 1161";

    private final static String CMD_ST_DOWN =
            "0000 004A 0011 0000 0035 0037 0024 0048 0012 0024 0012 0036 0012 0036 0012 0036 0012 0036 0012 0036 0012 0048 0012 0024 0012 005A 0012 0012 0012 0036 0012 0036 0012 006C 0012 0012 0012 0900";
    private final static String CMD_ST_LEFT =
            "0000 004A 0011 0000 0035 0037 0024 0048 0012 0024 0012 0036 0012 0036 0012 0036 0012 0036 0012 0036 0012 0048 0012 0036 0012 0048 0012 0012 0012 0036 0012 0036 0012 006C 0012 0024 0012 0900";
    private final static String CMD_ST_RIGHT =
            "0000 004A 0011 0000 0035 0037 0024 0048 0012 0024 0012 0036 0012 0036 0012 0036 0012 0036 0012 0036 0012 0048 0012 0048 0012 0036 0012 0012 0012 0036 0012 0036 0012 006C 0012 0036 0012 0900";
    private final static String CMD_ST_UP =
            "0000 004A 0010 0000 0035 0037 0024 0048 0012 0024 0012 0036 0012 0036 0012 0036 0012 0036 0012 0036 0012 0036 0012 006C 0012 0024 0012 0012 0012 0036 0012 0036 0012 006C 0024 0900";
    private final static String CMD_ST_OK =
            "0000 004A 0010 0000 0035 0037 0024 0048 0012 0024 0012 0036 0012 0036 0012 0036 0012 0036 0012 0036 0012 0036 0012 0036 0012 006C 0012 0036 0024 0036 0012 0036 0012 0036 0012 0900";

    private final static String CMD_ST_0 =
            "0000 004A 0011 0000 0035 0037 0024 0048 0012 0024 0012 0036 0012 0036 0012 0036 0012 0036 0012 0036 0012 0036 0012 0048 0012 005A 0012 0012 0012 0024 0012 0036 0012 006C 0012 0036 0012 0900";
    private final static String CMD_ST_1 =
            "0000 004A 0011 0000 0035 0037 0024 0048 0012 0024 0012 0036 0012 0036 0012 0036 0012 0036 0012 0036 0012 0048 0012 0036 0012 0048 0012 0024 0012 0024 0012 0036 0012 006C 0012 0036 0012 0900";
    private final static String CMD_ST_2 =
            "0000 004A 0011 0000 0035 0037 0024 0048 0012 0024 0012 0036 0012 0036 0012 0036 0012 0036 0012 0036 0012 0048 0012 0048 0012 0036 0012 0024 0012 0024 0012 0036 0012 0036 0012 0036 0012 0900";
    private final static String CMD_ST_3 =
            "0000 004A 0011 0000 0035 0036 0024 0048 0012 0024 0012 0036 0012 0036 0012 0036 0012 0036 0012 0036 0012 0048 0012 005A 0012 0024 0012 0024 0012 0024 0012 0036 0012 0036 0012 0048 0012 0900";
    private final static String CMD_ST_4 =
            "0000 004A 0010 0000 0035 0037 0024 0048 0012 0024 0012 0036 0012 0036 0012 0036 0012 0036 0012 0036 0012 0036 0012 0048 0012 005A 0024 0036 0012 0036 0012 006C 0012 0024 0012 0900";
    private final static String CMD_ST_5 =
            "0000 004A 0010 0000 0035 0037 0024 0048 0012 0024 0012 0036 0012 0036 0012 0036 0012 0036 0012 0036 0012 0036 0012 005A 0012 0048 0024 0036 0012 0036 0012 006C 0012 0036 0012 0900";
    private final static String CMD_ST_6 =
            "0000 004A 0010 0000 0035 0037 0024 0048 0012 0024 0012 0036 0012 0036 0012 0036 0012 0036 0012 0036 0012 0036 0012 006C 0012 0036 0024 0036 0012 0036 0012 0036 0012 0036 0012 0900";
    private final static String CMD_ST_7 =
            "0000 004A 0010 0000 0035 0037 0024 0048 0012 0024 0012 0036 0012 0036 0012 0036 0012 0036 0012 0036 0012 0048 0012 0024 0012 006C 0024 0036 0012 0036 0012 0036 0012 0048 0012 0900";
    private final static String CMD_ST_8 =
            "0000 004A 0010 0000 0035 0036 0024 0048 0012 0024 0012 0036 0012 0036 0012 0036 0012 0036 0012 0036 0012 0048 0012 0036 0012 005A 0024 0036 0012 0036 0012 0036 0012 005A 0012 0900";
    private final static String CMD_ST_9 =
            "0000 004A 0010 0000 0035 0037 0024 0048 0012 0024 0012 0036 0012 0036 0012 0036 0012 0036 0012 0036 0012 0048 0012 0048 0012 0048 0024 0036 0012 0036 0012 0036 0012 006C 0012 0900";
    private final static String CMD_ST_PREVIOUS_CHANNEL =
            "0000 004A 0010 0000 0035 0037 0024 0048 0012 0024 0012 0036 0012 0036 0012 0036 0012 0036 0012 0036 0012 0048 0012 0036 0012 0048 0012 0048 0024 0036 0012 0036 0012 0048 0012 0900";
    private final static String CMD_ST_MENU =
            "0000 004A 0011 0000 0035 0037 0024 0048 0012 0024 0012 0036 0012 0036 0012 0036 0012 0036 0012 0036 0012 0048 0012 0024 0012 005A 0012 0024 0012 0024 0012 0036 0012 006C 0012 0024 0012 0900";

    private final static String CMD_ST_BLUE =
            "0000 004A 0010 0000 0035 0037 0024 0049 0012 0024 0012 0036 0012 0036 0012 0036 0012 0037 0012 0036 0012 0036 0012 0048 0012 005B 0012 0036 0024 0036 0012 0036 0012 0049 0012 0900";
    private final static String CMD_ST_GREEN =
            "0000 004A 0010 0000 0036 0037 0024 0048 0012 0024 0012 0036 0012 0037 0012 0036 0012 0036 0012 0036 0012 0036 0013 006C 0012 0036 0012 0036 0024 0037 0012 0036 0012 006C 0012 0900";
    private final static String CMD_ST_RED =
            "0000 004A 0010 0000 0035 0036 0024 0049 0012 0024 0012 0036 0012 0036 0012 0037 0012 0036 0012 0036 0012 0048 0012 0024 0013 005A 0012 0048 0024 0036 0012 0037 0012 0036 0012 0900";
    private final static String CMD_ST_YELLOW =
            "0000 004A 0011 0000 0035 0037 0024 0048 0012 0024 0012 0037 0012 0036 0012 0036 0012 0036 0012 0037 0012 0048 0012 005A 0012 0024 0012 0012 0012 0037 0012 0036 0012 0036 0012 0036 0012 0900";

    private final static String CMD_ST_PLAY =
            "0000 004A 0010 0000 0035 0037 0024 0048 0012 0024 0012 0036 0013 0036 0012 0036 0012 0036 0012 0036 0012 0037 0012 005A 0012 0036 0012 0024 0012 0025 0012 0036 0012 006C 0024 0900";
    private final static String CMD_ST_PAUSE =
            "0000 004A 0011 0000 0036 0036 0024 0049 0012 0024 0012 0036 0012 0036 0012 0036 0013 0036 0012 0036 0012 0036 0012 0036 0012 005B 0012 0036 0012 0012 0012 0036 0012 005B 0012 0048 0012 0900";
    private final static String CMD_ST_FFORWARD =
            "0000 004A 0000 0011 0037 0035 0023 0048 0012 0024 0012 0036 0012 0036 0012 0036 0012 0036 0012 0036 0012 0036 0012 006D 0012 0024 0012 0036 0012 0011 0012 0036 0012 006D 0012 0024 0012 1113";
    private final static String CMD_ST_REWIND =
            "0000 004A 0000 0011 0037 0035 0023 0048 0012 0024 0012 0036 0012 0036 0012 0036 0012 0036 0012 0036 0012 0036 0012 005A 0012 0036 0012 0036 0012 0011 0012 0036 0012 006D 0012 0011 0012 1125";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(pt.rpmlourenco.myremote.R.layout.content_main);

        // TV
        findViewById(pt.rpmlourenco.myremote.R.id.tvpower).setOnTouchListener(new RepeatListener(CMD_TV_POWER, this));
        findViewById(pt.rpmlourenco.myremote.R.id.tvhdmi1).setOnTouchListener(new RepeatListener(CMD_TV_HDMI1, this));
        findViewById(pt.rpmlourenco.myremote.R.id.tvhdmi2).setOnTouchListener(new RepeatListener(CMD_TV_HDMI2, this));
        findViewById(pt.rpmlourenco.myremote.R.id.tvhdmi3).setOnTouchListener(new RepeatListener(CMD_TV_HDMI3, this));
        findViewById(pt.rpmlourenco.myremote.R.id.tvok).setOnTouchListener(new RepeatListener(CMD_TV_ENTER, this));

        // Amplifier
        findViewById(pt.rpmlourenco.myremote.R.id.sbpower).setOnTouchListener(new RepeatListener(CMD_SB_POWER, this));
        findViewById(pt.rpmlourenco.myremote.R.id.sbvolup).setOnTouchListener(new RepeatListener(165, 108, CMD_SB_VOLUP, this));
        findViewById(pt.rpmlourenco.myremote.R.id.sbvoldown).setOnTouchListener(new RepeatListener(165, 108, CMD_SB_VOLDOWN, this));
        findViewById(pt.rpmlourenco.myremote.R.id.sbvolmute).setOnTouchListener(new RepeatListener(CMD_SB_VOLMUTE, this));
        findViewById(pt.rpmlourenco.myremote.R.id.sbvideo).setOnTouchListener(new RepeatListener(CMD_SB_VIDEO, this));
        findViewById(pt.rpmlourenco.myremote.R.id.sbaux).setOnTouchListener(new RepeatListener(CMD_SB_AUX, this));

        // Movistar STB
        findViewById(pt.rpmlourenco.myremote.R.id.stbpower).setOnTouchListener(new RepeatListener(CMD_ST_POWER, this));
        findViewById(pt.rpmlourenco.myremote.R.id.stbchup).setOnTouchListener(new RepeatListener(CMD_ST_CHUP, this));
        findViewById(pt.rpmlourenco.myremote.R.id.stbchdown).setOnTouchListener(new RepeatListener(CMD_ST_CHDOWN, this));
        findViewById(pt.rpmlourenco.myremote.R.id.stbguide).setOnTouchListener(new RepeatListener(CMD_ST_GUIDE, this));
        findViewById(pt.rpmlourenco.myremote.R.id.stbback).setOnTouchListener(new RepeatListener(CMD_ST_BACK, this));

        findViewById(pt.rpmlourenco.myremote.R.id.stbup).setOnTouchListener(new RepeatListener(CMD_ST_UP, this));
        findViewById(pt.rpmlourenco.myremote.R.id.stbdown).setOnTouchListener(new RepeatListener(CMD_ST_DOWN, this));
        findViewById(pt.rpmlourenco.myremote.R.id.stbleft).setOnTouchListener(new RepeatListener(CMD_ST_LEFT, this));
        findViewById(pt.rpmlourenco.myremote.R.id.stbright).setOnTouchListener(new RepeatListener(CMD_ST_RIGHT, this));
        findViewById(pt.rpmlourenco.myremote.R.id.stbok).setOnTouchListener(new RepeatListener(CMD_ST_OK, this));

        findViewById(pt.rpmlourenco.myremote.R.id.button_0).setOnTouchListener(new RepeatListener(CMD_ST_0, this));
        findViewById(pt.rpmlourenco.myremote.R.id.button_1).setOnTouchListener(new RepeatListener(CMD_ST_1, this));
        findViewById(pt.rpmlourenco.myremote.R.id.button_2).setOnTouchListener(new RepeatListener(CMD_ST_2, this));
        findViewById(pt.rpmlourenco.myremote.R.id.button_3).setOnTouchListener(new RepeatListener(CMD_ST_3, this));
        findViewById(pt.rpmlourenco.myremote.R.id.button_4).setOnTouchListener(new RepeatListener(CMD_ST_4, this));
        findViewById(pt.rpmlourenco.myremote.R.id.button_5).setOnTouchListener(new RepeatListener(CMD_ST_5, this));
        findViewById(pt.rpmlourenco.myremote.R.id.button_6).setOnTouchListener(new RepeatListener(CMD_ST_6, this));
        findViewById(pt.rpmlourenco.myremote.R.id.button_7).setOnTouchListener(new RepeatListener(CMD_ST_7, this));
        findViewById(pt.rpmlourenco.myremote.R.id.button_8).setOnTouchListener(new RepeatListener(CMD_ST_8, this));
        findViewById(pt.rpmlourenco.myremote.R.id.button_9).setOnTouchListener(new RepeatListener(CMD_ST_9, this));
        findViewById(pt.rpmlourenco.myremote.R.id.stbmenu).setOnTouchListener(new RepeatListener(CMD_ST_MENU, this));
        findViewById(pt.rpmlourenco.myremote.R.id.stbchprev).setOnTouchListener(new RepeatListener(CMD_ST_PREVIOUS_CHANNEL, this));

        findViewById(pt.rpmlourenco.myremote.R.id.stbred).setOnTouchListener(new RepeatListener(CMD_ST_RED, this));
        findViewById(pt.rpmlourenco.myremote.R.id.stbgreen).setOnTouchListener(new RepeatListener(CMD_ST_GREEN, this));
        findViewById(pt.rpmlourenco.myremote.R.id.stbyellow).setOnTouchListener(new RepeatListener(CMD_ST_YELLOW, this));
        findViewById(pt.rpmlourenco.myremote.R.id.stbblue).setOnTouchListener(new RepeatListener(CMD_ST_BLUE, this));

        findViewById(pt.rpmlourenco.myremote.R.id.stbrewind).setOnTouchListener(new RepeatListener(CMD_ST_REWIND, this));
        findViewById(pt.rpmlourenco.myremote.R.id.stbplay).setOnTouchListener(new RepeatListener(CMD_ST_PLAY, this));
        findViewById(pt.rpmlourenco.myremote.R.id.stbpause).setOnTouchListener(new RepeatListener(CMD_ST_PAUSE, this));
        findViewById(pt.rpmlourenco.myremote.R.id.stbfforward).setOnTouchListener(new RepeatListener(CMD_ST_FFORWARD, this));

        findViewById(pt.rpmlourenco.myremote.R.id.wol).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ImageView iview;

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        if (view instanceof ImageView) {
                            iview = (ImageView) view;
                            //overlay is black with transparency of 0x77 (119)
                            iview.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                            iview.invalidate();
                        }

                        new WOL().execute("3085A9EBFBE1");
                        Vibrator vibe = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                        vibe.vibrate(20);
                        return true;
                    case MotionEvent.ACTION_UP:

                    case MotionEvent.ACTION_CANCEL:

                        if (view instanceof ImageView) {
                            iview = (ImageView) view;
                            //clear the overlay
                            iview.getDrawable().clearColorFilter();
                            iview.invalidate();
                        }
                        return true;
                }

                return false;
            }

        });

    }

}
