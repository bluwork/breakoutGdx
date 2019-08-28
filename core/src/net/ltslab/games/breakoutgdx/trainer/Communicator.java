/*
 * Copyright (c) 2019. BLoo
 */

package net.ltslab.games.breakoutgdx.trainer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import net.ltslab.games.breakoutgdx.management.GameManager;
import net.ltslab.games.breakoutgdx.util.Const;
import net.ltslab.games.breakoutgdx.util.GameUtils;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Communicator {

    private final static String TAG = Communicator.class.getSimpleName();
    private final static int DEFAULT_PORT = 49001;
    private final ExecutorService pool;

    private Socket client;
    private int port;
    private ObjectOutputStream dataOutputStream;
    private static boolean canSend;

    private ArrayList<byte[]> frames;

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    private boolean done = true;

    private int reward;
    private int takeFrame;

    public Communicator() throws IOException {
        this(DEFAULT_PORT);
    }

    public Communicator(int port) {

        pool = Executors.newSingleThreadExecutor();
        this.port = port;
        if (client == null) {
            try {
                client = new Socket("localhost", port);
                if (client.isConnected()) {
                    dataOutputStream = new ObjectOutputStream(client.getOutputStream());
                    pool.execute(new InputHandler(client));
                    canSend = true;
                }
            } catch (IOException e) {
                canSend = false;
            }
        }
    }

    int everyFrame = 0;
    // Called every frame when playing
    public void update() {
        if (takeFrame > Const.SKIP_FRAMES) {
            takeFrame = 0;
        }
        if (takeFrame == 0) {
            takeScreenShot();
        }
        takeFrame++;
    }

    public void sendState() throws IOException{

        net.ltslab.stepdata.StepData data = new net.ltslab.stepdata.StepData();
        data.setFrames(frames);
        data.setDone(done);
        data.setReward(reward);
        dataOutputStream.writeObject(data);
        dataOutputStream.flush();
        GameUtils.print(TAG, "Sending data *** done:", done, "Reward:", reward);
        //resetReward();
//            dataOutputStream.writeInt(state.length);
//            dataOutputStream.write(state);
//            dataOutputStream.flush();

    }

    public void stop() throws Exception {
        if (dataOutputStream != null) {
            dataOutputStream.writeInt(-1);
            dataOutputStream.flush();
        }
    }

    private void takeScreenShot() {

        if (canSend) {
            byte[] pixels = ScreenUtils.getFrameBufferPixels(true);
            GameUtils.print(TAG, "Pixels length:" , pixels.length);
            int no_alpha_size = pixels.length*3/4;
            byte[] no_alpha = new byte[no_alpha_size];
            GameUtils.print(TAG, "No alpha length:" , no_alpha.length);

            // this loop makes sure the whole screenshot is opaque and looks exactly like what the user is seeing

            for (int i = 4; i < pixels.length; i += 4) {
                pixels[i - 1] = (byte) 255;

            }
            int alpha_index = 0;
            for (int i=0; i <pixels.length; i++) {

                if ((i + 1)%4 == 0) {
                    continue;
                }

                no_alpha[alpha_index] = pixels[i];
                alpha_index ++;
            }
            GameUtils.print(TAG, "Alpha", alpha_index);

            if (frames == null) {
                frames = new ArrayList<>(Const.STATE_FRAME_SIZE);
            }

            frames.add(pixels);

            if (frames.size() == Const.STATE_FRAME_SIZE) {
                try {
                    sendState();
                    //GameUtils.print(TAG, "State was send.");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    frames.clear();
                }
            }



            //System.out.println("Pixels size: " + pixels.length);

        Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), Pixmap.Format.RGB888);

        BufferUtils.copy(no_alpha, 0, pixmap.getPixels(), no_alpha.length);
        PixmapIO.writePNG(new FileHandle(Gdx.files.getLocalStoragePath() + "mynoalpha.png"), pixmap);
        pixmap.dispose();
        }

    }

    public boolean needMoreTime() {
        return frames != null && frames.size() > 0 && frames.size()< Const.STATE_FRAME_SIZE;
    }

    public void addToReward(int amount) {
        reward += amount;
        GameUtils.print(TAG, "Reward: ", reward);
    }

    public void setReward(int value) {
        reward = value;
        GameUtils.print(TAG, "Setting reward. Reward:", reward);
    }



    private static class InputHandler implements Runnable {
        private Socket client;
        private ObjectOutputStream objectOutputStream;

        public InputHandler(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            int protocolCode = 0;
            byte lastAction = 0x00;
            boolean transmissionOn = true;
            try {
                DataInputStream dis = new DataInputStream(client.getInputStream());
                while (transmissionOn) {

                    protocolCode = dis.readInt();

                    if (protocolCode == PROTOCOL_READ_ACTION) {
                        //GameUtils.print(TAG, "State delivered.");
                        //GameUtils.print(TAG, "Receiving action...");
                        lastAction = dis.readByte();
                        //GameUtils.print(TAG, "Last action: ", lastAction);
                        GameManager.getInstance().updatePaddleInput(lastAction);


                    } else if (protocolCode == PROTOCOL_DISCONNECT) {
                        GameUtils.print(TAG, "Everything ok. End of transmission.");
                        transmissionOn = false;
                        client.getOutputStream().close();
                    } else if (protocolCode == PROTOCOL_NEW_STATE) {
                        canSend = true;
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static final int PROTOCOL_NEW_STATE = 205;
    private static final int PROTOCOL_READ_ACTION = 200;
    private static final int PROTOCOL_DISCONNECT = 202;


}
