package org.smartregister.chw.ovc_sample.activity;

import static org.smartregister.chw.ovc_sample.utils.JsonFormUtils.REQUEST_CODE_GET_JSON;

import android.app.Activity;
import android.content.Intent;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.Form;

import org.json.JSONObject;
import org.smartregister.chw.ovc.activity.BaseOvcProfileActivity;
import org.smartregister.chw.ovc.domain.MemberObject;
import org.smartregister.chw.ovc.util.Constants;
import org.smartregister.chw.ovc.util.OvcJsonFormUtils;
import org.smartregister.chw.ovc_sample.R;

import timber.log.Timber;


public class OvcHeadOfHouseholdProfileActivity extends BaseOvcProfileActivity {

    public static void startMe(Activity activity, String baseEntityID) {
        Intent intent = new Intent(activity, OvcHeadOfHouseholdProfileActivity.class);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID, baseEntityID);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE_GET_JSON);
    }

    @Override
    protected void onCreation() {
        super.onCreation();
        textViewRecordOvc.setText(R.string.record_mvc_household_services);
    }

    @Override
    public void recordOvc(MemberObject memberObject) {
        JSONObject jsonObject;
        try {
            jsonObject = OvcJsonFormUtils.getFormAsJson(Constants.FORMS.MVC_HOUSEHOLD_SERVICES);
            startFormActivity(jsonObject);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    @Override
    protected MemberObject getMemberObject(String baseEntityId) {
        return EntryActivity.getSampleHeadOfHouseholdMember();
    }


    public void startFormActivity(JSONObject jsonForm) {
        Form form = new Form();
        form.setWizard(false);

        Intent intent = new Intent(this, SampleJsonFormActivity.class);
        intent.putExtra(Constants.JSON_FORM_EXTRA.JSON, jsonForm.toString());
        intent.putExtra(JsonFormConstants.JSON_FORM_KEY.FORM, form);
        startActivityForResult(intent, REQUEST_CODE_GET_JSON);
    }
}