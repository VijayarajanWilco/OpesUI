package com.wilco.opesui.mapmyplan;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by vijayarajan on 7/4/2018.
 */

public interface RetroInterface {

    @GET(".json")
    Call<QuesAnsModel.DataResult> getQuesAns();
}
