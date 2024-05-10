package org.smartregister.chw.ovc.actionhelper;

import static org.smartregister.client.utils.constants.JsonFormConstants.JSON_FORM_KEY.GLOBAL;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.ovc.domain.MemberObject;
import org.smartregister.chw.ovc.domain.VisitDetail;
import org.smartregister.chw.ovc.model.BaseOvcVisitAction;
import org.smartregister.chw.ovc.util.JsonFormUtils;

import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class MvcEducationAndPsychosocialSupportActionHelper extends OvcVisitActionHelper {
    private String selectEducationVocationalTraining;
    private String selectEcdPsychosocialSupport;

    private MemberObject memberObject;

    private String jsonString;

    public MvcEducationAndPsychosocialSupportActionHelper(MemberObject memberObject) {
        this.memberObject = memberObject;
    }

    @Override
    public void onJsonFormLoaded(String jsonString, Context context, Map<String, List<VisitDetail>> details) {
        this.jsonString = jsonString;
        super.onJsonFormLoaded(jsonString, context, details);
    }

    @Override
    public String getPreProcessed() {
        try {
            JSONObject form = new JSONObject(jsonString);
            form.getJSONObject(GLOBAL).put("age", memberObject.getAge());
            return form.toString();
        } catch (Exception e) {
            Timber.e(e);
        }
        return null;
    }

    @Override
    public void onPayloadReceived(String jsonPayload) {
        JSONObject payload;
        try {
            payload = new JSONObject(jsonPayload);
            selectEducationVocationalTraining = JsonFormUtils.getValue(payload, "select_education_vocational_training");
            selectEcdPsychosocialSupport = JsonFormUtils.getCheckBoxValue(payload, "select_ecd_psychosocial_support");
        } catch (JSONException e) {
            Timber.d(e);
        }
    }

    @Override
    public String evaluateSubTitle() {
        if (selectEcdPsychosocialSupport != null) {
            return null;
        } else {
            return null;
        }
    }

    @Override
    public BaseOvcVisitAction.Status evaluateStatusOnPayload() {
        if (selectEcdPsychosocialSupport != null) {
            return BaseOvcVisitAction.Status.COMPLETED;
        } else {
            return BaseOvcVisitAction.Status.PENDING;
        }
    }
}
