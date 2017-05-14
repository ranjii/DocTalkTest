package com.doctalktest.data;

import com.doctalktest.models.IssuesModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by rajesh on 29/4/17.
 */

public interface RemoteRetrofitInterfaces {


    @GET("repos/{name}/{repo}/issues")
    Observable<List<IssuesModel>> getIssues(@Path("name") String name, @Path("repo") String repo);
}
