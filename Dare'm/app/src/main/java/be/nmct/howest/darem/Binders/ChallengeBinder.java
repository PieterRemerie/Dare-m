package be.nmct.howest.darem.Binders;

import android.databinding.BindingAdapter;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;

import be.nmct.howest.darem.Adapter.ChallengeAdapter;
import be.nmct.howest.darem.Model.Challenge;

/**
 * Created by Piete_000 on 30/10/2017.
 */

public class ChallengeBinder {

    @BindingAdapter("items")
    public static void setItems(RecyclerView recyclerView, ObservableList<Challenge> challenges){
        if(challenges != null){
            ChallengeAdapter adapter = new ChallengeAdapter(recyclerView.getContext(), challenges);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}
