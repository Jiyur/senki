package com.abc.senki.model.payload.request.CategoryRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddNewCategoryRequest {
    private String name;
}
