package com.sanshisoft.lreboot;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.RadioGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gc.materialdesign.views.ButtonFlat;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.radioGroup)
    RadioGroup radioGroup;
    @InjectView(R.id.button_choose)
    ButtonFlat btnChoose;

    static Process rebootRecovery, rebootNormal, powerOff, rebootFastboot, rebootSafe,
            rebootHot;
    static String message, title, rebootType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId = radioGroup.getCheckedRadioButtonId();
                switch (radioId){
                    case R.id.reboot:
                        normalReboot();
                        break;
                    case R.id.recovery_reboot:
                        recoveryReboot();
                        break;
                    case R.id.bootloader_reboot:
                        fastbootReboot();
                        break;
                    case R.id.hot_reboot:
                        hotReboot();
                        break;
                    case R.id.safe_reboot:
                        safeReboot();
                        break;
                    case R.id.power_off:
                        powerOff();
                        break;
                }
            }
        });
    }

    public String normalReboot() {

        message = getString(R.string.msg_reboot);
        title = getString(R.string.title_reboot);
        rebootType = "normal";

        alertDialog(message, title);

        return rebootType;

    }

    public String recoveryReboot() {

        message = getString(R.string.msg_recovery);
        title = getString(R.string.title_recovery);
        rebootType = "recovery";

        alertDialog(message, title);

        return rebootType;
    }

    public String  fastbootReboot() {

        message = getString(R.string.msg_bootloader);
        title = getString(R.string.title_bootloader);
        rebootType = "fastboot";

        alertDialog(message, title);

        return rebootType;
    }

    public String hotReboot() {

        message = getString(R.string.msg_hot);
        title = getString(R.string.title_hot);
        rebootType = "hot";

        alertDialog(message, title);

        return rebootType;
    }

    public String safeReboot() {

        title = getString(R.string.title_safe);
        message = getString(R.string.msg_safe);
        rebootType = "safe";

        alertDialog(message, title);

        return rebootType;
    }

    public String powerOff() {

        title = getString(R.string.title_power);
        message = getString(R.string.msg_power);
        rebootType = "poweroff";

        alertDialog(message, title);

        return rebootType;
    }

    public void alertDialog(String message, String title) {

        new MaterialDialog.Builder(this)
                .title(title)
                .content(message)
                .positiveText(R.string.dlg_ok)
                .negativeText(R.string.dlg_cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        rebooter(rebootType);
                        showProgressDialog();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                    }
                })
                .build()
                .show();
    }

    private void showProgressDialog(){
        new MaterialDialog.Builder(this)
                .customView(R.layout.custom_dialog, true)
                .positiveColor(Color.parseColor("#03a9f4"))
                .build()
                .show();
    }

    public void rebooter(String rebootType) {

        switch (rebootType) {
            case "normal":
                try {
                    rebootNormal = Runtime.getRuntime().exec(new String[]{"su", "-c", "reboot"});
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case "recovery":
                try {
                    rebootRecovery = Runtime.getRuntime().exec(new String[]{"su", "-c", "reboot",
                            "recovery"});
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case "bootloader":
                try {
                    rebootFastboot = Runtime.getRuntime().exec(new String[]{"su", "-c", "reboot",
                            "bootloader"});
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case "hot":
                try {
                    rebootHot = Runtime.getRuntime().exec(new String[]{"su", "-c", "setprop",
                            "ctl.restart", "zygote"});
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case "safe":
                try {
                    rebootSafe = Runtime.getRuntime().exec(new String[]{"su", "-c", "setprop",
                            "persist.sys.safemode", "1"});
                    rebootHot = Runtime.getRuntime().exec(new String[]{"su", "-c", "setprop",
                            "ctl.restart", "zygote"});
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "poweroff":
                try {
                    powerOff = Runtime.getRuntime().exec(new String[]{"su", "-c", "reboot","-p"});
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
