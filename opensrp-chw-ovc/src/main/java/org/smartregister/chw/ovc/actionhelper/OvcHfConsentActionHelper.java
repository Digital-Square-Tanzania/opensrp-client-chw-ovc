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

public abstract class OvcHfConsentActionHelper extends OvcVisitActionHelper {
    private MemberObject memberObject;

    private String clientConsent;

    private JSONObject jsonForm;


    public OvcHfConsentActionHelper(MemberObject memberObject) {
        this.memberObject = memberObject;
    }

    @Override
    public void onJsonFormLoaded(String jsonString, Context context, Map<String, List<VisitDetail>> details) {
        super.onJsonFormLoaded(jsonString, context, details);
        try {
            jsonForm = new JSONObject(jsonString);
        } catch (Exception e) {
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
            clientConsent = JsonFormUtils.getValue(payload, "client_consent");
        } catch (JSONException e) {
            Timber.d(e);
        }
        processClientConsent(clientConsent);
    }

    public abstract void processClientConsent(String clientConsent);

    @Override
    public String evaluateSubTitle() {
        if (StringUtils.isNotBlank(clientConsent)) {
            return "Was Consent Provided: " + clientConsent;
        }

        return null;
    }

    @Override
    public BaseOvcVisitAction.Status evaluateStatusOnPayload() {
        if (StringUtils.isNotBlank(clientConsent)) {
            return BaseOvcVisitAction.Status.COMPLETED;
        } else
            return BaseOvcVisitAction.Status.PENDING;
    }

    public void setMemberObject(MemberObject memberObject) {
        this.memberObject = memberObject;
    }

    public String getClientConsent() {
        return clientConsent;
    }

    public void setClientConsent(String clientConsent) {
        this.clientConsent = clientConsent;
    }
}
