package com.wilco.opesui.mapmyplan;

/**
 * Created by vijayarajan on 7/4/2018.
 */

public class ServerEvent {


    private QuesAnsModel.DataResult quesAnsModel;

    public ServerEvent(QuesAnsModel.DataResult quesAnsModel) {
        this.quesAnsModel = quesAnsModel;
    }

    public QuesAnsModel.DataResult getQuesAnsModel() {
        return quesAnsModel;
    }

    public void setQuesAnsModel(QuesAnsModel.DataResult quesAnsModel) {
        this.quesAnsModel = quesAnsModel;
    }

}
