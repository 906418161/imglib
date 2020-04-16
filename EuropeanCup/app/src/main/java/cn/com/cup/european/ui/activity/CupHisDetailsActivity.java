package cn.com.cup.european.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.cup.european.R;
import cn.com.cup.european.bean.HomeCupBean;
import cn.com.cup.european.ui.adapter.CupHisDetailsAdapter;
import cn.com.cup.european.ui.adapter.HomeCupAdapter;
import cn.com.cup.european.util.base.BaseActivity;
import cn.com.cup.european.util.http.HttpModel;
import cn.com.imageselect.ImageSelectUtil;
import cn.com.imageselect.util.http.RequestCallbackListener;

public class CupHisDetailsActivity extends BaseActivity implements RequestCallbackListener {
    @BindView(R.id.tv_title_name)
    TextView title;
    @BindView(R.id.hisdetails_title)
    TextView hTitle;
    @BindView(R.id.hisdetails_content)
    TextView content;
    @BindView(R.id.hisdetails_bestplayer)
    TextView bestplayer;
    @BindView(R.id.hisdetails_bestshooter)
    TextView bestshooter;
    @BindView(R.id.hisdetails_champion)
    TextView champion;
    @BindView(R.id.hisdetails_stitle)
    TextView sTitle;
    @BindView(R.id.hisdetails_simg)
    ImageView simg;
    @BindView(R.id.hisdetails_scontent)
    TextView scontent;
    @BindView(R.id.hisdetails_finals)
    TextView finals;
    @BindView(R.id.hisdetails_semifinal)
    TextView semifinal;
    @BindView(R.id.hisdetails_quartermatch)
    TextView quartermatch;
    @BindView(R.id.hisdetails_dtitle)
    TextView dtitle;
    @BindView(R.id.hisdetails_dimg)
    ImageView dimg;
    @BindView(R.id.hisdetails_dcontent)
    TextView dcontent;
    @BindView(R.id.hisdetails_poster)
    ImageView poster;
    @BindView(R.id.hisdetails_mascot)
    ImageView mascot;
    @BindView(R.id.hisdetails_gridview)
    GridView gridView;
    CupHisDetailsAdapter adapter;
    List<String> beans;
    private String id;
    HttpModel httpModel = new HttpModel(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuphisdetails);
        ButterKnife.bind(this);
        hideStatueBar(0);
        loadView();
        loadData();
    }

    @Override
    protected void loadView() {
        id = getIntent().getStringExtra("id");
        getStatusBarHeight(findViewById(R.id.toplinear));
        title.setText("欧洲杯详情");
    }

    @Override
    protected void loadData() {
        httpModel.getEurocupDetails(id, 10001);
        httpModel.getEurocupImg(id, 10002);
    }


    @Override
    public void doResult(String result, int action) {
        try {
            dismissProgressDialog();
            JSONObject object = new JSONObject(result);
            if (object.getString("code").equals("1")) {
                if (action == 10001) {
                    object = object.getJSONObject("data");
                    hTitle.setText(object.getString("summarizetitle"));
                    content.setText(object.getString("summarizecontent"));
                    bestplayer.setText(object.getString("bestplayers"));
                    bestshooter.setText(object.getString("beststriker"));
                    champion.setText(object.getString("champion"));
                    sTitle.setText(object.getString("historytitle"));
                    ImageSelectUtil.showImg(simg, object.getString("historyimg"));
                    scontent.setText(Html.fromHtml(object.getString("historycontent")));
                    finals.setText(object.getString("finals"));
                    semifinal.setText(object.getString("semifinal"));
                    quartermatch.setText(object.getString("quarterfinals"));
                    dtitle.setText(object.getString("squadtitle"));
                    ImageSelectUtil.showImg(dimg, object.getString("squadimg"));
                    dcontent.setText(Html.fromHtml(object.getString("squadcontent")));
                    ImageSelectUtil.showImg(poster, object.getString("posters"));
                    if(!object.getString("mascot").equals("")){
                        ImageSelectUtil.showImg(mascot, object.getString("mascot"));
                    }
                }else if(action==10002){
                    beans = new ArrayList<>();
                    JSONArray array = object.getJSONArray("data");
                    for (int i=0;i<array.length();i++){
                        object = array.getJSONObject(i);
                        beans.add(object.getString("img"));
                    }
                    adapter = new CupHisDetailsAdapter(beans, this);
                    gridView.setAdapter(adapter);
                }
            } else {
                showToast(object.getString("msg"));
            }
        } catch (Exception e) {
            showToast("网络不给力哦");
        }
    }

    @Override
    public void onErr(String errMessage) {
        try {
            dismissProgressDialog();
            showToast("网络不给力哦");
        } catch (Exception e) {

        }
    }
}
