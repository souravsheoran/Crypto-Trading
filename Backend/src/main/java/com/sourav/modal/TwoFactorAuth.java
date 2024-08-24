package com.sourav.modal;

import com.sourav.domain.VerificationType;
import lombok.Data;

@Data
public class TwoFactorAuth {
    private boolean isEnabled = false;
    //Enum
    private VerificationType sendTo;
}
