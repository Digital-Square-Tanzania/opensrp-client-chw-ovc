package org.smartregister.chw.ovc.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.chw.ovc.R;
import org.smartregister.chw.ovc.contract.OvcProfileContract;
import org.smartregister.chw.ovc.custom_views.BaseOvcFloatingMenu;
import org.smartregister.chw.ovc.dao.OvcDao;
import org.smartregister.chw.ovc.domain.MemberObject;
import org.smartregister.chw.ovc.interactor.BaseOvcProfileInteractor;
import org.smartregister.chw.ovc.presenter.BaseOvcProfilePresenter;
import org.smartregister.chw.ovc.util.Constants;
import org.smartregister.chw.ovc.util.OvcUtil;
import org.smartregister.helper.ImageRenderHelper;
import org.smartregister.view.activity.BaseProfileActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;


public class BaseOvcProfileActivity extends BaseProfileActivity implements OvcProfileContract.View, OvcProfileContract.InteractorCallBack {
    protected MemberObject memberObject;
    protected OvcProfileContract.Presenter profilePresenter;
    protected CircleImageView imageView;
    protected TextView textViewName;
    protected TextView textViewGender;
    protected TextView textViewLocation;
    protected TextView textViewUniqueID;
    protected TextView textViewRecordOvc;
    protected View view_most_due_overdue_row;
    protected RelativeLayout rlLastVisit;
    protected RelativeLayout visitStatus;
    protected ImageView imageViewCross;
    protected TextView textViewUndo;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
    protected TextView textViewVisitDone;
    protected RelativeLayout visitDone;
    protected TextView textViewVisitDoneEdit;

    private ProgressBar progressBar;
    protected BaseOvcFloatingMenu baseOvcFloatingMenu;

    protected TextView manualProcessVisit;

    public static void startProfileActivity(Activity activity, String baseEntityId) {
        Intent intent = new Intent(activity, BaseOvcProfileActivity.class);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID, baseEntityId);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreation() {
        setContentView(R.layout.activity_ovc_profile);
        Toolbar toolbar = findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
        String baseEntityId = getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
            upArrow.setColorFilter(getResources().getColor(R.color.text_blue), PorterDuff.Mode.SRC_ATOP);
            actionBar.setHomeAsUpIndicator(upArrow);
        }

        toolbar.setNavigationOnClickListener(v -> BaseOvcProfileActivity.this.finish());
        appBarLayout = this.findViewById(R.id.collapsing_toolbar_appbarlayout);
        if (Build.VERSION.SDK_INT >= 21) {
            appBarLayout.setOutlineProvider(null);
        }

        textViewName = findViewById(R.id.textview_name);
        textViewGender = findViewById(R.id.textview_gender);
        textViewLocation = findViewById(R.id.textview_address);
        textViewUniqueID = findViewById(R.id.textview_id);
        view_most_due_overdue_row = findViewById(R.id.view_most_due_overdue_row);
        imageViewCross = findViewById(R.id.tick_image);
        rlLastVisit = findViewById(R.id.rlLastVisit);
        textViewVisitDone = findViewById(R.id.textview_visit_done);
        visitStatus = findViewById(R.id.record_visit_not_done_bar);
        visitDone = findViewById(R.id.visit_done_bar);
        progressBar = findViewById(R.id.progress_bar);
        textViewVisitDoneEdit = findViewById(R.id.textview_edit);
        textViewRecordOvc = findViewById(R.id.textview_record_ovc);
        textViewUndo = findViewById(R.id.textview_undo);
        imageView = findViewById(R.id.imageview_profile);
        manualProcessVisit = findViewById(R.id.textview_manual_process);

        textViewVisitDoneEdit.setOnClickListener(this);
        textViewVisitDoneEdit.setVisibility(View.GONE);

        textViewVisitDoneEdit.setOnClickListener(this);
        rlLastVisit.setOnClickListener(this);
        textViewRecordOvc.setOnClickListener(this);
        textViewUndo.setOnClickListener(this);

        imageRenderHelper = new ImageRenderHelper(this);
        memberObject = getMemberObject(baseEntityId);
        initializePresenter();
        profilePresenter.fillProfileData(memberObject);
        setupViews();
    }

    protected MemberObject getMemberObject(String baseEntityId) {
        return OvcDao.getMember(baseEntityId);
    }

    @Override
    protected void setupViews() {
        initializeFloatingMenu();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.title_layout) {
            onBackPressed();
        } else if (id == R.id.rlLastVisit) {
            this.openMedicalHistory();
        } else if (id == R.id.textview_record_ovc) {
            this.recordOvc(memberObject);
        }
    }

    @Override
    protected void initializePresenter() {
        showProgressBar(true);
        profilePresenter = new BaseOvcProfilePresenter(this, new BaseOvcProfileInteractor(), memberObject);
        fetchProfileData();
        profilePresenter.refreshProfileBottom();
    }

    public void initializeFloatingMenu() {
        if (StringUtils.isNotBlank(memberObject.getPhoneNumber())) {
            baseOvcFloatingMenu = new BaseOvcFloatingMenu(this, memberObject);
            baseOvcFloatingMenu.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
            LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            addContentView(baseOvcFloatingMenu, linearLayoutParams);
        }
    }

    @Override
    public void hideView() {
        textViewRecordOvc.setVisibility(View.GONE);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void setProfileViewWithData() {
        textViewName.setText(String.format("%s %s %s, %d", memberObject.getFirstName(), memberObject.getMiddleName(), memberObject.getLastName(), memberObject.getAge()));
        textViewGender.setText(OvcUtil.getGenderTranslated(this, memberObject.getGender()));
        textViewLocation.setText(memberObject.getAddress());
        textViewUniqueID.setText(memberObject.getUniqueId());
    }

    @Override
    public void setOverDueColor() {
        textViewRecordOvc.setBackground(getResources().getDrawable(R.drawable.record_btn_selector_overdue));
    }

    @Override
    protected ViewPager setupViewPager(ViewPager viewPager) {
        return null;
    }

    @Override
    protected void fetchProfileData() {
        //fetch profile data
    }

    @Override
    public void showProgressBar(boolean status) {
        progressBar.setVisibility(status ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void refreshMedicalHistory(boolean hasHistory) {
        showProgressBar(false);
        rlLastVisit.setVisibility(hasHistory ? View.VISIBLE : View.GONE);
    }


    @Override
    public void openMedicalHistory() {
        //implement
    }

    @Override
    public void recordOvc(MemberObject memberObject) {
        //implement
    }

    @Nullable
    private String formatTime(Date dateTime) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
            return formatter.format(dateTime);
        } catch (Exception e) {
            Timber.d(e);
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_GET_JSON && resultCode == RESULT_OK) {
            profilePresenter.saveForm(data.getStringExtra(Constants.JSON_FORM_EXTRA.JSON));
        }
    }
}
