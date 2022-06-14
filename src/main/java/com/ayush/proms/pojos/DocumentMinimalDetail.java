package com.ayush.proms.pojos;

import com.ayush.proms.enums.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentMinimalDetail {
    private Long id;
    private DocumentType documentType;
}
