/*
 * Copyright (c) 2019. BLoo
 */

package net.ltslab.games.breakoutgdx.trainer;

import com.badlogic.gdx.utils.ScreenUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SendStateReceiveAction {
    private final static int DEFAULT_PORT = 49001;
    private final ExecutorService pool;

    private Socket client;
    private int port;
    private DataOutputStream dataOutputStream;
    boolean canSend;

    private int skipFrames;
    private int totalSend;
    private boolean stopSending;

    public SendStateReceiveAction() throws IOException {
        this(DEFAULT_PORT);
    }

    public SendStateReceiveAction(int port) {

        pool = Executors.newSingleThreadExecutor();
        this.port = port;
        if (client == null) {
            try {
                client = new Socket("localhost", port);
                if (client.isConnected()) {
                    dataOutputStream = new DataOutputStream(client.getOutputStream());

                    pool.execute(new InputHandler(client));
                    canSend = true;
                }
            } catch (IOException e) {
                canSend = false;
            }
        }
    }

    public void sendState(byte[] state) throws IOException {

        if (canSend) {

            dataOutputStream.writeInt(state.length);
            dataOutputStream.write(state);
            dataOutputStream.flush();
        }
    }

    public void stop() throws Exception {
        if (dataOutputStream != null) {
            dataOutputStream.writeInt(-1);
            dataOutputStream.flush();
        }
    }

    public void update () {
        if (skipFrames >= 4) {
            if (!stopSending) {
                takeScreenShot();
            }
            skipFrames = 0;
        } else {
            skipFrames++;
        }
    }

    private void takeScreenShot() {
        totalSend++;
        if (totalSend > 500) {
            stopSending = true;
            try {
                stop();
                System.out.println("Stop sending.");
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        byte[] pixels = ScreenUtils.getFrameBufferPixels(true);

        // this loop makes sure the whole screenshot is opaque and looks exactly like what the user is seeing
        for (int i = 4; i < pixels.length; i += 4) {
            pixels[i - 1] = (byte) 255;
        }

        try {
            sendState(pixels);
            //System.out.println("State was send.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.println("Pixels size: " + pixels.length);

//        Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), Pixmap.Format.RGBA8888);
//        BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
//        PixmapIO.writePNG(new FileHandle(Gdx.files.getLocalStoragePath() + "mypixmap.png"), pixmap);
//        pixmap.dispose();


    }

    private static class InputHandler implements Runnable {
        private Socket client;

        public InputHandler(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            int protocolCode = 0;
            boolean transmissionOn = true;
            try {
                DataInputStream dis = new DataInputStream(client.getInputStream());
                while (transmissionOn) {
                    protocolCode = dis.readInt();
                    if (protocolCode == 200) {
                        System.out.println("State received.");
                    } else if (protocolCode == 202) {
                        System.out.println("Everything ok. End of transmission.");
                        transmissionOn = false;
                        client.getOutputStream().close();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
