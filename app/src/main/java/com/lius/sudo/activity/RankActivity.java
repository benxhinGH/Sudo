package com.lius.sudo.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lius.sudo.Dialog.LDialog;
import com.lius.sudo.R;
import com.lius.sudo.adapter.RankLvAdapter;
import com.lius.sudo.model.RankItemData;
import com.lius.sudo.DB.DBUtil;
import com.lius.sudo.tools.OtherUtil;

import java.util.List;

/**
 * Created by 刘有泽 on 2016/8/17.
 */
public class RankActivity extends AppCompatActivity{
    private ListView rankLv;
    private List<RankItemData> rankItemDatas;
    private RankLvAdapter rankLvAdapter;
    private Button backBtn;
    private TextView levelTv;
    private String tempStr="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.rank_acitivty_layout);
        rankLv=(ListView)findViewById(R.id.rank_listview);
        rankItemDatas= DBUtil.getRankItemDataFromDB(this, OtherUtil.getStringLevel());
        rankLvAdapter=new RankLvAdapter(rankItemDatas,this);
        rankLv.setAdapter(rankLvAdapter);
        backBtn=(Button)findViewById(R.id.back_button);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        levelTv=(TextView)findViewById(R.id.rank_level_tv);
        levelTv.setText(OtherUtil.getStringLevel());
        rankLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                LDialog mLDialog=new LDialog(RankActivity.this,2);
                mLDialog.setTempBtnListener(new LDialog.TempBtnListener() {
                    @Override
                    public void onClick(String str) {
                        tempStr=str;
                    }
                });
                mLDialog.show();
                mLDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if(tempStr.equals("confirm")){
                            DBUtil.getDatabase(RankActivity.this).delete("Rank",null,null);
                            rankItemDatas.clear();
                            rankLvAdapter.notifyDataSetChanged();
                            Toast.makeText(RankActivity.this,"已清空",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                return false;
            }
        });

    }
}
