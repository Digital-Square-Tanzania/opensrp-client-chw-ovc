package org.smartregister.chw.ovc_sample.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import androidx.appcompat.widget.Toolbar;

import org.smartregister.chw.ovc_sample.R;
import org.smartregister.chw.ovc.contract.BaseOvcVisitContract;
import org.smartregister.chw.ovc.domain.MemberObject;
import org.smartregister.view.activity.SecuredActivity;

import timber.log.Timber;

public class EntryActivity extends SecuredActivity implements View.OnClickListener, BaseOvcVisitContract.VisitView {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.ovc_activity).setOnClickListener(this);
        findViewById(R.id.mvc_head_of_household_profile_activity).setOnClickListener(this);
        findViewById(R.id.mvc_client_profile_activity).setOnClickListener(this);
    }

    @Override
    protected void onCreation() {
        Timber.v("onCreation");
    }

    @Override
    protected void onResumption() {
        Timber.v("onCreation");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ovc_activity:
                startActivity(new Intent(this, OvcRegisterActivity.class));
                break;
            case R.id.mvc_head_of_household_profile_activity:
                OvcHeadOfHouseholdProfileActivity.startMe(this, "12345");
                break;
            case R.id.mvc_client_profile_activity:
                OvcMemberProfileActivity.startMe(this, "12345");
                break;
            default:
                break;
        }
    }

    public static MemberObject getSampleMember() {
        MemberObject memberObject = new MemberObject();
        memberObject.setFirstName("Glory");
        memberObject.setLastName("Juma");
        memberObject.setMiddleName("Ali");
        memberObject.setGender("Female");
        memberObject.setDob("2023-01-18T03:00:00.000+03:00");
        memberObject.setUniqueId("3503504");
        memberObject.setBaseEntityId("3503504");
        memberObject.setFamilyBaseEntityId("3503504");
        memberObject.setAddress("Njiro");

        return memberObject;
    }

    public static MemberObject getSampleHeadOfHouseholdMember() {
        MemberObject memberObject = new MemberObject();
        memberObject.setFirstName("James");
        memberObject.setLastName("Juma");
        memberObject.setMiddleName("Kachinga");
        memberObject.setGender("Male");
        memberObject.setDob("1985-01-18T03:00:00.000+03:00");
        memberObject.setUniqueId("3503504");
        memberObject.setBaseEntityId("3503504");
        memberObject.setFamilyBaseEntityId("3503504");
        memberObject.setAddress("Njiro");

        return memberObject;
    }


    @Override
    public void onDialogOptionUpdated(String jsonString) {
        Timber.v("onDialogOptionUpdated %s", jsonString);
    }

    @Override
    public Context getMyContext() {
        return this;
    }
}