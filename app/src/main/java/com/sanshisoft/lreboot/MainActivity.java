package com.sanshisoft.lreboot;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.IOException;


public class MainActivity extends ActionBarActivity {

    static Process rebootRecovery, rebootNormal, rebootFastboot,
            rebootSoft;
    static String message, title, rebootType;
    static int sSelectIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            Process process = Runtime.getRuntime().exec("su");
        }catch (Exception e){
            e.printStackTrace();
        }
        showRebootDialog(0);
    }

    public void showRebootDialog(int selectIndex){
        new MaterialDialog.Builder(this)
                .title(getString(R.string.app_name))
                .items(getResources().getStringArray(R.array.reboot_arrays))
                .itemsCallbackSingleChoice(selectIndex, new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        sSelectIndex = which;
                        switch (which){
                            case 0:
                                normalReboot();
                                break;
                            case 1:
                                softReboot();
                                break;
                            case 2:
                                recoveryReboot();
                                break;
                            case 3:
                                fastbootReboot();
                                break;
                        }
                    }
                }).cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        MainActivity.this.finish();
                    }
                })
                .positiveText(R.string.dlg_ok)
                .negativeText(R.string.dlg_cancel)
                .callback(new MaterialDialog.ButtonCallback() {

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        MainActivity.this.finish();
                    }
                })
                .show();
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

    public String softReboot() {

        message = getString(R.string.msg_soft);
        title = getString(R.string.title_soft);
        rebootType = "soft";

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
                        showRebootDialog(sSelectIndex);
                    }
                })
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        showRebootDialog(sSelectIndex);
                    }
                })
                .build()
                .show();
    }

    private void showProgressDialog(){
        MaterialDialog pDlg = new MaterialDialog.Builder(this)
                .customView(R.layout.custom_dialog, true)
                .build();
        pDlg.setCancelable(false);
        pDlg.show();
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
            case "soft":
                try {
                    rebootSoft = Runtime.getRuntime().exec(new String[]{"su", "-c", "setprop",
                            "ctl.restart", "zygote"});
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
        }
    }
}
