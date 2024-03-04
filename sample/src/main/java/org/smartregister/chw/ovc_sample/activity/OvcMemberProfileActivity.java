package org.smartregister.chw.ovc_sample.activity;

import android.app.Activity;
import android.content.Intent;

import org.smartregister.chw.ovc.activity.BaseOvcProfileActivity;
import org.smartregister.chw.ovc.domain.MemberObject;
import org.smartregister.chw.ovc.util.Constants;


public class OvcMemberProfileActivity extends BaseOvcProfileActivity {

    public static void startMe(Activity activity, String baseEntityID) {
        Intent intent = new Intent(activity, OvcMemberProfileActivity.class);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID, baseEntityID);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE_GET_JSON);
    }

    @Override
    public void recordGbv(MemberObject memberObject) {
        OvcHfVisitActivity.startMe(this, memberObject.getBaseEntityId(), false);
    }

    @Override
    protected MemberObject getMemberObject(String baseEntityId) {
        return EntryActivity.getSampleMember();
    }
}