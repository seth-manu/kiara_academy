package com.kiaraacademy.ui.allcourses;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.kiaraacademy.R;
import com.kiaraacademy.data.models.Chapters;
import com.kiaraacademy.data.response.ResponseChapterwiseCourse;
import com.kiaraacademy.data.response.ResponseCourse;
import com.kiaraacademy.data.response.ResponseMainCourse;
import com.kiaraacademy.ui.chapters.ChapterwiseCourseActivity;
import com.kiaraacademy.utils.AppConstants;
import com.kiaraacademy.utils.AppUtils;

import java.util.ArrayList;

public class AllCourseActivity extends AppCompatActivity implements AllCourseVIew {

    private AppCoursePresenter mPresenter;
    private ProgressDialog progressDialog;
    private RecyclerView rvMyCourse;
    private TextView tv_no_data_availble;
    private SearchView searchView;
    private MyCourseAdapter myCourseAdapter;
    private ArrayList<ResponseMainCourse> list = new ArrayList<>();
    private String courseName = null, courseId = null;
    private boolean isSelectedCourseFree = false;
    private ResponseMainCourse course;

    @Override
    protected void onResume() {
        super.onResume();
        isSelectedCourseFree = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isSelectedCourseFree = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_all_course);

        initComponents();
        attachPresenter();
        addListeners();
        getAllCourses();
    }

    private void getAllCourses() {
        mPresenter.getAllCourses();
    }

    private void attachPresenter() {
        mPresenter = new AppCoursePresenter();
        mPresenter.attachView(this);
    }

    private void initComponents() {
        rvMyCourse = findViewById(R.id.rv_my_course);
        searchView = findViewById(R.id.search_view);
        tv_no_data_availble = findViewById(R.id.tv_no_data_availble);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvMyCourse.setLayoutManager(linearLayoutManager);
    }


    @Override
    public void onSuccess(Object object, Integer tag) {
        if (tag == AppConstants.RequestConstants.REQUEST_GET_ALL_COURSE) {
            if (object instanceof ResponseCourse) {
                ResponseCourse responseCourse = (ResponseCourse) object;
                if (responseCourse.code.equalsIgnoreCase(AppConstants.VolleyConstants.CODE_SUCCESS)) {
                    list = responseCourse.getResponse();
                    populateCourseData(responseCourse.getResponse());
                }
            }
        }
        if (tag == AppConstants.RequestConstants.REQUEST_CHAPTERWISE_COURSE) {
            if (object instanceof ResponseChapterwiseCourse) {
                ResponseChapterwiseCourse response = (ResponseChapterwiseCourse) object;
                if (response.code.equalsIgnoreCase(AppConstants.VolleyConstants.CODE_SUCCESS)) {
                    if (response.chapters == null || response.chapters.isEmpty()) {
                        AppUtils.showToast(this, "No chapters found");
                        return;
                    }
                    navigateToChapterwiseCourse(response.chapters, courseName, courseId);
                }
            }
        }
    }

    private void navigateToChapterwiseCourse(ArrayList<Chapters> chapters, String courseName, String courseId) {
        Intent intent = new Intent(this, ChapterwiseCourseActivity.class);
        intent.putExtra("chapters", chapters);
        intent.putExtra("course", course);
        if (isSelectedCourseFree)
            intent.putExtra("from", "MyCourseFragment");
        startActivity(intent);
    }

    private void populateCourseData(ArrayList<ResponseMainCourse> response) {
        if (response != null && !response.isEmpty()) {
            tv_no_data_availble.setVisibility(View.GONE);
            rvMyCourse.setVisibility(View.VISIBLE);

            myCourseAdapter = new MyCourseAdapter(this, response);
            rvMyCourse.setAdapter(myCourseAdapter);
        } else {
            tv_no_data_availble.setVisibility(View.VISIBLE);
            rvMyCourse.setVisibility(View.GONE);
        }
    }

    private void addListeners() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
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

    @Override
    public void onError(Object object, Integer tag) {
        if (object instanceof VolleyError) {
            VolleyError volleyError = (VolleyError) object;
            String strMsg = volleyError.getMessage() != null ? volleyError.getMessage() : "";
            if (!TextUtils.isEmpty(strMsg) && strMsg.split(">").length > 1) {
                String msg = strMsg.split(">")[1];
                AppUtils.showToast(this, msg);
            } else
                AppUtils.showErrorToast(this);
        }
    }

    @Override
    public void showProgress(String message) {
        if (!isFinishing())
            progressDialog = AppUtils.generateProgressDialog(this, R.string.empty_title, message,
                    false, false);
    }

    @Override
    public void hideProgress() {
        AppUtils.dismissProgressDialog(progressDialog);
    }

    @Override
    public void showNetworkDialog(Integer tag) {
        AppUtils.showAlertToEnableMobileData(this);
    }

    @Override
    protected void onDestroy() {
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
                viewHolder.btnStartLearning.setVisibility(View.GONE);
                viewHolder.tvCourseName.setText(mainCourse.getCourse_name());
                viewHolder.tvAuthorName.setText(mainCourse.getAuthor());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        course = mainCourse;
                        if (course.getCoursePrice().equals("0")) {
                            isSelectedCourseFree = true;
                        }
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
