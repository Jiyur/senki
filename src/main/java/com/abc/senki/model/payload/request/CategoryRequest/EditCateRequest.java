package com.abc.senki.model.payload.request.CategoryRequest;


import lombok.*;

import java.util.UUID;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditCateRequest {
    private String name;
    private UUID parentCateId;
}
