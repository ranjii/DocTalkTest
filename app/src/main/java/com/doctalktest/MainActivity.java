package com.doctalktest;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.doctalktest.adapters.ListAdapter;
import com.doctalktest.interfaces.RecyclersOnItemClickListener;
import com.doctalktest.models.IssuesModel;

import com.doctalktest.data.RemoteRetrofitInterfaces;
import com.doctalktest.utils.ConnectionDetector;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements RecyclersOnItemClickListener{

    @BindView(R.id.search_edittext)
    EditText searchEditText;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private ListAdapter adapter;
    List<IssuesModel> issueList = new ArrayList<>();
    private ConnectionDetector connectionDetector;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        connectionDetector = new ConnectionDetector(this);

        adapter = new ListAdapter(this, this, issueList);


        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);

    }

    @OnClick(R.id.search_icon)
    public void search(View view) {


        if (connectionDetector.isConnectingToInternet()) {


            String repoName = searchEditText.getText().toString();



            if(repoName.equals("")){
                Toast.makeText(this, "Please enter name and repo in format name/repo", Toast.LENGTH_SHORT).show();

            }else {

                if (repoName.contains("/")) {

//                    String str = "square/retrofit";

                    String splitter = "/";
                    Pattern p = Pattern.compile("(.*?)" + splitter + "(.*)");
                    Matcher m = p.matcher(repoName);
                    if (m.matches()) {
                        String firstSubString = m.group(1);
                        String secondSubString = m.group(2);

                        issueList.clear();
                        mRecyclerView.getRecycledViewPool().clear();
                        adapter.notifyDataSetChanged();

                        MainApplication peopleApplication = MainApplication.create(this);
                        RemoteRetrofitInterfaces peopleService = peopleApplication.getPeopleService();

                        Disposable disposable = peopleService.getIssues(firstSubString, secondSubString)
                                .subscribeOn(peopleApplication.subscribeScheduler())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<List<IssuesModel>>() {
                                    @Override
                                    public void accept(List<IssuesModel> issueModels) throws Exception {


                                        issueList.addAll(issueModels);
                                        adapter.notifyDataSetChanged();

                                        InputMethodManager inputMethodManager = (InputMethodManager)
                                                getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        inputMethodManager.hideSoftInputFromWindow(
                                                getCurrentFocus().getWindowToken(),
                                                0
                                        );

                                    }

                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {

                                        InputMethodManager inputMethodManager = (InputMethodManager)
                                                getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        inputMethodManager.hideSoftInputFromWindow(
                                                getCurrentFocus().getWindowToken(),
                                                0
                                        );
                                        Toast.makeText(MainActivity.this, "repository entered in not correct", Toast.LENGTH_SHORT).show();

                                    }
                                });

                        compositeDisposable.add(disposable);


                    } else {
                        // splitter not found in str

                        Toast.makeText(this, "Please enter name and repo in format name/repo", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this, "Please enter name and repo in format name/repo", Toast.LENGTH_SHORT).show();
                }



            }
        } else {
            displayAlert();
        }

    }

    @Override
    public void onItemClick(int position, View v) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }
}
