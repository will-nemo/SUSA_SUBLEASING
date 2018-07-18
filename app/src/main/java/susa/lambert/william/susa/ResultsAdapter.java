package susa.lambert.william.susa;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.MyViewHolder>  {

    private ResultsAdapterListener listener;
    private Context context;
    private List<Results> resultList;
    private List<Results> resultListFiltered;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView sName;

        public MyViewHolder(View view) {
            super(view);
            sName = view.findViewById(R.id.school_name);

       /*     view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                   listener.onSchoolSelected(resultListFiltered.get(getAdapterPosition()));
                }
            });
        */
        }

    }


    public ResultsAdapter(Context context, List<Results> resultList, ResultsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.resultList = resultList;
        this.resultListFiltered = resultList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.school_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Results result = resultListFiltered.get(position);
        School temp = result.getSchool();
        holder.sName.setText(temp.getName());

        holder.sName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send selected contact in callback
                listener.onSchoolSelected(resultListFiltered.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return resultListFiltered.size();
    }



    public interface ResultsAdapterListener {
        void onSchoolSelected(Results result);
    }



}