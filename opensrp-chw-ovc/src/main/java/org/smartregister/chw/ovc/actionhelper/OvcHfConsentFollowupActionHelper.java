package org.smartregister.chw.ovc.actionhelper;

import static org.smartregister.client.utils.constants.JsonFormConstants.GLOBAL;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.ovc.domain.MemberObject;
import org.smartregister.chw.ovc.domain.VisitDetail;
import org.smartregister.chw.ovc.model.BaseOvcVisitAction;
import org.smartregister.chw.ovc.util.JsonFormUtils;

import java.util.List;
import java.util.Map;

import timber.log.Timber;

public abstract class OvcHfConsentFollowupActionHelper extends OvcVisitActionHelper {
    private final MemberObject memberObject;

    private String clientConsentAfterCounseling;

    private String wasSocialWelfareOfficerInvolved;

    private JSONObject jsonForm;


    public OvcHfConsentFollowupActionHelper(MemberObject memberObject) {
        this.memberObject = memberObject;
    }

    @Override
    public void onJsonFormLoaded(String jsonString, Context context, Map<String, List<VisitDetail>> details) {
        super.onJsonFormLoaded(jsonString, context, details);
        try {
            jsonForm = new JSONObject(jsonString);
        } catch (JSONException e) {
            Timber.e(e);
        }
    }

    /**
     * Update the preprocessed form to pass client age as global parameter
     *
     * @return the updated form
     */
    @Override
    public String getPreProcessed() {
        try {
            JSONObject global = jsonForm.getJSONObject(GLOBAL);
            global.put("age", memberObject.getAge());
            return jsonForm.toString();
        } catch (JSONException e) {
            Timber.e(e);
        }
        return null;
    }

    @Override
    public void onPayloadReceived(String jsonPayload) {
        JSONObject payload;
        try {
            payload = new JSONObject(jsonPayload);
            clientConsentAfterCounseling = JsonFormUtils.getValue(payload, "client_consent_after_counseling");
            wasSocialWelfareOfficerInvolved = JsonFormUtils.getValue(payload, "was_social_welfare_officer_involved");
        } catch (JSONException e) {
            Timber.d(e);
        }
        processConsentFollowup(clientConsentAfterCounseling, wasSocialWelfareOfficerInvolved);
    }

    public abstract void processConsentFollowup(String clientConsentAfterCounseling, String wasSocialWelfareOfficerInvolved);

    @Override
    public String evaluateSubTitle() {
        if (StringUtils.isNotBlank(clientConsentAfterCounseling)) {
            return "Was Consent Provided After Counselling: " + clientConsentAfterCounseling;
        } else if (StringUtils.isNotBlank(wasSocialWelfareOfficerInvolved)) {
            return "Was Social Welfare Officer Involved: " + wasSocialWelfareOfficerInvolved;
        }

        return null;
    }

    @Override
    public BaseOvcVisitAction.Status evaluateStatusOnPayload() {
        if (StringUtils.isNotBlank(clientConsentAfterCounseling) || StringUtils.isNotBlank(wasSocialWelfareOfficerInvolved)) {
            return BaseOvcVisitAction.Status.COMPLETED;
        } else return BaseOvcVisitAction.Status.PENDING;
    }
}
