package com.kiaraacademy.ui.dashboard.fragment.withlist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.kiaraacademy.R;
import com.kiaraacademy.data.SharedPreferenceManager;
import com.kiaraacademy.data.models.Chapters;
import com.kiaraacademy.data.response.BaseResponse;
import com.kiaraacademy.data.response.ResponseChapterwiseCourse;
import com.kiaraacademy.data.response.ResponseMainCourse;
import com.kiaraacademy.data.response.ResponseSavedCourse;
import com.kiaraacademy.ui.PaymentActivity;
import com.kiaraacademy.ui.chapters.ChapterwiseCourseActivity;
import com.kiaraacademy.ui.dashboard.DashboardPresenter;
import com.kiaraacademy.ui.dashboard.DashboardView;
import com.kiaraacademy.utils.AppConstants;
import com.kiaraacademy.utils.AppUtils;
import com.kiaraacademy.utils.ConfimationListener;

import java.util.ArrayList;

public class WithlistFragment extends Fragment implements DashboardView {

    private RecyclerView myCourse;
    private ProgressDialog progressDialog;
    private DashboardPresenter mPresenter;
    private SavedCourseAdapter savedCourseAdapter;
    private TextView tv_no_data_availble;
    private SearchView searchView;
    private ArrayList<ResponseMainCourse> list = new ArrayList<>();
    private ResponseMainCourse course;

    public WithlistFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        initializeViews(view);
        addListeners();
        attachPresenter();
        getSavedCourses();

