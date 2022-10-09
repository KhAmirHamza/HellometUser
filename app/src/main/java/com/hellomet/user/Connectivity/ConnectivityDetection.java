package com.hellomet.user.Connectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

public class ConnectivityDetection extends BroadcastReceiver {
    CustomDialog customDialog;
//    WebView webView;

    ConnectionBroadcast connectionBroadcast;

/*    ConnectivityDetection(){
        this.webView = webView;
    }*/
    @Override
    public void onReceive(Context context, Intent intent) {

        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            boolean noConnectivity = intent.getBooleanExtra(
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY,false
            );

            if (noConnectivity){
                if (customDialog==null){
                    customDialog = new CustomDialog(context);
                    customDialog.setCancelable(false);
                    customDialog.show();
                }else {
                    customDialog.show();
                }
                 //Toast.makeText(context,"Disconnected",Toast.LENGTH_SHORT).show();
            }
            else {
                if (customDialog!=null){
                    customDialog.dismiss();
                    customDialog.cancel();
                    customDialog.dismiss();
                    /*String url = "https://banglanews26.com";
                    webView.loadUrl(url);*/
                    connectionBroadcast.onConnectionReset(noConnectivity);

                }

                 //Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
            }
        }
    }



    public interface ConnectionBroadcast {
        void onConnectionReset(boolean connectionStatus);
    }
    public void setConnectionBroadcast(ConnectionBroadcast connectionBroadcast){
        this.connectionBroadcast = connectionBroadcast;
    }
}


