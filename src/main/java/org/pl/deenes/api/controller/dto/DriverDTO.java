package org.pl.deenes.api.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverDTO {

    Integer id;
    String name;
    String surname;
    Integer pesel;
/*    Set<LocomotiveType> locomotiveAuthorization;
    Set<Line> lineAuthorization;
    Set<Train> trains;*/
}