        return view;
    }

    private void addListeners() {
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                filter(newText);
//                return true;
//            }
//        });
    }

    private void filter(String text) {
        ArrayList<ResponseMainCourse> filterdNames = new ArrayList<>();
        for (ResponseMainCourse mainCourse : list) {
            if (mainCourse.getCourse_name().toLowerCase().contains(text.toLowerCase())) {
                filterdNames.add(mainCourse);
            }
        }
        savedCourseAdapter.filterList(filterdNames);
    }

    private void initializeViews(View view) {
        searchView = view.findViewById(R.id.search_view);
        tv_no_data_availble = view.findViewById(R.id.tv_no_data_availble);
        myCourse = view.findViewById(R.id.rv_my_course);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        myCourse.setLayoutManager(linearLayoutManager);
    }

    private void attachPresenter() {
        mPresenter = new DashboardPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onSuccess(Object object, Integer tag) {
        switch (tag) {
            case AppConstants.RequestConstants.REQUEST_SAVE_UNSAVE_COURSE:
                if (object instanceof BaseResponse) {
                    BaseResponse responseCourse = (BaseResponse) object;
                    if (responseCourse.code.equalsIgnoreCase(AppConstants.VolleyConstants.CODE_SUCCESS)) {
                        AppUtils.showToast(getActivity(), responseCourse.msg);
                        mPresenter.getSavedCourses();
                    }
                }
                break;
            case AppConstants.RequestConstants.REQUEST_SAVED_COURSE:
                if (object instanceof ResponseSavedCourse) {
                    ResponseSavedCourse responseCourse = (ResponseSavedCourse) object;
                    if (responseCourse.code.equalsIgnoreCase(AppConstants.VolleyConstants.CODE_SUCCESS)) {
                        list = responseCourse.data.getMainCourses();
                        populateCourseData(responseCourse.data.getMainCourses());
                    }
                }
                break;
            case AppConstants.RequestConstants.REQUEST_CHAPTERWISE_COURSE:
                if (object instanceof ResponseChapterwiseCourse) {
                    ResponseChapterwiseCourse response = (ResponseChapterwiseCourse) object;
                    if (response.code.equalsIgnoreCase(AppConstants.VolleyConstants.CODE_SUCCESS)) {
                        if (response.chapters == null || response.chapters.isEmpty()) {
                            AppUtils.showToast(getActivity(), "No chapters found");
                            return;
                        }
                        navigateToChapterwiseCourse(response.chapters);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void getSavedCourses() {
        mPresenter.getSavedCourses();
    }

    private void populateCourseData(ArrayList<ResponseMainCourse> response) {
        if (response != null && !response.isEmpty()) {
            tv_no_data_availble.setVisibility(View.GONE);
            myCourse.setVisibility(View.VISIBLE);

            savedCourseAdapter = new SavedCourseAdapter(getActivity(), response);
            myCourse.setAdapter(savedCourseAdapter);

        } else {
            tv_no_data_availble.setVisibility(View.VISIBLE);
            myCourse.setVisibility(View.GONE);
        }
    }

    private void navigateToChapterwiseCourse(ArrayList<Chapters> chapters) {
        Intent intent = new Intent(getActivity(), ChapterwiseCourseActivity.class);
        intent.putExtra("chapters", chapters);
        intent.putExtra("course", course);
        intent.putExtra("from", "MyCourseFragment");
        getActivity().startActivity(intent);
    }

    @Override
    public void onError(Object object, Integer tag) {
        if (object instanceof VolleyError) {
            VolleyError volleyError = (VolleyError) object;
            String strMsg = volleyError.getMessage() != null ? volleyError.getMessage() : "";
            if (!TextUtils.isEmpty(strMsg) && strMsg.split(">").length > 1) {
                String msg = strMsg.split(">")[1];
                AppUtils.showToast(getActivity(), msg);
            } else
                AppUtils.showErrorToast(getActivity());
        }
    }

    @Override
    public void showProgress(String message) {
        if (!getActivity().isFinishing())
            progressDialog = AppUtils.generateProgressDialog(getActivity(), R.string.empty_title, message,
                    false, false);
    }

    @Override
    public void hideProgress() {
        AppUtils.dismissProgressDialog(progressDialog);
    }

    @Override
    public void showNetworkDialog(Integer tag) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    public class SavedCourseAdapter extends RecyclerView.Adapter<SavedCourseAdapter.Holder> {
        private ArrayList<ResponseMainCourse> mainCourses;
        private Context context;

        public SavedCourseAdapter(Context context, ArrayList<ResponseMainCourse> mainCourses) {
            this.context = context;
            this.mainCourses = mainCourses;
        }

        @NonNull
        @Override
        public SavedCourseAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            return new SavedCourseAdapter.Holder(LayoutInflater.from(context).inflate(R.layout.layout_withlist,
                    viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final SavedCourseAdapter.Holder viewHolder, final int position) {
            if (mainCourses != null && !mainCourses.isEmpty()) {
                final ResponseMainCourse mainCourse = mainCourses.get(position);
                viewHolder.tvCourseName.setText(mainCourse.getCourse_name());
                viewHolder.tvAuthorName.setText(mainCourse.getAuthor());
                if (mainCourse.getCoursePrice().equals("0"))
                    viewHolder.btnBuy.setText("Free");

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        course = mainCourse;
                        if (!course.getCoursePrice().equals("0"))
                            return;
                        mPresenter.getChapterwiseCourse(course.getId());
                    }
                });

                viewHolder.btnBuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mainCourse.getCoursePrice().equals("0")) {
                            course = mainCourse;
                            mPresenter.getChapterwiseCourse(course.getId());
                        } else {
                            AppUtils.showConfirmationDialog("Are you sure to purchase this product ?", getActivity(), new ConfimationListener() {
                                @Override
                                public void onOkClick() {
                                    Intent intent = new Intent(getActivity(), PaymentActivity.class);
                                    intent.putExtra("course", mainCourse);
                                    startActivity(intent);
                                }

                                @Override
                                public void onCancelCLick() {

                                }
                            });
                        }
                    }
                });

                viewHolder.ibRemoveFromCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AppUtils.showConfirmationDialog("Are you sure to remove this product from cart?", getActivity(), new ConfimationListener() {
                            @Override
                            public void onOkClick() {
                                mPresenter.saveUnsaveCourses(AppConstants.MainConstants.IS_UNSAVE, mainCourse.getId(), SharedPreferenceManager.getUserId());
                            }

                            @Override
                            public void onCancelCLick() {

                            }
                        });
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mainCourses.size();
        }

        public void filterList(ArrayList<ResponseMainCourse> filterdNames) {
            mainCourses = filterdNames;
            notifyDataSetChanged();
        }

        class Holder extends RecyclerView.ViewHolder {
            private TextView tvCourseName;
            private TextView tvCourseDesc;
            private TextView tvAuthorName;
            private ImageButton ibRemoveFromCart;
            private AppCompatTextView btnBuy;

            Holder(@NonNull View itemView) {
                super(itemView);
                tvCourseName = itemView.findViewById(R.id.tv_course_name);
                tvAuthorName = itemView.findViewById(R.id.tv_author_name);
                btnBuy = itemView.findViewById(R.id.btn_buy);
                ibRemoveFromCart = itemView.findViewById(R.id.btn_dislike);

            }
        }
    }
}
