package projetxml.equipsync.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Affect_req {
    String userId;
    String equipmentId;
}
