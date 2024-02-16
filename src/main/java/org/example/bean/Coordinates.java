package org.example.bean;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Coordinates {
    private int x;
    private int y;
}
