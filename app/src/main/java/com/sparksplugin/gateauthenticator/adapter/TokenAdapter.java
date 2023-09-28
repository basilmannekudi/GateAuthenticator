package com.sparksplugin.gateauthenticator.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.sparksplugin.gateauthenticator.R;
import com.sparksplugin.gateauthenticator.activity.TokenActivity;
import com.sparksplugin.gateauthenticator.model.Tokens;
import java.util.List;

public class TokenAdapter extends RecyclerView.Adapter<TokenAdapter.ViewHolder> {

    private Context mContext;
    private List<Tokens> mTokens;

    public TokenAdapter(Context mContext, List<Tokens> mTokens){
        this.mTokens = mTokens;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(mContext).inflate(R.layout.token_item, parent, false);
        return new TokenAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Tokens token = mTokens.get(position);
        holder.mtype.setText(token.getmType());
        holder.date.setText(token.getZdate());
        holder.time.setText(token.getZtime());
        holder.tokId.setText(token.getId());
        holder.verif.setText(token.getSupername());
       //
        if(token.isVerified()) {
            if (!token.isExpired()) {
                holder.Stats.setText("Verified");
                holder.veri_im.setImageResource(R.drawable.ic_baseline_verified_24);
            }
        }
        else
        {   holder.Stats.setText("None");
            holder.veri_im.setImageResource(R.drawable.ic_baseline_not_interested_24);
        }
        if (token.isExpired())
        {   holder.Stats.setText("Expired");
            holder.veri_im.setImageResource(R.drawable.ic_baseline_delete_forever_24);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Loading", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext,TokenActivity.class);
                intent.putExtra("key", token.getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mTokens.size();
    }
    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mtype;
        public TextView date;
        public TextView time;
        public TextView verif;
        public TextView tokId;
        public TextView Stats;
        public ImageView veri_im;
        CardView bgLayout;


        public ViewHolder(View itemView) {
            super(itemView);

            mtype = itemView.findViewById(R.id.entry_exit);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time_time);
            verif = itemView.findViewById(R.id.verified_by);
            tokId = itemView.findViewById(R.id.token_id);
            veri_im = itemView.findViewById(R.id.verified_image);
            bgLayout = itemView.findViewById(R.id.token_item_bg);
            Stats = itemView.findViewById(R.id.stats);


        }
    }
}
