package org.ligi.rooctl;

import android.util.Log;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.util.ArrayList;
import java.util.List;

public class Connector {

    private Session session = null;
    private List<String> commands;
    private boolean process_running=false;

    public Connector() {

        commands = new ArrayList<String>();

        new Thread(new Runnable() {

            @Override
            public void run() {
                connect();

            }
        }).start();
    }

    private void connect() {
        try {
            JSch shell = new JSch();

            session = shell.getSession("ligi", "192.168.0.185", 22);

            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.setPassword("raspberry");

            session.connect();

        } catch (JSchException e) {
            Log.w("", e);
        }
    }

    public void runCommand(final String command) {
        commands.add(command);

        if (!process_running) {
            new Thread(new CommandExecuteRunnable()).start();
        }

    }

    public void disconnect() {
        session.disconnect();
    }

    private class CommandExecuteRunnable implements Runnable {
        @Override
        public void run() {
            process_running=true;
            while (commands.size() > 0) {

                try {
                    if (session == null || !session.isConnected()) {
                        connect();
                    }
                    Channel channel = session.openChannel("exec");

                    ((ChannelExec) channel).setCommand(commands.get(0));
                    channel.setInputStream(null);

                    channel.connect();
                    channel.disconnect();
                    commands.remove(0);

                } catch (JSchException e) {
                    session.disconnect();
                    session = null;
                }
            }
            process_running=false;
        }
    }
}
