package org.example.bean;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StructureBean {
    private String name;
    private CoortinateType type;
    private Coordinates coordinates;
    @Builder.Default private boolean summationInvolved = false;
    private List<StructureBean> nestedFields;
}
