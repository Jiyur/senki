package com.abc.senki.model.payload.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PickOrderResponse {
    private UUID orderId;
    private UUID sellerId;
    private UUID buyerId;
}
