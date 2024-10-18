package org.smartregister.chw.ovc.custom_views;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.smartregister.chw.ovc.R;
import org.smartregister.chw.ovc.domain.MemberObject;
import org.smartregister.chw.ovc.fragment.BaseOvcCallDialogFragment;

public class BaseOvcFloatingMenu extends LinearLayout implements View.OnClickListener {
    private MemberObject MEMBER_OBJECT;

    public BaseOvcFloatingMenu(Context context, MemberObject MEMBER_OBJECT) {
        super(context);
        initUi();
        this.MEMBER_OBJECT = MEMBER_OBJECT;
    }

    protected void initUi() {
        inflate(getContext(), R.layout.view_ovc_floating_menu, this);
        FloatingActionButton fab = findViewById(R.id.ovc_fab);
        if (fab != null)
            fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ovc_fab) {
            Activity activity = (Activity) getContext();
            BaseOvcCallDialogFragment.launchDialog(activity, MEMBER_OBJECT);
        }  else if (view.getId() == R.id.ovc_refer_to_facility_layout) {
            Activity activity = (Activity) getContext();
            BaseOvcCallDialogFragment.launchDialog(activity, MEMBER_OBJECT);
        }
    }
}