package com.kiaraacademy.ui.dashboard.fragment.video;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.ct7ct7ct7.androidvimeoplayer.view.VimeoPlayerView;
import com.kiaraacademy.R;
import com.kiaraacademy.data.SharedPreferenceManager;
import com.kiaraacademy.data.response.ResponseCourse;
import com.kiaraacademy.data.response.ResponseMainCourse;
import com.kiaraacademy.ui.FullscreenActivity;
import com.kiaraacademy.ui.PdfViewActivity;
import com.kiaraacademy.ui.dashboard.DashboardPresenter;
import com.kiaraacademy.ui.dashboard.DashboardView;
import com.kiaraacademy.utils.AppConstants;
import com.kiaraacademy.utils.AppUtils;
import com.kiaraacademy.utils.ConfimationListener;

import java.util.ArrayList;

public class VideoFragment extends Fragment implements DashboardView {

    private RecyclerView rcMyCourse;
    private ProgressDialog progressDialog;
    private DashboardPresenter mPresenter;

    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        initializeViews(view);
        attachPresenter();
        //getAllCourses();

        return view;
    }

    private void getAllCourses() {
        mPresenter.getAllCourses();
    }


    private void initializeViews(View view) {
        rcMyCourse = view.findViewById(R.id.rv_my_course);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rcMyCourse.setLayoutManager(linearLayoutManager);
    }

    private void attachPresenter() {
        mPresenter = new DashboardPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onSuccess(Object object, Integer tag) {
        if (tag == AppConstants.RequestConstants.REQUEST_GET_ALL_COURSE) {
            if (object instanceof ResponseCourse) {
                ResponseCourse responseCourse = (ResponseCourse) object;
                if (responseCourse.code.equalsIgnoreCase(AppConstants.VolleyConstants.CODE_SUCCESS)) {
                    populateCourseData(responseCourse.getResponse());
                }
            }
        }
    }

    private void populateCourseData(ArrayList<ResponseMainCourse> response) {
        VideoAdapter adapter = new VideoAdapter(getActivity(), response);
        rcMyCourse.setAdapter(adapter);
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

    public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final ArrayList<ResponseMainCourse> mainCourses;
        private Context context;
        private int TYPE_VIDEO = 1;
        private int TYPE_PDF = 2;

        public VideoAdapter(Context context, ArrayList<ResponseMainCourse> mainCourses) {
            this.context = context;
            this.mainCourses = mainCourses;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view;
            if (viewType == TYPE_VIDEO) {
                view = LayoutInflater.from(context).inflate(R.layout.layout_videos, viewGroup, false);
                return new VideoViewHolder(view);
            } else {
                view = LayoutInflater.from(context).inflate(R.layout.layout_pdf, viewGroup, false);
                return new PDFViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
            if (mainCourses != null && !mainCourses.isEmpty()) {
                final ResponseMainCourse course = mainCourses.get(position);
                if (getItemViewType(position) == TYPE_VIDEO) {
                    ((VideoViewHolder) viewHolder).tvCourseName.setText(course.getCourse_name());
                    ((VideoViewHolder) viewHolder).vimeoPlayer.initialize(Integer.parseInt(course.getCourse_url_video().split("https://vimeo.com/")[1]), course.getCourse_url_video());
                    ((VideoViewHolder) viewHolder).vimeoPlayer.setFullscreenVisibility(true);

                    ((VideoViewHolder) viewHolder).vimeoPlayer.setFullscreenClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, FullscreenActivity.class);
                            intent.putExtra("video_url", course.getCourse_url_video());
                            intent.putExtra("video_name", course.getCourse_name());
                            context.startActivity(intent);
                        }
                    });

                    ((VideoViewHolder) viewHolder).btnAddToCart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            AppUtils.showConfirmationDialog("Are you sure to add this product in cart ?", getActivity(), new ConfimationListener() {
                                @Override
                                public void onOkClick() {
                                    ((VideoViewHolder) viewHolder).btnAddToCart.setBackground(getResources().getDrawable(R.drawable.ic_heart_red_fill));
                                    mPresenter.saveUnsaveCourses(AppConstants.MainConstants.IS_SAVE, course.getId(), SharedPreferenceManager.getUserId());
                                }

                                @Override
                                public void onCancelCLick() {

                                }
                            });
                        }
                    });
                } else if (getItemViewType(position) == TYPE_PDF) {
                    ((PDFViewHolder) viewHolder).tvCourseName.setText(course.getCourse_name());
                    ((PDFViewHolder) viewHolder).tvCourseName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, PdfViewActivity.class);
                            intent.putExtra("pdf_url", course.getCourse_url_video());
                            context.startActivity(intent);
                        }
                    });
                }
            }
        }

        @Override
        public int getItemViewType(int position) {
            int viewType = 0;
            if (mainCourses != null && mainCourses.size() > 0) {
                if (mainCourses.get(position).getCourse_url_video() != null) {
                    if (mainCourses.get(position).getCourse_url_video().contains(".pdf")) {
                        viewType = TYPE_PDF;
                    } else {
                        viewType = TYPE_VIDEO;
                    }
                }
            }
            return viewType;
        }

        @Override
        public int getItemCount() {
            return mainCourses.size();
        }

        class VideoViewHolder extends RecyclerView.ViewHolder {
            private VimeoPlayerView vimeoPlayer;
            private AppCompatTextView tvCourseName;
            private AppCompatImageButton btnAddToCart;

            VideoViewHolder(@NonNull View itemView) {
                super(itemView);
                vimeoPlayer = itemView.findViewById(R.id.vimeoPlayer);
                tvCourseName = itemView.findViewById(R.id.tv_course_name);
                btnAddToCart = itemView.findViewById(R.id.btn_add_to_cart);
            }
        }

        class PDFViewHolder extends RecyclerView.ViewHolder {
            private AppCompatButton btnViewPdf;
            private AppCompatTextView tvCourseName;

            PDFViewHolder(@NonNull View itemView) {
                super(itemView);
                btnViewPdf = itemView.findViewById(R.id.btn_view_pdf);
                tvCourseName = itemView.findViewById(R.id.tv_course_name);
            }
        }
    }
}