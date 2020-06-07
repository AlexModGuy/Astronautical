package com.github.alexthe666.astro.client.model;

import com.github.alexthe666.astro.client.model.animation.GlopepodAnimator;
import com.github.alexthe666.astro.client.model.animation.SpaceSquidAnimator;
import com.github.alexthe666.astro.client.model.animation.StarchovyAnimator;
import com.github.alexthe666.citadel.client.model.ITabulaModelAnimator;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.github.alexthe666.citadel.client.model.TabulaModelHandler;

import javax.annotation.Nullable;
import java.io.IOException;

public class TabulaModels {
    public static TabulaModel SPACE_SQUID;
    public static TabulaModel SPACE_SQUID_INJURED;
    public static TabulaModel SPACE_SQUID_FALLING;
    public static TabulaModel STARCHOVY;
    public static TabulaModel GLOPEPOD;
    public static TabulaModel STARON;

    public static void loadAll(){
        SPACE_SQUID = loadModel("space_squid", new SpaceSquidAnimator());
        SPACE_SQUID_INJURED = loadModel("space_squid_injured", null);
        SPACE_SQUID_FALLING = loadModel("space_squid_falling", null);
        STARCHOVY = loadModel("starchovy", new StarchovyAnimator());
        GLOPEPOD = loadModel("glopepod", new GlopepodAnimator());
        STARON = loadModel("staron", new StaronAnimator());
    }

    private static TabulaModel loadModel(String name, @Nullable ITabulaModelAnimator animator){
        try {
            return new TabulaModel(TabulaModelHandler.INSTANCE.loadTabulaModel("/assets/astro/models/tabula/" + name), animator);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
