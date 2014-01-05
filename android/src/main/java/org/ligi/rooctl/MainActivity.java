package org.ligi.rooctl;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Views;

public class MainActivity extends ActionBarActivity {

    boolean blue_state=false;
    boolean green_state=false;
    private boolean white_state;

    Connector connector;

    @InjectView(R.id.blue)
    ImageView blue;


    @OnClick(R.id.blue)
    public void blue() {
        blue_state=!blue_state;

        syncBlueState();
    }

    private void syncBlueState() {
        if (blue_state) {
            connector.runCommand("sudo /home/ligi/rcswitch-pi/send 01101 2 1");
            blue.setImageResource(R.drawable.blue_on);
        } else {
            connector.runCommand("sudo /home/ligi/rcswitch-pi/send 01101 2 0");
            blue.setImageResource(R.drawable.blue_off);
        }
    }

    @InjectView(R.id.white)
    ImageView white;

    @OnClick(R.id.white)
    public void white() {
        white_state=!white_state;

        syncWhiteState();
    }

    private void syncWhiteState() {
        if (white_state) {
            connector.runCommand("sudo /home/ligi/rcswitch-pi/send 01101 3 1");
            white.setImageResource(R.drawable.white_on);
        } else {
            connector.runCommand("sudo /home/ligi/rcswitch-pi/send 01101 3 0");
            white.setImageResource(R.drawable.white_off);
        }
    }

    @InjectView(R.id.green)
    ImageView green;

    @OnClick(R.id.green)
    public void green() {
        green_state=!green_state;

        syncGreenState();
    }

    private void syncGreenState() {
        if (green_state) {
            connector.runCommand("sudo /home/ligi/rcswitch-pi/send 01101 1 1");
            green.setImageResource(R.drawable.green_on);
        } else {
            connector.runCommand("sudo /home/ligi/rcswitch-pi/send 01101 1 0");
            green.setImageResource(R.drawable.green_off);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connector=new Connector();
        setContentView(R.layout.activity_main);
        Views.inject(this);
        connector.runCommand("sudo /home/ligi/rcswitch-pi/send 01101 1 0;sudo /home/ligi/rcswitch-pi/send 01101 2 0;sudo /home/ligi/rcswitch-pi/send 01101 3 0");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connector.disconnect();
    }
}
