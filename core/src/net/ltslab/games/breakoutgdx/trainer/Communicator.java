/*
 * Copyright (c) 2019. BLoo
 */

package net.ltslab.games.breakoutgdx.trainer;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import net.ltslab.games.breakoutgdx.management.GameManager;
import net.ltslab.games.breakoutgdx.util.Const;
import net.ltslab.games.breakoutgdx.util.GameUtils;
import java.util.concurrent.Executors;

import static spark.Spark.*;

public class Communicator{

    private final static String TAG = Communicator.class.getSimpleName();

    public Communicator(){
        this(Const.DEFAULT_PORT);
    }

    public Communicator(int port) {

        Executors.newSingleThreadExecutor().execute(new StepListener());

    }

    private StepData updatedData() {
        StepData data = new StepData();
        data.setLastFrame(GameManager.getInstance().getScreenSnapshot());
        data.setReward(GameManager.getInstance().getReward());
        data.setDone(GameManager.getInstance().isGameOver());
        return data;
    }

    private class StepListener implements  Runnable {
        private Gson gson;
        public StepListener () {
            gson = new Gson();
        }
    @Override
    public void run() {
        get("/hello", (req, res) -> "Hello World");
        get("/step/:action", (request, response) -> {
            int action = Integer.parseInt(request.params(":action"));
            GameManager.getInstance().updatePaddleInput(action);
            //GameUtils.print(TAG, "Action received:", action );
            response.status(200);
            response.type("application/json");
            return gson.toJson(updatedData());
        });
        get("/reset", (request, response) -> {


            GameUtils.print(TAG, "Reset");
            response.status(200);
            response.type("application/json");
            StepData data = updatedData();
            GameManager.getInstance().getGame().start();
            return gson.toJson(data);
        });
    }
}
}
