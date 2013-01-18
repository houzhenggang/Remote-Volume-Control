package com.adam.rvc.activity;

import android.os.Bundle;
import android.view.View;
import com.adam.rvc.R;
import com.adam.rvc.receiver.ServerMessageReceiver;
import com.adam.rvc.service.RVCServiceFactory;
import com.adam.rvc.util.MessageHandler;
import com.adam.rvc.util.SharedPrefsHelper;

public class MainActivity extends RVCActivity  {

    private final ServerMessageReceiver messageReceiver;

    public MainActivity() {
        messageReceiver = new ServerMessageReceiver();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMessageReceiver();
    }

    private void initMessageReceiver() {
        messageReceiver.setMessageHandler(new MessageHandler(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setFragmentVisibility(findViewById(R.id.server_discovery_fragment));
        startService(RVCServiceFactory.startServerScanner(this));
        registerReceiver();
    }

    private void setFragmentVisibility(View view) {
        if (new SharedPrefsHelper(this).getShowServerDetailsSetting()) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    private void registerReceiver() {
        registerReceiver(messageReceiver, messageReceiver.getIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(RVCServiceFactory.stopService(this));
        unregisterReceiver();
    }

    private void unregisterReceiver() {
        unregisterReceiver(messageReceiver);
    }

}

