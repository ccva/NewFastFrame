package com.example.cootek.newfastframe;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.example.commonlibrary.baseadapter.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.RecyclerFooterViewClickListener;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.mvp.BaseFragment;
import com.example.cootek.newfastframe.dagger.DaggerMainFragmentComponent;
import com.example.cootek.newfastframe.dagger.MainFragmentModule;
import com.example.cootek.newfastframe.mvp.MainPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by COOTEK on 2017/8/13.
 */

public class MainFragment extends BaseFragment<List<Music>, MainPresenter> implements OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.srcv_fragment_main_display)
    SuperRecyclerView display;
    @BindView(R.id.refresh_fragment_main_refresh)
    SwipeRefreshLayout refresh;
    private LoadMoreFooterView loadMoreFooterView;
    @Inject
    MainAdapter mainAdapter;

    @Override
    public void updateData(List<Music> musics) {
        mainAdapter.addData(musics);
    }

    @Override
    protected boolean isNeedHeadLayout() {
        return false;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return true;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView() {
        display.setLayoutManager(new LinearLayoutManager(getContext()));
        loadMoreFooterView = new LoadMoreFooterView(getContext());
        display.setLoadMoreFooterView(loadMoreFooterView);
        display.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
    }


    @Override
    protected void initData() {
        DaggerMainFragmentComponent.builder().mainComponent(MainApplication.getMainComponent()).mainFragmentModule(new MainFragmentModule(this)).build().inject(this);
        mainAdapter.setEnableAnimator(true);
        mainAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Toast.makeText(getActivity(), "position:" + position, Toast.LENGTH_SHORT).show();
                MusicManager.getInstance().play(getActivity(), mainAdapter.getIds(), position, -1, MusicIdType.NORMAL, false);
            }
        });
        display.setIAdapter(mainAdapter);
    }

    @Override
    protected void updateView() {
        presenter.getAllMusic(true, true);
    }


    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void loadMore() {
        presenter.getAllMusic(false, false);
    }

    @Override
    public void onRefresh() {
        presenter.getAllMusic(true, false);
    }
}
