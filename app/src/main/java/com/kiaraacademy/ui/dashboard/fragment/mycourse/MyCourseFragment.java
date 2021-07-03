package com.kiaraacademy.ui.dashboard.fragment.mycourse;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.kiaraacademy.R;
import com.kiaraacademy.data.models.Chapters;
import com.kiaraacademy.data.response.ResponseChapterwiseCourse;
import com.kiaraacademy.data.response.ResponseMainCourse;
import com.kiaraacademy.data.response.ResponseMyCourse;
import com.kiaraacademy.ui.allcourses.AllCourseActivity;
import com.kiaraacademy.ui.chapters.ChapterwiseCourseActivity;
import com.kiaraacademy.ui.dashboard.DashboardPresenter;
import com.kiaraacademy.ui.dashboard.DashboardView;
import com.kiaraacademy.utils.AppConstants;
import com.kiaraacademy.utils.AppUtils;

import java.util.ArrayList;

public class MyCourseFragment extends Fragment implements DashboardView {

    private RecyclerView myCourse;
    private ProgressDialog progressDialog;
    private DashboardPresenter mPresenter;
    private TextView tv_no_data_availble;
    private AppCompatImageButton searchView;
    private MyCourseAdapter myCourseAdapter;
    private ArrayList<ResponseMainCourse> list = new ArrayList<>();
    private String courseName = null, courseId = null;
    private ResponseMainCourse course;

    public MyCourseFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_withlist, container, false);

        initializeViews(view);
        addListeners();
        attachPresenter();
        getMyCourses();

        return view;
    }

    private void addListeners() {
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AllCourseActivity.class);
                intent.putExtra("from", "MyCourseFragment");
                startActivity(intent);
            }
        });
    }

    private void filter(String text) {
        ArrayList<ResponseMainCourse> filterdNames = new ArrayList<>();
        for (ResponseMainCourse mainCourse : list) {
            if (mainCourse.getCourse_name().toLowerCase().contains(text.toLowerCase())) {
                filterdNames.add(mainCourse);
            }
        }
        myCourseAdapter.filterList(filterdNames);
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
            case AppConstants.RequestConstants.REQUEST_MY_COURSE:
                if (object instanceof ResponseMyCourse) {
                    ResponseMyCourse responseCourse = (ResponseMyCourse) object;
                    if (responseCourse.code.equalsIgnoreCase(AppConstants.VolleyConstants.CODE_SUCCESS)) {
                        list = responseCourse.data.getMainCourses();
                        populateCourseData(list);
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

    private void navigateToChapterwiseCourse(ArrayList<Chapters> chapters) {
        Intent intent = new Intent(getActivity(), ChapterwiseCourseActivity.class);
        intent.putExtra("chapters", chapters);
        intent.putExtra("course", course);
        intent.putExtra("from", "MyCourseFragment");
        getActivity().startActivity(intent);
    }

    private void getMyCourses() {
        mPresenter.getMyCourses();
    }

    private void populateCourseData(ArrayList<ResponseMainCourse> response) {
        if (response != null && !response.isEmpty()) {
            tv_no_data_availble.setVisibility(View.GONE);
            myCourse.setVisibility(View.VISIBLE);

            myCourseAdapter = new MyCourseAdapter(getActivity(), response);
            myCourse.setAdapter(myCourseAdapter);
        } else {
            tv_no_data_availble.setVisibility(View.VISIBLE);
            myCourse.setVisibility(View.GONE);
        }
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

    public class MyCourseAdapter extends RecyclerView.Adapter<MyCourseAdapter.Holder> {
        private ArrayList<ResponseMainCourse> mainCourses;
        private Context context;

        public MyCourseAdapter(Context context, ArrayList<ResponseMainCourse> mainCourses) {
            this.context = context;
            this.mainCourses = mainCourses;
        }

        @NonNull
        @Override
        public MyCourseAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            return new MyCourseAdapter.Holder(LayoutInflater.from(context).inflate(R.layout.layout_my_course,
                    viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final MyCourseAdapter.Holder viewHolder, final int position) {
            if (mainCourses != null && !mainCourses.isEmpty()) {
                final ResponseMainCourse mainCourse = mainCourses.get(position);

                viewHolder.tvCourseName.setText(mainCourse.getCourse_name());
                viewHolder.tvAuthorName.setText(mainCourse.getAuthor());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        course = mainCourse;
                        mPresenter.getChapterwiseCourse(course.getId());
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
            private AppCompatImageButton btnStartLearning;

            Holder(@NonNull View itemView) {
                super(itemView);
                tvCourseName = itemView.findViewById(R.id.tv_course_name);
                tvAuthorName = itemView.findViewById(R.id.tv_author_name);
                btnStartLearning = itemView.findViewById(R.id.btn_start_learning);
            }
        }
    }
}