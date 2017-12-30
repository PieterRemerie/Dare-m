package be.nmct.howest.darem;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import be.nmct.howest.darem.database.CategoriesData;
import be.nmct.howest.darem.Loader.CategoryLoader;
import be.nmct.howest.darem.database.Contract;


public class AddCategoryToChallengeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private RecyclerView recyclerViewAddCategory;
    private AddCategoryRecycleViewAdapter addCategoryRecycleViewAdapter;
    private Bundle bundle;

    public AddCategoryToChallengeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_add_category_to_challenge, container, false);

        bundle = getArguments();

        recyclerViewAddCategory = (RecyclerView) v.findViewById(R.id.add_category_to_challenge);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        getLoaderManager().initLoader(0, null, this);
        recyclerViewAddCategory.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CategoryLoader(this.getContext());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        addCategoryRecycleViewAdapter = new AddCategoryRecycleViewAdapter(cursor);
        recyclerViewAddCategory.setAdapter(addCategoryRecycleViewAdapter);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public class AddCategoryRecycleViewAdapter extends RecyclerView.Adapter<AddCategoryToChallengeFragment.AddCategoryViewHolder>{

        Cursor mCursorAddCategory;

        public AddCategoryRecycleViewAdapter(Cursor cursor) {
            this.mCursorAddCategory = cursor;
        }

        @Override
        public AddCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_add_category, parent, false);
            return new AddCategoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(AddCategoryViewHolder holder, final int position) {
            mCursorAddCategory.moveToPosition(position);

            int colnr1 = mCursorAddCategory.getColumnIndex(Contract.CategoryColumns.COLUMN_CATEGORY_NAME);

            holder.txtAddCategoryName.setText(mCursorAddCategory.getString(colnr1));

            holder.imgCategoryImage.setImageResource(CategoriesData.imgIds[position]);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    showCreateChallengeFragment(position);

                }
            });

        }

        @Override
        public int getItemCount() {
            return mCursorAddCategory.getCount();
        }
    }


    public class AddCategoryViewHolder extends RecyclerView.ViewHolder {

        public final TextView txtAddCategoryName;
        public final ImageView imgCategoryImage;
        public final Button btnAddCategory;

        public AddCategoryViewHolder(View v) {
            super(v);

            txtAddCategoryName = (TextView) v.findViewById(R.id.txtAddCategoryName);
            imgCategoryImage  = (ImageView) v.findViewById(R.id.addCategoryImage);
            btnAddCategory = (Button) v.findViewById(R.id.btnAddCategory);

        }
    }

    public void showCreateChallengeFragment(int categoryID){

        FragmentManager fragmentManager = getFragmentManager();
        Bundle args = new Bundle();
        args.putInt("categoryId", categoryID);
        if(bundle.getString("challengeName" )!= null){
            args.putString("challengeName",bundle.getString("challengeName" ));
        }
        if(bundle.getString("challengeDescr" )!= null){
            args.putString("challengeDescr",bundle.getString("challengeDescr" ));
        }
        if(bundle.getString("challengeDate" )!= null){
            args.putString("challengeDate",bundle.getString("challengeDate" ));
        }
        args.putStringArrayList("key", bundle.getStringArrayList("friends"));

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CreateChallengeFragment createChallengeFragment = new CreateChallengeFragment();
        createChallengeFragment.setArguments(args);
        fragmentTransaction.replace(R.id.framelayout_in_create_challenge_activity, createChallengeFragment);
        fragmentTransaction.commit();
    }

}
