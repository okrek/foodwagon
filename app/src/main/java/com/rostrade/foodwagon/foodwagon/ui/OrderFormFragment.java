package com.rostrade.foodwagon.foodwagon.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.rostrade.foodwagon.foodwagon.R;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderFormFragment extends Fragment {

    private MaterialEditText mCustomerName;
    private MaterialEditText mCustomerLastname;
    private MaterialEditText mCustomerPhoneNumber;
    private MaterialEditText mCustomerStreet;
    private MaterialEditText mCustomerHouseNumber;
    private MaterialEditText mCustomerFlatNumber;
    private MaterialEditText mCustomerIntercom;
    private MaterialEditText mPersonsCount;
    private MaterialEditText mPromoCardNumber;
    private CheckBox mHasBirthday;

    public OrderFormFragment() {

    }

    public static OrderFormFragment newInstance() {
        return new OrderFormFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order_form, container, false);

        mCustomerName = (MaterialEditText) v.findViewById(R.id.customer_name);
        mCustomerLastname = (MaterialEditText) v.findViewById(R.id.customer_lastname);
        mCustomerPhoneNumber = (MaterialEditText) v.findViewById(R.id.customer_phone_number);
        mCustomerPhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        mCustomerStreet = (MaterialEditText) v.findViewById(R.id.customer_street);
        mCustomerHouseNumber = (MaterialEditText) v.findViewById(R.id.customer_house_number);
        mCustomerFlatNumber = (MaterialEditText) v.findViewById(R.id.customer_flat_number);
        mCustomerIntercom = (MaterialEditText) v.findViewById(R.id.customer_intercom);
        mPersonsCount = (MaterialEditText) v.findViewById(R.id.persons_count);
        mPromoCardNumber = (MaterialEditText) v.findViewById(R.id.promo_card_number);
        mHasBirthday = (CheckBox) v.findViewById(R.id.birthday);

        mCustomerName.setHintTextColor(getResources().getColor(R.color.hint_white_text));
        mCustomerLastname.setHintTextColor(getResources().getColor(R.color.hint_white_text));
        mCustomerPhoneNumber.setHintTextColor(getResources().getColor(R.color.hint_white_text));
        mCustomerStreet.setHintTextColor(getResources().getColor(R.color.hint_white_text));
        mCustomerHouseNumber.setHintTextColor(getResources().getColor(R.color.hint_white_text));
        mCustomerFlatNumber.setHintTextColor(getResources().getColor(R.color.hint_white_text));
        mCustomerIntercom.setHintTextColor(getResources().getColor(R.color.hint_white_text));
        mPersonsCount.setHintTextColor(getResources().getColor(R.color.hint_white_text));
        mPromoCardNumber.setHintTextColor(getResources().getColor(R.color.hint_white_text));
        mHasBirthday.setHintTextColor(getResources().getColor(R.color.hint_white_text));

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Button) getActivity().findViewById(R.id.button_process)).setText(R.string.card_order_finish);
        Button buttonBack = ((Button) getActivity().findViewById(R.id.button_back));
        buttonBack.setText(R.string.back);
        buttonBack.setClickable(true);
    }

    public String buildUserDataRequest() {
        JSONObject customerData = new JSONObject();
        JSONObject orderform = new JSONObject();

        try {
            orderform.put("user_flat", mCustomerFlatNumber.getText().toString());
            orderform.put("user_house", mCustomerHouseNumber.getText().toString());
            orderform.put("user_lastname", mCustomerLastname.getText().toString());
            orderform.put("user_name", mCustomerName.getText().toString());
            orderform.put("user_phone", mCustomerPhoneNumber.getText().toString());
            orderform.put("user_street", mCustomerStreet.getText().toString());
            orderform.put("user_flat", mCustomerFlatNumber.getText().toString());
            orderform.put("user_intercom", mCustomerIntercom.getText().toString());
            orderform.put("persons", mPersonsCount.getText().toString());
            orderform.put("card_number", mPromoCardNumber);
            orderform.put("birthday", mHasBirthday.isChecked());
            customerData.put("orderform", orderform);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return customerData.toString();
    }
}
