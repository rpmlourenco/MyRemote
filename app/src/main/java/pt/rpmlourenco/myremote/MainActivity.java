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
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

    public static MainActivity activity;
    public Toast toast;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        activity = this;
        toast = null;

        super.onCreate(savedInstanceState);

        setContentView(pt.rpmlourenco.myremote.R.layout.content_main);
        String packageName = getPackageName();

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.remotelayout);
        for (int i = 0; i < layout.getChildCount(); i++) {

            View v = layout.getChildAt(i);
            String name = getResources().getResourceEntryName(v.getId());

            if (!name.equals("wol")) {

                try {
                    int fieldId = getResources().getIdentifier("cmd_" + name, "string", packageName);
                    if (v instanceof ImageView) {
                        if (name.equals("sbvoldown") || name.equals("sbvolup"))
                            v.setOnTouchListener(new RepeatListener(165, 108, getString(fieldId), this));
                        else
                            v.setOnTouchListener(new RepeatListener(getString(fieldId), this));
                    } else if (v instanceof Button) {
                        v.setOnTouchListener(new RepeatListener(getString(fieldId), this));
                    }
                } catch (Exception e) {
                    Log.e("IR commands", "IR command " + "cmd_" + name + " not defined in ircodes.xml");
                }

            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.amenu_wakep8z77i) {

            new WOL().execute(getString(R.string.P8Z77I_RUI_MAC));
            //Toast.makeText(activity.getApplicationContext(), "P8Z77I woken", Toast.LENGTH_SHORT).show();
            showAToast("P8Z77I woken");
            return true;
        }
        if (id == R.id.amenu_wakeasusefi) {

            new WOL().execute(getString(R.string.ASUS_EFI_MAC));
            //Toast.makeText(activity.getApplicationContext(), "ASUS-EFI woken", Toast.LENGTH_SHORT).show();
            showAToast("ASUS-EFI woken");
            return true;
        }
        if (id == R.id.amenu_itunes) {

            TCPClientParams params = new TCPClientParams(getString(R.string.ASUS_EFI), 13000, 50000, "startitunes");
            new TCPClient(activity).execute(params);
            return true;
        }
        if (id == R.id.amenu_shutdown) {

            TCPClientParams params = new TCPClientParams(getString(R.string.ASUS_EFI), 13000, 50000, "shutdown");
            new TCPClient(activity).execute(params);
            return true;
        }
        if (id == R.id.amenu_retune) {
            startNewActivity(activity.getBaseContext(),"com.squallydoc.retune");
            return true;
        }
        if (id == R.id.amenu_rmouse) {
            startNewActivity(activity.getBaseContext(),"com.hungrybolo.remotemouseandroid");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startNewActivity(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + packageName));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void showAToast (String st){
        try{ toast.getView().isShown();     // true if visible
            toast.setText(st);
        } catch (Exception e) {         // invisible if exception
            toast = Toast.makeText(activity.getApplicationContext(), st, Toast.LENGTH_SHORT);
        }
        toast.show();  //finally display it
    }

}
