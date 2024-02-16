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
    private List<StructureBean> nestedFields;
}
