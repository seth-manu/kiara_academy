package com.kiaraacademy.ui.chapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.kiaraacademy.R;
import com.kiaraacademy.data.models.Chapters;
import com.kiaraacademy.data.response.ResponseCourse;
import com.kiaraacademy.data.response.ResponseMainCourse;
import com.kiaraacademy.ui.FullscreenActivity;
import com.kiaraacademy.ui.PaymentActivity;
import com.kiaraacademy.utils.AppConstants;
import com.kiaraacademy.utils.AppUtils;
import com.kiaraacademy.utils.ConfimationListener;

import java.util.ArrayList;

public class ChapterwiseCourseActivity extends AppCompatActivity implements ChapterwiseView {

    ArrayList<Chapters> chaptersList = new ArrayList<>();
    private RecyclerView rcChapterwiseCourse;
    private AppCompatTextView bookTitle;
    private String from;
    private ResponseMainCourse course;
    private AppCompatButton btnBuy;
    private ChapterwisePresenter mPresenter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_chapterwise_course);

        initializeViews();
        getIntentData();
        attachPresenter();
        addListeners();
    }

    private void attachPresenter() {
        mPresenter = new ChapterwisePresenter();
        mPresenter.attachView(this);
    }

    private void addListeners() {
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.showConfirmationDialog("Are you sure to purchase this product ?", ChapterwiseCourseActivity.this, new ConfimationListener() {
                    @Override
                    public void onOkClick() {
                        Intent intent = new Intent(ChapterwiseCourseActivity.this, PaymentActivity.class);
                        intent.putExtra("course", course);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelCLick() {

                    }
                });
            }
        });
    }

    private void getIntentData() {
        Intent intent = getIntent();
        chaptersList = (ArrayList<Chapters>) intent.getSerializableExtra("chapters");
        course = (ResponseMainCourse) intent.getSerializableExtra("course");
        from = intent.getStringExtra("from");

        if (from != null) {
            if (from.equalsIgnoreCase("MyCourseFragment")) {
                btnBuy.setVisibility(View.GONE);
            }
        } else {
            btnBuy.setVisibility(View.VISIBLE);
        }

        if (chaptersList != null && !chaptersList.isEmpty()) {
            populateChaptersData(chaptersList);
        }

        bookTitle.setText(course.getCourse_name());
    }

    private void initializeViews() {
        bookTitle = findViewById(R.id.book_title);
        btnBuy = findViewById(R.id.btn_buy);
        rcChapterwiseCourse = findViewById(R.id.rv_chapterwise_course);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcChapterwiseCourse.setLayoutManager(linearLayoutManager);
    }

    private void populateChaptersData(ArrayList<Chapters> chaptersList) {
        ChapterwiseCourseAdapter myCourseAdapter = new ChapterwiseCourseAdapter(this, chaptersList);
        rcChapterwiseCourse.setAdapter(myCourseAdapter);
    }

    @Override
    public void onSuccess(Object object, Integer tag) {
        switch (tag) {
            case AppConstants.RequestConstants.REQUEST_PURCHASE_COURSE:
                if (object instanceof ResponseCourse) {
                    ResponseCourse responseCourse = (ResponseCourse) object;
                    if (responseCourse.code.equalsIgnoreCase(AppConstants.VolleyConstants.CODE_SUCCESS)) {
                        AppUtils.showToast(this, responseCourse.msg);
                    }
                }
                break;
            default:
                break;
        }
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

    public class ChapterwiseCourseAdapter extends RecyclerView.Adapter<ChapterwiseCourseAdapter.Holder> {
        private final ArrayList<Chapters> chapters;
        private Context context;

        public ChapterwiseCourseAdapter(Context context, ArrayList<Chapters> chapters) {
            this.context = context;
            this.chapters = chapters;
        }

        @NonNull
        @Override
        public ChapterwiseCourseAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            return new ChapterwiseCourseAdapter.Holder(LayoutInflater.from(context).inflate(R.layout.layout_chapterlist,
                    viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final ChapterwiseCourseAdapter.Holder viewHolder, final int position) {
            if (chapters != null && !chapters.isEmpty()) {
                final Chapters chapter = chapters.get(position);
                viewHolder.tvChapterName.setText(chapter.chapterName);

                if (chapter.chapterUrlVideo.contains(".pdf")) {
                    viewHolder.ibPlayVideo.setVisibility(View.GONE);
                }

                if (from == null) {
                    viewHolder.ibPlayVideo.setEnabled(false);
                }

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (chapter.isFree.equals("1")) {
                            if (chapter.chapterUrlVideo.contains(".pdf")) {
//                                Intent intent = new Intent(ChapterwiseCourseActivity.this, PdfViewActivity.class);
//                                intent.putExtra("pdf_url", chapter.chapterUrlVideo);
//                                startActivity(intent);

                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(chapter.chapterUrlVideo));
                                startActivity(i);
                            } else {
                                Intent intent = new Intent(ChapterwiseCourseActivity.this, FullscreenActivity.class);
                                intent.putExtra("video_url", chapter.chapterUrlVideo);
                                intent.putExtra("video_name", chapter.chapterName);
                                startActivity(intent);
                            }
                        } else {
                            AppUtils.showToast(ChapterwiseCourseActivity.this, "This is course is not for free.");
                        }
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return chapters.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            private TextView tvChapterNo;
            private TextView tvChapterName;
            private TextView tvChapterDesc;
            private AppCompatImageButton ibPlayVideo;

            Holder(@NonNull View itemView) {
                super(itemView);
                tvChapterName = itemView.findViewById(R.id.tv_chapter_name);
                ibPlayVideo = itemView.findViewById(R.id.ib_play_video);
            }
        }
    }
}
