package org.ei.drishti.view.matcher;

import android.widget.EditText;
import org.ei.drishti.domain.Alert;

public class MatchByBeneficiaryOrThaayiCard extends TextFieldMatcher {
    public MatchByBeneficiaryOrThaayiCard(EditText editText) {
        super(editText);
    }

    public boolean matches(Alert alert) {
        String currentValue = currentValue().toLowerCase();
        return (alert.beneficiaryName().toLowerCase().contains(currentValue) || alert.thaayiCardNo().toLowerCase().contains(currentValue));
    }
}
