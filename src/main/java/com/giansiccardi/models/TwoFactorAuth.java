package com.giansiccardi.models;

import com.giansiccardi.enums.VerificationType;
import lombok.Data;

@Data
public class TwoFactorAuth {

private boolean isEnabled= false;
private VerificationType sendTo;
}
