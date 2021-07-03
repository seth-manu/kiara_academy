package com.kiaraacademy.ui.dashboard.fragment.home;

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
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.kiaraacademy.R;
import com.kiaraacademy.data.SharedPreferenceManager;
import com.kiaraacademy.data.models.Chapters;
import com.kiaraacademy.data.response.BaseResponse;
import com.kiaraacademy.data.response.ResponseChapterwiseCourse;
import com.kiaraacademy.data.response.ResponseCourse;
import com.kiaraacademy.data.response.ResponseMainCourse;
import com.kiaraacademy.ui.PaymentActivity;
import com.kiaraacademy.ui.allcourses.AllCourseActivity;
import com.kiaraacademy.ui.chapters.ChapterwiseCourseActivity;
import com.kiaraacademy.ui.dashboard.DashboardPresenter;
import com.kiaraacademy.ui.dashboard.DashboardView;
import com.kiaraacademy.ui.dashboard.fragment.settings.SettingFragment;
import com.kiaraacademy.utils.AppConstants;
import com.kiaraacademy.utils.AppUtils;
import com.kiaraacademy.utils.ConfimationListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements DashboardView {

    private RecyclerView rv_most_viewed;
    private CircleImageView profileImage;
    private RecyclerView rv_top_recommedation;
    private ProgressDialog progressDialog;
    private DashboardPresenter mPresenter;
    private AppCompatTextView tv_name;
    private AppCompatImageButton searchView;
    private AppCompatTextView tvSeeMore;
    private String courseName = null, courseId = null;
    private ResponseMainCourse course;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initComponents(view);
        attachPresenter();
        addListener();
        getAllCourses();
        return view;
    }

    private void addListener() {
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SettingFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.rl_fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        tvSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AllCourseActivity.class);
                startActivity(intent);
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AllCourseActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getAllCourses() {
        mPresenter.getAllCourses();
    }

    private void initComponents(View view) {

        profileImage = view.findViewById(R.id.profile_image);

        rv_top_recommedation = view.findViewById(R.id.rv_top_recommedation);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv_top_recommedation.setLayoutManager(linearLayoutManager);

        rv_most_viewed = view.findViewById(R.id.rv_most_viewed);
        LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        rv_most_viewed.setLayoutManager(layoutManager);

        tv_name = view.findViewById(R.id.tv_name);
        tv_name.setText("Hey, " + SharedPreferenceManager.getUserName());
        tvSeeMore = view.findViewById(R.id.tv_see_more);
        searchView = view.findViewById(R.id.search_view);
    }


    private void attachPresenter() {
        mPresenter = new DashboardPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onSuccess(Object object, Integer tag) {
        switch (tag) {
            case AppConstants.RequestConstants.REQUEST_PURCHASE_COURSE:
                if (object instanceof ResponseCourse) {
                    ResponseCourse responseCourse = (ResponseCourse) object;
                    if (responseCourse.code.equalsIgnoreCase(AppConstants.VolleyConstants.CODE_SUCCESS)) {
                        AppUtils.showToast(getActivity(), responseCourse.msg);
                    }
                }
                break;
            case AppConstants.RequestConstants.REQUEST_SAVE_UNSAVE_COURSE:
                if (object instanceof BaseResponse) {
                    BaseResponse responseCourse = (BaseResponse) object;
                    if (responseCourse.code.equalsIgnoreCase(AppConstants.VolleyConstants.CODE_SUCCESS)) {
                        AppUtils.showToast(getActivity(), responseCourse.msg);
                    }
                }
                break;
            case AppConstants.RequestConstants.REQUEST_GET_ALL_COURSE:
                if (object instanceof ResponseCourse) {
                    ResponseCourse responseCourse = (ResponseCourse) object;
                    if (responseCourse.code.equalsIgnoreCase(AppConstants.VolleyConstants.CODE_SUCCESS)) {
                        populateCourseData(responseCourse.getResponse());
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
        getActivity().startActivity(intent);
    }

    private void populateCourseData(ArrayList<ResponseMainCourse> response) {
        RecommendedCourseAdapter recommendedCourseAdapter = new RecommendedCourseAdapter(getActivity(), response);
        rv_top_recommedation.setAdapter(recommendedCourseAdapter);

        MostVisitedCourseAdapter mostVisitedCourseAdapter = new MostVisitedCourseAdapter(getActivity(), response);
        rv_most_viewed.setAdapter(mostVisitedCourseAdapter);
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
        AppUtils.showAlertToEnableMobileData(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    public class RecommendedCourseAdapter extends RecyclerView.Adapter<RecommendedCourseAdapter.Holder> {
        private final ArrayList<ResponseMainCourse> mainCourses;
        private Context context;

        public RecommendedCourseAdapter(Context context, ArrayList<ResponseMainCourse> mainCourses) {
            this.context = context;
            this.mainCourses = mainCourses;
        }

        @NonNull
        @Override
        public RecommendedCourseAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            return new RecommendedCourseAdapter.Holder(LayoutInflater.from(context).inflate(R.layout.layout_top_recommendation,
                    viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final RecommendedCourseAdapter.Holder viewHolder, final int position) {
            if (mainCourses != null && !mainCourses.isEmpty()) {
                final ResponseMainCourse mainCourse = mainCourses.get(position);
                viewHolder.tvCourseName.setText(mainCourse.getCourse_name());
                viewHolder.tvAuthorName.setText(mainCourse.getAuthor());
                if (mainCourse.getCoursePrice().equals("0"))
                    viewHolder.btnBuy.setText("Free");

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, AllCourseActivity.class);
                        startActivity(intent);
                    }
                });

                viewHolder.btnBuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (mainCourse.getCoursePrice().equals("0")) {
//                            course = mainCourse;
//                            mPresenter.getChapterwiseCourse(course.getId());
                            Intent intent = new Intent(context, AllCourseActivity.class);
                            startActivity(intent);
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

                viewHolder.ibAddToCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AppUtils.showConfirmationDialog("Are you sure to add this product in cart ?", getActivity(), new ConfimationListener() {
                            @Override
                            public void onOkClick() {
                                mPresenter.saveUnsaveCourses(AppConstants.MainConstants.IS_SAVE, mainCourse.getId(), SharedPreferenceManager.getUserId());
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

        class Holder extends RecyclerView.ViewHolder {
            private TextView tvCourseName;
            private TextView tvCourseDesc;
            private TextView tvAuthorName;
            private ImageButton ibAddToCart;
            private AppCompatTextView btnBuy;
            private AppCompatTextView tvTitleName;

            Holder(@NonNull View itemView) {
                super(itemView);
                tvCourseName = itemView.findViewById(R.id.tv_course_name);
                tvAuthorName = itemView.findViewById(R.id.tv_author_name);
                btnBuy = itemView.findViewById(R.id.btn_buy);
                ibAddToCart = itemView.findViewById(R.id.ib_add_to_cart);
            }
        }
    }

    public class MostVisitedCourseAdapter extends RecyclerView.Adapter<MostVisitedCourseAdapter.Holder> {
        private final ArrayList<ResponseMainCourse> mainCourses;
        private Context context;

        public MostVisitedCourseAdapter(Context context, ArrayList<ResponseMainCourse> mainCourses) {
            this.context = context;
            this.mainCourses = mainCourses;
        }

        @NonNull
        @Override
        public MostVisitedCourseAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            return new MostVisitedCourseAdapter.Holder(LayoutInflater.from(context).inflate(R.layout.layout_most_visited,
                    viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final MostVisitedCourseAdapter.Holder viewHolder, final int position) {
            if (mainCourses != null && !mainCourses.isEmpty()) {
                final ResponseMainCourse course = mainCourses.get(position);

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, AllCourseActivity.class);
                        startActivity(intent);
                    }
                });

                viewHolder.tvCourseName.setText(course.getCourse_name());
            }
        }

        @Override
        public int getItemCount() {
            return mainCourses.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            private TextView tvCourseName;

            Holder(@NonNull View itemView) {
                super(itemView);
                tvCourseName = itemView.findViewById(R.id.tv_course_name);
            }
        }
    }
}
