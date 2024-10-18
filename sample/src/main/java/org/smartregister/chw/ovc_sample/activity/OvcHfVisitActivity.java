package org.smartregister.chw.ovc_sample.activity;

import android.app.Activity;
import android.content.Intent;

import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.json.JSONObject;
import org.smartregister.chw.ovc.activity.BaseOvcChildVisitActivity;
import org.smartregister.chw.ovc.domain.MemberObject;
import org.smartregister.chw.ovc.presenter.BaseOvcVisitPresenter;
import org.smartregister.chw.ovc.util.Constants;
import org.smartregister.chw.ovc_sample.interactor.OvcVisitInteractor;

public class OvcHfVisitActivity extends BaseOvcChildVisitActivity {
    public static void startMe(Activity activity, String baseEntityID, Boolean isEditMode) {
        Intent intent = new Intent(activity, OvcHfVisitActivity.class);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID, baseEntityID);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.EDIT_MODE, isEditMode);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE_GET_JSON);
    }

    @Override
    protected MemberObject getMemberObject(String baseEntityId) {
        return EntryActivity.getSampleMember();
    }

    protected void registerPresenter() {
        presenter = new BaseOvcVisitPresenter(memberObject, this, new OvcVisitInteractor());
    }

    @Override
    public void startFormActivity(JSONObject jsonForm) {
        Intent intent = new Intent(this, SampleJsonFormActivity.class);
        intent.putExtra(Constants.JSON_FORM_EXTRA.JSON, jsonForm.toString());

        if (getFormConfig(jsonForm) != null) {
            intent.putExtra(JsonFormConstants.JSON_FORM_KEY.FORM, getFormConfig(jsonForm));
        }

        startActivityForResult(intent, Constants.REQUEST_CODE_GET_JSON);
    }
}
