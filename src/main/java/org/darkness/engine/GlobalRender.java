package org.darkness.engine;

import org.darkness.engine.camera.Camera;
import org.darkness.engine.logs.Logs;
import org.darkness.engine.models.Model;
import org.darkness.engine.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class GlobalRender {
    private final List<Model> modelList = new ArrayList<>();
    private Camera camera;

    public GlobalRender(Camera camera) {
        this.camera = camera;
    }

    public void load(Model model){
        modelList.add(model);

        Logs.makeInfoLog("Model " + model + " loaded successfully");
    }

    public void renderAll(){
        modelList.forEach(Model::render);
    }

    public List<Model> getModelList() {
        return modelList;
    }

    public void clear(){
        modelList.clear();
    }
}
