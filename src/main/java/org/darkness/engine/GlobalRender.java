package org.darkness.engine;

import lombok.Getter;
import org.darkness.engine.logs.Logs;
import org.darkness.engine.models.Model;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GlobalRender {
    private final List<Model> modelList = new ArrayList<>();

    public void load(Model model){
        modelList.add(model);
        model.setId(modelList.size()-1);

        Logs.makeInfoLog("Model " + model + " loaded successfully");
    }

    public void renderAll(){
        modelList.forEach(Model::render);
    }

    public void clear(){
        modelList.clear();
    }
}
