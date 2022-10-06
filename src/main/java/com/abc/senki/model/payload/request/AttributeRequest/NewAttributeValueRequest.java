package com.abc.senki.model.payload.request.AttributeRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewAttributeValueRequest {
    private String value;
}
